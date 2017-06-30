package jp.co.topgate.jan.web.program.board;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Calendar;
import java.util.List;

/**
 * Modelにデータをセットしり、データを取得したりします
 * Created by aizijiang.aerken on 2017/06/20.
 */

class DataControl {

    private static final int COUNT_START = 1;
    private static final char YEAR = '年';
    private static final char MONTH = '月';
    private static final char DAY = '日';
    private static final String WEEKDAY = "曜日";
    private static final int WEEKDAY_COUNT = 7;
    private static final char SUNDAY = '日';
    private static final char MONDAY = '月';
    private static final char TUESDAY = '火';
    private static final char WEDNESDAY = '水';
    private static final char THURSDAY = '木';
    private static final char FRIDAY = '金';
    private static final char SATURDAY = '土';
    private static final char LEFT_PARENTHESIS = '(';
    private static final char RIGHT_PARENTHESIS = ')';
    private static final String[] LF_AND_CR = { "\r\n","\n"};
    private static final String HTML_LINE_FEED = "<br>";

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
        int countNum = COUNT_START;

        if (allDataModels.size() > 0) {
            countNum = allDataModels.get(allDataModels.size() - 1).getCountNum() + 1;
        }

        DataModel dataModel = new DataModel();
        dataModel.setCountNum(countNum);
        dataModel.setName(name);
        dataModel.setTitle(title);
        dataModel.setPassword(new BCryptPasswordEncoder().encode(pass));
        dataModel.setPostContent(changeContentLineFeed(content));
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

        char[] week = new char[WEEKDAY_COUNT];
        week[0] = SUNDAY;
        week[1] = MONDAY;
        week[2] = TUESDAY;
        week[3] = WEDNESDAY;
        week[4] = THURSDAY;
        week[5] = FRIDAY;
        week[6] = SATURDAY;

        int week_int = now.get(Calendar.DAY_OF_WEEK);
        String week_string = week[week_int - 1] + WEEKDAY;

        return y + YEAR + m + MONTH + d + DAY + RIGHT_PARENTHESIS + week_string + LEFT_PARENTHESIS;
    }

    private static String changeContentLineFeed(String content) {
        for (String lineFeed : LF_AND_CR) {
            content = content.replace(lineFeed, HTML_LINE_FEED);
        }
        return content;
    }
}