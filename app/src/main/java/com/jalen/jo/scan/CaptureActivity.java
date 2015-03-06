package com.jalen.jo.scan;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.google.zxing.Result;

import com.jalen.jo.R;
import com.jalen.jo.activities.BaseActivity;

import java.io.IOException;

public class CaptureActivity extends BaseActivity {
//    M
    private Result lastResult;  // 最近一个结果对象
    private CameraManager mCameraManager;   // 相机管理类
    private boolean hasSurface; // 是否可写入Surface
//    V
    private SurfaceView mSurfaceView;
    private ViewfinderView mViewFinderView;
//    C
    private SurfaceHolder mSurfaceHolder;   // surface <=> surfaceholder <=> surfaceview
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        设置屏幕常亮
        Log.d(tag, "set keep screen on");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture);

//        初始化数据
        hasSurface = false;

//        初始化视图
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_preview);
        mSurfaceHolder = mSurfaceView.getHolder();
        mViewFinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_capture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up butto n, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
//    数据初始化
        lastResult = null;
//        初始化相机管理类
        mCameraManager = new CameraManager(getApplicationContext());
//        ？根据配置文件设置横竖屏配置

//        判断是否有Surface对象
        if (hasSurface){
//            stop状态时camera已经关闭， 但是surface还在
//            initCamera(mSurfaceHolder);
            // 开启摄像头
            try {
                mCameraManager.openDriver(mSurfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
//                ？显示bug信息和退出app
            }
        }else{

        }

        mViewFinderView.setCameraManager(mCameraManager);


    }

    @Override
    protected void onPause() {
        super.onPause();


    }
}
