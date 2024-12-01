package news_search;

/**
 * Output Data for the News Search Use Case.
 */
public class NewsSearchOutputData {

    private final String title;
    private final String link;
    private final boolean useCaseFailed;

    public NewsSearchOutputData(String title, String link, boolean useCaseFailed) {
        this.title = title;
        this.link = link;
        this.useCaseFailed = useCaseFailed;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link; }

}

