package com.jalen.jo.scan.docode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.jalen.jo.R;
import com.jalen.jo.scan.CaptureActivity;

import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * Created by jh on 2015/3/10.
 * 解码的消息传递、消息处理
 */
public class DecodeHandler extends Handler {
    private static final String TAG = DecodeHandler.class.getSimpleName();

    private final CaptureActivity mActivity;
    private final MultiFormatReader multiFormatReader;
    private boolean running = true;

    DecodeHandler(CaptureActivity activity, Map<DecodeHintType,Object> hints) {
        multiFormatReader = new MultiFormatReader();
        // 设置自定义解码偏好
        multiFormatReader.setHints(hints);
        this.mActivity = activity;
    }

    @Override
    public void handleMessage(Message message) {
        if (!running) {
            return;
        }
        switch (message.what) {
            case R.id.decode:
                decode((byte[]) message.obj, message.arg1, message.arg2);
                break;
            case R.id.quit:
                running = false;
                Looper.myLooper().quit();
                break;
        }
    }

    /**
     * Decode the data within the viewfinder rectangle, and time how long it took. For efficiency,
     * reuse the same reader objects from one decode to the next.   <br/>
     * 解析在viewfinder矩形框内的图像数据，
     * @param data   The YUV preview frame.
     * @param width  The width of the preview frame.
     * @param height The height of the preview frame.
     */
    private void decode(byte[] data, int width, int height) {
        long start = System.currentTimeMillis();
        Result rawResult = null;
//        LuminanceSource的子类，封装的图像元数据               ==> 第一次封装
        PlanarYUVLuminanceSource source = mActivity.getCameraManager().buildLuminanceSource(data, width, height);
        if (source != null) {
//            用图像元数据生成二进制Bitmap                      ==> 第二次封装
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
//                用MultiFormatReader解析这个二进制Bitmap，等待结果对象    ==> 第三次封装
                rawResult = multiFormatReader.decodeWithState(bitmap);
            } catch (ReaderException re) {
                // continue
            } finally {
                multiFormatReader.reset();
            }
        }

//        发送消息到CaptureActivityHandler,如果成功（what=decode_succeeded, object=rawResult）,如果失败（what=decode_failed, object=null）
        Handler handler = mActivity.getHandler();   // 获取CaptureActivity线程的Handler-->CaptureActivityHandler
        if (rawResult != null) {
            // 解析成功
            // Don't log the barcode contents for security.
            long end = System.currentTimeMillis();
            Log.d(TAG, "Found barcode in " + (end - start) + " ms");
            if (handler != null) {
                Message message = Message.obtain(handler, R.id.decode_succeeded, rawResult);
                //  把缩略图也携带上
                Bundle bundle = new Bundle();
                bundleThumbnail(source, bundle);
                message.setData(bundle);
                message.sendToTarget();
            }
        } else {
            // 解析失败
            if (handler != null) {
                Message message = Message.obtain(handler, R.id.decode_failed);
                message.sendToTarget();
            }
        }
    }

    /**
     * 绑定缩略图数据：把图像元数据生成缩略图绑定到bundle中
     * @param source    图像元数据
     * @param bundle    bundle
     */
    private static void bundleThumbnail(PlanarYUVLuminanceSource source, Bundle bundle) {
        int[] pixels = source.renderThumbnail();    // 缩略图数据
        int width = source.getThumbnailWidth(); // 缩略图宽
        int height = source.getThumbnailHeight();   // 缩略图高
        // 用图像元数据生成缩略图
        Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
        bundle.putByteArray(DecodeThread.BARCODE_BITMAP, out.toByteArray());
        bundle.putFloat(DecodeThread.BARCODE_SCALED_FACTOR, (float) width / source.getWidth());
    }
}
