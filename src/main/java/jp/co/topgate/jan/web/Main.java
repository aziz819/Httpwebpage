package jp.co.topgate.jan.web;
import org.junit.Test;

import java.net.ServerSocket;
import java.net.Socket;
/**
 * Created by aizijiang.aerken on 2017/04/13.
 */
public class Main {
    int SERVER_PORT= 6543 ;         //サーバポート番号

    ServerSocket serversocket = null ;

    public static void main(String[] args) throws Exception {

        new Main().runserver();        //Mainクラスのインスタンス作成

    }

     @Test public void runserver() throws Exception {

        System.out.println("Server is started");

        serversocket = new  ServerSocket(SERVER_PORT);      //サーバソケットのインスタンスを生成、ポート番号セット

        acceptClient();
    }


     @Test private void acceptClient() throws Exception {

        while(true) {
            Socket clientsocket = serversocket.accept();        //クライアント側からの接続待ち

            ConnectionHandler handler = new ConnectionHandler(clientsocket);

            handler.start();
        }
    }
}
