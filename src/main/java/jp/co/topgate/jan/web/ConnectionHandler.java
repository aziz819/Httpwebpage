package jp.co.topgate.jan.web;
import org.junit.Test;
import java.io.BufferedReader;
import java.io.IOException;
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

    public ConnectionHandler(Socket clientsocket) throws IOException {

        socket = clientsocket;

        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));            //入力ストリーム
        pr = new PrintWriter(socket.getOutputStream());         //出力ストリーム
    }


    @Override
     @Test public void run(){       //Threadのstart()メソッドにより呼び出されるメソッド

        try {

           String ReQ = " ";

           while (br.ready()) ReQ += (char) br.read();

           System.out.println(ReQ);


           HttpRequest request = new HttpRequest(ReQ);

           HttpResponse response = new HttpResponse(request);




       }catch(Exception e){

           e.printStackTrace();

       }
    }
}
