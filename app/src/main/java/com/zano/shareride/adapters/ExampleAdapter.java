package com.zano.shareride.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zano.shareride.R;

import java.util.Map;

import butterknife.BindView;

/**
 * Created by Zano on 07/06/2017, 18:47.
 */

public class ExampleAdapter extends BaseRecyclerAdapter<ExampleAdapter.ViewHolder, String> {


    public ExampleAdapter(Context context, int listItemLayoutId, Map<String, String> data) {
        super(context, listItemLayoutId, data);
    }

    @Override
    protected ViewHolder createViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, String data) {
        holder.textViewExample.setText(data);
    }

    public class ViewHolder extends BaseRecyclerAdapter.ViewHolder {
        @BindView(R.id.my_textview_example) TextView textViewExample;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
