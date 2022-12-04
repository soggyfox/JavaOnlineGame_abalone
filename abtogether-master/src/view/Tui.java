package view;

import controller.game.GameRules;
import model.objects.Board;
import model.objects.Marble;
import model.player.Move;
import model.player.Player;
import network.protocol.Protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class Tui {

    public static void main(String[] args) throws IOException {
        int size = 2;
        Player player = new Player("Blab");
        player.setColor(Marble.BLACK);
        Board b = new Board(size);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Format: <number of marbles>;[row of marble;column of marble;];<direction>");
        System.out.println(b.toString());
        while (true) {
            while (!br.ready()) {
                //Do nothing
            }
            String[] in = br.readLine().split(";");
            String[] from = new String[Integer.parseInt(in[0])];
            for (int i = 0; i < from.length; i++) {
                from[i] = in[2*i+1] + in[2*i+2];
            }
            Move move = new Move(size, player, Arrays.asList(from),
                    Protocol.toVector(in[in.length - 1]));
            if (GameRules.isLegal(move, b)) {
                move.make(b);
                System.out.println(b.toString());
            } else {
                System.out.println("Illegal move. Enter another.");
            }

        }
    }
}
