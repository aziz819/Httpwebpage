package jp.co.topgate.jan.web;

import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/*
 * Created by aizijiang.aerken on 2017/05/08.
 */


public class HttpRequestTest {

    public static class コンストラクタの引数入力ストリームnullの時のテスト {

        @Test(expected = NullPointerException.class)
        public void キャッチされるか() {

            InputStream is = null;

            new HttpRequest(is);
        }
    }


    public static class GETメソッドの時のリクエスト行の分割テスト {
        HttpRequest req;

        @Before
        public void パラメーター付きurlのリクエストを初期セットに() throws IOException {
            try (InputStream is = new FileInputStream(new File("./src/test/Testresources/getTest.txt"))) {
                req = new HttpRequest(is);
                req.parseRequest();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                fail("指定したファイルが見つかりません。");

            }
        }

        @Test
        public void 正しく分割できるか() throws Exception {

            assertThat(req.getMethod(), is("GET"));
            assertThat(req.getURL(), is("/index.html"));
            assertThat(req.getVersion(), is("HTTP/1.1"));
            assertThat(req.getGetparameter("title"), is("yudetamago"));
            assertThat(req.getGetparameter("name"), is("kinnikuman"));

        }

    }


    public static class POSTメソッドの時のリクエスト行とパラメーターの分割確認テスト {
        HttpRequest req;

        @Before
        public void パラメーター付きメッセージボディーのリクエストを初期セットに() throws IOException {

            try (InputStream is = new FileInputStream(new File("./src/test/Testresources/postTest.txt"))) {
                req = new HttpRequest(is);
                req.parseRequest();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                fail("指定したファイルが見つかりません。");
            }
        }

        @Test
        public void 正しく分割できるか() throws Exception {

            assertThat(req.getMethod(), is("POST"));
            assertThat(req.getURL(), is("/index.html"));
            assertThat(req.getVersion(), is("HTTP/1.1"));
            assertThat(req.getPostparameter("title"), is("yudetamago"));
            assertThat(req.getPostparameter("name"), is("kinnikuman"));

        }

    }

    public static class リクエスト行の読み込み確認 {
        HttpRequest req;

        @Before
        public void パラメーター付きリクエスト行のリクエストを初期セットに() throws IOException {

            try (InputStream is = new FileInputStream(new File("./src/test/Testresources/getTest.txt"))) {
                req = new HttpRequest(is);
                req.parseRequest();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                fail("指定したファイルが見つかりません。");
            }
        }

        @Test
        public void リクエスト行の確認() throws Exception {

            assertThat(req.getRequestLine(), is("GET /index.html?title=yudetamago&name=kinnikuman HTTP/1.1"));

        }
    }
}
