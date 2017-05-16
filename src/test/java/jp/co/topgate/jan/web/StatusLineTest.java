package jp.co.topgate.jan.web;

import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * Created by aizijiang.aerken on 2017/05/15.
 */
public class StatusLineTest {

    public static class ステータスコードのセットテスト {
        @Test
        public void コード200の場合() throws IOException {
            String statusline = String.valueOf(new StatusLine().getStatusCode(200));
            assertThat(statusline, is("HTTP/1.1 200 OK\n"));
        }

        @Test
        public void コード400の場合() throws IOException {
            String statusline = String.valueOf(new StatusLine().getStatusCode(400));
            assertThat(statusline, is("HTTP/1.1 400 Bad Request\n"));
        }

        @Test
        public void コード404の場合() throws IOException {
            String statusline = String.valueOf(new StatusLine().getStatusCode(404));
            assertThat(statusline, is("HTTP/1.1 404 Not Found\n"));
        }

        @Test
        public void コード405の場合() throws IOException {
            String statusline = String.valueOf(new StatusLine().getStatusCode(405));
            assertThat(statusline, is("HTTP/1.1 405 Method Not Allowed\n"));
        }

        @Test
        public void コード500の場合() throws IOException {
            String statusline = String.valueOf(new StatusLine().getStatusCode(500));
            assertThat(statusline, is("HTTP/1.1 500 Internet Server Error\n"));
        }

        @Test
        public void コード505の場合() throws IOException {
            String statusline = String.valueOf(new StatusLine().getStatusCode(505));
            assertThat(statusline, is("HTTP/1.1 505 Version Not Supported\n"));
        }





        public static class ステータスコード200以外の時のメッセージボディ {
            @Test
            public void コード400の場合() {
                assertThat(String.valueOf(new StatusLine().IncorrectStatusCode(400)),
                        is("<html><head><title>400 Bad Request</title></head>" +
                                "<body><h1>Bad Request</h1>" +
                                "<p>リクエストは不正な構文であるために、サーバーに理解できませんでした。<br /></p></body></html>"));
            }

            @Test
            public void コード404の場合() {
                assertThat(String.valueOf(new StatusLine().IncorrectStatusCode(404)),
                        is("<html><head><title>404 Not Found</title></head>" +
                                "<body><h1>Not Found</h1>" +
                                "<p>サーバーは、リクエストURIと一致するものを見つけられませんでした。</p></body></html>"));
            }

            @Test
            public void コード405の場合() {
                assertThat(String.valueOf(new StatusLine().IncorrectStatusCode(405)),
                        is("<html><head><title>405 Method Not Allowed</title></head>" +
                                "<body><h1>Not Implemented</h1>" +
                                "<p>実装されていないサーバメソッドです。</p></body></html>"));
            }

            @Test
            public void コード500の場合() {
                assertThat(String.valueOf(new StatusLine().IncorrectStatusCode(500)),
                        is("<html><head><title>500 Internal Server Error</title></head>" +
                                "<body><h1>Internal Server Error</h1>" +
                                "<p>サーバー内部の不明なエラーにより表示できません。</p></body></html>"));
            }

            @Test
            public void コード505の場合() {
                assertThat(String.valueOf(new StatusLine().IncorrectStatusCode(505)),
                        is("<html><head><title>505 HTTP Version Not Supported</title></head>" +
                                "<body><h1>HTTP Version Not Supported</h1>" +
                                "<p>サーバーは、リクエスト・メッセージで使用されたHTTPプロトコル・バージョンをサポートしていない、あるいはサポートを拒否している。</p> " +
                                "</body></html>"));
            }
        }
    }
}