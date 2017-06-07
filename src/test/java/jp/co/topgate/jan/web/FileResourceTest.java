package jp.co.topgate.jan.web;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by aizijiang.aerken on 2017/05/15.
 */


public class FileResourceTest {

    public static class getContentTypeテスト {

        @Test
        public void ファイル拡張子がhtmlの場合() {
            FileResource fileResource = new FileResource("/index.html");
            assertThat(fileResource.getContentType(), is("text/html; charset=utf-8"));
        }

        @Test
        public void ファイル拡張子がcssの場合() {
            FileResource fileResource = new FileResource("/test.css");
            assertThat(fileResource.getContentType(), is("text/css; charset=utf-8"));
        }

        @Test
        public void ファイル拡張子がjsの場合() {
            FileResource fileResource = new FileResource("/test.js");
            assertThat(fileResource.getContentType(), is("text/javascript; charset=utf-8"));
        }


        @Test
        public void ファイル拡張子がjpgの場合() {
            FileResource fileResource = new FileResource("/natural1.jpg");
            assertThat(fileResource.getContentType(), is("image/jpg"));
        }


        @Test
        public void 想定外の拡張子のファイルパステスト() {
            FileResource fileResource = new FileResource("/music.mp3");
            assertThat(fileResource.getContentType(), is("application/octet-stream"));
        }

    }
}
