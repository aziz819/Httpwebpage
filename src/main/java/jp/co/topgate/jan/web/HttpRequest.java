package jp.co.topgate.jan.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aizijiang.aerken on 2017/04/13.
 */
public class HttpRequest {

    private static final int REQUEST_LINE_QUANTYTI = 3;                       // リクエストラインの分割結果3つか

    private static final int PARAMETER_ATTRIBUTE_VALUE = 2 ;                  // パラメーターの分割結果２つか

    private static final int HEADER_ATTRIBUTE_VALUE = 2 ;                     // ヘッダーをコロンから分割して２つか

    private static final String REQUEST_LINE_SEPARATOR = " ";                 // リクエスト行を空白の所から分割

    private static final String HEADERS_SEPARATOR = ":";                      // リクエストヘッダーのフィールドと値を分割

    private static final String QUERIES_SEPARATOR = "=";                      // リクエスト行でのパラメーターの属性と値を分割

    private static final String QUERIES_PARAMETERS_SEPARATE = "&";            // パラメーターとパラメーターを分割

    private static final String GET_QUERY_QUEASTION_SEPARATE = "\\?";         // uriとクエリーを分割

    private static String headerLine;                                         // リクエストヘッダーを一行ずつ代入する一時的な変数

    private BufferedReader buffr;

    private String method;                                                    // HTTPメソッド

    private String uri;                                                       // URL

    private String version;                                                   // HTTPバージョン

    private Map<String, String> headersMap = new HashMap<>();                 // リクエストヘッダーのフィールドと値を扱う

    private Map<String, String> postParametersMap = new HashMap<>();          // ポストクエリーを扱う

    private Map<String, String> getParametersMap = new HashMap<>();           // ゲットクエリーを扱う


    public HttpRequest(InputStream is) throws RuntimeException, IOException {

        if (is == null) {
            throw new NullPointerException("入力ストリームはnullになっています。");
        }

        try {

            buffr = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String requestLine = buffr.readLine();                                         // 取り敢えずリクエストの一行目を読み取る

            if (requestLine == null) {
                throw new NullPointerException("requestLineがnullになっています。");
            }

            System.out.println(requestLine);

            String[] str1 = requestLine.split(REQUEST_LINE_SEPARATOR);                    // REQUEST_LINE_SEPARATOR == " " / 一行目を空白のところから三つに分ける


            if (str1.length != REQUEST_LINE_QUANTYTI) {                                  // REQUEST_LINE_QUANTYTI == 3
                throw new RuntimeException("正しくないrequestLine");
            }

            method = str1[0];                                                            // メソッド

            uri = str1[1];                                                               // URI

            version = str1[2];                                                           // バージョン

            while ((headerLine = buffr.readLine()) != null && !headerLine.equals("")) {  // 二行目からヘッダーフィールドと値を読み取る

                String[] header = headerLine.split(HEADERS_SEPARATOR,2);           // HEADERS_SEPARATOR  == ":"

                if (header.length == HEADER_ATTRIBUTE_VALUE) {                           // HEADER_ATTRIBUTE_VALUE == 2
                    
                    header[1] = header[1].trim();

                    headersMap.put(header[0], header[1]);

                } else if (header.length > HEADER_ATTRIBUTE_VALUE) {

                    header[1] = header[1].trim();

                    headersMap.put(header[0], header[1] + HEADERS_SEPARATOR + header[2]);         // ヘッダーに　”：”コロンが2以上の場合

                }

                System.out.println(headerLine);                                                   // ヘッダーフィールドと値
            }

            if (("GET".equals(method))) {
                if (uri.matches(".*" + GET_QUERY_QUEASTION_SEPARATE + ".*")) {             // GET_QUERY_QUEASTION_SEPARATE == "?"疑問符があるかどうかの判断
                    String[] get1 = uri.split(GET_QUERY_QUEASTION_SEPARATE);
                    uri = get1[0];
                    //String[] get2 = get1[1].split(QUERIES_PARAMETERS_SEPARATE);
                    for (String get3 : get1[1].split(QUERIES_PARAMETERS_SEPARATE)) {             // QUERY_PARAMETER_SEPARATE == "&"
                        String[] get4 = get3.split(QUERIES_SEPARATOR);                           // QUERIES__SEPARATOR == "="
                        if(get4.length != PARAMETER_ATTRIBUTE_VALUE){                            // PARAMETER_ATTRIBUTE_VALUE == 2
                            throw new RuntimeException("正しくないGETパラメーター:" + get3);
                        }
                        getParametersMap.put(get4[0], get4[1]);
                    }
                }
            } else if (("POST".equals(method))) {
                String parameterLine;
                while ((parameterLine = buffr.readLine()) != null) {                            // ボディーメッセージを読み取り
                    //String[] post1 = parameterLine.split(QUERIES_PARAMETERS_SEPARATE);
                    for (String post2 : parameterLine.split(QUERIES_PARAMETERS_SEPARATE)) {
                        String[] post3 = post2.split(QUERIES_SEPARATOR);
                        if(post3.length != PARAMETER_ATTRIBUTE_VALUE){
                            throw new RuntimeException("正しくないPOSTパラメーター:" + post2);
                        }
                        postParametersMap.put(post3[0], post3[1]);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("BufferedReader読み込みに不具合がありました。");
        }
    }


    public String getMethod(){
        return method;
    }           // privateのメソッドにアクセス

    public String getURL(){
        return uri ;
    }                //privateのuriにアクセス

    public String getVersion(){
        return version ;
    }        //priavteのバージョンにアクセス


    /*
    * 下記はテストの時に使用
    * */

    public String getGetparameter(String name){
        if(name != null){
            name = getParametersMap.get(name);
            return name ;
        }else{
            return null;
        }
    }

    public String getPostparameter(String name){
        if(name != null){
            name = postParametersMap.get(name);
            return name ;
        }else{
            return null;
        }
    }
}
