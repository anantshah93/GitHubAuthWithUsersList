package io.studio.githubdemo.activity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import io.studio.githubdemo.retrofit.api.ApiClient;
import retrofit2.Call;


/**
 * Created by acer 4745 on 20-11-2016.
 */

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    public ProgressDialog progressDialog;
    Toolbar toolbar;
    ApiClient apiClient;
    EditText edit_text_search_toolbar;
    ImageView image_view_cross_search_toolbar;
    ProgressBar progress_bar_toolbar;
    boolean isLocationEnabled;
    String className;
    int isFirstTime = 0;
    TextView textView_toolbar_title;
    TextView text_view_toolbar_secondary;
    ImageView image_view_secondary;
    DecimalFormat decimalFormat;
    String orderType;
    GoogleApiClient mGoogleApiClient;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        className = this.getClass().getSimpleName();
        decimalFormat = new DecimalFormat("#.##");
        apiClient = ApiClient.getInstance();

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home: // default back
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    //show toast message
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }


    public void setRecyclerViewWithAdapter(RecyclerView recyclerView,
            RecyclerView.LayoutManager layoutManager,
            RecyclerView.Adapter<RecyclerView.ViewHolder> recyclerAdapter,
            boolean isNestedScrollingEnabled) {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(isNestedScrollingEnabled);
        recyclerView.setAdapter(recyclerAdapter);

    }


    public void showProgressDialogSimple(String msg, Boolean isShow) {
        try {
            if (progressDialog != null &&
                    progressDialog.isShowing())
                progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(msg);

        if (isShow)
            progressDialog.show();
        else
            progressDialog.dismiss();

    }

    public void hideProgressDialogSimple() {
        progressDialog.dismiss();
    }


    public String buildSharedPreferenceKey(Call<Object> api, Map<String, String> inputMap) {
        String preferenceKeyGenerated = "";
        try {
            try {
                inputMap.values().removeAll(Collections.singleton(null));
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            Log.e("jsonString ", "" + jsonString);
            preferenceKeyGenerated = api.request().url().toString() + "-" + jsonString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return preferenceKeyGenerated;
    }

    public String buildSharedPreferenceKeyList(Call<ArrayList<Object>> api, Map<String, String> inputMap) {
        String preferenceKeyGenerated = "";
        try {
            try {
                inputMap.values().removeAll(Collections.singleton(null));
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            Log.e("jsonString ", "" + jsonString);
            preferenceKeyGenerated = api.request().url().toString() + "-" + jsonString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return preferenceKeyGenerated;
    }

    public String buildSharedPreferenceKeyObj(Call<Object> api, Map<String, Object> inputMap) {
        String preferenceKeyGenerated = "";
        try {
            try {
                inputMap.values().removeAll(Collections.singleton(null));
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            Log.e("jsonString ", "" + jsonString);
            preferenceKeyGenerated = api.request().url().toString() + "-" + jsonString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return preferenceKeyGenerated;
    }



}
