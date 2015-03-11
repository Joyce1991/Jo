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
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_capture_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_capture_settings, container, false);
            return rootView;
        }
    }
}
