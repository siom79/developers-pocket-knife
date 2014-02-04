package developers.pocket.knife.ui.tools.classfinder;

public class SearchResult {
    private final String location;
    private String zipFilePath = "";

    public SearchResult(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public String getZipFilePath() {
        return zipFilePath;
    }

    public void setZipFilePath(String zipFilePath) {
        this.zipFilePath = zipFilePath;
    }
}
