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

    private static final int REQUEST_LINE_QUANTYTI = 3;                             // リクエストラインを分割してから３か

    private static final int PARAMETER_ATTRIBUTE_VALUE = 2 ;

    private static final String REQUEST_LINE_SPACE_SEPARATE = " ";                  // リクエスト行を空白の所から分割

    private static final String HEADER_SEPARATE = ":";                              // リクエストヘッダーのフィールドと値を分割

    private static final String GET_QUERY_EQUAL_SEPARATE = "=";                     // リクエスト行でのパラメーターの属性と値を分割

    private static final String QUERY_PARAMETER_SEPARATE = "&";                     // パラメーターとパラメーターを分割

    private static final String GET_QUERY_QUEASTION_SEPARATE = "\\?";               // uriとクエリーを分割

    private BufferedReader bfin;

    private static String headerLine;                                               // リクエストヘッダーを一行ずつ代入する一時的な変数

    private String method;                                                                 // HTTPメソッド

    private String uri;                                                                    // URL

    private String version;                                                                // HTTPバージョン

    private Map<String, String> header_Key_value = new HashMap<>();                        // リクエストヘッダーのフィールドと値を扱う

    private Map<String, String> post_Parameter_attribute_value = new HashMap<>();          // ポストクエリーを扱う

    private Map<String, String> get_Parameter_attribute_value = new HashMap<>();           // ゲットクエリーを扱う


    public HttpRequest(InputStream in) throws RuntimeException, IOException {

        if (in == null) {
            throw new IOException("入力ストリームはnullになっています。");
        }

        try {

            bfin = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            String requestLine = bfin.readLine();                                        // 取り敢えずリクエストの一行目を読み取る

            if (requestLine != null) {

                System.out.println(requestLine);

                String[] str1 = requestLine.split(REQUEST_LINE_SPACE_SEPARATE);         // REQUEST_LINE_SPACE_SEPARATE == " " / 一行目を空白のところから三つに分ける


                if (str1.length == REQUEST_LINE_QUANTYTI) {                             // REQUEST_LINE_QUANTYTI == 3

                    method = str1[0];                                                   // メソッド
                    uri = str1[1];                                                      // URI
                    version = str1[2];                                                  // バージョン

                    while ((headerLine = bfin.readLine()) != null && !headerLine.equals("")) {    // 二行目からヘッダーフィールドと値を読み取る

                        String[] header = headerLine.split(HEADER_SEPARATE);                     // HEADER_SEPARATE  == ":"

                        if (header.length == PARAMETER_ATTRIBUTE_VALUE) {                        // PARAMETER_ATTRIBUTE_VALUE == 2

                            header[1] = header[1].trim();

                            header_Key_value.put(header[0], header[1]);

                        } else if (header.length > 2) {

                            header[1] = header[1].trim();

                            header_Key_value.put(header[0], header[1] + HEADER_SEPARATE + header[2]);         // ヘッダーに　”：”コロンが多かったら

                        }

                        System.out.println(headerLine);                                                       // ヘッダーフィールドと値
                    }

                    if (("GET".equals(method))) {
                        if (uri.matches(".*" + GET_QUERY_QUEASTION_SEPARATE + ".*")) {                  // GET_QUERY_QUEASTION_SEPARATE == "?"疑問符があるかどうかの判断
                            String[] get1 = uri.split(GET_QUERY_QUEASTION_SEPARATE);
                            uri = get1[0];
                            String[] get2 = get1[1].split(QUERY_PARAMETER_SEPARATE);                          // QUERY_PARAMETER_SEPARATE == "&"
                            for (String get3 : get2) {
                                String[] get4 = get3.split(GET_QUERY_EQUAL_SEPARATE);                         // GET_QUERY_EQUAL_SEPARATE == "="
                                if(get4.length == PARAMETER_ATTRIBUTE_VALUE){
                                    get_Parameter_attribute_value.put(get4[0], get4[1]);
                                }
                                else{
                                    throw new RuntimeException("正しくないGETパラメーター:" + get3);
                                }
                            }
                        }
                    } else if (("POST".equals(method))) {
                        String parameterLine;
                        while ((parameterLine = bfin.readLine()) != null) {            // ボディーメッセージを読み取り
                            String[] post1 = parameterLine.split(QUERY_PARAMETER_SEPARATE);
                            for (String post2 : post1) {
                                String[] post3 = post2.split(GET_QUERY_EQUAL_SEPARATE);
                                if(post3.length == PARAMETER_ATTRIBUTE_VALUE){
                                    post_Parameter_attribute_value.put(post3[0], post3[1]);
                                }else{
                                    throw new RuntimeException("正しくないPOSTパラメーター:" + post2);
                                }
                            }
                        }
                    }
                } else {
                    throw new RuntimeException("正しくないriquestLine");
                }
            } else {
                throw new RuntimeException("riquestLineがnullになっています。");
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
            name = get_Parameter_attribute_value.get(name);
            return name ;
        }else{
            return null;
        }
    }

    public String getPostparameter(String name){
        if(name != null){
            name = post_Parameter_attribute_value.get(name);
            return name ;
        }else{
            return null;
        }
    }
}
