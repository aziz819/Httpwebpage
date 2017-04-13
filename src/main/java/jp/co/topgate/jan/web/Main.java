package jp.co.topgate.jan.web;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by aizijiang.aerken on 2017/04/13.
 */
public class Main {
    int SERVER_PORT= 1989 ;         //サーバポート番号

    ServerSocket serversocket = null ;

    public static void main(String[] args) throws IOException {

        new Main().runserver();        //Mainクラスのインスタンス作成

    }

     public void runserver() throws IOException {

        serversocket = new  ServerSocket(SERVER_PORT);      //サーバソケットのインスタンスを生成、ポート番号セット

        acceptClient();
    }


     private void acceptClient() throws IOException {

        while(true) {
            Socket clientsocket = serversocket.accept();        //クライアント側からの接続待ち

            ConnectionHandler handler = new ConnectionHandler(clientsocket);

            handler.start();
        }
    }
}
