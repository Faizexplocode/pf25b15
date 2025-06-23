package Tahap2;
import java.util.Scanner;

/**
 * The main class for the Tic-Tac-Toe (Console-OO, non-graphics version)
 * It acts as the overall controller of the game.
 */
public class GameMain {
    // Define properties
    /** The game board */
    private Board board;
    /** The current state of the game (of enum State) */
    private State currentState;
    /** The current player (of enum Seed) */
    private Seed  currentPlayer;

    private static Scanner in = new Scanner(System.in);

    // ANSI escape codes for colors
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String ORANGE = "\u001B[38;5;208m"; // Contoh warna orange

    /** Constructor to setup the game */
    public GameMain() {
        // Perform one-time initialization tasks
        initGame();

        // Reset the board, currentStatus and currentPlayer
        newGame();

        // Play the game once
        do {
            // The currentPlayer makes a move.
            // Update cells[][] and currentState
            stepGame();
            // Refresh the display
            board.paint(); // Board class now handles its own full painting, including colors
            // Print message if game over
            if (currentState == State.CROSS_WON) {
                System.out.println(GREEN + "====================================" + RESET);
                System.out.println(GREEN + "           'X' MENANG! 🎉           " + RESET);
                System.out.println(GREEN + "====================================" + RESET);
            } else if (currentState == State.NOUGHT_WON) {
                System.out.println(GREEN + "====================================" + RESET);
                System.out.println(GREEN + "           'O' MENANG! 🎉           " + RESET);
                System.out.println(GREEN + "====================================" + RESET);
            } else if (currentState == State.DRAW) {
                System.out.println(YELLOW + "====================================" + RESET);
                System.out.println(YELLOW + "             SERI! 🤝             " + RESET);
                System.out.println(YELLOW + "====================================" + RESET);
            }
            // Switch currentPlayer
            currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
        } while (currentState == State.PLAYING);  // repeat until game over
    } // End of GameMain constructor

    /** Perform one-time initialization tasks */
    public void initGame() {
        board = new Board();  // allocate game-board
    }

    /** Reset the game-board contents and the current states, ready for new game */
    public void newGame() {
        board.newGame();  // clear the board contents
        currentPlayer = Seed.CROSS;    // CROSS plays first
        currentState = State.PLAYING; // ready to play
    }

    /** The currentPlayer makes one move.
     Update cells[][] and currentState. */
    public void stepGame() {
        boolean validInput = false;  // for validating input
        do {
            String icon = currentPlayer.getIcon();
            // Apply color to player prompt
            String playerColor = (currentPlayer == Seed.CROSS) ? BLUE : RED;
            System.out.print(playerColor + "Player '" + icon + "' " + ((currentPlayer == Seed.CROSS) ? "🔵" : "🔴") + ", enter your move (row[1-3] column[1-3]): " + RESET);

            int row = in.nextInt() - 1;    // [0-2]
            int col = in.nextInt() - 1;
            if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                    && board.cells[row][col].content == Seed.NO_SEED) {
                // Update cells[][] and return the new game state after the move
                currentState = board.stepGame(currentPlayer, row, col);
                validInput = true; // input okay, exit loop
            } else {
                System.out.println(RED + "⛔ This move at (" + (row + 1) + "," + (col + 1)
                        + ") is not valid. Try again..." + RESET);
            }
        } while (!validInput);    // repeat until input is valid
    }

    /** The entry main() method */
    public static void main(String[] args) {
        displayStartScreen(); // Show the start screen first

        // Loop for playing multiple games
        do {
            new GameMain();  // Let the constructor do the job of running one game
            boolean invalid=true;
            do {
                System.out.print(PURPLE + "\nPlay again (y/n)? " + RESET);
                char ans = in.next().charAt(0);
                if (ans == 'n' || ans == 'N') {
                    displayEndScreen(); // Show the end screen
                    System.exit(0);  // terminate the program
                } else if (ans =='y' || ans =='Y'){
                    invalid = false;
                    System.out.print("\033[H\033[2J"); // Clear console before new game
                    System.out.flush();
                } else {
                    System.out.println(RED + "Invalid input, try again!" + RESET);
                }
            } while (invalid);
        } while (true);  // repeat until user does not answer 'y'
    }

    public static void displayStartScreen() {
        System.out.println(ORANGE + " ");
        System.out.println("  ██████████████████████████████████████");
        System.out.println("  █                                    █");
        System.out.println("  █    ╔═══════════════════════════╗   █");
        System.out.println("  █    ║  WELCOME TO TIC-TAC-TOE!  ║   █");
        System.out.println("  █    ║   Press ENTER to start!   ║   █");
        System.out.println("  █    ╚═══════════════════════════╝   █");
        System.out.println("  █                                    █");
        System.out.println("  ██████████████████████████████████████" + RESET);
        System.out.print(CYAN + "\n        Siap bermain? Tekan ENTER... " + RESET);
        in.nextLine(); // Consume the newline after initial inputs if any
        in.nextLine(); // Menunggu input ENTER
        System.out.print("\033[H\033[2J"); // Membersihkan konsol (bisa jadi tidak bekerja di semua terminal)
        System.out.flush();
    }
    public static void displayEndScreen() {
        System.out.println(PURPLE + " ");
        System.out.println("  ██████████████████████████████████████");
        System.out.println("  █                                    █");
        System.out.println("  █   ╔════════════════════════════╗   █");
        System.out.println("  █   ║          GAME OVER!        ║   █");
        System.out.println("  █   ║    THANKS FOR PLAYING!     ║   █");
        System.out.println("  █   ╚════════════════════════════╝   █");
        System.out.println("  █                                    █");
        System.out.println("  █         Visit us again soon!       █");
        System.out.println("  █                                    █");
        System.out.println("  ██████████████████████████████████████" + RESET);
        System.out.println("\n");
    }
}