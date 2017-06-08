package com.zano.shareride.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by Zano on 07/06/2017, 17:59.
 */

public abstract class BaseRecyclerAdapter<MVH extends BaseRecyclerAdapter.ViewHolder, D> extends RecyclerView.Adapter<BaseRecyclerAdapter.ViewHolder> {

    protected String TAG;
    protected Context context;
    protected int listItemLayoutId;
    protected Map<String, D> data;

    public BaseRecyclerAdapter(Context context, int listItemLayoutId, Map<String,D> data) {
        this.context = context;
        this.listItemLayoutId = listItemLayoutId;
        this.data = data;
        this.TAG = setTag();
    }

    @Override
    public void onBindViewHolder(BaseRecyclerAdapter.ViewHolder holder, int position) {
        D data = new ArrayList<>(this.data.values()).get(position);
        if(data!=null) {
            onBindViewHolder((MVH)holder,data);
        }
    }

    @Override
    public BaseRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(listItemLayoutId,parent,false);
        return createViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    protected abstract MVH createViewHolder(View itemView);

    protected abstract void onBindViewHolder(MVH holder,D data);

    protected abstract String setTag();


}
