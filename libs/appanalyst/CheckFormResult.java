package silence.com.cn.a310application.appanalyst;

public class CheckFormResult extends Exception {
    private int[] mPath;

    public CheckFormResult(int[] path, String message) {
        super(message);
        mPath = path;
    }

    public int[] getPath() {
        return mPath;
    }
}
