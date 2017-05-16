package jp.co.topgate.jan.web;

import org.junit.Test;

import java.io.*;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by aizijiang.aerken on 2017/05/12.
 */
public class HttpResponseTest {

    public static class ステータスコードテスト {
        @Test
        public void 予期されていないスタータスコード確認() throws IOException {
            File file = new File("./src/test/Testresources/ResponseMessage.txt");
            file.delete();

            FileResources fileResources = new FileResources("/index.html");
            StatusLine statusline = new StatusLine();
            OutputStream os = null;
            InputStream is = null;

            try {
                os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

                new HttpResponse(506, os, fileResources, statusline);

            } catch (FileNotFoundException e) {
                System.out.println("エラー：指定したファイルが見つかりませんでした。");
                e.printStackTrace();
                fail();
            } catch (NullPointerException e){
                System.out.println("エラー：osかfileresourcesがnullになっています。");
                e.printStackTrace();
                fail();

            }finally {
                if (is != null) is.close();
                if (os != null) os.close();
            }

            try {
                is = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
                BufferedReader bf = new BufferedReader(new InputStreamReader(is));
                assertThat(bf.readLine(), is("HTTP/1.1 500 INTERNET_SERVER_ERROR"));
                assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
            } finally {
                if (is != null) is.close();
            }
        }
    }


    public static class レスポンスメッセージ生成テスト {
        @Test
        public void レスポンスメッセージ() throws IOException {
            File file = new File("./src/test/Testresources/ResponseMessage.txt");
            file.delete();

            StatusLine statusline = new StatusLine();
            OutputStream os = null;
            InputStream is = null;

            try {
                os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
                FileResources fileResources = new FileResources("/index.html");
                new HttpResponse(200, os, fileResources, statusline);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) is.close();
                if (os != null) os.close();
            }

            try {
                is = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
                BufferedReader bf = new BufferedReader(new InputStreamReader(is));
                assertThat(bf.readLine(), is("HTTP/1.1 200 OK"));
                assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
                assertThat(bf.readLine(), is(""));
                assertThat(bf.readLine(), is("<html>"));
                assertThat(bf.readLine(), is("<meta charset=\"utf-8\">"));
                assertThat(bf.readLine(), is("<head>"));
                assertThat(bf.readLine(), is("<title>my index</title>"));
            } finally {
                if (is != null) is.close();
            }
        }

    }
}
