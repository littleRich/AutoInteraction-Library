package silence.com.cn.a310application.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.FileReader;

import silence.com.cn.a310application.R;
import silence.com.cn.a310application.util.BashHelper;
import silence.com.cn.a310application.util.FilterFile;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_start_capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "抓包数据文件：" + BashHelper.DEST_FILE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final boolean retVal = BashHelper.startCapture(MainActivity.this);
                        Log.d(TAG, "抓包状态:" + retVal);
                    }
                }).start();
            }
        });

        findViewById(R.id.btn_stop_capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FilterFile.extractQQ(null);
                        BashHelper.stopCapture(MainActivity.this);
                    }
                }).start();
            }
        });
    }
}
