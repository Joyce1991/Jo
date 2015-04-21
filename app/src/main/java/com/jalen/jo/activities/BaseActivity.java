package com.jalen.jo.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.jalen.jo.R;
import com.jalen.jo.fragments.AccountEditFragment;
import com.jalen.jo.fragments.BaseFragment;
import com.jalen.jo.fragments.NicknameFragment;
import com.jalen.jo.library.LibraryBookDisplayFragment;

/**
 * Created by jh on 2015/2/27.  <br/>
 * Activity基类
 * @see <a href="http://my.oschina.net/kymjs/blog/206178">android应用框架搭建------BaseActivity</a>
 */
public class BaseActivity extends ActionBarActivity {


    public String tag;

    public BaseActivity(){
        super();
        tag = this.getClass().getSimpleName();
    }

    /**************************************************************************
     *  根据id创建fragment
     * @param mFragmentID 标识要启动的fragment的id号
     * @param params    需要传递给fragment的{@link java.lang.String}类型的参数
     * @return
     **************************************************************************/
    public BaseFragment createFragmentById(int mFragmentID, String... params){
        BaseFragment fragment = null;
        switch (mFragmentID){
            case R.id.fragment_nickname:
                fragment = NicknameFragment.newInstance();
                break;
            case R.id.fragment_accountedit:
                fragment = AccountEditFragment.newInstance(params[0], params[1]);
                break;
            case R.id.fragment_library_bookdisplay:
                fragment = LibraryBookDisplayFragment.newInstance(params[0]);
                break;
        }
        return fragment;
    }
    /***************************************************************************
     * 打印Activity生命周期
     ***************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(tag, "---------onCreat ");
        // 竖屏锁定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(tag, "---------onStart ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(tag, "---------onResume ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(tag, "---------onStop ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(tag, "---------onPause ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(tag, "---------onRestart ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(tag, "---------onDestroy ");
//        JoActivityManager.getAppManager().finishActivity(this);

    }

    /**
     * Get the method name for a depth in call stack. <br />
     * Utility function
     * @param depth depth in the call stack (0 means current method, 1 means call method, ...)
     * @return method name
     */
    public static String getMethodName(final int depth)
    {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        //System. out.println(ste[ste.length-depth].getClassName()+"#"+ste[ste.length-depth].getMethodName());
        // return ste[ste.length - depth].getMethodName();  //Wrong, fails for depth = 0
        return ste[ste.length - 1 - depth].getMethodName(); //Thank you Tom Tresansky
    }
}
