package silence.com.cn.a310application.appanalyst;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.accessibility.AccessibilityEvent;

import java.lang.reflect.Constructor;

public class AnalystService extends AccessibilityService {
    public static final int ID_POST_EVENT = 1;
    public static final int ID_POST_KEEP_ALIVE = 2;

    public static final int DELAY_POST_EVENT = 1000;
    public static final int DELAY_KEEP_ALIVE = 15* 1000;

    private static Constructor<? extends AnalystBusiness> mBusinessClazzConstructor;
    private static MyProcess mSuProcess;

    private final Handler mMainHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ((Runnable) msg.obj).run();
            return true;
        }
    });
    private final GpsMocker mGpsMocker = new GpsMocker();

    private AnalystBusiness mBusiness;
    private String mLastEventClassName;
    private int mLastPostingCount;
    private int mNullChildAccessibilityNodeInfoExceptionCount;
    private int mUnknownUiCount;
    private int mKeepAliveCount;

    private boolean mRunning = false;

    public class ServiceHandler implements AnalystServiceHandler {
        @Override
        public Context getContext() {
            return AnalystService.this;
        }

        @Override
        public GpsMocker getGpsMocker() {
            return mGpsMocker;
        }

        @Override
        public void requirePost() {
            if (!mRunning) {
                return;
            }
            postEvent();
        }

        @Override
        public void requireBack() {
            if (!mRunning) {
                return;
            }
            performGlobalAction(GLOBAL_ACTION_BACK);
        }

        @Override
        public void requireMockClick(PointXY point) {
            if (!mRunning) {
                return;
            }
            if (null == point) {
                return;
            }
            try {
                mSuProcess.exec("input tap " + point.mX + " " + point.mY);
            } catch (MyException e) {
                Log.e(new MyException("执行requireMockClick异常", e), AnalystService.this, ToastDuration.LENGTH_SHORT);
            }
        }

        @Override
        public void requireMockLongClick(PointXY point) {
            if (!mRunning) {
                return;
            }
            if (null == point) {
                return;
            }
            try {
                mSuProcess.exec("input swipe " + point.mX + " " + point.mY + " " + point.mX + " " + point.mY + " 1200");
            } catch (MyException e) {
                Log.e(new MyException("执行requireMockLongClick异常", e), AnalystService.this, ToastDuration.LENGTH_SHORT);
            }
        }

        @Override
        public void requireMockMove(PointXY2XY pointXY2XY) {
            if (!mRunning) {
                return;
            }
            if (null == pointXY2XY) {
                return;
            }
            try {
                mSuProcess.exec("input swipe " + pointXY2XY.mFrom.mX + " " + pointXY2XY.mFrom.mY + " " + pointXY2XY
                        .mTo.mX + " " + pointXY2XY.mTo.mY + " 800");
            } catch (MyException e) {
                Log.e(new MyException("执行requireMockMove异常", e), AnalystService.this, ToastDuration.LENGTH_SHORT);
            }
        }
    }

    public static void init(@NonNull Class<? extends AnalystBusiness> businessClazz) throws MyException {
        try {
            mSuProcess = new MyProcess("su");
        } catch (Exception e) {
            throw new MyException("获取ROOT权限失败", e);
        }
        try {
            mBusinessClazzConstructor = businessClazz.getConstructor(AnalystServiceHandler.class);
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    @Override
    public void onCreate() {
        mGpsMocker.init(this);
        //SilenceLog.e("onCreate");
    }

    @Override
    protected void onServiceConnected() {
        //SilenceLog.e("onServiceConnected");
        Log.i("AnalystService.onServiceConnected");

        try {
            mBusiness = mBusinessClazzConstructor.newInstance(new ServiceHandler());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mLastEventClassName = null;
        mLastPostingCount = 0;
        mNullChildAccessibilityNodeInfoExceptionCount = 0;
        mUnknownUiCount = 0;
        mKeepAliveCount = 0;
        mRunning = true;
       // WatcherUtil.post2watcher(QQApplication.getContext(), "start");
      //  WatcherUtil.startAlarm(QQApplication.getContext(), true);
    }


    @Override
    public void onInterrupt() {
        Log.i("AnalystService.onInterrupt");
        mRunning = false;
        mMainHandler.removeMessages(ID_POST_EVENT);
        mMainHandler.removeMessages(ID_POST_KEEP_ALIVE);
        mBusiness = null;
        //        post2watcher("end");
      //  SilenceLog.e("中断了==============");
    }

    @Override
    public boolean onUnbind(Intent intent) {
      //  SilenceLog.e("onUnbind");
      //  WatcherUtil.post2watcher(QQApplication.getContext(), "end");
       // WatcherUtil.startAlarm(QQApplication.getContext(), false);
        return super.onUnbind(intent);
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (null == accessibilityEvent) {
            return;
        }
        switch (accessibilityEvent.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED: {
                Log.i("AnalystService.onAccessibilityEvent # AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED");
                mLastEventClassName = StringUtil.toNb(accessibilityEvent.getClassName());
                postEvent();
            }
            break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED: {
                Log.i("AnalystService.onAccessibilityEvent # AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED");
                if (null != mLastEventClassName) {
                    postEvent();
                }
            }
            break;
        }
    }

    private void postEvent() {
        postEvent(false);
    }


    private void postEvent(final boolean keepingAlive) {
        if (!mRunning) {
            return;
        }
        if (keepingAlive) {
            mKeepAliveCount++;
            if (mKeepAliveCount > 3) {
       //         WatcherUtil.post2watcher(QQApplication.getContext(), "KillWeChat");
                return;
            }
        } else {
            mKeepAliveCount = 0;
        }
      //  WatcherUtil.post2watcher(QQApplication.getContext(), "postEvent");
        mMainHandler.removeMessages(ID_POST_EVENT);
        Message message = mMainHandler.obtainMessage(ID_POST_EVENT, new Runnable() {
            @Override
            public void run() {
                mLastPostingCount = 0;
                UiTree uiTree = null;
                try {
                    uiTree = new UiTree(mLastEventClassName, getRootInActiveWindow());
                    Log.i(uiTree.getLog());
                    if (null == uiTree.getFoundForm()) {
                        Log.i("UI类型: 未知");
                        mUnknownUiCount++;
                        if (mUnknownUiCount > 15) {
                            mUnknownUiCount = 0;
                 //           WatcherUtil.post2watcher(QQApplication.getContext(), "KillWeChat");
                            Log.e("未知页面次数太多，返回", AnalystService.this, ToastDuration.LENGTH_SHORT);
                            performGlobalAction(GLOBAL_ACTION_BACK);
                        }
                    } else {
                //        WatcherUtil.post2watcher(QQApplication.getContext(), "PageChange");
                        String formPage = uiTree.getFoundFormPage();
                        Log.i("UI类型: " + formPage);
                        mUnknownUiCount = 0;
                        mBusiness.handleUiEvent(uiTree, keepingAlive);
                    }
                    mNullChildAccessibilityNodeInfoExceptionCount = 0;
                } catch (UiNode.NullChildAccessibilityNodeInfoException e) {
                    Log.e(new MyException("AnalystService.postEvent # run", e));
                    mNullChildAccessibilityNodeInfoExceptionCount++;
                    if (mNullChildAccessibilityNodeInfoExceptionCount > 5) {
                        Log.e(e, AnalystService.this, ToastDuration.LENGTH_SHORT);
               //         WatcherUtil.post2watcher(QQApplication.getContext(), "AccessibilityNodeInfo");
                    } else {
                        postEvent();
                    }
                } catch (Exception e) {
                    Log.e(new MyException("AnalystService.postEvent # run", e));
                    mNullChildAccessibilityNodeInfoExceptionCount = 0;
                } finally {
                    if (null != uiTree) {
                        uiTree.recycle();
                    }
                }
            }
        });
        mLastPostingCount++;
        if (keepingAlive || mLastPostingCount > 20) {
            // KeepingAlive的时候，立马执行
            // 连续post次数过多，则不再延迟，立马执行
            mMainHandler.sendMessage(message);
        } else {
            mMainHandler.sendMessageDelayed(message, DELAY_POST_EVENT);
        }
        postKeepAlive();
    }

    private void postKeepAlive() {
        if (!mRunning) {
            return;
        }
        mMainHandler.removeMessages(ID_POST_KEEP_ALIVE);
        Message message = mMainHandler.obtainMessage(ID_POST_KEEP_ALIVE, new Runnable() {
            @Override
            public void run() {
                postEvent(true);
            }
        });
        mMainHandler.sendMessageDelayed(message, DELAY_KEEP_ALIVE);
    }


}
