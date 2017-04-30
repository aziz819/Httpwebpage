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

    private static final int  REQUEST_LINE_QUANTYTI = 3 ; // リクエストラインを分割してから３か

    private static final String REQUEST_LINE_SPACE_SEPARATE = " "; // リクエスト行を空白の所から分割

    private static final String HEADER_SEPARATE = ":"; // リクエストヘッダーのフィールドと値を分割

    private static final String GET_QUERY_EQUAL_SEPARATE = "="; // リクエスト行でのパラメーターの属性と値を分割

    private static final String QUERY_PARAMETER_SEPARATE = "&"; // パラメーターとパラメーターを分割

    private static final String GET_QUERY_QUEASTION_SEPARATE = "\\?"; // uriとクエリーを分割

    private  static String HEADER_LINE ; // リクエストヘッダーを一行ずつ代入する一時的な変数

    private  BufferedReader bfin ;

    String method ; // HTTPメソッド

    String uri ; // URL

    String version ; // HTTPバージョン

    static final int OK = 200 ;

    static final int BAD = 400 ;

    static final int NOT = 404 ;

    int statuscode ;

    Map<String,String> HAEDER_FIELD_VALUE = new HashMap<>(); // リクエストヘッダーのフィールドと値を扱う

    Map<String,String> POST_REQUEST_PARAMETER_EQUAL_SEPARATE = new HashMap<>(); //

    Map<String,String> GET_PARAMETER_EQUAL_SEPARATE = new HashMap<>();


    public HttpRequest(InputStream in) throws IOException {

        bfin = new BufferedReader(new InputStreamReader(in,"UTF-8"));

        String requestLine = bfin.readLine();   // 取り敢えずリクエストの一行目を読み取る

        System.out.println(requestLine);

        String[] str1 = requestLine.split(" "); // 一行目を空白のところから三つに分ける


        if(str1.length == 3) {

            method = str1[0];       // GET　か　POSTか
            uri = str1[1];         // URI 例：/index.html か　/index.html?name=jan&sex=man
            version = str1[2];     // HTTPバージョン　の　HTTP/1.1

            while ((HEADER_LINE = bfin.readLine()) != null && !HEADER_LINE.equals("")) { // 二行目からヘッダーフィールドと値を読み取る

                String[] header = HEADER_LINE.split(":");

                if (header.length == 2) {

                    header[1] = header[1].trim();

                    HAEDER_FIELD_VALUE.put(header[0], header[1]);

                } else if (header.length > 2) {

                    header[1] = header[1].trim();

                    HAEDER_FIELD_VALUE.put(header[0], header[1] + ":" + header[2]); // ヘッダーに　”：”コロンが多かったら

                }

                System.out.println(HEADER_LINE); // ヘッダーフィールドと値
            }

            if (("GET".equals(method))) {
                if (uri.matches(".*" + "\\?" + ".*")) { // 疑問符があるかどうかの判断
                    String[] get1 = uri.split("\\?");
                    method = get1[0];
                    String[] get2 = get1[1].split("&");
                    for (String get3 : get2) {
                        String[] get4 = get3.split("=");
                        GET_PARAMETER_EQUAL_SEPARATE.put(get4[0], get4[1]);
                    }
                }
            } else if (("POST".equals(method))) {
                String parameterLine;
                while ((parameterLine = bfin.readLine()) != null) { // ボディーメッセージを読み取り
                    String[] post1 = parameterLine.split("&");
                    for (String post2 : post1) {
                        String[] post3 = post2.split("=");
                        POST_REQUEST_PARAMETER_EQUAL_SEPARATE.put(post3[0], post3[1]);
                    }

                }
            } else {
                statuscode = 404 ;
            }
        }
    }

    public String getUri(){
        return uri;
    }
}
