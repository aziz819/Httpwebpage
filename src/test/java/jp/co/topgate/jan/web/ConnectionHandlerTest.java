/*
package jp.co.topgate.jan.web;

import jp.co.topgate.jan.web.exception.RequestParseException;
import org.junit.Test;

import java.io.*;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

*/
/**
 * Connectionhandlerクラスのホワイトボックステスト
 * Created by aizijiang.aerken on 2017/05/07.
 *//*



public class ConnectionHandlerTest {

    public static class ConnectionHandlerコンストラクタの引数の入出力ストリームはnullの時の確認テスト {

        @Test(expected = NullPointerException.class)
        public void キャッチされるか() {
            InputStream is = null;
            OutputStream os = null;

            new ConnectionHandler(is, os);
        }
    }

    public static class writeResponseメソッド内でキャッチされる例外メッセージ確認テスト {

        @Test
        public void requestLineがnullの時に投げられる例外メッセージ() throws IOException {

            try (InputStream is = new FileInputStream(new File("./src/test/Testresources/emptyingRequestTest.txt"))) {
                RequestParser requestParser = new RequestParser(is);
                requestParser.parseRequest();

                fail("途中で例外が発生するのでここにはこないことを期待している");
            } catch (RequestParseException e) {
                assertEquals("リクエスト行が空かnullになっています", e.getMessage());
            }
        }

        @Test
        public void 不正なGETパラメーターの時に投げられる例外メッセージ() throws IOException {

            try (InputStream is = new FileInputStream(new File("./src/test/Testresources/getParameterTest.txt"))) {

                RequestParser requestParser = new RequestParser(is);
                requestParser.parseRequest();

                fail("途中で例外が発生するのでここにはこないことを期待している");
            } catch (RequestParseException e) {
                assertEquals("正しくないGETパラメーター:namekinnikuman", e.getMessage());
            }
        }


        @Test
        public void 不正なPOSTパラメーターの例外メッセージ() throws IOException {
            try (InputStream is = new FileInputStream(new File("./src/test/Testresources/postParameterTest.txt"))) {

                RequestParser requestParser = new RequestParser(is);
                requestParser.parseRequest();

                fail("途中で例外が発生するのでここにはこないことを期待している");
            } catch (RequestParseException e) {
                assertEquals("正しくないPOSTパラメーター:namekinnikuman", e.getMessage());
            }
        }
    }
}
*/
