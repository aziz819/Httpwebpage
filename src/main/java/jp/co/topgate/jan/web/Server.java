package jp.co.topgate.jan.web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/* ボート番号8080で、クライアントからの接続要求を待ち
 * Created by aizijiang.aerken on 2017/04/13.
 */

public class Server {

    /*
     * ポート番号8080
     */

    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {

        Server server = new Server();

        try {

            server.startServer(SERVER_PORT);

        } catch (IOException e) {

            System.out.println("エラー:サーバ起動時にエラー発生しました:" + e.getMessage());

            e.printStackTrace();
        }
    }


    /*
     * クライアントからの接続要求をまち
     */

    public void startServer(int serverPort) throws IOException {

        ServerSocket serverSocket = new ServerSocket(serverPort);

        ConnectionHandler connectionHandler = null;

        System.out.println("Server is started •••••••••\n");

        while (true) {


            /*
             * クライアントからの接続要求を受け取る
             */

            Socket client = serverSocket.accept();

            try {

                connectionHandler = new ConnectionHandler(client.getInputStream(), client.getOutputStream());

            } catch (IOException e){

                System.out.println("エラー:" + e.getMessage());

                e.printStackTrace();

            }catch (NullPointerException e) {

                System.out.println("エラー:" + e.getMessage());

                e.printStackTrace();
            }

             /*
              * クライアントからのリクエストを解析
              */

            connectionHandler.readRequest();

             /*
              * クライアントにレスポンスを作って返す
              */

            connectionHandler.writeResponse();


             /*
              * ソケットを閉じる
              */

            client.close();
        }
    }
}