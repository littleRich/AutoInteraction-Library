package silence.com.cn.a310application.qqanalyst;

import android.content.Context;
import android.support.annotation.NonNull;

import silence.com.cn.a310application.R;
import silence.com.cn.a310application.appanalyst.AnalystApplication;
import silence.com.cn.a310application.appanalyst.AnalystService;
import silence.com.cn.a310application.appanalyst.MyException;

public class QQApplication extends AnalystApplication {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        FormPage.values();
        try {
            AnalystService.init(QQSimulateBusiness.class);
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    public static Context getContext() {
        return mContext;
    }

    @NonNull
    @Override
    public String getAppName() {
        return getText(R.string.app_name).toString();
    }
}
