package jp.co.topgate.jan.web;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by aizijiang.aerken on 2017/05/22.
 */
public class ErrorMessageBodyTest {

    public static class ステータスコード200以外の時エラーメッセージボディ確認テスト {
        
        @Test
        public void コード400の場合() {
            assertThat(String.valueOf(new ErrorMessageBody().getErrorMessageBody(400)),
                    is("<html><head><title>400 Bad Request</title></head>" +
                            "<body><h1>Bad Request</h1>" +
                            "<p>リクエストは不正な構文であるために、サーバーに理解できませんでした。<br /></p></body></html>"));
        }

        @Test
        public void コード404の場合() {
            assertThat(String.valueOf(new ErrorMessageBody().getErrorMessageBody(404)),
                    is("<html><head><title>404 Not Found</title></head>" +
                            "<body><h1>Not Found</h1>" +
                            "<p>サーバーは、リクエストURIと一致するものを見つけられませんでした。</p></body></html>"));
        }

        @Test
        public void コード405の場合() {
            assertThat(String.valueOf(new ErrorMessageBody().getErrorMessageBody(405)),
                    is("<html><head><title>405 Method Not Allowed</title></head>" +
                            "<body><h1>Not Implemented</h1>" +
                            "<p>実装されていないサーバメソッドです。</p></body></html>"));
        }

        @Test
        public void コード500の場合() {
            assertThat(String.valueOf(new ErrorMessageBody().getErrorMessageBody(500)),
                    is("<html><head><title>500 Internal Server exception</title></head>" +
                            "<body><h1>Internal Server exception</h1>" +
                            "<p>サーバー内部の不明なエラーにより表示できません。</p></body></html>"));
        }

        @Test
        public void コード505の場合() {
            assertThat(String.valueOf(new ErrorMessageBody().getErrorMessageBody(505)),
                    is("<html><head><title>505 HTTP Version Not Supported</title></head>" +
                            "<body><h1>HTTP Version Not Supported</h1>" +
                            "<p>サーバーは、リクエスト・メッセージで使用されたHTTPプロトコル・バージョンをサポートしていない、あるいはサポートを拒否している。</p> " +
                            "</body></html>"));
        }
    }

}