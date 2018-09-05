package io.studio.githubdemo.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

/**
 * Created by vis on 17-05-2015.
 */
public class Utility {



    public static void openURL(Context activity, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        activity.startActivity(i);
    }

    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }

}
