/*
 * @author Tom SUMKaOn
 */
import java.util.Scanner;

public class Reversi {

    public static Scanner kb = new Scanner(System.in);//Global Scanner

    public static void main(String[] args) {
        while (true) {//// Initialize the board
            int[][] board;
            int size;
            System.out.print("Please enter the board size (4 or above and even number):");
            size = kb.nextInt();

            if (size % 2 != 0 || size < 4) {
                System.out.println("Error - input numbers should be 4 or above and even number.");
                continue;
            }
            //Place 1,2 in middle position.
            board = new int[size][size];
            int middle = size / 2;
            board[middle - 1][middle - 1] = 1;
            board[middle][middle] = 1;
            board[middle - 1][middle] = 2;
            board[middle][middle - 1] = 2;
            //Pass values to method
            createBoard(board);
            startPlaying(board);
            break;//game finishes, stop all program.
        }
    }

    public static void createBoard(int[][] board) {

        for (int row = 0; row < board.length; row++) {//Print row numbers
            System.out.printf("%3d |", row);

            for (int col = 0; col < board[row].length; col++) {//Print column data
                System.out.printf("%3d", board[row][col]);
            }

            System.out.println();

        }

        System.out.printf("    +");//Print +-----line
        for (int colNumber = 0; colNumber < board.length; colNumber++) {
            System.out.printf("---");
        }

        System.out.println();
        System.out.print("     ");//Print column numbers
        for (int colNumber = 0; colNumber < board.length; colNumber++) {
            System.out.printf("%3d", colNumber);
        }
    }

    public static void startPlaying(int board[][]) {
        int currentPlayer = 1;

        boolean gameFinishes = false;

        while (gameFinishes == false) {

            if (PlayerCannotMove(board, currentPlayer)) {
                currentPlayer = 3 - currentPlayer;//currentPlayer cannot move, switch to opponentPlayer
                if (PlayerCannotMove(board, currentPlayer)) {
                    gameFinishes = true;//Both Player cannot move, end game.
                    break;//game finishes, stop the following programs
                }else{
                    System.out.printf("\n '%d' cannot move. Pass to '%d' ", (3 - currentPlayer), currentPlayer);
                }
            }

            System.out.printf("\nPlease enter the position of '%d' (row col): \n", currentPlayer);

            int playerRow = kb.nextInt();
            int playerCol = kb.nextInt();

            if (playerRow < 0 || playerRow >= board.length || playerCol < 0 || playerCol >= board.length) {// Check if the selected cell is not within the board range
                System.out.print("Error - input numbers should be 0 to " + (board.length - 1) + "!");
                continue;
            }
            if (board[playerRow][playerCol] != 0) {// Check if the selected cell is empty
                System.out.print("Error - input cell is not empty.");
                continue;
            }
            if (CheckInvalidMove(board, playerRow, playerCol, currentPlayer) == true) {// Check if the player can make a valid move, call method
                System.out.print("Error - Invalid move.");
                continue;
            }

            // Update the game board with the player's move
            board[playerRow][playerCol] = currentPlayer;// Update board data
            flip(board, playerRow, playerCol, currentPlayer);//call method, flip pieces
            createBoard(board); // call method, Print the updated board
            //currentPlayer has moved, switch player
            if (currentPlayer == 1) {
                currentPlayer = 2;
            } else {
                currentPlayer = 1;
            }
        }
        if (gameFinishes) {
            winLoseDraw(board);//call winLoseDraw method 
        }
    }

    public static boolean CheckInvalidMove(int[][] board, int playerRow, int playerCol, int currentPlayer) {
        int opponentPlayer;//Get the opponent's number, 1 or 2?
        if (currentPlayer == 1) {
            opponentPlayer = 2;
        } else {
            opponentPlayer = 1;
        }
        //Scan Directions in board
        int[] dirRows = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dirCols = {-1, 0, 1, -1, 1, -1, 0, 1};
        //there are 8 directions, so loop 8 times
        for (int i = 0; i < 8; i++) {
            int dirR = dirRows[i];
            int dirC = dirCols[i];

            // Move in the current direction until reaching the board boundaries or an empty cell
            int row = playerRow + dirR;
            int col = playerCol + dirC;

            boolean opponentDetected = false;
            while (row >= 0 && row < board.length && col >= 0 && col < board.length && board[row][col] == opponentPlayer) {
                opponentDetected = true;
                row += dirR;
                col += dirC;
            }
            // If the loop ended at the player's own cell and at least one opponent's cell was found, then the move is valid.
            if (row >= 0 && row < board.length && col >= 0 && col < board.length && board[row][col] == currentPlayer && opponentDetected == true) {
                return false;
            }

        }
        return true;
    }

    public static boolean PlayerCannotMove(int[][] board, int currentPlayer) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                if (board[row][col] == 0 && (CheckInvalidMove(board, row, col, currentPlayer)) == false) {
                    return false;// At least one valid move is found
                }
            }
        }
        return true; // No valid moves are found
    }

    public static void flip(int[][] board, int playerRow, int playerCol, int currentPlayer) {
        int opponentPlayer;
        if (currentPlayer == 1) {
            opponentPlayer = 2;
        } else {
            opponentPlayer = 1;
        }
        //Scan Directions in board
        int[] directionRows = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] directionCols = {-1, 0, 1, -1, 1, -1, 0, 1};
        //there are 8 directions, so loop 8 times
        for (int i = 0; i < 8; i++) {
            int dirR = directionRows[i];
            int dirC = directionCols[i];
            
            // Compute the initial position in the current direction
            int row = playerRow + dirR;
            int col = playerCol + dirC;

            boolean opponentDetected = false;
            // Keep moving in the current direction until the board boundary is reached or a non-opponent piece is found
            while (row >= 0 && row < board.length && col >= 0 && col < board.length && board[row][col] == opponentPlayer) {
                opponentDetected = true;
                row += dirR;
                col += dirC;
            }
            // If a non-opponent piece is found after detecting an opponent's piece
            if (row >= 0 && row < board.length && col >= 0 && col < board.length && board[row][col] == currentPlayer && opponentDetected) {
                // Flip the opponent's pieces
                int flipRow = playerRow + dirR;
                int flipCol = playerCol + dirC;
                while (flipRow != row || flipCol != col) {
                    board[flipRow][flipCol] = currentPlayer;
                    flipRow += dirR;
                    flipCol += dirC;
                }
            }
        }
    }

    public static void winLoseDraw(int[][] board) {//Calculate scores and determine winner and loser
        int blackScore = 0;
        int whiteScore = 0;

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                if (board[row][col] == 1) {
                    blackScore++;
                } else if (board[row][col] == 2) {
                    whiteScore++;
                }
            }
        }

        if (blackScore == 0) {
            System.out.printf("\n'1' has no piece left.");
        } else if (whiteScore == 0) {
            System.out.printf("\n'2' has no piece left.");
        }

        System.out.printf("\nGame Finishes.");

        System.out.printf("\n'1'- " + blackScore);
        System.out.printf("\n'2'- " + whiteScore);

        if (blackScore > whiteScore) {
            System.out.println("\nBlack wins.");
        } else if (whiteScore > blackScore) {
            System.out.println("\nWhite wins.");
        } else {
            System.out.println("\nDraw.");
        }
    }
}
