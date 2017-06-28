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
            new RequestParser(is);
        }
    }


    public static class GETメソッドの時のリクエスト行の分割テスト {
        RequestParser requestParser;

        @Before
        public void パラメーター付きurlのリクエストを初期セットに() throws IOException {
            try (InputStream is = new FileInputStream(new File("./src/test/Testresources/getTest.txt"))) {
                requestParser = new RequestParser(is);
                requestParser.parseRequest();
            } catch (FileNotFoundException e) {
                fail("読み込みたいのファイルが存在しない");
            }
        }

        @Test
        public void 正しく分割できるか() throws Exception {

            assertThat(requestParser.getMethod(), is("GET"));
            assertThat(requestParser.getUrl(), is("/index.html"));
            assertThat(requestParser.getVersion(), is("HTTP/1.1"));
            assertThat(requestParser.getGetParameter("title"), is("yudetamago"));
            assertThat(requestParser.getGetParameter("name"), is("kinnikuman"));

        }

    }


    public static class POSTメソッドの時のリクエスト行とパラメーターの分割確認テスト {
        RequestParser requestParser;

        @Before
        public void パラメーター付きメッセージボディーのリクエストを初期セットに() throws IOException {

            try (InputStream is = new FileInputStream(new File("./src/test/Testresources/postTest.txt"))) {
                requestParser = new RequestParser(is);
                requestParser.parseRequest();
            } catch (FileNotFoundException e) {
                fail("読み込みたいのファイルが存在しない");
            }
        }

        @Test
        public void 正しく分割できるか() throws Exception {

            assertThat(requestParser.getMethod(), is("POST"));
            assertThat(requestParser.getUrl(), is("/index.html"));
            assertThat(requestParser.getVersion(), is("HTTP/1.1"));
            assertThat(requestParser.getPostparameter("title"), is("yudetamago"));
            assertThat(requestParser.getPostparameter("name"), is("kinnikuman"));

        }

    }

    public static class リクエスト行の読み込み確認テスト {
        RequestParser requestParser;

        @Before
        public void パラメーター付きリクエスト行のリクエストを初期セットに() throws IOException {

            try (InputStream is = new FileInputStream(new File("./src/test/Testresources/getTest.txt"))) {
                requestParser = new RequestParser(is);
                requestParser.parseRequest();
            } catch (FileNotFoundException e) {
                fail("読み込みたいのファイルが存在しない");
            }
        }

        @Test
        public void リクエスト行の確認() throws Exception {
            assertThat(requestParser.getRequestLine(), is("GET /index.html?title=yudetamago&name=kinnikuman HTTP/1.1"));
        }
    }
}
