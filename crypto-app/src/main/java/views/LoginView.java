package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class LoginView extends JPanel {
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JButton loginButton = new JButton("✨ Login ✨");
    private final JButton registerButton = new JButton("🎉 Register 🎉");
    private final JButton resetPasswordButton = new JButton("💡 Forgot Password?");
    private final JLabel statusLabel = new JLabel("");
    private final AnimatedTitlePanel animatedTitlePanel = new AnimatedTitlePanel("💸 NOVASTAR Crypto Manager 💸");

    public LoginView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(255, 200, 200)); // Soft pink background
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Subtitle
        JLabel subtitleLabel = new JLabel("Where Fun Meets Finance!");
        subtitleLabel.setFont(new Font("Comic Sans MS", Font.ITALIC, 16));
        subtitleLabel.setForeground(new Color(0, 150, 0)); // Green text
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Email input
        JLabel emailLabel = new JLabel("📧 Email:");
        emailLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        emailLabel.setForeground(new Color(0, 0, 255)); // Blue text
        emailField.setFont(new Font("Courier New", Font.PLAIN, 16));
        emailField.setBackground(new Color(255, 255, 180)); // Light yellow

        // Password input
        JLabel passwordLabel = new JLabel("🔒 Password:");
        passwordLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        passwordLabel.setForeground(new Color(255, 0, 0)); // Red text
        passwordField.setFont(new Font("Courier New", Font.PLAIN, 16));
        passwordField.setBackground(new Color(255, 255, 180)); // Light yellow

        // Buttons with goofy styles
        styleButton(loginButton, new Color(100, 255, 100)); // Bright green
        styleButton(registerButton, new Color(100, 100, 255)); // Bright blue
        styleButton(resetPasswordButton, new Color(255, 200, 0)); // Bright yellow

        // Status label
        statusLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        statusLabel.setForeground(new Color(255, 0, 255)); // Pink text
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Layout organization
        add(Box.createVerticalStrut(20));
        add(animatedTitlePanel);
        add(subtitleLabel);
        add(Box.createVerticalStrut(20));
        add(emailLabel);
        add(emailField);
        add(Box.createVerticalStrut(10));
        add(passwordLabel);
        add(passwordField);
        add(Box.createVerticalStrut(20));
        add(loginButton);
        add(Box.createVerticalStrut(10));
        add(registerButton);
        add(Box.createVerticalStrut(10));
        add(resetPasswordButton);
        add(Box.createVerticalStrut(20));
        add(statusLabel);

        animatedTitlePanel.startAnimation();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void setStatusMessage(String message) {
        statusLabel.setText(message);
    }

    public void displayErrorMessage(String jsonResponse) {
        try {
            // Parse the JSON response to extract the "message" field
            int secondCurlyIndex = jsonResponse.indexOf("{", jsonResponse.indexOf("{") + 1);
            if (secondCurlyIndex != -1) {
                jsonResponse = jsonResponse.substring(secondCurlyIndex);
            }
            String message = new org.json.JSONObject(jsonResponse).optString("message", "An error occurred!");

            // Set the extracted message as the status label
            setStatusMessage(message);
            statusLabel.setForeground(Color.RED); // Start with red text
            statusLabel.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 20));

            // Start animation
            Timer animationTimer = new Timer(50, new ActionListener() {
                private int step = 0;
                private final int maxSteps = 20; // Number of animation steps
                private final float baseFontSize = 20f;

                @Override
                public void actionPerformed(ActionEvent e) {
                    // Change the text color to random bright colors
                    statusLabel.setForeground(new Color(
                            (int) (Math.random() * 255),
                            (int) (Math.random() * 255),
                            (int) (Math.random() * 255)
                    ));

                    // Apply a goofy scaling effect
                    float scale = 1.0f + 0.1f * (float) Math.sin(step * Math.PI / maxSteps);
                    statusLabel.setFont(statusLabel.getFont().deriveFont(baseFontSize * scale));

                    // Increment animation step
                    step++;
                    if (step > maxSteps) {
                        ((Timer) e.getSource()).stop();

                        // Reset to normal style after animation
                        statusLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
                        statusLabel.setForeground(Color.PINK);
                    }

                    // Repaint the label to reflect changes
                    statusLabel.repaint();
                }
            });

            animationTimer.start();
        } catch (Exception ex) {
            setStatusMessage("⚠️ Error parsing message!"); // Fallback error
            ex.printStackTrace(); // Log the exception for debugging
        }
    }



    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void addRegisterListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    public void addResetPasswordListener(ActionListener listener) {
        resetPasswordButton.addActionListener(listener);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        button.setForeground(new Color(255, 255, 255)); // White text
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 2)); // Black border
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add goofy hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    // Inner class for animated title
    private static class AnimatedTitlePanel extends JPanel {
        private final String title;
        private final Font font = new Font("Comic Sans MS", Font.BOLD, 40);
        private final Random random = new Random();
        private double scale = 1.0;
        private double xOffset = 0;
        private double yOffset = 0;
        private Color color = Color.MAGENTA;

        public AnimatedTitlePanel(String title) {
            this.title = title;
            setPreferredSize(new Dimension(600, 100));
            setOpaque(false); // Transparent background
        }

        public void startAnimation() {
            Timer timer = new Timer(16, e -> {
                // Smoothly vary scale
                scale = 1.0 + 0.2 * Math.sin(System.currentTimeMillis() * 0.002);

                // Smoothly vary offsets
                xOffset = 10 * Math.sin(System.currentTimeMillis() * 0.001);
                yOffset = 10 * Math.cos(System.currentTimeMillis() * 0.001);

                // Change color randomly but gradually
                color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));

                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            AffineTransform transform = g2d.getTransform();
            g2d.setColor(color);

            // Apply transformations
            g2d.translate(getWidth() / 2 + xOffset, getHeight() / 2 + yOffset);
            g2d.scale(scale, scale);

            // Draw centered text
            FontMetrics metrics = g2d.getFontMetrics(font);
            int textWidth = metrics.stringWidth(title);
            int textHeight = metrics.getHeight();
            g2d.setFont(font);
            g2d.drawString(title, -textWidth / 2, textHeight / 2);

            g2d.setTransform(transform);
        }
    }
}
