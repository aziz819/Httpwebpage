package jp.co.topgate.jan.web;

/**
 * Created by aizijiang.aerken on 2017/04/13.
 */
public class HttpRequest {

    String filename ;

    public HttpRequest(String reQ) {

        String lines[] = reQ.split("/n");

        filename = lines[0].split("")[1];





    }
}
