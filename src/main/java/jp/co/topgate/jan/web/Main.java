package jp.co.topgate.jan.web;

import java.net.ServerSocket;

/**
 * Created by aizijiang.aerken on 2017/04/13.  クライアントからのアクセスを受け取る
 */
public class Main {

    final int SERVER_PORT= 8080 ;         //サーバポート番号

    ServerSocket serversocket = null ;

    public static void main(String[] args) throws Exception {

        new Main().runserver();        //Mainクラスのインスタンス作成,当時にrunserver()メソッドを呼び出す

    }

    public void runserver() throws Exception {

        System.out.println("Server is started •••••••••");

        serversocket = new  ServerSocket(SERVER_PORT);      //サーバソケットのインスタンスを生成、ポート番号セット

        ConnectionHandler handler = new ConnectionHandler(serversocket); //ポート番号セットされた参照変数serversocketをConnectionHandler
                                                                         //クラスのコンストラクタに引数として渡す

    }
}