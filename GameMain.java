import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameMain extends JFrame {
    private static final long serialVersionUID = 1L;
    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar; // Deklarasi JLabel statusBar

    private boolean isVsAI = false;
    private String player1Name = "Player X";
    private String player2Name = "Player O";

    private CardLayout cardLayout;
    private JPanel containerPanel;

    private MainMenuPanel mainMenuPanel;
    private SubMenuPanel subMenuPanel;
    private GamePanelInternal gamePanelInternal; // Panel internal untuk game
    private JPanel instructionPanel;
    private JPanel gameOverPanel;
    private JLabel gameOverLabel;

    public GameMain() {
        SoundEffect.initGame(); // Inisialisasi semua efek suara

        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Atur ukuran frame awal. Ini akan di-override oleh pack() nanti,
        // tapi ini memberikan ukuran default yang baik.
        setSize(400, 600);
        setLocationRelativeTo(null); // Posisikan jendela di tengah layar

        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);

        // --- PENTING: Inisialisasi statusBar di sini, sebelum GamePanelInternal dibuat ---
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12)); // Border untuk statusBar

        // Inisialisasi semua panel
        mainMenuPanel = new MainMenuPanel(this); // Mengirim referensi GameMain
        subMenuPanel = new SubMenuPanel(this);   // Mengirim referensi GameMain
        gamePanelInternal = new GamePanelInternal(); // Panel yang akan menampilkan board dan status bar
        instructionPanel = createInstructionPanel(); // Buat panel instruksi
        gameOverPanel = createGameOverPanel();       // Buat panel game over

        // Tambahkan semua panel ke container utama dengan nama kartu unik
        containerPanel.add(mainMenuPanel, "MENU");
        containerPanel.add(subMenuPanel, "SUB_MENU");
        containerPanel.add(gamePanelInternal, "GAME");
        containerPanel.add(instructionPanel, "INSTRUCTION");
        containerPanel.add(gameOverPanel, "OVER");

        // Atur containerPanel sebagai content pane dari JFrame ini
        setContentPane(containerPanel);
        pack(); // Mengemas komponen-komponennya agar sesuai dengan preferred size
        setVisible(true); // Jadikan frame terlihat

        // Mulai dengan memutar suara selamat datang dan tampilkan menu utama
        SoundEffect.WELCOME.play();
        cardLayout.show(containerPanel, "MENU");
    }

    // --- Metode untuk Navigasi Antar Panel ---

    /** Menampilkan Main Menu Panel */
    public void showMainMenu() {
        SoundEffect.WELCOME.play();
        cardLayout.show(containerPanel, "MENU");
    }

    /** Menampilkan Sub Menu Panel (pilihan mode game) */
    public void showSubMenu() {
        cardLayout.show(containerPanel, "SUB_MENU");
    }

    /** Menampilkan Panel Instruksi */
    public void showInstructions() {
        cardLayout.show(containerPanel, "INSTRUCTION");
    }

    // --- Metode untuk Memulai Game ---

    /** Memulai permainan mode Player vs Player */
    public void startVsPlayer() {
        isVsAI = false;
        player1Name = JOptionPane.showInputDialog(this, "Masukkan nama untuk pemain X:", "Player X");
        if (player1Name == null || player1Name.isBlank()) player1Name = "Player X"; // Default jika kosong/cancel
        player2Name = JOptionPane.showInputDialog(this, "Masukkan nama untuk pemain O:", "Player O");
        if (player2Name == null || player2Name.isBlank()) player2Name = "Player O"; // Default jika kosong/cancel
        startGame();
    }

    /** Memulai permainan mode Player vs AI */
    public void startVsAI() {
        isVsAI = true;
        player1Name = JOptionPane.showInputDialog(this, "Masukkan nama Anda (X):", "Player X");
        if (player1Name == null || player1Name.isBlank()) player1Name = "Player X"; // Default jika kosong/cancel
        player2Name = "AI"; // Nama default untuk AI
        startGame();
    }

    /** Logika umum untuk memulai permainan baru */
    private void startGame() {
        SoundEffect.WELCOME.stop(); // Hentikan suara welcome jika masih diputar
        board = new Board(); // Inisialisasi papan permainan baru
        newGame();           // Reset status game
        cardLayout.show(containerPanel, "GAME"); // Tampilkan panel game
        gamePanelInternal.repaint(); // Penting: pastikan papan tergambar ulang
    }

    /** Mereset status game untuk permainan baru */
    public void newGame() {
        for (int r = 0; r < Board.ROWS; ++r)
            for (int c = 0; c < Board.COLS; ++c)
                board.cells[r][c].content = Seed.NO_SEED; // Kosongkan semua sel
        currentPlayer = Seed.CROSS; // Set pemain pertama ke X
        currentState = State.PLAYING; // Set status game ke bermain
    }

    /** Memeriksa apakah langkah yang dipilih valid */
    private boolean isValidMove(int row, int col) {
        // Memastikan board tidak null dan sel berada dalam batas serta kosong
        return board != null && row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS &&
                board.cells[row][col].content == Seed.NO_SEED;
    }

    /** Memainkan langkah yang dipilih oleh pemain/AI */
    private void playMove(int row, int col) {
        currentState = board.stepGame(currentPlayer, row, col); // Update papan dan dapatkan status baru
        if (currentPlayer == Seed.CROSS) {
            SoundEffect.CROSS_PLAY.play(); // Putar suara untuk langkah X
        } else {
            SoundEffect.NOUGHT_PLAY.play(); // Putar suara untuk langkah O
        }

        if (currentState != State.PLAYING) {
            SoundEffect.DIE.play(); // Putar suara jika game selesai
            showPopupWinner();      // Tampilkan popup pemenang/seri
        }
        // Ganti pemain setelah langkah
        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
    }

    /** Logika AI untuk membuat langkah (sangat sederhana: mencari sel kosong pertama) */
    private void aiMove() {
        for (int r = 0; r < Board.ROWS; r++) {
            for (int c = 0; c < Board.COLS; c++) {
                if (isValidMove(r, c)) { // Temukan sel kosong pertama yang valid
                    playMove(r, c);      // Lakukan langkah AI
                    return;              // Keluar setelah langkah pertama
                }
            }
        }
    }

    /** Menampilkan popup dialog dengan hasil permainan */
    private void showPopupWinner() {
        String msg = switch (currentState) {
            case DRAW -> "Permainan berakhir seri!";
            case CROSS_WON -> "Selamat! " + player1Name + " (X) menang!";
            case NOUGHT_WON -> isVsAI ? "AI menang!" : "Selamat! " + player2Name + " (O) menang!";
            default -> ""; // Kasus default, seharusnya tidak tercapai jika currentState selalu valid
        };
        JOptionPane.showMessageDialog(this, msg, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        showGameOver(); // Setelah popup, tampilkan panel Game Over
    }

    /** Menampilkan Panel Game Over */
    private void showGameOver() {
        String msg = switch (currentState) {
            case DRAW -> "It's a Draw!";
            case CROSS_WON -> player1Name + " (X) Won!";
            case NOUGHT_WON -> player2Name + " (O) Won!";
            default -> "";
        };
        gameOverLabel.setText(msg); // Atur teks label di panel game over
        cardLayout.show(containerPanel, "OVER"); // Tampilkan panel game over
    }

    // --- Inner class untuk panel game yang sebenarnya (tempat papan digambar) ---
    /**
     * GamePanelInternal adalah JPanel yang bertanggung jawab untuk menampilkan papan permainan
     * dan status bar. Ini adalah komponen tempat event mouse untuk permainan ditangani
     * dan juga tempat logika penggambaran papan terjadi.
     */
    private class GamePanelInternal extends JPanel {
        GamePanelInternal() {
            // Set preferredSize untuk panel ini. Ini akan menjadi ukuran "canvas" untuk papan.
            // Tinggi total = Tinggi Board + Tinggi StatusBar
            setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + statusBar.getPreferredSize().height));
            setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2)); // Border di sekeliling area game
            setLayout(new BorderLayout()); // Gunakan BorderLayout untuk menempatkan statusBar di PAGE_END

            // Tambahkan statusBar ke GamePanelInternal ini
            add(statusBar, BorderLayout.PAGE_END);

            // Tambahkan MouseListener ke panel ini untuk mendeteksi klik pada papan
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Hanya proses klik jika game sedang berlangsung
                    if (currentState == State.PLAYING) {
                        // Hitung baris dan kolom berdasarkan koordinat mouse dan ukuran sel
                        int row = e.getY() / (Board.CANVAS_HEIGHT / Board.ROWS); // y-coord / tinggi per sel
                        int col = e.getX() / (Board.CANVAS_WIDTH / Board.COLS); // x-coord / lebar per sel

                        if (isValidMove(row, col)) {
                            playMove(row, col); // Lakukan langkah
                            repaint(); // Gambar ulang panel setelah langkah pemain

                            // Jika game masih berjalan dan mode AI, biarkan AI bergerak
                            if (currentState == State.PLAYING && isVsAI && currentPlayer == Seed.NOUGHT) {
                                aiMove();
                                repaint(); // Gambar ulang setelah langkah AI
                            }
                        }
                    } else {
                        // Jika game sudah selesai (DRAW, CROSS_WON, NOUGHT_WON), klik akan menampilkan layar Game Over
                        showGameOver();
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(COLOR_BG); // Set warna latar belakang panel

            // Gambar papan permainan jika sudah diinisialisasi
            if (board != null) {
                board.paint(g); // Delegasikan penggambaran papan ke objek Board
            }

            // Update teks dan warna status bar berdasarkan currentState
            if (currentState == State.PLAYING) {
                statusBar.setForeground(Color.BLACK);
                statusBar.setText((currentPlayer == Seed.CROSS) ? player1Name + " (X)'s Turn" : player2Name + " (O)'s Turn");
            } else if (currentState == State.DRAW) {
                statusBar.setForeground(Color.RED);
                statusBar.setText("It's a Draw! Click to continue.");
            } else if (currentState == State.CROSS_WON) {
                statusBar.setForeground(Color.RED);
                statusBar.setText(player1Name + " (X) Won! Click to continue.");
            } else if (currentState == State.NOUGHT_WON) {
                statusBar.setForeground(Color.RED);
                statusBar.setText(player2Name + " (O) Won! Click to continue.");
            }
        }
    }


    // --- Metode Pembantu untuk Membuat Panel Lain ---

    /** Membuat dan mengembalikan panel untuk instruksi permainan */
    private JPanel createInstructionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea instructions = new JTextArea("""
                Aturan Permainan:
                1. Dua pemain atau lawan AI.
                2. Pemain pertama menggunakan 'X', pemain kedua atau AI menggunakan 'O'.
                3. Giliran bergantian menandai kotak di papan 3x3.
                4. Tiga simbol yang sejajar secara horizontal, vertikal, atau diagonal memenangkan permainan.
                5. Jika papan penuh tanpa ada pemenang, maka hasilnya seri.
                """);
        instructions.setEditable(false);
        instructions.setWrapStyleWord(true); // Mengatur wrap kata
        instructions.setLineWrap(true);       // Mengatur wrap baris
        instructions.setMargin(new Insets(10, 10, 10, 10)); // Padding teks
        instructions.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Font untuk teks instruksi

        JButton back = new JButton("Back to Menu");
        back.addActionListener(e -> {
            SoundEffect.WELCOME.play(); // Putar suara welcome saat kembali ke menu
            cardLayout.show(containerPanel, "MENU"); // Tampilkan menu utama
        });

        panel.add(new JScrollPane(instructions), BorderLayout.CENTER); // Teks instruksi di-scrollable
        panel.add(back, BorderLayout.SOUTH); // Tombol "Back" di bagian bawah
        return panel;
    }

    /** Membuat dan mengembalikan panel Game Over */
    private JPanel createGameOverPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10)); // GridLayout untuk tombol
        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60)); // Padding

        gameOverLabel = new JLabel("", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("SansSerif", Font.BOLD, 24)); // Font untuk pesan game over

        JButton again = new JButton("Play Again");
        again.addActionListener(e -> startGame()); // Memulai game baru

        JButton back = new JButton("Back to Menu");
        back.addActionListener(e -> {
            SoundEffect.WELCOME.play(); // Putar suara welcome saat kembali ke menu
            cardLayout.show(containerPanel, "MENU"); // Tampilkan menu utama
        });

        JButton exit = new JButton("Exit Game");
        exit.addActionListener(e -> System.exit(0)); // Keluar dari aplikasi

        panel.add(gameOverLabel);
        panel.add(again);
        panel.add(back);
        panel.add(exit);
        return panel;
    }

    /** Metode utama untuk menjalankan aplikasi */
    public static void main(String[] args) {
        // Menjalankan GUI di Event Dispatch Thread (EDT) untuk keamanan thread
        SwingUtilities.invokeLater(GameMain::new);
    }
}