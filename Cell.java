// Cell.java
import java.awt.*;
import javax.swing.ImageIcon;

public class Cell {
    Seed content;
    int row, col;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        content = Seed.NO_SEED;
    }

    public void newGame() {
        content = Seed.NO_SEED;
    }

    public void paint(Graphics g) {
        // Hitung lebar dan tinggi sel berdasarkan total ukuran papan dan jumlah baris/kolom
        int cellWidth = Board.CANVAS_WIDTH / Board.COLS;
        int cellHeight = Board.CANVAS_HEIGHT / Board.ROWS;

        // Padding untuk gambar X/O di dalam sel
        int paddingX = cellWidth / 8;
        int paddingY = cellHeight / 8;

        int seedDrawWidth = cellWidth - 2 * paddingX;
        int seedDrawHeight = cellHeight - 2 * paddingY;

        int xDraw = col * cellWidth + paddingX;
        int yDraw = row * cellHeight + paddingY;

        if (content == Seed.CROSS || content == Seed.NOUGHT) {
            Image img = content.getImage();
            if (img != null) {
                g.drawImage(img, xDraw, yDraw, seedDrawWidth, seedDrawHeight, null);
            } else {
                // Fallback: Jika gambar tidak dimuat, gambar simbol teks
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(content == Seed.CROSS ? Color.RED : Color.BLUE);
                g2d.setFont(new Font("Arial", Font.BOLD, Math.min(seedDrawWidth, seedDrawHeight) / 2));
                String symbol = content.getDisplayName(); // Ambil dari displayName enum Seed
                FontMetrics fm = g2d.getFontMetrics();
                int textX = xDraw + (seedDrawWidth - fm.stringWidth(symbol)) / 2;
                int textY = yDraw + (fm.getAscent() + (seedDrawHeight - fm.getAscent() - fm.getDescent()) / 2);
                g2d.drawString(symbol, textX, textY);
            }
        }
    }
}