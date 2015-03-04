package com.jalen.jo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.jalen.jo.activities.BaseActivity;

import java.util.Stack;

/**
 * Created by jh on 2015/2/27.<br/>
 * 用一个堆栈来管理activity
 * @see <a href="http://my.oschina.net/kymjs/blog/210805">android应用框架搭建------AppManager</a>
 */
public class JoActivityManager {
    private static Stack<BaseActivity> mStack;
    private static JoActivityManager mInstance;

    private JoActivityManager() {
    }
    /**
     * 单实例 , UI无需考虑多线程同步问题
     */
    public static JoActivityManager getAppManager() {
        if (mInstance == null) {
            mInstance = new JoActivityManager();
        }
        return mInstance;
    }

    /**
     * 添加Activity到栈
     */
    public void addActivity(BaseActivity activity) {
        if (mStack == null) {
            mStack = new Stack<BaseActivity>();
        }
        mStack.add(activity);
    }

    /**
     * 获取当前Activity（栈顶Activity）
     */
    public BaseActivity getCurrentActivity() {
        if (mStack == null || mStack.isEmpty()) {
            return null;
        }
        BaseActivity activity = mStack.lastElement();
        return activity;
    }

    /**
     * 获取当前Activity（栈顶Activity） 没有找到则返回null
     */
    public BaseActivity findActivity(Class<?> cls) {
        BaseActivity activity = null;
        for (BaseActivity aty : mStack) {
            if (aty.getClass().equals(cls)) {
                activity = aty;
                break;
            }
        }
        return activity;
    }

    /**
     * 结束当前Activity（栈顶Activity）
     */
    public void finishActivity() {
        BaseActivity activity = mStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            mStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Class<?> cls) {
        for (BaseActivity activity : mStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     *
     * @param cls
     */
    public void finishOthersActivity(Class<?> cls) {
        for (BaseActivity activity : mStack) {
            if (!(activity.getClass().equals(cls))) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = mStack.size(); i < size; i++) {
            if (null != mStack.get(i)) {
                mStack.get(i).finish();
            }
        }
        mStack.clear();
    }

    /**
     * 应用程序退出
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            System.exit(0);
        }
    }
}
