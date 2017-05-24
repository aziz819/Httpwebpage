package jp.co.topgate.jan.web;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/* Fileクラスを継承してファイル有無の確認＆ファイル拡張子によってContentTypeをセットする
 * Created by aizijiang.aerken on 2017/05/10.
 */


public class FileResources extends File {

    private static final String rootPath = "./src/main/resources";

    private String url ;

    final static Map<String, String> contentType = new HashMap<>();


    public FileResources(String uri) {

        super(rootPath + uri);

        url = uri ;

        contentType.put("html", "Content-Type: text/html; charset=utf-8\n");
        contentType.put("htm", "Content-Type: text/htm; charset=utf-8\n");
        contentType.put("css", "Content-Type: text/css; charset=utf-8\n");
        contentType.put("js", "Content-Type: text/javascript; charset=utf-8\n");
        contentType.put("png", "Content-Type: image/png\n");
        contentType.put("jpg", "Content-Type: image/jpg\n");
        contentType.put("jpeg", "Content-Type: image/jpeg\n");
        contentType.put("gif", "Content-Type: image/gif\n");
        contentType.put("txt", "Content-Type: text/plain\n");
        contentType.put("audio", "Content-Type: audio/mpeg\n");
        contentType.put("mp4", "Content-Type: audio/mp4\n");
        contentType.put("mpeg", "Content-Type: video/mpeg\n");
        contentType.put("csv", "Content-Type: text/csv\n");
        contentType.put("ico", "Content-Type: image/x-icon\n");

    }




    /*
     *ファイル拡張子によってContentTypeのセット
     */

    public String getContentType(int statusCode) {


        String extension = "";

        if (url != null && !"".equals(url) && statusCode == StatusLine.OK) {

            for (String key : contentType.keySet()) {

                if (url.endsWith(key)) {

                    extension = key;        // ファイル拡張子と一致するキーを獲得

                    break;
                }
            }
        }

        /*
         * 獲得したキーの値(Content-Type)を返す,一致しなかったらdefaultValueの値を返す
         */

        return contentType.getOrDefault(extension, "Content-Type: text/html; charset=utf-8\n");
    }


}
