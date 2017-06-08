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

    private OutputStream os = null;

    private StatusLine statusLine = null;

    private ErrorMessageBody errorMessageBody = null;

    /**
     *
     * @param os  出力ストリーム
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
     */

    public void writeResponse(int statusCode, FileResource fileResource) {

        String contentType ;

        String codeDescription = statusLine.getStatusLine(statusCode) + "\n";

        try {

            os.write(codeDescription.getBytes());

            if (fileResource.checkFile()) {
                contentType = "Content-Type: " + fileResource.getContentType() + "\n\n";
                os.write(contentType.getBytes());

                is = new FileInputStream(fileResource.getPath());
                int i;
                while ((i = is.read()) != -1) {
                    os.write(i);
                }
            } else {
                contentType = "Content-Type: " + fileResource.getErrorBodyContentType() + "\n\n";
                os.write(contentType.getBytes());
                os.write(errorMessageBody.getErrorMessageBody(statusCode).getBytes());
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
}