package io.studio.githubdemo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.studio.githubdemo.R;
import io.studio.githubdemo.adapter.viewholders.GitHubUserViewHolders;
import io.studio.githubdemo.adapter.viewholders.LoadingViewHolder;
import io.studio.githubdemo.retrofit.response.GitHubUser;
import io.studio.githubdemo.utils.Utility;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Anant Shah on 24/04/15.
 */
public class GitHubUserRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private Context context;
    private ArrayList<GitHubUser> dataArrayList;
    private ArrayList<GitHubUser> dataFilteredArrayList;
    private boolean isErrorView = false;
    private final int TYPE_ERROR = -1;
    private final int TYPE_CELL = 1;
    private final int VIEW_TYPE_LOADING = 2;
    ItemClickedListener itemClickedListener;

    public void setAndShowError() {
        isErrorView = true;
        notifyDataSetChanged();
    }


    public void add(GitHubUser gitHubUser, int position) {
        dataFilteredArrayList.add(position, gitHubUser);
        dataArrayList.add(position, gitHubUser);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, dataFilteredArrayList.size());

    }

    public GitHubUserRecyclerAdapter(Context context,
            ArrayList<GitHubUser> dataArrayList, ItemClickedListener itemClickedListener) {
        this.context = context;
        this.itemClickedListener = itemClickedListener;
        this.dataFilteredArrayList = dataArrayList;
    }

    public GitHubUserRecyclerAdapter(Context context,
            ArrayList<GitHubUser> dataArrayList) {
        this.context = context;
        this.dataFilteredArrayList = dataArrayList;
    }


    @Override
    public int getItemViewType(int position) {
        switch (position) {
            default: {
                try {
                    if (isErrorView) {
                        return TYPE_ERROR;
                    } else {
                        return dataFilteredArrayList.get(position) == null ? VIEW_TYPE_LOADING :
                                TYPE_CELL;
                    }
                } catch (Exception e) {
                    return TYPE_CELL;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        int size = dataFilteredArrayList != null ? dataFilteredArrayList.size() : 1;


        if (isErrorView) {
            size = 1;
        }
        return size;
    }

    public void swap(ArrayList<GitHubUser> dataArrayList) {
        try {
            this.dataArrayList = dataArrayList;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_user, parent, false);
                GitHubUserViewHolders
                        viewHolder = new GitHubUserViewHolders(view, context);

                return viewHolder;
            }
            case VIEW_TYPE_LOADING: {
                view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.layout_loading_item, parent, false);
                return new LoadingViewHolder(view, context);
            }


        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {

            case TYPE_CELL: {
                GitHubUserViewHolders mVHolder = (GitHubUserViewHolders) holder;
                populateAdapterView(mVHolder, position);
                break;
            }
            case VIEW_TYPE_LOADING: {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dataFilteredArrayList = dataArrayList;
                } else {
                    ArrayList<GitHubUser> filteredList = new ArrayList<>();
                    for (GitHubUser row : dataFilteredArrayList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getLogin().toLowerCase()
                                .contains(charString.toLowerCase()) ||
                                row.getLogin().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    dataFilteredArrayList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataFilteredArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataFilteredArrayList = (ArrayList<GitHubUser>) filterResults.values;
                itemClickedListener.noResultFound(dataFilteredArrayList.size());
                notifyDataSetChanged();
            }
        };
    }

    private GitHubUser getItem(final int position) {
        return dataFilteredArrayList.get(position);
    }

    @SuppressLint("DefaultLocale")
    private void populateAdapterView(GitHubUserViewHolders viewHolders, final int position) {
        try {
            GitHubUser gitHubUser = getItem(position);
            if (gitHubUser != null) {
                if (!TextUtils.isEmpty(gitHubUser.getAvatar_url())) {
                    Picasso.with(context)
                            .load(gitHubUser.getAvatar_url())
                            .fit().centerCrop()
                            .transform(new RoundedCornersTransformation(5, 0))
                            .into(viewHolders.imageViewUser);
                    viewHolders.imageViewUser.setVisibility(View.VISIBLE);
                } else {
                    viewHolders.imageViewUser.setVisibility(View.INVISIBLE);
                }


                viewHolders.textViewTitle.setText(gitHubUser.getLogin());
                viewHolders.textViewType.setText(gitHubUser.getType());
                viewHolders.textViewRepos
                        .setOnClickListener(new ClickHandler(viewHolders, position));
                viewHolders.textViewProfile
                        .setOnClickListener(new ClickHandler(viewHolders, position));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class ClickHandler implements View.OnClickListener {

        int position;
        GitHubUserViewHolders viewHolders;

        public ClickHandler(GitHubUserViewHolders viewHolders,
                int position) {
            this.position = position;
            this.viewHolders = viewHolders;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.textViewProfile:
                    Log.e("getHtml_url", ":" + dataArrayList.get(position).getHtml_url());
                    Utility.openURL(context, dataArrayList.get(position).getHtml_url());

                case R.id.textViewRepos:
                    Utility.openURL(context,
                            dataArrayList.get(position).getHtml_url() + "?tab=repositories");
                    break;
            }
        }
    }


    public interface ItemClickedListener {
        void onItemClicked(GitHubUser countryCode);

        void noResultFound(int size);
    }


}
