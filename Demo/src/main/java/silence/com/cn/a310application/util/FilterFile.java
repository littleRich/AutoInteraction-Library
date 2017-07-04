package silence.com.cn.a310application.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 从数据包文件中提取QQ号
 *
 * Created by littleRich on 2016/10/28.
 */

public class FilterFile {
    public static String extractQQ(String f){
        long begin = System.currentTimeMillis();
        File file = new File("/sdcard/demo.txt");
        try {
            InputStream inputstream = new FileInputStream(file);
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
            String line;
          //  StringBuilder sb = new StringBuilder(line);
            while ((line = bufferedreader.readLine()) != null) {
                if(line.contains("eviluin")){
                    System.out.println(line);
                    Log.d("BashHelper", line);
                    String regex = "eviluin=(.*)&appname";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(line);
                    while(matcher.find()){
                        Log.d("BashHelper", "获取到附近的人QQ号：" + matcher.group(1));
                    }
                    break;
                }
            }
            long end = System.currentTimeMillis() - begin;
            Log.d("BashHelper", "解析数据包用时（毫秒）：" + end);
            return line;
        } catch (IOException e) {
            e.printStackTrace();
        }
    return null;
    }
}
