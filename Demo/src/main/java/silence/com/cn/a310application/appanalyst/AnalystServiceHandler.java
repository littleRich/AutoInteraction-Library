package silence.com.cn.a310application.appanalyst;

import android.content.Context;

public interface AnalystServiceHandler {
    Context getContext();

    GpsMocker getGpsMocker();

    void requirePost();

    void requireBack();

    void requireMockClick(PointXY point);

    void requireMockLongClick(PointXY point);

    void requireMockMove(PointXY2XY pointXY2XY);
}
