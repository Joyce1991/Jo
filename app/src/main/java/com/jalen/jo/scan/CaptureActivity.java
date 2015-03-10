package com.jalen.jo.scan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

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
//    V
    private SurfaceView mSurfaceView;
    private ViewfinderView mViewFinderView;
//    C
    private SurfaceHolder mSurfaceHolder;   // surface <=> surfaceholder <=> surfaceview
    private CaptureActivityHandler handler; // activity <=> handler <=> view
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

        inactivityTimer.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        inactivityTimer.onPause();
//        关闭相机
        mCameraManager.closeDriver();
        if (hasSurface){
            // 移除surface
            mSurfaceHolder.removeCallback(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        inactivityTimer.shutdown();
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
                handler = new CaptureActivityHandler(this, decodeFormats, decodeHints, characterSet, mCameraManager);
            }
        } catch (IOException e) {
            e.printStackTrace();
//                ？显示bug信息和退出app
        }
    }

    /**
     * A valid barcode has been found, so give an indication of success and show the results.<br/>
     * 已经发现一个可用的barcode，所以提示用户扫描成功并且给出结果
     * @param rawResult The contents of the barcode.结果对象
     * @param scaleFactor amount by which thumbnail was scaled  缩放比例
     * @param barcode   A greyscale bitmap of the camera data which was decoded.   灰度位图
     */
/*    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        inactivityTimer.onActivity();
        lastResult = rawResult;
        ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(this, rawResult);

        boolean fromLiveScan = barcode != null;
        if (fromLiveScan) {
            historyManager.addHistoryItem(rawResult, resultHandler);
            // Then not from history, so beep/vibrate and we have an image to draw on
            beepManager.playBeepSoundAndVibrate();
            drawResultPoints(barcode, scaleFactor, rawResult);
        }

        switch (source) {
            case NATIVE_APP_INTENT:
            case PRODUCT_SEARCH_LINK:
                handleDecodeExternally(rawResult, resultHandler, barcode);
                break;
            case ZXING_LINK:
                if (scanFromWebPageManager == null || !scanFromWebPageManager.isScanFromWebPage()) {
                    handleDecodeInternally(rawResult, resultHandler, barcode);
                } else {
                    handleDecodeExternally(rawResult, resultHandler, barcode);
                }
                break;
            case NONE:
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                if (fromLiveScan && prefs.getBoolean(PreferencesActivity.KEY_BULK_MODE, false)) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.msg_bulk_mode_scanned) + " (" + rawResult.getText() + ')',
                            Toast.LENGTH_SHORT).show();
                    // Wait a moment or else it will scan the same barcode continuously about 3 times
                    restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
                } else {
                    handleDecodeInternally(rawResult, resultHandler, barcode);
                }
                break;
        }
    }*/

    /**
     * 调用ViewFinderView的drawViewfinder()方法，重新绘制一遍ViewFinderView控件
     */
    public void drawViewfinder() {
        mViewFinderView.drawViewfinder();
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
