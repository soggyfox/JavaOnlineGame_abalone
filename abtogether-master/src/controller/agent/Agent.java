package controller.agent;

import model.player.Move;
import model.player.Player;

public abstract class Agent {
    private Player player;

    public Agent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public abstract Move makeMove();
}
