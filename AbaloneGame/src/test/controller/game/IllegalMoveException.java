package controller.game;

public class IllegalMoveException extends Exception {
    public IllegalMoveException() {
        super("Move is illegal");
    }
}
