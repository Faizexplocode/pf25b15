import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {

    public MainMenuPanel(GameMain gameMain) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Load the GIF from resources
        ImageIcon gifIcon = loadFullScreenGif("images/welcome.gif");
        JLabel gifLabel = new JLabel();
        gifLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gifLabel.setVerticalAlignment(SwingConstants.CENTER);
        gifLabel.setIcon(gifIcon);

        // Transparent panel for buttons over GIF
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(60, 20, 60, 20));

        // Add buttons
        buttonPanel.add(createMenuButton("Play vs Player", e -> gameMain.startVsPlayer()));
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(createMenuButton("Play vs AI", e -> gameMain.startVsAI()));
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(createMenuButton("Petunjuk Permainan", e -> gameMain.showInstructions()));
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(createMenuButton("Exit", e -> System.exit(0)));

        // Container panel with overlay layout
        JPanel overlay = new JPanel(new GridBagLayout());
        overlay.setOpaque(false);
        overlay.add(buttonPanel);

        // Layered pane to combine GIF and overlay
        JLayeredPane layeredPane = new JLayeredPane();
        gifLabel.setBounds(0, 0, 400, 500); // Adjust as needed
        overlay.setBounds(0, 0, 400, 500);

        layeredPane.setPreferredSize(new Dimension(400, 500));
        layeredPane.add(gifLabel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(overlay, JLayeredPane.PALETTE_LAYER);

        add(layeredPane, BorderLayout.CENTER);
    }

    private ImageIcon loadFullScreenGif(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL == null) {
            System.err.println("GIF not found at: " + path);
            return null;
        }
        return new ImageIcon(imgURL);
    }

    private JButton createMenuButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
        button.addActionListener(listener);
        return button;
    }
}
