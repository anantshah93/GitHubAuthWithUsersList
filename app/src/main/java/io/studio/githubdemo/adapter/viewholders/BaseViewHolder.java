package io.studio.githubdemo.adapter.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    private Context context;

    public BaseViewHolder(View itemView, Context activity) {
        super(itemView);
        this.context = activity;

    }

    public void setRecyclerViewWithAdapter(RecyclerView recyclerView,
            RecyclerView.LayoutManager layoutManager,
            RecyclerView.Adapter<RecyclerView.ViewHolder> recyclerAdapter,
            boolean isFixedSize,
            boolean isAutoMeasureEnabled,
            boolean isNestedScrollingEnabled) {
        recyclerView.setHasFixedSize(isFixedSize);
        layoutManager.setAutoMeasureEnabled(isAutoMeasureEnabled);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(isNestedScrollingEnabled);
        recyclerView.setAdapter(recyclerAdapter);

    }

}
