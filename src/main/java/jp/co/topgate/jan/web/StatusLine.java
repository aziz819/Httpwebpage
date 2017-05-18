package jp.co.topgate.jan.web;

import java.util.HashMap;
import java.util.Map;

/* ステータスコードの確認してコード説明をセット＆ステータスコード200以外の場合に返すメッセージボディ
 * Created by aizijiang.aerken on 2017/05/10.
 */


public class StatusLine {

    static final int OK = 200 ;

    static final int BAD_REQUEST = 400;

    static final int NOT_FOUND = 404 ;

    static final int METHOD_NOT_ALLOWED = 405 ;

    static final int INTERNET_SERVER_ERROR = 500;

    static final int HTTP_VERSION_NOT_SUPPORTED = 505 ;


    Map<Integer, String> codeDescription = new HashMap<>();

    public StatusLine(){

        codeDescription.put(OK, "OK");
        codeDescription.put(NOT_FOUND, "Not Found");
        codeDescription.put(BAD_REQUEST, "Bad Request");
        codeDescription.put(METHOD_NOT_ALLOWED, "Method Not Allowed");
        codeDescription.put(INTERNET_SERVER_ERROR, "Internal Server exception");
        codeDescription.put(HTTP_VERSION_NOT_SUPPORTED, "Version Not Supported");

    }

    /*
     *ステータスコードの確認
     */

    public int statusCodeCheck(int statusCode) {
        for (int code : codeDescription.keySet()) {
            if (statusCode == code) {
                return statusCode;
            }
        }
        return INTERNET_SERVER_ERROR;
    }


    /*
     * ステータスコードによってコード説明を返す
     */

    public String getCodeDescription(int statusCode) {

        return codeDescription.get(statusCode);

    }


    /*
     * ステーツコード200以外の場合に返すメッセージボディ
     */

    public String getErrorMessageBody(int statusCode) {

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
                m = "<html><head><title>500 Internal Server exception</title></head>" +
                        "<body><h1>Internal Server exception</h1>" +
                        "<p>サーバー内部の不明なエラーにより表示できません。</p></body></html>";
        }

        return m;
    }
}
