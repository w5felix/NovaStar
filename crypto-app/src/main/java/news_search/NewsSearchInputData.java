package news_search;

/**
 * The Input Data for the News Search Use Case.
 */
public class NewsSearchInputData {
    private final String searchTerm;

    public NewsSearchInputData(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    String getSearchTerm() {
        return searchTerm;
    }


}
