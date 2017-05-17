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

    public static class GETとPOSTパラメーターの分割テスト {


        @Test
        public void GETの場合() throws Exception {
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



        @Test
        public void POSTの場合() throws Exception {
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



    public static class POSTとGETメソッド取得テスト {
        @Test
        public void POSTの場合() throws Exception {
            HttpRequest req;
            try (InputStream in = new FileInputStream(new File("./src/test/Testresources/postTest.txt"))) {
                req = new HttpRequest(in);

                assertThat(req.getMethod(), is("POST"));
            }
        }




        @Test
        public void GETの場合() throws Exception {
            HttpRequest req;
            try (InputStream in = new FileInputStream(new File("./src/test/Testresources/getTest.txt"))) {
                req = new HttpRequest(in);

                assertThat(req.getMethod(), is("GET"));
            }
        }
    }




    public static class HTTPバージョン取得テスト {
        @Test
        public void 正しく値を取得できるか() throws Exception {
            HttpRequest req;
            try (InputStream in = new FileInputStream(new File("./src/test/Testresources/getTest.txt"))) {
                req = new HttpRequest(in);

                assertThat(req.getVersion(), is("HTTP/1.1"));
            }
        }
    }




    public static class URL取得テスト {
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