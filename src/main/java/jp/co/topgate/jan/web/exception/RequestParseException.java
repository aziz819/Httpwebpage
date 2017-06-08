package jp.co.topgate.jan.web.exception;

/**
 * 自作のエラークラス、特にリクエストが不正が解析がうまくいけなかった際に使用
 * Created by aizijiang.aerken on 2017/05/17.
 */

public class RequestParseException extends RuntimeException {
    public RequestParseException(String s) {super(s);}
}