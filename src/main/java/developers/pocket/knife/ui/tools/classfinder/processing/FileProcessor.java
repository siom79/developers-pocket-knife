package developers.pocket.knife.ui.tools.classfinder.processing;

import com.google.common.base.Optional;
import developers.pocket.knife.ui.tools.classfinder.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessor.class);

    public static Optional<List<SearchResult>> processPath(Path path, String className) {
        if (isClassFile(path)) {
            return processClassFile(path, className);
        } else if (isZipFile(path)) {
            return processZipFile(path, className);
        }
        return Optional.absent();
    }

    private static boolean isClassFile(Path path) {
        return path.getFileName().toString().endsWith(".class");
    }

    private static Optional<List<SearchResult>> processClassFile(Path path, String className) {
        String fileName = path.getFileName().toString();
        LOGGER.debug("Processing class file " + fileName);
        if (fileName.contains(className)) {
            List<SearchResult> searchResults = new ArrayList<>(1);
            SearchResult searchResult = new SearchResult(path.toString());
            searchResults.add(searchResult);
            return Optional.of(searchResults);
        }
        return Optional.absent();
    }

    private static Optional<List<SearchResult>> processZipFile(Path path, String className) {
        List<SearchResult> searchResults = null;
        try (
                ZipFile zipFile = new ZipFile(path.toFile())
        ) {
            LOGGER.debug("Processing zip file " + path);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                String name = zipEntry.getName();
                LOGGER.debug("Processing zip file entry " + name);
                String filename = extractFilename(name);
                if (filename.endsWith(".class") && filename.contains(className)) {
                    if (searchResults == null) {
                        searchResults = new LinkedList<>();
                    }
                    String location = path.toString();
                    SearchResult searchResult = new SearchResult(location);
                    searchResult.setZipFilePath(name);
                    searchResults.add(searchResult);
                }
            }
        } catch (Exception e) {
            return Optional.absent();
        }
        if (searchResults != null) {
            return Optional.of(searchResults);
        }
        return Optional.absent();
    }

    private static String extractFilename(String name) {
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
            if (readInt == 0x504B0304) {
                isZipFile = true;
            }
        } catch (IOException e) {
            isZipFile = false;
        }
        return isZipFile;
    }
}
