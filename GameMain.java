import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L;
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
    private JPanel gameOverPanel, gamePanel, instructionPanel, mainMenuPanel;
    private JLabel gameOverLabel;

    public GameMain() {
        SoundEffect.initGame();
        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);

        mainMenuPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        mainMenuPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        JLabel titleLabel = new JLabel("Tic Tac Toe", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        JButton btnPlayer = new JButton("Play vs Player");
        JButton btnAI = new JButton("Play vs AI");
        JButton btnInstructions = new JButton("Petunjuk Permainan");
        JButton btnExit = new JButton("Exit");

        mainMenuPanel.add(titleLabel);
        mainMenuPanel.add(btnPlayer);
        mainMenuPanel.add(btnAI);
        mainMenuPanel.add(btnInstructions);
        mainMenuPanel.add(btnExit);

        instructionPanel = new JPanel(new BorderLayout());
        JTextArea instructions = new JTextArea("""
                Aturan Permainan Tic Tac Toe:
                1. Permainan dimainkan oleh 2 pemain atau melawan AI.
                2. Pemain pertama menggunakan 'X', pemain kedua atau AI menggunakan 'O'.
                3. Giliran bergantian menandai kotak di papan 3x3.
                4. Tiga simbol yang sejajar secara horizontal, vertikal, atau diagonal menang.
                5. Jika papan penuh tanpa pemenang, maka hasilnya seri.
                """);
        instructions.setEditable(false);
        instructions.setLineWrap(true);
        instructions.setWrapStyleWord(true);
        instructions.setMargin(new Insets(10, 10, 10, 10));
        JButton backToMenu = new JButton("Back to Menu");
        instructionPanel.add(new JScrollPane(instructions), BorderLayout.CENTER);
        instructionPanel.add(backToMenu, BorderLayout.SOUTH);

        gamePanel = new JPanel(new BorderLayout());
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));
        this.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        this.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));
        this.setLayout(new BorderLayout());
        this.add(statusBar, BorderLayout.PAGE_END);
        gamePanel.add(this, BorderLayout.CENTER);

        gameOverPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        gameOverPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        gameOverLabel = new JLabel("", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        JButton btnPlayAgain = new JButton("Play Again");
        JButton btnBackToMenu = new JButton("Back to Menu");
        JButton btnExitFromOver = new JButton("Exit Game");

        gameOverPanel.add(gameOverLabel);
        gameOverPanel.add(btnPlayAgain);
        gameOverPanel.add(btnBackToMenu);
        gameOverPanel.add(btnExitFromOver);

        containerPanel.add(mainMenuPanel, "MENU");
        containerPanel.add(instructionPanel, "INSTRUCTION");
        containerPanel.add(gamePanel, "GAME");
        containerPanel.add(gameOverPanel, "OVER");

        btnPlayer.addActionListener(e -> {
            isVsAI = false;
            player1Name = JOptionPane.showInputDialog(this, "Masukkan nama untuk pemain X:", "Player X");
            if (player1Name == null || player1Name.isBlank()) player1Name = "Player X";
            player2Name = JOptionPane.showInputDialog(this, "Masukkan nama untuk pemain O:", "Player O");
            if (player2Name == null || player2Name.isBlank()) player2Name = "Player O";
            startGame();
        });

        btnAI.addActionListener(e -> {
            isVsAI = true;
            player1Name = JOptionPane.showInputDialog(this, "Masukkan nama Anda (X):", "Player X");
            if (player1Name == null || player1Name.isBlank()) player1Name = "Player X";
            player2Name = "AI";
            startGame();
        });

        btnInstructions.addActionListener(e -> cardLayout.show(containerPanel, "INSTRUCTION"));
        btnExit.addActionListener(e -> System.exit(0));
        backToMenu.addActionListener(e -> {
            SoundEffect.WELCOME.play();
            cardLayout.show(containerPanel, "MENU");
        });

        btnPlayAgain.addActionListener(e -> startGame());
        btnBackToMenu.addActionListener(e -> {
            SoundEffect.WELCOME.play();
            cardLayout.show(containerPanel, "MENU");
        });
        btnExitFromOver.addActionListener(e -> System.exit(0));

        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (currentState == State.PLAYING) {
                    int mouseX = e.getX();
                    int mouseY = e.getY();
                    int row = mouseY / Cell.SIZE;
                    int col = mouseX / Cell.SIZE;

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

        JFrame frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(containerPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        SoundEffect.WELCOME.play();
        cardLayout.show(containerPanel, "MENU");
    }

    private void startGame() {
        SoundEffect.WELCOME.stop();
        initGame();
        newGame();
        cardLayout.show(containerPanel, "GAME");
    }

    public void initGame() {
        board = new Board();
    }

    public void newGame() {
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                board.cells[row][col].content = Seed.NO_SEED;
            }
        }
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
    }

    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS &&
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
        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                if (isValidMove(row, col)) {
                    playMove(row, col);
                    return;
                }
            }
        }
    }

    private void showGameOver() {
        String message = switch (currentState) {
            case DRAW -> "It's a Draw!";
            case CROSS_WON -> player1Name + " (X) Won!";
            case NOUGHT_WON -> player2Name + " (O) Won!";
            default -> "";
        };
        gameOverLabel.setText(message);
        cardLayout.show(containerPanel, "OVER");
    }

    private void showPopupWinner() {
        String message = switch (currentState) {
            case DRAW -> "Permainan berakhir seri!";
            case CROSS_WON -> "Selamat! " + player1Name + " (X) menang!";
            case NOUGHT_WON -> isVsAI ? "AI menang!" : "Selamat! " + player2Name + " (O) menang!";
            default -> "";
        };
        JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        showGameOver();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);
        if (board != null) board.paint(g);

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameMain::new);
    }
}
