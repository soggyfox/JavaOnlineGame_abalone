package controller.agent;

import controller.game.Game;
import controller.game.GameRules;
import model.player.Move;
import model.player.Player;
import view.UI;

public class HumanAgent extends Agent {
    private UI ui;

    public HumanAgent(UI ui) {
        super();
        this.ui = ui;
    }

    public HumanAgent(Player player, Game game, UI ui) {
        super(player, game);
        this.ui = ui;
    }

    @Override
    public Move makeMove() {
        Move move = ui.makeMove(game, player);
        while (!GameRules.isLegal(move, game.getBoard())) {
            ui.invalidMove();
            move = ui.makeMove(game, player);
        }
        return move;
    }
}
