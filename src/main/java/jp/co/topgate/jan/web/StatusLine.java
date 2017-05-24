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


    static final Map<Integer, String> codeDescription = new HashMap<Integer,String>();

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
}
