package jp.co.topgate.jan.web.program.board;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Calendar;
import java.util.List;

/**
 * Modelにデータをセットしり、データを取得したりします
 * Created by aizijiang.aerken on 2017/06/20.
 */
public class DataControl {

    /**
     * 新しい投稿のデータをモデルにセット
     *
     * @param name    名前
     * @param title   タイトル
     * @param pass    パスワード
     * @param content テキスト内容
     */
    static void receiveNewData(String name, String title, String pass, String content) {

        List<DataModel> allDataModels = CsvDataManagement.getAllmessage();
        int countNum = 1;

        if (allDataModels.size() > 0) {
            countNum = allDataModels.get(allDataModels.size() - 1).getCountNum() + 1;
        }

        DataModel dataModel = new DataModel();
        dataModel.setCountNum(countNum);
        dataModel.setName(name);
        dataModel.setTitle(title);
        dataModel.setPassword(new BCryptPasswordEncoder().encode(pass));
        dataModel.setPostContent(content);
        dataModel.setPostDate(createDateAndWeekday());

        allDataModels.add(dataModel);

        CsvDataManagement.addNewDate(allDataModels);
    }

    /**
     * 投稿された日付と曜日を作成
     *
     * @return 投稿時当日の日付 例：2017年6月27日(火曜日) の形で返す
     */

    private static String createDateAndWeekday() {
        Calendar now = Calendar.getInstance();

        String y = String.valueOf(now.get(Calendar.YEAR));
        String m = String.valueOf(now.get(Calendar.MONTH) + 1);
        String d = String.valueOf(now.get(Calendar.DATE));

        String[] week = new String[7];
        week[0] = "日";
        week[1] = "月";
        week[2] = "火";
        week[3] = "水";
        week[4] = "木";
        week[5] = "金";
        week[6] = "土";

        int week_int = now.get(Calendar.DAY_OF_WEEK);
        String week_string = week[week_int - 1] + "曜日";

        return y + "年" + m + "月" + d + "日" + "(" + week_string + ")";
    }
}