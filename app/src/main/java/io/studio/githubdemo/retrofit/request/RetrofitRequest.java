package io.studio.githubdemo.retrofit.request;

import java.util.HashMap;
import java.util.Map;

public class RetrofitRequest {

    /**
     * Static variables which is being used as APIs params
     */
    public static final String ACTION_PAGE = "page";
    public static final String ACTION_PER_PAGE = "per_page";


    /**
     * get Signup params to hit Api
     *
     * @param page
     * @param perPage
     * @return
     */
    public static Map<String, String> getGitHubUsers(final String page, final String perPage) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_PAGE, page);
        params.put(ACTION_PER_PAGE, perPage);
        return params;
    }


}