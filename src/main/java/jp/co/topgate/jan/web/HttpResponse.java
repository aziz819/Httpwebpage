package jp.co.topgate.jan.web;

import java.io.*;

/**
 * Created by aizijiang.aerken on 2017/04/13.
 */

public class HttpResponse {
    FileInputStream fileIn = null ;

    public HttpResponse(int statusCode, OutputStream os, FileResources fileresources, StatusLine statusline) throws IOException {

        if (os == null || fileresources == null) {
            throw new RuntimeException("osかfileresourcesがnullになっています。");
        }

        try {
            StringBuilder responseMessage = statusline.statusfirstline(statusCode);   // 現在のステータスコードでコード説明を選択する

            responseMessage.append(fileresources.getContenType(statusCode)).append("\n");      // ファイルの拡張によってコンテントタイプをセットする

            if (statusCode != statusline.OK) {
                responseMessage.append(statusline.irregularityStatusCode(statusCode));
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