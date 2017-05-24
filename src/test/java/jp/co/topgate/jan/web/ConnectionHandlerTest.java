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

    public static class コンストラクタの引数の入出力ストリームはnullの時のテスト {

        @Test(expected = NullPointerException.class)
        public void キャッチされるか() {
            InputStream is = null;
            OutputStream os = null;

            new ConnectionHandler(is, os);
        }
    }


    public static class readRequestTest例外メッセージ {

        @Test
        public void コンストラクタ引数出力ストリームがnullの時の例外メッセージ() {
            InputStream is = null;
            try {
                new HttpRequest(is);
            } catch (NullPointerException e) {
                assertEquals("エラー:入力ストリームはnullになっています。", e.getMessage());
            }
        }
    }

    public static class parseRequestTest例外メッセージ {

        @Test
        public void リクエスト行がnullの例外メッセージ() {
            HttpRequest httpRequest = null;
            InputStream is = null;
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
                assertEquals("リクエスト行がnullになっています。", e.getMessage());

            } finally {
                try {
                    if (is != null) is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Test
        public void ファイル存在しないときの例外メッセージ() {
            ConnectionHandler connectionHandler = null;
            InputStream is = null;
            OutputStream os = null;
            String url = "/yudetamago.html";
            try {
                is = new FileInputStream(new File("./src/test/Testresources/getTest.txt"));
                os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
                connectionHandler = new ConnectionHandler(is, os);
                connectionHandler.getFilePas(url);
            } catch (FileNotFoundException e) {
                fail("指定したファイルが見つかりません");
            }


            try {
                connectionHandler.fileCheck(url);
            } catch (FileNotFoundException e) {
                assertEquals("エラー:ファイルは存在しないかファイルではありません", e.getMessage());
            } finally {
                try {
                    if (os != null) os.close();
                    if (is != null) is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Test
        public void 不正なリクエスト行の例外メッセージ() {
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
                }
            }

        }

        @Test
        public void 不正なGETパラメーターの例外メッセージ() {
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
                }
            }
        }
    }


    public static class writeResponse例外メッセージ {

        FileResources fileResources = new FileResources("./src/main/resources/index.html");

        OutputStream os = null;

        @Test
        public void コンストラクタ引数出力ストリームがnullの時の例外メッセージ() {
            try {

                new HttpResponse(os, fileResources);

            } catch (NullPointerException e) {

                assertEquals("エラー:出力ストリームがnullになっています", e.getMessage());
            } finally {
                try {
                    if (os != null) os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        @Test
        public void レスポンス行の作成に発生した不具合の例外メッセージ() {

            HttpResponse httpResponse = null;

            try {
                os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

                httpResponse = new HttpResponse(os, fileResources);

                StatusLine.codeDescription.clear();

            } catch (FileNotFoundException e) {

                fail("指定したファイルが見つかりません" + e.getMessage());
            }

            try {

                httpResponse.createStatusLine(200);

            } catch (RuntimeException e) {

                assertEquals("エラー:正しくないステータスコード説明", e.getMessage());
            } finally {
                try {
                    if (os != null) os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        @Test
        public void ContentTypeの作成に発生した不具合の例外メッセージ() {

            HttpResponse httpResponse = null;

            try {
                os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

                httpResponse = new HttpResponse(os, fileResources);

                httpResponse.setStatusCode(200);

                FileResources.contentType.clear();

            } catch (FileNotFoundException e) {
                fail("指定したファイルが見つかりません" + e.getMessage());
            }

            try {

                httpResponse.creatContentType();

            } catch (RuntimeException e) {

                assertEquals("エラー:正しくないContetn-Type", e.getMessage());
            } finally {
                try {
                    if (os != null) os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}