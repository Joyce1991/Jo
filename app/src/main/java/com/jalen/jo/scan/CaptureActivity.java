package com.jalen.jo.scan;

import android.content.Intent;
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

public class CaptureActivity extends BaseActivity implements SurfaceHolder.Callback {
//    M
    private Result lastResult;  // 最近一个结果对象
    private CameraManager mCameraManager;   // 相机管理类
    private boolean hasSurface; // 是否可写入Surface
//    V
    private SurfaceView mSurfaceView;
    private ViewfinderView mViewFinderView;
//    C
    private SurfaceHolder mSurfaceHolder;   // surface <=> surfaceholder <=> surfaceview
    private CaptureActivityHandler handler; // activity <=> handler <=> view
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        设置屏幕常亮
        Log.d(tag, "set keep screen on");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture);
        // 设置actionbar的Up按钮可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        switch (item.getItemId()){
            case R.id.home:
                finish();
                break;
            case R.id.action_settings:
                Intent captureSettingsIntent = new Intent(this, CaptureSettingsActivity.class);
                startActivity(captureSettingsIntent);
                break;
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
            initCamera(mSurfaceHolder);
        }else{
//            给surfaceholder添加回调方法，等待surface的创建，surface创建后初始化相机
            mSurfaceHolder.addCallback(this);
        }

        mViewFinderView.setCameraManager(mCameraManager);


    }

    @Override
    protected void onPause() {
        super.onPause();

//        关闭相机
        mCameraManager.closeDriver();
        if (hasSurface){
            // 移除surface
            mSurfaceHolder.removeCallback(this);
        }
    }

//    surface的生命周期方法  START
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 初始化相机
        if (!hasSurface){
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
            mCameraManager.startPreview();
        } catch (Exception e) {
            Log.e(tag, "Could not start preview", e);
            mCameraManager.stopPreview();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCameraManager.stopPreview();
    }   // END

    /**
     * 初始化相机
     * @param holder
     */
    private void initCamera(SurfaceHolder holder){
//        假如相机已经打开
        if (mCameraManager.isOpen()){
            Log.w(tag, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            mCameraManager.openDriver(holder);
//            创建handler开始预览
            if (handler == null) {
                handler = new CaptureActivityHandler(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
//                ？显示bug信息和退出app
        }
    }
}
