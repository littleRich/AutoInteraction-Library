package silence.com.cn.a310application.appanalyst;

import android.app.Application;
import android.support.annotation.NonNull;

public abstract class AnalystApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.init(getAppName());
        Log.open();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.close();
    }

    @NonNull
    public abstract String getAppName();
}
