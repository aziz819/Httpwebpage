package jp.co.topgate.jan.web;

import java.io.*;

/* クエストライン行　＆　Content-Typeの作成　ストリームに書き込む
 * Created by aizijiang.aerken on 2017/04/13.
 */

public class HttpResponse {

    private FileInputStream fileInputStream = null;

    private FileResources fileResources = null;

    private OutputStream os = null;

    private StringBuilder responseMessage = new StringBuilder();

    private StatusLine statusLine = null;

    private ErrorMessageBody errorMessageBody = null;

    private int statusCode;

    public HttpResponse(OutputStream os, FileResources fileResources) {

        if (os == null) {

            throw new NullPointerException("出力ストリームがnullになっています");

        }

        statusLine = new StatusLine();

        errorMessageBody = new ErrorMessageBody();

        this.fileResources = fileResources;

        this.os = os;

    }

    /*
     * ステータス行　＆　ContentType　＆　ストリームに書き込むのを各メソッドで処理する
     */

    public void writeResponse(int statusCode) {

        /*
         * ステータスコードの確認　＆　コード説明のセット
         */

        try {

            createStatusLine(statusCode);

        } catch (RuntimeException e) {

            System.out.println("エラ:" + e.getMessage());

            e.printStackTrace();
        }

        /*
         * ファイル拡張子によってContentTypeをセット
         */

        try {

            createContentType();

        } catch (RuntimeException e) {

            System.out.println("エラー:" + e.getMessage());

            e.printStackTrace();
        }


        /*
         * ストリームに書き込む
         */

        writeResponse();
    }


    /*
     * ステータスコードの確認　＆　コードとコード説明のセット
     */

    private void createStatusLine(int statuscode) {

        statusCode = statusLine.statusCodeCheck(statuscode);

        String codeDescription = statusLine.getCodeDescription(statusCode);

        if (codeDescription == null || codeDescription == "") {
            throw new RuntimeException("ステータスコード説明がnullで成り立っていない");
        }
        responseMessage.append("HTTP/1.1").append(" ").append(statusCode).append(" ").append(codeDescription).append("\n");
    }


    /*
     * ファイル拡張子の確認　＆　Content-Typのセット
     */

    private void createContentType() {

        String contenType = fileResources.getContentType(statusCode);

        if (contenType == null || contenType == "") {
            throw new RuntimeException("Contetn-Typeがnullで成り立っていない");
        }
        responseMessage.append(contenType).append("\n");
    }


    /*
     * レスポンスの書き込み
     */

    private void writeResponse() {

        try {

            if (statusCode != statusLine.OK) {

                responseMessage.append(errorMessageBody.getErrorMessageBody(statusCode));

                System.out.println(responseMessage);

                os.write(responseMessage.toString().getBytes());

            } else {

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

    /*
     *  下記テスト時に使用
     */

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setResponseMessage(StringBuilder responseMessage) {

        this.responseMessage = responseMessage;
    }
}