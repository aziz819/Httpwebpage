package jp.co.topgate.jan.web.program.board;

/**
 * Csvファイルにデータを書き出すための、データを取得するための経路
 * Created by aizijiang.aerken on 2017/06/20.
 */
public class DataModel {
    private int countNum;
    private String name;
    private String title;
    private String password;
    private String postContent;
    private String postDeta;

    /**
     *
     * @param countNum カウンター番号
     */
    public void setCountNum(int countNum) {
        this.countNum = countNum;
    }

    /**
     *
     * @param name 投稿者名前
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param title タイトル
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @param password パスワード
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @param postContent テキスト内容
     */
    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    /**
     *
     * @param postDate 投稿日日付と曜日
     */
    public void setPostDate(String postDate) {
        this.postDeta = postDate;
    }

    /**
     *
     * @return カウンター番号を返す
     */
    public int getCountNum() {
        return countNum;
    }

    /**
     *
     * @return 投稿者名前を返す
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return タイトルを返す
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return パスワードを返す
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @return テキスト内容を返す
     */
    public String getPostContent() {
        return postContent;
    }

    /**
     *
     * @return 投稿日日付と曜日を返す
     */
    public String getPostDate() {
        return postDeta;
    }
}
