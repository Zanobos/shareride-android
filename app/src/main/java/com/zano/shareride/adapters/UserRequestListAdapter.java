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

    public UserRequestListAdapter(Context context,Map<String, UserRequest> data,DataClickListener<UserRequest> listener) {
        super(context, R.layout.listitem_user_request, data,listener);
    }

    @Override
    protected ViewHolder createViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, UserRequest data) {
        holder.pickupDateTV.setText(data.getProposedPickup().getDate().toString());
        holder.pickupTimeTV.setText(data.getProposedPickup().getTime().toString());
        holder.pickupAddressTV.setText(data.getProposedPickup().getAddress());
        holder.deliveryDateTV.setText(data.getProposedDevilery().getDate().toString());
        holder.deliveryTimeTV.setText(data.getProposedDevilery().getTime().toString());
        holder.deliveryAddressTV.setText(data.getProposedDevilery().getAddress());
    }

    @Override
    protected String setTag() {
        return "RequestListAdapter";
    }

    class ViewHolder extends BaseRecyclerAdapter.ViewHolder {
        @BindView(R.id.listitem_user_request_tv_pickup_date) TextView pickupDateTV;
        @BindView(R.id.listitem_user_request_tv_pickup_time) TextView pickupTimeTV;
        @BindView(R.id.listitem_user_request_tv_pickup_address) TextView pickupAddressTV;
        @BindView(R.id.listitem_user_request_tv_delivery_date) TextView deliveryDateTV;
        @BindView(R.id.listitem_user_request_tv_delivery_time) TextView deliveryTimeTV;
        @BindView(R.id.listitem_user_request_tv_delivery_address) TextView deliveryAddressTV;

        ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
