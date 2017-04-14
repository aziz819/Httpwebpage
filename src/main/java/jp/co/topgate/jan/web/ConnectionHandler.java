package jp.co.topgate.jan.web;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by aizijiang.aerken on 2017/04/13.
 */
public class ConnectionHandler  extends Thread {

    private Socket socket = null;

    PrintWriter  pr = null ;

    BufferedReader br = null ;

    public ConnectionHandler(Socket clientsocket) throws Exception {

        socket = clientsocket;

        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));            //入力ストリーム
        pr = new PrintWriter(socket.getOutputStream());         //出力ストリーム
    }



     public void run(){       //Threadのstart()メソッドにより呼び出されるメソッド

        try {

           String ReQ = " ";

           while (br.ready()) ReQ += (char) br.read();

           System.out.println(ReQ);


           HttpRequest req = new HttpRequest(ReQ);

           HttpResponse res  = new HttpResponse(req);

           pr.write(res.response.toCharArray());

           socket.close();
           br.close();
           pr.close();

       }catch(Exception e){

           e.printStackTrace();

       }
    }
}
