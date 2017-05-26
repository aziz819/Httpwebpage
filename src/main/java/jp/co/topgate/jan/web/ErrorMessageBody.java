package jp.co.topgate.jan.web;

/* エラーメッセージボディーを返す
 * Created by aizijiang.aerken on 2017/05/22.
 */
public class ErrorMessageBody {

     /*
     * ステーツコード200以外の場合に返すメッセージボディ
     */

    public String getErrorMessageBody(int statusCode) {

        String m;

        switch (statusCode) {

            case StatusLine.BAD_REQUEST:
                m = "<html><head><title>400 Bad Request</title></head>" +
                        "<body><h1>Bad Request</h1>" +
                        "<p>リクエストは不正な構文であるために、サーバーに理解できませんでした。<br /></p></body></html>";
                break;

            case StatusLine.NOT_FOUND:
                m = "<html><head><title>404 Not Found</title></head>" +
                        "<body><h1>Not Found</h1>" +
                        "<p>サーバーは、リクエストURIと一致するものを見つけられませんでした。</p></body></html>";
                break;

            case StatusLine.METHOD_NOT_ALLOWED:
                m = "<html><head><title>405 Method Not Allowed</title></head>" +
                        "<body><h1>Not Implemented</h1>" +
                        "<p>実装されていないサーバメソッドです。</p></body></html>";
                break;

            case StatusLine.HTTP_VERSION_NOT_SUPPORTED:
                m = "<html><head><title>505 HTTP Version Not Supported</title></head>" +
                        "<body><h1>HTTP Version Not Supported</h1>" +
                        "<p>サーバーは、リクエスト・メッセージで使用されたHTTPプロトコル・バージョンをサポートしていない、あるいはサポートを拒否している。</p> " +
                        "</body></html>";
                break;

            default:
                m = "<html><head><title>500 Internal Server exception</title></head>" +
                        "<body><h1>Internal Server exception</h1>" +
                        "<p>サーバー内部の不明なエラーにより表示できません。</p></body></html>";
        }

        return m;
    }
}
