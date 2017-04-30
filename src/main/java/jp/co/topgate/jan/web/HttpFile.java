package jp.co.topgate.jan.web;

import java.io.File;

/**
 * Created by aizijiang.aerken on 2017/04/30.
 */
public class HttpFile {

    int statuscode ;

    private String rootpath = "/Users/aizijiang.aerken/homepage/src/main/java/jp/co/topgate/jan/web";


    public int filechck(String uri) {

        File file = new File(uri);

        if (file.exists()){
            statuscode = 200 ;
        }else{
            statuscode = 404 ;
        }
        return statuscode ;
    }
}
