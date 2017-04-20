package jp.co.topgate.jan.web;
import java.io.*;
import java.net.Socket;

/**
 * Created by aizijiang.aerken on 2017/04/13.
 */
public class ConnectionHandler  extends Thread {

    Socket clientsocket = null;

    PrintWriter  pr = null ;

    OutputStreamWriter or = null ;

    BufferedReader br = null ;

    public ConnectionHandler(Socket clientsocket) throws Exception {

        this.clientsocket = clientsocket;

       br = new BufferedReader(new InputStreamReader(this.clientsocket.getInputStream()));     //リクエスト入力をバイト形式で取得して文字ストリームに変換して
                                                                                                // バッファリングしています。
        //this.clientsocket.getOutputStream().write();
       // pr = new PrintWriter(this.clientsocket.getOutputStream(),true);         //リクエスト出力をバイト形式で取得して、
                                                                                        // フォーマットされたオブジェクトの表現を文字ストリームに出力



    }



    public void run(){       //Threadのstart()メソッドにより呼び出されるメソッド

        try {

            String reQ = "";


            while (br.ready()|| reQ.length() == 0) //ここではリクエスト読み込んでいます  GET /index.html HTTP/1.1 を一文字ずつ読み込んでいます。
                reQ += (char) br.read();

            System.out.println(reQ);


            HttpRequest req = new HttpRequest(reQ);

            HttpResponse res  = new HttpResponse(req,clientsocket);


            br.close();
           clientsocket.close();

        }catch(Exception e){

            e.printStackTrace();

        }
    }
}