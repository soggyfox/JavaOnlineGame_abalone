package model.player;

import controller.game.Sumito;
import model.objects.Board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Move {
    private int players;
    private Player player;
    private List<String> from;
    private int[] vector;

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
