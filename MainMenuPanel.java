// MainMenuPanel.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenuPanel extends JPanel {

    private GameMain gameMain;

    public MainMenuPanel(GameMain gameMain) {
        this.gameMain = gameMain;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        ImageIcon gifIcon = loadFullScreenGif("/images/welcome.gif");
        JLabel gifLabel = new JLabel();
        gifLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gifLabel.setVerticalAlignment(SwingConstants.CENTER);
        if (gifIcon != null) {
            gifLabel.setIcon(gifIcon);
        } else {
            gifLabel.setText("Background GIF Not Found");
            gifLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        }

        JPanel buttonOverlayPanel = new JPanel(new GridBagLayout());
        buttonOverlayPanel.setOpaque(false);

        JPanel buttonContainer = new JPanel();
        buttonContainer.setOpaque(false);
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
        buttonContainer.setBorder(BorderFactory.createEmptyBorder(350, 20, 20, 20)); // Adjust padding

        JButton btnStart = createMenuButton("Start", e -> gameMain.showSubMenu());
        JButton btnExit = createMenuButton("Exit", e -> System.exit(0));

        buttonContainer.add(btnStart);
        buttonContainer.add(Box.createVerticalStrut(20));
        buttonContainer.add(btnExit);

        buttonOverlayPanel.add(buttonContainer);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(400, 500)); // Ukuran preferensi menu

        gifLabel.setBounds(0, 0, 400, 500);
        buttonOverlayPanel.setBounds(0, 0, 400, 500);

        layeredPane.add(gifLabel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(buttonOverlayPanel, JLayeredPane.PALETTE_LAYER);

        add(layeredPane, BorderLayout.CENTER);
    }

    private ImageIcon loadFullScreenGif(String path) {
        try {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL == null) {
                System.err.println("GIF not found at: " + path);
                return null;
            }
            return new ImageIcon(imgURL);
        } catch (Exception e) {
            System.err.println("Error loading GIF: " + path + " - " + e.getMessage());
            return null;
        }
    }

    private JButton createMenuButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 50));
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.addActionListener(listener);
        button.setFocusPainted(false);
        button.setBackground(new Color(200, 220, 255));
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(150, 180, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(200, 220, 255));
            }
        });

        return button;
    }
}