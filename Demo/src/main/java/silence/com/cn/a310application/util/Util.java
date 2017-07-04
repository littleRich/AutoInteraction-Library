package silence.com.cn.a310application.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONStringer;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Util {
    private static Context mContext;
    private static Toast mToast = null;

    // 限制按钮快速点击
    private static long lastClickTime = 0;
    private static long DIFF = 500;
    private static int lastButtonId = -1;

    public static Toast showToast(Context context, String hint) {

        if (mContext == context) {
            mToast.setText(hint);
        } else {
            mContext = context;
            mToast = Toast.makeText(context, hint, Toast.LENGTH_SHORT);
        }
        mToast.show();
        return mToast;
    }

    public static Toast showToast(Context context, int i) {

        if (mContext == context) {
            mToast.setText(i);
        } else {
            mContext = context;
            mToast = Toast.makeText(context, i, Toast.LENGTH_SHORT);
        }
        mToast.show();
        return mToast;
    }

    /**
     * @return true: debug false:release
     * @description 判断当前程序是否出于DEBUG模式
     */
    public static boolean isDebug(Context context) {
        // 如果是开发使用则直接开启日志，否则根据当前状态判断
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

    /**
     * @return null may be returned if the specified process not found
     */
    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    /**
     * @return true: 存在可用的网络 flase: 无可用的网络
     * @description 判断当前是否有可用的网络
     */
    public static boolean isNetWorkUseful(Context context) {
        boolean result = false;
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context
                    .CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        result = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @return null :不存在可用的网络 !null:存在可用的网络，并将可用网络返给用户使用
     * @description 获取当前可用的网络
     */
    public static NetworkInfo getActiveNetworkInfo(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo();
    }

    /***
     * @return true:存在 false:不存在
     * @description 判断是否存在SDCard
     */
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * @return true:最上层 false:！最上层
     * @description 判断APP是否运行于最上层
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName())) {
            return true;
        }
        return false;
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
        }
        return versionName;
    }

    public static byte[] getAppVersionCode(Context context) {
        byte[] versionCode = new byte[4];
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            String a[] = versionName.split("\\.");
            versionCode[0] = (byte) Integer.parseInt(a[0]);
            versionCode[1] = (byte) Integer.parseInt(a[1]);
            versionCode[2] = (byte) Integer.parseInt(a[2]);
            versionCode[3] = (byte) Integer.parseInt(a[3]);
        } catch (Exception e) {
        }
        return versionCode;
    }

    /**
     * java字节码转字符串
     *
     * @param b
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String byte2hex(byte[] b) { // 一个字节的数，

        // 转成16进制字符串
        String hs = "";
        String tmp = "";
        for (int n = 0; n < b.length; n++) {
            // 整数转成十六进制表示
            tmp = (Integer.toHexString(b[n] & 0XFF));
            if (tmp.length() == 1) {
                hs = hs + "0" + tmp;
            } else {
                hs = hs + tmp;
            }
        }
        tmp = null;
        return hs.toUpperCase(); // 转成大写
    }

    /**
     * 字符串转java字节码
     *
     * @param b
     * @return
     */
    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节

            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        b = null;
        return b2;
    }

    /**
     * dp转成px
     *
     * @param dipValue
     * @return
     */
    //    public static int dip2px(float dipValue) {
    //        return (int) (dipValue * scale + 0.5f);
    //    }

    /**
     * px转成dp
     *
     * @param pxValue
     * @return
     */
    //    public static int px2dip(float pxValue) {
    //        return (int) (pxValue / scale + 0.5f);
    //    }

    /**
     * 获取图片的ID
     */
    public static int getDrawableResId(Context context, String name) {
        ApplicationInfo appInfo = context.getApplicationInfo();
        int resID = context.getResources().getIdentifier(name, "drawable", appInfo.packageName);
        return resID;
    }

    /**
     * 获取布局的ID
     */
    public static int getLayoutResId(Context context, String name) {
        ApplicationInfo appInfo = context.getApplicationInfo();
        int resID = context.getResources().getIdentifier(name, "layout", appInfo.packageName);
        return resID;
    }

    /**
     * 得到有效的字符串数据
     */
    public static String getValidData(byte[] invalid) {

        int index = 0;
        while (index < invalid.length) {
            if (0 == invalid[index]) {
                break;
            }
            index++;
        }

        byte[] valid = new byte[index];
        System.arraycopy(invalid, 0, valid, 0, index);
        return new String(valid);

    }

    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(-1, DIFF);
    }

    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isFastDoubleClick(int buttonId) {
        return isFastDoubleClick(buttonId, DIFF);
    }

    /**
     * 判断两次点击的间隔，如果小于diff，则认为是多次无效点击
     *
     * @param diff
     * @return
     */
    public static boolean isFastDoubleClick(int buttonId, long diff) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastButtonId == buttonId && lastClickTime > 0 && timeD < diff) {
            return true;
        }

        lastClickTime = time;
        lastButtonId = buttonId;
        return false;
    }

    /**
     * @param key
     * @param value
     * @return
     * @throws
     * @Title: createJsonString
     * @Description: 生成json字符串
     * @return： String
     */
    public static String createJsonString(String key, JSONArray value) {
        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object().key(key).value(value).endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonStringer.toString();
    }

    /**
     * ip地址校验
     *
     * @param ipAddress ip地址
     * @return boolean
     */
    public static boolean isIpv4(String ipAddress) {

        String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." + "" +
                "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "" +
                "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "" +
                "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    /**
     * 子网掩码校验
     *
     * @param mask 子网掩码
     * @return boolean
     */
    public static boolean isMask(String mask) {
        Pattern pattern = Pattern.compile("^((128|192)|2(24|4[08]|5[245]))(\\.(0|(128|192)|2((24)|(4[08])|(5[245]))))" +
                "" + "{3}$");
        Matcher matcher = pattern.matcher(mask);
        return matcher.matches();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition  
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
