package interface_adapterss;

import java.util.List;

public interface INewsService {
    /**
     * Fetches news articles based on the query.
     *
     * @param query the search query
     * @return a list of news articles
     * @throws Exception if there is an error fetching the news
     */
    List<String> fetchNews(String query) throws Exception;
}
