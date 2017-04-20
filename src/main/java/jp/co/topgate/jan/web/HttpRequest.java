package jp.co.topgate.jan.web;

/**
 *
 * Created by aizijiang.aerken on 2017/04/13.
 */
public class HttpRequest {

    String filename ;

    public HttpRequest(String reQ) {

        String lines[] = reQ.split("\n");       //这是从该行的那块分看一个个放入到桌子里

        System.out.println(lines[0]);       // 从桌子里选出第一个要素  Grt /test.html HTTP/1.1
        lines = lines[0].split(" ");        //在这行把 "Get","/test.html","HTTP/1.1" 从空白的地方又分开放入到桌子里
//        System.out.println(lines[1]);

        filename = lines[1];            //从桌子里取出了第一个要素 "/test.html"
        //System.out.println(filename);


    }
}
