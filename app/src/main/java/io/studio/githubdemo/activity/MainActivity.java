package io.studio.githubdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GithubAuthProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import io.studio.githubdemo.R;
import io.studio.githubdemo.pref.PrefsHelper;
import io.studio.githubdemo.utils.Constant;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    Button buttonLogin;
    private Context context;
    private SecureRandom random = new SecureRandom();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        //firebase auth listener
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {
                } else {

                    //on success redirect to Dashboard Activity to get users data
                    Intent intent = new Intent();
                    intent.setClass(context, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        mAuth = FirebaseAuth.getInstance();
        //mAuth.signOut();
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);


        //Called after the github server redirect us to REDIRECT_URL_CALLBACK
        Uri data = getIntent().getData();
        if (data != null && data.toString().startsWith(getString(R.string.firebase_web_host_url))) {
            String code = data.getQueryParameter(Constant.CODE);
            String state = data.getQueryParameter(Constant.STATE);
            if (code != null && state != null)
                sendPost(code, state);
        }


    }



    public void checkCurrentUser() {
        // [START check_current_user]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
        } else {
            askGitLogin();
            //askLogin();
        }
        // [END check_current_user]
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.buttonLogin:
                    checkCurrentUser();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void askGitLogin() {

        //https://developer.github.com/apps/building-integrations/setting-up-and-registering-oauth-apps/about-authorization-options-for-oauth-apps/
        //GET http://github.com/login/oauth/authorize
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("github.com")
                .addPathSegment("login")
                .addPathSegment("oauth")
                .addPathSegment("authorize")
                .addQueryParameter("client_id", getString(R.string.github_client_id))
                .addQueryParameter("redirect_uri", getString(R.string.firebase_web_host_url))
                .addQueryParameter("state", getRandomString())
                .addQueryParameter("scope", "user:email")
                .build();

        //print url
        // Log.d(TAG, httpUrl.toString());

        //Call implicit intent to handle url
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(httpUrl.toString()));
        startActivity(intent);
    }


    private String getRandomString() {
        //get Random number
        return new BigInteger(130, random).toString(32);
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }


    private void sendPost(String code, String state) {
        //POST https://github.com/login/oauth/access_token
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            FormBody form = new FormBody.Builder()
                    .add("client_id", getString(R.string.github_client_id))
                    .add("client_secret", getString(R.string.github_client_secret))
                    .add("code", code)
                    .add("redirect_uri", getString(R.string.firebase_web_host_url))
                    .add("state", state)
                    .build();

            Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(form)
                    .build();


            showProgressDialogSimple(getString(R.string.please_wait), true);
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "onFailure: " + e.toString(), Toast.LENGTH_SHORT)
                            .show();
                    hideProgressDialogSimple();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //access_token=e72e16c7e42f292c6912e7710c838347ae178b4a&token_type=bearer
                    String responseBody = response.body().string();
                    String[] splitted = responseBody.split("=|&");
                    if (splitted[0].equalsIgnoreCase("access_token"))
                        signInWithToken(splitted[1]);
                    else {
                        showMessage("splitted[0] =>" + splitted[0]);
                        hideProgressDialogSimple();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void signInWithToken(String token) {

        //save token
        PrefsHelper.getInstance().saveString(context, PrefsHelper.PreferenceKey.token, token);


        //pass token to firebase auth
        AuthCredential credential = GithubAuthProvider.getCredential(token);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        hideProgressDialogSimple();

                        if (!task.isSuccessful()) {
                            task.getException().printStackTrace();
                            showMessage(getString(R.string.auth_failed));
                        }
                    }
                });
    }
}
