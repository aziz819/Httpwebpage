package jp.co.topgate.jan.web;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by aizijiang.aerken on 2017/05/15.
 */


public class FileResourceTest {

    public static class getContentTypeテスト {
        FileResource fileResource = null;

        @Before
        public void setUpHtml(){
            fileResource = new FileResource("/index.html");
        }
        @Test
        public void ファイル拡張子がhtmlの場合() {
            assertThat(fileResource.getContentType(), is("text/html; charset=utf-8"));
        }

        @Before
        public void setUpCss(){
            fileResource = new FileResource("/test.css");
        }
        @Test
        public void ファイル拡張子がcssの場合() {
            assertThat(fileResource.getContentType(), is("text/css; charset=utf-8"));
        }

        @Before
        public void setUpJs(){
            fileResource = new FileResource("/test.js");
        }
        @Test
        public void ファイル拡張子がjsの場合() {
            assertThat(fileResource.getContentType(), is("text/javascript; charset=utf-8"));
        }

        @Before
        public void setUpJpg(){
            fileResource = new FileResource("/natural1.jpg");
        }
        @Test
        public void ファイル拡張子がjpgの場合() {
            assertThat(fileResource.getContentType(), is("image/jpg"));
        }

        @Before
        public void setUpunexpectedExtension(){
            fileResource = new FileResource("/music.mp3");
        }
        @Test
        public void 想定外の拡張子のファイルパステスト() {
            assertThat(fileResource.getContentType(), is("application/octet-stream"));
        }

    }
}
