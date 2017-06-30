package jp.co.topgate.jan.web;

import java.util.HashMap;
import java.util.Map;

/**
 * ステータスコードの確認してコード説明をセット
 * Created by aizijiang.aerken on 2017/05/10.
 *
 * @author jan
 */

class StatusLine {

    static final int OK = 200;

    static final int BAD_REQUEST = 400;

    static final int NOT_FOUND = 404;

    static final int METHOD_NOT_ALLOWED = 405;

    private static final int INTERNEL_SERVER_ERROR = 500;

    static final int HTTP_VERSION_NOT_SUPPORTED = 505;

    private static final String HTTP_VERSION = "HTTP/1.1";


    private static final Map<Integer, String> codeDescription = new HashMap<>();

    static {
        codeDescription.put(OK, "OK");
        codeDescription.put(NOT_FOUND, "Not Found");
        codeDescription.put(BAD_REQUEST, "Bad Request");
        codeDescription.put(METHOD_NOT_ALLOWED, "Method Not Allowed");
        codeDescription.put(INTERNEL_SERVER_ERROR, "Internal Server Error");
        codeDescription.put(HTTP_VERSION_NOT_SUPPORTED, "Http Version Not Supported");
    }

    /**
     *
     * @param statusCode    ステータスコード
     * @return              ステータスラインを作成して返す
     */

    static String getStatusLine(int statusCode) {
        String[] statusLine = {HTTP_VERSION, String.valueOf(statusCode), codeDescription.get(statusCode)};
        return String.join(" ", statusLine);
    }
}
