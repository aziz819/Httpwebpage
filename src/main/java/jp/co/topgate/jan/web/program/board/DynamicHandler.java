package jp.co.topgate.jan.web.program.board;

import jp.co.topgate.jan.web.ErrorMessageBody;
import jp.co.topgate.jan.web.FileResource;
import jp.co.topgate.jan.web.RequestMessage;
import jp.co.topgate.jan.web.ResponseMessage;

import java.io.*;

/**
 * 動的なファイルの処理を行う
 * Created by aizijiang.aerken on 2017/06/18.
 */
public class DynamicHandler extends UrlHandler {

    private RequestMessage requestMessage = null;
    private OutputStream os = null;
    private int statusCode;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String USER_NAME = "name";
    private static final String INSTRUCTION = "instruction";
    private static final String HTML = "html";
    private static final String POST_TITLE = "title";
    private static final String PASSWORD = "pass";
    private static final String POST_CONTENT = "content";
    private static final String COUNT_NUM="countNum";
    private static final String REDIRECT_RESPONSE_LINE = "HTTP/1.1 302 Found\r\n";
    private static final String LOCATION = "Location: /program/board/\r\n";



    /**
     * @param requestMessage リクエスト分析結果を持つオブジェクト
     * @param os             出力ストリーム
     * @param statusCode     ステータスコード
     */

    DynamicHandler(RequestMessage requestMessage, OutputStream os, int statusCode) {

        this.requestMessage = requestMessage;
        this.os = os;
        this.statusCode = statusCode;
    }

    /**
     * httpメソッドがGETかPOSTかで処理が違う
     */

    @Override
    public void writeResponse() {
        String method = requestMessage.getMethod();
        if (method.equals(GET)) {
            doGet();
        } else if (method.equals(POST)) {
            doPost();
        }
    }

    /**
     * 全てのデータの表示と
     * 名前で検索した際の処理をする
     */

    private void doGet() {

        try {
            os.write(ResponseMessage.setStatusLine().getBytes());

            /*
             * ファイルが見つからなかった際にステータスコードに合ったエラーメッセージボディ書き込む
             */
            if (FileResource.checkFile()) {
                os.write(ResponseMessage.setNormalContentType().getBytes());
                if (!FileResource.getPath().endsWith(HTML)) {  // ローカルファイルの読み込み
                    InputStream is = null;
                    try {
                        is = new FileInputStream(FileResource.getPath());
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
                } else if (requestMessage.getParameters().get(INSTRUCTION) != null) {
                    os.write(HtmlEditor.showScannedData(CsvDataManagement.searchFromName(requestMessage.getParameters().get(USER_NAME))).getBytes()); // 既存の全てのデータを読んできてhtml立てる

                } else {
                    os.write(HtmlEditor.showAllData(CsvDataManagement.getAllmessage()).getBytes()); // 検索されたデータを読んできてhtml立てる
                }
            } else {
                os.write(ResponseMessage.setErrorMessageBodyContentType().getBytes()); // エラーメッセージContent-Type
                os.write(ErrorMessageBody.getErrorMessageBody(statusCode).getBytes()); // エラーメッセージボディ
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

        String deletionHint = requestMessage.getParameters().get(INSTRUCTION);
        if (deletionHint != null) {
            delete();
            return;
        }

        String name = requestMessage.getParameters().get(USER_NAME);
        String title = requestMessage.getParameters().get(POST_TITLE);
        String pass = requestMessage.getParameters().get(PASSWORD);
        String content = requestMessage.getParameters().get(POST_CONTENT);

        DataControl.receiveNewData(name, title, pass, content);
        redirect(); // 新投稿を書き込んでからリダイレクト
    }


    private void delete() {
        String countNum = requestMessage.getParameters().get(COUNT_NUM);
        String pass = requestMessage.getParameters().get(PASSWORD);
        CsvDataManagement.delete(countNum, pass);
        redirect(); // 投稿を削除してからリダイレクト
    }

    /**
     * 新投稿の処理と投稿削除処理の後リダイレクト
     */

    private void redirect() {
        try {
            os.write(REDIRECT_RESPONSE_LINE.getBytes());
            os.write(LOCATION.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
