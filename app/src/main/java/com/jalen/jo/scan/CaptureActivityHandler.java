package com.jalen.jo.scan;

import android.os.Handler;
import android.os.Message;

/**
 * Created by jh on 2015/3/9.
 * 这个类处理所有capture传递的状态机
 */
public class CaptureActivityHandler extends Handler {
    private static final String TAG = CaptureActivityHandler.class.getSimpleName();

    private CaptureActivity activity;

    CaptureActivityHandler(CaptureActivity activity){
        this.activity = activity;
    }


    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){

        }
        super.handleMessage(msg);
    }
}
