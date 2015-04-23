package com.jalen.jo;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.SaveCallback;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by jh on 2015/2/28.
 *
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

        initImageLoader(getApplicationContext());

        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            public void done(AVException e) {
                if (e == null) {
                    // 保存成功
                    String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                    // 关联  installationId 到用户表等操作……
                } else {
                    // 保存失败，输出错误信息
                }
            }
        });
    }

    /**
     * 初始化{@link com.nostra13.universalimageloader.core.ImageLoader}的全局配置
     * @param context Application上下文
     */
    private void initImageLoader(Context context) {
        // ImageLoader全局配置
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);    // NORM_PRIORITY = 5,所以这里的线程优先级别为3
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());  // 图片缓存到硬盘时的文件名称生成方式
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB（硬盘缓存空间大小）
        config.tasksProcessingOrder(QueueProcessingType.LIFO);  // 加载和显示图片队列的处理方式（Last In First Out）
        config.writeDebugLogs(); // Remove for release app（打印图片加载过程中的log）
        config.threadPoolSize(5);   // 设置线程池大小

        // 用config来初始化ImageLoader
        ImageLoader.getInstance().init(config.build());
    }
}
