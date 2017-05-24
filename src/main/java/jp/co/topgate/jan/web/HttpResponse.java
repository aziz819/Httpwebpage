package jp.co.topgate.jan.web;

import java.io.*;

/* ConnectionHandlerクラスからの結果を持ってリクエストライン＆Content-Typeの作成
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

            throw new NullPointerException("エラー:出力ストリームがnullになっています");

        }

        statusLine = new StatusLine();

        errorMessageBody = new ErrorMessageBody();

        this.fileResources = fileResources;

        this.os = os;

    }


    /*
     * ステータスコードの確認　＆　コードとコード説明のセット
     */

    public void createStatusLine(int statuscode) {

        statusCode = statusLine.statusCodeCheck(statuscode);

        String codeDescription = " " + statusLine.getCodeDescription(statusCode) + "\n";

        if (codeDescription == null || codeDescription == "") {
            throw new RuntimeException("エラー:正しくないステータスコード説明");
        }

        responseMessage.append("HTTP/1.1").append(" ").append(statusCode).append(codeDescription);



    }


    /*
     * ファイル拡張子の確認　＆　Content-Typのセット
     */

    public void creatContentType() {

        responseMessage.append(fileResources.getContentType(statusCode)).append("\n");

        String ContenType = fileResources.getContentType(statusCode);

        if (ContenType == null || ContenType == "") {
            throw new RuntimeException("エラー:正しくないContetn-Type");
        }
    }


    /*
     * レスポンスの書き込み
     */

    public void toWriteResponse() {

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

            System.out.println("エラー:ファイルの読み書きに不具合が発生しました" + e.toString());

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