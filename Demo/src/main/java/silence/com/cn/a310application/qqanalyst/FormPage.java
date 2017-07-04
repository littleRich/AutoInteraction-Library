package silence.com.cn.a310application.qqanalyst;

import silence.com.cn.a310application.appanalyst.DescriptionMatcher;
import silence.com.cn.a310application.appanalyst.FormControl;
import silence.com.cn.a310application.appanalyst.FormTree;
import silence.com.cn.a310application.appanalyst.Macro;
import silence.com.cn.a310application.appanalyst.TextMatcher;
import silence.com.cn.a310application.appanalyst.UiTree;

public enum FormPage {
    QQ_MainActivity_Message, //QQ主页面：消息Fragment
    QQ_MainActivity_Dynamic, //QQ主页面：动态Fragment
    ;

    static {
        try {
            readyForm_MainActivity_message();
            readyForm_MainActivity_dynamic();

        } catch (Exception e) {
            throw new RuntimeException("准备控件结构异常", e);
        }
    }

    private static void readyForm_MainActivity_dynamic() {
        FormControl formControl = new FormControl("android.widget.FrameLayout")//0
            .set(0, new FormControl("android.widget.LinearLayout")//1
                    .set(0, new FormControl("android.widget.FrameLayout")//2
                    )
            );
    }

    /**
     * 创建主页面的消息布局树
     */
    private static void readyForm_MainActivity_message() {
        FormControl formControl = new FormControl("android.widget.FrameLayout")//0
                .set(0,new FormControl("android.widget.LinearLayout")//1
                        .set(0,new FormControl("android.widget.FrameLayout")//2
                                .set(0,new FormControl("android.widget.FrameLayout")//3
                                        .set(0,new FormControl("android.widget.TabHost")//4
                                                .set(0,new FormControl("android.widget.FrameLayout")//5
                                                        .set(0,new FormControl("android.widget.FrameLayout")//6
                                                                .set(0,new FormControl("android.widget.FrameLayout")//7
                                                                        .set(0,new FormControl("android.widget.RelativeLayout")//8
                                                                                .set(0,new FormControl("android.widget.RelativeLayout")//9
                                                                                        .set(0,new FormControl("android.view.View")//10

                                                                                        )
                                                                                        .set(1,new FormControl("android.widget.RelativeLayout")//10
                                                                                                .set(0,new FormControl("android.widget.RadioGroup")//11
                                                                                                        .set(0,new FormControl("android.widget.RadioButton", new TextMatcher("消息"))//12

                                                                                                        )
                                                                                                        .set(1,new FormControl("android.widget.RadioButton", new TextMatcher("电话"))//12

                                                                                                        )
                                                                                                )
                                                                                                .set(1,new FormControl("android.widget.ImageView", new DescriptionMatcher("快捷入口"))//11

                                                                                                )
                                                                                        )
                                                                                )
                                                                                .set(1,new FormControl("android.widget.RelativeLayout")//9
                                                                                        .set(0,new FormControl("android.widget.AbsListView")//10
                                                                                                .set(0,new FormControl("android.widget.RelativeLayout")//11
                                                                                                        .set(0,new FormControl("android.widget.EditText", new DescriptionMatcher("搜索"))//12

                                                                                                        )
                                                                                                        .set(1,new FormControl("android.widget.TextView", new TextMatcher("搜索"))//12

                                                                                                        )
                                                                                                )
                                                                                                .set(1,new FormControl("android.widget.LinearLayout")//11
                                                                                                        .set(0,new FormControl("android.widget.RelativeLayout")//12
                                                                                                                .set(0,new FormControl("android.widget.ImageView")//13

                                                                                                                )
                                                                                                                .set(1,new FormControl("android.widget.RelativeLayout")//13
                                                                                                                        .set(0,new FormControl("android.view.View")//14

                                                                                                                        )
                                                                                                                        .set(1,new FormControl("android.view.View")//14

                                                                                                                        )
                                                                                                                        .set(2,new FormControl("android.widget.TextView")//14

                                                                                                                        )

                                                                                                                )

                                                                                                        )
                                                                                                        .set(1, new FormControl("android.view.View", new DescriptionMatcher("置顶"))//2
                                                                                                        )
                                                                                                        .set(2, new FormControl("android.view.View", new DescriptionMatcher("删除"))//12
                                                                                                        )
                                                                                                )
                                                                                                .set(2, new FormControl("android.widget.LinearLayout")//11
                                                                                                        .set(0, new FormControl("android.widget.RelativeLayout")//12
                                                                                                            .set(0, new FormControl("android.widget.ImageView")//13
                                                                                                            )
                                                                                                            .set(1, new FormControl("android.widget.RelativeLayout")//13
                                                                                                                .set(0, new FormControl("android.view.View")//14
                                                                                                                )
                                                                                                                .set(1, new FormControl("android.view.View")//14
                                                                                                                )
                                                                                                            )
                                                                                                        )
                                                                                                        .set(1, new FormControl("android.view.View", new DescriptionMatcher("置顶"))//12
                                                                                                        )
                                                                                                        .set(2, new FormControl("android.view.View", new DescriptionMatcher("删除"))//12
                                                                                                        )
                                                                                                )
                                                                                                .set(3, new FormControl("android.widget.LinearLayout")//11
                                                                                                        .set(0, new FormControl("android.widget.RelativeLayout")//12
                                                                                                                .set(0, new FormControl("android.widget.ImageView")//13
                                                                                                                )
                                                                                                                .set(1, new FormControl("android.widget.RelativeLayout")//13
                                                                                                                        .set(0, new FormControl("android.view.View")//14
                                                                                                                        )
                                                                                                                        .set(1, new FormControl("android.view.View")//14
                                                                                                                        )
                                                                                                                )
                                                                                                        )
                                                                                                        .set(1, new FormControl("android.view.View", new DescriptionMatcher("置顶"))//12
                                                                                                        )
                                                                                                        .set(2, new FormControl("android.view.View", new DescriptionMatcher("删除"))//12
                                                                                                        )
                                                                                                )
                                                                                        )
                                                                                        .set(1, new FormControl("android.widget.FrameLayout")//10
                                                                                        )
                                                                                        .set(2, new FormControl("android.view.View")//10
                                                                                        )
                                                                                        .set(3, new FormControl("android.widget.RelativeLayout")//10
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                                .set(1, new FormControl("android.widget.RelativeLayout")//7
                                                                        .set(0, new FormControl("android.widget.Button", new DescriptionMatcher("帐户及设置"))//8
                                                                        )
                                                                        .set(1, new FormControl("android.widget.FrameLayout")//8
                                                                                .set(0, new FormControl("android.widget.ImageView")//9
                                                                                )
                                                                        )
                                                                        .set(2, new FormControl("android.widget.TextView")//8
                                                                        )
                                                                )
                                                                .set(2, new FormControl("android.widget.TabWidget")//7
                                                                        .set(0, new FormControl("android.widget.RelativeLayout")//8
                                                                                .set(0, new FormControl("android.widget.ImageView")//9
                                                                                )
                                                                                .set(1, new FormControl("android.widget.ImageView")//9
                                                                                )
                                                                                .set(2, new FormControl("android.widget.TextView")//9
                                                                                )
                                                                                .set(3, new FormControl("android.widget.ImageView")//9
                                                                                )
                                                                        )
                                                                        .set(1, new FormControl("android.widget.FrameLayout")//8
                                                                                .set(0, new FormControl("android.widget.RelativeLayout")//9
                                                                                        .set(0, new FormControl("android.widget.ImageView")//10
                                                                                        )
                                                                                        .set(1, new FormControl("android.widget.ImageView")//10
                                                                                        )
                                                                                        .set(2, new FormControl("android.widget.ImageView")//10
                                                                                        )
                                                                                )
                                                                        )
                                                                        .set(2, new FormControl("android.widget.FrameLayout")//8
                                                                                .set(0, new FormControl("android.widget.RelativeLayout")//9
                                                                                        .set(0, new FormControl("android.widget.ImageView")//10消息
                                                                                        )
                                                                                        .set(1, new FormControl("android.widget.ImageView")//10动态
                                                                                        )
                                                                                        .set(2, new FormControl("android.widget.ImageView")//10联系人
                                                                                        )
                                                                                )
                                                                                .set(1, new FormControl("android.widget.LinearLayout")//9
                                                                                        .set(0, new FormControl("android.widget.ImageView")//10
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );
        FormTree formTree = new FormTree(FormPage.QQ_MainActivity_Message.name(), "com.tencent.mobileqq.activity.SplashActivity", formControl);
        formTree.addControlPath("动态", new int[]{0, 0, 0, 0, 0, 0, 0, 2, 2});
        formTree.addControlPath("电话", new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1});
        presetForm(AddWhere.Normal, formTree);

    }

    private enum AddWhere {
        Back,
        Normal,
    }

    private static void presetForm(AddWhere addWhere, FormTree formTree) {
        if (Macro.Debug) {
            for (FormTree item : UiTree.PRESET_FORM) {
                if (item.getFormPage().equals(formTree.getFormPage())) {
                    throw new ArrayStoreException("代码错误");
                }
            }
        }
        switch (addWhere) {
            case Normal:
                UiTree.PRESET_FORM.add(0, formTree);
                break;
            case Back:
                UiTree.PRESET_FORM.add(formTree);
                break;
        }
    }

}
