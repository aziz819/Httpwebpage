package jp.co.topgate.jan.web;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;

/**
 * Created by aizijiang.aerken on 2017/04/13.
 */
public class ConnectionHandler{
    private ServerSocket serversocket;

    HttpFile resource = new HttpFile();

    private BufferedReader br = null;

    String method;

    String uri ;

    String version ;

    static final int OK = 200 ;

    static final int BAD = 400 ;

    static final int NOT = 404 ;

    static int statuscode ;


    public ConnectionHandler(InputStream in , OutputStream ot) throws Exception {

                HttpRequest req = new HttpRequest(in);


               // if(req.statuscode != 400){

                    method = req.method ;
                    uri = req.uri;
                    version = req.version;

                    HttpResponse res = new HttpResponse(req,ot);


              //  }







    }
}