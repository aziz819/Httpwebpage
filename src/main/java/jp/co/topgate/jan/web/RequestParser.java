package jp.co.topgate.jan.web;

import jp.co.topgate.jan.web.exception.RequestParseException;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *  リクエストを分析し、結果を返す
 * Created by aizijiang.aerken on 2017/04/13.
 */

public class RequestParser {

    private static final int REQUEST_LINE_SIZE = 3;                       // リクエストラインの分割結果3つか

    private static final int PARAMETER_SIZE = 2;                          // パラメーターの分割結果２つか

    private static final int HEADERS_SIZE = 2;                            // ヘッダーをコロンから分割して２つか

    private static final String REQUEST_LINE_SEPARATOR = " ";             // リクエスト行を空白の所から分割

    private static final String HEADERS_SEPARATOR = ":";                  // リクエストヘッダーのフィールドと値を分割

    private static final String QUERIES_SEPARATOR = "=";                  // リクエスト行でのパラメーターの属性と値を分割

    private static final String QUERIES_PARAMETERS_SEPARATE = "&";        // パラメーターとパラメーターを分割

    private static final String GET_QUERY_QUEASTION_SEPARATE = "\\?";     // uriとクエリーを分割

    private static String method;

    private static String url;

    private static String version;

    private static String requestLine = null;                                  // リクエスト行格納、テスト用のためクラスメンバにしました。

    private static Map<String, String> requestHeaders = new HashMap<>();             // リクエストヘッダーのフィールドと値を扱う

    private static Map<String, String> requestParameters = null;          // ポストクエリーを扱う

    private static int HeadersLength;

    /*
     *  リクエスト行　＆　メッセージ・ヘッダー　＆　パラメーターを部分ごとに各メソッドで処理する
     */

    public static RequestMessage parseRequest(InputStream is) {

        Objects.requireNonNull(is, "入力ストリームがnullになっています");

        BufferedInputStream bufIn = new BufferedInputStream(is);  // 主要是打个标签再次读度过的部分

        try {
            bufIn.mark(bufIn.available()); // 确保整个收到的数据的长度，并且打个记号
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
         * リクエスト行の解析
         */

        String[] requestLineElement = parseRequestLine(bufIn);

        method = requestLineElement[0];
        url = requestLineElement[1];
        version = requestLineElement[2];

        /*
         * リクエストヘッダーの解析
         */

        try {
            bufIn.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseRequestHeaders(bufIn);

        /*
         * パラメーターの解析
         */

        try {
            bufIn.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseParameter(bufIn);


        return new RequestMessage(method, url, version, requestHeaders, requestParameters);

    }


    /*
     * リクエスト行の確認　＆　分割処理
     */

    private static String[] parseRequestLine(InputStream is) {
        String requestLine;

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            requestLine = bufferedReader.readLine();
        } catch (IOException e) {
            throw new RequestParseException("リクエスト行の読み込みに不具合が発生しました");
        }

        if (requestLine == null || requestLine.equals("")) {
            throw new RequestParseException("リクエスト行が空かnullになっています");
        }

        return requestLine.split(REQUEST_LINE_SEPARATOR);                   // REQUEST_LINE_SEPARATOR == " " / 一行目を空白のところから三つに分ける

    }


    /*
     * リクエストヘッダーの確認　＆　分割処理
     */

    private static void parseRequestHeaders(InputStream is) {
        String headers;

        StringBuilder builder = new StringBuilder();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            headers = bufferedReader.readLine();
            builder.append(headers).append("\r\n");

            while ((headers = bufferedReader.readLine()) != null && !headers.equals("")) {    // 二行目からヘッダーフィールドと値を読み取る
                builder.append(headers).append("\r\n");

                String[] header = headers.split(HEADERS_SEPARATOR, 2);                      // HEADERS_SEPARATOR  == ":"

                if (header.length == HEADERS_SIZE) {                                               // HEADER_ATTRIBUTE_VALUE == 2
                    header[1] = header[1].trim();
                    requestHeaders.put(header[0], header[1]);
                }
            }
            builder.append("\r\n");
        } catch (IOException e) {
            throw new RequestParseException("リクエストヘッダーの読み込みに不具合が発生しました");
        }
        HeadersLength = builder.toString().length(); // リクエストの長さ
    }


    /*
     * パラメーターの確認　＆　分割処理
     */

    private static void parseParameter(BufferedInputStream bufIn) {

        String[] attributeAndValue;

        requestParameters = new HashMap<>();

        String contentLength = requestHeaders.get("Content-Length");

        if (method.equals("GET")) {
            if (url.matches(".*" + GET_QUERY_QUEASTION_SEPARATE + ".*")) {                        // GET_QUERY_QUEASTION_SEPARATE == "?"疑問符があるかどうかの判断
                String[] parameterAcquisition = url.split(GET_QUERY_QUEASTION_SEPARATE, 2);
                url = parameterAcquisition[0];                                                          // url
                for (String parameters : parameterAcquisition[1].split(QUERIES_PARAMETERS_SEPARATE)) {  // QUERY_PARAMETER_SEPARATE == "&"
                    attributeAndValue = parameters.split(QUERIES_SEPARATOR, 2);                  // QUERIES__SEPARATOR == "="
                    if (attributeAndValue.length != PARAMETER_SIZE) {                                 // PARAMETER_ATTRIBUTE_VALUE == 2
                        throw new RequestParseException("正しくないGETパラメーター:" + parameters);
                    }
                    requestParameters.put(attributeAndValue[0], attributeAndValue[1]);
                }
            }
        } else {

            String stringParameter = convertingParameterLengthToString(bufIn, contentLength);    // パラメーターを取得

            for (String parameters : stringParameter.split(QUERIES_PARAMETERS_SEPARATE)) {
                attributeAndValue = parameters.split(QUERIES_SEPARATOR, 2);
                if (attributeAndValue.length != PARAMETER_SIZE) {
                    throw new RequestParseException("正しくないPOSTパラメーター:" + parameters);
                }
                requestParameters.put(attributeAndValue[0], attributeAndValue[1]);
            }
        }
    }

    /**
     * パラメータの長さをまずバイト型に変換して、それをまた文字列に変換する
     *
     * @param parameter Content-Lengthパラメータの長さ
     * @return パラメータを文字列に変換して返す
     */

    private static String convertingParameterLengthToString(InputStream is, String parameter) {

        try {
            long loadedPart = is.skip(HeadersLength); // リクエストの長さでボディメッセージまでをスキップする
        } catch (IOException e) {
            e.printStackTrace();
        }

        int integerParameter = Integer.parseInt(parameter);

        byte[] byteContentLength = new byte[integerParameter];

        try {
            int i = is.read(byteContentLength);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String stringpostParameter = new String(byteContentLength);

        try {
            stringpostParameter = URLDecoder.decode(stringpostParameter, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return stringpostParameter;

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
            name = requestParameters.get(name);
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
            name = requestParameters.get(name);
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
