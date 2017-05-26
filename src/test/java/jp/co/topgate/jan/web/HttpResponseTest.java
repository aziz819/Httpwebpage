package jp.co.topgate.jan.web;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
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

	public static class ステータスコードが200OKの場合のメッセージボディー書き込み確認テスト {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		FileResources fileResources = new FileResources("/index.html");

		@Before
		public void setUpStream() {
			System.setOut(new PrintStream(byteArrayOutputStream));
		}

		@Test
		public void メッセージボディボディーの書き込み()
				throws FileNotFoundException,
				IllegalAccessException,
				IllegalArgumentException,
				InvocationTargetException,
				NoSuchMethodException {

			File file = new File("./src/test/Testresources/ResponseMessage.txt");

			file.delete();

			OutputStream os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

			HttpResponse httpResponse = new HttpResponse(os, fileResources);

			httpResponse.setStatusCode(200);

			Method method = HttpResponse.class.getDeclaredMethod("writeResponse");
			method.setAccessible(true);
			method.invoke(httpResponse);

			assertThat(byteArrayOutputStream.toString(), is(
					"<html>\n" +
							"<meta charset=\"utf-8\">\n" +
							"<head>\n" +
							"<title>my index</title>\n" +
							"<script\n" +
							"  src=\"https://code.jquery.com/jquery-3.2.1.js\"\n" +
							"  integrity=\"sha256-DZAnKJ/6XZ9si04Hgrsxu/8s717jcIzLy3oi35EouyE=\"\n" +
							"  crossorigin=\"anonymous\"></script>\n" +
							"  <script src=\"./js/test.js\"></script>\n" +
							"  <link rel=\"stylesheet\" href=\"./css/test.css\" type=\"text/css\" />\n" +
							"</head>\n" +
							"<body>\n" +
							"  <div class=\"one\">\n" +
							"    <h2>Beautiful Natural</h2>\n" +
							"    <img src=\"./image/natural1.jpg\" height=\"350\" width=\"390\"> <img src=\"./image/natural2.jpg\" height=\"350\" width=\"390\"> <img src=\"./image/natural3.gif\" height=\"350\" width=\"390\"><br><br>\n" +
							"    <button>hide</button>\n" +
							"  </div>\n" +
							"</body>\n" +
							"</html>\n" + System.lineSeparator()));

			try {
				if(os != null)os.close();
			} catch (IOException e) {
				e.printStackTrace();
				fail("ストリームが閉じられていません");
			}
		}

	}

	public static class ステータスコードが200以外の時のメッセージボディー書き込み確認テスト {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		FileResources fileResources = new FileResources("");

		@Before
		public void setUpStrems() {
			System.setOut(new PrintStream(byteArrayOutputStream));
		}


		@Test
		public void ステータスコードが400の場合() throws FileNotFoundException,
				IllegalAccessException,
				IllegalArgumentException,
				InvocationTargetException,
				NoSuchMethodException {

			OutputStream os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

			HttpResponse httpResponse = new HttpResponse(os, fileResources);

			httpResponse.setStatusCode(400);

			Method method = HttpResponse.class.getDeclaredMethod("writeResponse");
			method.setAccessible(true);
			method.invoke(httpResponse);

			assertThat(byteArrayOutputStream.toString(), is("<html><head><title>400 Bad Request</title></head>" +
					"<body><h1>Bad Request</h1>" +
					"<p>リクエストは不正な構文であるために、サーバーに理解できませんでした。<br /></p></body></html>" + System.lineSeparator()));

			try {
				if(os != null)os.close();
			} catch (IOException e) {
				e.printStackTrace();
				fail("ストリームが閉じられていません");
			}
		}

		@Test
		public void ステータスコードが404の場合() throws FileNotFoundException,
				IllegalAccessException,
				IllegalArgumentException,
				InvocationTargetException,
				NoSuchMethodException {

			OutputStream os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

			HttpResponse httpResponse = new HttpResponse(os, fileResources);

			httpResponse.setStatusCode(404);

			Method method = HttpResponse.class.getDeclaredMethod("writeResponse");
			method.setAccessible(true);
			method.invoke(httpResponse);

			assertThat(byteArrayOutputStream.toString(), is("<html><head><title>404 Not Found</title></head>" +
					"<body><h1>Not Found</h1>" +
					"<p>サーバーは、リクエストURIと一致するものを見つけられませんでした。</p></body></html>" + System.lineSeparator()));


			try {
				if(os != null)os.close();
			} catch (IOException e) {
				e.printStackTrace();
				fail("ストリームが閉じられていません");
			}
		}

		@Test
		public void ステータスコードが405の場合()
				throws FileNotFoundException,
				IllegalAccessException,
				IllegalArgumentException,
				InvocationTargetException,
				NoSuchMethodException {
			OutputStream os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

			HttpResponse httpResponse = new HttpResponse(os, fileResources);

			httpResponse.setStatusCode(405);

			Method method = HttpResponse.class.getDeclaredMethod("writeResponse");
			method.setAccessible(true);
			method.invoke(httpResponse);

			assertThat(byteArrayOutputStream.toString(), is("<html><head><title>405 Method Not Allowed</title></head>" +
					"<body><h1>Not Implemented</h1>" +
					"<p>実装されていないサーバメソッドです。</p></body></html>" + System.lineSeparator()));


			try {
				if(os != null)os.close();
			} catch (IOException e) {
				e.printStackTrace();
				fail("ストリームが閉じられていません");
			}
		}


		@Test
		public void ステータスコードが505の場合()
				throws FileNotFoundException,
				IllegalAccessException,
				IllegalArgumentException,
				InvocationTargetException,
				NoSuchMethodException {

			OutputStream os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

			HttpResponse httpResponse = new HttpResponse(os, fileResources);

			httpResponse.setStatusCode(505);

			Method method = HttpResponse.class.getDeclaredMethod("writeResponse");
			method.setAccessible(true);
			method.invoke(httpResponse);

			assertThat(byteArrayOutputStream.toString(), is("<html><head><title>505 HTTP Version Not Supported</title></head>" +
					"<body><h1>HTTP Version Not Supported</h1>" +
					"<p>サーバーは、リクエスト・メッセージで使用されたHTTPプロトコル・バージョンをサポートしていない、あるいはサポートを拒否している。</p> " +
					"</body></html>" + System.lineSeparator()));


			try {
				if(os != null)os.close();
			} catch (IOException e) {
				e.printStackTrace();
				fail("ストリームが閉じられていません");
			}
		}


		@Test
		public void ステータスコードが500の場合()
				throws FileNotFoundException,
				IllegalAccessException,
				IllegalArgumentException,
				InvocationTargetException,
				NoSuchMethodException {

			OutputStream os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

			HttpResponse httpResponse = new HttpResponse(os, fileResources);

			httpResponse.setStatusCode(500);

			Method method = HttpResponse.class.getDeclaredMethod("writeResponse");
			method.setAccessible(true);
			method.invoke(httpResponse);

			assertThat(byteArrayOutputStream.toString(), is("<html><head><title>500 Internal Server exception</title></head>" +
					"<body><h1>Internal Server exception</h1>" +
					"<p>サーバー内部の不明なエラーにより表示できません。</p></body></html>" + System.lineSeparator()));


			try {
				if(os != null)os.close();
			} catch (IOException e) {
				e.printStackTrace();
				fail("ストリームが閉じられていません");
			}
		}

	}


	public static class 想定外のステータスコードの時のレスポンスメッセージ確認テスト {

		HttpResponse httpResponse = null;

		@Before
		public void setUpOuputStreamとFileResources()
				throws FileNotFoundException,
				IllegalAccessException,
				IllegalArgumentException,
				InvocationTargetException,
				NoSuchMethodException {

			File file = null;

			FileResources fileResources = null;

			OutputStream os = null;

			try {
				file = new File("./src/test/Testresources/ResponseMessage.txt");

				file.delete();

				fileResources = new FileResources("/index.html");

				os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

				httpResponse = new HttpResponse(os, fileResources);

				Method method = HttpResponse.class.getDeclaredMethod("createStatusLine", int.class);
				method.setAccessible(true);
				method.invoke(httpResponse, 900);

				method = HttpResponse.class.getDeclaredMethod("createContentType");
				method.setAccessible(true);
				method.invoke(httpResponse);

				method = HttpResponse.class.getDeclaredMethod("writeResponse");
				method.setAccessible(true);
				method.invoke(httpResponse);

			} catch (FileNotFoundException e) {

				e.printStackTrace();

				fail("エラー：指定したファイルが見つかりませんでした。");

			} catch (NullPointerException e) {

				e.printStackTrace();

				fail("エラー：osかfileresourcesがnullになっています。");
			}finally {

				try {
					if(os != null)os.close();
				} catch (IOException e) {
					e.printStackTrace();
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
					fail("ストリームが閉じられていません");
				}
			}
		}

	}


	public static class ファイル存在で200OKの時のレスポンスメッセージ {

		HttpResponse httpResponse = null ;

		@Before
		public void OuputStreamとFileResourcesの初期化()
				throws FileNotFoundException,
				IllegalAccessException,
				IllegalArgumentException,
				InvocationTargetException,
				NoSuchMethodException {

			File file = null;

			FileResources fileResources = null;

			OutputStream os = null;

			try {
				file = new File("./src/test/Testresources/ResponseMessage.txt");

				file.delete();

				fileResources = new FileResources("/index.html");

				os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

				httpResponse = new HttpResponse(os, fileResources);

				Method method = HttpResponse.class.getDeclaredMethod("createStatusLine", int.class);
				method.setAccessible(true);
				method.invoke(httpResponse, 200);

				method = HttpResponse.class.getDeclaredMethod("createContentType");
				method.setAccessible(true);
				method.invoke(httpResponse);

				method = HttpResponse.class.getDeclaredMethod("writeResponse");
				method.setAccessible(true);
				method.invoke(httpResponse);


			} catch (FileNotFoundException e) {

				e.printStackTrace();

				fail("エラー：指定したファイルが見つかりませんでした。");

			} catch (NullPointerException e) {

				e.printStackTrace();

				fail("エラー：osがnullになっています。");
			}finally {

				try {
					if(os != null)os.close();
				} catch (IOException e) {
					e.printStackTrace();
					fail("ファイルが閉じられていません");
				}
			}
		}

		@Test
		public void レスポンスメッセージ() throws IOException {

			InputStream is = null;

			try {
				is = new FileInputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

				BufferedReader bf = new BufferedReader(new InputStreamReader(is));

				assertThat(bf.readLine(), is("HTTP/1.1 200 OK"));

				assertThat(bf.readLine(), is("Content-Type: text/html; charset=utf-8"));

				assertThat(bf.readLine(), is(""));

				assertThat(bf.readLine(), is("<html>"));

				assertThat(bf.readLine(), is("<meta charset=\"utf-8\">"));

				assertThat(bf.readLine(), is("<head>"));

				assertThat(bf.readLine(), is("<title>my index</title>"));

			} finally {
				if (is != null) is.close();
			}
		}

	}


	public static class BadRequestの時のレスポンスメッセージ {

		HttpResponse httpResponse = null ;

		@Before
		public void OuputStreamとFileResourcesの初期化()
				throws FileNotFoundException,
				IllegalAccessException,
				IllegalArgumentException,
				InvocationTargetException,
				NoSuchMethodException {

			File file = null;

			FileResources fileResources = null;

			OutputStream os = null;

			try {
				file = new File("./src/test/Testresources/ResponseMessage.txt");

				file.delete();

				fileResources = new FileResources("/index.html");

				os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

				httpResponse = new HttpResponse(os, fileResources);

				Method method = HttpResponse.class.getDeclaredMethod("createStatusLine", int.class);
				method.setAccessible(true);
				method.invoke(httpResponse, 400);

				method = HttpResponse.class.getDeclaredMethod("createContentType");
				method.setAccessible(true);
				method.invoke(httpResponse);

				method = HttpResponse.class.getDeclaredMethod("writeResponse");
				method.setAccessible(true);
				method.invoke(httpResponse);

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



	public static class 実装されていないHttpメソッドの時のレスポンスメッセージ {

		HttpResponse httpResponse = null ;

		@Before
		public void OuputStreamとFileResourcesの初期化()
				throws FileNotFoundException,
				IllegalAccessException,
				IllegalArgumentException,
				InvocationTargetException,
				NoSuchMethodException {

			File file = null;

			FileResources fileResources = null;

			OutputStream os = null;

			try {
				file = new File("./src/test/Testresources/ResponseMessage.txt");

				file.delete();

				fileResources = new FileResources("/index.html");

				os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

				httpResponse = new HttpResponse(os, fileResources);

				Method method = HttpResponse.class.getDeclaredMethod("createStatusLine", int.class);
				method.setAccessible(true);
				method.invoke(httpResponse, 405);

				method = HttpResponse.class.getDeclaredMethod("createContentType");
				method.setAccessible(true);
				method.invoke(httpResponse);

				method = HttpResponse.class.getDeclaredMethod("writeResponse");
				method.setAccessible(true);
				method.invoke(httpResponse);

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


	public static class ファイル存在しない時のレスポンスメッセージ {

		HttpResponse httpResponse = null ;

		@Before
		public void OuputStreamとFileResourcesの初期化()
				throws FileNotFoundException,
				IllegalAccessException,
				IllegalArgumentException,
				InvocationTargetException,
				NoSuchMethodException {

			File file = null;

			FileResources fileResources = null;

			OutputStream os = null;

			try {
				file = new File("./src/test/Testresources/ResponseMessage.txt");

				file.delete();

				fileResources = new FileResources("/index.html");

				os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

				httpResponse = new HttpResponse(os, fileResources);

				Method method = HttpResponse.class.getDeclaredMethod("createStatusLine", int.class);
				method.setAccessible(true);
				method.invoke(httpResponse, 404);

				method = HttpResponse.class.getDeclaredMethod("createContentType");
				method.setAccessible(true);
				method.invoke(httpResponse);

				method = HttpResponse.class.getDeclaredMethod("writeResponse");
				method.setAccessible(true);
				method.invoke(httpResponse);

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

	public static class リクエスト行とContentTypeを組み立てるメッソドのテスト {

		FileResources fileResources = new FileResources("./src/main/resources/index.html");

		OutputStream os = null;


		@Test
		public void createStatusLineメソッドレスポンス行の作成に発生した不具合の例外メッセージ() throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException {

			HttpResponse httpResponse = null;

			try {
				os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

				httpResponse = new HttpResponse(os, fileResources);

				StatusLine.codeDescription.clear();

			} catch (FileNotFoundException e) {

				fail("指定したファイルが見つかりません" + e.getMessage());
			}


			Method method = HttpResponse.class.getDeclaredMethod("createStatusLine", int.class);
			method.setAccessible(true);
			try {
				method.invoke(httpResponse, 200);
			} catch (InvocationTargetException e) {
				assertEquals(e.getCause().getMessage(), "ステータスコード説明がnullで成り立っていない");
			}

			try {
				if (os != null) os.close();
			} catch (IOException e) {
				e.printStackTrace();
				fail("ストリームクローズに不具合が発生しました。");
			}

		}


		@Test
		public void createContentTypeメソッドContentTypeの作成に発生した不具合の例外メッセージ() throws NoSuchMethodException, IllegalAccessException {

			HttpResponse httpResponse = null;

			try {
				os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

				httpResponse = new HttpResponse(os, fileResources);

				FileResources.contentType.clear();

				httpResponse.setStatusCode(200);

				FileResources.contentType.put("html", null);


			} catch (FileNotFoundException e) {
				fail("指定したファイルが見つかりません" + e.getMessage());
			}


			Method method = HttpResponse.class.getDeclaredMethod("createContentType");
			method.setAccessible(true);
			try {
				method.invoke(httpResponse);
			} catch (InvocationTargetException e) {
				assertEquals(e.getCause().getMessage(), "Contetn-Typeがnullで成り立っていない");
			}


			try {
				if (os != null) os.close();
			} catch (IOException e) {
				e.printStackTrace();
				fail("ストリームクローズに不具合が発生しました。");
			}

		}
	}

}
