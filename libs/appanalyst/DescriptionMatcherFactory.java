package silence.com.cn.a310application.appanalyst;

import android.support.annotation.Nullable;

public abstract class DescriptionMatcherFactory {
    public abstract boolean onMatch(@Nullable String description);
}