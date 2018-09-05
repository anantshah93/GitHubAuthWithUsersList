package io.studio.githubdemo.retrofit.api;


import java.util.ArrayList;
import java.util.Map;

import io.studio.githubdemo.retrofit.response.GitHubUser;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by MotoBeans on 12/16/2015.
 */
public interface GitHubUsers<T> {

    /**
     * Description :
     *
     * @param options
     * @return
     */



    @GET("/users")
    Call<ArrayList<GitHubUser>> getUsersList(@QueryMap Map<String, String> options);



}
