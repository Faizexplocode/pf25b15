import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Board {
    public static final int ROWS = 3;
    public static final int COLS = 3;

    public static final int CANVAS_WIDTH = 400;
    public static final int CANVAS_HEIGHT = 500;

    public static final int GRID_WIDTH = 8;
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
    public static final Color COLOR_GRID = Color.LIGHT_GRAY;

    Cell[][] cells;
    private BufferedImage backgroundImage;

    public Board() {
        initGame();
        loadBackgroundImage();
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("images/board.jpg")); // pastikan path benar
        } catch (IOException e) {
            System.err.println("Gagal memuat background: " + e.getMessage());
        }
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

        if (cells[selectedRow][0].content == player && cells[selectedRow][1].content == player && cells[selectedRow][2].content == player || // horizontal
                cells[0][selectedCol].content == player && cells[1][selectedCol].content == player && cells[2][selectedCol].content == player || // vertical
                (selectedRow == selectedCol && cells[0][0].content == player && cells[1][1].content == player && cells[2][2].content == player) || // diagonal
                (selectedRow + selectedCol == 2 && cells[0][2].content == player && cells[1][1].content == player && cells[2][0].content == player)) { // anti-diagonal
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
        // Gambar latar belakang
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT, null);
        }

        // Gambar grid
        g.setColor(COLOR_GRID);
        int cellWidth = CANVAS_WIDTH / COLS;
        int cellHeight = CANVAS_HEIGHT / ROWS;

        for (int row = 1; row < ROWS; ++row) {
            g.fillRoundRect(0, cellHeight * row - GRID_WIDTH_HALF,
                    CANVAS_WIDTH, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
        }

        for (int col = 1; col < COLS; ++col) {
            g.fillRoundRect(cellWidth * col - GRID_WIDTH_HALF, 0,
                    GRID_WIDTH, CANVAS_HEIGHT, GRID_WIDTH, GRID_WIDTH);
        }

        // Gambar isi sel
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g);
            }
        }
    }
}
