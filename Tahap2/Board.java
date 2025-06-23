package Tahap2;
/**
 * The Board class models the TTT game-board of 3x3 cells.
 */
public class Board {  // save as "Board.java"
    // Define named constants for the grid
    public static final int ROWS = 3;
    public static final int COLS = 3;

    // Define properties (package-visible)
    /** A board composes of [ROWS]x[COLS] Cell instances */
    Cell[][] cells;

    /** Constructor to initialize the game board */
    public Board() {
        initGame();
    }

    /** Initialize the board (run once) */
    public void initGame() {
        cells = new Cell[ROWS][COLS];  // allocate the array
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                // Allocate element of the array
                cells[row][col] = new Cell(row, col);
            }
        }
    }

    /** Reset the contents of the game board, ready for new game. */
    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].newGame();  // The cells init itself
            }
        }
    }

    /**
     * The given player makes a move on (selectedRow, selectedCol).
     * Update cells[selectedRow][selectedCol]. Compute and return the
     * new game state (PLAYING, DRAW, CROSS_WON, NOUGHT_WON).
     */
    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        // Update game board
        cells[selectedRow][selectedCol].content = player;

        // Compute and return the new game state
        if (cells[selectedRow][0].content == player   // 3-in-the-row
                && cells[selectedRow][1].content == player
                && cells[selectedRow][2].content == player
                || cells[0][selectedCol].content == player // 3-in-the-column
                && cells[1][selectedCol].content == player
                && cells[2][selectedCol].content == player
                || selectedRow == selectedCol       // 3-in-the-diagonal
                && cells[0][0].content == player
                && cells[1][1].content == player
                && cells[2][2].content == player
                || selectedRow + selectedCol == 2    // 3-in-the-opposite-diagonal
                && cells[0][2].content == player
                && cells[1][1].content == player
                && cells[2][0].content == player) {
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        } else {
            // Nobody win. Check for DRAW (all cells occupied) or PLAYING.
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    if (cells[row][col].content == Seed.NO_SEED) {
                        return State.PLAYING; // still have empty cells
                    }
                }
            }
            return State.DRAW; // no empty cell, it's a draw
        }
    }

    /** The board paints itself */
    public void paint() {
        // ANSI escape codes for colors (re-defined here for Board class's independent painting)
        final String YELLOW = "\u001B[33m";
        final String RESET = "\u001B[0m";
        final String BLUE = "\u001B[34m"; // For 'X'
        final String RED = "\u001B[31m";  // For 'O'

        System.out.println(YELLOW + "\n╔═══════════╗" + RESET);
        System.out.println(YELLOW + "║   BOARD!  ║" + RESET);
        System.out.println(YELLOW + "╠═══════════╣" + RESET);
        for (int row = 0; row < ROWS; ++row) {
            System.out.print(YELLOW + "║" + RESET); // Left border of the frame
            for (int col = 0; col < COLS; ++col) {
                // Modified paint logic to use colors directly here,
                // as Cell.paint() doesn't have access to these color constants.
                switch (cells[row][col].content) {
                    case CROSS:   System.out.print(BLUE + " X " + RESET); break;
                    case NOUGHT:  System.out.print(RED + " O " + RESET); break;
                    case NO_SEED: System.out.print("   "); break;
                }
                if (col < COLS - 1) System.out.print(YELLOW + "|" + RESET);  // column separator
            }
            System.out.println(YELLOW + "║" + RESET); // Right border of the frame
            if (row < ROWS - 1) {
                System.out.println(YELLOW + "╠═══╬═══╬═══╣" + RESET);  // row separator
            }
        }
        System.out.println(YELLOW + "╚═══════════╝" + RESET);
        System.out.println();
    }
}