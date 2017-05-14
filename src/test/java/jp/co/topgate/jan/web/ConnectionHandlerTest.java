package jp.co.topgate.jan.web;

import org.junit.Test;

import java.io.*;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by aizijiang.aerken on 2017/05/07.
 */
public class ConnectionHandlerTest {

    public static class パラメーター付きGETとPOST場合の200OK確認テスト {


        @Test
        public void GETの場合() throws IOException {
            File file = new File("./src/test/Testresources/ResponseMessage.txt");
            file.delete();

            InputStream is = null;
            OutputStream os = null;


            try {
                is = new FileInputStream(new File("./src/test/Testresources/getTest.txt"));
                os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

                new ConnectionHandler(is, os);

            } catch (FileNotFoundException e) {
                System.out.println("エラー：指定したファイルが見つかりませんでした。");
                e.printStackTrace();
                fail();
            } finally {
                if (is != null) is.close();
                if (os != null) os.close();
            }

            try {
                is = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
                BufferedReader bf = new BufferedReader(new InputStreamReader(is));
                assertThat(bf.readLine(), is("HTTP/1.1 200 OK"));
                assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
            } finally {
                if (is != null) is.close();
            }
        }


        @Test
        public void POSTの場合() throws IOException {
            File file = new File("./src/test/Testresources/ResponseMessage.txt");
            file.delete();

            InputStream is = null;
            OutputStream os = null;

            try {
                is = new FileInputStream(new File("./src/test/Testresources/postTest.txt"));
                os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

                new ConnectionHandler(is, os);

            } catch (FileNotFoundException e) {
                System.out.println("エラー：指定したファイルが見つかりませんでした。");
                e.printStackTrace();
                fail();
            } finally {
                if (is != null) is.close();
                if (os != null) os.close();
            }

            try {
                is = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
                BufferedReader bf = new BufferedReader(new InputStreamReader(is));
                assertThat(bf.readLine(), is("HTTP/1.1 200 OK"));
                assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
            } finally {
                if (is != null) is.close();
            }
        }
    }


    public static class Httpバージョンテスト {
        @Test
        public void 最新のHTTPバージョンか() throws IOException {
            File file = new File("./src/test/Testresources/ResponseMessage.txt");
            file.delete();

            InputStream is = null;
            OutputStream os = null;

            try {
                is = new FileInputStream(new File("./src/test/Testresources/versionTest.txt"));
                os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

                new ConnectionHandler(is, os);

            } catch (FileNotFoundException e) {
                System.out.println("エラー：指定したファイルが見つかりませんでした。");
                e.printStackTrace();
                fail();
            } finally {
                if (is != null) is.close();
                if (os != null) os.close();
            }

            try {
                is = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
                BufferedReader bf = new BufferedReader(new InputStreamReader(is));
                assertThat(bf.readLine(), is("HTTP/1.1 505 Version Not Supported"));
                assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
            } finally {
                if (is != null) is.close();
            }
        }

    }


    public static class 指定したファイルを見つからないテスト {
        @Test
        public void 指定したファイルかどうか() throws IOException {
            File file = new File("./src/test/Testresources/ResponseMessage.txt");
            file.delete();

            InputStream is = null;
            OutputStream os = null;

            try {
                is = new FileInputStream(new File("./src/test/Testresources/notFileTest.txt"));
                os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

                new ConnectionHandler(is, os);

            } catch (FileNotFoundException e) {
                System.out.println("エラー：指定したファイルが見つかりませんでした。");
                e.printStackTrace();
                fail();
            } finally {
                if (is != null) is.close();
                if (os != null) os.close();
            }

            try {
                is = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
                BufferedReader bf = new BufferedReader(new InputStreamReader(is));
                assertThat(bf.readLine(), is("HTTP/1.1 404 Not Found"));
                assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
            } finally {
                if (is != null) is.close();
            }
        }
    }

    public static class 許されないHTTPメソッドテスト {
        @Test
        public void GETやPOST以外のメソッドかどうか() throws IOException {
            File file = new File("./src/test/Testresources/ResponseMessage.txt");
            file.delete();

            InputStream is = null;
            OutputStream os = null;

            try {
                is = new FileInputStream(new File("./src/test/Testresources/notMethodTest.txt"));
                os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

                new ConnectionHandler(is, os);

            } catch (FileNotFoundException e) {
                System.out.println("エラー：指定したファイルが見つかりませんでした。");
                e.printStackTrace();
                fail();
            } finally {
                if (is != null) is.close();
                if (os != null) os.close();
            }

            try {
                is = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
                BufferedReader bf = new BufferedReader(new InputStreamReader(is));
                assertThat(bf.readLine(), is("HTTP/1.1 405 Method Not Allowed"));
                assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
            } finally {
                if (is != null) is.close();
            }
        }
    }

    public static class リクエストラインがNULLの時でのテスト {

        @Test
        public void リクエストがヌールか() throws IOException {
            File file = new File("./src/test/Testresources/ResponseMessage.txt");
            file.delete();

            InputStream is = null;
            OutputStream os = null;

            try {
                is = new FileInputStream(new File("./src/test/Testresources/emptyingRequestTest.txt"));
                os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

                new ConnectionHandler(is, os);

            } catch (FileNotFoundException e) {
                System.out.println("エラー：指定したファイルが見つかりませんでした。");
                e.printStackTrace();
                fail();
            } finally {
                if (is != null) is.close();
                if (os != null) os.close();
            }

            try {
                is = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
                BufferedReader bf = new BufferedReader(new InputStreamReader(is));
                assertThat(bf.readLine(), is("HTTP/1.1 400 Bad Request"));
                assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
            } finally {
                if (is != null) is.close();
            }
        }
    }


    public static class GETの場合パラメーターの属性と値の取得テスト {
        @Test
        public void 不正なパラメーターかどうか() throws IOException {
            File file = new File("./src/test/Testresources/ResponseMessage.txt");
            file.delete();

            InputStream is = null;
            OutputStream os = null;

            try {
                is = new FileInputStream(new File("./src/test/Testresources/getParameterTest.txt"));
                os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

                new ConnectionHandler(is, os);

            } catch (FileNotFoundException e) {
                System.out.println("エラー：指定したファイルが見つかりませんでした。");
                e.printStackTrace();
                fail();
            } finally {
                if (is != null) is.close();
                if (os != null) os.close();
            }

            try {
                is = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
                BufferedReader bf = new BufferedReader(new InputStreamReader(is));
                assertThat(bf.readLine(), is("HTTP/1.1 400 Bad Request"));
                assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
            } finally {
                if (is != null) is.close();
            }
        }
    }


    public static class POSTの場合パラメーターの属性と値の取得テスト {
        @Test
        public void 不正なパラメーターかどうか() throws IOException {
            File file = new File("./src/test/Testresources/ResponseMessage.txt");
            file.delete();

            InputStream is = null;
            OutputStream os = null;

            try {
                is = new FileInputStream(new File("./src/test/Testresources/postParameterTest.txt"));
                os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

                new ConnectionHandler(is, os);

            } catch (FileNotFoundException e) {
                System.out.println("エラー：指定したファイルが見つかりませんでした。");
                e.printStackTrace();
                fail();
            } finally {
                if (is != null) is.close();
                if (os != null) os.close();
            }

            try {
                is = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
                BufferedReader bf = new BufferedReader(new InputStreamReader(is));
                assertThat(bf.readLine(), is("HTTP/1.1 400 Bad Request"));
                assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
            } finally {
                if (is != null) is.close();
            }
        }
    }
}