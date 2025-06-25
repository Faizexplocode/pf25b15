import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenuPanel extends JPanel {

    public MainMenuPanel(GameMain gameMain) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        setBackground(Color.WHITE);  // hilangkan background gelap

        // === Top Panel (title + gif) ===
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Tic Tac Toe", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel gifLabel = new JLabel();
        gifLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gifLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            gifLabel.setIcon(new ImageIcon(getClass().getResource("images/welcome.gif")));
        } catch (Exception e) {
            gifLabel.setText("GIF not found");
        }

        topPanel.add(titleLabel);
        topPanel.add(gifLabel);

        // === Button Panel ===
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        buttonPanel.add(createImageButton("/assets/btn_play.png", e -> gameMain.startVsPlayer()));
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(createImageButton("/assets/btn_ai.png", e -> gameMain.startVsAI()));
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(createImageButton("/assets/btn_info.png", e -> gameMain.showInstructions()));
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(createImageButton("/assets/btn_exit.png", e -> System.exit(0)));

        add(topPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createImageButton(String imagePath, ActionListener action) {
        JButton button = new JButton();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            button.setIcon(icon);
        } catch (Exception e) {
            button.setText("Button");
        }

        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);

        // Efek hover animasi sederhana
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setLocation(button.getX(), button.getY() - 2);
            }

            public void mouseExited(MouseEvent e) {
                button.setLocation(button.getX(), button.getY() + 2);
            }
        });

        return button;
    }
}
