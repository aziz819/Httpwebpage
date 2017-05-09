package jp.co.topgate.jan.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by aizijiang.aerken on 2017/04/13.
 */
public class ConnectionHandler {
    private HttpRequest req ;
    private HttpResponse res ;
    private String version ;
    private String uri ;
    private String method ;
    private int statusCode ;



    public ConnectionHandler(InputStream in, OutputStream ot) throws IOException {

        if (in == null || ot == null) {
            throw new RuntimeException("入力ストリームと出力ストリームどっちかnullになっています。");
        }

        try {

            req = new HttpRequest(in);
            method = req.getMethod();
            uri = req.getURL();
            version = req.getVersion();

            if(!("HTTP/1.1".equals(version))){
                statusCode = HttpResponse.HTTP_VERSION_NOT_SUPPORTED ;
            }else if(!"GET".equals(method) && !"POST".equals(method)){
                statusCode = HttpResponse.METHOD_NOT_ALLOWED ;
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