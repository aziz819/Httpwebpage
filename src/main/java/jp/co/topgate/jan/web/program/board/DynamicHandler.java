package jp.co.topgate.jan.web.program.board;

import jp.co.topgate.jan.web.ErrorMessageBody;
import jp.co.topgate.jan.web.FileResource;
import jp.co.topgate.jan.web.RequestMessage;
import jp.co.topgate.jan.web.StatusLine;

import java.io.*;

/**
 * 動的なファイルの処理を行う
 * Created by aizijiang.aerken on 2017/06/18.
 */
public class DynamicHandler extends UrlHandler {

    private RequestMessage requestMessage = null;
    private OutputStream os = null;
    private FileResource fileResource = null;
    private int statusCode;
    private ErrorMessageBody errorMessageBody = null;

    /**
     * @param requestMessage リクエスト分析結果を持つオブジェクト
     * @param os             出力ストリーム
     * @param statusCode     ステータスコード
     * @param fileResource   ファイルチェック、パス作成などを行うFileResourceのオブジェクト
     */

    DynamicHandler(RequestMessage requestMessage, OutputStream os, int statusCode, FileResource fileResource) {

        this.requestMessage = requestMessage;
        this.os = os;
        this.statusCode = statusCode;
        this.fileResource = fileResource;
        this.errorMessageBody = new ErrorMessageBody();

    }

    /**
     * httpメソッドがGETかPOSTかで処理が違う
     */

    @Override
    public void writeResponse() {
        String method = requestMessage.getMethod();
        if (method.equals("GET")) {
            doGet();
        } else if (method.equals("POST")) {
            doPost();
        }
    }

    /**
     * 全てのデータの表示と
     * 名前で検索した際の処理をする
     */

    private void doGet() {


        String contentType;

        try {
            String statusLine = StatusLine.getStatusLine(statusCode) + "\n";
            contentType = "Content-Type: " + fileResource.getContentType() + "\n\n";
            os.write(statusLine.getBytes());

            String searchHint = requestMessage.getParameters().get("name");

            /*
             * ファイルが見つからなかった際にステータスコードに合ったエラーメッセージボディ書き込む
             */
            if (fileResource.checkFile()) {
                os.write(contentType.getBytes());
                if (!fileResource.getPath().endsWith("html")) {  // ローカルファイルの読み込み
                    InputStream is = null;
                    try {
                        is = new FileInputStream(fileResource.getPath());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    int i;
                    try {
                        assert is != null;
                        while ((i = is.read()) != -1) {
                            os.write(i);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (requestMessage.getParameters().get("name") != null) {
                    String htmlAllDataMessageBody = HtmlEditor.showScannedData(CsvDataManagement.searchFromName(searchHint)); // 既存の全てのデータを読んできてhtml立てる
                    os.write(htmlAllDataMessageBody.getBytes());

                } else {
                    String htmlScannedDataMessageBody = HtmlEditor.showAllData(CsvDataManagement.getAllmessage()); // 検索されたデータを読んできてhtml立てる
                    os.write(htmlScannedDataMessageBody.getBytes());
                }
            } else {
                contentType = "Content-Type: " + fileResource.fixedContentType() + "\n\n";
                os.write(contentType.getBytes());
                os.write(errorMessageBody.getErrorMessageBody(statusCode).getBytes());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 新投稿の書き込みと
     * 削除対象のデータの処理を行う
     */

    private void doPost() {

        String deletionHint = requestMessage.getParameters().get("delete");
        if (deletionHint != null) {
            delete();
            return;
        }

        String name = requestMessage.getParameters().get("name");
        String title = requestMessage.getParameters().get("title");
        String pass = requestMessage.getParameters().get("pass");
        String content = requestMessage.getParameters().get("content");

        DataControl.receiveNewData(name, title, pass, content);
        redirect(); // 新投稿を書き込んでからリダイレクト
    }


    private void delete() {
        String countNum = requestMessage.getParameters().get("countNum");
        String pass = requestMessage.getParameters().get("pass");
        CsvDataManagement.delete(countNum, pass);
        redirect(); // 投稿を削除してからリダイレクト
    }

    /**
     * 新投稿の処理と投稿削除処理の後リダイレクト
     */

    private void redirect() {
        try {
            os.write("HTTP/1.1 302 FOUNT\r\n".getBytes());
            os.write("Location: /program/board/\r\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
