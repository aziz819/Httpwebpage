package jp.co.topgate.jan.web.program.board;

import jp.co.topgate.jan.web.RequestMessage;
import jp.co.topgate.jan.web.ResponseMessage;

import java.io.OutputStream;

/**
 * ハンドラー抽象クラス
 * Created by aizijiang.aerken on 2017/06/18.
 */
public abstract class UrlHandler {

    private static final String PROGRAM_BOARD = "/program/board/";

    /**
     * 動的なページへか静的なページへかで行き道が違う
     *
     * @param requestMessage リクエスト分析結果を持つオブジェクト
     * @param os             出力ストリーム
     * @param statusCode     ステータスコード
     * @return               条件によって動的なページを作るクラスを返すか静的なページを作るクラスを返す
     */
    public static UrlHandler judgeURL(RequestMessage requestMessage, OutputStream os, int statusCode) {
        String url = requestMessage.getUrl();

        if (url.startsWith(PROGRAM_BOARD)) {
            return new DynamicHandler(requestMessage, os, statusCode);
        }
        return new ResponseMessage(os, statusCode);
    }

    public abstract void writeResponse();
}
