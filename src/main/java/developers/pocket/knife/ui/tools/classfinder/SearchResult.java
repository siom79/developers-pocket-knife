package developers.pocket.knife.ui.tools.classfinder;

public class SearchResult {
    private String zipFilePath = "";
    private String location;

    public SearchResult(String location) {
        this.location = location;
    }

    public String getZipFilePath() {
        return zipFilePath;
    }

    public void setZipFilePath(String zipFilePath) {
        this.zipFilePath = zipFilePath;
    }

    public String getLocation() {
        return location;
    }
}
