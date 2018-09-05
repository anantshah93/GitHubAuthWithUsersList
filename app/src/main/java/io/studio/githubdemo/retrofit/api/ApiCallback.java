package io.studio.githubdemo.retrofit.api;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import io.studio.githubdemo.pref.PrefsHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anantshah on 07/03/16.
 */
public abstract class ApiCallback<S> implements Callback {
    private Context activity;
    private boolean isGetDataFromSavedPreferences = false;
    private String preferenceKey = "";
    private Gson gson;
    private int errorCode = 0;
    private int isShowTitle = 0;

    public ApiCallback(Activity activity) {
        this.activity = activity;
    }


    public ApiCallback(Context context, boolean isGetDataFromSavedPreferences,
            String preferenceKey, Object object, String defaultValue, Call<Object> call) {

        this.activity = context;
        this.preferenceKey = preferenceKey;
        this.isGetDataFromSavedPreferences = isGetDataFromSavedPreferences;
        gson = new Gson();
        if (isGetDataFromSavedPreferences) {

            String getSavedResponse =
                    PrefsHelper.getInstance().getString(context, preferenceKey, defaultValue);

            if (getSavedResponse != null &&
                    getSavedResponse.length() > 0) {

                object = gson.fromJson(getSavedResponse, object.getClass());
                if (object != null) {
                    // call.cancel();
                    Log.e("getSavedResponse ", ": " + getSavedResponse);
                    onSuccess(call, object);
                } else {
                    this.isGetDataFromSavedPreferences = false;
                }
            } else {
                this.isGetDataFromSavedPreferences = false;
            }


        } else {
            this.isGetDataFromSavedPreferences = false;
        }

    }

    public ApiCallback(Context context, boolean isGetDataFromSavedPreferences,
            String preferenceKey, ArrayList<Object> object, Call<ArrayList<Object>> call) {

        this.activity = context;
        this.preferenceKey = preferenceKey;
        this.isGetDataFromSavedPreferences = isGetDataFromSavedPreferences;
        gson = new Gson();
        if (isGetDataFromSavedPreferences) {

            String getSavedResponse =
                    PrefsHelper.getInstance().getString(context, preferenceKey, "");

            if (getSavedResponse != null &&
                    getSavedResponse.length() > 0) {

                object = gson.fromJson(getSavedResponse,
                        new TypeToken<ArrayList<Object>>() {
                        }.getType());


                if (object != null) {
                    // call.cancel();
                    Log.e("getSavedResponse ", ": " + getSavedResponse);
                    onSuccess(call, object);
                } else {
                    this.isGetDataFromSavedPreferences = false;
                }
            } else {
                this.isGetDataFromSavedPreferences = false;
            }


        } else {
            this.isGetDataFromSavedPreferences = false;
        }

    }


    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(Call call, Response response) {

        String code = String.valueOf(response.code());
        //Log.e("code", ":" + code);
        if (code.substring(0, 2).contains("50")) {
            errorCode = 3; //500 error
            onError("Oops, Something went wrong", errorCode);
        } else {
            ArrayList<Object> obj = (ArrayList<Object>) response.body();
            if (obj != null) {


                //save Data
                String result_json = gson.toJson(obj);

                PrefsHelper.getInstance().saveString(activity, preferenceKey, result_json);
                if (!isGetDataFromSavedPreferences) {
                    onSuccess(call, obj);
                }
            } else {
                errorCode = 4; // response is null
                onError("Oops, Something went wrong", errorCode);
            }
        }

    }

    @Override
    public void onFailure(Call call, Throwable t) {

        try {
            Log.e("onFailure", "" + t.getMessage());
            t.printStackTrace();
            if (t.getMessage() != null &&
                    t.getMessage().contains("Unable to resolve host")) {
                //Log.e("onFailure", ":" + t.getMessage());
                errorCode = 1; // no internet connection then show the no internet dialog
            } else if (t.getMessage() != null &&
                    t.getMessage().contains("Unable to resolve host")) {
                //Log.e("onFailure", ":" + t.getMessage());
                errorCode =
                        2; // no internet connection and there is saved data then dont do anything
            } else {
                errorCode = 0;// show message
            }


            Log.e("t", ": " + t.getMessage());
            if (t.getMessage() != null &&
                    (t.getMessage().equalsIgnoreCase("Canceled") ||
                            t.getMessage().equalsIgnoreCase("Socket closed"))) {
                //onError("Oops, Something went wrong", errorCode);
            } else {
                onError("Oops, Something went wrong", errorCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            onError("Oops, Something went wrong", errorCode);

        }
    }

    protected void updateTitle(Call call, Object object) {
    }

    protected abstract void onError(String error, int errorCode);

    protected abstract void onSuccess(Call call, Object response);
}
