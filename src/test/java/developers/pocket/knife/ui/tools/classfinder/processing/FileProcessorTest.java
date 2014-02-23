package developers.pocket.knife.ui.tools.classfinder.processing;

import com.google.common.base.Optional;
import developers.pocket.knife.ui.tools.classfinder.SearchCriteria;
import developers.pocket.knife.ui.tools.classfinder.SearchResult;
import org.apache.log4j.BasicConfigurator;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;
import org.hamcrest.CoreMatchers.*;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FileProcessorTest {

    @BeforeClass
    public static void beforeClass() {
        BasicConfigurator.configure();
    }

    @Test
    public void testSelfFind() throws IOException {
        String userDir = System.getProperty("user.dir");
        final SearchCriteria searchCriteria = new SearchCriteria(FileProcessor.class.getSimpleName());
        searchCriteria.setContains(true);
        final List<SearchResult> searchResults = new LinkedList<>();
        Files.walkFileTree(Paths.get(userDir), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                FileProcessor fileProcessor = new FileProcessor();
                Optional<List<SearchResult>> listOptional = fileProcessor.processPath(file, searchCriteria);
                if(listOptional.isPresent()) {
                    searchResults.addAll(listOptional.get());
                }
                return super.visitFile(file, attrs);
            }
        });
        assertThat(searchResults.size(), Matchers.greaterThan(1));
        for(SearchResult searchResult : searchResults) {
            assertThat(searchResult.getLocation().contains(FileProcessor.class.getSimpleName()), is(true));
        }
    }
}
