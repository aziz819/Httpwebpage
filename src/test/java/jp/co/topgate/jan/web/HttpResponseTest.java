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

			FileResource fileResource = new FileResource();
			File file = new File("/index.html");

			OutputStream os = null;
			new HttpResponse(os, file, fileResource);

		}

	}


	public static class サーバ内部エラー確認テスト {

		HttpResponse httpResponse = null;

		@Before
		public void setUpOuputStreamとFileResources() {

			File file = new File("./src/test/Testresources/ResponseMessage.txt");
			file.delete();

			FileResource fileResource = new FileResource();

			OutputStream os = null;

			try {

				os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

				httpResponse = new HttpResponse(os, file, fileResource);

				httpResponse.writeResponse(500, false);

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
					assertThat(bf.readLine(), is("HTTP/1.1 500 Internal Server Error"));
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


	public static class 正常処理200OKの時のレスポンスメッセージテスト {

		HttpResponse httpResponse = null ;

		@Before
		public void OuputStreamとFileResourcesの初期化() {
			File file = new File("./src/test/Testresources/ResponseMessage.txt");

			file.delete();

			new FileResource().checkFile("/index.html");

			FileResource fileResource = new FileResource();

			OutputStream os = null;

			try {
				file = new File("./src/main/resources/index.html");

				os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

				httpResponse = new HttpResponse(os, file, fileResource);

				httpResponse.writeResponse(200, true);


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
			File file = new File("./src/test/Testresources/ResponseMessage.txt");

			file.delete();

			FileResource fileResource = new FileResource();

			OutputStream os = null;

			try {


				os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

				httpResponse = new HttpResponse(os, file, fileResource);

				httpResponse.writeResponse(400, false);

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

			File file = new File("./src/test/Testresources/ResponseMessage.txt");

			file.delete();

			FileResource fileResource = new FileResource();

			OutputStream os = null;

			try {


				os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

				httpResponse = new HttpResponse(os, file, fileResource);

				httpResponse.writeResponse(405, false);

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

			File file = new File("./src/test/Testresources/ResponseMessage.txt");

			file.delete();

			FileResource fileResource = new FileResource();

			OutputStream os = null;

			try {

				os = new FileOutputStream(new File("./src/test/Testresources/ResponseMessage.txt"));

				httpResponse = new HttpResponse(os, file, fileResource);

				httpResponse.writeResponse(404,false);

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
