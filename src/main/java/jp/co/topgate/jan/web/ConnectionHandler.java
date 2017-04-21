package jp.co.topgate.jan.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by aizijiang.aerken on 2017/04/13.
 */
public class ConnectionHandler  extends Thread {

    ServerSocket serverSocket = null ;
    Socket clientsocket = null;
    BufferedReader br = null ;
    public ConnectionHandler(ServerSocket sercersocket) throws Exception {

        this.serverSocket = sercersocket;

        this.start(); // run()メソッドを呼び出す
    }



    public void run(){       //start()メソッドにより呼び出されるメソッド

        try {

            while(true) {

                clientsocket = serverSocket.accept();


                br = new BufferedReader(new InputStreamReader(this.clientsocket.getInputStream()));     //在这行把从客户端发来的

                String reQ = "";


                while (br.ready() || reQ.length() == 0) //ここではリクエスト読み込んでいます  GET /index.html HTTP/1.1 を一文字ずつ読み込んでいます。
                    reQ += (char) br.read();

                System.out.println(reQ);


                HttpRequest req = new HttpRequest(reQ);

                HttpResponse res = new HttpResponse(req, clientsocket);


                br.close();
                clientsocket.close();
            }

        }catch(Exception e){

            e.printStackTrace();

        }
    }
}