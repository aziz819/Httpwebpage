package jp.co.topgate.jan.web;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aizijiang.aerken on 2017/05/10.
 */
public class StatusLine {
    static final int OK = 200 ;

    static final int BAD_REQUEST = 400;

    static final int NOT_FOUND = 404 ;

    static final int METHOD_NOT_ALLOWED = 405 ;

    static final int HTTP_VERSION_NOT_SUPPORTED = 505 ;

    Map<Integer, String> codeType = new HashMap<>();

    public StatusLine(){
        codeType.put(OK, "OK");
        codeType.put(NOT_FOUND, "Not Found");
        codeType.put(BAD_REQUEST, "Bad Request");
        codeType.put(METHOD_NOT_ALLOWED, "Method Not Allowed");
        codeType.put(HTTP_VERSION_NOT_SUPPORTED, "HTTP Version Not Supported");
    }

    public StringBuilder statusfirstline(int statusCode){

        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1").append(" ").append(statusCode).append(" ").append(codeType.get(statusCode)).append("\r\n");
        return  builder ;
    }

}
