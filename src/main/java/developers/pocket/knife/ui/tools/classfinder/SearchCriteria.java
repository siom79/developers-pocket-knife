package developers.pocket.knife.ui.tools.classfinder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchCriteria {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchCriteria.class);
    private final String className;
    private boolean contains = false;
    private boolean caseSensitive = false;

    public SearchCriteria(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public boolean matches(String fileName) {
        boolean match = false;
        String tempClassName = className;
        if (!caseSensitive) {
            fileName = fileName.toLowerCase();
            tempClassName = tempClassName.toLowerCase();
        }
        if (contains) {
            if (fileName.contains(tempClassName)) {
                match = true;
            }
            LOGGER.debug("matches(): {} contains {} = {}", fileName, tempClassName, match);
        } else {
            if (fileName.equals(tempClassName)) {
                match = true;
            }
            LOGGER.debug("matches(): {} equals {} = {}", fileName, tempClassName, match);
        }
        return match;
    }

    public boolean isContains() {
        return contains;
    }

    public void setContains(boolean contains) {
        this.contains = contains;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
}
