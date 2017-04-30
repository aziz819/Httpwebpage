package jp.co.topgate.jan.web;

import java.io.*;

/**
 * Created by aizijiang.aerken on 2017/04/13.
 */
public class HttpResponse {

    private HttpRequest reqs;

    private BufferedReader br = null;

    private InputStreamReader inp = null;

    private String response;

    private String rootpath = "/Users/aizijiang.aerken/homepage/src/main/java/jp/co/topgate/jan/web";

    private PrintWriter pr;

    private String badR;

    static final int bad = 404 ;



    public HttpResponse(HttpRequest request,OutputStream ot) throws IOException {

        reqs = request;

        String fileUri = reqs.getUri();

        File file = new File(rootpath + fileUri);

        if (file.exists() && file.isFile()) {


            String[] url = fileUri.split("\\.", 0);
            switch (url[1]) {
                case "html":
                    //System.out.println("HTTP/1.1 200 OK \r\nContent-Length: " + file.length() + "\r\nContent-Type: text/html; charset=utf-8" + "\r\n");
                    response = "HTTP/1.1 200 OK \r\n";
                    response += "Content-Length: " + file.length() + "\r\n";
                    response += "Content-Type: text/html; charset=utf-8" + "\r\n";
                    response += "\r\n";
                    break;
                case "css":
                    response = "HTTP/1.1 200 OK \r\n";
                    response += "Content-Length: " + file.length() + "\r\n";
                    response += "Content-Type: text/css; charset=utf-8" + "\r\n";
                    response += "\r\n";
                    break;
                case "js":
                    response = "HTTP/1.1 200 OK \r\n";
                    response += "Content-Length: " + file.length() + "\r\n";
                    response += "Content-Type: text/javascript; charset=utf-8" + "\r\n";
                    response += "\r\n";
                    break;
                case "png":
                    response = "HTTP/1.1 200 OK \r\n";
                    response += "Content-Length: " + file.length() + "\r\n";
                    response += "Content-Type: image/png;" + "\r\n";
                    response += "\r\n";
                    break;
                case "jpg":
                case "jpeg":
                    response = "HTTP/1.1 200 OK \r\n";
                    response += "Content-Length: " + file.length() + "\r\n";
                    response += "Content-Type: image/jpeg;" + "\r\n";
                    response += "\r\n";
                    break;
            }

            if (!("bad".equals(badR))) {

                ot.write(response.getBytes());

                BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file))); //テキストは文字化けにならないが画像がなる。
                String line;       //读取html code暂时放入到这个变数里然后添加在response变数后面
                while ((line = bfr.readLine()) != null) {
                    response += line + "\n";
                }
                System.out.println(response);  //在这里显示包括正确访问的句子和html code**/


                FileInputStream fin = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                while (true) {
                    int r = fin.read(bytes);
                    if (r == -1) {
                        break;
                    }

                    ot.write(bytes, 0, r); //通信ソケットに送信するバイトストリームを取得(浏览器显示)
                }


                ot.close();
                fin.close();
            } else {
                response = "HTTP/1.1 404 Bad Request \r\n";
                response += "Content-Type: text/html; charset=utf-8" + "\r\n";
                response += "<html><head><title>Not Found</title></head><body><h1>Bad_Request</h1><p>タイプミス等、リクエストにエラーがあります。\n</p></body></html>";
                response += "\r\n";
                System.out.println(response);
                ot.write(response.getBytes());
            }


        } else {
            String[] nofound = fileUri.split("/");
            response = "HTTP/1.1 404 Not Found \r\n";
            // response += "Content-Length: " + file.length() + "\r\n";
            response += "Content-Type: text/html; charset=utf-8" + "\r\n";
            response += "\r\n";
            response += "<html><head><title>Not Found</title></head><body><h1>Not_Found</h1><p>http://www." + nofound[1] + "のサーバーの DNS アドレスが見つかりませんでした。\n</p></body></html>";
            System.out.println(response);
            ot.write(response.getBytes());
        }

    }

}