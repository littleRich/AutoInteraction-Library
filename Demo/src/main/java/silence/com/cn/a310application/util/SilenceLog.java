package silence.com.cn.a310application.util;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SilenceLog {


    private static final String TAG = "Silence";
    private static Toast toast;

    public static void d(String msg) {
        //        if (Util.isDebug()) {
        Log.d(TAG, msg);
        f(msg);
        //        }
    }

    public static void v(String msg) {
        //        if (Util.isDebug()) {
        Log.v(TAG, msg);
        f(msg);
        //        }
    }

    public static void e(String msg) {
        //        if (Util.isDebug()) {
        Log.e(TAG, msg);
        f(msg);
        //        }
    }

    public static void i(String msg) {
        //        if (Util.isDebug()) {
        Log.i(TAG, msg);
        f(msg);
        //        }
    }

    public static void w(String msg) {
        //        if (Util.isDebug()) {
        Log.w(TAG, msg);
        f(msg);
        //        }
    }

    public static void f(String msg) {
        //        if (Util.isDebug()) {
        // writeFile(msg);
        //        }
    }

    private static void writeFile(String msg) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Log.d("TestFile", "SD card is not avaiable/writeable right now.");
            return;
        }
        try {
            String pathName = Environment.getExternalStorageDirectory().getPath() + "/ForeSightLog/";
            String fileName = "runningLog.txt";
            File path = new File(pathName);
            File file = new File(pathName + fileName);
            if (!path.exists()) {
                Log.d("runningLog", "Create the path:" + pathName);
                path.mkdir();
            }
            if (!file.exists()) {
                Log.d("runningLog", "Create the file:" + fileName);
                file.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(file, true);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
            String s = "[" + dateFormat.format(new Date()) + "][thread:" + Thread.currentThread().getId() + "][" +
                    msg + "]\r\n";
            byte[] buf = s.getBytes();
            stream.write(buf);
            stream.close();

        } catch (Exception e) {
            Log.e("TestFile", "Error on writeFilToSD.");
            e.printStackTrace();
        }
    }


    /**
     * @description 显示当前行的相关信息，如：当前文件、当前函数、当前行号
     */
    public static void l() {
        d(getCurrentLineInfo());
    }

    /**
     * @return 当前行的相关信息
     * @description 获取当前行的相关信息
     */
    public static String getCurrentLineInfo() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return "FileName:[" + ste.getFileName() + "] MethodName: [" + ste.getMethodName() + "]: Line [" + ste
                .getLineNumber() + "]";
    }

    /**
     * @return
     * @description 将ByteBuff转换成16进制
     */
    public static String printHex(String s, ByteBuffer buff) {
        SilenceLog.w(s + ":  " + byteToHex(buff.array()));
        return byteToHex(buff.array());
    }

    public static String byteToHex(byte[] buff) {
        if (null != buff) {
            String hexString = "";
            for (int i = 0; i < buff.length; i++) {
                String hex = Integer.toHexString(buff[i] & 0xFF);
                if (hex.length() == 1) {
                    hexString += '0' + hex;
                } else {
                    hexString += hex;
                }
            }
            String regex = "(.{8})";
            hexString = hexString.replaceAll(regex, "$1 ");
            return hexString.toUpperCase();
        }
        return "";
    }
}
