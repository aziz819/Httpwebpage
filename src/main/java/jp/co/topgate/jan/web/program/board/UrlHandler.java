package jp.co.topgate.jan.web.program.board;

import jp.co.topgate.jan.web.FileResource;
import jp.co.topgate.jan.web.RequestMessage;
import jp.co.topgate.jan.web.ResponseMessage;

import java.io.OutputStream;

/**
 * ハンドラー抽象クラス
 * Created by aizijiang.aerken on 2017/06/18.
 */
public abstract class UrlHandler {


    /**
     * 動的なページへか静的なページへかで行き道が違う
     *
     * @param requestMessage リクエスト分析結果を持つオブジェクト
     * @param os             出力ストリーム
     * @param statusCode     ステータスコード
     * @param fileResource   ファイルチェック、パス作成などを行うFileResourceのオブジェクト
     * @return               条件によって動的なページを作るクラスを返すか静的なページを作るクラスを返す
     */

    public static UrlHandler judgeURL(RequestMessage requestMessage, OutputStream os, int statusCode, FileResource fileResource) {
        String url = requestMessage.getUrl();

        if (url.startsWith("/program/board/")) {
            return new DynamicHandler(requestMessage, os, statusCode, fileResource);
        }
        return new ResponseMessage(requestMessage, os, statusCode, fileResource);
    }

    public abstract void writeResponse();
}
