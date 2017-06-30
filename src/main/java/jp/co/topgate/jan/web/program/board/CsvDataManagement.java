package jp.co.topgate.jan.web.program.board;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * csvファイルから全てのデータを読み込み、検索されたデータの読み込み、削除データを処理をする
 * Created by aizijiang.aerken on 2017/06/20.
 */

class CsvDataManagement {
    private static final String csvFilePath = "./src/main/resources/program/board/clientInformation.csv";

    private static final String COMMA = ",";
    /**
     * Csvファイルの中身を一行ずつ読んでモデルにセットしている
     *
     * @return DataModelのオブジェクトを返す
     */

    static List<DataModel> getAllmessage() {

        File csvfile = new File(csvFilePath);

        try {

            csvfile.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        List<DataModel> dataModels = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csvfile))) {
            String csvLine;
            while ((csvLine = bufferedReader.readLine()) != null) {

                String[] csvData = csvLine.split(COMMA);

                DataModel dataModel = new DataModel();
                dataModel.setCountNum(Integer.parseInt(csvData[0]));
                dataModel.setName(csvData[1]);
                dataModel.setTitle(csvData[2]);
                dataModel.setPassword(csvData[3]);
                dataModel.setPostContent(csvData[4]);
                dataModel.setPostDate(csvData[5]);

                dataModels.add(dataModel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataModels;
    }


    /**
     * 新しい投稿をcsvファイルに書き込む
     *
     * @param newDataModel DataModelの複数のオブジェクト
     */

    static void addNewDate(List<DataModel> newDataModel) {

        File file = new File(csvFilePath);
        try {
            try (OutputStream outputStream = new FileOutputStream(file)) {

                for (DataModel dataModel : newDataModel) {
                    String countNum = String.valueOf(dataModel.getCountNum());
                    String name = dataModel.getName();
                    String title = dataModel.getTitle();
                    String pass = dataModel.getPassword();
                    String content = dataModel.getPostContent();
                    String dateAndWeek = dataModel.getPostDate();

                    String[] dataTable = {countNum, name, title, pass, content, dateAndWeek};

                    String dataLine = String.join(COMMA, dataTable) + "\n";

                    outputStream.write(dataLine.getBytes());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param name 検索対象者の名前
     * @return 検索対象者の名前と一致するデータ全てを返す
     */

    static List<DataModel> searchFromName(String name) {

        File csvfile = new File(csvFilePath);

        List<DataModel> dataModels = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csvfile))) {
            String csvLine;
            while ((csvLine = bufferedReader.readLine()) != null) {

                String[] csvData = csvLine.split(COMMA);

                if (csvData[1].equalsIgnoreCase(name)) {

                    DataModel dataModel = new DataModel();
                    dataModel.setCountNum(Integer.parseInt(csvData[0]));
                    dataModel.setName(csvData[1]);
                    dataModel.setTitle(csvData[2]);
                    dataModel.setPassword(csvData[3]);
                    dataModel.setPostContent(csvData[4]);
                    dataModel.setPostDate(csvData[5]);

                    dataModels.add(dataModel);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataModels;
    }

    /**
     * カウンター番号が一致するデータを見つかって
     * パスワードが一致する際に削除される
     *
     * @param num  削除対象者のカウンター番号
     * @param pass 　投稿時に設定したパスワード
     */

    static void delete(String num, String pass) {

        List<DataModel> allMessage = getAllmessage();

        for (DataModel deleteMessage : allMessage) {

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

            if (deleteMessage.getCountNum() == Integer.parseInt(num) && bCryptPasswordEncoder.matches(pass, deleteMessage.getPassword())) {
                allMessage.remove(deleteMessage);

                addNewDate(allMessage);
                break;
            }
        }
    }
}