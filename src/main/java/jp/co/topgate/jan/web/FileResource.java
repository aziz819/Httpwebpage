package jp.co.topgate.jan.web;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aizijiang.aerken on 2017/05/10.
 *
 * @author jan
 */


public class FileResource {

    private static final String rootPath = "./src/main/resources";

    private File file = null;

    private static String url;

    static final Map<String, String> contentType = new HashMap<>();

    static {

        contentType.put("html", "text/html; charset=utf-8");
        contentType.put("htm", "text/htm; charset=utf-8");
        contentType.put("css", "text/css; charset=utf-8");
        contentType.put("js", "text/javascript; charset=utf-8");
        contentType.put("png", "image/png");
        contentType.put("jpg", "image/jpg");
        contentType.put("jpeg", "image/jpeg");
        contentType.put("gif", "image/gif");
        contentType.put("txt", "text/plain");
        contentType.put("audio", "audio/mpeg");
        contentType.put("mp4", "audio/mp4");
        contentType.put("mpeg", "video/mpeg");
        contentType.put("csv", "text/csv");
        contentType.put("ico", "image/x-icon");

    }


    /**
     * @param url
     * @return ファイル存在すればtrueを存在しなければfalseを返す
     */

    public boolean checkFile(String url) {

        file = new File(rootPath + url);
        this.url = url;

        if (file.exists() || file.isFile()) {
            return true;
        }
        return false;
    }

    /**
     * @return ファイルが存在した時にそのファイルパスを返す
     */

    public File getPath() {
        return file;
    }


    /**
     *
     * @return Content-typを返す、拡張子がみつからなかった場合はデフォルトのContent-Typeを返す
     */

    public String getContentType() {
        String extension = "";
        if (url != null && !"".equals(url)) {
            for (String key : contentType.keySet()) {
                if (url.endsWith(key)) {
                    extension = key;
                    break;
                }
            }
        }

        return contentType.getOrDefault(extension, "application/octet-stream");
    }

    /**
     * @return エラーのメッセージボディのContent-Typeを返す
     */

    public String getErrorBodyContentType() {
        return "text/html; charset=utf-8";
    }
}
