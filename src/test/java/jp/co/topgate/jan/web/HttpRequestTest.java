package jp.co.topgate.jan.web;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by aizijiang.aerken on 2017/05/08.
 */
public class HttpRequestTest {

    public static class GETパラメーター付きリクエストラインを分割テスト{
        @Test
        public void 正しく値を取得できるか()throws Exception{
            HttpRequest req;
            try (InputStream in = new FileInputStream(new File("./src/test/Testresources/getTest.txt"))) {
                req = new HttpRequest(in);

                assertThat(req.getMethod(), is("GET"));
                assertThat(req.getURL(), is("/index.html"));
                assertThat(req.getVersion(), is("HTTP/1.1"));
                assertThat(req.getGetparameter("title"), is("yudetamago"));
                assertThat(req.getGetparameter("name"), is("kinnikuman"));
            }
        }
    }

    public static class POSTパラメーター付きリクエストラインを分割テスト {
        @Test
        public void 正しく値を取得できるか() throws Exception {
            HttpRequest req;
            try (InputStream in = new FileInputStream(new File("./src/test/Testresources/postTest.txt"))) {
                req = new HttpRequest(in);

                assertThat(req.getMethod(), is("POST"));
                assertThat(req.getURL(), is("/index.html"));
                assertThat(req.getVersion(), is("HTTP/1.1"));
                assertThat(req.getPostparameter("title"), is("yudetamago"));
                assertThat(req.getPostparameter("name"), is("kinnikuman"));
            }
        }
    }


    public static class POSTとGETメソッドテスト {
        @Test
        public void post正しく値を取得できるか() throws Exception {
            HttpRequest req;
            try (InputStream in = new FileInputStream(new File("./src/test/Testresources/postTest.txt"))) {
                req = new HttpRequest(in);

                assertThat(req.getMethod(), is("POST"));
            }
        }


        @Test
        public void get正しく値を取得できるか() throws Exception {
            HttpRequest req;
            try (InputStream in = new FileInputStream(new File("./src/test/Testresources/getTest.txt"))) {
                req = new HttpRequest(in);

                assertThat(req.getMethod(), is("GET"));
            }
        }
    }



    public class HTTPバージョンテスト {
        @Test
        public void 正しく値を取得できるか() throws Exception {
            HttpRequest req;
            try (InputStream in = new FileInputStream(new File("./src/test/Testresources/getTest.txt"))) {
                req = new HttpRequest(in);

                assertThat(req.getVersion(), is("HTTP/1.1"));
            }
        }
    }



    public static class URLのテスト {
        @Test
        public void 正しく値を取得できるか() throws Exception {
            HttpRequest req;
            try (InputStream in = new FileInputStream(new File("./src/test/Testresources/getTest.txt"))) {
                req = new HttpRequest(in);

                assertThat(req.getURL(), is("/index.html"));
            }
        }
    }

}