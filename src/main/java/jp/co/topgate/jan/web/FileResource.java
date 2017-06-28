package jp.co.topgate.jan.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * ファイルパスを設定してファイルをチェックをして結果を返す
 * Created by aizijiang.aerken on 2017/05/10.
 *
 * @author jan
 */


public class FileResource {

    private static final String ROOT_PATH = "./src/main/resources";

    private static final String WEB_APP = "/program/board/";

    private static final String FIRST_PAGE_NAME = "index.html";

    private File file = null;

    private String url;

    private static final Map<String, String> contentType = new HashMap<>();

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
     *
     * @param uri  Fileクラスコンストラクタにパスを指定
     */

    FileResource(String uri) {
        this.url = getFirstPagePath(getFirstPage(uri));
        file = new File(url);
    }


    /**
     * @return ファイル存在すればtrueを存在しなければfalseを返す
     */

    public boolean checkFile() {

        return (file.exists() && file.isFile());
    }

    /**
     * @return ファイルが存在した時にそのファイルパスを返す
     */

    public String getPath() {

        return url;
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
     * @return htmlのContent-Typeを返す
     */

    public String fixedContentType() {
        return "text/html; charset=utf-8";
    }


    private String getFirstPage(String url) {

        if (url.equals("/program/board/")) {
            file = new File(ROOT_PATH + WEB_APP + FIRST_PAGE_NAME);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file.toString();
        }

        return ROOT_PATH + url;

        /*if (!url.endsWith("/")) {
            return url;
        }
        return url + FIRST_PAGE_NAME;*/
    }

    private String getFirstPagePath(String url) {
        Path path = Paths.get(url).normalize();

        return path.toString();
    }
}
