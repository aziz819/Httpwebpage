package jp.co.topgate.jan.web;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by aizijiang.aerken on 2017/05/15.
 */
public class StatusLineTest {

    public static class ステータスコードとコード説明の確認 {

        StatusLine statusLine = new StatusLine();

        @Test
        public void ステータスコードの確認() {

            int[] consistentNumber = new int[]{200, 400, 404, 405, 900, 505};

            int[] statusCode = new int[]{StatusLine.OK, StatusLine.BAD_REQUEST, StatusLine.NOT_FOUND, StatusLine.METHOD_NOT_ALLOWED, StatusLine.INTERNET_SERVER_ERROR, StatusLine.HTTP_VERSION_NOT_SUPPORTED};

            int i = 0;

            for (int statusNumber : consistentNumber) {

                statusNumber = statusLine.statusCodeCheck(statusNumber);

                assertThat(statusCode[i++], is(statusNumber));

            }
        }


        @Test
        public void コード説明の確認() {

            int[] consistentNumber = new int[]{200, 400, 404, 405, 500, 505};

            String[] codeDescriptions = new String[]{"OK", "Bad Request", "Not Found", "Method Not Allowed", "Internal Server exception", "Version Not Supported"};

            int i = 0;

            for (int statusNumber : consistentNumber) {

                String codeDescription = statusLine.getCodeDescription(statusNumber);

                if (codeDescription != null) {

                    assertThat(codeDescriptions[i++], is(codeDescription));

                }
            }
        }
    }
}