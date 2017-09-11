package com.zano.shareride.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zano.shareride.R;
import com.zano.shareride.network.common.UserRequest;

import java.util.Map;

import butterknife.BindView;

/**
 * Created by Zano on 10/09/2017, 18:20.
 */

public class UserRequestListAdapter extends BaseRecyclerAdapter<UserRequestListAdapter.ViewHolder,UserRequest> {

    public UserRequestListAdapter(Context context,Map<String, UserRequest> data) {
        super(context, R.layout.listitem_user_request, data);
    }

    @Override
    protected ViewHolder createViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, UserRequest data) {
        holder.dateTV.setText(data.getAskedDevilery().getTime().toString());
    }

    @Override
    protected String setTag() {
        return "RequestListAdapter";
    }

    public class ViewHolder extends BaseRecyclerAdapter.ViewHolder {
        @BindView(R.id.listitem_user_request_tv_date) TextView dateTV;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
