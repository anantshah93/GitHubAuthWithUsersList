package io.studio.githubdemo.retrofit.api;


import java.util.concurrent.TimeUnit;

import io.studio.githubdemo.utils.Constant;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static ApiClient uniqInstance;
    private final String URL_SANDBOX = Constant.API.BASEURL_API;
    private GitHubUsers apiAertrip;

    public static synchronized ApiClient getInstance() {
        if (uniqInstance == null) {
            uniqInstance = new ApiClient();
        }
        return uniqInstance;
    }

    private void ApiClient() {
        try {

            /**
             * retrofit 2.0 Code
             */
            {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                // set your desired log level
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient httpClient = new OkHttpClient.Builder()
                        .readTimeout(360, TimeUnit.SECONDS)
                        .connectTimeout(360, TimeUnit.SECONDS)
                        .addInterceptor(logging)
                        .build();

                // <-- this is the important line!
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL_SANDBOX)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient)
                        .build();

                apiAertrip = retrofit.create(GitHubUsers.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GitHubUsers getApi() {
        if (uniqInstance == null) {
            getInstance();
        }
        uniqInstance.ApiClient();
        return apiAertrip;
    }



}
