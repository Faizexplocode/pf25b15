// SubMenuPanel.java (Ini adalah file yang harus Anda buat dan isi)
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SubMenuPanel extends JPanel { // Pastikan nama kelas ini SubMenuPanel

    private GameMain gameMain;

    public SubMenuPanel(GameMain gameMain) {
        this.gameMain = gameMain;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(100, 50, 50, 50));

        JLabel titleLabel = new JLabel("Pilih Mode Permainan", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
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

        add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 60));
        button.setPreferredSize(new Dimension(250, 60));
        button.setFont(new Font("SansSerif", Font.BOLD, 20));
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