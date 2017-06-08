package jp.co.topgate.jan.web;

import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * HttpRequestクラスのホワイトボックステスト
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
        HttpRequest httpRequest;

        @Before
        public void パラメーター付きurlのリクエストを初期セットに() throws IOException {
            try (InputStream is = new FileInputStream(new File("./src/test/Testresources/getTest.txt"))) {
                httpRequest = new HttpRequest(is);
                httpRequest.parseRequest();
            } catch (FileNotFoundException e) {
                fail("読み込みたいのファイルが存在しない");
            }
        }

        @Test
        public void 正しく分割できるか() throws Exception {

            assertThat(httpRequest.getMethod(), is("GET"));
            assertThat(httpRequest.getUrl(), is("/index.html"));
            assertThat(httpRequest.getVersion(), is("HTTP/1.1"));
            assertThat(httpRequest.getGetParameter("title"), is("yudetamago"));
            assertThat(httpRequest.getGetParameter("name"), is("kinnikuman"));

        }

    }


    public static class POSTメソッドの時のリクエスト行とパラメーターの分割確認テスト {
        HttpRequest httpRequest;

        @Before
        public void パラメーター付きメッセージボディーのリクエストを初期セットに() throws IOException {

            try (InputStream is = new FileInputStream(new File("./src/test/Testresources/postTest.txt"))) {
                httpRequest = new HttpRequest(is);
                httpRequest.parseRequest();
            } catch (FileNotFoundException e) {
                fail("読み込みたいのファイルが存在しない");
            }
        }

        @Test
        public void 正しく分割できるか() throws Exception {

            assertThat(httpRequest.getMethod(), is("POST"));
            assertThat(httpRequest.getUrl(), is("/index.html"));
            assertThat(httpRequest.getVersion(), is("HTTP/1.1"));
            assertThat(httpRequest.getPostparameter("title"), is("yudetamago"));
            assertThat(httpRequest.getPostparameter("name"), is("kinnikuman"));

        }

    }

    public static class リクエスト行の読み込み確認テスト {
        HttpRequest httpRequest;

        @Before
        public void パラメーター付きリクエスト行のリクエストを初期セットに() throws IOException {

            try (InputStream is = new FileInputStream(new File("./src/test/Testresources/getTest.txt"))) {
                httpRequest = new HttpRequest(is);
                httpRequest.parseRequest();
            } catch (FileNotFoundException e) {
                fail("読み込みたいのファイルが存在しない");
            }
        }

        @Test
        public void リクエスト行の確認() throws Exception {
            assertThat(httpRequest.getRequestLine(), is("GET /index.html?title=yudetamago&name=kinnikuman HTTP/1.1"));
        }
    }
}
