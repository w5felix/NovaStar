package application.interactors.news_search;

import java.util.List;

import infrastructure.api_clients.NewsSearchApiClient;

/**
 * The News Search Interactor.
 */
public class NewsSearchInteractor implements NewsSearchDataAccessInterface {

    @Override
    public List<String> fetchNews(String query) throws Exception {
        return NewsSearchApiClient.fetchNews(query);
    }
}
