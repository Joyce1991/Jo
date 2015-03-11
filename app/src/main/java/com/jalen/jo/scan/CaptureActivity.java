package com.jalen.jo.scan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;

import com.jalen.jo.R;
import com.jalen.jo.activities.BaseActivity;
import com.jalen.jo.utils.InactivityTimer;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class CaptureActivity extends BaseActivity implements SurfaceHolder.Callback {
//    M
    private Result lastResult;  // 最近一个结果对象
    private CameraManager mCameraManager;   // 相机管理类
    private boolean hasSurface; // 是否可写入Surface
    private Collection<BarcodeFormat> decodeFormats;    // 码格式集合
    private Map<DecodeHintType,?> decodeHints;      // hints
    private String characterSet;        // 码内容编码
    private AmbientLightManager ambientLightManager;    // 闪光灯管理
    private BeepManager beepManager;        // “哔哔声”和“震动”管理
//    V
    private SurfaceView mSurfaceView;
    private ViewfinderView mViewFinderView;
//    C
    private SurfaceHolder mSurfaceHolder;   // surface <=> surfaceholder <=> surfaceview
    private CaptureActivityHandler handler; // activity <=> handler <=> view （在initCamera()方法中进行初始化）
    private InactivityTimer inactivityTimer;    // 这是一个工具类，用于消灭使用了有一段时间没有活动的activity
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
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        ambientLightManager = new AmbientLightManager(this);

//        初始化视图
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_preview);
        mSurfaceHolder = mSurfaceView.getHolder();
        mViewFinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

//        赶紧让设置信息生效
//        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
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
            case android.R.id.home:
                Toast.makeText(this, "点击了Up键", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.action_settings:
                Intent captureSettingsIntent = new Intent(this, CapturePreferencesActivity.class);
                startActivity(captureSettingsIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

//    数据重置
        lastResult = null;
        handler = null;
        decodeFormats = null;
        characterSet = null;
//        初始化相机管理类
        mCameraManager = new CameraManager(getApplicationContext());
//        根据配置文件设置横竖屏配置
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean(CapturePreferencesActivity.KEY_DISABLE_AUTO_ORIENTATION, true)) {
            setRequestedOrientation(getCurrentOrientation());
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
//        判断是否有Surface对象
        if (hasSurface){
//            stop状态时camera已经关闭， 但是surface还在
            initCamera(mSurfaceHolder);
        }else{
//            给surfaceholder添加回调方法，等待surface的创建，surface创建后初始化相机
            mSurfaceHolder.addCallback(this);
        }

        mViewFinderView.setCameraManager(mCameraManager);

        inactivityTimer.onResume();
        ambientLightManager.start(mCameraManager);
        beepManager.updatePrefs();



    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        // 关闭闪光灯管理
        ambientLightManager.stop();
        // 这里主要是资源释放
        beepManager.close();
//        关闭相机
        mCameraManager.closeDriver();
        if (!hasSurface){
            // 移除surface
            mSurfaceHolder.removeCallback(this);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        inactivityTimer.shutdown();
    }

    //  *****************************  surface的生命周期方法  **********************************START
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

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }
    //  *****************************  surface的生命周期方法  **********************************END

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
                handler = new CaptureActivityHandler(this, decodeFormats, decodeHints, characterSet, mCameraManager);
            }
        } catch (IOException e) {
            e.printStackTrace();
//                ？显示bug信息和退出app
        }
    }

    /**
     * A valid barcode has been found, so give an indication of success and show the results.<br/>
     * （UI层对解码结果的响应）已经发现一个可用的barcode，所以提示用户扫描成功并且给出结果
     * @param rawResult The contents of the barcode.结果对象
     * @param scaleFactor amount by which thumbnail was scaled  缩放比例
     * @param barcode   A greyscale bitmap of the camera data which was decoded.   灰度位图
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        inactivityTimer.onActivity();
        lastResult = rawResult;

        if (barcode != null){
//            显示扫描结果信息(DecodeFormat/Text)
            Toast.makeText(this, "DecodeFormat: " + rawResult.getBarcodeFormat() + "\n" + "Text: " + rawResult.getText(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 调用ViewFinderView的drawViewfinder()方法，重新绘制一遍ViewFinderView控件
     */
    public void drawViewfinder() {
        mViewFinderView.drawViewfinder();
    }

    /**
     * 获取当前屏幕的旋转方向
     * @return
     */
    private int getCurrentOrientation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_90:
                return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            default:
                return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
        }
    }
//    getter方法  start
    public CameraManager getCameraManager() {
        return mCameraManager;
    }
    public CaptureActivityHandler getHandler() {
        return handler;
    }
    public ViewfinderView getViewfinderView() {
        return mViewFinderView;
    }// end
}
