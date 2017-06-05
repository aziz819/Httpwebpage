package jp.co.topgate.jan.web;

import java.util.HashMap;
import java.util.Map;

/**
 * ステータスコードの確認してコード説明をセット
 * Created by aizijiang.aerken on 2017/05/10.
 *
 * @author jan
 */

public class StatusLine {

    public static final int OK = 200 ;

    public static final int BAD_REQUEST = 400;

    public static final int NOT_FOUND = 404;

    public static final int METHOD_NOT_ALLOWED = 405 ;

    public static final int INTERNEL_SERVER_ERROR = 500;

    public static final int HTTP_VERSION_NOT_SUPPORTED = 505 ;

    private static final String HTTP_VERSION = "HTTP/1.1";


    static final Map<Integer, String> codeDescription = new HashMap<Integer,String>();

    public StatusLine(){

        codeDescription.put(OK, "OK");
        codeDescription.put(NOT_FOUND, "Not Found");
        codeDescription.put(BAD_REQUEST, "Bad Request");
        codeDescription.put(METHOD_NOT_ALLOWED, "Method Not Allowed");
        codeDescription.put(INTERNEL_SERVER_ERROR, "Internal Server Error");
        codeDescription.put(HTTP_VERSION_NOT_SUPPORTED, "Http Version Not Supported");

    }

    /**
     *
     * @param statusCode
     * @return ステータスラインを作成して返す
     */

    public String getStatusLine(int statusCode) {
        String[] statusLine = {HTTP_VERSION, String.valueOf(statusCode), codeDescription.get(statusCode)};
        return String.join(" ", statusLine);
    }
}
