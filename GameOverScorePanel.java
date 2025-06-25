import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameOverScorePanel extends JPanel {
    private JLabel gameOverLabel;
    private JLabel playerXLabel;
    private JLabel playerOLabel;
    private JLabel drawLabel;

    private JButton playAgainButton;
    private JButton backToMenuButton;
    private JButton exitGameButton;

    private Image backgroundImage;

    // Skor
    private int playerXWins = 0;
    private int playerOWins = 0;
    private int draws = 0;

    public GameOverScorePanel() {
        // Load background
        try {
            backgroundImage = new ImageIcon(getClass().getResource("/images/gambar.gif")).getImage();
        } catch (Exception e) {
            System.err.println("Background error: " + e.getMessage());
            backgroundImage = null;
        }

        setLayout(new GridBagLayout());

        // Panel tengah
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Label Game Over
        gameOverLabel = new JLabel("GAME OVER", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        gameOverLabel.setForeground(Color.WHITE);
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(gameOverLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        // Papan skor
        JPanel scorePanel = new JPanel(new GridLayout(3, 1, 10, 5));
        scorePanel.setOpaque(false);
        scorePanel.setMaximumSize(new Dimension(300, 100));

        playerXLabel = createScoreLabel("Player X Wins: 0");
        playerOLabel = createScoreLabel("Player O Wins: 0");
        drawLabel = createScoreLabel("Draws       : 0");

        scorePanel.add(playerXLabel);
        scorePanel.add(playerOLabel);
        scorePanel.add(drawLabel);

        centerPanel.add(scorePanel);
        centerPanel.add(Box.createVerticalStrut(20));

        // Tombol
        playAgainButton = createButton("Play Again");
        backToMenuButton = createButton("Back to Menu");
        exitGameButton = createButton("Exit Game");

        centerPanel.add(playAgainButton);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(backToMenuButton);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(exitGameButton);

        add(centerPanel);
    }

    private JLabel createScoreLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setForeground(Color.YELLOW);
        return label;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(new Color(167, 211, 45));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setMaximumSize(new Dimension(200, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(92, 122, 23));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(167, 211, 45));
            }
        });

        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public void setGameOverMessage(String message) {
        gameOverLabel.setText(message);
    }

    public void updateScore(State finalState) {
        if (finalState == State.CROSS_WON) {
            playerXWins++;
        } else if (finalState == State.NOUGHT_WON) {
            playerOWins++;
        } else if (finalState == State.DRAW) {
            draws++;
        }

        playerXLabel.setText("Player X Wins: " + playerXWins);
        playerOLabel.setText("Player O Wins: " + playerOWins);
        drawLabel.setText("Draws       : " + draws);
    }

    public void resetScore() {
        playerXWins = 0;
        playerOWins = 0;
        draws = 0;
        updateScore(null);
    }

    public void addPlayAgainListener(ActionListener listener) {
        playAgainButton.addActionListener(listener);
    }

    public void addBackToMenuListener(ActionListener listener) {
        backToMenuButton.addActionListener(listener);
    }

    public void addExitGameListener(ActionListener listener) {
        exitGameButton.addActionListener(listener);
    }
}
