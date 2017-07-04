package silence.com.cn.a310application.appanalyst;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UiNode {
    private AccessibilityNodeInfo mAccessibilityNodeInfo;
    private String mClassName;
    private String mText;
    private String mDescription;
    private final ArrayList<UiNode> mChilds = new ArrayList<>();
    private int mFormIndex = -1;

    public static class NullChildAccessibilityNodeInfoException extends MyException {
        public NullChildAccessibilityNodeInfoException() {
            super();
        }

        public NullChildAccessibilityNodeInfoException(String detailMessage) {
            super(detailMessage);
        }

        public NullChildAccessibilityNodeInfoException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public NullChildAccessibilityNodeInfoException(Throwable throwable) {
            super(throwable);
        }
    }

    public UiNode(@NonNull AccessibilityNodeInfo accessibilityNodeInfo) throws NullChildAccessibilityNodeInfoException, MyException {
        if (null == accessibilityNodeInfo) {
            throw new NullPointerException("accessibilityNodeInfo");
        }
        try {
            mAccessibilityNodeInfo = accessibilityNodeInfo;
            mClassName = StringUtil.toEb(accessibilityNodeInfo.getClassName());
            mText = StringUtil.toNb(accessibilityNodeInfo.getText());
            mDescription = StringUtil.toNb(accessibilityNodeInfo.getContentDescription());
            int count = accessibilityNodeInfo.getChildCount();
            for (int i = 0; i < count; i++) {
                AccessibilityNodeInfo childAccessibilityNodeInfo = accessibilityNodeInfo.getChild(i);
                if (null == childAccessibilityNodeInfo) {
                    throw new NullChildAccessibilityNodeInfoException("出现空的子AccessibilityNodeInfo");
                }
                mChilds.add(new UiNode(childAccessibilityNodeInfo));
            }
        } catch (NullChildAccessibilityNodeInfoException e) {
            throw e;
        } catch (MyException e) {
            throw e;
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    public void recycle() {
        for (UiNode childNode : mChilds) {
            childNode.recycle();
        }
        if (null != mAccessibilityNodeInfo) {
            mAccessibilityNodeInfo.recycle();
        }
    }

    @NonNull
    public String getClassName() {
        return mClassName;
    }

    @Nullable
    public String getText() {
        return mText;
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }

    @NonNull
    public Iterator<UiNode> childIterator() {
        return mChilds.iterator();
    }

    public int getChildCount() {
        return mChilds.size();
    }

    @Nullable
    public UiNode getChild(int index) {
        try {
            return mChilds.get(index);
        } catch (Exception e) {
            return null;
        }
    }

    public void setFormIndex(int index) {
        mFormIndex = index;
    }

    public int getFormIndex() {
        return mFormIndex;
    }

    @Nullable
    public List<UiNode> findChildsByText(@NonNull String text) {
        if (null == text) {
            throw new NullPointerException("text");
        }
        List<UiNode> childs = new ArrayList<>();
        findChildsByText(childs, this, text);
        return childs;
    }

    private static void findChildsByText(@NonNull List<UiNode> childs, @NonNull UiNode uiNode, @NonNull String text) {
        if (null == childs) {
            throw new NullPointerException("childs");
        }
        if (null == uiNode) {
            throw new NullPointerException("uiNode");
        }
        if (null == text) {
            throw new NullPointerException("text");
        }
        if (uiNode.getText().equals(text)) {
            childs.add(uiNode);
        }
        for (UiNode child : uiNode.mChilds) {
            child.findChildsByText(childs, child, text);
        }
    }

    @Nullable
    public UiNode getNode(@NonNull int[] path) throws MyException {
        return getNode(path, 0);
    }

    @Nullable
    private UiNode getNode(@NonNull int[] path, int level) throws MyException {
        if (null == path) {
            throw new NullPointerException("path");
        }
        if (path.length <= level) {
            throw new MyException("路径表示异常");
        }
        if (0 == level) {
            if (0 != path[0]) {
                throw new MyException("路径表示错误");
            }
        }
        if (path.length == level + 1) {
            return this;
        }
        int index = path[level + 1];
        for (UiNode child : mChilds) {
            if (child.getFormIndex() == index) {
                return child.getNode(path, level + 1);
            }
        }
        return null;
    }

    public void click() throws MyException {
        try {
            mAccessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    public void longClick() throws MyException {
        try {
            mAccessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    public void setEditText(@NonNull Context context, @NonNull String text) throws MyException {
        if (null == context) {
            throw new NullPointerException("context");
        }
        if (null == text) {
            throw new NullPointerException("text");
        }
        try {
            //清空内容
            Bundle arguments = new Bundle();
            arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT, AccessibilityNodeInfo
                    .MOVEMENT_GRANULARITY_LINE);
            arguments.putBoolean(AccessibilityNodeInfo.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN, true);
            mAccessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY,
                    arguments);
            //粘贴内容
            ClipData clipData = ClipData.newPlainText(context.getPackageName(), text);
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(clipData);
            mAccessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    public void scrollForward() throws MyException {
        try {
            mAccessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    public void getBoundsInScreen(Rect bounds) throws MyException {
        try {
            mAccessibilityNodeInfo.getBoundsInScreen(bounds);
        } catch (Exception e) {
            throw new MyException(e);
        }
    }
}
