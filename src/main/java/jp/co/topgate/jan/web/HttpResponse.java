package jp.co.topgate.jan.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    private StatusLine statusLine = null;

    private ErrorMessageBody errorMessageBody = null;

    private int statusCode;

    /**
     *
     * @param os            出力ストリーム
     */

    public HttpResponse(OutputStream os) {
        statusLine = new StatusLine();
        errorMessageBody = new ErrorMessageBody();
        this.os = Objects.requireNonNull(os, "出力ストリームがnullになっています");
    }


    /**
     * レスポンスをストリームに書き込む
     * ステータス行とContent-Typeを別クラスで組み立てさせてもらう
     *
     * @param statusCode  ステータスコード
     * @param fileExist   ファイルが存在(true)場合にファイルを書き込む、存在しない(false)場合にエラーメッセージボディを書き込む
     */

    public void writeResponse(int statusCode, String filePath, boolean fileExist, FileResource fileResource) {

        Objects.requireNonNull(filePath);

        String contentType ;

        String codeDescription = statusLine.getStatusLine(statusCode) + "\n";

        try {


            os.write(codeDescription.toString().getBytes());

            if (fileExist) {
                contentType = "Content-Type: " + fileResource.getContentType() + "\n\n";
                os.write(contentType.toString().getBytes());

                is = new FileInputStream(filePath);
                int i;
                while ((i = is.read()) != -1) {
                    os.write(i);
                }
            } else {
                contentType = "Content-Type: " + fileResource.getErrorBodyContentType() + "\n\n";
                os.write(contentType.toString().getBytes());
                os.write(errorMessageBody.getErrorMessageBody(statusCode).toString().getBytes());
            }
        } catch (IOException e) {
            System.out.println("通信経路が切断されたかファイル書き込みの不具合");
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