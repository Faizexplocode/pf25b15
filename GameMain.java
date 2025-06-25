import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameMain extends JFrame {
    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;

    private boolean isVsAI = false;
    private String player1Name = "Player X";
    private String player2Name = "Player O";

    private CardLayout cardLayout;
    private JPanel containerPanel;

    private MainMenuPanel mainMenuPanel;
    private SubMenuPanel subMenuPanel;
    private GamePanelInternal gamePanelInternal;
    private JPanel instructionPanel;
    private JPanel gameOverPanel;
    private JLabel gameOverLabel;

    public GameMain() {
        SoundEffect.initGame();

        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);

        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        mainMenuPanel = new MainMenuPanel(this);
        subMenuPanel = new SubMenuPanel(this);
        gamePanelInternal = new GamePanelInternal();
        instructionPanel = createInstructionPanel();
        gameOverPanel = createGameOverPanel();

        containerPanel.add(mainMenuPanel, "MENU");
        containerPanel.add(subMenuPanel, "SUB_MENU");
        containerPanel.add(gamePanelInternal, "GAME");
        containerPanel.add(instructionPanel, "INSTRUCTION");
        containerPanel.add(gameOverPanel, "OVER");

        setContentPane(containerPanel);
        pack();
        setVisible(true);

        showMainMenu();
    }

    public void showMainMenu() {
        SoundEffect.WELCOME.play();
        cardLayout.show(containerPanel, "MENU");
    }

    public void showSubMenu() {
        SoundEffect.WELCOME.stop();
        cardLayout.show(containerPanel, "SUB_MENU");
    }

    public void showInstructions() {
        SoundEffect.WELCOME.stop();
        cardLayout.show(containerPanel, "INSTRUCTION");
    }

    public void startVsPlayer() {
        isVsAI = false;
        player1Name = JOptionPane.showInputDialog(this, "Masukkan nama untuk pemain X:", "Player X");
        if (player1Name == null || player1Name.isBlank()) player1Name = "Player X";
        player2Name = JOptionPane.showInputDialog(this, "Masukkan nama untuk pemain O:", "Player O");
        if (player2Name == null || player2Name.isBlank()) player2Name = "Player O";
        startGame();
    }

    public void startVsAI() {
        isVsAI = true;
        player1Name = JOptionPane.showInputDialog(this, "Masukkan nama Anda (X):", "Player X");
        if (player1Name == null || player1Name.isBlank()) player1Name = "Player X";
        player2Name = "AI";
        startGame();
    }

    private void startGame() {
        SoundEffect.WELCOME.stop();
        board = new Board();
        newGame();
        cardLayout.show(containerPanel, "GAME");
        gamePanelInternal.repaint();
    }

    public void newGame() {
        for (int r = 0; r < Board.ROWS; ++r)
            for (int c = 0; c < Board.COLS; ++c)
                board.cells[r][c].content = Seed.NO_SEED;
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
    }

    private boolean isValidMove(int row, int col) {
        return board != null && row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS &&
                board.cells[row][col].content == Seed.NO_SEED;
    }

    private void playMove(int row, int col) {
        currentState = board.stepGame(currentPlayer, row, col);
        if (currentPlayer == Seed.CROSS) {
            SoundEffect.CROSS_PLAY.play();
        } else {
            SoundEffect.NOUGHT_PLAY.play();
        }

        if (currentState != State.PLAYING) {
            SoundEffect.DIE.play();
            showPopupWinner();
        }
        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
    }

    private void aiMove() {
        for (int r = 0; r < Board.ROWS; r++) {
            for (int c = 0; c < Board.COLS; c++) {
                if (isValidMove(r, c)) {
                    playMove(r, c);
                    return;
                }
            }
        }
    }

    private void showPopupWinner() {
        String msg = switch (currentState) {
            case DRAW -> "Seri!";
            case CROSS_WON -> "Selamat! " + player1Name + " (X) menang!";
            case NOUGHT_WON -> isVsAI ? "AI menang!" : "Selamat! " + player2Name + " (O) menang!";
            default -> "";
        };
        JOptionPane.showMessageDialog(this, msg, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        showGameOver();
    }

    private void showGameOver() {
        String msg = switch (currentState) {
            case DRAW -> "It's a Draw!";
            case CROSS_WON -> player1Name + " (X) Won!";
            case NOUGHT_WON -> player2Name + " (O) Won!";
            default -> "";
        };
        gameOverLabel.setText(msg);
        cardLayout.show(containerPanel, "OVER");
    }

    private class GamePanelInternal extends JPanel {
        GamePanelInternal() {
            setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + statusBar.getPreferredSize().height));
            setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2));
            setLayout(new BorderLayout());

            // Panel atas kanan (tombol petunjuk)
            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
            topPanel.setOpaque(false);

            JButton btnHelp = new JButton("?");
            btnHelp.setFont(new Font("SansSerif", Font.BOLD, 14));
            btnHelp.setMargin(new Insets(2, 8, 2, 8));
            btnHelp.setFocusPainted(false);
            btnHelp.setToolTipText("Petunjuk permainan");
            btnHelp.addActionListener(e -> {
                JOptionPane.showMessageDialog(GameMain.this, """
                    📜 Petunjuk Permainan:

                    1. Dua pemain atau lawan AI.
                    2. Pemain pertama: X, pemain kedua: O.
                    3. Klik kotak untuk menandai giliranmu.
                    4. 3 simbol sejajar = Menang!
                    5. Papan penuh tanpa pemenang = Seri.
                    """, "Petunjuk", JOptionPane.INFORMATION_MESSAGE);
            });

            topPanel.add(btnHelp);
            add(topPanel, BorderLayout.NORTH);
            add(statusBar, BorderLayout.SOUTH);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (currentState == State.PLAYING) {
                        int row = e.getY() / (Board.CANVAS_HEIGHT / Board.ROWS);
                        int col = e.getX() / (Board.CANVAS_WIDTH / Board.COLS);

                        if (isValidMove(row, col)) {
                            playMove(row, col);
                            repaint();

                            if (currentState == State.PLAYING && isVsAI && currentPlayer == Seed.NOUGHT) {
                                aiMove();
                                repaint();
                            }
                        }
                    } else {
                        showGameOver();
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(COLOR_BG);
            if (board != null) board.paint(g);

            if (statusBar != null) {
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
    }

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
        instructions.setWrapStyleWord(true);
        instructions.setLineWrap(true);
        instructions.setMargin(new Insets(10, 10, 10, 10));
        instructions.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton back = new JButton("Back to Menu");
        back.addActionListener(e -> showMainMenu());

        panel.add(new JScrollPane(instructions), BorderLayout.CENTER);
        panel.add(back, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createGameOverPanel() {
        JPanel panel = new JPanel() {
            private Image backgroundImage = new ImageIcon("images/gambar.gif").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new GridBagLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        gameOverLabel = new JLabel("", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        gameOverLabel.setForeground(Color.WHITE);
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameOverLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JButton again = new JButton("Play Again");
        again.setAlignmentX(Component.CENTER_ALIGNMENT);
        again.setMaximumSize(new Dimension(200, 40));
        again.addActionListener(e -> startGame());

        JButton back = new JButton("Back to Menu");
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.setMaximumSize(new Dimension(200, 40));
        back.addActionListener(e -> showMainMenu());

        JButton exit = new JButton("Exit Game");
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.setMaximumSize(new Dimension(200, 40));
        exit.addActionListener(e -> System.exit(0));

        centerPanel.add(gameOverLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(again);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(back);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(exit);

        panel.add(centerPanel);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameMain::new);
    }
}
