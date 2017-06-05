package jp.co.topgate.jan.web;

import java.io.*;
import java.util.Objects;

/**
 * ステータス行とContent-Typeを組み立てストリームに書き込む
 * Created by aizijiang.aerken on 2017/04/13.
 *
 * @author jan
 */

public class HttpResponse {

    private InputStream is = null;

    private FileResource fileResource = null;

    private OutputStream os = null;

    private StringBuilder responseMessage = new StringBuilder();

    private StatusLine statusLine = null;

    private ErrorMessageBody errorMessageBody = null;

    private int statusCode;

    private File file = null;

    /**
     *
     * @param os            出力ストリーム
     * @param file          ファイルパス
     * @param fileResource  FileResourceクラスのオブジェクト
     */

    public HttpResponse(OutputStream os, File file, FileResource fileResource) {
        statusLine = new StatusLine();
        errorMessageBody = new ErrorMessageBody();
        this.file = file;
        this.fileResource = fileResource;
        this.os = Objects.requireNonNull(os, "出力ストリームがnullになっています");
    }


    /**
     * レスポンスをストリームに書き込む
     * ステータス行とContent-Typeを別クラスで組み立てさせてもらう
     *
     * @param statusCode  ステータスコード
     * @param fileExist   ファイルが存在(true)場合にファイルを書き込む、存在しない(false)場合にエラーメッセージボディを書き込む
     */

    public void writeResponse(int statusCode, boolean fileExist) {

        String contentType ;

        String codeDescription = statusLine.getStatusLine(statusCode) + "\n";

        try {


            os.write(codeDescription.toString().getBytes());

            if (fileExist) {
                contentType = fileResource.getContentType() + "\n\n";
                responseMessage.append("Content-Type: ").append(contentType);
                os.write(responseMessage.toString().getBytes());

                is = new FileInputStream(file);
                int i;
                while ((i = is.read()) != -1) {
                    os.write(i);
                }
            } else {
                contentType = fileResource.getErrorBodyContentType() + "\n\n";
                responseMessage.append("Content-Type: ").append(contentType);
                responseMessage.append(errorMessageBody.getErrorMessageBody(statusCode));
                //System.out.println(responseMessage);
                os.write(responseMessage.toString().getBytes());
            }
        } catch (IOException e) {
            System.out.println("ファイルの読み書きに不具合が発生しました");
            e.printStackTrace();
        } finally {
            try {
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (os != null)
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * 下記はテスト時に使用
     *
     * @param statusCode        statusCodeに値をセットする
     */

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

}