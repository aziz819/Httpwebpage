package jp.co.topgate.jan.web;

import java.util.Map;

/**
 * リクエスト分析結果を持つ
 * Created by aizijiang.aerken on 2017/06/15.
 */
public class RequestMessage {

    private String method;
    private String url;
    private String version;
    private Map<String, String> parameters;

    /**
     * @param method         HTTPメソッド
     * @param url            　URL
     * @param version        HTTPバージョン
     * @param parameters     　リクエストパラメータ
     */

    RequestMessage(String method, String url, String version, Map<String, String> parameters) {
        this.method = method;
        this.url = url;
        this.version = version;
        this.parameters = parameters;
    }

    /**
     * @return HTTPメソッドを返す
     */

    public String getMethod() {
        return this.method;
    }

    /**
     * @return URLを返す
     */

    String getUrl() {
        return this.url;
    }

    /**
     * @return HTTPバージョンを返す
     */

    String getVersion() {
        return this.version;
    }

    /**
     * @return リクエストパラメータを返す
     */

    public Map<String, String> getParameters() {
        return parameters;
    }
}
