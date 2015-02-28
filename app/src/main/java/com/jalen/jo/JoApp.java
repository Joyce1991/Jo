package com.jalen.jo;

import android.app.Application;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;

/**
 * Created by jh on 2015/2/28.
 */
public class JoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化leanCloud,设置应用的 Application ID 和 Key
        AVOSCloud.initialize(
                this,
                "lgt86x4nela39ip0w9sual23hwubpgp1d5qhcl7k3jbkl9hv",
                "ujnmqih2olni35gb7774ocd4jdosvbd0s725gdzjhoaqpx4z");

        AVAnalytics.enableCrashReport(this, true);
    }
}
