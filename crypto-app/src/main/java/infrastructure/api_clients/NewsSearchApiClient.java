package infrastructure.api_clients;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NewsSearchApiClient {

    private static final String API_URL = "https://api.marketaux.com/v1/news/all";
    private static final String API_TOKEN = "ZQowIU3ELta99lZVnwIWkCNf4UrXnXOPe0WzBW05";  // Replace with your actual API token

    // Method to get news based on a search query
    public static List<String> fetchNews(String searchQuery) {
        List<String> newsArticles = new ArrayList<>();

        try {
            // URL encode the search query to ensure it's safe for HTTP requests
            String encodedSearchQuery = java.net.URLEncoder.encode(searchQuery, "UTF-8");

            // Build the request URL with the search parameter and API token
            String urlString = String.format("%s?search=%s&api_token=%s&language=en", API_URL, encodedSearchQuery, API_TOKEN);
            URL url = new URL(urlString);

            // Open connection and set HTTP method to GET
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);  // Timeout in ms

            // Check if the request was successful
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response data
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse the response JSON
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray articles = jsonResponse.getJSONArray("data");

                // If no articles are found, notify the user


                // Print each article's title, description, and URL
                for (int i = 0; i < articles.length(); i++) {
                    JSONObject article = articles.getJSONObject(i);
                    String title = article.getString("title"); // Adjust the key based on actual response
                    String urlArticle = article.getString("url");
                    // Adjust if necessary
                    newsArticles.add(title + ": " + urlArticle);
                }
            } else {
                System.out.println("GET request failed, Response Code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsArticles;
    }

    public static void main(String[] args) {
        // Create a Scanner object to take input from the user
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your search query (you can use advanced search operators like +, |, -):");
        String searchQuery = scanner.nextLine();

        // Fetch and display the news based on the search query
        fetchNews(searchQuery);

        scanner.close();
    }
}
