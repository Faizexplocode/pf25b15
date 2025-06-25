import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SubMenuPanel extends JPanel {

    private GameMain gameMain;

    public SubMenuPanel(GameMain gameMain) {
        this.gameMain = gameMain;
        setLayout(new OverlayLayout(this));
        setPreferredSize(new Dimension(400, 500));

        // Load gambar GIF sebagai ImageIcon
        ImageIcon gifIcon = new ImageIcon("images/gambar.gif");

        // Panel custom untuk menampilkan GIF dengan ukuran 400x500
        JPanel gifPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Ambil frame pertama dari ImageIcon
                Image image = gifIcon.getImage();
                // Gambar ulang dengan ukuran 400x500
                g.drawImage(image, 0, 0, 400, 500, this);
            }
        };
        gifPanel.setOpaque(false);
        gifPanel.setPreferredSize(new Dimension(400, 500));
        gifPanel.setMaximumSize(new Dimension(400, 500));
        gifPanel.setMinimumSize(new Dimension(400, 500));
        gifPanel.setAlignmentX(0.5f);
        gifPanel.setAlignmentY(0.5f);

        // Panel tombol transparan
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(80, 50, 50, 50));
        buttonPanel.setAlignmentX(0.5f);
        buttonPanel.setAlignmentY(0.5f);

        JLabel titleLabel = new JLabel("Pilih Mode Permainan", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JButton btnVsPlayer = createMenuButton("Play vs Player", e -> gameMain.startVsPlayer());
        JButton btnVsAI = createMenuButton("Play vs AI", e -> gameMain.startVsAI());
        JButton btnBack = createMenuButton("Back to Main Menu", e -> gameMain.showMainMenu());

        buttonPanel.add(titleLabel);
        buttonPanel.add(btnVsPlayer);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(btnVsAI);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(btnBack);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(400, 500));
        wrapper.add(buttonPanel);

        add(wrapper);   // Panel tombol
        add(gifPanel);  // Background
    }

    private JButton createMenuButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 50));
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.addActionListener(listener);
        button.setFocusPainted(false);
        button.setBackground(new Color(180, 220, 180));
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(130, 180, 130));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(180, 220, 180));
            }
        });

        return button;
    }
}
