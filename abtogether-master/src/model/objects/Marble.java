package model.objects;


/**
 * Represents the marbles in the game, including a space with no marble (Marble.null).
 * @author Ivo Broekhof
 */
public enum Marble {
    BLACK, //Protocol: Blue
    WHITE, //Protocol: Red
    BLUE,  //Protocol: Green
    RED,;  //Protocol: Yellow

    /**
     * Generates the initial board state for a game with the given amount of players.
     * @param players the amount of players in the game
     * @return Initial board state
     */
    public static Marble[][] startState(int players) {
        switch (players) {
            case 2: return new Marble[][] {
            new Marble[] {        BLACK, BLACK, BLACK, BLACK, BLACK,},
            new Marble[] {     BLACK, BLACK, BLACK, BLACK, BLACK, BLACK,},
            new Marble[] {    null, null, BLACK, BLACK, BLACK, null, null,},
            new Marble[] {   null, null, null, null, null, null, null, null,},
            new Marble[] {null, null, null, null, null, null, null, null, null,},
            new Marble[] {   null, null, null, null, null, null, null, null,},
            new Marble[] {    null, null, WHITE, WHITE, WHITE, null, null,},
            new Marble[] {     WHITE, WHITE, WHITE, WHITE, WHITE, WHITE,},
            new Marble[] {        WHITE, WHITE, WHITE, WHITE, WHITE,},
            };
            case 3: return new Marble[][] {
            new Marble[] {            BLUE, BLUE, null, BLACK, BLACK,},
            new Marble[] {         BLUE, BLUE, null, null, BLACK, BLACK,},
            new Marble[] {      BLUE, BLUE, null, null, null, BLACK, BLACK,},
            new Marble[] {   BLUE, BLUE, null, null, null, null, BLACK, BLACK,},
            new Marble[] {BLUE, BLUE, null, null, null, null, null, BLACK, BLACK,},
            new Marble[] {   BLUE, null, null, null, null, null, null, BLACK,},
            new Marble[] {       null, null, null, null, null, null, null,},
            new Marble[] {       WHITE, WHITE, WHITE, WHITE, WHITE, WHITE,},
            new Marble[] {           WHITE, WHITE, WHITE, WHITE, WHITE,},
            };
            case 4: return new Marble[][] {
            new Marble[] {        BLACK, BLACK, BLACK, BLACK, null,},
            new Marble[] {      null, BLACK, BLACK, BLACK, null, RED,},
            new Marble[] {    null, null, BLACK, BLACK, null, RED, RED,},
            new Marble[] {   null, null, null, null, null, RED, RED, RED,},
            new Marble[] {BLUE, BLUE, BLUE, null, null, null, RED, RED, RED,},
            new Marble[] {  BLUE, BLUE, BLUE, null, null, null, null, null,},
            new Marble[] {    BLUE, BLUE, null, WHITE, WHITE, null, null,},
            new Marble[] {       BLUE, null, WHITE, WHITE, WHITE, null,},
            new Marble[] {          null, WHITE, WHITE, WHITE, WHITE,},
            };
            default: throw new IllegalArgumentException(String.format("Abalone cannot be played with %d players", players));
        }
    }

    /**
     * Checks if a marble is on the same team.
     * @param other a marble to be compared
     * @return true if the two marbles are on the same team
     */
    public boolean isFriendly(Marble other) {
        if (this == BLACK || this == WHITE) {
            return other == BLACK || other == WHITE;
        } else /*if (this == RED || this == BLUE)*/ {
            return other == RED || other == BLUE;
        }
    }

    public String toString() {
        switch(this) {
            case WHITE: return "\u001b[97m";
            case BLACK: return "\u001b[30m";
            case BLUE:  return "\u001b[34m";
            case RED:   return "\u001b[91m";
            default:    return null;
        }
    }
}
