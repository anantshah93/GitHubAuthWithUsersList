package io.studio.githubdemo.adapter.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.studio.githubdemo.R;


/**
 * Created by anantshah on 16/04/18.
 */

public class GitHubUserViewHolders extends BaseViewHolder {

    public ImageView imageViewUser;
    public TextView textViewTitle;
    public TextView textViewType;
    public TextView textViewRepos;
    public TextView textViewProfile;
    public Context context;

    public GitHubUserViewHolders(View itemView, Context context) {
        super(itemView, context);
        this.context = context;
        imageViewUser = itemView.findViewById(R.id.imageViewUser);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        textViewRepos = itemView.findViewById(R.id.textViewRepos);
        textViewProfile = itemView.findViewById(R.id.textViewProfile);
        textViewType = itemView.findViewById(R.id.textViewType);
        ;
    }

}
