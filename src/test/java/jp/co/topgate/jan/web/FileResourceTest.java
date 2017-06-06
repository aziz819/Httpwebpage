package jp.co.topgate.jan.web;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by aizijiang.aerken on 2017/05/15.
 */


public class FileResourceTest {

    public static class getContentTypeテスト {

        FileResource fileResource = new FileResource();

        @Test
        public void ファイル拡張子がhtmlの場合() {
            fileResource.checkFile("/index.html");
            assertThat(fileResource.getContentType(), is("text/html; charset=utf-8"));
        }

        @Test
        public void ファイル拡張子がcssの場合() {
            fileResource.checkFile("/test.css");
            assertThat(fileResource.getContentType(), is("text/css; charset=utf-8"));
        }

        @Test
        public void ファイル拡張子がjsの場合() {
            fileResource.checkFile("/test.js");
            assertThat(fileResource.getContentType(), is("text/javascript; charset=utf-8"));
        }

        @Test
        public void ファイル拡張子がhtmの場合() {
            fileResource.checkFile("index.htm");
            assertThat(fileResource.getContentType(), is("text/htm; charset=utf-8"));
        }

        @Test
        public void ファイル拡張子がjpgの場合() {
            fileResource.checkFile("/natural1.jpg");
            assertThat(fileResource.getContentType(), is("image/jpg"));
        }

        @Test
        public void 想定外の拡張子のファイルパステスト() {
            fileResource.checkFile("/music.mp3");
            assertThat(fileResource.getContentType(), is("application/octet-stream"));
        }

    }
}
