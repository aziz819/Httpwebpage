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

    private String rootPath = "./src/main/resouces";                 //リソースパス

    private PrintWriter pr;

    private String response;

    private String responseMessage;

    static final int OK = 200 ;

    static final int BAD_REQUEST = 400;

    static final int NOT_FOUND = 404 ;

    static final int Method_Not_Allowed = 405 ;

    static final int HTTP_Version_Not_Supported = 505 ;

    Map<String, String> fileType = new HashMap<>();

    public HttpResponse(int statusCode,String uri, OutputStream ot) throws IOException {

        fileType.put("html", "Content-Type: text/html; charset=utf-8\r\n");
        fileType.put("htm", "Content-Type: text/htm; charset=utf-8\r\n");
        fileType.put("css", "Content-Type: text/css; charset=utf-8\r\n");
        fileType.put("js", "Content-Type: text/javascript; charset=utf-8\r\n");
        fileType.put("png", "Content-Type: image/png\r\n");
        fileType.put("jpg", "Content-Type: image/jpg\r\n");
        fileType.put("jpeg", "Content-Type: image/jpeg\r\n");
        fileType.put("gif", "Content-Type: image/gif\r\n");
        fileType.put("txt", "Content-Type: text/plain\r\n");



        if (statusCode == OK) {
            if(uri.endsWith("/") || uri.length() < 1){
                uri="/index.html";
            }

            File file = new File(rootPath + uri);

            if (file.exists() && file.isFile()) {

                String[] url = uri.split("\\.", 0);

                responseMessage = "HTTP/1.1 200 OK\r\n";

                ot.write(contenttype(url[1]).getBytes());

                BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String line;
                while ((line = bfr.readLine()) != null) {
                    responseMessage += line + "\n";
                }
                System.out.println("\n" + responseMessage);


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
                String[] nofound = uri.split("/");
                responseMessage = "HTTP/1.1 404 Not Found\r\n";
                // response += "Content-Length: " + file.length() + "\r\n";
                responseMessage += "Content-Type: text/html; charset=utf-8" + "\r\n";
                responseMessage += "\r\n";
                responseMessage += "<html><head><title>Not Found</title></head><body><h1>Not_Found</h1><p>要求されたURL:" + nofound[1] + "はこのサーバ上に見つかりませんでした。\n</p></body></html>";
                System.out.println(responseMessage);
                ot.write(responseMessage.getBytes());
            }

        } else if(statusCode == BAD_REQUEST) {
            responseMessage = "HTTP/1.1 400 Bad Request\r\n";
            responseMessage += "Content-Type: text/html; charset=utf-8" + "\r\n";
            responseMessage += "\r\n";
            responseMessage += "<html><head><title>Bad Request</title></head><body><h1>Bad Request</h1><p>The request cannot be fulfilled due to bad syntax.</p></body></html>";
            System.out.println(responseMessage);
            ot.write(responseMessage.getBytes());
        }else if(statusCode == HTTP_Version_Not_Supported){
            responseMessage = "HTTP/1.1 505 Version not supported\r\n";
            responseMessage += "Content-Type: text/html; charset=utf-8" + "\r\n";
            responseMessage += "\r\n";
            responseMessage += "<html><head><title>Version not supported</title></head><body><h1>Version not supported</h1><p>" +
                               "The server does not support the HTTP protocol version used in the request.\r\n</p></body></html>";
            System.out.println(responseMessage);
            ot.write(responseMessage.getBytes());
        }else if(statusCode == Method_Not_Allowed) {
            responseMessage = "HTTP/1.1 405 Method Not Allowed\r\n";
            responseMessage += "Content-Type: text/html; charset=utf-8" + "\r\n";
            responseMessage += "\r\n";
            responseMessage += "<html><head><title>Method Not Allowed</title></head><body><h1>Method Not Allowed</h1><p>" +
                    "A request method is not supported for the requested resource.\r\n</p></body></html>";
            System.out.println(responseMessage);
            ot.write(responseMessage.getBytes());
        }

    }

    public String contenttype(String url) {

        for (String type : fileType.keySet()) {
            if (type.equals(url)) {
                responseMessage += fileType.get(type) + "\r\n";
                break;
            }
        }
        return responseMessage;
    }
}