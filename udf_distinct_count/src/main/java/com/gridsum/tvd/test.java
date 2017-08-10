package com.gridsum.tvd;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.io.*;

/**
 * Created by T440P on 2017/7/24.
 */
public class test extends UDF {
    public String evaluate(String str) {
       return "TEST " + str;
    }

    public static void main(String [] args) throws IOException {
        InputStream inputStream = new FileInputStream("");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null)
        {
            System.out.println(line);
        }
    }
}
