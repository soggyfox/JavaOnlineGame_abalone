package view;

import controller.game.Game;
import model.player.Move;
import model.player.Player;
import network.client.Client;

public abstract class UI {
    private Client client;

    public abstract void invalidMove();

    public abstract void draw();

    public abstract void winner(Player player);

    public abstract Move makeMove(Game game, Player player);
}
