package jp.co.topgate.jan.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by aizijiang.aerken on 2017/04/13.  クライアントからのアクセスを受け取る
 */
public class Main extends  Thread{

    final int SERVER_PORT = 8080;         // ポート番号

    ServerSocket serversocket = null;

    Socket client = null ;

    ConnectionHandler handler = null ;

    public static void main(String[] args) throws Exception {

        new Main().runserver();        // Mainクラスのインスタンス作成,当時にrunserver()メソッドを呼び出す

    }

    public void runserver() throws Exception {

        System.out.println("Server is started •••••••••\r\n");

        serversocket = new ServerSocket(SERVER_PORT);      // サーバソケットのインスタンスを生成、ポート番号セット

        this.start();
    }


    public void run(){
        while(true){
            try {
                client = serversocket.accept();
                InputStream in = client.getInputStream();
                OutputStream ot = client.getOutputStream();
                handler = new ConnectionHandler(in,ot); // ポート番号セットされた参照変数serversocketをConnectionHandler
                client.close();
            }catch (RuntimeException e){

            }catch (IOException e) {
                System.out.println("エラー:"+e.getMessage());
                e.printStackTrace();
            }
        }
    }
}