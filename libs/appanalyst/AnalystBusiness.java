package silence.com.cn.a310application.appanalyst;

import android.support.annotation.NonNull;

public abstract class AnalystBusiness {
    private AnalystServiceHandler mServiceHandler;

    protected AnalystBusiness(@NonNull AnalystServiceHandler serviceHandler) {
        if (null == serviceHandler) {
            throw new NullPointerException("serviceHandler");
        }
        mServiceHandler = serviceHandler;
    }

    protected AnalystServiceHandler getServiceHandler() {
        return mServiceHandler;
    }

    public abstract void handleUiEvent(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException;
}
