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

    private ServerSocket serverSocket = null;


    public static void main(String[] args) {

        Server server = new Server();

        try {

            server.startServer(SERVER_PORT);

        } catch (IOException e) {

            System.out.println("エラー:サーバ開始時にエラー発生しました:" + e.getMessage());

            e.printStackTrace();
        }
    }


    /*
     * クライアントからの接続要求をまち
     */

    public void startServer(int serverPort) throws IOException {

        Socket client = null;

        ConnectionHandler connectionHandler = null;

        serverSocket = new ServerSocket(serverPort);

        System.out.println("Server is started •••••••••\n");

        while (true) {

            try {

                client = serverSocket.accept();

                connectionHandler = new ConnectionHandler(client.getInputStream(), client.getOutputStream());


                /*
                 * クライアントからのリクエストを解析
                 */

                connectionHandler.readRequest();


                /*
                 * クライアントにレスポンウを返す
                 */

                connectionHandler.writeResponse();

                client.close();

            } catch (NullPointerException e) {

                System.out.println("エラー：" + e.getMessage());

                e.printStackTrace();

            }
        }
    }
}