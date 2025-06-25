// === GameMain.java ===

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameMain extends JPanel {
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
    private JPanel gameOverPanel, gamePanel, instructionPanel;
    private JLabel gameOverLabel;

    public GameMain() {
        SoundEffect.initGame();

        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);

        // Panel-panel utama
        MainMenuPanel mainMenuPanel = new MainMenuPanel(this);
        instructionPanel = createInstructionPanel();
        gamePanel = createGamePanel();
        gameOverPanel = createGameOverPanel();

        // Tambah ke container utama
        containerPanel.add(mainMenuPanel, "MENU");
        containerPanel.add(instructionPanel, "INSTRUCTION");
        containerPanel.add(gamePanel, "GAME");
        containerPanel.add(gameOverPanel, "OVER");

        JFrame frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(containerPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        SoundEffect.WELCOME.play();
        cardLayout.show(containerPanel, "MENU");
    }

    private JPanel createInstructionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea instructions = new JTextArea("""
                Aturan Permainan:
                1. Dua pemain atau lawan AI.
                2. X dan O bergantian.
                3. Tiga sejajar menang.
                4. Full board = seri.
                """);
        instructions.setEditable(false);
        instructions.setWrapStyleWord(true);
        instructions.setLineWrap(true);
        instructions.setMargin(new Insets(10, 10, 10, 10));

        JButton back = new JButton("Back to Menu");
        back.addActionListener(e -> {
            SoundEffect.WELCOME.play();
            cardLayout.show(containerPanel, "MENU");
        });

        panel.add(new JScrollPane(instructions), BorderLayout.CENTER);
        panel.add(back, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createGamePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);

        this.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        this.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2));
        this.setLayout(new BorderLayout());
        this.add(statusBar, BorderLayout.PAGE_END);

        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (currentState == State.PLAYING) {
                    int row = e.getY() / Cell.SIZE;
                    int col = e.getX() / Cell.SIZE;
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

        panel.add(this, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createGameOverPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        gameOverLabel = new JLabel("", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        JButton again = new JButton("Play Again");
        again.addActionListener(e -> startGame());

        JButton back = new JButton("Back to Menu");
        back.addActionListener(e -> {
            SoundEffect.WELCOME.play();
            cardLayout.show(containerPanel, "MENU");
        });

        JButton exit = new JButton("Exit Game");
        exit.addActionListener(e -> System.exit(0));

        panel.add(gameOverLabel);
        panel.add(again);
        panel.add(back);
        panel.add(exit);
        return panel;
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

    public void showInstructions() {
        cardLayout.show(containerPanel, "INSTRUCTION");
    }

    private void startGame() {
        SoundEffect.WELCOME.stop();
        board = new Board();
        newGame();
        cardLayout.show(containerPanel, "GAME");
    }

    public void newGame() {
        for (int r = 0; r < Board.ROWS; ++r)
            for (int c = 0; c < Board.COLS; ++c)
                board.cells[r][c].content = Seed.NO_SEED;
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
    }

    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS &&
                board.cells[row][col].content == Seed.NO_SEED;
    }

    private void playMove(int row, int col) {
        currentState = board.stepGame(currentPlayer, row, col);
        if (currentPlayer == Seed.CROSS) SoundEffect.CROSS_PLAY.play();
        else SoundEffect.NOUGHT_PLAY.play();

        if (currentState != State.PLAYING) {
            SoundEffect.DIE.play();
            showPopupWinner();
        }
        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
    }

    private void aiMove() {
        for (int r = 0; r < Board.ROWS; r++)
            for (int c = 0; c < Board.COLS; c++)
                if (isValidMove(r, c)) {
                    playMove(r, c);
                    return;
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);
        if (board != null) board.paint(g);

        if (currentState == State.PLAYING)
            statusBar.setText(currentPlayer == Seed.CROSS ? player1Name + " (X)'s Turn" : player2Name + " (O)'s Turn");
        else if (currentState == State.DRAW)
            statusBar.setText("It's a Draw! Click to continue.");
        else
            statusBar.setText((currentPlayer == Seed.CROSS ? player1Name : player2Name) + " Won! Click to continue.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameMain::new);
    }
}
