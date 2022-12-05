package controller.game;

import java.util.List;
import model.objects.Board;
import model.objects.Marble;
import model.player.Move;
import model.player.Player;

public class Game {
    private Board board;
    private List<Player> players;
    private int turn;
    private int turnsMade = 0;

    /**
     * Creates a new Game to be played.
     * @param players the players participating in the Game
     */
    public Game(List<Player> players) {
        this.players = players;
        board = new Board(players.size());
        turn = (int) Math.floor(Math.random() * players.size());
        Marble[] marbles;
        switch (players.size()) {
            case 2:
                marbles = new Marble[] {Marble.WHITE, Marble.BLACK};
                break;
            case 3:
                marbles = new Marble[] {Marble.WHITE, Marble.BLACK, Marble.BLUE};
                break;
            case 4:
                marbles = new Marble[] {Marble.WHITE, Marble.RED, Marble.BLACK, Marble.BLUE};
                break;
            default:
                throw new IllegalStateException(String.format("Players must be 2, 3, or 4 instead of %s",
                    players.size()));
        }
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setColor(marbles[i]);
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int turnsLeft() {
        return 96 - turnsMade;
    }

    public boolean finished() {
        return turnsLeft() <= 0 || GameRules.isFinished(this);
    }

    public Board getBoard() {
        return board;
    }

    /**
     * Gets the number of the player whose turn it is.
     *
     * @return the number of the player whose turn it is
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Makes a move in the game, assuming it is that player's turn.
     *
     * @param move the move to be made
     * @throws IllegalMoveException if the move is illegal
     */
    public void move(Move move) throws IllegalMoveException {
        if (!GameRules.isLegal(move, board)) {
            throw new IllegalMoveException();
        }
        if (Sumito.sumito(move, board) && Sumito.pushOff(move, board)) {
            move.getPlayer().incScore();
        }
        move.make(board);
        turn = (turn + 1) % players.size();
        ++turnsMade;
    }


}
