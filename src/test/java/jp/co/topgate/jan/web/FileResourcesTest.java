package jp.co.topgate.jan.web;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/*
 * Created by aizijiang.aerken on 2017/05/15.
 */


public class FileResourcesTest {

    public static class getContentTypeテスト {

        @Test
        public void ファイル拡張子がhtmlの場合() {
            assertThat(new FileResources("/index.html").getContentType(200), is("text/html; charset=utf-8"));
        }

        @Test
        public void ファイル拡張子がcssの場合() {
            assertThat(new FileResources("/test.css").getContentType(200), is("text/css; charset=utf-8"));
        }

        @Test
        public void ファイル拡張子がjsの場合() {
            assertThat(new FileResources("/test.js").getContentType(200), is("text/javascript; charset=utf-8"));
        }

        @Test
        public void ファイル拡張子がhtmの場合() {
            assertThat(new FileResources("/test.htm").getContentType(200), is("text/htm; charset=utf-8"));
        }

        @Test
        public void ファイル拡張子がjpgの場合() {
            assertThat(new FileResources("/natural1.jpg").getContentType(200), is("image/jpg"));
        }

    }

    @Test
    public void 想定外の拡張子のファイルパステスト(){
        assertThat(new FileResources("index.mp3").getContentType(200), is("application/octet-stream"));
    }

}
