package controller.agent;

import controller.game.Game;
import model.player.Move;
import model.player.Player;
import network.client.Client;

public abstract class Agent {
    protected Player player;
    protected Game game;

    public Agent() {

    }

    public Agent(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public abstract Move makeMove();
}
