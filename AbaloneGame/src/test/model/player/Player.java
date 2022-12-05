package model.player;

import controller.game.Game;
import model.objects.Marble;

/**
 * A player of the game.
 */
public class Player {
    /**
     * Current score of the Player.
     */
    private int score;
    /**
     * Color of the Player.
     */
    private Marble color;
    /**
     * Name of the Player.
     */
    private String name;

    /**
     * Initiates a Player with score 0.
     */
    public Player(String name) {
        this.name = name;
        score = 0;
    }

    /*
    Basic queries and commands:
     */

    /**
     * Getter for the score.
     * @return score
     */
    public int getScore() {
        return score;
    }

    /**
     * Increments the score.
     */
    public void incScore() {
        score++;
    }

    /**
     * Getter for the color.
     * @return color
     */
    public Marble getColor() {
        return color;
    }

    public void setColor(Marble color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    /**
     * Checks if the player won.
     * @return true if winner
     */
    public boolean isWinner() {
        return score >= 6;
    }
}
