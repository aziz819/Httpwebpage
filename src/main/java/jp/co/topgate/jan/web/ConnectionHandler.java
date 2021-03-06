package jp.co.topgate.jan.web;

import jp.co.topgate.jan.web.exception.RequestParseException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * HttpRequestからの解析結果によってHttpResponseに表示すべきものを渡しています
 * Created by aizijiang.aerken on 2017/04/13.
 *
 * @author  jan
 */


public class ConnectionHandler {

    private InputStream is = null;
    private OutputStream os = null;

    /**
     *
     * @param is        入力ストリーム
     * @param os        出力ストリーム
     */

    public ConnectionHandler(InputStream is, OutputStream os) {

        this.is = Objects.requireNonNull(is, "入力ストリームがnullになっています");
        this.os = Objects.requireNonNull(os, "出力ストリームがnullになっています");
    }


    public void writeResponse() {

        FileResource fileResource = null;
        int statusCode = StatusLine.OK;


        try {

            HttpRequest httpRequest = new HttpRequest(is);

            httpRequest.parseRequest();

            String method = httpRequest.getMethod();
            String url = httpRequest.getUrl();
            String version = httpRequest.getVersion();

            if (!"GET".equals(method) && !"POST".equals(method)) {
                statusCode = StatusLine.METHOD_NOT_ALLOWED;
            } else if (!("HTTP/1.1".equals(version))) {
                statusCode = StatusLine.HTTP_VERSION_NOT_SUPPORTED;
            } else {
                fileResource = new FileResource(url);
                if (!(fileResource.checkFile())) {
                    statusCode = StatusLine.NOT_FOUND;
                }
            }
        } catch (RequestParseException e) {
            System.out.println("エラー:" + e.getMessage());
            e.printStackTrace();
            statusCode = StatusLine.BAD_REQUEST;
        }


        HttpResponse httpResponse = new HttpResponse(os);

        /*
         * レスポンスを書き込む
         */

        httpResponse.writeResponse(statusCode, fileResource);
    }

}