package jp.co.topgate.jan.web;

import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * HttpResponseクラスのホワイトボックステスト
 * Created by aizijiang.aerken on 2017/05/12.
 */


public class HttpResponseTest {

	public static class コンストラクタの引数出力ストリームがnullの時のテスト {

		@Test(expected = NullPointerException.class)
		public void キャッチされるか() {
			OutputStream os = null;
			new HttpResponse(os);

		}

	}


	public static class サーバ内部エラー確認テスト {

		@Before
		public void setUpOuputStreamとFileResources() throws IOException {

			File file = new File("./src/test/Testresources/ResponseMessage.txt");
			file.delete();

			try (OutputStream os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"))) {

				HttpResponse httpResponse = new HttpResponse(os);
				httpResponse.writeResponse(500, new FileResource(""));
			}
		}

		@Test
		public void レスポンスメッセージ() throws IOException {

			try (InputStream is = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"))) {

				BufferedReader bf = new BufferedReader(new InputStreamReader(is));
				assertThat(bf.readLine(), is("HTTP/1.1 500 Internal Server Error"));
				assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
			} catch (FileNotFoundException e) {
				fail("読み込みたいのファイルが存在しない");
			} catch (IOException e) {
				fail("BufferedReaderファイルの読み込みの不具合");
			}
		}

	}


	public static class 正常処理200OKの時のレスポンスメッセージテスト {

		@Before
		public void OuputStreamとFileResourcesの初期化() throws IOException {

			File file = new File("./src/test/Testresources/ResponseMessage.txt");
			file.delete();

			try (OutputStream os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"))) {

				HttpResponse httpResponse = new HttpResponse(os);
				httpResponse.writeResponse(200, new FileResource("/index.html"));
			}
		}

		@Test
		public void レスポンスメッセージ() {

			try (InputStream is = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"))) {

				BufferedReader bf = new BufferedReader(new InputStreamReader(is));

				assertThat(bf.readLine(), is("HTTP/1.1 200 OK"));

				assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));

				assertThat(bf.readLine(), is(""));

				assertThat(bf.readLine(), is("<html>"));

				assertThat(bf.readLine(), is("<meta charset=\"utf-8\">"));

				assertThat(bf.readLine(), is("<head>"));

				assertThat(bf.readLine(), is("<title>my index</title>"));

			} catch (FileNotFoundException e) {
				fail("読み込みたいのファイルが存在しない");
			} catch (IOException e) {
				fail("BufferedReaderファイルの読み込みの不具合");
			}
		}

	}


	public static class BadRequestの時のレスポンスメッセージテスト {

		@Before
		public void OuputStreamとFileResourcesの初期化() throws IOException {

			File file = new File("./src/test/Testresources/ResponseMessage.txt");
			file.delete();

			try (OutputStream os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"))) {

				HttpResponse httpResponse = new HttpResponse(os);
				httpResponse.writeResponse(400, new FileResource(""));
			}
		}

		@Test
		public void レスポンスメッセージ() throws IOException {

			try (InputStream is = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"))) {

				BufferedReader bf = new BufferedReader(new InputStreamReader(is));

				try {
					assertThat(bf.readLine(), is("HTTP/1.1 400 Bad Request"));
					assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
				} catch (FileNotFoundException e) {
					fail("読み込みたいのファイルが存在しない");
				} catch (IOException e) {
					fail("BufferedReaderファイルの読み込みの不具合");
				}
			}
		}
	}


	public static class 実装されていないHttpメソッドの時のレスポンスメッセージテスト {

		@Before
		public void OuputStreamとFileResourcesの初期化() throws IOException {

			File file = new File("./src/test/Testresources/ResponseMessage.txt");
			file.delete();

			try (OutputStream os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"))) {

				HttpResponse httpResponse = new HttpResponse(os);
				httpResponse.writeResponse(405, new FileResource(""));
			}
		}

		@Test
		public void レスポンスメッセージ() throws IOException {

			try (InputStream is = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"))) {

				BufferedReader bf = new BufferedReader(new InputStreamReader(is));
				assertThat(bf.readLine(), is("HTTP/1.1 405 Method Not Allowed"));
				assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));

			} catch (FileNotFoundException e) {
				fail("読み込みたいのファイルが存在しない");
			} catch (IOException e) {
				fail("BufferedReaderファイルの読み込みの不具合");
			}

		}
	}


	public static class ファイル存在しない時のレスポンスメッセージテスト {

		@Before
		public void OuputStreamとFileResourcesの初期化() throws IOException {

			File file = new File("./src/test/Testresources/ResponseMessage.txt");
			file.delete();

			try (OutputStream os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"))) {

				HttpResponse httpResponse = new HttpResponse(os);
				httpResponse.writeResponse(404, new FileResource(""));
			}
		}

		@Test
		public void レスポンスメッセージ() throws IOException {

			try (InputStream is = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"))) {

				BufferedReader bf = new BufferedReader(new InputStreamReader(is));
				assertThat(bf.readLine(), is("HTTP/1.1 404 Not Found"));
				assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
			} catch (FileNotFoundException e) {
				fail("読み込みたいのファイルが存在しない");
			} catch (IOException e) {
				fail("BufferedReaderファイルの読み込みの不具合");
			}
		}
	}
}
