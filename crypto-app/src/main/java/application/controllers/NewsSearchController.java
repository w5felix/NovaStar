package application.controllers;

import application.interactors.news_search.NewsSearchDataAccessInterface;
import views.NewsSearchView;

import javax.swing.*;
import java.util.List;

public class NewsSearchController {

    private final NewsSearchDataAccessInterface newsService;
    private final NewsSearchView newsSearchView;

    public NewsSearchController(NewsSearchDataAccessInterface newsService, NewsSearchView newsSearchView) {
        this.newsService = newsService;
        this.newsSearchView = newsSearchView;
    }

    public JPanel getView() {
        newsSearchView.setSearchListener(e -> handleSearch());
        return newsSearchView;
    }

    private void handleSearch() {
        SwingUtilities.invokeLater(() -> {
            String query = newsSearchView.getSearchQuery();
            if (query.isEmpty()) {
                newsSearchView.displayNews(List.of("Please enter a search query."));
                return;
            }
            try {
                List<String> articles = newsService.fetchNews(query);
                newsSearchView.displayNews(articles);
            } catch (Exception e) {
                newsSearchView.displayNews(List.of("Error fetching news: " + e.getMessage()));
            }
        });
    }
}
