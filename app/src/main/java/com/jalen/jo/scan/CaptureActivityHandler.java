package com.jalen.jo.scan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.jalen.jo.R;
import com.jalen.jo.scan.docode.DecodeThread;

import java.util.Collection;
import java.util.Map;

/**
 * Created by jh on 2015/3/9.
 * 这个类处理所有capture传递的状态机
 */
public class CaptureActivityHandler extends Handler {
    private static final String TAG = CaptureActivityHandler.class.getSimpleName();

    private CaptureActivity activity;
    private CameraManager cameraManager;
    private DecodeThread decodeThread;  // 解码线程
    private State mState;   // 扫描状态

    private enum State {
        /**
         * 捕获
         */
        PREVIEW,
        /**
         * 捕获成功
         */
        SUCCESS,
        /**
         * 解码完成
         */
        DONE
    }

    /**
     * CaptureActivityHandler的构造方法
     * @param activity  对应的activity（CaptureActivity）
     * @param decodeFormats 码格式集合
     * @param baseHints     解码支持默认设置
     * @param characterSet  码内容编码
     * @param cameraManager CameraManager
     */
    CaptureActivityHandler(CaptureActivity activity, Collection<BarcodeFormat> decodeFormats,
                           Map<DecodeHintType, ?> baseHints,
                           String characterSet, CameraManager cameraManager) {
        this.activity = activity;
        this.cameraManager = cameraManager;
//        开启解码线程等待消息
        decodeThread = new DecodeThread(activity, decodeFormats, baseHints, characterSet,
                new ViewfinderResultPointCallback(activity.getViewfinderView()));
        decodeThread.start();

        // 开始捕获图像
        cameraManager.startPreview();
        restartPreviewAndDecode();
    }


    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.decode_succeeded:
                // 解码成功
                mState = State.SUCCESS;
                Bundle bundle = msg.getData();
                Bitmap barcode = null;
                float scaleFactor = 1.0f;
                if (bundle != null) {
                    // 压缩过的缩略图二进制数据
                    byte[] compressedBitmap = bundle.getByteArray(DecodeThread.BARCODE_BITMAP);
                    if (compressedBitmap != null) {
                        // 生成Bitmap
                        barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
                        // Mutable copy:
                        barcode = barcode.copy(Bitmap.Config.ARGB_8888, true);
                    }
                    // 缩放比例
                    scaleFactor = bundle.getFloat(DecodeThread.BARCODE_SCALED_FACTOR);
                }
                // 处理解析结果
//                activity.handleDecode((Result) msg.obj, barcode, scaleFactor);
                Result result = (Result) msg.obj;
                Toast.makeText(activity, "result: " + "barcodeformat: " + result.getBarcodeFormat()
                        + " text: " + result.getText(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.decode_failed:
                // 解码失败,设置状态为PREVIEW，重新获取预览图并解码
                mState = State.PREVIEW;
                cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
                break;
        }
        super.handleMessage(msg);
    }

    /**
     * 重新开始扫描和解码
     */
    private void restartPreviewAndDecode() {
        if (mState == State.SUCCESS) {
            mState = State.PREVIEW;     // 标识为扫描状态
            // 扫描获取图片成功，把
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
            // 通知ViewFinderView重新绘制一遍
            activity.drawViewfinder();
        }
    }
}
