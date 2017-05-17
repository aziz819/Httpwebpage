package jp.co.topgate.jan.web.Error;

/**
 * Created by aizijiang.aerken on 2017/05/17.
 */
public class VersionNotSupported extends RuntimeException{
    public VersionNotSupported(String s) {
        super(s);
    }
}
