package news_search;

import java.util.List;

import data_access.NewsSearchDataAccessObject;

/**
 * The News Search Interactor.
 */
public class NewsSearchInteractor implements NewsSearchDataAccessInterface {

    @Override
    public List<String> fetchNews(String query) throws Exception {
        return NewsSearchDataAccessObject.fetchNews(query);
    }
}
