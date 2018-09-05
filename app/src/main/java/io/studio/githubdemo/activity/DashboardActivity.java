package io.studio.githubdemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

import io.studio.githubdemo.R;
import io.studio.githubdemo.adapter.GitHubUserRecyclerAdapter;
import io.studio.githubdemo.retrofit.api.ApiCallback;
import io.studio.githubdemo.retrofit.api.GitHubUsers;
import io.studio.githubdemo.retrofit.request.RetrofitRequest;
import io.studio.githubdemo.retrofit.response.GitHubUser;
import io.studio.githubdemo.utils.NetworkUtil;
import io.studio.githubdemo.utils.Utility;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import retrofit2.Call;

public class DashboardActivity extends BaseActivity implements GitHubUserRecyclerAdapter.ItemClickedListener {

    private static final String TAG = "DashboardActivity";
    RecyclerView recyclerViewUsers;
    EditText editTextSearch;
    TextView textViewNoData;
    private Context context;
    private int page = 1;
    private int limitPerPage = 100;
    ArrayList<GitHubUser> airLinesDataAllArrayList;
    GitHubUserRecyclerAdapter gitHubUserRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initView();
        bindEvent();
        setData();


    }

    private void initView() {
        context = this;
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        editTextSearch = findViewById(R.id.editTextSearch);
        textViewNoData = findViewById(R.id.textViewNoData);

    }

    @Override
    public void onItemClicked(GitHubUser countryCode) {

    }

    @Override
    public void noResultFound(int size) {
        if (size == 0) {
            if (page < 3) {
                loadGitHubUsersList(true, true);
            } else {
                showMessage(getString(R.string.two_hundered_limit_over));
                recyclerViewUsers.setVisibility(View.GONE);
                textViewNoData.setVisibility(View.VISIBLE);
            }
        } else {
            recyclerViewUsers.setVisibility(View.VISIBLE);
            textViewNoData.setVisibility(View.GONE);
        }
    }

    private void bindEvent() {
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    gitHubUserRecyclerAdapter.getFilter().filter(s.toString().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setData() {
        airLinesDataAllArrayList = new ArrayList<>();

        gitHubUserRecyclerAdapter =
                new GitHubUserRecyclerAdapter(context, airLinesDataAllArrayList, this);

        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewUsers.setNestedScrollingEnabled(false);

        //set animation to recyclerview
        SlideInBottomAnimationAdapter alphaAdapter =
                new SlideInBottomAnimationAdapter(gitHubUserRecyclerAdapter);
        alphaAdapter.setDuration(1000);
        alphaAdapter.setFirstOnly(true);
        recyclerViewUsers.setAdapter(alphaAdapter);
        recyclerViewUsers.setHasFixedSize(true);


        //get github users list
        loadGitHubUsersList(true, true);


    }


    private Map<String, String> buildDictionary() {
        return RetrofitRequest.getGitHubUsers(String.valueOf(page),
                String.valueOf(limitPerPage));
    }

    @SuppressWarnings("unchecked")
    private void loadGitHubUsersList(final boolean isShow,
            final boolean isGetDataFromCache) {

        if (NetworkUtil.checkNetworkStatus(context)) {
            showProgressDialogSimple(getString(R.string.loading_github_user_data), isShow);

            GitHubUsers aertrip = apiClient.getApi();
            Call<ArrayList<Object>> call =
                    aertrip.getUsersList(buildDictionary());
            NetworkUtil.callApiList(call, new ApiCallback(context, isGetDataFromCache,
                    buildSharedPreferenceKeyList(call, buildDictionary()),
                    new ArrayList<GitHubUser>(), call) {

                @Override
                protected void onError(String error, int errorCode) {
                    showMessage(error);
                    hideProgressDialogSimple();
                }

                @Override
                protected void onSuccess(Call call, final Object response) {
                    handleResponse(isShow, response);
                    hideProgressDialogSimple();
                }
            });
        } else {
            showMessage(getString(R.string.no_internet_connection));
        }
    }

    @SuppressWarnings("unchecked")
    private void handleResponse(boolean isShow, Object response) {
        try {

            ArrayList<GitHubUser> users = (ArrayList<GitHubUser>) response;
            ArrayList<GitHubUser> usersLocal = new ArrayList<>();
            usersLocal.addAll(Utility.stringToArray(new Gson().toJson(users), GitHubUser[].class));

            if (usersLocal.size() > 0) {

                for (int i = 0; i < usersLocal.size(); i++) {
                    GitHubUser gitHubUser = usersLocal.get(i);
                    airLinesDataAllArrayList.add(gitHubUser);
                    gitHubUserRecyclerAdapter
                            .notifyItemInserted(airLinesDataAllArrayList.size() - 1);
                }
                gitHubUserRecyclerAdapter.swap(airLinesDataAllArrayList);
                textViewNoData.setVisibility(View.GONE);
                recyclerViewUsers.setVisibility(View.VISIBLE);

                page++;

            } else {
                recyclerViewUsers.setVisibility(View.GONE);
                textViewNoData.setVisibility(View.VISIBLE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
