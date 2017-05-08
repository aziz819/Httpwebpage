package jp.co.topgate.jan.web;

import org.junit.Test;

import java.io.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by aizijiang.aerken on 2017/05/07.
 */
public class ConnectionHandlerTest {

    @Test
    public void GETメソッドステータスコード200のテスト() throws IOException {
        File file = new File("./src/test/Testresources/ResponseMessage.txt");
        file.delete();

        InputStream in = null;
        OutputStream ot = null;

        try {
            in = new FileInputStream(new File("./src/test/Testresources/getTest.txt"));
            ot = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

            new ConnectionHandler(in,ot);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(in != null)in.close();
            if (ot != null)ot.close();
        }

        try {
            in = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            assertThat(bf.readLine(), is("HTTP/1.1 200 OK"));
            assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
        }finally {
            if(in != null)in.close();
        }
    }

    @Test
    public void POSTメソッドのステータスコード200テスト() throws IOException {
        File file = new File("./src/test/Testresources/ResponseMessage.txt");
        file.delete();

        InputStream in = null;
        OutputStream ot = null;

        try {
            in = new FileInputStream(new File("./src/test/Testresources/postTest.txt"));
            ot = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

            new ConnectionHandler(in,ot);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(in != null)in.close();
            if (ot != null)ot.close();
        }

        try {
            in = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            assertThat(bf.readLine(), is("HTTP/1.1 200 OK"));
            assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
        }finally {
            if(in != null)in.close();
        }
    }

    @Test
    public void Httpバージョンテスト() throws IOException {
        File file = new File("./src/test/Testresources/ResponseMessage.txt");
        file.delete();

        InputStream in = null;
        OutputStream ot = null;

        try {
            in = new FileInputStream(new File("./src/test/Testresources/versionTest.txt"));
            ot = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

            new ConnectionHandler(in,ot);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(in != null)in.close();
            if (ot != null)ot.close();
        }

        try {
            in = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            assertThat(bf.readLine(), is("HTTP/1.1 505 Version not supported"));
            assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
        }finally {
            if(in != null)in.close();
        }
    }


    @Test
    public void 指定ファイルを見つからないテスト() throws IOException {
        File file = new File("./src/test/Testresources/ResponseMessage.txt");
        file.delete();

        InputStream in = null;
        OutputStream ot = null;

        try {
            in = new FileInputStream(new File("./src/test/Testresources/notFileTest.txt"));
            ot = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

            new ConnectionHandler(in,ot);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(in != null)in.close();
            if (ot != null)ot.close();
        }

        try {
            in = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            assertThat(bf.readLine(), is("HTTP/1.1 404 Not Found"));
            assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
        }finally {
            if(in != null)in.close();
        }
    }

    @Test
    public void 指定されていないHTTPメソッドテスト() throws IOException {
        File file = new File("./src/test/Testresources/ResponseMessage.txt");
        file.delete();

        InputStream in = null;
        OutputStream ot = null;

        try {
            in = new FileInputStream(new File("./src/test/Testresources/notMethodTest.txt"));
            ot = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

            new ConnectionHandler(in,ot);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(in != null)in.close();
            if (ot != null)ot.close();
        }

        try {
            in = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            assertThat(bf.readLine(), is("HTTP/1.1 405 Method Not Allowed"));
            assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
        }finally {
            if(in != null)in.close();
        }
    }

    @Test
    public void 空のリクエストラインテスト() throws IOException {
        File file = new File("./src/test/Testresources/ResponseMessage.txt");
        file.delete();

        InputStream in = null;
        OutputStream ot = null;

        try {
            in = new FileInputStream(new File("./src/test/Testresources/emptyingRequestTest.txt"));
            ot = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

            new ConnectionHandler(in,ot);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(in != null)in.close();
            if (ot != null)ot.close();
        }

        try {
            in = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            assertThat(bf.readLine(), is("HTTP/1.1 400 Bad Request"));
            assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
        }finally {
            if(in != null)in.close();
        }
    }

    @Test
    public void 正しくGETパラメーター取得できるかのテスト() throws IOException {
        File file = new File("./src/test/Testresources/ResponseMessage.txt");
        file.delete();

        InputStream in = null;
        OutputStream ot = null;

        try {
            in = new FileInputStream(new File("./src/test/Testresources/getParameterTest.txt"));
            ot = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

            new ConnectionHandler(in,ot);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(in != null)in.close();
            if (ot != null)ot.close();
        }

        try {
            in = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            assertThat(bf.readLine(), is("HTTP/1.1 400 Bad Request"));
            assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
        }finally {
            if(in != null)in.close();
        }
    }

    @Test
    public void 正しくPOSTパラメーター取得できるかのテスト() throws IOException {
        File file = new File("./src/test/Testresources/ResponseMessage.txt");
        file.delete();

        InputStream in = null;
        OutputStream ot = null;

        try {
            in = new FileInputStream(new File("./src/test/Testresources/postParameterTest.txt"));
            ot = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

            new ConnectionHandler(in,ot);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(in != null)in.close();
            if (ot != null)ot.close();
        }

        try {
            in = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            assertThat(bf.readLine(), is("HTTP/1.1 400 Bad Request"));
            assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
        }finally {
            if(in != null)in.close();
        }
    }
}