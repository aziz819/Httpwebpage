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

    public ConnectionHandler(InputStream in, OutputStream ot) throws IOException {

        if (in == null || ot == null) {
            throw new IOException("入力ストリームと出力ストリームどっちかnullになっています。");
        }

        HttpRequest req = new HttpRequest(in);

        HttpResponse res = new HttpResponse(req, ot);
    }
}