package silence.com.cn.a310application.appanalyst;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

public class UiTree {
    public static final List<FormTree> PRESET_FORM = new ArrayList<>();

    private final String mClassName;
    private final UiNode mNode;
    private final FormTree mFoundForm;
    private final String mLog;

    public UiTree(@NonNull String className, @Nullable AccessibilityNodeInfo accessibilityNodeInfo) throws UiNode.NullChildAccessibilityNodeInfoException, MyException {
        if (null == className) {
            throw new NullPointerException("className");
        }
        mClassName = className;
        mNode = (null == accessibilityNodeInfo ? null : new UiNode(getRootAccessibilityNodeInfo(accessibilityNodeInfo)));
        mFoundForm = findForm();
        mLog = obtainLog();
    }

    public static AccessibilityNodeInfo getRootAccessibilityNodeInfo(@NonNull AccessibilityNodeInfo accessibilityNodeInfo) throws MyException {
        if (null == accessibilityNodeInfo) {
            throw new NullPointerException("accessibilityNodeInfo");
        }
        AccessibilityNodeInfo current = null;
        try {
            current = accessibilityNodeInfo;
            while (true) {
                AccessibilityNodeInfo parent = current.getParent();
                if (null == parent) {
                    return current;
                }
                current = parent;
            }
        } catch (Exception e) {
            if (null != current) {
                current.recycle();
            }
            throw new MyException(e);
        }
    }

    public void recycle() {
        if (null != mNode) {
            mNode.recycle();
        }
    }

    @NonNull
    public String getClassName() {
        return mClassName;
    }

    @NonNull
    public UiNode getNode() {
        return mNode;
    }

    @Nullable
    public FormTree getFoundForm() {
        return mFoundForm;
    }

    @NonNull
    public String getFoundFormPage() {
        return null == mFoundForm ? "" : mFoundForm.getFormPage();
    }

    @NonNull
    public UiNode getNode(@NonNull String pathName) throws MyException {
        if (null == pathName) {
            throw new NullPointerException("pathName");
        }
        if (null == mNode) {
            throw new MyException("没有结点");
        }
        if (null == mFoundForm) {
            throw new MyException("没有匹配的Form");
        }
        int[] path = mFoundForm.getControlPath(pathName);
        if (null == path) {
            throw new MyException("没有匹配的路径名");
        }
        UiNode node;
        try {
            node = mNode.getNode(path);
        } catch (MyException e) {
            throw new MyException("获取结点异常", e);
        }
        if (null == node) {
            throw new MyException("没有找到结点");
        }
        return node;
    }

    @NonNull
    public FormTree findForm() {
        for (FormTree formTree : PRESET_FORM) {
            if (mClassName.equals(formTree.getClassName())) {
                if (null != mNode) {
                    try {
                        formTree.getControl().check(mNode);
                        return formTree;
                    } catch (CheckFormResult e) {
                        int path[] = e.getPath();
                        String text = "检查 # " + formTree.getFormPage() + "\n[";
                        for (int i = 0; i < path.length; i++) {
                            if (0 != i) {
                                text += ",";
                            }
                            text += i;
                        }
                        text += "]\n[";
                        for (int i = 0; i < path.length; i++) {
                            if (0 != i) {
                                text += ",";
                            }
                            text += path[i];
                        }
                        text += "]\n: " + e.getMessage();
                        Log.e(text);
                    }
                }
            }
        }
        return null;
    }

    @NonNull
    public String getLog() {
        return mLog;
    }

    @NonNull
    public String obtainLog() {
        String log = "控件树：ClassName=" + mClassName + "\n";
        if (null == mNode) {
            return log + "空";
        } else {
            return log + obtainChildLog(0, 0, mNode);
        }
    }

    private String obtainChildLog(int level, int index, UiNode uiNode) {
        if (null == uiNode) {
            return "";
        }
        String log = "" + level + "[" + index + "]";
        for (int i = 0; i <= level; i++) {
            log += "---";
        }
        log += uiNode.getClassName() + "|" + uiNode.getText() + "|" + uiNode.getDescription();
        for (int i = 0; i < uiNode.getChildCount(); i++) {
            String childLog = obtainChildLog(level + 1, i, uiNode.getChild(i));
            if (!"".equals(childLog)) {
                log += "\n" + childLog;
            }
        }
        return log;
    }
}
