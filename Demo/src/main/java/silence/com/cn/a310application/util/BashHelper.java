package silence.com.cn.a310application.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;

/**
 * 执行Linux命令的工具类
 *
 * Created by xuqingfu on 2016/10/28.
 */
public class BashHelper {
    private static final String TAG = "BashHelper";
    private static final String CAPTURE_TOOL = "tcpdump";
    /**抓包数据包数据存放文件*/
    public static final String DEST_FILE = Environment.getExternalStorageDirectory() + File.separator + "capture.txt";

    /**
     * 开始拦截网络数据包
     * @param context
     * @return 返回是否拦截出现异常,true: 出现异常，拦截中断, false: 拦截正常
     */
    public static boolean startCapture(Context context) {
        InputStream is = null;
        OutputStream os = null;
        boolean retVal = false;
        try {
            AssetManager am = context.getAssets();
            is = am.open(CAPTURE_TOOL);
            File sdcardFile = Environment.getExternalStorageDirectory();
            File dstFile = new File(sdcardFile, CAPTURE_TOOL);

            os = new FileOutputStream(dstFile);
            copyStream(is, os);

            String[] commands = new String[7];
            commands[0] = "adb shell";
            //tcpdump -vv -s 0 -w /sdcard/ca.txt -vnn host www.baidu.com
            //commands[2] = "tcpdump -p -vv -s 0 -w /sdcard/capture.txt";
            commands[2] = "tcpdump -p -v -s 0 -w /sdcard/demo.txt -vnn host jubao.qq.com";
         /*   commands[1] = "su";
            commands[2] = "cp -rf " + dstFile.toString() + " /system/xbin/tcpdump";
            commands[3] = "rm -r " + dstFile.toString();
            commands[4] = "chmod 777 /system/xbin/tcpdump";
            commands[5] = "cd /system/xbin";
            commands[6] = "tcpdump -i any -p -s 0 -w " + DEST_FILE;*/
            //过滤IP183.3.226.32

            Process proc = execCmd(commands);
          /*  //使用wairFor()可以等待命令执行完成以后才返回
            if (proc.waitFor() != 0) {
                    System.err.println("exit value = " + proc.exitValue());
            }*/

        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "抓包出现问题：" + e.getMessage());
            retVal = true;
        } finally {
            closeSafely(is);
            closeSafely(os);
        }

        return retVal;
    }

    /**
     * 停止拦截本地网络数据包
     * 主要操作：找出所有的带有tcpdump的进程，并结束该进程
     * @param context
     */
    public static void stopCapture(Context context) {
        String[] commands = new String[2];
        commands[0] = "adb shell";
        commands[1] = "ps|grep tcpdump|grep root|awk '{print $2}'";
        Process process = execCmd(commands);
        String result = parseInputStream(process.getInputStream());
        if (!TextUtils.isEmpty(result)) {
            String[] pids = result.split("\n");
            if (null != pids) {
                String[] killCmds = new String[pids.length];
                for (int i = 0; i < pids.length; ++i) {
                    killCmds[i] = "kill -9 " + pids[i];
                }
                execCmd(killCmds);
                Log.d(TAG, "停止抓包");
            }
        }
    }

    public static Process execCmd(String command) {
        return execCmd(new String[] { command }, true);
    }

    public static Process execCmd(String[] commands) {
        return execCmd(commands, true);
    }

    public static Process execCmd(String[] commands, boolean waitFor) {
        Process suProcess = null;
        try {
            suProcess = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            for (String cmd : commands) {
                if (!TextUtils.isEmpty(cmd)) {
                    os.writeBytes(cmd + "\n");
                }
            }
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        showShell(suProcess);

        if (waitFor) {
            boolean retval = false;
            try {
                int suProcessRetval = suProcess.waitFor();

                if (255 != suProcessRetval) {
                    retval = true;
                } else {
                    retval = false;
                    Log.e(TAG, "exit value = " + suProcess.exitValue());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return suProcess;
    }

    /**
     * 显示Linux命令执行后返回值
     * @param suProcess
     */
    private static void showShell(Process suProcess) {
        try {
            InputStream inputstream = suProcess.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
            // read the ls output
            String line = "";
            StringBuilder sb = new StringBuilder(line);
            while ((line = bufferedreader.readLine()) != null) {
                //System.out.println(line);
                sb.append(line);
                sb.append('\n');
            }
            Log.d(TAG, "shell:" + sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件拷贝
     * @param is
     * @param os
     */
    private static void copyStream(InputStream is, OutputStream os) {
        final int BUFFER_SIZE = 1024;
        try {
            byte[] bytes = new byte[BUFFER_SIZE];
            for (;;) {
                int count = is.read(bytes, 0, BUFFER_SIZE);
                if (count == -1) {
                    break;
                }

                os.write(bytes, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放IO流资源
     * @param is
     */
    private static void closeSafely(Closeable is) {
        try {
            if (null != is) {
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取IO输入流中的数据
     * @param is
     * @return 流数据字符串
     */
    private static String parseInputStream(InputStream is) {
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ( (line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}

