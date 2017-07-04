package silence.com.cn.a310application.appanalyst;

import android.support.annotation.Nullable;

public class DescriptionMatcher extends DescriptionMatcherFactory {
    public final String mDescription;

    public DescriptionMatcher(@Nullable String description) {
        if (null == description) {
            throw new NullPointerException("description");
        }
        mDescription = description;
    }

    @Override
    public boolean onMatch(@Nullable String description) {
        return StringUtil.compareNb(mDescription, description);
    }
}