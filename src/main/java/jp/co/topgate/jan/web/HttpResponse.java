package jp.co.topgate.jan.web;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aizijiang.aerken on 2017/04/13.
 */

public class HttpResponse {

    private HttpRequest reqs;

    private BufferedReader br = null;

    private InputStreamReader inp = null;

    private String rootpath = "/Users/aizijiang.aerken/homepage/src/main/resource";                 //リソースパス

    private PrintWriter pr;

    String response ;

    String version ;

    static final int BAD_REQUEST = 400 ;

    Map<String,String> filetype = new HashMap<>();



    public HttpResponse(HttpRequest request , OutputStream ot) throws IOException {

        filetype.put("html","Content-Type: text/html; charset=utf-8\r\n");
        filetype.put("htm","Content-Type: text/htm; charset=utf-8\r\n");
        filetype.put("css","Content-Type: text/css; charset=utf-8\r\n");
        filetype.put("js","Content-Type: text/javascript; charset=utf-8\r\n");
        filetype.put("png","Content-Type: image/png\r\n");
        filetype.put("jpg","Content-Type: image/jpg\r\n");
        filetype.put("jpeg","Content-Type: image/jpeg\r\n");
        filetype.put("gif","Content-Type: image/gif\r\n");
        filetype.put("txt","Content-Type: text/plain\r\n");

        if (request.statuscode != BAD_REQUEST) {

            File file = new File(rootpath + request.uri);

            if (file.exists() && file.isFile()) {

                String[] url = request.uri.split("\\.", 0);

                version = "HTTP/1.1 200 OK\r\n";

                ot.write(contenttype(url[1]).getBytes());

                    BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    String line;
                    while ((line = bfr.readLine()) != null) {
                        version += line + "\n";
                    }
                    System.out.println("\n"+version);


                    FileInputStream fin = new FileInputStream(file);
                    byte[] bytes = new byte[1024];
                    while (true) {
                        int r = fin.read(bytes);
                        if (r == -1) {
                            break;
                        }

                        ot.write(bytes, 0, r);      //通信ソケットに送信するバイトストリームを取得(ブラウザーに表示)
                    }


                    ot.close();
                    fin.close();

            } else {
                String[] nofound = request.uri.split("/");
                version = "HTTP/1.1 404 Not Found \r\n";
                // response += "Content-Length: " + file.length() + "\r\n";
                version += "Content-Type: text/html; charset=utf-8" + "\r\n";
                version += "\r\n";
                version += "<html><head><title>Not Found</title></head><body><h1>Not_Found</h1><p>http://www." + nofound[1] + "のサーバーの DNS アドレスが見つかりませんでした。\n</p></body></html>";
                System.out.println(version);
                ot.write(version.getBytes());
            }

        }else{
            version = "HTTP/1.1 400 Bad Request \r\n";
            version += "Content-Type: text/html; charset=utf-8" + "\r\n";
            version += "\r\n";
            version += "<html><head><title>Not Found</title></head><body><h1>Not_Found</h1><p>Bad Request\n</p></body></html>";
            System.out.println(version);
            ot.write(version.getBytes());
        }
    }

    public String contenttype(String url){

        for(String type : filetype.keySet()){
            if(type.equals(url)){
                version += filetype.get(type)+"\r\n";
                break;
            }
        }
        return version ;
    }
}