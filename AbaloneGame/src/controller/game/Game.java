package controller.game;

import model.objects.Board;
import model.objects.Marble;
import model.player.Move;
import model.player.Player;

import java.util.List;
import java.util.Random;
public class Game {
    private Board board;
    private List<Player> players;
    private int turns = 0;

    public Game(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int turnsLeft() {
        return 96 - turns;
    }

    /**
     * Starts a game and selects a player to start.
     * @return the index of the starting player.
     */
    private int init() {
        board = new Board(players.size());
        int turn = new Random().nextInt() % players.size();
        Marble[] marbles;
        switch (players.size()) {
            case 2: marbles = new Marble[] {Marble.WHITE, Marble.BLACK};
                    break;
            case 3: marbles = new Marble[] {Marble.WHITE, Marble.BLACK, Marble.BLUE};
                    break;
            case 4: marbles = new Marble[] {Marble.WHITE, Marble.RED, Marble.BLACK, Marble.BLUE};
                    break;
            default: throw new IllegalStateException(String.format("Players must be 2, 3, or 4 instead of %s",
                    players.size()));
        }
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setColor(marbles[i]);
        }
        return turn;
    }

    public boolean finished() {
        switch (players.size()) {
            case 2:
            case 3: for (Player p : players) {
                if (p.getScore() >= 6) {
                    return true;
                }
            }
            case 4: return players.get(0).getScore() + players.get(2).getScore() >= 6
                    || players.get(1).getScore() + players.get(3).getScore() >= 6;
            default: throw new IllegalStateException(String.format("Amount of players must be 2, 3 or 4 and not %s",
                    players.size()));
        }
    }

    public Board getBoard() {
        return board;
    }
}
