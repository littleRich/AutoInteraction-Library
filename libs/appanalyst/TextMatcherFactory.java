package silence.com.cn.a310application.appanalyst;

import android.support.annotation.Nullable;

public abstract class TextMatcherFactory {
    public abstract boolean onMatch(@Nullable String text);
}