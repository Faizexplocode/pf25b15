// Cell.java (UPDATED)
import java.awt.*;
import javax.swing.ImageIcon; // Perlu ini untuk ImageIcon

/**
 * The Cell class models each individual cell of the game board.
 */
public class Cell {
    // Define properties (package-visible)
    /** Content of this cell (Seed.NO_SEED, Seed.CROSS, or Seed.NOUGHT) */
    Seed content;
    /** Row and column of this cell */
    int row, col;

    // Tidak lagi ada SIZE di sini, akan ditentukan oleh Board
    // Padding dan SEED_SIZE juga akan dihitung dinamis di paint() atau diambil dari Board/GameMain

    /** Constructor to initialize this cell with the specified row and col */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        content = Seed.NO_SEED;
    }

    /** Reset this cell's content to EMPTY, ready for new game */
    public void newGame() {
        content = Seed.NO_SEED;
    }

    /** Paint itself on the graphics canvas, given the Graphics context */
    public void paint(Graphics g) {
        // Asumsi Board akan menyediakan Cell.ACTUAL_CELL_WIDTH dan Cell.ACTUAL_CELL_HEIGHT
        // Atau kita bisa mengestimasinya dari ukuran total papan.
        // Untuk saat ini, kita bisa mengambilnya dari Board.CANVAS_WIDTH/HEIGHT
        // Jika Board.CANVAS_WIDTH dan CANVAS_HEIGHT sudah diatur ke 400 dan 500,
        // maka lebar dan tinggi setiap sel bisa dihitung di sini.

        int cellWidth = Board.CANVAS_WIDTH / Board.COLS; // 400 / 3 = 133
        int cellHeight = Board.CANVAS_HEIGHT / Board.ROWS; // 500 / 3 = 166

        // Padding dinamis
        int paddingX = cellWidth / 8; // Sekitar 1/8 lebar sel
        int paddingY = cellHeight / 8; // Sekitar 1/8 tinggi sel

        int seedDrawWidth = cellWidth - 2 * paddingX;
        int seedDrawHeight = cellHeight - 2 * paddingY;


        // Draw the Seed if it is not empty
        int xDraw = col * cellWidth + paddingX;
        int yDraw = row * cellHeight + paddingY;

        if (content == Seed.CROSS || content == Seed.NOUGHT) {
            // Pastikan getImage() mengembalikan Image atau null
            Image img = content.getImage();
            if (img != null) {
                g.drawImage(img, xDraw, yDraw, seedDrawWidth, seedDrawHeight, null);
            } else {
                // Fallback jika gambar tidak dimuat
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(content == Seed.CROSS ? Color.RED : Color.BLUE);
                g2d.setFont(new Font("Arial", Font.BOLD, Math.min(seedDrawWidth, seedDrawHeight) / 2));
                String symbol = (content == Seed.CROSS) ? "X" : "O";
                FontMetrics fm = g2d.getFontMetrics();
                int textX = xDraw + (seedDrawWidth - fm.stringWidth(symbol)) / 2;
                int textY = yDraw + (fm.getAscent() + (seedDrawHeight - fm.getAscent() - fm.getDescent()) / 2);
                g2d.drawString(symbol, textX, textY);
            }
        }
    }
}