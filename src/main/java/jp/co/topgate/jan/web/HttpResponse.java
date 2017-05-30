package jp.co.topgate.jan.web;

import java.io.*;

/**
 * ステータス行とContent-Typeを組み立てストリームに書き込む
 * Created by aizijiang.aerken on 2017/04/13.
 *
 * @author jan
 */

public class HttpResponse {

    private FileInputStream fileInputStream = null;

    private FileResources fileResources = null;

    private OutputStream os = null;

    private StringBuilder responseMessage = new StringBuilder();

    private StatusLine statusLine = null;

    private ErrorMessageBody errorMessageBody = null;

    private int statusCode;

    /**
     * @param os            出力ストリーム
     * @param fileResources FileResourcesのオブジエクト
     */

    public HttpResponse(OutputStream os, FileResources fileResources) {
        if(os == null) {
            throw new NullPointerException("出力ストリームがnullになっています");
        }
        statusLine = new StatusLine();
        errorMessageBody = new ErrorMessageBody();
        this.fileResources = fileResources;
        this.os = os;
    }


    /**
     * レスポンスをストリームに書き込む
     *
     * @param statusCode  ステータスコード
     */

    public void writeResponse(int statusCode) {
        statusCode = statusLine.CheckStatusCode(statusCode);
        String codeDescription = statusLine.getCodeDescription(statusCode) + "\n";
        responseMessage.append("HTTP/1.1 ").append(statusCode).append(" ").append(codeDescription);

        try {
            if (statusCode != statusLine.OK) {
                responseMessage.append("Content-Type: text/html; charset=utf-8").append("\n\n");
                responseMessage.append(errorMessageBody.getErrorMessageBody(statusCode));
                System.out.println(responseMessage);
                os.write(responseMessage.toString().getBytes());
            } else {
                responseMessage.append("Content-Type: ").append(fileResources.getContentType(statusCode)).append("\n\n");
                os.write(responseMessage.toString().getBytes());

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileResources)));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    responseMessage.append(line + "\n");
                }
                System.out.println(responseMessage);

                fileInputStream = new FileInputStream(fileResources);
                byte[] bytes = new byte[1024];
                while (true) {
                    int r = fileInputStream.read(bytes);
                    if (r == -1) {
                        break;
                    }
                    os.write(bytes, 0, r);              //OutputStreamに書き込む
                }
            }

        } catch (IOException e) {
            System.out.println("ファイルの読み書きに不具合が発生しました" + e.toString());
            e.printStackTrace();
        } finally {
            try {
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (fileInputStream != null)
                try {
                    fileInputStream.close();
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
     * @param statusCode        ステータスコードの値を変更する
     */

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


    /**
     * 下記テスト時に使用
     *
     * @return ステータスコードを返す
     */

    public int getStatusCode() {
        return statusCode;
    }

}