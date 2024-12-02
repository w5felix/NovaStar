package interface_adapterss;

import api.MarketAuxAPIClient;

import java.util.List;

public class NewsService implements INewsService {

    @Override
    public List<String> fetchNews(String query) throws Exception {
        return MarketAuxAPIClient.fetchNews(query);
    }
}
