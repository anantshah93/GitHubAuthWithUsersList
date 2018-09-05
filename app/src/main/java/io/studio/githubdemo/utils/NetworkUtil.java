package io.studio.githubdemo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class NetworkUtil {

	private NetworkUtil() {
	}

	/**
	 * Check if network is available or not.
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNetworkStatus(Context context) {
		boolean status = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
		for (NetworkInfo tempNetworkInfo : networkInfos) {

			if (tempNetworkInfo.isConnected()) {
				status = true;
				break;
			}
		}
		return status;
	}

	public static <T extends Object> void callApi(Call<T> call, Callback<T> callback) {
		call.enqueue(callback);
	}
	public static <T extends Object> void callApiList(Call<ArrayList<T>> call, Callback<ArrayList<T>> callback) {
		call.enqueue(callback);
	}

}
