package jp.co.topgate.jan.web;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aizijiang.aerken on 2017/05/10.
 */
public class FileResources extends File {
    private boolean filecheck ;
    private static final String rootPath = "./src/main/resources";                 //リソースパス
    private String contentType;
    private String url ;

    Map<String, String> fileType = new HashMap<>();


    public FileResources(String uri){
        super(rootPath + uri);
        url = uri ;
        fileType.put("html", "Content-Type: text/html; charset=utf-8\r\n");
        fileType.put("htm", "Content-Type: text/htm; charset=utf-8\r\n");
        fileType.put("css", "Content-Type: text/css; charset=utf-8\r\n");
        fileType.put("js", "Content-Type: text/javascript; charset=utf-8\r\n");
        fileType.put("png", "Content-Type: image/png\r\n");
        fileType.put("jpg", "Content-Type: image/jpg\r\n");
        fileType.put("jpeg", "Content-Type: image/jpeg\r\n");
        fileType.put("gif", "Content-Type: image/gif\r\n");
        fileType.put("txt", "Content-Type: text/plain\r\n");

        if(uri != null && !"".equals(this.url)) {
            for (String type : fileType.keySet()) {
                if (uri.endsWith(type)) {
                    contentType = type;
                    //contentType = fileType.get(type) + "\r\n";
                     break;
                }

            }
        }

    }

    public String getContenType(int statusCode){

        if(statusCode != StatusLine.OK){
            return fileType.get("html");
        }
       return fileType.getOrDefault(contentType,"Content-Type: text/html; charset=utf-8\n");


       /**if(contentType == null || "".equals(contentType)){
            throw new RuntimeException("Content-Type:はnullになっています。");
        }
        return contentType **/
    }


}
