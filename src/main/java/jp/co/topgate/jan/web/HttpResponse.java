package jp.co.topgate.jan.web;

import java.io.*;

/**
 * Created by aizijiang.aerken on 2017/04/13.
 */

public class HttpResponse {
    FileInputStream filein = null ;

    public HttpResponse(int statusCode, OutputStream os, FileResources file,StatusLine statusline) throws IOException {

        if(os == null || file == null){
            throw new RuntimeException("osかfileがnullになっています。");
        }

        try {
            StringBuilder responseMessage = statusline.statusfirstline(statusCode);
            responseMessage.append(file.getContenType());

            os.write(responseMessage.toString().getBytes());

            BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = bfr.readLine()) != null) {
                responseMessage.append(line + "\n");
            }
            System.out.println("\n" + responseMessage);

            filein = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            while (true) {
                int r = filein.read(bytes);
                if (r == -1) {
                    break;
                }

                os.write(bytes, 0, r);      //通信ソケットに送信するバイトストリームを取得(ブラウザーに表示)
            }
            os.flush();
        }finally {
            if(filein != null)filein.close();
        }

    }

}