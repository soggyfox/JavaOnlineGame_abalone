package network.protocol;

import java.util.Arrays;
import model.objects.Marble;
import model.player.Move;

/**
 * All methods related to the protocol and encoding.
 */
public interface Protocol {
    /**
     * Encodes a Move conform the Protocol.
     * @param move the move to be encoded
     * @return the encoded move
     */
    static String encodeMove(Move move) {
        String from = "";
        for (String marble : move.getFrom()) {
            from += ProtocolMessages.DELIMITER + marble.charAt(0) + ProtocolMessages.DELIMITER + marble.charAt(1);
        }
        return move.getFrom().size() + from + ProtocolMessages.DELIMITER + toDirection(move.getVector());
    }

    /**
     * Converts a direction string to a vector.
     * @param direction the direction to be decoded
     * @return the corresponding vector
     */
    static int[] toVector(String direction) {
        switch (direction) {
            case ProtocolMessages.NORTHWEST: return new int[] {-1, -1};
            case ProtocolMessages.NORTHEAST: return new int[] {-1, 0};
            case ProtocolMessages.WEST: return new int[] {0, -1};
            case ProtocolMessages.EAST: return new int[] {0, 1};
            case ProtocolMessages.SOUTHWEST: return new int[] {1, -1};
            case ProtocolMessages.SOUTHEAST: return new int[] {1, 0};
            default: throw new IllegalArgumentException(String.format("%s is not a valid direction", direction));
        }
    }

    /**
     * Converts a vector to the corresponding Direction code.
     * @param vector the vector to be encoded
     * @return the direction code
     */
    static String toDirection(int[] vector) {
        if (Arrays.equals(vector, new int[]{-1, -1})) {
            return ProtocolMessages.NORTHWEST;
        } else if (Arrays.equals(vector, new int[]{-1, 0})) {
            return ProtocolMessages.NORTHEAST;
        } else if (Arrays.equals(vector, new int[]{0, -1})) {
            return ProtocolMessages.WEST;
        } else if (Arrays.equals(vector, new int[]{0, 1})) {
            return ProtocolMessages.EAST;
        } else if (Arrays.equals(vector, new int[]{1, -1})) {
            return ProtocolMessages.SOUTHWEST;
        } else if (Arrays.equals(vector, new int[]{1, 0})) {
            return ProtocolMessages.SOUTHEAST;
        } else {
            throw new IllegalArgumentException(String.format("[%d, %d] is not a valid vector", vector[0], vector[1]));
        }
    }

    /**
     * Finds the color for a player given the number appointed by the server.
     * @param amountPlayers the amount of players in the game
     * @param player the player number appointed
     * @return the corresponding Marble color
     */
    static Marble getColor(int amountPlayers, int player) {
        if (amountPlayers == 4) {
            return new Marble[] {Marble.BLACK, Marble.BLUE, Marble.WHITE, Marble.RED}[player - 1];
        } else {
            return new Marble[] {Marble.BLACK, Marble.WHITE, Marble.BLUE}[player - 1];
        }
    }
}
