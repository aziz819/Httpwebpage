package jp.co.topgate.jan.web;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aizijiang.aerken on 2017/05/10.
 */
public class FileResources extends File {
    private boolean filecheck ;
    private static final String rootPath = "./src/main/resouces";                 //リソースパス
    String url ;
    private String contentType = "Contesnt-Type:";

    Map<String, String> fileType = new HashMap<>();


    public FileResources(String uri){
        super(rootPath + uri);
        this.url = uri ;
        String[] extension  = uri.split("\\.");
        fileType.put("html", "Content-Type: text/html; charset=utf-8\r\n");
        fileType.put("htm", "Content-Type: text/htm; charset=utf-8\r\n");
        fileType.put("css", "Content-Type: text/css; charset=utf-8\r\n");
        fileType.put("js", "Content-Type: text/javascript; charset=utf-8\r\n");
        fileType.put("png", "Content-Type: image/png\r\n");
        fileType.put("jpg", "Content-Type: image/jpg\r\n");
        fileType.put("jpeg", "Content-Type: image/jpeg\r\n");
        fileType.put("gif", "Content-Type: image/gif\r\n");
        fileType.put("txt", "Content-Type: text/plain\r\n");


        for (String type : fileType.keySet()) {
            if (type.equals(extension[1])) {
                contentType += fileType.get(type) + "\r\n";
                System.out.println(contentType);

                break;
            }

        }
        if(contentType == null){
            contentType += "application/octet-stream";
        }

    }

    public String getContenType(){
        if(contentType == null || "".equals(contentType)){
            throw  new RuntimeException("ContentTypeはnullになっています。");
        }
        return contentType;
    }


}
