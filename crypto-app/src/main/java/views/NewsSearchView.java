package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class NewsSearchView extends JPanel {

    private final JTextField searchField = new JTextField(30);
    private final JTextArea resultsArea = new JTextArea();

    public NewsSearchView() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search News:"));
        searchPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton);

        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultsArea), BorderLayout.CENTER);

        searchButton.addActionListener(e -> fireSearchEvent());
    }

    public String getSearchQuery() {
        return searchField.getText().trim();
    }

    public void displayNews(List<String> articles) {
        resultsArea.setText("");
        if (articles.isEmpty()) {
            resultsArea.setText("No news articles found.");
        } else {
            for (String article : articles) {
                resultsArea.append(article + "\n\n");
            }
        }
    }

    public void setSearchListener(ActionListener listener) {
        searchField.addActionListener(listener);
    }

    private void fireSearchEvent() {
        for (ActionListener listener : searchField.getActionListeners()) {
            listener.actionPerformed(null);
        }
    }
}
