package jp.co.topgate.jan.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;

/**
 * Created by aizijiang.aerken on 2017/04/13.
 */
public class ConnectionHandler {
    private ServerSocket serversocket;
    HttpRequest req ;
    HttpResponse res ;
    private String version ;
    private String uri ;
    private String method ;
    private int statusCode ;



    public ConnectionHandler(InputStream in, OutputStream ot) throws IOException {
        //String method ;
        //int statusCode;
        //String uri = "" ;


        if (in == null || ot == null) {
            throw new RuntimeException("入力ストリームと出力ストリームどっちかnullになっています。");
        }


        try {

            req = new HttpRequest(in);

            method = req.getMethod();
            uri = req.getURL();
            version = req.getVersion();

            if(!("HTTP/1.1".equals(version))){
                statusCode = HttpResponse.HTTP_Version_Not_Supported ;
            }else if(!"GET".equals(method) && !"POST".equals(method)){
                statusCode = HttpResponse.Method_Not_Allowed ;
            }else{
                statusCode = HttpResponse.OK;
            }

        }catch (RuntimeException e){
            System.out.println("エラー：" + e.getMessage());
            statusCode = HttpResponse.BAD_REQUEST;
        }
        try {
            res = new HttpResponse(statusCode, uri, ot);
        }catch (IOException e){
            System.out.println("エラー：" + e.getMessage());
            e.printStackTrace();
        }
    }
}