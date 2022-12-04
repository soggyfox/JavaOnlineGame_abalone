package network.protocol;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * GENERAL AGREEMENTS:
 *
 * IMPORTANT!! ALL MESSAGES HAVE TO BE OF TYPE STRING
 *
 * STANDARD PORT = 69
 *
 * GRID IS SUPPOSED TO BE TWO-DIMENSIONAL AND WE START TO COUNT FROM 0
 *
 * IF PLAYER DISCONNECTS THE GAME ENDS AND THE OPPONENT WINS (2 PLAYER GAME)
 * IF PLAYER DISCONNECTS THE GAME ENDS AND THE OPPONENT WITH THE MOST MARBLES LEFT WINS (3 PLAYER GAME)
 * IF PLAYER DISCONNECTS THE GAME ENDS AND THE OPPONENT TEAM WINS (4 PLAYER GAME)
 *
 *
 *
 * STANDARD BOARD REPRESENTATION
 *
 * 2 Player game:
 * Player1 = BLUE Player1 is positioned SOUTH at the board
 * Player2 = RED Player2 is positioned NORTH at the board
 *
 * 3 Player game:
 * Player1 = BLUE    Player1 is positioned SOUTH at the board
 * Player2 = RED    Player2 is positioned NORTHEAST at the board
 * Player3 = GREEN  Player3 is positioned NORTHWEST at the board
 *
 * 4 Player game:
 * PLAYER1 = BLUE   (Teams with PLAYER3 RED)     Player1 is positioned SOUTH at the board
 * PLAYER2 = GREEN    (Teams with PLAYER4 YELLOW)  Player2 is positioned WEST at the board
 * PLAYER3 = RED  (Teams with Player1 BLUE)      Player3 is positioned NORTH at the board
 * PLAYER4 = YELLOW (Teams with player2 GREEN)   Player4 is positioned EAST at the board
 *
 *
 * @author l.a.hartmans@student.utwente.nl
 *
 */
public class ProtocolMessages {
    /**
     * Client: this is the first thing the client sends to the server. The syntax goes as follows:
     * ProtocolMessages.JOIN + ProtocolMessages.DELIMITER + playername
     * Client example message: j;John -> Player John wants to join game
     *
     * Server: Server answers message with ProtocolMessages.SUCCESS when he joined succesfully and he is not the first player.
     *   !!! If player is first player server will send ProtocolMessages.FIRST_PLAYER and will wait for Client to response with
     * an integer between 1 and 4. If client responses with ProtocolMessages.PLAYER_AMOUNT + ProtocolMessages.DELIMIMTER + 1, server will play the computer and play against the client.
     * If client wants to play with 2,3 or 4 humanplayers the client has to send ProtocolMessages.PLAYER_AMOUNT + ProtocolMessages.DELIMITER + numOfPlayers.
     * After the firstplayer has send the message the server replies with ProtocolMessages.SUCCESS and other players are able to join this game.
     * Example conversation:
     * Client: j;John
     * Server: f
     * Client: p;3
     * Server: s
     * --- Other players can now connect ---
     *
     */
    public static final String JOIN = "j";

    /**
     * Server: Server sends this when you joined using ProtocolMessages.JOIN + ProtocolMessages.DELIMITER + playername
     * Client: Reacts with ProtocolMessages.READY when ready to start. Once all players send ProtocolMessages.Ready after they got ProtocolMessages.Success
     * and all the required players are connected the server will start the game.
     * Example server message: s
     */
    public static final String SUCCESS = "s";
    /**
     * Server sends this to all clients when everyone is ready to indicate what colour you are, what position you have
     * , and what the colors and positions of your opponents are. As last argument the Server sends what player you are. Using that information you will also know what color you are and at what position you play.
     * Syntax: ProtocolMessages.NUM_OF_PLAYERS + (2,3 or 4) + (ProtocolMessages.PLAYER_ONE OR ProtocolMessages.PLAYER_TWO OR ProtocolMessages.PLAYER_THREE OR ProtocolMessages.PLAYER_FOUR)
     * Example server message: g;2 (This will be a two player game)
     */
    public static final String NUM_OF_PLAYERS = "g";
    /**
     * Server sends this to client when he is the first player to connect, so he can choose how many players can play
     * Message syntax: ProtocolMessages.FIRST_PLAYER
     * example: f
     * See !!! at the JavaDoc of JOIN for what the client has to respond
     */
    public static final String FIRST_PLAYER = "f";
    /**
     * See !!! at the JavaDoc of JOIN for usage information
     */
    public static final String PLAYER_AMOUNT =  "p";
    /**
     *  Client: Client sends this to server when he is ready to play. This message can only be send and is expected to be send
     *  when client receives ProtocolMessages.Success from server
     *  After all players send ProtocolMessages.READY the server will start the game.
     */
    public static final String READY = "r";
    /**
     * CLIENT PART:
     *
     * Client sends this after ProtocolMessages.TURN is received from the server.
     * Client message syntax: ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + numOfMarblesYouWantToMove (1,2 or3)
     * + ProtocolMessages.DELIMITER+ROW1+ProtocolMessages.DELIMITER+COL1
     * +ProtocolMessages.DELIMITER+(ROW2)+ProtocolMessages.DELIMITER+(COL2)+ProtocolMessages.DELIMITER+(ROW3)+
     * ProtocolMessages.DELIMITER+ProtocolMessages.NorthEast (OR OTHER DIRECTION)
     * example 1: 3;0;0;0;1;0;2;V -> This will move 3 Marbles (row1 = 0, col1 = 0, row2 =0, col2=1. row3 =0, col3 =1) to SouthEast
     * example 2: 2;3;2;3;3;T -> This will move 2 marbles (row1 = 3, col2 = 2, row2=3, col3=3) to the Direction NorthEast.
     *
     * SERVER PART:
     *
     * Server sends Move to all clients once the client who"s turn it was send his move and this move is also valid according to the server.
     * Server message syntax: ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + ProtocolMessages.PlAYER_(ONE,TWO,THREE or FOUR)
     * + ProtocolMessages.DELIMITER + numOfMarblestoMove (1,2 or3)
     * + ProtocolMessages.DELIMITER+ROW1+ProtocolMessages.DELIMITER+COL1
     * +ProtocolMessages.DELIMITER+(ROW2)+ProtocolMessages.DELIMITER+(COL2)+ProtocolMessages.DELIMITER+(ROW3)+
     * ProtocolMessages.DELIMITER+ProtocolMessages.NorthEast (OR OTHER DIRECTION)
     * example message: m;1;2;4;3;4;4;T -> Player1 has moved TWO marbles (Row1 = 4, col2 = 3, Row2 = 4, col2 = 4) in direction NORTHEAST
     * Client should now update their boards according to this move and wait until it"s their turn again.
     *
     */
    public static final String MOVE = "m";
    /**
     * Server sends this if the MOVE the client sent is incorrect according to the server.
     * Player then has to resubmit the Move (See MOVE javadoc)
     * syntax server message: ProtocolMessages.NOT_VALID
     * client syntax: See Javadoc of MOVE
     */
    public static final String NOT_VALID = "n";
    /**
     * Server sends this to client if the clients move is valid and accepted. Now the client will just wait for other messages.
     * Syntax server message: ProtocolMessages.VALID
     */
    public static final String VALID = "v";
    /**
     * Server sends this to client to indicate that it is his turn.
     * Client now has to make a move and responds according to the JavaDoc of MOVE
     */
    public static final String TURN = "t";
    /**
     * Once 40 total moves have passed or someone won the server will send this
     * Server message syntax: ProtocolMessages.WINNER + ProtocolMessages.DELIMITER + ProtocolMessages.PLAYER_ONE OR PLAYER_TWO OR PLAYER_THREE OR PLAYER_FOUR depending on who Won
     * Example: w;1 -> Player1 has won
     * Example: w;3 -> Player3 has won
     */
    public static final String WINNER = "w";
    /**
     * ProtocolMessages.DELIMITER is used whenever a new command starts BUT NOT at the end of a sentence.
     * Example:
     * m;3;1;2;1;3;1;4;V Move 3 Marbles(row1=1,col1=2,row2=1,col2=3,row3=1,col2=4) to the Direction SouthEast
     */
    public static final String DELIMITER = ";";
    /**
     *  Server sends this whenever it gets an unexpected or invalid message from the client
     *  Syntax: ProtocolMessages.UNEXPECTED_MESSAGE
     *  Client doesn"t have to react on this
     */
    public static final String UNEXPECTED_MESSAGE = "u";

    /**
     * Used by MOVE
     */
    public static final String NORHTEAST ="T";
    /**
     * Direction NORTHWEST
     * used by MOVE
     */
    public static final String NORTHWEST ="R";
    /**
     * Direction EAST
     * used by MOVE
     */
    public static final String EAST = "G";
    /**
     * Direction WEST
     * used by MOVE
     */
    public static final String WEST = "D";
    /**
     * Direction SOUTHEAST
     * used by MOVE
     */
    public static final String SOUTHEAST = "V";
    /**
     * Direction SOUTHWEST
     * used by MOVE
     */
    public static final String SOUTHWEST = "C";
    /**
     * Representation of Player1
     * Player1 is ALWAYS blue and plays SOUTH on theboard
     */
    public static final String PLAYER_ONE = "1";
    /**
     * Representation of Player2
     * Player2 is ALWAYS red and plays NORTH on the board
     */
    public static final String PLAYER_TWO = "2";
    /**
     * Representation of Player3
     * Player3 is ALWAYS green and plays WEST on the board
     */
    public static final String PLAYER_THREE = "3";
    /**
     * Representation of Player4
     * Player4 is ALWAYS red and plays EAST on the board
     */
    public static final String PLAYER_FOUR = "4";

    /**
     * Set of all valid directions
     * @author i.broekhof@student.utwente.nl
     */
    public static final Set<String> directions = new HashSet<>(Arrays.stream(
            new String[]{NORTHWEST, NORHTEAST, EAST, WEST, SOUTHEAST, SOUTHWEST}).collect(Collectors.toSet()));
}

