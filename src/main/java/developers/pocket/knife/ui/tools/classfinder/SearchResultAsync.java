package developers.pocket.knife.ui.tools.classfinder;

import java.util.LinkedList;
import java.util.List;

public class SearchResultAsync {
    private final long id;
    private final List<SearchResult> searchResultList = new LinkedList<>();

    public SearchResultAsync(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public List<SearchResult> getSearchResultList() {
        return searchResultList;
    }
}
