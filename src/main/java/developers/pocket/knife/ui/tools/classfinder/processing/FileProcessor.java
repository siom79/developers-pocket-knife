package developers.pocket.knife.ui.tools.classfinder.processing;

import com.google.common.base.Optional;
import developers.pocket.knife.ui.tools.classfinder.SearchCriteria;
import developers.pocket.knife.ui.tools.classfinder.SearchResult;
import developers.pocket.knife.ui.tools.classfinder.SearchResultAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class FileProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessor.class);
    private static final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private final ConcurrentLinkedQueue<SearchResultAsync> queue = new ConcurrentLinkedQueue<>();
    private final Map<Long,Long> submittedTaskIds = new HashMap<>();
    private long id = 0L;

    public Optional<List<SearchResult>> processPath(Path path, SearchCriteria searchCriteria) {
        if (isClassFile(path)) {
            return processClassFile(path, searchCriteria);
        } else if (isZipFile(path)) {
            return processZipFile(path, searchCriteria);
        }
        return Optional.absent();
    }

    public Optional<List<SearchResult>> getNextAsyncResults() {
        List<SearchResult> searchResults = null;
        SearchResultAsync resultAsync = this.queue.poll();
        while(resultAsync != null) {
            this.submittedTaskIds.remove(resultAsync.getId());
            List<SearchResult> searchResultList = resultAsync.getSearchResultList();
            if(searchResultList.size() > 0) {
                if(searchResults == null) {
                    searchResults = new LinkedList<>();
                }
                searchResults.addAll(searchResultList);
            }
            resultAsync = this.queue.poll();
        }
        if(searchResults != null) {
            return Optional.of(searchResults);
        }
        return Optional.absent();
    }

    public boolean hasMoreAsyncResults() {
        return this.submittedTaskIds.size() > 0;
    }

    private static boolean isClassFile(Path path) {
        return path.getFileName().toString().endsWith(".class");
    }

    private Optional<List<SearchResult>> processClassFile(Path path, SearchCriteria searchCriteria) {
        String fileName = path.getFileName().toString();
        LOGGER.debug("Processing class file " + fileName);
        if (searchCriteria.matches(fileName)) {
            List<SearchResult> searchResults = new ArrayList<>(1);
            SearchResult searchResult = new SearchResult(path.toString());
            searchResults.add(searchResult);
            return Optional.of(searchResults);
        }
        return Optional.absent();
    }

    private static class ZipFileProcessor implements Runnable {
        private Path path;
        private SearchCriteria searchCriteria;
        private ConcurrentLinkedQueue<SearchResultAsync> queue;
        private long id;

        public ZipFileProcessor(Path path, SearchCriteria searchCriteria, ConcurrentLinkedQueue<SearchResultAsync> queue, long id) {
            this.path = path;
            this.searchCriteria = searchCriteria;
            this.queue = queue;
            this.id = id;
        }

        @Override
        public void run() {
            List<SearchResult> searchResults = new LinkedList<>();
            try (ZipFile zipFile = new ZipFile(path.toFile())) {
                LOGGER.debug("Processing zip file " + path);
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry zipEntry = entries.nextElement();
                    Stack<String> zipFileStack = new Stack();
                    processZipEntry(path, searchCriteria, searchResults, zipFile, zipEntry, zipFileStack);
                }
            } catch (Exception e) {
                LOGGER.error("Processing zip file {} failed.", path);
            } finally {
                SearchResultAsync resultAsync = new SearchResultAsync(id);
                resultAsync.getSearchResultList().addAll(searchResults);
                this.queue.add(resultAsync);
            }
        }

        private void processZipEntry(Path path, SearchCriteria searchCriteria, List<SearchResult> searchResults, ZipFile zipFile, ZipEntry zipEntry, Stack zipFileStack) {
            if (!zipEntry.isDirectory()) {
                String zipEntryName = zipEntry.getName();
                LOGGER.debug("Processing zip file entry " + zipEntryName);
                String filename = filenameFromZipEntryName(zipEntryName);
                if (searchCriteria.matches(filename)) {
                    String location = path.toString();
                    SearchResult searchResult = new SearchResult(location);
                    String zipFilePath = constructZipFilePath(zipFileStack, zipEntryName);
                    searchResult.setZipFilePath(zipFilePath);
                    searchResults.add(searchResult);
                } else {
                    try (ZipInputStream zipInputStream = new ZipInputStream(zipFile.getInputStream(zipEntry))) {
                        ZipEntry nextEntry = zipInputStream.getNextEntry();
                        while (nextEntry != null) {
                            zipFileStack.push(zipEntry.getName());
                            processZipEntry(path, searchCriteria, searchResults, zipFile, nextEntry, zipFileStack);
                            zipFileStack.pop();
                            nextEntry = zipInputStream.getNextEntry();
                        }
                    } catch (Exception e) {
                        LOGGER.debug(String.format("Failed to open zip file entry %s as ZipInputStream: %s", zipEntryName, e.getMessage()));
                    }
                }
            }
        }

        private String constructZipFilePath(Stack<String> zipFileStack, String zipEntryName) {
            StringBuilder sb = new StringBuilder();
            for (String zipFileStackEntry : zipFileStack) {
                appendExclamationMark(sb);
                sb.append(zipFileStackEntry);
            }
            appendExclamationMark(sb);
            sb.append(zipEntryName);
            return sb.toString();
        }

        private void appendExclamationMark(StringBuilder sb) {
            if (sb.length() > 0) {
                sb.append("!");
            }
        }

        private String filenameFromZipEntryName(String name) {
            String[] strings = name.split("/");
            if (strings != null && strings.length > 0) {
                return strings[strings.length - 1];
            }
            return name;
        }
    }

    private synchronized long getId() {
        return this.id++;
    }

    private Optional<List<SearchResult>> processZipFile(Path path, SearchCriteria searchCriteria) {
        long newId = getId();
        executorService.submit(new ZipFileProcessor(path, searchCriteria, this.queue, newId));
        submittedTaskIds.put(newId, newId);
        return Optional.absent();
    }

    private boolean isZipFile(Path path) {
        boolean isZipFile = false;
        try (RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r")) {
            int readInt = raf.readInt();
            isZipFile = isZipMagicNumber(readInt);
        } catch (IOException e) {
            isZipFile = false;
        }
        return isZipFile;
    }

    private boolean isZipMagicNumber(int readInt) {
        boolean isMagicNumber = false;
        if (readInt == 0x504B0304) {
            isMagicNumber = true;
        }
        return isMagicNumber;
    }
}
