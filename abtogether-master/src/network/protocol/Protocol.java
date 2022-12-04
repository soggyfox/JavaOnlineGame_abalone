package network.protocol;

import model.player.Move;

import java.util.Arrays;

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
            case ProtocolMessages.NORHTEAST: return new int[] {-1, 0};
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
            return ProtocolMessages.NORHTEAST;
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
}
