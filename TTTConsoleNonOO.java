import java.util.Scanner;

public class TTTConsoleNonOO {
    public static final int CROSS   = 0;
    public static final int NOUGHT  = 1;
    public static final int NO_SEED = 2;

    public static final int ROWS = 3, COLS = 3;
    public static int[][] board = new int[ROWS][COLS];

    public static int currentPlayer;
    public static final int PLAYING    = 0;
    public static final int DRAW       = 1;
    public static final int CROSS_WON  = 2;
    public static final int NOUGHT_WON = 3;
    public static int currentState;

    public static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        boolean playAgain = true;

        while (playAgain) {
            initGame();

            do {
                stepGame();
                paintBoard();
                if (currentState == CROSS_WON) {
                    System.out.println("'X' won!\nBye!");
                } else if (currentState == NOUGHT_WON) {
                    System.out.println("'O' won!\nBye!");
                } else if (currentState == DRAW) {
                    System.out.println("It's a Draw!\nBye!");
                }
                currentPlayer = (currentPlayer == CROSS) ? NOUGHT : CROSS;
            } while (currentState == PLAYING);

            // Validasi input yes/no
            boolean validResponse = false;
            while (!validResponse) {
                System.out.print("Do you want to play again? (yes/no): ");
                String response = in.next();
                if (response.equalsIgnoreCase("yes")) {
                    playAgain = true;
                    validResponse = true;
                } else if (response.equalsIgnoreCase("no")) {
                    playAgain = false;
                    validResponse = true;
                } else {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            }
        }
    }

    public static void initGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board[row][col] = NO_SEED;
            }
        }
        currentPlayer = CROSS;
        currentState  = PLAYING;
    }

    public static void stepGame() {
        boolean validInput = false;
        do {
            if (currentPlayer == CROSS) {
                System.out.print("Player 'X', enter your move (row[1-3] column[1-3]): ");
            } else {
                System.out.print("Player 'O', enter your move (row[1-3] column[1-3]): ");
            }
            int row = in.nextInt() - 1;
            int col = in.nextInt() - 1;
            if (row >= 0 && row < ROWS && col >= 0 && col < COLS && board[row][col] == NO_SEED) {
                currentState = stepGameUpdate(currentPlayer, row, col);
                validInput = true;
            } else {
                System.out.println("This move at (" + (row + 1) + "," + (col + 1) + ") is not valid. Try again...");
            }
        } while (!validInput);
    }

    public static int stepGameUpdate(int player, int selectedRow, int selectedCol) {
        board[selectedRow][selectedCol] = player;

        if (board[selectedRow][0] == player && board[selectedRow][1] == player && board[selectedRow][2] == player
                || board[0][selectedCol] == player && board[1][selectedCol] == player && board[2][selectedCol] == player
                || selectedRow == selectedCol && board[0][0] == player && board[1][1] == player && board[2][2] == player
                || selectedRow + selectedCol == 2 && board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return (player == CROSS) ? CROSS_WON : NOUGHT_WON;
        } else {
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    if (board[row][col] == NO_SEED) {
                        return PLAYING;
                    }
                }
            }
            return DRAW;
        }
    }

    public static void paintBoard() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                paintCell(board[row][col]);
                if (col != COLS - 1) {
                    System.out.print("|");
                }
            }
            System.out.println();
            if (row != ROWS - 1) {
                System.out.println("-----------");
            }
        }
        System.out.println();
    }

    public static void paintCell(int content) {
        switch (content) {
            case CROSS:   System.out.print(" X "); break;
            case NOUGHT:  System.out.print(" O "); break;
            case NO_SEED: System.out.print("   "); break;
        }
    }
}