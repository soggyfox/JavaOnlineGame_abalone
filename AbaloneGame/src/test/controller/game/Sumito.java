package controller.game;

import model.objects.Board;
import model.player.Move;

/**
 * Utility interface for determining Sumitos.
 */
public interface Sumito {
    /**
     * Checks if a marble gets pushed off, given the move is legal and in Sumito position.
     *
     * @param move  the move to be checked, given it is legal and in Sumito position
     * @param board the board played on
     * @return <code>true</code> if any marble is pushed off
     */
    static boolean pushOff(Move move, Board board) {
        String head = "";
        for (String m : move.getFrom()) {
            if (!move.getFrom().contains(Board.applyVector(m, move.getVector()))) {
                head = m;
                break;
            }
        }
        String[] front = new String[move.getFrom().size()];
        for (int i = 0; i < front.length; i++) {
            front[i] = i == 0 ? Board.applyVector(head, move.getVector()) :
                Board.applyVector(front[i - 1], move.getVector());
            if (!Board.isField(front[i])) {
                return board.getField(front[i - 1]) != null;
            }
        }
        return false;
    }

    /**
     * Checks if a move has a sumito.
     *
     * @param move  the move to be checked
     * @param board the board played on
     * @return <code>true</code> if the move has any sumito.
     */
    static boolean sumito(Move move, Board board) {
        return !move.getFrom().get(0).equals("")
            && sumito21(move, board) || sumito31(move, board) || sumito32(move, board);
    }

    /**
     * Checks if a move has a 2-1 sumito.
     *
     * @param move  the move to be checked
     * @param board the board played on
     * @return <code>true</code> if the move has a 2-1 sumito.
     */
    private static boolean sumito21(Move move, Board board) {
        if (move.getFrom().size() != 2) {
            return false;
        }
        String head = "";
        for (String m : move.getFrom()) {
            if (!move.getFrom().contains(Board.applyVector(m, move.getVector()))) {
                head = m;
                break;
            }
        }
        String[] front = new String[2];
        for (int i = 0; i < front.length; i++) {
            front[i] = i == 0 ? Board.applyVector(head, move.getVector()) :
                Board.applyVector(front[i - 1], move.getVector());
        }
        switch (move.getPlayers()) {
            case 2:
            case 3:
                return move.getFrom().size() == 2
                    && board.getField(front[0]) != move.getPlayer().getColor()
                    && (!Board.isField(front[front.length - 1])
                    || board.getField(front[front.length - 1]) == null);
            case 4:
                return move.getFrom().size() == 2
                    && !move.getPlayer().getColor().isFriendly(board.getField(front[0]))
                    && (!Board.isField(front[front.length - 1])
                    || board.getField(front[front.length - 1]) == null);
            default:
                throw new IllegalStateException(String.format("Amount of players must be 2,3,4 and not %s",
                    move.getPlayers()));
        }
    }

    /**
     * Checks if a move has a 3-1 sumito.
     *
     * @param move  the move to be checked
     * @param board the board played on
     * @return <code>true</code> if the move has a 3-1 sumito.
     */
    private static boolean sumito31(Move move, Board board) {
        if (move.getFrom().size() != 2) {
            return false;
        }
        String head = "";
        for (String m : move.getFrom()) {
            if (!move.getFrom().contains(Board.applyVector(m, move.getVector()))) {
                head = m;
                break;
            }
        }
        String[] front = new String[2];
        for (int i = 0; i < front.length; i++) {
            front[i] = i == 0 ? Board.applyVector(head, move.getVector()) :
                Board.applyVector(front[i - 1], move.getVector());
        }
        switch (move.getPlayers()) {
            case 2:
            case 3:
                return move.getFrom().size() == 3
                    && board.getField(front[0]) != move.getPlayer().getColor()
                    && (!Board.isField(front[front.length - 1])
                    || board.getField(front[front.length - 1]) == null);
            case 4:
                return move.getFrom().size() == 3
                    && !move.getPlayer().getColor().isFriendly(board.getField(front[0]))
                    && (!Board.isField(front[front.length - 1])
                    || board.getField(front[front.length - 1]) == null);
            default:
                throw new IllegalStateException(String.format("Amount of players must be 2,3,4 and not %s",
                    move.getPlayers()));
        }
    }

    /**
     * Checks if a move has a 3-2 sumito.
     *
     * @param move  the move to be checked
     * @param board the board played on
     * @return <code>true</code> if the move has a 3-2 sumito.
     */
    private static boolean sumito32(Move move, Board board) {
        if (move.getFrom().size() != 3) {
            return false;
        }
        String head = "";
        for (String m : move.getFrom()) {
            if (!move.getFrom().contains(Board.applyVector(m, move.getVector()))) {
                head = m;
                break;
            }
        }
        String[] front = new String[3];
        for (int i = 0; i < front.length; i++) {
            front[i] = i == 0 ? Board.applyVector(head, move.getVector()) :
                Board.applyVector(front[i - 1], move.getVector());
        }
        switch (move.getPlayers()) {
            case 2:
            case 3:
                return move.getFrom().size() == 3
                    && board.getField(front[0]) != move.getPlayer().getColor()
                    && board.getField(front[1]) != move.getPlayer().getColor()
                    && !Board.isField(front[front.length - 1])
                    || (board.getField(front[front.length - 1]) == null);
            case 4:
                return move.getFrom().size() == 3
                    && !move.getPlayer().getColor().isFriendly(board.getField(front[0]))
                    && !move.getPlayer().getColor().isFriendly(board.getField(front[1]))
                    && (!Board.isField(front[front.length - 1])
                    || board.getField(front[front.length - 1]) == null);
            default:
                throw new IllegalStateException(String.format("Amount of players must be 2,3,4 and not %s",
                    move.getPlayers()));
        }
    }
}
