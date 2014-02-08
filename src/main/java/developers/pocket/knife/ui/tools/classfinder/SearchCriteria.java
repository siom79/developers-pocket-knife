package developers.pocket.knife.ui.tools.classfinder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchCriteria {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchCriteria.class);
    private final String className;

    public SearchCriteria(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public boolean matches(String fileName) {
        boolean match = false;
        if(fileName.contains(className)) {
            match = true;
        }
        LOGGER.debug("matches(): {} contains {} = {}", fileName, className, match);
        return match;
    }
}
