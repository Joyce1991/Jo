package com.jalen.jo.scan;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.jalen.jo.R;
import com.jalen.jo.activities.BaseActivity;
import com.jalen.jo.fragments.CapturePreferencesFragment;

public class CapturePreferencesActivity extends BaseActivity {

    public static final String KEY_AUTO_FOCUS = "preferences_auto_focus";   // 自动对焦
    public static final String KEY_PLAY_BEEP = "preferences_play_beep";     // 蜂铃
    public static final String KEY_VIBRATE = "preferences_vibrate";         // 震动
    public static final String KEY_FRONT_LIGHT_MODE = "preferences_front_light_mode";   // 闪光灯模式[ON|AUTO|OFF]
    public static final String KEY_DISABLE_EXPOSURE = "preferences_disable_exposure";   // 是否可以触摸
    public static final String KEY_DISABLE_AUTO_ORIENTATION = "preferences_orientation";    // 是否自动横竖屏切换

    public static final String KEY_DECODE_1D_PRODUCT = "preferences_decode_1D_product";     // 1D 商品条码
    public static final String KEY_DECODE_1D_INDUSTRIAL = "preferences_decode_1D_industrial";   // 1D 工业码
    public static final String KEY_DECODE_QR = "preferences_decode_QR";     // QR二维码
    public static final String KEY_DECODE_DATA_MATRIX = "preferences_decode_Data_Matrix";   //  Matrix矩阵码
    public static final String KEY_DECODE_AZTEC = "preferences_decode_Aztec";
    public static final String KEY_DECODE_PDF417 = "preferences_decode_PDF417";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_settings);
        if (savedInstanceState == null) {
            // Display the fragment as the main content.
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new CapturePreferencesFragment())
                    .commit();
        }

    }
}
