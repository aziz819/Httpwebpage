package jp.co.topgate.jan.web;

import jp.co.topgate.jan.web.Error.BadRequest;
import jp.co.topgate.jan.web.Error.MethodNotAllowed;
import jp.co.topgate.jan.web.Error.VersionNotSupported;

import java.io.FileNotFoundException;
import java.io.IOException;
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

    public ConnectionHandler(InputStream is, OutputStream os) throws IOException {

        if (is == null || os == null) {

            throw new NullPointerException("入力ストリームと出力ストリームどっちかnullになっています。");

        }
        request(is, os);
    }


    public void request(InputStream is, OutputStream os){

        try {

            httpRequest = new HttpRequest(is);

            methodCheck(httpRequest.getMethod());                    //HTTPメソッド

            fileResources = filrCheck(httpRequest.getURL());        //url

            versionCheck(httpRequest.getVersion());                 //HTTPバージョン

            statusCode = StatusLine.OK;

        } catch (MethodNotAllowed e) {                              // 実装されていないHTTPメソッドの場合にキャッチ
            e.printStackTrace();
            statusCode = StatusLine.METHOD_NOT_ALLOWED;

        } catch (VersionNotSupported e) {                           // サポートされていないHTTPバージョンの場合にキャッチ
            e.printStackTrace();
            statusCode = StatusLine.HTTP_VERSION_NOT_SUPPORTED;

        } catch (FileNotFoundException e) {                         // 指定したファイルが見つからなかった場合にキャッチ
            e.printStackTrace();
            statusCode = StatusLine.NOT_FOUND;

        } catch (IOException | BadRequest e) {                      // nullのリクエストライン、正しくないリクエストライン、正しくないGET&POSTパラメーター、BuffereadReader読み込み不具合にキャッチ
            e.printStackTrace();
            statusCode = StatusLine.BAD_REQUEST;

        } finally {
            if (fileResources == null) {
                fileResources = new FileResources("");
            }
        }

        try {

            httpResponse = new HttpResponse(os, fileResources);

            httpResponse.makeStastusLine(statusCode);

        } catch (NullPointerException e) {
            System.out.println("エラー:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
     * HTTPバージョン確認
     */

    private void versionCheck(String version) {
        if (!("HTTP/1.1".equals(version))) {
            throw new VersionNotSupported("エラー:サポートされていないバージョン");
        }
    }



    /*
     * HTTPメソッド確認
     */

    private void methodCheck(String method) {
        if (!"GET".equals(method) && !"POST".equals(method)) {
            throw new MethodNotAllowed("エラー:GETでもPOSTでもないメソッド");
        }
    }



    /*
     * ファイル有無確認
     */

    private FileResources filrCheck(String url) throws FileNotFoundException {

        fileResources = new FileResources(url);

        if (!(fileResources.exists() && fileResources.isFile())) {
            throw new FileNotFoundException("エラー:ファイルは見つかりません");
        }

        return fileResources;
    }
}