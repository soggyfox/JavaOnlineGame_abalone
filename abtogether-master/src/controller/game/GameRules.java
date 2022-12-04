package controller.game;

import model.objects.Board;
import model.objects.Marble;
import model.player.Move;
import model.player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility interface for determining game rules.
 */
public interface GameRules {
    /**
     * Checks if a move is legal using proof by (lack of) counterexample.
     * The following rule violations are checked: <br>
     * <ul>
     * <li>It is not allowed to commit suicide, i.e., it is not allowed to push or move either your
     * marbles or your teammate’s marbles off the board.</li>
     * <li>Unless in a Sumito position, all marbles must move into a free space</li>
     * <li>(4-players) In-line moves are only allowed if the first marble in the pushing line
     * belongs to the current player</li>
     * <li>(4-players) The column must contain at least one marble that belongs to you.</li>
     * <li>You may only move your own (4-players: or your teammate's) marbles</li>
     * </ul>
     * @param move the move to be checked
     * @param board the board played on
     * @return true if none of the rules are violated
     */
    static boolean isLegal(Move move, Board board) {
        int players = move.getPlayers();
        if (players > 4 || players < 2) {
            throw new IllegalStateException(String.format("Amount of players must be between 2 and 4, not %d"
                    , players));
        }
        for (String from : move.getFrom()) {
            boolean inSumito = Sumito.sumito(move, board);
            //It is not allowed to commit suicide, i.e., it is not allowed to push or move either your
            //marbles or your teammate’s marbles off the board.
            if (!Board.isField(Board.applyVector(from, move.getVector()))) {
                return false;
            //Unless in a Sumito position, all marbles must move into a free space
            } else if (!inSumito && board.getField(Board.applyVector(from, move.getVector())) != null
                    && !move.getFrom().contains(Board.applyVector(from, move.getVector()))) {
                return false;
            }
        }
        if (players == 4) {
            String rear = "";
            for (String marble: move.getFrom()) {
                rear = marble;
                String head = rear;
                for (int i = 1; i < move.getFrom().size(); i++) {
                    head = Board.applyVector(head, move.getVector());
                }
                if (move.getFrom().contains(head)) {
                    break;
                } else {
                    rear = "";
                }
            }
            // In-line moves are only allowed if the first marble in the pushing line belongs to the current player
            if (rear.equals("") && board.getField(rear) != move.getPlayer().getColor()) {
                return false;
            } else if (!rear.equals("")) {
                boolean hasCurrent = false;
                for (String pos : move.getFrom()) {
                    if (board.getField(pos) == move.getPlayer().getColor()) {
                        hasCurrent = true;
                        break;
                    }
                }
                // The column must contain at least one marble that belongs to you.
                if (!hasCurrent) {
                    return false;
                }
            }
            for (String pos : move.getFrom()) {
                // You cannot move marbles of the opponent
                if (!board.getField(pos).isFriendly(move.getPlayer().getColor())) {
                    return false;
                }
            }
        } else {
            for (String pos: move.getFrom()) {
                // You may only move your own marbles
                if (board.getField(pos) != move.getPlayer().getColor()) {
                    return false;
                }
            }
        }
        //No rules violated, the move is legal
        return true;
    }

    /**
     * Gives the winner of the game (or winning team), assuming the game has finished.
     * @param game the game to be evaluated
     * @return the winning marble, or null in case of draw
     */
    static Marble getWinner(Game game) {
        Map<Marble, Integer> scores = new HashMap<>();
        for (Player p : game.getPlayers()) {
            if (p.isWinner()) {
                return p.getColor();
            } else {
                scores.put(p.getColor(), p.getScore());
            }
        }
        if (game.getPlayers().size() == 4) {
            int scoreWhite = scores.get(Marble.WHITE) + scores.get(Marble.BLACK);
            int scoreBlue = scores.get(Marble.BLUE) + scores.get(Marble.RED);
            Marble colorWinner = scoreWhite > scoreBlue ? Marble.WHITE : Marble.BLUE;
            return scoreWhite == scoreBlue ? null : colorWinner;
        } else {
            int maxScore = scores.values().stream().max(Integer::compareTo).orElse(-1);
            for (Marble m : scores.keySet()) {
                if (scores.get(m).equals(maxScore)) {
                    return m;
                }
            }
        }
        return null;
    }

    /**
     * Checks if a game is finished.
     * @param game the game to be checked
     * @return true if the game is finished
     */
    static boolean isFinished(Game game) {
        if (game.turnsLeft() <= 0) {
            return true;
        } else {
            if (game.getPlayers().size() == 4) {
                int scoreWhite = 0;
                int scoreBlue = 0;
                for (Player p : game.getPlayers()) {
                    if (p.getColor().isFriendly(Marble.WHITE)) {
                        scoreWhite += p.getScore();
                    } else {
                        scoreBlue += p.getScore();
                    }
                }
                return scoreBlue >= 6 || scoreWhite >= 6;
            } else {
                for (Player p : game.getPlayers()) {
                    if (p.getScore() >= 6) {
                        return true;
                    }
                }
                return false;
            }
        }
    }
}
