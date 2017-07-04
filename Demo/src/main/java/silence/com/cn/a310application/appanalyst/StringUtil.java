package silence.com.cn.a310application.appanalyst;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class StringUtil {
    @NonNull
    public static String toEmptiable(@Nullable CharSequence text) {
        if (null == text) {
            return "";
        }
        return toEmptiable(text.toString());
    }

    @NonNull
    public static String toEb(@Nullable CharSequence text) {
        return toEmptiable(text);
    }

    @NonNull
    public static String toEmptiable(@Nullable String text) {
        if (null == text) {
            return "";
        }
        return text;
    }

    @NonNull
    public static String toEb(@Nullable String text) {
        return toEmptiable(text);
    }

    @Nullable
    public static String toNullable(@Nullable CharSequence text) {
        if (null == text) {
            return null;
        }
        return text.toString();
    }

    @Nullable
    public static String toNb(@Nullable CharSequence text) {
        return toNullable(text);
    }

    public static boolean compareEmptiable(@Nullable CharSequence left, @Nullable CharSequence right) {
        return compareEmptiable(null == left ? null : left.toString(), null == right ? null : right.toString());
    }

    public static boolean compareEb(@Nullable CharSequence left, @Nullable CharSequence right) {
        return compareEmptiable(left, right);
    }

    public static boolean compareEmptiable(@Nullable String left, @Nullable CharSequence right) {
        return compareEmptiable(left, null == right ? null : right.toString());
    }

    public static boolean compareEb(@Nullable String left, @Nullable CharSequence right) {
        return compareEmptiable(left, right);
    }

    public static boolean compareEmptiable(@Nullable CharSequence left, @Nullable String right) {
        return compareEmptiable(null == left ? null : left.toString(), right);
    }

    public static boolean compareEb(@Nullable CharSequence left, @Nullable String right) {
        return compareEmptiable(left, right);
    }

    public static boolean compareEmptiable(@Nullable String left, @Nullable String right) {
        return toEmptiable(left).equals(toEmptiable(right));
    }

    public static boolean compareEb(@Nullable String left, @Nullable String right) {
        return compareEmptiable(left, right);
    }

    public static boolean compareNullable(@Nullable CharSequence left, @Nullable CharSequence right) {
        return compareNullable(null == left ? null : left.toString(), null == right ? null : right.toString());
    }

    public static boolean compareNb(@Nullable CharSequence left, @Nullable CharSequence right) {
        return compareNullable(left, right);
    }

    public static boolean compareNullable(@Nullable String left, @Nullable CharSequence right) {
        return compareNullable(left, null == right ? null : right.toString());
    }

    public static boolean compareNb(@Nullable String left, @Nullable CharSequence right) {
        return compareNullable(left, right);
    }

    public static boolean compareNullable(@Nullable CharSequence left, @Nullable String right) {
        return compareNullable(null == left ? null : left.toString(), right);
    }

    public static boolean compareNullable(@Nullable String left, @Nullable String right) {
        if (null == left) {
            return null == right;
        }
        if (null == right) {
            return null == left;
        }
        return left.equals(right);
    }

    public static boolean compareNb(@Nullable String left, @Nullable String right) {
        return compareNullable(left, right);
    }
}
