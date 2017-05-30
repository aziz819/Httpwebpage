package jp.co.topgate.jan.web;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aizijiang.aerken on 2017/05/10.
 *
 * @author jan
 */


public class FileResources extends File {

    private static final String rootPath = "./src/main/resources";

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


    public FileResources(String uri) {
        super(rootPath + uri);
        url = uri;
    }


    /**
     *
     * @param statusCode
     * @return Content-typを返す、拡張子がみつからなかった場合はデフォルトのContent-Typeを返す
     */

    public String getContentType(int statusCode) {
        String extension = "";
        if (url != null && !"".equals(url) && statusCode == StatusLine.OK) {
            for (String key : contentType.keySet()) {
                if (url.endsWith(key)) {
                    extension = key;
                    break;
                }
            }
        }

        return contentType.getOrDefault(extension, "application/octet-stream");
    }
}
