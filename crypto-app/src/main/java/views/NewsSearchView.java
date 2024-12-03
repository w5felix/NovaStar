package views;

import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.awt.*;

public class NewsSearchView extends JPanel {

    private final JTextField searchField = new JTextField(30);
    private final JTextArea resultsArea = new JTextArea();

    public NewsSearchView() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Funny Title
        JLabel title = new JLabel("🔥 Super Serious News Search 3000 🔥", JLabel.CENTER);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        title.setForeground(new Color(255, 69, 0)); // Hot red-orange
        add(title, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(new Color(240, 230, 140)); // Khaki for humor
        searchPanel.setLayout(new FlowLayout());

        JLabel searchLabel = new JLabel("🔍 Search:");
        searchLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        searchField.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));

        JButton searchButton = new JButton("Go Fetch!");
        searchButton.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(new Color(60, 179, 113)); // Sea green
        searchButton.setOpaque(true);
        searchButton.setBorderPainted(false);

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.CENTER);

        // Results Area with Centering
        JPanel resultsPanel = new JPanel(new GridBagLayout());
        resultsPanel.setBackground(new Color(255, 239, 213)); // Blanched almond
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        resultsArea.setForeground(new Color(75, 0, 130)); // Indigo text
        resultsArea.setBorder(BorderFactory.createTitledBorder("📜 Your News Results 📜"));
        resultsArea.setWrapStyleWord(true);
        resultsArea.setLineWrap(true);

        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setPreferredSize(new Dimension(400, 200)); // Fixed size for the scroll pane

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;

        resultsPanel.add(scrollPane, gbc);
        add(resultsPanel, BorderLayout.SOUTH);

        // Adding Event Listeners
        searchButton.addActionListener(e -> fireSearchEvent());
    }

    public String getSearchQuery() {
        return searchField.getText().trim();
    }

    public void displayNews(List<String> articles) {
        resultsArea.setText("");
        if (articles.isEmpty()) {
            resultsArea.setText("😢 Oops! No news articles found. Maybe try searching for 'cats'?");
        } else {
            for (String article : articles) {
                resultsArea.append("📰 " + article + "\n\n");
            }
        }
    }

    public void setSearchListener(ActionListener listener) {
        searchField.addActionListener(listener);
    }

    void fireSearchEvent() {
        for (ActionListener listener : searchField.getActionListeners()) {
            listener.actionPerformed(null);
        }
    }
}
