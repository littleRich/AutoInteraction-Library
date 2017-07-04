package silence.com.cn.a310application.appanalyst;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    private static String mAppName;
    private static FileOutputStream mOuputStream;

    private interface LogOutput {
        void output(String tag, String message);
    }

    public static void init(String appName) {
        if (null == appName) {
            throw new NullPointerException("appName");
        }
        mAppName = appName;
    }

    public static void open() {
        if (null == mAppName) {
            throw new NullPointerException("mAppName");
        }
        if (null != mOuputStream) {
            try {
                mOuputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mOuputStream = null;
        }
        File file = null;
        for (int i = 0; i < 5; i++) {
            file = new File(Environment.getExternalStorageDirectory(), mAppName + "/logs/" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".txt");
            if (!file.exists()) {
                break;
            }
        }
        if (null != file && !file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                mOuputStream = new FileOutputStream(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void close() {
        if (null != mOuputStream) {
            try {
                mOuputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mOuputStream = null;
        }
    }

    public static void d(@NonNull String message) {
        d(message, null, null);
    }

    public static void i(@NonNull String message) {
        i(message, null, null);
    }

    public static void w(@NonNull String message) {
        w(message, null, null);
    }

    public static void e(@NonNull String message) {
        e(message, null, null);
    }

    public static void e(@NonNull Throwable throwable) {
        e(throwable, null, null);
    }

    public static void d(@NonNull String message, Context context, @Nullable ToastDuration toastDuration) {
        outputLog(new LogOutput() {
            @Override
            public void output(String tag, String message) {
                android.util.Log.d(tag, message);
            }
        }, message);
        outputFileLog("调试", message);
        if (null != toastDuration) {
            showToast(message, context, toastDuration.value());
        }
    }

    public static void i(@NonNull String message, Context context, @Nullable ToastDuration toastDuration) {
        outputLog(new LogOutput() {
            @Override
            public void output(String tag, String message) {
                android.util.Log.i(tag, message);
            }
        }, message);
        outputFileLog("信息", message);
        if (null != toastDuration) {
            showToast(message, context, toastDuration.value());
        }
    }

    public static void w(@NonNull String message, Context context, @Nullable ToastDuration toastDuration) {
        outputLog(new LogOutput() {
            @Override
            public void output(String tag, String message) {
                android.util.Log.w(tag, message);
            }
        }, message);
        outputFileLog("警告", message);
        if (null != toastDuration) {
            showToast(message, context, toastDuration.value());
        }
    }

    public static void e(@NonNull String message, Context context, @Nullable ToastDuration toastDuration) {
        outputLog(new LogOutput() {
            @Override
            public void output(String tag, String message) {
                android.util.Log.e(tag, message);
            }
        }, message);
        outputFileLog("错误", message);
        if (null != toastDuration) {
            showToast(message, context, toastDuration.value());
        }
    }

    public static void e(@NonNull Throwable throwable, Context context, @Nullable ToastDuration toastDuration) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        String text = writer.toString();
        outputLog(new LogOutput() {
            @Override
            public void output(String tag, String message) {
                android.util.Log.e(tag, message);
            }
        }, text);
        outputFileLog("错误", text);
        if (null != toastDuration) {
            String message = throwable.getMessage();
            if (null != message) {
                showToast(message, context, toastDuration.value());
            }
        }
    }

    private static void outputLog(@NonNull LogOutput logOutput, @NonNull String message) {
        if (null == logOutput) {
            throw new NullPointerException("logOutput");
        }
        if (null == message) {
            throw new NullPointerException("message");
        }
        if (null == mAppName) {
            throw new NullPointerException("mAppName");
        }
        String texts[] = message.split("\\n");
        String log = "";
        int n = 0;
        for (String text : texts) {
            if ("".equals(log)) {
                log = text;
            } else {
                if (log.length() + text.length() > 3800) {
                    logOutput.output(mAppName, log);
                    n++;
                    log = "第" + (n + 1) + "部分: \n" + text;
                } else {
                    if (!"".equals(log)) {
                        log += "\n";
                    }
                    log += text;
                }
            }
        }
        logOutput.output(mAppName, log);
    }

    private static void outputFileLog(@NonNull String type, @NonNull String message) {
        if (null == type) {
            throw new NullPointerException("type");
        }
        if (null == message) {
            throw new NullPointerException("message");
        }
        if (null != mOuputStream) {
            synchronized (mOuputStream) {
                String text = new SimpleDateFormat("yyyyMMdd,HHmmss,SSS").format(new Date()) + "|" + type + ": " + message + "\n";
                try {
                    mOuputStream.write(text.getBytes());
                    mOuputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void showToast(@NonNull final String message, final Context context, final int duration) {
        if (null == message) {
            throw new NullPointerException("message");
        }
        if (null == mAppName) {
            throw new NullPointerException("mAppName");
        }
        if (Looper.myLooper() == context.getMainLooper()) {
            Toast.makeText(context, mAppName + ": " + message, duration).show();
        } else {
            new Handler(context.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, mAppName + ": " + message, duration).show();
                }
            });
        }
    }
}
