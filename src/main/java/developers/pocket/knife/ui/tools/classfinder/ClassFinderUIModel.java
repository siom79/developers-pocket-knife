package developers.pocket.knife.ui.tools.classfinder;

import com.google.common.base.Optional;
import developers.pocket.knife.exceptions.TechnicalException;
import developers.pocket.knife.ui.tools.classfinder.processing.FileProcessor;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class ClassFinderUIModel extends SwingWorker<Void, List<SearchResult>> {
    private String directory;
    private String classNameCriteria;
    private boolean caseSensitive;
    private boolean contains;

    public ClassFinderUIModel(String directory, String classNameCriteria, boolean caseSensitive, boolean contains) {
        this.directory = directory;
        this.classNameCriteria = classNameCriteria;
        this.caseSensitive = caseSensitive;
        this.contains = contains;
    }

    public void search(final String directory, final String inputClassNameText, final boolean caseSensitive, final
    boolean contains) {
        try {
            final FileProcessor fileProcessor = new FileProcessor();
            FileVisitor<? super Path> visitor = new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (!isCancelled()) {
                        SearchCriteria searchCriteria = new SearchCriteria(inputClassNameText);
                        searchCriteria.setCaseSensitive(caseSensitive);
                        searchCriteria.setContains(contains);
                        Optional<List<SearchResult>> listOptional = fileProcessor.processPath(file, searchCriteria);
                        if (listOptional.isPresent()) {
                            List<SearchResult> results = listOptional.get();
                            publish(results);
                        }
                        if (fileProcessor.hasMoreAsyncResults()) {
                            Optional<List<SearchResult>> nextAsyncResults = fileProcessor.getNextAsyncResults();
                            if (nextAsyncResults.isPresent()) {
                                publish(nextAsyncResults.get());
                            }
                        }
                    } else {
                        return FileVisitResult.TERMINATE;
                    }
                    return super.visitFile(file, attrs);
                }
            };
            Files.walkFileTree(Paths.get(directory), visitor);
            while (fileProcessor.hasMoreAsyncResults() && !isCancelled()) {
                Optional<List<SearchResult>> nextAsyncResults = fileProcessor.getNextAsyncResults();
                if (nextAsyncResults.isPresent()) {
                    publish(nextAsyncResults.get());
                }
            }
        } catch (IOException e) {
            throw new TechnicalException(TechnicalException.Reason.IoError, e);
        }
    }

    @Override
    protected Void doInBackground() throws Exception {
        search(directory, classNameCriteria, caseSensitive, contains);
        return null;
    }
}
