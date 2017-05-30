package jp.co.topgate.jan.web;

import jp.co.topgate.jan.web.exception.RequestParseException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

/** HttpRequestからの解析結果によってHttpResponseに表示すべきものを渡しています
 * Created by aizijiang.aerken on 2017/04/13.
 *
 * @author  jan
 */


public class ConnectionHandler {

    private int statusCode ;
    private FileResources fileResources = null;
    private InputStream is = null;
    private OutputStream os = null;


    /**
     *
     * @param is        入力ストリーム
     * @param os        出力ストリーム
     */

    public ConnectionHandler(InputStream is, OutputStream os) {

        if (is == null || os == null) {
            throw new NullPointerException("入力ストリームと出力ストリームどっちかnullになっています。");
        }

        this.is = is;
        this.os = os;
    }


    public void writeResponse() {

        HttpRequest httpRequest = null;
        statusCode = StatusLine.OK;

        try {

            httpRequest = new HttpRequest(is);

            httpRequest.parseRequest();

            String method = httpRequest.getMethod();
            String url = httpRequest.getURL();
            String version = httpRequest.getVersion();


            if (!"GET".equals(method) && !"POST".equals(method)) {
                statusCode = StatusLine.METHOD_NOT_ALLOWED;
            } else if (!("HTTP/1.1".equals(version))) {
                statusCode = StatusLine.HTTP_VERSION_NOT_SUPPORTED;
            } else {
                try {
                    fileResources = new FileResources(url);
                    checkFile(fileResources);
                } catch (FileNotFoundException e) {
                    System.out.println("エラー:" + e.getMessage());
                    e.printStackTrace();
                    statusCode = StatusLine.NOT_FOUND;
                }
            }
        } catch (RequestParseException e) {
            System.out.println("エラー:" + e.getMessage());
            e.printStackTrace();
            statusCode = StatusLine.BAD_REQUEST;
        }


        HttpResponse httpResponse = null;
        try {
            httpResponse = new HttpResponse(os, fileResources);
        } catch (NullPointerException e) {
            System.out.println("エラー:" + e.getMessage());
            e.printStackTrace();
        }

        /*
         * レスポンスを書き込む
         */

        httpResponse.writeResponse(statusCode);
    }


    /*
     *ファイル有無をチェックする
     */

    private static void checkFile(FileResources file) throws FileNotFoundException {
        if (!(file.exists() && file.isFile())) {
            throw new FileNotFoundException("ファイルは存在しないかファイルではありません");
        }
    }
}