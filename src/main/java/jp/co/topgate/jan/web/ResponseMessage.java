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

    private ErrorMessageBody errorMessageBody = null;

    private RequestMessage requestMessage = null;

    private int statucCode ;

    private FileResource fileResource ;

    /**
     *
     * @param os  出力ストリーム
     */

    public ResponseMessage(RequestMessage requestMessage,OutputStream os,int statusCode,FileResource fileResource) {
        this.requestMessage = requestMessage ;
        errorMessageBody = new ErrorMessageBody();
        this.os = Objects.requireNonNull(os, "出力ストリームがnullになっています");
        this.statucCode = statusCode ;
        this.fileResource = fileResource;
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

            if (fileResource.checkFile()) {
                contentType = "Content-Type: " + fileResource.getContentType() + "\n\n";
                os.write(contentType.getBytes());

                is = new FileInputStream(fileResource.getPath());
                int i;
                while ((i = is.read()) != -1) {
                    os.write(i);
                }
            } else {
                contentType = "Content-Type: " + fileResource.fixedContentType() + "\n\n";
                os.write(contentType.getBytes());
                os.write(errorMessageBody.getErrorMessageBody(statucCode).getBytes());
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