package jp.co.topgate.jan.web;
import jp.co.topgate.jan.web.exception.RequestParseException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

/* HttpRequestクラスから返してきたメソッド、url,バージョンの確認してHttpResponseクラスのい渡す
 * Created by aizijiang.aerken on 2017/04/13.
 */


public class ConnectionHandler {

    private int statusCode ;

    private HttpRequest httpRequest = null;

    private HttpResponse httpResponse = null;

    private FileResources fileResources = null;

    private InputStream is = null;

    private OutputStream os = null;


    public ConnectionHandler(InputStream is, OutputStream os) {

        if (is == null || os == null) {

            throw new NullPointerException("入力ストリームと出力ストリームどっちかnullになっています。");

        }

        this.is = is;

        this.os = os;
    }


    public void readRequest() {

        try {

            httpRequest = new HttpRequest(is);


            /*
             * リクエスト行&メッセンジャ・ヘッダー読み込み
             */

            httpRequest.parseRequestLine();



            /*
             * GETパラメーター & POSTパラメーター、メッセージボディの読み込み
             */

            httpRequest.parseRequestParameter();


            String method = httpRequest.getMethod();

            String url = httpRequest.getURL();

            String version = httpRequest.getVersion();

            fileResources = fileCheck(url);

            if (!"GET".equals(method) && !"POST".equals(method)) {

                statusCode = StatusLine.BAD_REQUEST;

            } else if (!("HTTP/1.1".equals(version))) {

                statusCode = StatusLine.HTTP_VERSION_NOT_SUPPORTED;

            }


            statusCode = StatusLine.OK;

        } catch (FileNotFoundException e) {                         // 指定したファイルが見つからなかった場合にキャッチ

            System.out.println("エラー:" + e.getMessage());

            e.printStackTrace();

            statusCode = StatusLine.NOT_FOUND;

        } catch (RequestParseException e) {                      // nullのリクエストライン、正しくないリクエストライン、正しくないGET&POSTパラメーター、BuffereadReader読み込み不具合にキャッチ

            System.out.println("エラー:" + e.getMessage());

            e.printStackTrace();

            statusCode = StatusLine.BAD_REQUEST;

        } finally {

            if (fileResources == null) {

                fileResources = new FileResources("");

            }
        }


    }


    public void writeResponse() {

        try {

            httpResponse = new HttpResponse(os, fileResources);


            /*
             * ステータスコードの確認　＆　コード説明のセット
             */

            httpResponse.createStatusLine(statusCode);


            /*
             * ファイル拡張子によってContentTypeをセット
             */

            httpResponse.selectContentType();


            /*
             * レスポンセを書き込む
             */

            httpResponse.createResponse();


        } catch (NullPointerException e) {

            System.out.println("エラー:" + e.getMessage());

            e.printStackTrace();
        }

    }

    /*
     * ファイル有無確認
     */

    private FileResources fileCheck(String url) throws FileNotFoundException {

        fileResources = new FileResources(url);

        if (!(fileResources.exists() && fileResources.isFile())) {

            throw new FileNotFoundException("エラー:ファイルは見つかりません");

        }

        return fileResources;

    }


}