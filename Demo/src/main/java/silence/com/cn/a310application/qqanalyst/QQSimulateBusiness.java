package silence.com.cn.a310application.qqanalyst;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import silence.com.cn.a310application.appanalyst.AnalystBusiness;
import silence.com.cn.a310application.appanalyst.AnalystServiceHandler;
import silence.com.cn.a310application.appanalyst.GpsMocker;
import silence.com.cn.a310application.appanalyst.Log;
import silence.com.cn.a310application.appanalyst.MyException;
import silence.com.cn.a310application.appanalyst.ToastDuration;
import silence.com.cn.a310application.appanalyst.UiTree;


public class QQSimulateBusiness extends AnalystBusiness {
    public static final int RECONNECT_SERVER_DELAY = 5 * 1000;
    public static final int SUBMIT_WXPERSON_TIMEOUT = 10 * 1000;
    public static final int LOCATING_TIMEOUT = 10 * 1000;
    private static final java.lang.String TAG = "QQSimulateBusiness";

    private final Handler mMainHandler = new Handler();
   // private final DiskFiles mDiskFiles;
    private Thread mNewGpsThread[] = new Thread[]{null};
   // private WxTask mWxTask;
    private ClipboardManager.OnPrimaryClipChangedListener mPrimaryClipChangedListener;

    public QQSimulateBusiness(@NonNull AnalystServiceHandler serviceHandler) throws MyException {
        super(serviceHandler);
//        mDiskFiles = new DiskFiles(new File(Environment.getExternalStorageDirectory(), "tencent" + File.separator +
//                "MicroMsg" + File.separator + "WeiXin"));
    }

    @Override
    public void handleUiEvent(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        Log.i("QQSimulateBusiness.handleUiEvent # keepingAlive=" + (keepingAlive ? "true" : "false"));
        FormPage formPage = FormPage.valueOf(uiTree.getFoundFormPage());
        if (null == formPage) {
            return;
        }
        switch (formPage) {
            case QQ_MainActivity_Message:
                handle_QQMainActivity_Message(uiTree, keepingAlive);
                break;
        }
    }

    private void handle_QQMainActivity_Message(UiTree uiTree, boolean keepingAlive) {
        try {
            uiTree.getNode("动态").click();
            Log.d("点击了动态页面==============");
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    public Context getContext() {
        return getServiceHandler().getContext();
    }

    public GpsMocker getGpsMocker() {
        return getServiceHandler().getGpsMocker();
    }

    private void readyClipboard(ClipboardManager.OnPrimaryClipChangedListener listener) {
        ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (null != mPrimaryClipChangedListener) {
            clipboardManager.removePrimaryClipChangedListener(mPrimaryClipChangedListener);
        }
        mPrimaryClipChangedListener = listener;
        clipboardManager.addPrimaryClipChangedListener(mPrimaryClipChangedListener);
    }

    private void releaseClipboard() {
        ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.removePrimaryClipChangedListener(mPrimaryClipChangedListener);
        mPrimaryClipChangedListener = null;
    }

    public void requireBack(@NonNull UiTree uiTree) {
        try {
            uiTree.getNode("返回").click();
        } catch (MyException e) {
            getServiceHandler().requireBack();
        }
    }

    private void submitGlobalException(final String message) {
     //   final Config config = new Config();
      //  config.load(getContext());
     /*   new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://" + config.getAppServerUrl() + Url.SubmitGlobalException;
                try {
                    Connection connection = Jsoup.connect(url);
                    connection.ignoreContentType(true);           connection.data("content", message);
                    connection.data("pointId", "");
                    connection.data("type", "1");
                    connection.post();
                } catch (Exception e) {
                    Log.e(new MyException("提交异常信息失败", new MyException("连接URL: " + url, e)));
                }
            }
        }).start();*/
    }

    private void submitTaskException(final int taskId, final String message) {
       /* final Config config = new Config();
        config.load(getContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://" + config.getAppServerUrl() + Url.SubmitTaskException;
                try {
                    Connection connection = Jsoup.connect(url);
                    connection.ignoreContentType(true);
                    connection.data("content", message);
                    connection.data("pointId", "" + taskId);
                    connection.data("type", "2");
                    connection.post();
                } catch (Exception e) {
                    Log.e(new MyException("提交异常信息失败", new MyException("连接URL: " + url, e)));
                }
            }
        }).start();*/
    }

    private void submitException(@Nullable Integer taskId, final String message) {
        if (null == taskId) {
            submitGlobalException(message);
        } else {
            submitTaskException(taskId, message);
        }
    }

    private void handle_LauncherUI_Login(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        Log.e("界面异常 - 当前为登录界面", getContext(), ToastDuration.LENGTH_LONG);
        submitGlobalException("当前为登录界面，需要人工登录");
    }

    /*private void handle_LauncherUI_Main(@NonNull final UiTree uiTree, final boolean keepingAlive) throws MyException {
        if (!getGpsMocker().isWorking()) {
            synchronized (mNewGpsThread) {
                if (null == mNewGpsThread[0]) {
                    mNewGpsThread[0] = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final int id;
                            final int areaId;
                            final Gps mockGps;
                            final WxScope wxScope;
                            try {
                                if (Macro.RealGps || !Macro.Network) {
                                    mockGps = Macro.MockGps;
                                    id = 0;
                                    areaId = 0;
                                  //  wxScope = WxScope.forPersonCount(10);
                                } else {
                                    try {
                                        JSONObject jsonObject;
                                        String url = null;
                                        try {
                                            Config config = new Config();
                                            config.load(getContext());
                                            url = "http://" + config.getAppServerUrl() + Url.GetTask + config
                                                    .getAppAreaConfig();
                                            Connection connection = Jsoup.connect(url);
                                            connection.ignoreContentType(true);
                                            Document document = connection.get();
                                            SilenceLog.e(document.toString());
                                            jsonObject = new JSONObject(document.body().text());
                                        } catch (Exception e) {
                                            throw new MyException("连接服务器失败", new MyException("连接URL: " + url, e));
                                        }
                                        String gpsText;
                                        int amount;
                                        long intervalTime;//秒
                                        String type;
                                        try {
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                            JSONObject itemObject = jsonArray.getJSONObject(0);
                                            id = itemObject.getInt("id");
                                            areaId = itemObject.getInt("area_id");
                                            gpsText = itemObject.getString("position");
                                            amount = itemObject.getInt("amount");
                                            intervalTime = itemObject.getLong("interval_time");
                                            type = itemObject.getString("type");
                                        } catch (Exception e) {
                                            throw new MyException("解析服务器数据异常", new MyException("连接URL: " + url, e));
                                        }
                                        try {
                                            String[] texts = gpsText.split(":");
                                            mockGps = new Gps(Float.parseFloat(texts[2]), Float.parseFloat(texts[1]));
                                        } catch (Exception e) {
                                            throw new MyException("解析服务器GPS数据异常", new MyException("连接URL: " + url, e));
                                        }
                                        if ("amount".equals(type)) {
                                            wxScope = WxScope.forPersonCount(amount);
                                        } else if ("interval_time".equals(type)) {
                                            wxScope = WxScope.forTaskTime(intervalTime * 1000);
                                        } else {
                                            throw new MyException("解析服务器工作方式异常", new MyException("连接URL: " + url));
                                        }
                                    } catch (Exception e) {
                                        Log.e(e, getContext(), ToastDuration.LENGTH_SHORT);
                                        mMainHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                getServiceHandler().requirePost();
                                            }
                                        }, RECONNECT_SERVER_DELAY);
                                        return;
                                    }
                                }
                            } finally {
                                synchronized (mNewGpsThread) {
                                    mNewGpsThread[0] = null;
                                }
                            }
                            mMainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    getGpsMocker().start(getContext(), mockGps);
                                    mWxTask = new WxTask(id, areaId, wxScope);
                                    getServiceHandler().requirePost();
                                }
                            });
                        }
                    });
                    mNewGpsThread[0].start();
                }
            }
        } else {
            uiTree.getNode("发现").click();
            uiTree.getNode("附近的人").click();
        }
    }*/

  /*  private void handle_LoginHistoryUI(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        //TODO Zhukun 自动登录
        final Config config = new Config();
        config.load(getContext());
        uiTree.getNode("输入密码").setEditText(QQApplication.getContext(), "");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        uiTree.getNode("输入密码").setEditText(QQApplication.getContext(), config.getWeChatPwd());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        uiTree.getNode("登录").click();
        submitGlobalException("输入密码" + config.getWeChatPwd());
        Log.e("输入密码" + config.getWeChatPwd(), getContext(), ToastDuration.LENGTH_LONG);
    }*/

    /**
     * 帐号或密码错误，请重新填写
     *
     * @param uiTree       uiTree
     * @param keepingAlive keepingAlive
     * @throws MyException
     */
  /*  private void handle_LoginErro_1(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        uiTree.getNode("确定").click();
        Log.e("帐号或密码错误，请重新填写");
    }*/

    /**
     * 密码错误，找回或重置密码？
     *
     * @param uiTree       uiTree
     * @param keepingAlive keepingAlive
     * @throws MyException
     */
 /*   private void handle_LoginErro_2(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        uiTree.getNode("取消").click();
        Log.e("密码错误，找回或重置密码？");
    }*/

 /*   private void handle_NearbyFriendsIntroUI(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        if (!getGpsMocker().isWorking()) {
            requireBack(uiTree);
            return;
        }
        uiTree.getNode("开始查看").click();
    }*/

/*    private void handle_NearbyFriendShowSayHiUI(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        if (!getGpsMocker().isWorking()) {
            requireBack(uiTree);
            return;
        }
        uiTree.getNode("开始查看").click();
    }*/

/*    private void handle_BaseP_0(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        if (!getGpsMocker().isWorking()) {
            requireBack(uiTree);
            return;
        }
        if (null == mWxTask) {
            getGpsMocker().stop();
            requireBack(uiTree);
            return;
        }
        if (mWxTask.mLocatingStartTime <= 0) {
            //第一次进入该页面
            mWxTask.mLocatingStartTime = System.currentTimeMillis();
            return;
        }
        if (System.currentTimeMillis() >= mWxTask.mLocatingStartTime + LOCATING_TIMEOUT) {
            mWxTask.mLocatingStartTime = 0;
            submitTaskException(mWxTask.getId(), "定位超时");
            requireBack(uiTree);
            return;
        }
    }*/

/*
    private void handle_BaseH_0(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        uiTree.getNode("下次不提示").click();
        uiTree.getNode("确定").click();
    }
*/

 /*   private void handle_BaseH_1(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        uiTree.getNode("确定").click();
    }
*/
  /*  private void handle_BaseH_2(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        Log.e("定位异常", getContext(), ToastDuration.LENGTH_LONG);
        submitException(null == mWxTask ? null : mWxTask.getId(), "定位失败");
        getGpsMocker().stop();
        requireBack(uiTree);
    }*/

 /*   private void handle_BaseH_3(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        uiTree.getNode("确定").click();
    }*/

 /*   private void handle_BaseH_4(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        uiTree.getNode("确定").click();
    }*/

/*    private void handle_BaseH_5(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        uiTree.getNode("关闭").click();
    }*/

    private void handle_BaseH_6(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        uiTree.getNode("取消").click();
    }

    private void handle_BaseH_7(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        uiTree.getNode("取消").click();
    }

    private void handle_BaseH_8(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        uiTree.getNode("取消").click();
    }

    private void handle_BaseH_9(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        uiTree.getNode("确定").click();
    }

/*    private void handle_NearbyFriendsUI(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        if (!getGpsMocker().isWorking()) {
            requireBack(uiTree);
            return;
        }
        if (null == mWxTask) {
            getGpsMocker().stop();
            requireBack(uiTree);
            return;
        }
        if (null == mWxTask.person()) {
            uiTree.getNode("更多").click();
            return;
        }
        UiNode listNode = uiTree.getNode("列表");
        if (mWxTask.mPersonIndex >= listNode.getChildCount()) {
            //需要翻页
            int count = listNode.getChildCount();
            if (count > 0) {
                UiNode lastItemNode = listNode.getChild(count - 1);
                WxPersonTemp personTemp = new WxPersonTemp();
                try {
                    UiNode node = lastItemNode.getChild(0).getChild(1).getChild(0).getChild(0);
                    personTemp.mNickname = StringUtil.toNullable(node.getText());
                } catch (NullPointerException e) {
                    //忽略异常
                }
                try {
                    UiNode node = lastItemNode.getChild(0).getChild(1).getChild(0).getChild(1);
                    personTemp.mSex = StringUtil.toNullable(node.getDescription());
                } catch (NullPointerException e) {
                    //忽略异常
                }
                try {
                    UiNode node = lastItemNode.getChild(0).getChild(1).getChild(1).getChild(0);
                    personTemp.mDistance = StringUtil.toNullable(node.getText());
                } catch (NullPointerException e) {
                    //忽略异常
                }
                try {
                    UiNode node = lastItemNode.getChild(0).getChild(2).getChild(0);
                    personTemp.mIdiograph = StringUtil.toNullable(node.getText());
                } catch (NullPointerException e) {
                    //忽略异常
                }
                if (null != mWxTask.mPersonTemp) {
                    if (personTemp.equals(mWxTask.mPersonTemp)) {
                        Log.i("遍历结束", getContext(), ToastDuration.LENGTH_SHORT);
                        getGpsMocker().stop();
                        getServiceHandler().requirePost();
                        return;
                    }
                }
                mWxTask.mPersonTemp = personTemp;
            } else {
                mWxTask.mPersonTemp = null;
            }
            mWxTask.mPersonIndex = 0;
            listNode.scrollForward();
            getServiceHandler().requirePost();
        } else {
            UiNode itemNode = listNode.getChild(mWxTask.mPersonIndex);
            if (0 == mWxTask.mPersonIndex) {
                if (0 == itemNode.getChildCount()) {
                    //首页第一个为空，跳过
                    mWxTask.mPersonIndex++;
                    handle_NearbyFriendsUI(uiTree, keepingAlive);
                    return;
                } else {
                    Rect listViewBounds = new Rect();
                    listNode.getBoundsInScreen(listViewBounds);
                    Rect itemBounds = new Rect();
                    itemNode.getBoundsInScreen(itemBounds);
                    if (itemBounds.top < listViewBounds.top) {
                        //第一个为上一页已访问的，跳过
                        mWxTask.mPersonIndex++;
                        handle_NearbyFriendsUI(uiTree, keepingAlive);
                        return;
                    }
                }
            }
            mWxTask.mPersonIndex++;
            try {
                mWxTask.personNew();
            } catch (WxTask.TaskEndException e) {
                Log.i("当前任务结束", getContext(), ToastDuration.LENGTH_SHORT);
                getGpsMocker().stop();
                requireBack(uiTree);
                return;
            } catch (MyException e) {
                Log.e(new MyException("获取下个Person异常", e), getContext(), ToastDuration.LENGTH_SHORT);
                getGpsMocker().stop();
                requireBack(uiTree);
                return;
            }
            itemNode.click();
        }
    }
    */

    private void handle_NearbyFriendsUI_Nobody(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        if (getGpsMocker().isWorking()) {
            Log.e("没有附近的人", getContext(), ToastDuration.LENGTH_LONG);
            getGpsMocker().stop();
        }
        requireBack(uiTree);
    }

   /* private void handle_NearbyFriendsUI_NoLocation(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        if (getGpsMocker().isWorking()) {
            Log.e("不能获取位置", getContext(), ToastDuration.LENGTH_LONG);
            submitException(null == mWxTask ? null : mWxTask.getId(), "不能获取位置");
            getGpsMocker().stop();
        }
        requireBack(uiTree);
    }*/

/*    private void handle_NearbyFriendsUIMenu(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        if (null != mWxTask && null == mWxTask.person()) {
            mWxTask.personReset();
            uiTree.getNode("清除位置并退出").click();
        } else {
            requireBack(uiTree);
        }
    }*/

    /*private void handle_ContactInfoUI(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        if (null == mWxTask || null == mWxTask.person()) {
            requireBack(uiTree);
            return;
        }
        switch (mWxTask.person().step()) {
            case PersonInfo:
                handle_ContactInfoUI_CasePersonInfo(uiTree, keepingAlive);
                break;
            case PersonImage:
                handle_ContactInfoUI_CasePersonImage(uiTree, keepingAlive);
                break;
            case Messages:
                handle_ContactInfoUI_CaseMessages(uiTree, keepingAlive);
                break;
            case Finish:
            case Finished:
                handle_ContactInfoUI_CaseFinish(uiTree, keepingAlive);
                break;
            default:
                Log.e("QQSimulateBusiness.handle_ContactInfoUI # 错误步骤(" + mWxTask.person().step().name() + ")，返回",
                        getContext(), ToastDuration.LENGTH_SHORT);
                requireBack(uiTree);
                break;
        }
    }*/

    /*private void handle_ContactInfoUI_CasePersonInfo(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        if (null == mWxTask || null == mWxTask.person()) {
            requireBack(uiTree);
            return;
        }
        if (WxPerson.Step.PersonInfo != mWxTask.person().step()) {
            requireBack(uiTree);
            return;
        }
        String nickname = StringUtil.toNb(uiTree.getNode("昵称").getText());
        if (null != nickname) {
            // 昵称总是会多一个空格，这里去掉
            if (nickname.endsWith(" ")) {
                nickname = nickname.substring(0, nickname.length() - 1);
            }
        }
        mWxTask.person().setNickname(nickname);
        String sex = StringUtil.toNb(uiTree.getNode("性别").getDescription());
        mWxTask.person().setSex(sex);
        String location = StringUtil.toNb(uiTree.getNode("位置").getText());
        mWxTask.person().setLocation(location);
        UiNode listNode = uiTree.getNode("列表");
        for (int i = 2; i < listNode.getChildCount(); i++) {
            try {
                UiNode itemNode = listNode.getChild(i);
                UiNode labelNode = itemNode.getChild(0).getChild(0).getChild(0).getChild(0);
                String label = StringUtil.toEb(labelNode.getText());
                if (label.equals("地区")) {
                    UiNode valueNode = itemNode.getChild(0).getChild(0).getChild(1).getChild(0).getChild(0);
                    mWxTask.person().setRegion(StringUtil.toNb(valueNode.getText()));
                } else if (label.equals("个性签名")) {
                    UiNode valueNode = itemNode.getChild(0).getChild(0).getChild(1).getChild(0).getChild(0);
                    mWxTask.person().setIdiograph(StringUtil.toNb(valueNode.getText()));
                }
            } catch (NullPointerException e) {
                //继续遍历
            }
        }
        mWxTask.person().nextStep(WxPerson.Step.PersonImage);
        handle_ContactInfoUI_CasePersonImage(uiTree, keepingAlive);
    }*/

    /*private void handle_ContactInfoUI_CasePersonImage(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        if (null == mWxTask || null == mWxTask.person()) {
            requireBack(uiTree);
            return;
        }
        if (WxPerson.Step.PersonImage != mWxTask.person().step()) {
            requireBack(uiTree);
            return;
        }
        uiTree.getNode("头像").click();
    }*/

    /*private void handle_ContactInfoUIImage(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        if (null == mWxTask || null == mWxTask.person()) {
            requireBack(uiTree);
            return;
        }
        switch (mWxTask.person().step()) {
            case PersonImage: {
                UiNode node = uiTree.getNode("图片");
                Rect bounds = new Rect();
                node.getBoundsInScreen(bounds);
                mWxTask.person().nextStep(WxPerson.Step.PersonImageSaving);
                mWxTask.person().mImageSaveCount = 0;
                getServiceHandler().requireMockLongClick(new PointXY(bounds.centerX(), bounds.centerY()));
                getServiceHandler().requirePost();
            }
            break;
            case PersonImageSaving:
                if (mWxTask.person().mImageSaveCount > 3) {
                    Log.e("获取头像失败", getContext(), ToastDuration.LENGTH_SHORT);
                    mWxTask.person().nextStep(WxPerson.Step.PersonImageSaved);
                    mWxTask.person().nextStep();
                    requireBack(uiTree);
                } else {
                    mWxTask.person().mImageSaveCount++;
                    getServiceHandler().requirePost();
                }
                break;
            case PersonImageSaved:
                mWxTask.person().setPersonImage(mDiskFiles.nextFreshFileName());
                mWxTask.person().nextStep();
                requireBack(uiTree);
                break;
            default:
                Log.e("QQSimulateBusiness.handle_ContactInfoUIImage # 错误步骤(" + mWxTask.person().step().name() + ")，返回",
                        getContext(), ToastDuration.LENGTH_SHORT);
                requireBack(uiTree);
                break;
        }
    }*/

    /*private void handle_BaseK_ImageSaving(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        if (null == mWxTask || null == mWxTask.person()) {
            requireBack(uiTree);
            return;
        }
        if (WxPerson.Step.PersonImageSaving != mWxTask.person().step()) {
            requireBack(uiTree);
            return;
        }
        mDiskFiles.scanFiles();
        mWxTask.person().nextStep(WxPerson.Step.PersonImageSaved);
        uiTree.getNode("保存到手机").click();
    }*/

   /* private void handle_ContactInfoUI_CaseMessages(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        if (null == mWxTask || null == mWxTask.person()) {
            requireBack(uiTree);
            return;
        }
        if (WxPerson.Step.Messages != mWxTask.person().step()) {
            requireBack(uiTree);
            return;
        }
        UiNode listNode = uiTree.getNode("列表");
        UiNode photosNode = null;
        for (int i = 2; i < listNode.getChildCount(); i++) {
            try {
                UiNode itemNode = listNode.getChild(i);
                String description = StringUtil.toEb(itemNode.getDescription());
                if (description.startsWith("个人相册")) {
                    photosNode = itemNode;
                    break;
                }
            } catch (Exception e) {
                //继续遍历
            }
        }
        if (null == photosNode) {
            mWxTask.person().nextStep(WxPerson.Step.Finish);
            handle_ContactInfoUI_CaseFinish(uiTree, keepingAlive);
        } else {
            photosNode.click();
        }
    }*/

    /*private void handle_ContactInfoUI_CaseFinish(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        if (null == mWxTask || null == mWxTask.person()) {
            requireBack(uiTree);
            return;
        }
        switch (mWxTask.person().step()) {
            case Finish: {
                if (null != mWxTask.person().mFinishTime) {
                    //判断超时
                    if (System.currentTimeMillis() >= mWxTask.person().mFinishTime + SUBMIT_WXPERSON_TIMEOUT) {
                        mWxTask.person().nextStep(WxPerson.Step.Finished);
                        getServiceHandler().requirePost();
                    }
                } else {
                    final WxPerson wxPerson = mWxTask.person();
                    wxPerson.mFinishTime = System.currentTimeMillis();
                    final Config config = new Config();
                    config.load(getContext());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                wxPerson.submit(getContext(), config);
                                Log.i("提交完成");
                            } catch (MyException e) {
                                Log.e(e, getContext(), ToastDuration.LENGTH_SHORT);
                            }
                            mMainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    wxPerson.deleteImageFiles();
                                    wxPerson.nextStep(WxPerson.Step.Finished);
                                    getServiceHandler().requirePost();
                                }
                            });
                        }
                    }).start();
                }
            }
            break;
            case Finished:
                requireBack(uiTree);
                break;
            default:
                Log.e("QQSimulateBusiness.handle_ContactInfoUI_CaseFinish # 错误步骤(" + mWxTask.person().step().name() + ")" +
                        "，返回", getContext(), ToastDuration.LENGTH_SHORT);
                requireBack(uiTree);
                break;
        }
    }*/

    /*private void handle_SnsUserUI(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        if (null == mWxTask || null == mWxTask.person()) {
            requireBack(uiTree);
            return;
        }
        if (WxPerson.Step.Messages != mWxTask.person().step()) {
            requireBack(uiTree);
            return;
        }
        UiNode listNode = uiTree.getNode("列表");
        List<WxMessage> wxMessages = mWxTask.person().getMessages();
        String md = null;
        for (int i = 1; i < listNode.getChildCount(); i++) {
            if (i - 1 >= wxMessages.size()) {
                wxMessages.add(null);
            }
            WxMessage wxMessage = wxMessages.get(i - 1);
            UiNode itemNode = listNode.getChild(i);
            try {
                UiNode dateMdNode = itemNode.getChild(2).getChild(0).getChild(0).getChild(0);
                UiNode dateDNode = dateMdNode.getChild(0);
                if (StringUtil.compareNb(dateDNode.getClassName(), "android.widget.TextView")) {
                    String d = StringUtil.toEb(dateDNode.getText());
                    md = DateUtil.toMd(d, null);
                    if (null == md) {
                        UiNode dateMNode = dateMdNode.getChild(1);
                        String m = StringUtil.toEb(dateMNode.getText());
                        md = m + d;
                    }
                }
            } catch (NullPointerException e) {
                //忽略异常
            }
            if (null == wxMessage) {
                try {
                    if (StringUtil.compareNb(itemNode.getChild(0).getChild(0).getChild(1).getText(), "非朋友最多显示十张照片")) {
                        //结束
                        break;
                    }
                } catch (NullPointerException e) {
                    //忽略异常
                }
                try {
                    UiNode node = itemNode;
                    if (StringUtil.compareNb("android.widget.LinearLayout", node.getClassName()) && 1 == node
                            .getChildCount()) {
                        node = itemNode.getChild(0);
                        if (StringUtil.compareNb("android.widget.LinearLayout", node.getClassName()) && 1 == node
                                .getChildCount()) {
                            node = node.getChild(0);
                            if (StringUtil.compareNb("android.widget.LinearLayout", node.getClassName()) && 1 == node
                                    .getChildCount()) {
                                node = node.getChild(0);
                                if (StringUtil.compareNb("android.widget.ImageView", node.getClassName()) && 0 == 
                                        node.getChildCount()) {
                                    //结束行
                                    break;
                                }
                            }
                        }
                    }
                } catch (NullPointerException e) {
                    //忽略异常
                }
                //由于可点的位置在itemNode内部，所以使用最后一个子节点来判断
                UiNode childNode = null;
                for (int n = 0; n < itemNode.getChildCount(); n++) {
                    UiNode node = null;
                    try {
                        node = itemNode.getChild(n).getChild(0);
                    } catch (NullPointerException e) {
                        //忽略异常
                    }
                    try {
                        node = itemNode.getChild(n).getChild(1);
                    } catch (NullPointerException e) {
                        //忽略异常
                    }
                    if (null != node) {
                        childNode = node;
                    }
                }
                if (null == childNode) {
                    //异常，结束
                    break;
                }
                //点击进入具体的消息页面
                Rect listBounds = new Rect();
                listNode.getBoundsInScreen(listBounds);
                Rect childBounds = new Rect();
                childNode.getBoundsInScreen(childBounds);
                if (childBounds.top >= listBounds.bottom) {
                    //该项已经在显示界面之外，由于目前没有滚屏机制，所以忽略之后的item
                    break;
                }
                PointXY point = new PointXY(childBounds.centerX(), childBounds.centerY());
                if (point.mY >= listBounds.bottom) {
                    point.mY = listBounds.bottom - 1;
                }
                getServiceHandler().requireMockClick(point);
                return;
            } else {
                wxMessage.setDateMd(md);
            }
        }
        //遍历结束
        if (!wxMessages.isEmpty()) {
            //去掉最后一条，最后一条为结束图标
            if (null == wxMessages.get(wxMessages.size() - 1)) {
                wxMessages.remove(wxMessages.size() - 1);
            }
        }
        mWxTask.person().nextStep(WxPerson.Step.Finish);
        requireBack(uiTree);
    }
*/
    /*private void handle_SnsGalleryUI(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        if (null == mWxTask || null == mWxTask.person()) {
            requireBack(uiTree);
            return;
        }
        switch (mWxTask.person().step()) {
            case Messages: {
                List<WxMessage> wxMessages = mWxTask.person().getMessages();
                if (wxMessages.isEmpty()) {
                    requireBack(uiTree);
                    return;
                }
                WxMessage wxMessage = new WxMessage();
                wxMessage.mGalleryPageIndex = 0;
                wxMessage.mGalleryNoTitleClickCount = 0;
                try {
                    wxMessage.setDescription(StringUtil.toNb(uiTree.getNode("描述").getText()));
                } catch (MyException e) {
                    //忽略异常
                }
                wxMessage.setDateYmdhm(DateUtil.toYmdhm(StringUtil.toNb(uiTree.getNode("时间").getText())));
                wxMessages.set(wxMessages.size() - 1, wxMessage);
                mWxTask.person().nextStep(WxPerson.Step.MessageImageReading);
                handle_SnsGalleryUI(uiTree, keepingAlive);
            }
            break;
            case MessageImageReading: {
                uiTree.getNode("更多").click();
            }
            break;
            case MessageImageOneReadEnd: {
                List<WxMessage> wxMessages = mWxTask.person().getMessages();
                if (wxMessages.isEmpty()) {
                    requireBack(uiTree);
                    return;
                }
                WxMessage wxMessage = wxMessages.get(wxMessages.size() - 1);
                if (null == wxMessage) {
                    requireBack(uiTree);
                    return;
                }
                wxMessage.getImages().add(mDiskFiles.nextFreshFileName());
                UiNode pagingNode = null;
                try {
                    pagingNode = uiTree.getNode("分页");
                } catch (MyException e) {
                    //忽略异常
                }
                if (null != pagingNode) {
                    wxMessage.mGalleryPageIndex++;
                    String text = StringUtil.toEb(pagingNode.getText());
                    String separator = " / ";
                    int off = text.indexOf(separator);
                    int page = Integer.parseInt(text.substring(0, off));
                    if (page != wxMessage.mGalleryPageIndex) {
                        Log.e("当前页码不匹配，返回", getContext(), ToastDuration.LENGTH_SHORT);
                        mWxTask.person().nextStep(WxPerson.Step.Messages);
                        requireBack(uiTree);
                    }
                    int count = Integer.parseInt(text.substring(off + separator.length()));
                    if (page < count) {
                        UiNode galleryNode = uiTree.getNode("照片墙");
                        Rect bounds = new Rect();
                        galleryNode.getBoundsInScreen(bounds);
                        if (Math.abs(bounds.right - bounds.left) <= 2) {
                            throw new MyException("照片墙控件大小异常");
                        }
                        wxMessage.mGalleryNoTitleClickCount = 0;
                        mWxTask.person().nextStep(WxPerson.Step.MessageImageReading);
                        getServiceHandler().requireMockMove(new PointXY2XY(new PointXY(bounds.right - 1, bounds
                                .centerY()), new PointXY(bounds.left + 1, bounds.centerY())));
                        return;
                    } else {
                        mWxTask.person().nextStep(WxPerson.Step.Messages);
                        requireBack(uiTree);
                    }
                } else {
                    mWxTask.person().nextStep(WxPerson.Step.Messages);
                    requireBack(uiTree);
                }
            }
            break;
            default:
                Log.e("QQSimulateBusiness.handle_SnsGalleryUI # 错误步骤(" + mWxTask.person().step().name() + ")，返回", getContext
                        (), ToastDuration.LENGTH_SHORT);
                requireBack(uiTree);
                break;
        }
    }*/

    /*private void handle_SnsGalleryUI_NoTitle(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        if (null == mWxTask || null == mWxTask.person()) {
            requireBack(uiTree);
            return;
        }
        if (WxPerson.Step.MessageImageReading != mWxTask.person().step()) {
            requireBack(uiTree);
            return;
        }
        List<WxMessage> wxMessages = mWxTask.person().getMessages();
        if (wxMessages.isEmpty()) {
            requireBack(uiTree);
            return;
        }
        WxMessage wxMessage = wxMessages.get(wxMessages.size() - 1);
        if (null == wxMessage) {
            requireBack(uiTree);
            return;
        }
        if (wxMessage.mGalleryNoTitleClickCount >= 5) {
            //点击多次无反应，则当前页面为“图片获取中”状态
            requireBack(uiTree);
            return;
        }
        wxMessage.mGalleryNoTitleClickCount++;
        UiNode node = uiTree.getNode("照片墙");
        Rect bounds = new Rect();
        node.getBoundsInScreen(bounds);
        getServiceHandler().requireMockClick(new PointXY(bounds.centerX(), bounds.centerY()));
        getServiceHandler().requirePost();
    }

    private void handle_SnsGalleryUIMenu(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        if (null == mWxTask || null == mWxTask.person()) {
            requireBack(uiTree);
            return;
        }
        if (WxPerson.Step.MessageImageReading != mWxTask.person().step()) {
            requireBack(uiTree);
            return;
        }
        mWxTask.person().nextStep(WxPerson.Step.MessageImageOneReadEnd);
        mDiskFiles.scanFiles();
        try {
            uiTree.getNode("保存到手机").click();
        } catch (MyException e) {
            //当前为视频或其他
            requireBack(uiTree);
        }
    }*/

   /* private void handle_SnsCommentDetailUI(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        if (null == mWxTask || null == mWxTask.person()) {
            requireBack(uiTree);
            return;
        }
        switch (mWxTask.person().step()) {
            case Messages: {
                List<WxMessage> wxMessages = mWxTask.person().getMessages();
                if (wxMessages.isEmpty()) {
                    requireBack(uiTree);
                    return;
                }
                final WxMessage wxMessage = new WxMessage();
                try {
                    wxMessage.setTitle(StringUtil.toNb(uiTree.getNode("标题").getText()));
                } catch (MyException e) {
                    //忽略异常
                }
                try {
                    wxMessage.setDescription(StringUtil.toNb(uiTree.getNode("描述").getText()));
                } catch (MyException e) {
                    //忽略异常
                }
                try {
                    wxMessage.setSource(StringUtil.toNb(uiTree.getNode("源").getText()));
                } catch (MyException e) {
                    //忽略异常
                }
                try {
                    wxMessage.setRegion(StringUtil.toNb(uiTree.getNode("地区").getText()));
                } catch (MyException e) {
                    //忽略异常
                }
                try {
                    wxMessage.setDateYmdhm(DateUtil.toYmdhm(StringUtil.toNb(uiTree.getNode("时间").getText())));
                } catch (MyException e) {
                    //忽略异常
                }
                try {
                    wxMessage.setTail(StringUtil.toNb(uiTree.getNode("尾注").getText()));
                } catch (MyException e) {
                    //忽略异常
                }
                if (Macro.Debug) {
                    String text = "获取消息内容：";
                    text += "\n日期：" + wxMessage.getDateYmdhm();
                    text += "\n描述：" + wxMessage.getDescription();
                    text += "\n标题：" + wxMessage.getTitle();
                    text += "\n源：" + wxMessage.getSource();
                    text += "\n尾注：" + wxMessage.getTail();
                    text += "\n地区位置：" + wxMessage.getRegion();
                    Log.i(text);
                }
                wxMessages.set(wxMessages.size() - 1, wxMessage);
                UiNode enterNode = null;
                try {
                    enterNode = uiTree.getNode("进入");
                } catch (MyException e) {
                    //忽略异常
                }
                if (null != enterNode) {
                    mWxTask.person().nextStep(WxPerson.Step.MessageWebReading);
                    readyClipboard(new ClipboardManager.OnPrimaryClipChangedListener() {
                        @Override
                        public void onPrimaryClipChanged() {
                            ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService
                                    (Context.CLIPBOARD_SERVICE);
                            try {
                                wxMessage.setUrl(StringUtil.toNb(clipboardManager.getPrimaryClip().getItemAt(0)
                                        .getText()));
                                if (Macro.Debug) {
                                    Log.i("获取URL：" + wxMessage.getUrl());
                                }
                            } catch (Exception e) {
                                //忽略异常
                            }
                            releaseClipboard();
                        }
                    });
                    Rect bounds = new Rect();
                    enterNode.getBoundsInScreen(bounds);
                    getServiceHandler().requireMockClick(new PointXY(bounds.centerX(), bounds.centerY()));
                    getServiceHandler().requirePost();
                } else {
                    requireBack(uiTree);
                }
            }
            break;
            case MessageWebReading: {
                List<WxMessage> wxMessages = mWxTask.person().getMessages();
                if (wxMessages.isEmpty()) {
                    requireBack(uiTree);
                    return;
                }
                WxMessage wxMessage = wxMessages.get(wxMessages.size() - 1);
                if (null == wxMessage) {
                    requireBack(uiTree);
                    return;
                }
                if (wxMessage.mWebReadingCount > 3) {
                    Log.e("获取URL失败", getContext(), ToastDuration.LENGTH_SHORT);
                    mWxTask.person().nextStep(WxPerson.Step.Messages);
                    requireBack(uiTree);
                } else {
                    wxMessage.mWebReadingCount++;
                }
            }
            break;
            case MessageWebReadEnd: {
                releaseClipboard();
                mWxTask.person().nextStep(WxPerson.Step.Messages);
                requireBack(uiTree);
            }
            break;
            default:
                Log.e("QQSimulateBusiness.handle_SnsCommentDetailUI # 错误步骤(" + mWxTask.person().step().name() + ")，返回",
                        getContext(), ToastDuration.LENGTH_SHORT);
                requireBack(uiTree);
                break;
        }
    }*/

    /*private void handle_WebViewUI(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        if (null == mWxTask || null == mWxTask.person()) {
            requireBack(uiTree);
            return;
        }
        switch (mWxTask.person().step()) {
            case MessageWebReading:
                try {
                    uiTree.getNode("更多").click();
                } catch (MyException e) {
                    mWxTask.person().nextStep(WxPerson.Step.MessageWebReadEnd);
                    requireBack(uiTree);
                }
                break;
            case MessageWebReadEnd:
                requireBack(uiTree);
                break;
            default:
                Log.e("QQSimulateBusiness.handle_WebViewUI # 错误步骤(" + mWxTask.person().step().name() + ")，返回", getContext(),
                        ToastDuration.LENGTH_SHORT);
                requireBack(uiTree);
                break;
        }
    }
*/
    /*private void handle_WebViewUIMenu_Normal(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        if (null == mWxTask || null == mWxTask.person()) {
            requireBack(uiTree);
            return;
        }
        if (WxPerson.Step.MessageWebReading != mWxTask.person().step()) {
            requireBack(uiTree);
            return;
        }
        mWxTask.person().nextStep(WxPerson.Step.MessageWebReadEnd);
        uiTree.getNode("复制链接").click();
    }*/

    /*private void handle_WebViewUIMenu_Other(@NonNull UiTree uiTree, boolean keepingAlive) throws MyException {
        if (null == mWxTask || null == mWxTask.person()) {
            requireBack(uiTree);
            return;
        }
        if (WxPerson.Step.MessageWebReading != mWxTask.person().step()) {
            requireBack(uiTree);
            return;
        }
        mWxTask.person().nextStep(WxPerson.Step.MessageWebReadEnd);
        requireBack(uiTree);
    }*/
}
