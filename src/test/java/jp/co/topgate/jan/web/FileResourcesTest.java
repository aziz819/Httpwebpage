package jp.co.topgate.jan.web;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by aizijiang.aerken on 2017/05/15.
 */
public class FileResourcesTest {

    public static class getContentTypeテスト {

        @Test
        public void ステータスコード200でファイル拡張子がhtmlの場合() {
            assertThat(new FileResources("/index.html").getContentType(200), is("Content-Type: text/html; charset=utf-8\n"));
        }

        @Test
        public void ステータスコード200でファイル拡張子がcssの場合() {
            assertThat(new FileResources("/test.css").getContentType(200), is("Content-Type: text/css; charset=utf-8\n"));
        }

        @Test
        public void ステータスコード200でファイル拡張子がjsの場合() {
            assertThat(new FileResources("/test.js").getContentType(200), is("Content-Type: text/javascript; charset=utf-8\n"));
        }

        @Test
        public void ステータスコード200でファイル拡張子がhtmの場合() {
            assertThat(new FileResources("/test.htm").getContentType(200), is("Content-Type: text/htm; charset=utf-8\n"));
        }

        @Test
        public void ステータスコード200でファイル拡張子がjpgの場合() {
            assertThat(new FileResources("/natural1.jpg").getContentType(200), is("Content-Type: image/jpg\n"));
        }

        @Test
        public void ステータスコードが200以外の場合() {
            assertThat(new FileResources("/index.html").getContentType(900), is("Content-Type: text/html; charset=utf-8\n"));
        }

    }
}