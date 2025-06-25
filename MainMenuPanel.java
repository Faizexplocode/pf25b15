import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenuPanel extends JPanel {

    private GameMain gameMain; // Simpan referensi ke GameMain

    public MainMenuPanel(GameMain gameMain) {
        this.gameMain = gameMain; // Inisialisasi referensi
        setLayout(new BorderLayout());
        setBackground(Color.WHITE); // Default background

        // Load the GIF from resources
        ImageIcon gifIcon = loadFullScreenGif("/images/welcome.gif"); // Path sesuai struktur Anda
        JLabel gifLabel = new JLabel();
        gifLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gifLabel.setVerticalAlignment(SwingConstants.CENTER);
        if (gifIcon != null) {
            gifLabel.setIcon(gifIcon);
        } else {
            gifLabel.setText("Background GIF Not Found");
            gifLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        }

        // Overlay panel for buttons (transparent)
        // Gunakan GridBagLayout untuk kontrol posisi yang lebih presisi
        JPanel buttonOverlayPanel = new JPanel(new GridBagLayout());
        buttonOverlayPanel.setOpaque(false); // Make it transparent

        JPanel buttonContainer = new JPanel();
        buttonContainer.setOpaque(false); // Make it transparent
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
        // HIGHLIGHT MULAI: ATUR POSISI VERTIKAL TOMBOL
        // Sesuaikan nilai top, left, bottom, right padding untuk menggeser tombol ke atas/bawah/kiri/kanan
        // Top padding yang lebih besar akan mendorong tombol ke bawah
        buttonContainer.setBorder(BorderFactory.createEmptyBorder(350, 20, 20, 20)); // <-- EDIT INI untuk vertikal posisi
        // HIGHLIGHT AKHIR

        // --- HANYA DUA TOMBOL DI AWAL ---
        JButton btnStart = createMenuButton("Start", e -> gameMain.showSubMenu()); // Panggil showSubMenu
        JButton btnExit = createMenuButton("Exit", e -> System.exit(0));

        buttonContainer.add(btnStart);
        // HIGHLIGHT MULAI: ATUR SPASI ANTAR TOMBOL
        buttonContainer.add(Box.createVerticalStrut(20)); // <-- EDIT INI untuk spasi antar tombol
        // HIGHLIGHT AKHIR
        buttonContainer.add(btnExit);

        buttonOverlayPanel.add(buttonContainer); // Tambahkan container tombol ke overlay

        // Layered pane to combine GIF and overlay
        JLayeredPane layeredPane = new JLayeredPane();
        // Penting: Atur ukuran preferredSize untuk layeredPane agar pack() berfungsi
        // Ini harus sesuai dengan ukuran tampilan yang Anda inginkan (misal: 400x500)
        layeredPane.setPreferredSize(new Dimension(400, 500)); // <-- EDIT INI jika ukuran dasar berubah

        // Set bounds for GIF label and button overlay
        // Pastikan ukuran ini mencakup seluruh area panel
        // Ini mengatur ukuran dan posisi label GIF dan panel overlay
        gifLabel.setBounds(0, 0, 400, 500);         // <-- EDIT INI jika ukuran dasar berubah
        buttonOverlayPanel.setBounds(0, 0, 400, 500); // <-- EDIT INI jika ukuran dasar berubah

        layeredPane.add(gifLabel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(buttonOverlayPanel, JLayeredPane.PALETTE_LAYER); // Tombol di atas GIF

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
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align within BoxLayout
        // HIGHLIGHT MULAI: ATUR UKURAN TOMBOL
        button.setMaximumSize(new Dimension(200, 50)); // <-- EDIT INI untuk lebar & tinggi maksimum tombol
        button.setPreferredSize(new Dimension(200, 50)); // <-- EDIT INI untuk lebar & tinggi pilihan tombol
        // Ukuran ini akan memastikan tombol memiliki lebar 200px dan tinggi 50px.
        // Anda bisa mengubah angka 200 (lebar) dan 50 (tinggi) sesuai keinginan Anda.
        // HIGHLIGHT AKHIR
        // HIGHLIGHT MULAI: ATUR FONT TOMBOL
        button.setFont(new Font("SansSerif", Font.BOLD, 18)); // <-- EDIT INI untuk ukuran font tombol
        // HIGHLIGHT AKHIR
        button.addActionListener(listener);
        // Optional: Custom button styling (e.g., remove border, change background)
        button.setFocusPainted(false);
        button.setBackground(new Color(200, 220, 255)); // Light blue
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        // Efek hover sederhana
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(150, 180, 255)); // Darker blue on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(200, 220, 255)); // Original color
            }
        });

        return button;
    }
}