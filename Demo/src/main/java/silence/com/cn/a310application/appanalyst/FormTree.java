package silence.com.cn.a310application.appanalyst;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class FormTree {
    private final String mFormPage;
    private final String mClassName;
    private final FormControl mControl;
    private final Map<String, int[]> mControlPaths = new HashMap<>();

    public FormTree(@NonNull String formPage, @NonNull String className, @NonNull FormControl control) {
        if (null == formPage) {
            throw new NullPointerException("formPage");
        }
        if (null == className) {
            throw new NullPointerException("className");
        }
        if (null == control) {
            throw new NullPointerException("control");
        }
        mFormPage = formPage;
        mClassName = className;
        mControl = control;
    }

    @NonNull
    public String getClassName() {
        return mClassName;
    }

    @NonNull
    public String getFormPage() {
        return mFormPage;
    }

    @NonNull
    public FormControl getControl() {
        return mControl;
    }

    public void addControlPath(@NonNull String pathName, @NonNull int[] path) {
        if (null == pathName) {
            throw new NullPointerException("pathName");
        }
        if (null == path) {
            throw new NullPointerException("path");
        }
        mControlPaths.put(pathName, path);
    }

    @Nullable
    public int[] getControlPath(@NonNull String pathName) {
        if (null == pathName) {
            throw new NullPointerException("pathName");
        }
        return mControlPaths.get(pathName);
    }
}
