package jp.co.topgate.jan.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *　ボート番号8080で、クライアントからの接続要求を待ち
 * Created by aizijiang.aerken on 2017/04/13.  クライアントからのアクセスを受け取る
 */
public class Server {

    private static final int SERVER_PORT = 8080;         // ポート番号
    ServerSocket serverSocket = null ;


    public static void main(String[] args){
        try {
            Server server = new Server();
            server.coll();
            server.startServer();
        } catch (IOException e) {
            System.out.println("ServerSocketのインスタンス作成時にエラー発生しました:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void coll() throws IOException {
        serverSocket = new ServerSocket(SERVER_PORT);      // サーバソケットのインスタンスを生成、ポート番号セット
    }


    public void startServer(){
        Socket client = null;
        ConnectionHandler handler = null;
        System.out.println("Server is started •••••••••\r\n");
        while (true) {
            try {

                client = serverSocket.accept();
                InputStream is = client.getInputStream();
                OutputStream os = client.getOutputStream();
                handler = new ConnectionHandler(is, os); // ポート番号セットされた参照変数serversocketをConnectionHandler
                client.close();
            } catch (IOException e) {
                System.out.println("エラー:" + e.getMessage());
                e.printStackTrace();
            } catch (RuntimeException e) {
                System.out.println("エラー:" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}