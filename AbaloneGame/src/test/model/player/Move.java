package model.player;

import java.util.List;
import model.objects.Board;

public class Move {
    private int players;
    private Player player;
    private List<String> from;
    private int[] vector;

    /**
     * Creates a Move.
     * @param players the amount of players in the Game
     * @param player the player making the move
     * @param from the positions of the Marbles that are moved
     * @param vector the direction of the move, as a [row, column] vector
     */
    public Move(int players, Player player, List<String> from, int[] vector) {
        this.players = players;
        this.player = player;
        this.from = from;
        this.vector = vector;
    }

    public int getPlayers() {
        return players;
    }

    public Player getPlayer() {
        return player;
    }

    public List<String> getFrom() {
        return from;
    }

    public int[] getVector() {
        return vector;
    }

    /**
     * Makes the move on the board.
     * @param board the board played on
     */
    public void make(Board board) {
        for (int i = 0; i < from.size(); i++) {
            board.moveMarble(from.get(i), vector);
        }
    }

}
