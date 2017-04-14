package jp.co.topgate.jan.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by aizijiang.aerken on 2017/04/13.
 */
public class HttpResponse {

    HttpRequest request ;

    String response ;

    String rootpath = "//Users/aizijiang.aerken/jan.html";

    public HttpResponse(HttpRequest request)  {

        this.request = request ;

        File file = new File(rootpath + request.filename);


        try {
            /**
             * HTTP/1.1 200 OK
             Date: Fri, 14 Apr 2017 07:10:57 GMT
             Server: Apache/2.4.23 (Unix) PHP/5.6.25
             Content-Location: index.html.en
             Vary: negotiate
             TCN: choice
             X-Powered-By: PHP/5.6.25
             Content-Length: 108
             Content-Type: text/html; charset=UTF-8
             *
             * **/

            response = "HTTP/1.1 200 OK ¥r¥n";
            response ="Content-Length: " + rootpath.length();
            response ="Content-Type: text/html; charset=UTF-8";
            response ="¥r¥n";

            FileInputStream finput = new FileInputStream(file);

            int s ;

            while((s = finput.read()) != -1){
                response += (char) s;
            }

            finput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            response = response.replace("200","404");
        }catch(Exception e){
            response = response.replace("200","500");

        }

    }
}
