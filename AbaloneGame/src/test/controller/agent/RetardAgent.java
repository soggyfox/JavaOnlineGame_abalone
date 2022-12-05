package controller.agent;

import controller.game.Game;
import controller.game.GameRules;
import model.objects.Marble;
import model.player.Move;
import model.player.Player;
import view.UI;

import java.util.*;

public class RetardAgent extends Agent {

    public RetardAgent() {
        super();
    }

    public RetardAgent(Player player, Game game) {
        super(player, game);
    }

    public RetardAgent(UI ui) {
    }

    @Override
    public Move makeMove() {
        List<String> myMarbles = new ArrayList<>();
        Marble[][] board = game.getBoard().getState();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == player.getColor()) {
                    myMarbles.add(i + "" + j);
                }
            }
        }
        String pickedMarble = null;
        List<int[]> directions = null;
        while (directions == null) {
            pickedMarble = myMarbles.get((int) Math.floor(Math.random() * myMarbles.size()));
            directions = new ArrayList<>();
            int[][] allDirections = new int[][] {{-1, -1}, {-1, 0}, {0, -1}, {0, 1}, {1, -1}, {1, 0}};
            for (int[] allDirection : allDirections) {
                if (GameRules.isLegal(new Move(game.getPlayers().size(), player, Collections.singletonList(pickedMarble), allDirection), game.getBoard())) {
                    directions.add(allDirection);
                }
            }
            if (directions.size() == 0) {
                directions = null;
            }
        }
        int[] pickedDirection = directions.get((int) Math.floor(Math.random() * directions.size()));
        return new Move(game.getPlayers().size(), player, Collections.singletonList(pickedMarble), pickedDirection);
    }
}
