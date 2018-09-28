package SProject;

import org.apache.http.impl.client.CloseableHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This class contains the methods to:
 * print the board
 * make a move
 * turn the board into an array
 * check if game is won
 * check if game is a draw
 *
 */

public class Board {
    public static final Integer SERVER_SPOT = 1;
    public static final Integer CLIENT_SPOT = 2;
    public static final Integer EMPTY_SPOT = 0;
    public static final String CLIENT_MARK = "O";
    public static final String SERVER_MARK = "X";

    public Integer s1;
    private Integer s2;
    private Integer s3;
    private Integer s4;
    private Integer s5;
    private Integer s6;
    private Integer s7;
    private Integer s8;
    private Integer s9;


    /**
     * this prints the game board
     * @return the printed out board
     */
    @Override
    public String toString() {
        String clientO = "O";
        String serverX = "X";
        Integer count = 1;
        String gameBoard = "";
        for (int i = 0; i < 3; i++) {
            if (boardAsArray().get(i) == 0) {
                gameBoard += "|" + count + "|";
                count += 1;
            } else if (boardAsArray().get(i) == CLIENT_SPOT) {
                gameBoard += "|" + clientO + "|";
                count += 1;
            } else {
                gameBoard += "|" + serverX + "|";
                count += 1;
            }
        }
        gameBoard += "\n---------\n";

        for (int i = 3; i < 6; i++) {
            if (boardAsArray().get(i) == 0) {
                gameBoard += "|" + count + "|";
                count += 1;
            } else if (boardAsArray().get(i) == CLIENT_SPOT) {
                gameBoard += "|" + clientO + "|";
                count += 1;
            } else {
                gameBoard += "|" + serverX + "|";
                count += 1;
            }
        }

        gameBoard += "\n---------\n";

        for (int i = 6; i < 9; i++) {
            if (boardAsArray().get(i) == 0) {
                gameBoard += "|" + count + "|";
                count += 1;
            } else if (boardAsArray().get(i) == CLIENT_SPOT) {
                gameBoard += "|" + clientO + "|";
                count += 1;
            } else {
                gameBoard += "|" + serverX + "|";
                count += 1;
            }
        }
        return gameBoard;
    }


    /**
     * checks for a win/draw and then checks the users input validity
     * input: the current httpClient
     * return: void
     * side effect: updates the current board with the clients move
     * @param httpClient the current client
     * @throws IOException
     * @throws InterruptedException
     */
    public void makeMove(CloseableHttpClient httpClient) throws IOException, InterruptedException {
        String choice = "";
        //TODO check win and check full board

        //create the array
        ArrayList<Integer> updatedArray = this.boardAsArray();


        //check if the game has been won
        if (gameWon(updatedArray)) {
            System.out.println("Game Over\nYou Lost\n");
            httpClient.getConnectionManager().shutdown();
            System.exit(0);
        }

        //check if game is a DRAW/TIE
        if (isDraw(updatedArray)) {
            System.out.println("Game Over\nDRAW\n");
            httpClient.getConnectionManager().shutdown();
            System.exit(0);
        }

        //print the board to the user
        this.toString();

        //ask the user for input
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("SERVER = X\nCLIENT = O\nChoose one of the above numbered squares to make your move: ");
            choice = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(choice) > 9 || Integer.parseInt(choice) < 1) {
            throw  new IOException("Number not in range or not given space number!\n");
        } else if (this.boardAsArray().get(Integer.parseInt(choice) - 1) != EMPTY_SPOT) {
//            throw new IOException("That Space Is Already Taken!\n");
            System.out.println("That was not a valid move\n");
            this.makeMove(httpClient);
        } else {
            updatedArray.set(Integer.parseInt(choice) - 1, CLIENT_SPOT);
            this.updateBoard(updatedArray);

        }
    }


    /**
     * Checks to see if the game is a draw
     * input: takes the current board as an array
     * return: true if the board is full and false otherwise
     * side effects: none
     */
    public Boolean isDraw(ArrayList<Integer> boardArray) {
        Boolean result = true;
        for (Integer space : boardArray) {
            if (space == 0) {
                result = false;
            }
        }
        return result;
    }


    /**
     * checks to see if the game has been won
     * input: takes the currentttt board as an array
     * return: true if  there is a win on the board
     * side effects: none
     */
    public Boolean gameWon(ArrayList<Integer> boardArray) {
        if ((boardArray.get(0) != 0 && boardArray.get(0) == boardArray.get(1) && boardArray.get(0) == boardArray.get(2))
                || (boardArray.get(3) != 0 && boardArray.get(3) == boardArray.get(4) && boardArray.get(3) == boardArray.get(5))
                || (boardArray.get(6) != 0 && boardArray.get(6) == boardArray.get(7) && boardArray.get(8) == boardArray.get(5))
                || (boardArray.get(0) != 0 && boardArray.get(0) == boardArray.get(3) && boardArray.get(0) == boardArray.get(6))
                || (boardArray.get(1) != 0 && boardArray.get(1) == boardArray.get(4) && boardArray.get(1) == boardArray.get(7))
                || (boardArray.get(2) != 0 && boardArray.get(2) == boardArray.get(5) && boardArray.get(2) == boardArray.get(8))
                || (boardArray.get(0) != 0 && boardArray.get(0) == boardArray.get(4) && boardArray.get(0) == boardArray.get(8))
                || (boardArray.get(2) != 0 && boardArray.get(2) == boardArray.get(4) && boardArray.get(2) == boardArray.get(6))) {
            return true;
        }
        return false;
    }

    /**
     * updates the current game board
     * input: takes the current game board as an array
     * return: void
     * side effects: updates the game board
     * @param currentList
     */
    public void updateBoard(ArrayList<Integer> currentList) {
        this.s1 = currentList.get(0);
        this.s2 = currentList.get(1);
        this.s3 = currentList.get(2);
        this.s4 = currentList.get(3);
        this.s5 = currentList.get(4);
        this.s6 = currentList.get(5);
        this.s7 = currentList.get(6);
        this.s8 = currentList.get(7);
        this.s9 = currentList.get(8);
    }

    /**
     * takes the board and turns it into an array
     * input: none
     * return: the game board as an arrayList
     * side effects: none
     */
    public ArrayList<Integer> boardAsArray() {
        ArrayList<Integer> boardArray = new ArrayList<>();
        boardArray.add(s1);
        boardArray.add(s2);
        boardArray.add(s3);
        boardArray.add(s4);
        boardArray.add(s5);
        boardArray.add(s6);
        boardArray.add(s7);
        boardArray.add(s8);
        boardArray.add(s9);

        return boardArray;
    }
}
