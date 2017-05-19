package jp.co.topgate.jan.web;

import jp.co.topgate.jan.web.exception.RequestParseException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
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

    private static String headerLine;                                    // リクエストヘッダーを一行ずつ代入する一時的な変数

    private BufferedReader bufferedReader;

    private InputStream is = null;

    private String method;                                               // HTTPメソッド

    private String url;                                                  // URL

    private String version;                                               // HTTPバージョン

    private Map<String, String> headersMap = new HashMap<>();             // リクエストヘッダーのフィールドと値を扱う

    private Map<String, String> parametersMap = new HashMap<>();          // ポストクエリーを扱う


    public HttpRequest(InputStream is) {

        if (is == null) {

            throw new RequestParseException("入力ストリームはnullになっています。");

        }

        this.is = is;
    }

    /*
     *  リクエスト行　＆　メッセージ・ヘッダー　の読み込み、分割処理
     */

    public void parseRequestLine() {

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("文字のエンコーディングがサポートされていません");
            throw new RequestParseException("エラー:" + e.toString());

        }

        String requestLine = null;                              // 取り敢えずリクエストの一行目を読み取る

        try {

            requestLine = bufferedReader.readLine();

        } catch (IOException e) {

            System.out.println("リクエスト行の読み込みに不具合が発生しました");

            e.printStackTrace();

            throw new RequestParseException("エラー:" + e.toString());
        }

        if (requestLine == null) {

            throw new RequestParseException("リクエスト行がnullになっています。");
        }

        System.out.println(requestLine);

        String[] str1 = requestLine.split(REQUEST_LINE_SEPARATOR);                   // REQUEST_LINE_SEPARATOR == " " / 一行目を空白のところから三つに分ける


        if (str1.length != REQUEST_LINE_SIZE) {                                      // REQUEST_LINE_QUANTYTI == 3

            throw new RequestParseException("正しくないリクエストライン");
        }

        method = str1[0];                                                            // メソッド

        url = str1[1];                                                               // URI

        version = str1[2];                                                           // バージョン

        try {
            while ((headerLine = bufferedReader.readLine()) != null && !headerLine.equals("")) {    // 二行目からヘッダーフィールドと値を読み取る

                String[] header = headerLine.split(HEADERS_SEPARATOR, 2);                      // HEADERS_SEPARATOR  == ":"

                if (header.length == HEADERS_SIZE) {                                               // HEADER_ATTRIBUTE_VALUE == 2

                    header[1] = header[1].trim();

                    headersMap.put(header[0], header[1]);

                }

                System.out.println(headerLine);                                                    // ヘッダーフィールドと値
            }
        } catch (IOException e) {

            System.out.println("リクエストヘッダーの読み込みに不具合が発生しました");

            e.printStackTrace();

            throw new RequestParseException("エラー:" + e.toString());
        }
    }


    /*
     * POSTメッセージボディの読み込み、 GETパラメーター　＆　POSTパラメーター　の分割処理
     */

    public void parseRequestParameter() {

        if (("GET".equals(method))) {
            if (url.matches(".*" + GET_QUERY_QUEASTION_SEPARATE + ".*")) {              // GET_QUERY_QUEASTION_SEPARATE == "?"疑問符があるかどうかの判断
                String[] get1 = url.split(GET_QUERY_QUEASTION_SEPARATE, 2);
                url = get1[0];
                //String[] get2 = get1[1].split(QUERIES_PARAMETERS_SEPARATE);
                for (String get3 : get1[1].split(QUERIES_PARAMETERS_SEPARATE)) {              // QUERY_PARAMETER_SEPARATE == "&"
                    String[] get4 = get3.split(QUERIES_SEPARATOR, 2);                    // QUERIES__SEPARATOR == "="
                    if (get4.length != PARAMETER_SIZE) {                                      // PARAMETER_ATTRIBUTE_VALUE == 2
                        throw new RequestParseException("正しくないGETパラメーター:" + get3);
                    }
                    parametersMap.put(get4[0], get4[1]);
                }
            }
        } else if (("POST".equals(method))) {
            String parameterLine;
            try {
                while ((parameterLine = bufferedReader.readLine()) != null) {                     // ボディーメッセージを読み取り
                    //String[] post1 = parameterLine.split(QUERIES_PARAMETERS_SEPARATE);
                    for (String post2 : parameterLine.split(QUERIES_PARAMETERS_SEPARATE)) {
                        String[] post3 = post2.split(QUERIES_SEPARATOR, 2);
                        if (post3.length != PARAMETER_SIZE) {
                            throw new RequestParseException("正しくないPOSTパラメーター:" + post2);
                        }
                        parametersMap.put(post3[0], post3[1]);
                    }
                }
            } catch (IOException e) {
                System.out.println("POSTメッセージボディ読み込みに不具合が発生しました");
                e.printStackTrace();
                throw new RequestParseException("エラー:" + e.toString());
            }
        }
    }

    /*
     * privateのメソッドを返す
     */

    public String getMethod() {

        return method;
    }

    /*
     * privateのurlを返す
     */

    public String getURL() {

        if (url.endsWith("/")) {
            url = url + "/index.html";
        }

        return url;
    }


    /*
     * privateのバージョンを返す
     */

    public String getVersion() {

        return version;
    }


    /*
     * 下記はテストの時に使用,GETの場合とPOSTの場合のパラメーター属性と値の取得
     */

    public String getGetparameter(String name){
        if(name != null){
            name = parametersMap.get(name);
            return name ;
        }else{
            return null;
        }
    }

    public String getPostparameter(String name){
        if(name != null){
            name = parametersMap.get(name);
            return name ;
        }else{
            return null;
        }
    }


}
