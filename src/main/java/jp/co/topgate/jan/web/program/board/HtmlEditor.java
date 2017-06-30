package jp.co.topgate.jan.web.program.board;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Htmlを作る
 * Created by aizijiang.aerken on 2017/06/20.
 */
class HtmlEditor {

    /**
     * 最初のアクセスの際に作る
     *
     * @param allmessage DataModelの複数のオブジェクトをもつList
     * @return 全てのデータをhtmlで立てて返す
     */

    static void showAllData(List<DataModel> allmessage) {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File("./src/main/resources/program/board/index.html"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder htmlLayer = new StringBuilder();
        htmlLayer.append("<!DOCTYPE html>\n");
        htmlLayer.append("<html>\n");
        htmlLayer.append("<meta charset=\"utf-8\">\n");
        htmlLayer.append("<head>\n");
        htmlLayer.append("    <title>JANの掲示板</title>\n");
        htmlLayer.append("    <link rel=\"stylesheet\" href=\"./css/style.css\" type=\"text/css\"/>\n");
        htmlLayer.append("</head>\n");
        htmlLayer.append("<body>\n");
        htmlLayer.append("<center>\n");
        htmlLayer.append("<div id=\"textmaru\">\n<table>\n<tr>\n<th>自</th>\n<th>由</th>\n<th>書</th>\n<th>き</th>\n<th>込</th>\n<th>み</th>\n<th>掲</th>\n<th>示</th>\n<th>板</th>\n</tr>\n</table>\n</div>\n");
        htmlLayer.append("        <div class=\"ForFirstTimePosts\">\n");
        htmlLayer.append("            <form action=\"/program/board/\" method=\"POST\">\n");
        htmlLayer.append("                &emsp;&emsp;名前:<input type=\"text\" name=\"name\" required><br>\n");
        htmlLayer.append("                タイトル:<input type=\"text\" name=\"title\" required>\n");
        htmlLayer.append("                <h3>メッセージ欄</h3>\n");
        htmlLayer.append("                <textarea name=\"content\" rows=\"5\" cols=\"35\" maxlength=\"200\" required></textarea>\n");
        htmlLayer.append("                <br>\n");
        htmlLayer.append("                パスワード:<input type=\"password\" name=\"pass\" min=\"3\" max=\"6\" required>\n");
        htmlLayer.append("                <input type=\"submit\" value=\"投稿\">\n");
        htmlLayer.append("            <hr class=\"besthr\"/>\n");
        htmlLayer.append("            </form>\n");
        htmlLayer.append("            <form action=\"/program/board/\" method=\"GET\">\n");
        htmlLayer.append("                名前で検索:<input type=\"text\" name=\"name\" required>\n");
        htmlLayer.append("                <input type=\"hidden\" name=\"instruction\" value=\"search\">\n");
        htmlLayer.append("                <input type=\"submit\" name=\"send\" value=\"検索\">\n");
        htmlLayer.append("            </form>\n");
        htmlLayer.append("        </div>\n");
        htmlLayer.append("         <h4>全件：").append(allmessage.size()).append(" 件</h4>\n");
        for (DataModel dataModel : allmessage) {
            htmlLayer.append("        <div class=\"outer\">\n");
            htmlLayer.append("            <div class=\"in\">\n");
            htmlLayer.append("                <table border=\"1\">\n");
            htmlLayer.append("                    <tr>\n");
            htmlLayer.append("                        <th id=\"maru\"><table border=\"1\"><tr><th>").append(dataModel.getCountNum()).append("</th></tr></table></th>\n");
            htmlLayer.append("                        <td>").append(dataModel.getTitle()).append("</td>\n");
            htmlLayer.append("                    </tr>\n");
            htmlLayer.append("                    <tr>\n");
            htmlLayer.append("                        <th>投稿者名</th>\n");
            htmlLayer.append("                        <td>").append(dataModel.getName()).append("</td>\n");
            htmlLayer.append("                    <tr>\n");
            htmlLayer.append("                        <th>投稿内容</th>\n");
            htmlLayer.append("                        <td>").append(dataModel.getPostContent()).append("</td>\n");
            htmlLayer.append("                    </tr>\n");
            htmlLayer.append("                    <tr>\n");
            htmlLayer.append("                        <th>投稿日</th>\n");
            htmlLayer.append("                        <td id=\"time\">").append(dataModel.getPostDate()).append("</td>\n");
            htmlLayer.append("                    </tr>\n");
            htmlLayer.append("                    <tr>\n");
            htmlLayer.append("                        <th colspan=\"2\">\n");
            htmlLayer.append("                           <form action=\"/program/board/\" method=\"POST\">\n");
            htmlLayer.append("                              <input type=\"hidden\" name=\"instruction\" value=\"delete\">\n");
            htmlLayer.append("                              <input type=\"hidden\" name=\"countNum\" value=\"").append(dataModel.getCountNum()).append("\">\n");
            htmlLayer.append("                              password:<input type=\"password\" name=\"pass\" min=\"3\" max=\"6\" required>\n");
            htmlLayer.append("                              <input type=\"submit\" value=\"削除\">\n");
            htmlLayer.append("                           </form>\n");
            htmlLayer.append("                        </th>\n");
            htmlLayer.append("                    </tr>\n");
            htmlLayer.append("                </table>\n");
            htmlLayer.append("             </div>");
            htmlLayer.append("        </div>\n");
        }
        htmlLayer.append("</center>\n");
        htmlLayer.append("</body>\n");
        htmlLayer.append("</html>");
        try {
            assert outputStream != null;
            outputStream.write(htmlLayer.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return htmlLayer.toString();
    }

    /**
     * 大小文字区別せずに名前で検索した際に検索結果を作る
     *
     * @param dataSearched 　DataModelの複数のオブジェクトもつList
     * @return 検索結果を立てて返す
     */

    static void showScannedData(List<DataModel> dataSearched) {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File("./src/main/resources/program/board/index.html"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder htmlLayer = new StringBuilder();

        htmlLayer.append("<!DOCTYPE html>\n");
        htmlLayer.append("<html>\n");
        htmlLayer.append("<meta charset=\"utf-8\">\n");
        htmlLayer.append("<head>\n");
        htmlLayer.append("    <title>JANの掲示板</title>\n");
        htmlLayer.append("    <link rel=\"stylesheet\" href=\"./css/style.css\" type=\"text/css\"/>\n");
        htmlLayer.append("</head>\n");
        htmlLayer.append("<body>\n");
        htmlLayer.append("<center>\n");
        htmlLayer.append("    <div class=\"board\">\n");
        htmlLayer.append("        <div id=\"textmaru\">\n<table>\n<tr>\n<th>自</th>\n<th>由</th>\n<th>書</th>\n<th>き</th>\n<th>込</th>\n<th>み</th>\n<th>掲</th>\n<th>示</th>\n<th>板</th>\n</tr>\n</table>\n</div>\n");
        htmlLayer.append("        <div class=\"ForFirstTimePosts\">\n");
        htmlLayer.append("            <form action=\"/program/board/\" method=\"POST\">\n");
        htmlLayer.append("                &emsp;&emsp;名前:<input type=\"text\" name=\"name\" required><br>\n");
        htmlLayer.append("                タイトル:<input type=\"text\" name=\"title\" required>\n");
        htmlLayer.append("                <h3>メッセージ欄</h3>\n");
        htmlLayer.append("                <textarea name=\"content\" rows=\"5\" cols=\"35\" maxlength=\"200\" required></textarea>\n");
        htmlLayer.append("                <br>\n");
        htmlLayer.append("                パスワード:<input type=\"password\" name=\"pass\" min=\"3\" max=\"6\">\n");
        htmlLayer.append("                <input type=\"submit\" value=\"send\">\n");
        htmlLayer.append("            <hr class=\"besthr\"/>\n");
        htmlLayer.append("            </form>\n");
        htmlLayer.append("            <form action=\"/program/board/\" method=\"GET\">\n");
        htmlLayer.append("                名前で検索:<input type=\"text\" name=\"name\" required>\n");
        htmlLayer.append("                <input type=\"hidden\" name=\"instruction\" value=\"search\">\n");
        htmlLayer.append("                <input type=\"submit\" name=\"send\" value=\"検索\">\n");
        htmlLayer.append("            </form>\n");
        htmlLayer.append("        </div>\n");
        if (dataSearched.size() < 1) {
            htmlLayer.append("    <a href=\"./index.html\"><h4>全件表示</h4></a>\n");
            htmlLayer.append("    <h4>検索結果：").append(dataSearched.size()).append(" 件</h4>\n");
            htmlLayer.append("    <h2 id=\"notExists\">見つかりませんでした！</h2>\n");
        } else {
            htmlLayer.append("            <a href=\"./index.html\"><h4>全件表示</h4></a>\n");
            htmlLayer.append("         <h4>検索結果：").append(dataSearched.size()).append(" 件</h4>\n");
            for (DataModel dataModel : dataSearched) {
                htmlLayer.append("        <div class=\"outer\">\n");
                htmlLayer.append("            <div class=\"in\">\n");
                htmlLayer.append("                <table border=\"1\">\n");
                htmlLayer.append("                    <tr>\n");
                htmlLayer.append("                        <th id=\"maru\"><table border=\"1\"><tr><th>").append(dataModel.getCountNum()).append("</th></tr></table></th>\n");
                htmlLayer.append("                        <td>").append(dataModel.getTitle()).append("</td>\n");
                htmlLayer.append("                    </tr>\n");
                htmlLayer.append("                    <tr>\n");
                htmlLayer.append("                        <th>投稿者名</th>\n");
                htmlLayer.append("                        <td>").append(dataModel.getName()).append("</td>\n");
                htmlLayer.append("                    <tr>\n");
                htmlLayer.append("                        <th>投稿内容</th>\n");
                htmlLayer.append("                        <td>").append(dataModel.getPostContent()).append("</td>\n");
                htmlLayer.append("                    </tr>\n");
                htmlLayer.append("                    <tr>\n");
                htmlLayer.append("                        <th>投稿日</th>\n");
                htmlLayer.append("                        <td id=\"time\">").append(dataModel.getPostDate()).append("</td>\n");
                htmlLayer.append("                    </tr>\n");
                htmlLayer.append("                    <tr>\n");
                htmlLayer.append("                        <th colspan=\"2\">\n");
                htmlLayer.append("                           <form action=\"/program/board/\" method=\"POST\">\n");
                htmlLayer.append("                              <input type=\"hidden\" name=\"instruction\" value=\"delete\">\n");
                htmlLayer.append("                              <input type=\"hidden\" name=\"countNum\" value=\"").append(dataModel.getCountNum()).append("\">\n");
                htmlLayer.append("                              password:<input type=\"password\" name=\"pass\" min=\"3\" max=\"6\">\n");
                htmlLayer.append("                              <input type=\"submit\" value=\"削除\">\n");
                htmlLayer.append("                           </form>\n");
                htmlLayer.append("                        </th>\n");
                htmlLayer.append("                    </tr>\n");
                htmlLayer.append("                </table>\n");
                htmlLayer.append("             </div>\n");
                htmlLayer.append("        </div>\n");
            }
        }
        htmlLayer.append("    </div>\n");
        htmlLayer.append("</center>\n");
        htmlLayer.append("</body>\n");
        htmlLayer.append("</html>");
        try {
            assert outputStream != null;
            outputStream.write(htmlLayer.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return htmlLayer.toString();
    }

}
