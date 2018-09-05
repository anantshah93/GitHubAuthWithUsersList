package io.studio.githubdemo.adapter.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import io.studio.githubdemo.R;


/**
 * Created by anantshah on 15/07/17.
 */

public class LoadingViewHolder extends BaseViewHolder{
    public ProgressBar progressBar;

    public LoadingViewHolder(View itemView,Context context) {
        super(itemView,context);
        progressBar = itemView.findViewById(R.id.progress_bar_loading);
    }

}
