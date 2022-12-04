package model.objects;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *  The Abalone board.
 *  @author Ivo Broekhof
 */
public class Board {
    /**
     * Current state of the board.
     * Map of the board (rc):
     *       00 01 02 03 04
     *     10 11 12  13 14 15
     *    20 21 22 23 24 25 26
     *  30 31 32 33  34 35 36 37
     * 40 41 42 43 44 45 46 47 48
     *  50 51 52 53  54 55 56 57
     *    60 61 62 63 64 65 66
     *     70 71 72 73 74 75
     *       80 81 82 83 84
     * The gutters comprise (extended coords; - = -1):
     *        -- -0 -1 -2 -3 -4
     *       0- [] [] [] [] [] 05
     *     1- [] [] []  [] [] [] 16
     *    2- [] [] [] [] [] [] [] 27
     *  3- [] [] [] []  [] [] [] [] 38
     * 4- [] [] [] [] [] [] [] [] [] 49
     *  5- [] [] [] []  [] [] [] [] 58
     *    6- [] [] [] [] [] [] [] 67
     *     7- [] [] []  [] [] [] 76
     *       8- [] [] [] [] [] 85
     *        9- 90 91 92 93 94
     */
    private Marble[][] state;

    /**
     * Initializes a basic board. Default constructor.
     */
    public Board() {
        // Make an empty board state, without any marbles.
        state = new Marble[9][9];
        Arrays.fill(state, null);
    }

    /**
     * Initializes a board with the start state for the given amount of players.
     * @param players the amount of players
     */
    public Board(int players) {
        this();
        // Initialize the state of the board
        state = Marble.startState(players);
    }

    /**
     * Getter for the state field.
     * @return state
     */
    public Marble[][] getState() {
        return state;
    }

    /**Gets the contents of a field by coordinates.
     * @param row the row
     * @param col the column
     * @return contents of the field
     */
    public Marble getField(int row, int col) {
        return state[row][col];
    }
    /**.
     * @param coords the coordinates in String form (e.g. "E5")
     * @return contents of the field
     */
    public Marble getField(String coords) {
        return getField(Integer.parseInt(coords.substring(0, 1)), Integer.parseInt(coords.substring(1)));
    }

    /**
     * Moves a marble from one field to another.
     * Calls itself to push any marbles in the way
     * @param rowFrom row of the marble
     * @param colFrom column of the marble
     * @param vector direction of move
     */
    public void moveMarble(int rowFrom, int colFrom, int[] vector) {
        int rowTo = Integer.parseInt(Board.applyVector(rowFrom +""+ colFrom, vector).substring(0, 1));
        int colTo = Integer.parseInt(Board.applyVector(rowFrom +""+ colFrom, vector).substring(1));
        if (state[rowTo][colTo] == null) {
            state[rowTo][colTo] = state[rowFrom][colFrom];
            state[rowFrom][colFrom] = null;
        } else {
            moveMarble(rowTo, colTo, vector);
        }
    }
    /**
     * String support for moveMarble.
     * @param from coordinates of the marble
     * @param vector direction of move
     */
    public void moveMarble(String from, int[] vector) {
        moveMarble(Integer.parseInt(from.substring(0, 1)), Integer.parseInt(from.substring(1)),
                vector);
    }

    @Override
    public String toString() {
        String empty = "⎔";
        String marble = "⏺";
        String[] strings = {
                "        A   B   C   D   E   5\n",
                "      A   B   C   D   E   F   6\n",
                "    A   B   C   D   E   F   G   7\n",
                "  A   B   C   D   E   F   G   H   8\n",
                "A   B   C   D   E   F   G   H   I\n",
                "  A   B   C   D   E   F   G   H   8\n",
                "    A   B   C   D   E   F   G   7\n",
                "      A   B   C   D   E   F   6\n",
                "        A   B   C   D   E   5\n"};
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                strings[i] = state[i][j] == null ? strings[i].replaceAll(String.valueOf((char) (j + 65)), empty)
                        : strings[i].replaceAll(String.valueOf((char) (j + 65)),
                        state[i][j].toString() + marble + "\u001b[0m");
            }
        }
        StringBuilder string = new StringBuilder();
        string.append("            0   1    2    3   4\n");
        for (int i = 0; i < strings.length; i++) {
            String s = strings[i];
            string.append(i).append(" ").append(s);
        }
        string.append("            0   1    2    3   4\n");
        return new String(string);
    }

    /**
     * Checks if the given coords represent a valid field.
     * @param row the row
     * @param col the column
     * @return true if coords are a field
     */
    public static boolean isField(int row, int col) {
        return row >= 0 && row <= 8 && col >= 0 && col <= 8 - Math.abs(row - 4);
    }

    /**
     * String support for isField(char, int).
     * @param coords coords of the presumed field
     * @return true if it is a valid field.
     */
    public static boolean isField(String coords) {
        return isField(Integer.parseInt(coords.substring(0, 1)), Integer.parseInt(coords.substring(1)));
    }

    /**
     * Checks if two fields (field A and field B) are neighbors.
     * @param rowA row of field A
     * @param colA column of field A
     * @param rowB row of field B
     * @param colB column of field B
     * @return true if the fields are neighbors
     */
    public static boolean neighbors(int rowA, int colA, int rowB, int colB) {
        return rowA - rowB == 0 || colA - colB == 0 || rowA - rowB == colA - colB;
    }

    /**
     * Same as neighbors(char, int, char, int) but with String support.
     * @param a coords of field a
     * @param b coords of field b
     * @return true if the fields are neighbours
     */
    public static boolean neighbors(String a, String b) {
        return neighbors(Integer.parseInt(a.substring(0, 1)), Integer.parseInt(a.substring(1)),
                Integer.parseInt(b.substring(0, 1)), Integer.parseInt(b.substring(1)));
    }

    /**
     * Gives the hypothetical coordinates of a move.
     * @param pos the starting position
     * @param vector the direction vector of the move
     * @return the end point of the move
     */
    public static String applyVector(String pos, int[] vector) {
        char[] coords = pos.toCharArray();
        int[] vec = new int[] {vector[0], vector[1]};
        // Account for the bended columns, but ignore horizontal moves
        if (Math.signum(4 - Integer.parseInt("" + coords[0])) == vec[0] && vec[0] != 0) {
            vec[1]++;
        }
        char[] res = new char[] {(char) (coords[0] + vec[0]), (char) (coords[1] + vec[1])};
        return new String(res);
    }
}
