package jp.co.topgate.jan.web;

import jp.co.topgate.jan.web.exception.RequestParseException;
import org.junit.Test;

import java.io.*;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

/**
 * Created by aizijiang.aerken on 2017/05/07.
 */


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
        HttpRequest httpRequest = null;
        InputStream is = null;

        @Test
        public void requestLineがnullの時に投げられる例外メッセージ(){

            try {
                is = new FileInputStream(new File("./src/test/Testresources/emptyingRequestTest.txt"));
                httpRequest = new HttpRequest(is);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                fail("指定したファイルが見つかりません");
            }


            try {
                httpRequest.parseRequest();
            } catch (RequestParseException e) {
                assertEquals("リクエスト行が空かnullになっています", e.getMessage());
            } finally {
                try {
                    if (is != null) is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    fail("ストリームが閉じられていません");
                }
            }
        }

        @Test
        public void 不正なGETパラメーターの時に投げられる例外メッセージ() {

            try {
                is = new FileInputStream(new File("./src/test/Testresources/getParameterTest.txt"));
                httpRequest = new HttpRequest(is);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                fail("指定したファイルが見つかりません");
            }


            try {
                httpRequest.parseRequest();
            } catch (RequestParseException e) {
                assertEquals("正しくないGETパラメーター:namekinnikuman", e.getMessage());
            } finally {
                try {
                    if (is != null) is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    fail("ストリームが閉じられていません");
                }
            }
        }


        @Test
        public void 不正なPOSTパラメーターの例外メッセージ() {
            try {
                is = new FileInputStream(new File("./src/test/Testresources/postParameterTest.txt"));
                httpRequest = new HttpRequest(is);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                fail("指定したファイルが見つかりません");
            }


            try {
                httpRequest.parseRequest();
            } catch (RequestParseException e) {
                assertEquals("正しくないPOSTパラメーター:namekinnikuman", e.getMessage());
            } finally {
                try {
                    if (is != null) is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    fail("ストリームが閉じられていません");
                }
            }
        }
    }
}
