package SProject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;


public class Client {

    private static String URL = "http://localhost:8080/newGame";
    public static final String URLPOST =  "http://localhost:8080/yourTurn";
    

    /**
     * Takes the current board and POSTS's it to the server
     * input: the current game board and current httpClientttt
     * return: the board sent back by the server
     * side effects: the server makes a move
     *
     */
    public static Board postRequest(Board board, CloseableHttpClient httpClient) throws IOException, InterruptedException {
        String postRequestPayload = "";
        Board currentBoard = null;

        //convert java object to JSON
        Gson gson = new GsonBuilder().create();
        String jsonString = gson.toJson(board);


        //Setup the post request
        HttpPost httpPostRequest = new HttpPost(URLPOST);
        //Setup the payload for the post request
        httpPostRequest.setEntity(new StringEntity(jsonString, ContentType.APPLICATION_JSON));
        //Execute the request
        HttpResponse httpResponse = httpClient.execute(httpPostRequest);

        System.out.println("-----------------");
        System.out.println(httpResponse.getStatusLine());
        System.out.println("-----------------");


        HttpEntity entity = httpResponse.getEntity();
        if (entity == null) {
            System.out.println("entity is null\n");
        }



        byte[] buffer = new byte[1024];

        if (entity != null) {
            InputStream inputStream = entity.getContent();
            try {
                int bytesRead = 0;
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                    String piece = new String(buffer, 0, bytesRead);
                    postRequestPayload += piece;
//                    System.out.println("-----------------");
//                    System.out.println("\nthis is the payload from the server\n");
//                    System.out.println(postRequestPayload);
//                    System.out.println("-----------------");


                    Gson gson2 = new GsonBuilder().create();
                    currentBoard = gson2.fromJson(postRequestPayload, Board.class);
                    System.out.println();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return currentBoard;
    }


    /**
     * It  sends a GET request to the server for a new game. The server randomly decides who goes first and
     * sends back the new game board. The MAIN then runs a while loop to continually make moves and pass the game
     * to the server until win/draw
     * input: none
     * return: void
     * side effects: the game is played
     * @param args
     * @throws IOException
     */
    public static void main(final String[] args) throws IOException {


        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String completePayload = "";
//        ArrayList<String> arrayPayload = new ArrayList<>();

        try {
            //should find the 'get' endpoint for the URL

            HttpGet httpGetRequest = new HttpGet(URL);
            HttpResponse httpResponse = httpClient.execute(httpGetRequest);

            System.out.println("---------------------");
            System.out.println(httpResponse.getStatusLine());
            System.out.println("---------------------");

            HttpEntity entity = httpResponse.getEntity();
            if (entity == null) {
                System.out.println("entityy is null\n");
            }

            byte[] buffer = new byte[1024];

            if (entity != null) {
                InputStream inputStream = entity.getContent();
                try {
                    int bytesRead = 0;
                    BufferedInputStream is = new BufferedInputStream(inputStream);
                    while ((bytesRead = is.read(buffer)) != -1) {
                        String piece = new String(buffer, 0, bytesRead);
                     //   System.out.println("trying to print each piece: " + piece);
                        System.out.println("\n");
                        completePayload += piece;
                      //  System.out.println("json as string: " + completePayload + "\n");
//

                        //try to create  java class from json input
                        Gson gson = new GsonBuilder().create();
                        Board obj = gson.fromJson(completePayload, Board.class);
                        System.out.println("this is the very first board\n");
                        System.out.println("The Board obj: \n" + obj);
                        System.out.println();
                        obj.makeMove(httpClient);
//                        System.out.println("this is the board i am sending(Main): \n" + obj);
//                        TimeUnit.SECONDS.sleep(2);

                        //Call a recursive method to post to the server
                        while (!obj.isDraw(obj.boardAsArray()) && !obj.gameWon(obj.boardAsArray())) {
//                            Board currentBoard = null;
                            obj = postRequest(obj, httpClient);
                            if (obj.isDraw(obj.boardAsArray())) {
                                System.out.println(obj);
                                System.out.println("DRAW\nGame Over\n");
                                httpClient.getConnectionManager().shutdown();
                                System.exit(0);
                            }
                            if (obj.gameWon(obj.boardAsArray())) {
                                System.out.println(obj);
                                System.out.println("You Lost\nGame Over\n");
                                httpClient.getConnectionManager().shutdown();
                                System.exit(0);
                            }
                            System.out.println("The board: \n" + obj);
                            obj.makeMove(httpClient);
                            if (obj.isDraw(obj.boardAsArray())) {
                                System.out.println("DRAW\nGame Over\n");

                            } else if (obj.gameWon(obj.boardAsArray())) {
                                System.out.println("You won\nGame Over\n");
                            }

                        }
                        System.out.println("Game over. Draw or Computer Won\n");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        inputStream.close();
                    } catch (Exception ignore) {
                    }
                }
            }

//            Gson googleJson = new Gson();
////            ArrayList javaArrayFromJson = googleJson.fromJson();
//            //echo that json piece back to the server in json
//            System.out.println("\n");
//            for (String square : arrayPayload) {
//                System.out.println(square);
//                System.out.println("number of indices\n");
//            }

//POST TO THE SERVER HERE???

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("closing httpClient\n");
            httpClient.getConnectionManager().shutdown();
        }

    }

//    public static void printJsonObject(HttpEntity jsonObj) throws IOException {
//        for (Object key : jsonObj.getContent()) {
//            String keyStr = (String)key;
//            Object value = jsonObj.getContent(keyStr);
//        }
//    }

}
