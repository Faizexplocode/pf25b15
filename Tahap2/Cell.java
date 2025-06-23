package Tahap2;
/**
 * The Cell class models each individual cell of the TTT 3x3 grid.
 */
public class Cell {  // save as "Cell.java"
    // Define properties (package-visible)
    /** Content of this cell (CROSS, NOUGHT, NO_SEED) */
    Seed content;
    /** Row and column of this cell, not used in this program */
    int row, col;

    /** Constructor to initialize this cell */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.content = Seed.NO_SEED;
    }

    /** Reset the cell content to EMPTY, ready for a new game. */
    public void newGame() {
        this.content = Seed.NO_SEED;
    }

    /** The cell paints itself */
    public void paint() {
        // This paint method is now simplified.
        // The actual color application happens in Board.paint() for full control of the board's look.
        String icon = this.content.getIcon();
        System.out.print(icon);
    }
}