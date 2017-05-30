package jp.co.topgate.jan.web;

import jp.co.topgate.jan.web.exception.RequestParseException;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

        @Test
        public void ファイル存在しない時の投げられる例外メッセージ() {
            ConnectionHandler connectionHandler = null;
            InputStream is = null;
            OutputStream os = null;
            String url = "/yudetamago.html";
            FileResources fileResources = new FileResources(url);
            try {
                is = new FileInputStream(new File("./src/test/Testresources/getTest.txt"));
                os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
                connectionHandler = new ConnectionHandler(is, os);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                fail("指定したファイルが見つかりません");
            }

            Method method = null;
            try {
                method = ConnectionHandler.class.getDeclaredMethod("checkFile", FileResources.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                fail("テスト対象メソッドが存在しない");
            }
            method.setAccessible(true);
            try {
                method.invoke(connectionHandler, fileResources);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                fail("テスト対象メソッドにアクセスできませんでした。");
            } catch (InvocationTargetException e) {
                assertEquals(e.getCause().getMessage(), "ファイルは存在しないかファイルではありません");
            }

            try {
                if (os != null) os.close();
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
                fail("ストリームが閉じられていません");
            }

        }


        @Test
        public void 不正なリクエスト行の時に投げられる例外メッセージ() {
            HttpRequest httpRequest = null;
            InputStream is = null;
            try {
                is = new FileInputStream(new File("./src/test/Testresources/BadRequestLine.txt"));
                httpRequest = new HttpRequest(is);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                fail("指定したファイルが見つかりません");
            }

            try {
                httpRequest.parseRequest();
            } catch (RequestParseException e) {
                assertEquals("正しくないリクエストライン。", e.getMessage());
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
            HttpRequest httpRequest = null;
            InputStream is = null;
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
            HttpRequest httpRequest = null;
            InputStream is = null;
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
