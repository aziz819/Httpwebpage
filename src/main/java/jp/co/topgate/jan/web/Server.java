package jp.co.topgate.jan.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/*
 *　ボート番号8080で、クライアントからの接続要求を待ち
 * Created by aizijiang.aerken on 2017/04/13.
 */
public class Server {

    /*
     * ポート番号8080
     */

    private static final int SERVER_PORT = 8080;

    private ServerSocket serverSocket = null ;


    public static void main(String[] args){
        try {
            Server server = new Server();
            server.portNumSet();
            server.startServer();
        } catch (IOException e) {
            System.out.println("ServerSocketのインスタンス作成時にエラー発生しました:" + e.getMessage());
            e.printStackTrace();
        }
    }


    /*
     *  サーバソケットのインスタンスを生成、ポート番号セット
     */

    private void portNumSet() throws IOException {
        serverSocket = new ServerSocket(SERVER_PORT);
    }


    /*
     *クライアントからの接続要求をまち、受けたらConnectionhandlerクラスの渡す
     */

    public void startServer(){
        Socket client = null;
        ConnectionHandler handler = null;
        System.out.println("Server is started •••••••••\n");
        while (true) {
            try {

                client = serverSocket.accept();
                InputStream is = client.getInputStream();
                OutputStream os = client.getOutputStream();
                handler = new ConnectionHandler(is, os);
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