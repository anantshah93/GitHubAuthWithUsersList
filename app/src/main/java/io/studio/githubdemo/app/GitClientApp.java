package io.studio.githubdemo.app;

import android.app.Application;
import android.content.Context;

public class GitClientApp extends Application {
	private static Context context;
	
    @Override
    public void onCreate() {
        super.onCreate();
        GitClientApp.context = this;

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        System.gc();
    }
}