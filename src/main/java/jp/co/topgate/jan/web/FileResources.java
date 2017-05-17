package jp.co.topgate.jan.web;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/* Fileクラスを継承してファイル有無の確認＆ファイル拡張子によってContentTypeをセットする
 * Created by aizijiang.aerken on 2017/05/10.
 */


public class FileResources extends File {

    private static final String rootPath = "./src/main/resources";

    private String contentType;

    private String url ;

    Map<String, String> fileType = new HashMap<>();


    public FileResources(String uri) {

        super(rootPath + uri);

        url = uri ;

        fileType.put("html", "Content-Type: text/html; charset=utf-8\n");
        fileType.put("htm", "Content-Type: text/htm; charset=utf-8\n");
        fileType.put("css", "Content-Type: text/css; charset=utf-8\n");
        fileType.put("js", "Content-Type: text/javascript; charset=utf-8\n");
        fileType.put("png", "Content-Type: image/png\n");
        fileType.put("jpg", "Content-Type: image/jpg\n");
        fileType.put("jpeg", "Content-Type: image/jpeg\n");
        fileType.put("gif", "Content-Type: image/gif\n");
        fileType.put("txt", "Content-Type: text/plain\n");
        fileType.put("audio", "Content-Type: audio/mpeg\n");
        fileType.put("mp4", "Content-Type: audio/mp4\n");
        fileType.put("mpeg", "Content-Type: video/mpeg\n");
        fileType.put("csv", "Content-Type: text/csv\n");

    }

    /*
     *ファイル拡張子によってContentTypeのセット
     */

    public String getContentType() {

        if (url != null && !"".equals(url)) {
            for (String type : fileType.keySet()) {
                if (url.endsWith(type)) {
                    contentType = type;
                    break;
                }

            }
        }

        return fileType.getOrDefault(contentType, "Content-Type: text/html; charset=utf-8\n");
    }


}
