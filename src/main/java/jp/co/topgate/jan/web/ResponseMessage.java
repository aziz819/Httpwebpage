package jp.co.topgate.jan.web;

import jp.co.topgate.jan.web.program.board.UrlHandler;

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

public class ResponseMessage extends UrlHandler {

    private InputStream is = null;

    private OutputStream os = null;

    private static int statucCode;

    private static final String CONTENT_TYPE = "Content-Type: ";

    /**
     *
     * @param os  出力ストリーム
     * @param statusCode　ステータスコード
     */

    public ResponseMessage(OutputStream os, int statusCode) {
        this.os = Objects.requireNonNull(os, "出力ストリームがnullになっています");
        statucCode = statusCode;
    }


    /**
     * レスポンスをストリームに書き込む
     * ステータス行とContent-Typeを別クラスで組み立てさせてもらう
     *
     **/

    @Override
    public void writeResponse() {

        String contentType ;

        String codeDescription = StatusLine.getStatusLine(statucCode) + "\n";

        try {

            os.write(codeDescription.getBytes());

            if (FileResource.checkFile()) {
                contentType = CONTENT_TYPE + FileResource.getContentType() + "\n\n";
                os.write(contentType.getBytes());

                is = new FileInputStream(FileResource.getPath());
                int i;
                while ((i = is.read()) != -1) {
                    os.write(i);
                }
            } else {
                contentType = CONTENT_TYPE + FileResource.fixedContentType() + "\n\n";
                os.write(contentType.getBytes());
                os.write(ErrorMessageBody.getErrorMessageBody(statucCode).getBytes());
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
     * ステータスコードによってレスポンス行を組み立てる
     *
     * @return レスポンス行を返す
     */
    public static String setStatusLine() {
        return StatusLine.getStatusLine(statucCode) + "\n";
    }

    /**
     * Content-Typeを組み立てる
     *
     * @return Content-Typeを返す
     */
    public static String setNormalContentType() {
        return CONTENT_TYPE + FileResource.getContentType() + "\n\n";
    }

    /**
     * エラーメッセージボディのContent-Typeを組み立てる
     *
     * @return Content-Typeを返す
     */
    public static String setErrorMessageBodyContentType() {
        return CONTENT_TYPE + FileResource.fixedContentType() + "\n\n";

    }
}