package view;

import controller.game.Game;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.player.Move;
import model.player.Player;
import network.protocol.Protocol;
import network.protocol.ProtocolMessages;

public class Tui extends UI {

    @Override
    public void invalidMove() {
        System.out.println("Illegal move. Enter another.");
    }

    @Override
    public void draw() {
        System.out.println("The game has ended in a draw!");
    }

    @Override
    public void winner(Player player) {
        System.out.println("Player " + player.getName() + " has won!");
    }

    @Override
    public Move makeMove(Game game, Player player) {
        int marbleAmount = -1;
        while (marbleAmount == -1) {
            System.out.println("Please select how many marbles you want to move");
            Scanner in = new Scanner(System.in);
            String input = in.nextLine();
            try {
                marbleAmount = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                continue;
            }
        }
        List<String> from = new ArrayList<>();
        while (from.size() < marbleAmount) {
            System.out.println("Please select a marble to move (Row Column, no spaces)");
            Scanner in = new Scanner(System.in);
            String input = in.nextLine();
            try {
                from.add("" + Integer.parseInt(input));
            } catch (NumberFormatException e) {
                continue;
            }
        }
        String direction = "";
        while (direction.equals("")) {
            System.out.println("Please select a direction "
                    + "(type the letter that is in the same direction from the 'F' key");
            Scanner in = new Scanner(System.in);
            String input = in.nextLine().toUpperCase();
            if (ProtocolMessages.directions.contains(input)) {
                direction = input;
            }
        }
        return new Move(game.getPlayers().size(), player, from, Protocol.toVector(direction));
    }
}
