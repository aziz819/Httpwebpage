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

    private static String requestLine = null;                             // リクエスト行格納、テスト用のためクラスメンバにしました。

    private static Map<String, String> requestParameters = null;          // ポストクエリーを扱う

    private static final String HEADERS_LENGTH = "headersLength";

    private static final String CONTENT_LENGTH ="Content-Length";

    private static final String GET = "GET";


    /**
     * リクエスト行　＆　メッセージ・ヘッダー　＆　パラメーターを部分ごとに各メソッドで処理する
     * 分析結果をRequestMessageのコンストラクタに格納する
     *
     * @param is 入力ストリーム
     * @return RequestMessageのコンストラクタを返す
     */
    static RequestMessage parseRequest(InputStream is) {

        Objects.requireNonNull(is, "入力ストリームがnullになっています");

        BufferedInputStream bufIn = new BufferedInputStream(is);

        try {
            bufIn.mark(bufIn.available());
        } catch (IOException e) {
            e.printStackTrace();
        }


        /*
         * リクエスト行の分析結果を受け取る
         */
        String[] requestLineElement = parseRequestLine(bufIn);

        method = requestLineElement[0];
        url = requestLineElement[1];
        version = requestLineElement[2];

        try {
            bufIn.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
         * リクエストヘッダーの分析結果を受け取る
         */
        Map<String, String> requestHeaders = parseRequestHeaders(bufIn);

        try {
            bufIn.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
         * パラメーター分析結果を受け取る
         */
        requestParameters = parseParameter(bufIn, requestHeaders);

        return new RequestMessage(method, url, version, requestParameters);

    }


    /**
     *　リクエストラインを分割する
     *
     * @param is 入力ストリーム
     * @return リクエストライン要素を返す
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

        return requestLine.split(REQUEST_LINE_SEPARATOR);

    }


    /**
     *　リクエストヘッダーを分割してマップに格納する
     *
     * @param is　入力ストリーム
     * @return マップオブジェクトparseRequestHeadersを返す
     */
    private static Map<String, String> parseRequestHeaders(InputStream is) {
        String headers;
        Map<String, String> parseRequestHeaders = new HashMap<>();
        StringBuilder builder = new StringBuilder();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            headers = bufferedReader.readLine();
            builder.append(headers).append("\r\n");

            while ((headers = bufferedReader.readLine()) != null && !headers.equals("")) {
                builder.append(headers).append("\r\n");

                String[] header = headers.split(HEADERS_SEPARATOR, 2);

                if (header.length == HEADERS_SIZE) {
                    header[1] = header[1].trim();
                    parseRequestHeaders.put(header[0], header[1]);
                }
            }
            builder.append("\r\n");
        } catch (IOException e) {
            throw new RequestParseException("リクエストヘッダーの読み込みに不具合が発生しました");
        }
        String stringContentLength = String.valueOf(builder.toString().length()); // リクエストの長さ
        parseRequestHeaders.put(HEADERS_LENGTH, stringContentLength);

        return parseRequestHeaders;
    }


    /**
     *　パラメーターを分割してマップに格納
     *
     * @param bufIn バッファー入力ストリーム
     * @param requestHeaders　リクエストヘッダー情報をもつマップ
     * @return マップオブジェクトparseRequestParametersを返す
     */
    private static Map<String, String> parseParameter(BufferedInputStream bufIn, Map<String, String> requestHeaders) {

        String[] attributeAndValue;
        Map<String, String> parseRequestParameters = new HashMap<>();

        if (method.equals(GET)) {
            if (url.matches(".*" + GET_QUERY_QUEASTION_SEPARATE + ".*")) {
                String[] parameterAcquisition = url.split(GET_QUERY_QUEASTION_SEPARATE, 2);
                url = parameterAcquisition[0];
                for (String parameters : parameterAcquisition[1].split(QUERIES_PARAMETERS_SEPARATE)) {
                    attributeAndValue = parameters.split(QUERIES_SEPARATOR, 2);
                    if (attributeAndValue.length != PARAMETER_SIZE) {
                        throw new RequestParseException("正しくないGETパラメーター:" + parameters);
                    }
                    parseRequestParameters.put(attributeAndValue[0], attributeAndValue[1]);
                }
            }
        } else {

            String stringParameter = convertingParameterLengthToString(bufIn, requestHeaders);

            for (String parameters : stringParameter.split(QUERIES_PARAMETERS_SEPARATE)) {
                attributeAndValue = parameters.split(QUERIES_SEPARATOR, 2);
                if (attributeAndValue.length != PARAMETER_SIZE) {
                    throw new RequestParseException("正しくないPOSTパラメーター:" + parameters);
                }
                parseRequestParameters.put(attributeAndValue[0], attributeAndValue[1]);
            }
        }

        return parseRequestParameters;
    }

    /**
     * リクエストの長さをスキップしてコンテントの長さでメッセージボディでのパラメータを読み込む
     *
     * @param is 入力ストリーム
     * @param requestHeaders　リクエストの情報を持っているマップ
     * @return パラメータを返す
     */
    private static String convertingParameterLengthToString(InputStream is, Map<String, String> requestHeaders) {

        int HeadersLength = Integer.parseInt(requestHeaders.get(HEADERS_LENGTH));
        int integerParameter = Integer.parseInt(requestHeaders.get(CONTENT_LENGTH));

        try {
            long loadedPart = is.skip(HeadersLength); // リクエストの長さでボディメッセージまでをスキップする
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] byteContentLength = new byte[integerParameter];

        try {
            int i = is.read(byteContentLength);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String stringPostParameter = new String(byteContentLength);

        try {
            stringPostParameter = URLDecoder.decode(stringPostParameter, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return stringPostParameter;

    }


    /**
     *
     * @return Httpメソッドを返す
     */
    public String getMethod() {

        return method;
    }

    /**
     *
     * @return URLを返す
     */
    public String getUrl() {

       if (url.endsWith("/")) {
           url = url + "/index.html";
       }
        return url;
    }


    /**
     *
     * @return Httpバージョンを返す
     */
    public String getVersion() {
        return version;
    }



    /**
     *
     * @param name パラメーター属性
     * @return パラメーターの値を返す、一致しなかった場合はnullを返す
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
     * @return パラメーターの値を返す、一致しなかった場合はnullを返す
     */
    public String getPostParameter(String name) {
        if(name != null){
            name = requestParameters.get(name);
            return name ;
        }else{
            return null;
        }
    }

    /**
     *
     * @return リクエスト行を返す
     */
    public String getRequestLine(){
        return requestLine;
    }
}
