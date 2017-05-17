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

    private int StatusCode;

    public HttpResponse(OutputStream os, FileResources fileResources) {

        if (os == null) {

            throw new NullPointerException("エラー:出力ストリートがnullになっています");

        }

        statusLine = new StatusLine();

        this.os = os;

        this.fileResources = fileResources;

    }


    /*
     * ステータスコードの確認＆コード説明のセット
     */

    public void makeStastusLine(int statuscode) {

        StatusCode = statusLine.statusCodeCheck(statuscode);

        responseMessage.append("HTTP/1.1").append(" ").append(StatusCode).append(" ").append(statusLine.getStatusLine(StatusCode)).append("\n");

        contentType(responseMessage);

    }


    /*
     *Content-Typのセット＆OutputStreamに書き込む
     */

    private void contentType(StringBuilder responseMessage) {

        responseMessage.append(fileResources.getContentType()).append("\n");

        try {

            if (StatusCode != statusLine.OK) {

                responseMessage.append(statusLine.getErrorMessageBody(StatusCode));

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
            System.out.println("エラー:ファイルの読み込みに不具合が発生しました" + e.getMessage());
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