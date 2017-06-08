package jp.co.topgate.jan.web;

import jp.co.topgate.jan.web.exception.RequestParseException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *  リクエストを分析し、結果を返す
 * Created by aizijiang.aerken on 2017/04/13.
 */

public class HttpRequest {

    private static final int REQUEST_LINE_SIZE = 3;                       // リクエストラインの分割結果3つか

    private static final int PARAMETER_SIZE = 2;                          // パラメーターの分割結果２つか

    private static final int HEADERS_SIZE = 2;                            // ヘッダーをコロンから分割して２つか

    private static final String REQUEST_LINE_SEPARATOR = " ";             // リクエスト行を空白の所から分割

    private static final String HEADERS_SEPARATOR = ":";                  // リクエストヘッダーのフィールドと値を分割

    private static final String QUERIES_SEPARATOR = "=";                  // リクエスト行でのパラメーターの属性と値を分割

    private static final String QUERIES_PARAMETERS_SEPARATE = "&";        // パラメーターとパラメーターを分割

    private static final String GET_QUERY_QUEASTION_SEPARATE = "\\?";     // uriとクエリーを分割

    private BufferedReader bufferedReader;

    private String method;                                               // HTTPメソッド

    private String url;                                                  // URL

    private String version;                                               // HTTPバージョン

    private  String requestLine = null ;                                  // リクエスト行格納、テスト用のためクラスメンバにしました。

    private static final Map<String, String> headersMap = new HashMap<>();             // リクエストヘッダーのフィールドと値を扱う

    private static final Map<String, String> parametersMap = new HashMap<>();          // ポストクエリーを扱う

    /**
     *
     * @param is        入力ストリーム
     */

    public HttpRequest(InputStream is) {

        Objects.requireNonNull(is, "入力ストリームがnullになっています");

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RequestParseException("文字のエンコーディングがサポートされていません");
        }
    }

    /*
     *  リクエスト行　＆　メッセージ・ヘッダー　＆　パラメーターを部分ごとに各メソッドで処理する
     */

    public void parseRequest() {

        /*
         * リクエスト行の解析
         */

        parseRequestLine();

        /*
         * リクエストヘッダーの解析
         */

        parseRequestHeaders();

        /*
         * パラメーターの解析
         */

        parseParameter();

    }


    /*
     * リクエスト行の確認　＆　分割処理
     */

    private void parseRequestLine() {

        try {
            requestLine = bufferedReader.readLine();
        } catch (IOException e) {
            throw new RequestParseException("リクエスト行の読み込みに不具合が発生しました");
        }

        if (requestLine == null || requestLine.equals("")) {
            throw new RequestParseException("リクエスト行が空かnullになっています");
        }

        String[] requestLineElement = requestLine.split(REQUEST_LINE_SEPARATOR);                   // REQUEST_LINE_SEPARATOR == " " / 一行目を空白のところから三つに分ける


        if (requestLineElement.length != REQUEST_LINE_SIZE) {                                      // REQUEST_LINE_QUANTYTI == 3
            throw new RequestParseException("正しくないリクエスト行");
        }

        method = requestLineElement[0];                                                            // メソッド

        url = requestLineElement[1];                                                               // URI

        version = requestLineElement[2];                                                           // バージョン

    }


    /*
     * リクエストヘッダーの確認　＆　分割処理
     */

    private void parseRequestHeaders() {

        String headerLine;

        try {
            while ((headerLine = bufferedReader.readLine()) != null && !headerLine.equals("")) {    // 二行目からヘッダーフィールドと値を読み取る

                String[] header = headerLine.split(HEADERS_SEPARATOR, 2);                      // HEADERS_SEPARATOR  == ":"

                if (header.length == HEADERS_SIZE) {                                               // HEADER_ATTRIBUTE_VALUE == 2
                    header[1] = header[1].trim();
                    headersMap.put(header[0], header[1]);
                }
            }
        } catch (IOException e) {
            throw new RequestParseException("リクエストヘッダーの読み込みに不具合が発生しました");
        }
    }


    /*
     * パラメーターの確認　＆　分割処理
     */

    private void parseParameter() {

        String[] attributeAndValue;

        if (("GET".equals(method))) {
            if (url.matches(".*" + GET_QUERY_QUEASTION_SEPARATE + ".*")) {                        // GET_QUERY_QUEASTION_SEPARATE == "?"疑問符があるかどうかの判断
                String[] parameterAcquisition = url.split(GET_QUERY_QUEASTION_SEPARATE, 2);
                url = parameterAcquisition[0];                                                          // url
                for (String parameters : parameterAcquisition[1].split(QUERIES_PARAMETERS_SEPARATE)) {  // QUERY_PARAMETER_SEPARATE == "&"
                    attributeAndValue = parameters.split(QUERIES_SEPARATOR, 2);                  // QUERIES__SEPARATOR == "="
                    if (attributeAndValue.length != PARAMETER_SIZE) {                                 // PARAMETER_ATTRIBUTE_VALUE == 2
                        throw new RequestParseException("正しくないGETパラメーター:" + parameters);
                    }
                    parametersMap.put(attributeAndValue[0], attributeAndValue[1]);
                }
            }
        } else if (("POST".equals(method))) {
            String parameterLine;
            try {
                while ((parameterLine = bufferedReader.readLine()) != null) {                          // ボディーメッセージを読み取り
                    for (String parameters : parameterLine.split(QUERIES_PARAMETERS_SEPARATE)) {
                        attributeAndValue = parameters.split(QUERIES_SEPARATOR, 2);
                        if (attributeAndValue.length != PARAMETER_SIZE) {
                            throw new RequestParseException("正しくないPOSTパラメーター:" + parameters);
                        }
                        parametersMap.put(attributeAndValue[0], attributeAndValue[1]);
                    }
                }
            } catch (IOException e) {
                throw new RequestParseException("POSTメッセージボディ読み込みに不具合が発生しました" + e.toString());
            }
        }
    }

    /**
     *
     * @return      Httpメソッドを返す
     */

    public String getMethod() {

        return method;
    }

    /**
     *
     * @return      URLを返す
     */

    public String getUrl() {

       if (url.endsWith("/")) {
           url = url + "/index.html";
       }
        return url;
    }


    /**
     *
     * @return   Httpバージョンを返す
     */

    public String getVersion() {
        return version;
    }



    /**
     *
     * @param name 　パラメーター属性
     * @return      パラメーターの値を返す、一致しなかった場合はnullを返す
     */

    public String getGetParameter(String name){
        if(name != null){
            name = parametersMap.get(name);
            return name ;
        }else{
            return null;
        }
    }

    /**
     *
     * @param name  パラメーター属性
     * @return      パラメーターの値を返す、一致しなかった場合はnullを返す
     */

    public String getPostparameter(String name){
        if(name != null){
            name = parametersMap.get(name);
            return name ;
        }else{
            return null;
        }
    }

    /**
     *
     * @return  リクエスト行を返す
     */

    public String getRequestLine(){
        return requestLine;
    }
}
