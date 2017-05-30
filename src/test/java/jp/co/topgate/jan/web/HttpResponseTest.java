package jp.co.topgate.jan.web;

import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by aizijiang.aerken on 2017/05/12.
 */
public class HttpResponseTest {

	public static class コンストラクタの引数出力ストリームがnullの時のテスト {

		@Test(expected = NullPointerException.class)
		public void キャッチされるか() {

			FileResources fileResources = new FileResources("");

			OutputStream os = null;
			new HttpResponse(os, fileResources);

		}

	}

	public static class エラーメッセージボディー書き込み確認テスト {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		FileResources fileResources = new FileResources("");

		@Before
		public void setUpStrems() {
			System.setOut(new PrintStream(byteArrayOutputStream));
		}


		@Test
		public void ステータスコードが400の場合() {

			OutputStream os = null;
			try {
				os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				fail("指定したファイルが見つかりません");
			}

			HttpResponse httpResponse = new HttpResponse(os, fileResources);

			httpResponse.writeResponse(400);

			assertThat(byteArrayOutputStream.toString(), is("HTTP/1.1 400 Bad Request\n" +
					"Content-Type: text/html; charset=utf-8" + "\n\n" + "<html><head><title>400 Bad Request</title></head>" +
					"<body><h1>Bad Request</h1>" +
					"<p>リクエストは不正な構文であるために、サーバーに理解できませんでした。<br /></p></body></html>" + System.lineSeparator()));

			try {
				if(os != null)os.close();
			} catch (IOException e) {
				e.printStackTrace();
				fail("ストリームクローズに不具合が発生しました。");
			}
		}

		@Test
		public void ステータスコードが404の場合() {

			OutputStream os = null;
			try {
				os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				fail("エラー：指定したファイルが見つかりません");
			}

			HttpResponse httpResponse = new HttpResponse(os, fileResources);

			httpResponse.writeResponse(404);

			assertThat(byteArrayOutputStream.toString(), is("HTTP/1.1 404 Not Found\n" +
					"Content-Type: text/html; charset=utf-8\n\n" +
					"<html><head><title>404 Not Found</title></head>" +
					"<body><h1>Not Found</h1>" +
					"<p>サーバーは、リクエストURIと一致するものを見つけられませんでした。</p></body></html>" + System.lineSeparator()));


			try {
				if(os != null)os.close();
			} catch (IOException e) {
				e.printStackTrace();
				fail("ストリームクローズに不具合が発生しました。");
			}
		}

		@Test
		public void ステータスコードが405の場合() {
			OutputStream os = null;
			try {
				os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				fail("エラー：指定したファイルが見つかりません");
			}

			HttpResponse httpResponse = new HttpResponse(os, fileResources);

			httpResponse.writeResponse(405);


			assertThat(byteArrayOutputStream.toString(), is("HTTP/1.1 405 Method Not Allowed\n" +
					"Content-Type: text/html; charset=utf-8\n\n" +
					"<html><head><title>405 Method Not Allowed</title></head>" +
					"<body><h1>Not Implemented</h1>" +
					"<p>実装されていないサーバメソッドです。</p></body></html>" + System.lineSeparator()));


			try {
				if(os != null)os.close();
			} catch (IOException e) {
				e.printStackTrace();
				fail("ストリームクローズに不具合が発生しました。");
			}
		}


		@Test
		public void ステータスコードが505の場合() {

			OutputStream os = null;

			try {
				os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				fail("エラー：指定したファイルが見つかりません");
			}

			HttpResponse httpResponse = new HttpResponse(os, fileResources);

			httpResponse.writeResponse(505);

			assertThat(byteArrayOutputStream.toString(), is("HTTP/1.1 505 Version Not Supported\n" +
					"Content-Type: text/html; charset=utf-8\n\n" +
					"<html><head><title>505 HTTP Version Not Supported</title></head>" +
					"<body><h1>HTTP Version Not Supported</h1>" +
					"<p>サーバーは、リクエスト・メッセージで使用されたHTTPプロトコル・バージョンをサポートしていない、あるいはサポートを拒否している。</p> " +
					"</body></html>" + System.lineSeparator()));


			try {
				if(os != null)os.close();
			} catch (IOException e) {
				e.printStackTrace();
				fail("ストリームクローズに不具合が発生しました。");
			}
		}


		@Test
		public void ステータスコードが500の場合() {

			OutputStream os = null;
			try {
				os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				fail("エラー：指定したファイルが見つかりませんでした");
			}

			HttpResponse httpResponse = new HttpResponse(os, fileResources);

			httpResponse.writeResponse(500);

			assertThat(byteArrayOutputStream.toString(), is("HTTP/1.1 500 Internal Server exception\n" +
					"Content-Type: text/html; charset=utf-8\n\n" +
					"<html><head><title>500 Internal Server exception</title></head>" +
					"<body><h1>Internal Server exception</h1>" +
					"<p>サーバー内部の不明なエラーにより表示できません。</p></body></html>" + System.lineSeparator()));


			try {
				if(os != null)os.close();
			} catch (IOException e) {
				e.printStackTrace();
				fail("ストリームクローズに不具合が発生しました。");
			}
		}

	}


	public static class 想定外のステータスコードの時のレスポンスメッセージ確認テスト {

		HttpResponse httpResponse = null;

		@Before
		public void setUpOuputStreamとFileResources() {

			File file = new File("./src/test/Testresources/ResponseMessage.txt");
			file.delete();

			FileResources fileResources = null;

			OutputStream os = null;

			try {

				fileResources = new FileResources("/index.html");

				os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

				httpResponse = new HttpResponse(os, fileResources);

				httpResponse.writeResponse(900);


			} catch (FileNotFoundException e) {

				e.printStackTrace();

				fail("エラー：指定したファイルが見つかりませんでした。");

			} finally {

				try {
					if(os != null)os.close();
				} catch (IOException e) {
					e.printStackTrace();
					fail("ストリームクローズに不具合が発生しました。");
				}
			}
		}

		@Test
		public void レスポンスメッセージ() {

			InputStream is = null;

			try {
				try {
					is = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					fail("指定したファイルが見つかりません");
				}

				BufferedReader bf = new BufferedReader(new InputStreamReader(is));

				try {
					assertThat(bf.readLine(), is("HTTP/1.1 500 Internal Server exception"));
					assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
				} catch (IOException e) {
					e.printStackTrace();
					fail("BufferedReader読み込みエラー");
				}

			} finally {
				if (is != null) try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
					fail("ストリームクローズに不具合が発生しました。");
				}
			}
		}

	}


	public static class ファイル有りで200OKの時のレスポンスメッセージテスト {

		HttpResponse httpResponse = null ;

		@Before
		public void OuputStreamとFileResourcesの初期化() {

			File file = null;

			FileResources fileResources = null;

			OutputStream os = null;

			try {
				file = new File("./src/test/Testresources/ResponseMessage.txt");

				file.delete();

				fileResources = new FileResources("/index.html");

				os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

				httpResponse = new HttpResponse(os, fileResources);

				httpResponse.writeResponse(200);


			} catch (FileNotFoundException e) {

				e.printStackTrace();

				fail("エラー：指定したファイルが見つかりませんでした。");

			} finally {

				try {
					if(os != null)os.close();
				} catch (IOException e) {
					e.printStackTrace();
					fail("ストリームクローズに不具合が発生しました。");
				}
			}
		}

		@Test
		public void レスポンスメッセージ() {

			InputStream is = null;

			try {
				try {
					is = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					fail("指定したファイルが見つかりません");
				}

				BufferedReader bf = new BufferedReader(new InputStreamReader(is));

				try {
					assertThat(bf.readLine(), is("HTTP/1.1 200 OK"));

					assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));

					assertThat(bf.readLine(), is(""));

					assertThat(bf.readLine(), is("<html>"));

					assertThat(bf.readLine(), is("<meta charset=\"utf-8\">"));

					assertThat(bf.readLine(), is("<head>"));

					assertThat(bf.readLine(), is("<title>my index</title>"));

				} catch (IOException e) {
					e.printStackTrace();
					fail("BuffredReader読み込みエラー");
				}


			} finally {
				if (is != null) try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
					fail("ストリームクローズに不具合が発生しました。");

				}
			}
		}

	}


	public static class BadRequestの時のレスポンスメッセージテスト {

		HttpResponse httpResponse = null ;

		@Before
		public void OuputStreamとFileResourcesの初期化() {

			File file = null;

			FileResources fileResources = null;

			OutputStream os = null;

			try {
				file = new File("./src/test/Testresources/ResponseMessage.txt");

				file.delete();

				fileResources = new FileResources("/index.html");

				os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

				httpResponse = new HttpResponse(os, fileResources);

				httpResponse.writeResponse(400);

			} catch (FileNotFoundException e) {

				e.printStackTrace();

				fail("エラー：指定したファイルが見つかりませんでした。");

			} catch (NullPointerException e) {

				e.printStackTrace();

				fail("エラー：osがnullになっています。");
			}finally{
				if(os != null) try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
					fail("ストリームクローズに不具合が発生しました。");
				}
			}
		}

		@Test
		public void レスポンスメッセージ(){

			InputStream is = null;

			try {
				try {
					is = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					fail("指定したファイルが見つかりません");
				}

				BufferedReader bf = new BufferedReader(new InputStreamReader(is));

				try {
					assertThat(bf.readLine(), is("HTTP/1.1 400 Bad Request"));
					assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));
				} catch (IOException e) {
					e.printStackTrace();
					fail("BufferedReader読み込みエラー");
				}


			} finally {
				if (is != null) try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
					fail("ストリームクローズに不具合が発生しました。");
				}
			}
		}

	}


	public static class 実装されていないHttpメソッドの時のレスポンスメッセージテスト {

		HttpResponse httpResponse = null ;

		@Before
		public void OuputStreamとFileResourcesの初期化() {

			File file = null;

			FileResources fileResources = null;

			OutputStream os = null;

			try {
				file = new File("./src/test/Testresources/ResponseMessage.txt");

				file.delete();

				fileResources = new FileResources("/index.html");

				os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

				httpResponse = new HttpResponse(os, fileResources);

				httpResponse.writeResponse(405);

			} catch (FileNotFoundException e) {

				e.printStackTrace();

				fail("エラー：指定したファイルが見つかりませんでした。");

			} catch (NullPointerException e) {

				e.printStackTrace();

				fail("エラー：osがnullになっています。");
			}finally{
				if(os != null) try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
					fail("ストリームクローズに不具合が発生しました。");
				}
			}
		}

		@Test
		public void レスポンスメッセージ(){

			InputStream is = null;

			try {
				try {
					is = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					fail("指定したファイルが見つかりません");
				}

				BufferedReader bf = new BufferedReader(new InputStreamReader(is));

				try {
					assertThat(bf.readLine(), is("HTTP/1.1 405 Method Not Allowed"));
					assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));

				} catch (IOException e) {
					e.printStackTrace();
					fail("BufferedReader読み込みエラー");
				}


			} finally {
				if (is != null) try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
					fail("ストリームクローズに不具合が発生しました。");
				}
			}
		}

	}


	public static class ファイル存在しない時のレスポンスメッセージテスト {

		HttpResponse httpResponse = null ;

		@Before
		public void OuputStreamとFileResourcesの初期化() {

			File file = null;

			FileResources fileResources = null;

			OutputStream os = null;

			try {
				file = new File("./src/test/Testresources/ResponseMessage.txt");

				file.delete();

				fileResources = new FileResources("/index.html");

				os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

				httpResponse = new HttpResponse(os, fileResources);

				httpResponse.writeResponse(404);

			} catch (FileNotFoundException e) {

				e.printStackTrace();

				fail("エラー：指定したファイルが見つかりませんでした。");

			} catch (NullPointerException e) {

				e.printStackTrace();

				fail("エラー：osがnullになっています。");
			}finally{
				if(os != null) try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
					fail("ストリームクローズに不具合が発生しました。");
				}
			}
		}

		@Test
		public void レスポンスメッセージ(){

			InputStream is = null;

			try {
				try {
					is = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					fail("指定したファイルが見つかりません");
				}

				BufferedReader bf = new BufferedReader(new InputStreamReader(is));

				try {
					assertThat(bf.readLine(), is("HTTP/1.1 404 Not Found"));
					assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));

				} catch (IOException e) {
					e.printStackTrace();
					fail("BufferedReader読み込みエラー");
				}

			} finally {
				if (is != null) try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
					fail("ストリームクローズに不具合が発生しました。");
				}
			}
		}
	}
}
