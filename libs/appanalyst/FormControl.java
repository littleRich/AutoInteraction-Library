package silence.com.cn.a310application.appanalyst;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FormControl {
    private final String mClassName;
    private final TextMatcherFactory mTextMatcher;
    private final DescriptionMatcherFactory mDescriptionMatcher;
    private final ArrayList<FormControl> mChilds = new ArrayList<>();
    private boolean mEssential = true;
    private final Set<String> mIncludeTexts = new HashSet<>();
    private final Set<String> mExcludeTexts = new HashSet<>();

    public FormControl(@Nullable String className) {
        this(className, null, null);
    }

    public FormControl(@Nullable String className, @Nullable TextMatcherFactory textMatcher) {
        this(className, textMatcher, null);
    }

    public FormControl(@Nullable String className, @Nullable DescriptionMatcherFactory descriptionMatcher) {
        this(className, null, descriptionMatcher);
    }

    public FormControl(@Nullable String className, @Nullable TextMatcherFactory textMatcher, @Nullable DescriptionMatcherFactory descriptionMatcher) {
        mClassName = className;
        mTextMatcher = textMatcher;
        mDescriptionMatcher = descriptionMatcher;
    }

    public FormControl set(int index, @NonNull FormControl child) {
        set(index, true, child);
        return this;
    }

    public FormControl set(int index, boolean essential, @NonNull FormControl child) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("index=" + index);
        }
        if (index > 20) {
            throw new IndexOutOfBoundsException("index>20; index=" + index);
        }
        if (null == child) {
            throw new NullPointerException("child");
        }
        if (index < mChilds.size()) {
            throw new ArrayStoreException();
        }
        child.mEssential = essential;
        for (int i = mChilds.size(); i < index + 1; i++) {
            mChilds.add(null);
        }
        mChilds.set(index, child);
        return this;
    }

    public FormControl includeText(@NonNull String text) {
        if (null == text) {
            throw new NullPointerException("text");
        }
        if (Macro.Debug) {
            if (mIncludeTexts.contains(text)) {
                throw new ArrayStoreException("代码错误");
            }
            if (mExcludeTexts.contains(text)) {
                throw new ArrayStoreException("代码错误");
            }
        }
        mIncludeTexts.add(text);
        return this;
    }

    public FormControl excludeText(@NonNull String text) {
        if (null == text) {
            throw new NullPointerException("text");
        }
        if (Macro.Debug) {
            if (mIncludeTexts.contains(text)) {
                throw new ArrayStoreException("代码错误");
            }
            if (mExcludeTexts.contains(text)) {
                throw new ArrayStoreException("代码错误");
            }
        }
        mExcludeTexts.add(text);
        return this;
    }

    public void check(@NonNull UiNode uiNode) throws CheckFormResult {
        if (null == uiNode) {
            throw new NullPointerException("uiNode");
        }
        uiNode.setFormIndex(0);
        check(new int[]{0}, uiNode);
    }

    private void check(@NonNull int[] path, @NonNull UiNode uiNode) throws CheckFormResult {
        if (null == path) {
            throw new NullPointerException("path");
        }
        if (null == uiNode) {
            throw new NullPointerException("uiNode");
        }
        if (null == mClassName) {
            throw new CheckFormResult(path, "当前不应该存在控件");
        }
        if (!mClassName.equals(uiNode.getClassName())) {
            throw new CheckFormResult(path, "类型不匹配");
        }
        if (null != mTextMatcher) {
            if (!mTextMatcher.onMatch(uiNode.getText())) {
                throw new CheckFormResult(path, "文本不匹配");
            }
        }
        if (null != mDescriptionMatcher) {
            if (!mDescriptionMatcher.onMatch(uiNode.getDescription())) {
                throw new CheckFormResult(path, "描述不匹配");
            }
        }
        for (String text : mIncludeTexts) {
            if (uiNode.findChildsByText(text).isEmpty()) {
                throw new CheckFormResult(path, "没有找到必须的文本");
            }
        }
        for (String text : mExcludeTexts) {
            if (!uiNode.findChildsByText(text).isEmpty()) {
                throw new CheckFormResult(path, "含有需要排除的文本");
            }
        }
        int realIndex = 0;
        for (int i = 0; i < mChilds.size(); i++) {
            FormControl childControl = mChilds.get(i);
            if (null == childControl) {
                realIndex++;
            } else {
                int[] childPath = new int[path.length + 1];
                System.arraycopy(path, 0, childPath, 0, path.length);
                childPath[path.length] = i;
                if (realIndex >= uiNode.getChildCount()) {
                    if (null != childControl.mClassName && childControl.mEssential) {
                        throw new CheckFormResult(childPath, "未找到结点");
                    }
                } else {
                    UiNode childUiNode = uiNode.getChild(realIndex);
                    try {
                        childControl.check(childPath, childUiNode);
                        childUiNode.setFormIndex(i);
                        realIndex++;
                    } catch (CheckFormResult e) {
                        if (childControl.mEssential) {
                            throw e;
                        }
                    }
                }
            }
        }
    }
}
