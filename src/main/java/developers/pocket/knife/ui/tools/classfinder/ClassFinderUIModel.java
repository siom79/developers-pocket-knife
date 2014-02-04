package developers.pocket.knife.ui.tools.classfinder;

import com.google.common.base.Optional;
import developers.pocket.knife.exceptions.TechnicalException;
import developers.pocket.knife.ui.tools.classfinder.processing.FileProcessor;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

public class ClassFinderUIModel extends SwingWorker<List<SearchResult>, List<SearchResult>> {
    private String directory;
    private String classNameCriteria;

    public ClassFinderUIModel(String directory, String classNameCriteria) {
        this.directory = directory;
        this.classNameCriteria = classNameCriteria;
    }

    public List<SearchResult> search(final String directory, final String inputClassNameText) {
        try {
            final List<SearchResult> searchResults = new LinkedList<>();
            FileVisitor<? super Path> visitor = new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (!isCancelled()) {
                        Optional<List<SearchResult>> listOptional = FileProcessor.processPath(file, inputClassNameText);
                        if (listOptional.isPresent()) {
                            List<SearchResult> results = listOptional.get();
                            searchResults.addAll(results);
                            publish(results);
                        }
                    } else {
                        return FileVisitResult.TERMINATE;
                    }
                    return super.visitFile(file, attrs);
                }
            };
            Files.walkFileTree(Paths.get(directory), visitor);
            return searchResults;
        } catch (IOException e) {
            throw new TechnicalException(TechnicalException.Reason.IoError, e);
        }
    }

    @Override
    protected List<SearchResult> doInBackground() throws Exception {
        return search(directory, classNameCriteria);
    }
}
