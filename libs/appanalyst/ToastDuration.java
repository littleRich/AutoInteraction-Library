package silence.com.cn.a310application.appanalyst;

import android.widget.Toast;

public enum ToastDuration {
    LENGTH_SHORT(Toast.LENGTH_SHORT),
    LENGTH_LONG(Toast.LENGTH_LONG);

    private int mValue;

    ToastDuration(int value) {
        mValue = value;
    }

    public int value() {
        return mValue;
    }
}