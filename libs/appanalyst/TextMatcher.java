package silence.com.cn.a310application.appanalyst;

import android.support.annotation.Nullable;

public class TextMatcher extends TextMatcherFactory {
    public final String mText;

    public TextMatcher(@Nullable String text) {
        mText = text;
    }

    @Override
    public boolean onMatch(@Nullable String text) {
        return StringUtil.compareNb(mText, text);
    }
}