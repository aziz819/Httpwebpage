package jp.co.topgate.jan.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* Serverクラスからクライアントからの要求を受けとり、ステータス行の確認をする
 * Created by aizijiang.aerken on 2017/04/13.
 */
public class ConnectionHandler {

    private String version ;
    private String uri ;
    private String method ;
    private int statusCode ;

    public ConnectionHandler(InputStream is, OutputStream os) throws IOException {

        HttpRequest request;

        HttpResponse response;

        FileResources fileresources = null;

        StatusLine statusline = new StatusLine();

        if (is == null || os == null) {
            throw new NullPointerException("入力ストリームと出力ストリームどっちかnullになっています。");
        }

        try {

            /*
             * リクエストメッセージをHttpRequestクラスの渡して分析、分割してもらう
             */

            request = new HttpRequest(is);

            method = request.getMethod();

            uri = request.getURL();

            if (uri.endsWith("/")) {
                uri = "/index.html";
            }

            version = request.getVersion();

            /*
             * uriをFileResourcesクラスに渡してファイル拡張子の確認をする
             */

            fileresources = new FileResources(uri);

            if (!"GET".equals(method) && !"POST".equals(method)) {
                statusCode = StatusLine.METHOD_NOT_ALLOWED;
            } else if (!("HTTP/1.1".equals(version))) {
                statusCode = StatusLine.HTTP_VERSION_NOT_SUPPORTED;
            } else if (!(fileresources.exists() && fileresources.isFile())) {
                statusCode = StatusLine.NOT_FOUND;
            } else {
                statusCode = StatusLine.OK;
            }

        } catch (FileNotFoundException e) {
            System.out.println("エラー：" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("エラー：" + e.getMessage());
            e.printStackTrace();
        } catch (RuntimeException e) {
            System.out.println("エラー：" + e.getMessage());
            e.printStackTrace();
            statusCode = statusline.BAD_REQUEST;
            fileresources = new FileResources("");
        }


        try {

            response = new HttpResponse(statusCode, os, fileresources, statusline);

        }catch (IOException e){
            System.out.println("エラー：" + e.getMessage());
            e.printStackTrace();
        }catch (RuntimeException e){
            System.out.println("エラー：" + e.getMessage());
            e.printStackTrace();
            throw new NullPointerException();
        }
    }
}