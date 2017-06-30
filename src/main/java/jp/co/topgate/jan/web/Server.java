package jp.co.topgate.jan.web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ボート番号8080で、クライアントからの接続要求を待ち
 * Created by aizijiang.aerken on 2017/04/13.
 *
 * @author  jan
 */

public class Server {

    private static final int SERVER_PORT = 8080;           // ポート番号

    private static final String SERVER_START_MESSAGE = "Server is started •••••••••";


    public static void main(String[] args) {

        Server server = new Server();

        try {
            server.startServer(SERVER_PORT);
        } catch (IOException e) {
            System.out.println("エラー:サーバ開始時にエラー発生しました:" + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }


    /**
     *
     * @param serverPort       ポート番号8080
     * @throws IOException     ServerSocketにエラーが発生しました。
     */

    private void startServer(int serverPort) throws IOException {

        ServerSocket serverSocket = new ServerSocket(serverPort);
        System.out.println(SERVER_START_MESSAGE);

        while (true) {

            try {

                /*
                 * クライアントからの接続要求を受け取る
                 */

                Socket clientSocket = serverSocket.accept();

                ConnectionHandler connectionHandler = new ConnectionHandler(clientSocket.getInputStream(), clientSocket.getOutputStream());

                /*
                 * リクエストを解析しレスポンスを書き込む
                 */

                connectionHandler.writeResponse();

                /*
                 * ソケットを閉じる
                 */

                clientSocket.close();

            } catch (IOException | RuntimeException e) {
                System.out.println("エラー:" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}