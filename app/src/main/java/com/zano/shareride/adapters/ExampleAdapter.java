package com.zano.shareride.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zano.shareride.R;

import java.util.Map;

import butterknife.BindView;

/**
 * Created by Zano on 07/06/2017, 18:47.
 */

public class ExampleAdapter extends FirebaseRecyclerAdapter<ExampleAdapter.ViewHolder, String> {


    public ExampleAdapter(Context context, int listItemLayoutId, Map<String, String> data) {
        super(context, listItemLayoutId, data, "prova/examples",String.class);
    }

    @Override
    protected ViewHolder createViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, String data) {
        holder.textViewExample.setText(data);
    }

    @Override
    protected String setTag() {
        return "ExampleAdapter";
    }

    @Override
    protected void updatedData(String value) {
        Log.d(TAG,"Changed: " + value);
    }

    class ViewHolder extends BaseRecyclerAdapter.ViewHolder {
        @BindView(R.id.my_textview_example) TextView textViewExample;

        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
