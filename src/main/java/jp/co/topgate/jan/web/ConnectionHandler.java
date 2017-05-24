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

        HttpRequest httpRequest = null;

        statusCode = StatusLine.OK;

        try {

            httpRequest = new HttpRequest(is);

        } catch (NullPointerException e) {                      // 出力ストリームがnullの時にキャッチ

            System.out.println("エラー:" + e.getMessage());

            e.printStackTrace();

            statusCode = StatusLine.BAD_REQUEST;

        }



        /*
         * リクエスト行　&　メッセンジャ・ヘッダー読み込み　& パラメーターの扱い
         */

        try {

            httpRequest.parseRequest();

        } catch (RequestParseException e) {                   // 不正なリクエストライン＆メッセージ・ヘッダー＆パラメーター　の時にキャッチ

            System.out.println("エラー:" + e.getMessage());

            e.printStackTrace();

            statusCode = StatusLine.BAD_REQUEST;
        }


        String method = httpRequest.getMethod();

        String url = httpRequest.getURL();

        String version = httpRequest.getVersion();


         /*
          * ファイルパスを返す
          */


        getFilePas(url);


        /*
         * ファイルチェック
         */

        try {

            fileCheck(url);

        } catch (FileNotFoundException e) {                     // 指定したファイルが見つからなかった場合にキャッチ

            System.out.println("エラー:" + e.getMessage());

            e.printStackTrace();

            statusCode = StatusLine.NOT_FOUND;

        }


        if (!"GET".equals(method) && !"POST".equals(method)) {

            statusCode = StatusLine.METHOD_NOT_ALLOWED;

        } else if (!("HTTP/1.1".equals(version))) {

            statusCode = StatusLine.HTTP_VERSION_NOT_SUPPORTED;

            }

    }


    public void writeResponse() {

        HttpResponse httpResponse = null;

        try {

            httpResponse = new HttpResponse(os, fileResources);

        } catch (NullPointerException e) {

            System.out.println("エラー:" + e.getMessage());

            e.printStackTrace();
        }



        /*
         * ステータスコードの確認　＆　コード説明のセット
         */

        try {

            httpResponse.createStatusLine(statusCode);

        } catch (RuntimeException e) {

            System.out.println("レスポンスラインの作成に不具合が発生しました:" + e.getMessage());

            e.printStackTrace();
        }



        /*
         * ファイル拡張子によってContentTypeをセット
         */

        try {

            httpResponse.creatContentType();

        } catch (RuntimeException e) {

            System.out.println("エラー:Content-Typeの作成に不具合が発生しました");

            e.printStackTrace();
        }


        /*
         * レスポンスを書き込む
         */

        httpResponse.toWriteResponse();


    }



    /*
     *  urlを継承元のFileクラスのコンストラクタに渡してファイルパスを設定
     */

    public void getFilePas(String url) {

        fileResources = new FileResources(url);

    }



    /*
     * 継承元のFileクラスのメソッドを継承してファイル有無確認
     */

    public void fileCheck(String url) throws FileNotFoundException {

        if (!(fileResources.exists() && fileResources.isFile())) {

            throw new FileNotFoundException("エラー:ファイルは存在しないかファイルではありません");

        }
    }


}