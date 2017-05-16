package jp.co.topgate.jan.web;

import java.io.*;

/* 分析した最終結果をブラウザに表示する
 * Created by aizijiang.aerken on 2017/04/13.
 */

public class HttpResponse {
    FileInputStream fileIn = null ;

    public HttpResponse(int statusCode, OutputStream os, FileResources fileresources, StatusLine statusline) throws IOException {

        if (os == null || fileresources == null) {
            throw new NullPointerException("osかfileresourcesがnullになっています。");
        }

        try {

            /*
             * 現在のステータスコードでコードによってステータス行のコードとコード説明をセットする
             */

            StringBuilder responseMessage = statusline.getStatusCode(statusCode);


            /*
             * ステータスコードによってエラーメッセージのContentTypeかセットしてあるのを取得
             */

            responseMessage.append(fileresources.getContenType(statusCode)).append("\n");

            if (statusCode != statusline.OK) {

                /*
                 * ステータスコードによって表示するべきメッセージボディを選ぶ
                 */

                responseMessage.append(statusline.IncorrectStatusCode(statusCode));

                System.out.println(responseMessage);

                os.write(responseMessage.toString().getBytes());

            } else {

                os.write(responseMessage.toString().getBytes());

                BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(fileresources)));

                String line;

                while ((line = bfr.readLine()) != null) {

                    responseMessage.append(line + "\n");

                }
                System.out.println(responseMessage);

                fileIn = new FileInputStream(fileresources);

                byte[] bytes = new byte[1024];

                while (true) {

                    int r = fileIn.read(bytes);

                    if (r == -1) {

                        break;
                    }

                    os.write(bytes, 0, r);      //通信ソケットに送信するバイトストリームを取得(ブラウザーに表示)
                }
                os.flush();
            }
        }finally {

            if(fileIn != null) fileIn.close();

            if (os != null) os.close();

        }

    }

}