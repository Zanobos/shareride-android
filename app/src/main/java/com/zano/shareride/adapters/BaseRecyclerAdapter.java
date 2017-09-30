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
 * This class is the base for every recycler adapter, it sets some fields
 */
public abstract class BaseRecyclerAdapter<MVH extends BaseRecyclerAdapter.ViewHolder, D> extends RecyclerView.Adapter<BaseRecyclerAdapter.ViewHolder> {

    protected String TAG;
    protected Context context;
    protected int listItemLayoutId;
    protected Map<String, D> data;
    protected DataClickListener<D> listener; //This listener is ONLY to detect click on the data as a WHOLE (not a button inside the view, for example)

    BaseRecyclerAdapter(Context context, int listItemLayoutId, Map<String, D> data) {
        this.context = context;
        this.listItemLayoutId = listItemLayoutId;
        this.data = data;
        this.TAG = setTag();
    }

    BaseRecyclerAdapter(Context context, int listItemLayoutId, Map<String, D> data, DataClickListener<D> listener) {
        this(context,listItemLayoutId,data);
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerAdapter.ViewHolder holder, int position) {
        final D data = new ArrayList<>(this.data.values()).get(position);
        if(data!=null) {
            if(listener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onDataClicked(data);
                    }
                });
            }
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

    abstract class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    protected abstract MVH createViewHolder(View itemView);

    protected abstract void onBindViewHolder(MVH holder,D data);

    protected abstract String setTag();

    public interface DataClickListener<D> {
        void onDataClicked(D data);
    }
}
