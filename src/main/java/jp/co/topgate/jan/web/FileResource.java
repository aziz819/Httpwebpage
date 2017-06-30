package jp.co.topgate.jan.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * 実際受け取ったurlでパスを作り、ファイルチェックの処理を行う
 * Created by aizijiang.aerken on 2017/05/10.
 *
 * @author jan
 */

public class FileResource {

    private static final String ROOT_PATH = "./src/main/resources/";

    private static final String WEB_APP = "/program/board/";

    private static final String FIRST_PAGE_NAME = "/index.html";

    private File file = null;

    private String url;

    private static final String DYNAMIC_URL = "/program/board/";

    private static final String HTML_CONTENT = "text/html; charset=utf-8";

    private static final String DEFAULT_CONTENT = "application/octet-stream";

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
     * @param uri  実際のurl
     */
    FileResource(String uri) {
        this.url = getFirstPagePath(getFirstPage(uri));
        file = new File(url);
    }


    /**
     * ファイル有無チェック
     *
     * @return ファイル存在すればtrueを存在しなければfalseを返す
     */
    boolean checkFile() {
        return (file.exists() && file.isFile());
    }


    /**
     * @return パスを返す
     */
    String getPath() {

        return url;
    }


    /**
     * ファイル拡張子を確定する、拡張子がみつからなかった場合はデフォルトのapplication/octet-streamを返す
     *
     * @return Content-typを返す
     */
    String getContentType() {
        String extension = "";
        if (url != null && !"".equals(url)) {
            for (String key : contentType.keySet()) {
                if (url.endsWith(key)) {
                    extension = key;
                    break;
                }
            }
        }
        return contentType.getOrDefault(extension, DEFAULT_CONTENT);
    }

    /**
     * エラーメッセージボディの場合に呼び出される
     *
     * @return Content-Typeを返す
     */
    String fixedContentType() {
        return HTML_CONTENT;
    }


    /**
     * 実際のurlによってパスを組む
     *
     * @param url 実際のurl
     * @return パスを組んで返す
     */
    private String getFirstPage(String url) {

        if (url.equals(DYNAMIC_URL)) {
            file = new File(ROOT_PATH + WEB_APP + FIRST_PAGE_NAME);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file.toString();
        } else if (url.length() <= 1) {
            return ROOT_PATH + FIRST_PAGE_NAME;
        }
        return ROOT_PATH + url;
    }

    /**
     * 組み立てたパスから冗長な要素を消し、正規化する
     *
     * @param url 組み立てたパス
     * @return 正規化されたパスを返す
     */
    private String getFirstPagePath(String url) {
        Path path = Paths.get(url).normalize();
        return path.toString();
    }
}
