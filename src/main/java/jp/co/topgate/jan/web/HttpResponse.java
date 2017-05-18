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

    private int statusCode;


    public HttpResponse(OutputStream os, FileResources fileResources) {

        if (os == null) {

            throw new NullPointerException("エラー:出力ストリートがnullになっています");

        }

        statusLine = new StatusLine();

        this.fileResources = fileResources;

        this.os = os;

    }


    /*
     * ステータスコードの確認　＆　コードとコード説明のセット
     */

    public void createStatusLine(int statuscode) {

        statusCode = statusLine.statusCodeCheck(statuscode);

        responseMessage.append("HTTP/1.1").append(" ").append(statusCode).append(" ").append(statusLine.getCodeDescription(statusCode)).append("\n");

    }


    /*
     * ファイル拡張子の確認　＆　Content-Typのセット
     */

    public void selectContentType() {

        responseMessage.append(fileResources.getContentType(statusCode)).append("\n");


    }


    /*
     * レスポンセの書き込み
     */

    public void createResponse() {

        try {

            if (statusCode != statusLine.OK) {

                responseMessage.append(statusLine.getErrorMessageBody(statusCode));

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
}