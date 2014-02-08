package developers.pocket.knife.ui.tools.classfinder.processing;

import com.google.common.base.Optional;
import developers.pocket.knife.ui.tools.classfinder.SearchCriteria;
import developers.pocket.knife.ui.tools.classfinder.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class FileProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessor.class);

    public static Optional<List<SearchResult>> processPath(Path path, SearchCriteria searchCriteria) {
        if (isClassFile(path)) {
            return processClassFile(path, searchCriteria);
        } else if (isZipFile(path)) {
            return processZipFile(path, searchCriteria);
        }
        return Optional.absent();
    }

    private static boolean isClassFile(Path path) {
        return path.getFileName().toString().endsWith(".class");
    }

    private static Optional<List<SearchResult>> processClassFile(Path path, SearchCriteria searchCriteria) {
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

    private static Optional<List<SearchResult>> processZipFile(Path path, SearchCriteria searchCriteria) {
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
            return Optional.absent();
        }
        if (searchResults != null) {
            return Optional.of(searchResults);
        }
        return Optional.absent();
    }

    private static void processZipEntry(Path path, SearchCriteria searchCriteria, List<SearchResult> searchResults, ZipFile zipFile, ZipEntry zipEntry, Stack zipFileStack) {
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

    private static String constructZipFilePath(Stack<String> zipFileStack, String zipEntryName) {
        StringBuilder sb = new StringBuilder();
        for (String zipFileStackEntry : zipFileStack) {
            appendExclamationMark(sb);
            sb.append(zipFileStackEntry);
        }
        appendExclamationMark(sb);
        sb.append(zipEntryName);
        return sb.toString();
    }

    private static void appendExclamationMark(StringBuilder sb) {
        if (sb.length() > 0) {
            sb.append("!");
        }
    }

    private static String filenameFromZipEntryName(String name) {
        String[] strings = name.split("/");
        if (strings != null && strings.length > 0) {
            return strings[strings.length - 1];
        }
        return name;
    }

    private static boolean isZipFile(Path path) {
        boolean isZipFile = false;
        try (RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r")) {
            int readInt = raf.readInt();
            isZipFile = isZipMagicNumber(readInt);
        } catch (IOException e) {
            isZipFile = false;
        }
        return isZipFile;
    }

    private static boolean isZipMagicNumber(int readInt) {
        boolean isMagicNumber = false;
        if (readInt == 0x504B0304) {
            isMagicNumber = true;
        }
        return isMagicNumber;
    }
}
