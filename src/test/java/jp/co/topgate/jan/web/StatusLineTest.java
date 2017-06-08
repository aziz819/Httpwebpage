package jp.co.topgate.jan.web;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * StatusLineクラスのホワイトボックステスト
 * Created by aizijiang.aerken on 2017/05/15.
 */
public class StatusLineTest {


    public static class ステータス行の作成位確認テスト {

        @Test
        public void コード200の場合(){

            assertThat(new StatusLine().getStatusLine(200), is("HTTP/1.1 200 OK"));
        }

        @Test
        public void コード400の場合(){

            assertThat(new StatusLine().getStatusLine(400), is("HTTP/1.1 400 Bad Request"));
        }

        @Test
        public void コード404の場合(){

            assertThat(new StatusLine().getStatusLine(404), is("HTTP/1.1 404 Not Found"));
        }

        @Test
        public void コード405の場合() {
            assertThat(new StatusLine().getStatusLine(405), is("HTTP/1.1 405 Method Not Allowed"));
        }

        @Test
        public void コード500の場合(){
            assertThat(new StatusLine().getStatusLine(500), is("HTTP/1.1 500 Internal Server Error"));
        }

        @Test
        public void コード505の場合() {
            assertThat(new StatusLine().getStatusLine(505), is("HTTP/1.1 505 Http Version Not Supported"));
        }
    }
}