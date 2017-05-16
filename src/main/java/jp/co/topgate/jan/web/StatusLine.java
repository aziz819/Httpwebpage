package jp.co.topgate.jan.web;

import java.util.HashMap;
import java.util.Map;

/* ステータスコードを受け取り、ステータスコードとステータスメッセージをセットするして表示すべきメッセージボディを返す
 * Created by aizijiang.aerken on 2017/05/10.
 */
public class StatusLine {
    static final int OK = 200 ;

    static final int BAD_REQUEST = 400;

    static final int NOT_FOUND = 404 ;

    static final int METHOD_NOT_ALLOWED = 405 ;

    static final int INTERNET_SERVER_ERROR = 500;

    static final int HTTP_VERSION_NOT_SUPPORTED = 505 ;


    Map<Integer, String> codeType = new HashMap<>();

    public StatusLine(){
        codeType.put(OK, "OK");
        codeType.put(NOT_FOUND, "Not Found");
        codeType.put(BAD_REQUEST, "Bad Request");
        codeType.put(METHOD_NOT_ALLOWED, "Method Not Allowed");
        codeType.put(HTTP_VERSION_NOT_SUPPORTED, "Version Not Supported");
    }

    public StringBuilder getStatusCode(int statusCode) {
        StringBuilder builder = new StringBuilder();
        for (int code : codeType.keySet()) {
            if (code == statusCode) {
                builder.append("HTTP/1.1").append(" ").append(statusCode).append(" ").append(codeType.get(statusCode)).append("\n");
                return builder;
            }
        }
        return builder.append("HTTP/1.1").append(" ").append(INTERNET_SERVER_ERROR).append(" ").append("Internet Server Error").append("\n");
    }


    public String IncorrectStatusCode(int statusCode) {

        String m;
        switch (statusCode) {
            case BAD_REQUEST:
                m = "<html><head><title>400 Bad Request</title></head>" +
                        "<body><h1>Bad Request</h1>" +
                        "<p>リクエストは不正な構文であるために、サーバーに理解できませんでした。<br /></p></body></html>";
                break;

            case NOT_FOUND:
                m = "<html><head><title>404 Not Found</title></head>" +
                        "<body><h1>Not Found</h1>" +
                        "<p>サーバーは、リクエストURIと一致するものを見つけられませんでした。</p></body></html>";
                break;

            case METHOD_NOT_ALLOWED:
                m = "<html><head><title>405 Method Not Allowed</title></head>" +
                        "<body><h1>Not Implemented</h1>" +
                        "<p>実装されていないサーバメソッドです。</p></body></html>";
                break;

            case HTTP_VERSION_NOT_SUPPORTED:
                m = "<html><head><title>505 HTTP Version Not Supported</title></head>" +
                        "<body><h1>HTTP Version Not Supported</h1>" +
                        "<p>サーバーは、リクエスト・メッセージで使用されたHTTPプロトコル・バージョンをサポートしていない、あるいはサポートを拒否している。</p> " +
                        "</body></html>";
                break;

            default:
                m = "<html><head><title>500 Internal Server Error</title></head>" +
                        "<body><h1>Internal Server Error</h1>" +
                        "<p>サーバー内部の不明なエラーにより表示できません。</p></body></html>";
        }
        return m;
    }

}
