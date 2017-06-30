package jp.co.topgate.jan.web.program.board;

import jp.co.topgate.jan.web.*;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 動的なファイルの処理を行う
 * Created by aizijiang.aerken on 2017/06/18.
 */
public class DynamicHandler extends UrlHandler {

    private RequestMessage requestMessage = null;
    private OutputStream os = null;
    private FileResource fileResource = null;
    private int statusCode;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String INSTRUCTION = "instruction";
    private static final String USER_NAME = "name";
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
     * @param fileResource   ファイルチェック、パス作成などを行うFileResourceのオブジェクト
     */
    public DynamicHandler(RequestMessage requestMessage, OutputStream os, int statusCode, FileResource fileResource) {
        this.requestMessage = requestMessage;
        this.os = os;
        this.statusCode = statusCode;
        this.fileResource = fileResource;
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
        if (requestMessage.getParameters().get(INSTRUCTION) != null) {
            HtmlEditor.showScannedData(CsvDataManagement.searchFromName(requestMessage.getParameters().get(USER_NAME))); // 検索対象者のデータを読んできてhtml立てる

        } else {
            HtmlEditor.showAllData(CsvDataManagement.getAllmessage()); // 既存の全てのデータを読んできてhtml立てる
        }

        ResponseMessage responseMessage = new ResponseMessage(os, statusCode, fileResource);
        responseMessage.writeResponse();
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


    /**
     * 削除対象者のカウンター番号とパスワードで削除処理を行う
     */
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
