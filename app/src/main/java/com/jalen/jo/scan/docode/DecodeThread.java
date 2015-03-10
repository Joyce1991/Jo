package com.jalen.jo.scan.docode;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.ResultPointCallback;
import com.jalen.jo.scan.CaptureActivity;
import com.jalen.jo.scan.CaptureSettingsActivity;
import com.jalen.jo.scan.DecodeFormatManager;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by jh on 2015/3/10.<br/>
 * 解码线程（这个线程干了所有解码相关的重任务）<br/>
 * <p/>
 * <strong>这个线程用CountDownLatch实现了HandlerThread的效果</strong>
 */
public class DecodeThread extends Thread {
    /**
     * 缩略图
     */
    public static final String BARCODE_BITMAP = "barcode_bitmap";
    /**
     * 缩略图的缩放比例
     */
    public static final String BARCODE_SCALED_FACTOR = "barcode_scaled_factor";

    private CaptureActivity mActivity;
    private Handler mHandler;
    private CountDownLatch handlerInitLatch;
    /**
     * hint用来配置哪些码支持，哪些码不支持
     */
    private Map<DecodeHintType, Object> hints;

    /**
     * DecodeThread构造方法
     *
     * @param mActivity     扫码activity（我这里是CaptureActivity）
     * @param baseHints     默认支持的基础hint集合
     * @param characterSet  设置数据编码格式
     * @param decodeFormats 支持的码格式
     * @param resultPointCallback   扫描识别反馈红点点的callback
     */
    public DecodeThread(CaptureActivity mActivity, Collection<BarcodeFormat> decodeFormats, Map<DecodeHintType, ?> baseHints, String characterSet, ResultPointCallback resultPointCallback) {
        this.mActivity = mActivity;
        if (baseHints != null){
            hints.putAll(baseHints);
        }
        handlerInitLatch = new CountDownLatch(1);

        if (decodeFormats == null || decodeFormats.isEmpty()) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
//            创建一个空的Enum Set，里面的元素限制为类型为BarcodeFormat
            decodeFormats = EnumSet.noneOf(BarcodeFormat.class);
//            读取设置配置，是否支持相关制式的码
            if (prefs.getBoolean(CaptureSettingsActivity.KEY_DECODE_1D_PRODUCT, true)) {
                decodeFormats.addAll(DecodeFormatManager.PRODUCT_FORMATS);
            }
            if (prefs.getBoolean(CaptureSettingsActivity.KEY_DECODE_1D_INDUSTRIAL, true)) {
                decodeFormats.addAll(DecodeFormatManager.INDUSTRIAL_FORMATS);
            }
            if (prefs.getBoolean(CaptureSettingsActivity.KEY_DECODE_QR, true)) {
                decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
            }
            if (prefs.getBoolean(CaptureSettingsActivity.KEY_DECODE_DATA_MATRIX, true)) {
                decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
            }
            if (prefs.getBoolean(CaptureSettingsActivity.KEY_DECODE_AZTEC, false)) {
                decodeFormats.addAll(DecodeFormatManager.AZTEC_FORMATS);
            }
            if (prefs.getBoolean(CaptureSettingsActivity.KEY_DECODE_PDF417, false)) {
                decodeFormats.addAll(DecodeFormatManager.PDF417_FORMATS);
            }
//            这些都是支持的码
            hints.put(DecodeHintType.POSSIBLE_FORMATS, hints);
//            设置数据编码格式
            if (characterSet != null) {
                hints.put(DecodeHintType.CHARACTER_SET, characterSet);
            }
            // 设置扫描反馈红点点callback
            if (resultPointCallback != null){
                hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, resultPointCallback);
            }
            Log.i("DecodeThread", "Hints: " + hints);
        }
    }

    @Override
    public void run() {
        Looper.prepare();
        mHandler = new DecodeHandler(mActivity, hints);
        handlerInitLatch.countDown();
        Looper.loop();
    }

    /**
     * 获取当前线程的Handler
     *
     * @return
     */
    public Handler getHandler() {
        try {
            handlerInitLatch.await();
        } catch (InterruptedException ie) {
            // continue?
        }
        return mHandler;
    }
}
