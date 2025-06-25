// Board.java
import java.awt.*;

public class Board {
    public static final int ROWS = 3;
    public static final int COLS = 3;

    // Define CANVAS_WIDTH dan CANVAS_HEIGHT secara eksplisit
    public static final int CANVAS_WIDTH = 400;  // Lebar papan permainan
    public static final int CANVAS_HEIGHT = 500; // Tinggi papan permainan

    public static final int GRID_WIDTH = 8;
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
    public static final Color COLOR_GRID = Color.LIGHT_GRAY;

    Cell[][] cells;

    public Board() {
        initGame();
    }

    public void initGame() {
        cells = new Cell[ROWS][COLS];
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col] = new Cell(row, col);
            }
        }
    }

    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].newGame();
            }
        }
    }

    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        cells[selectedRow][selectedCol].content = player;

        if (cells[selectedRow][0].content == player && cells[selectedRow][1].content == player && cells[selectedRow][2].content == player || // 3-in-the-row
                cells[0][selectedCol].content == player && cells[1][selectedCol].content == player && cells[2][selectedCol].content == player || // 3-in-the-column
                (selectedRow == selectedCol && cells[0][0].content == player && cells[1][1].content == player && cells[2][2].content == player) || // 3-in-the-diagonal
                (selectedRow + selectedCol == 2 && cells[0][2].content == player && cells[1][1].content == player && cells[2][0].content == player)) { // 3-in-the-opposite-diagonal
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        } else {
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    if (cells[row][col].content == Seed.NO_SEED) {
                        return State.PLAYING;
                    }
                }
            }
            return State.DRAW;
        }
    }

    public void paint(Graphics g) {
        g.setColor(COLOR_GRID);

        // Hitung lebar dan tinggi sel secara dinamis untuk penggambaran grid
        int cellWidth = CANVAS_WIDTH / COLS;
        int cellHeight = CANVAS_HEIGHT / ROWS;

        for (int row = 1; row < ROWS; ++row) {
            g.fillRoundRect(0, cellHeight * row - GRID_WIDTH_HALF,
                    CANVAS_WIDTH, GRID_WIDTH,
                    GRID_WIDTH, GRID_WIDTH);
        }
        for (int col = 1; col < COLS; ++col) {
            g.fillRoundRect(cellWidth * col - GRID_WIDTH_HALF, 0,
                    GRID_WIDTH, CANVAS_HEIGHT,
                    GRID_WIDTH, GRID_WIDTH);
        }

        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g);
            }
        }
    }
}