package com.zano.shareride.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zano.shareride.R;
import com.zano.shareride.adapters.BaseRecyclerAdapter;
import com.zano.shareride.adapters.UserRequestListAdapter;
import com.zano.shareride.network.NetworkController;
import com.zano.shareride.network.common.UserRequest;
import com.zano.shareride.network.userrequestlist.UserRequestListRequest;
import com.zano.shareride.network.userrequestlist.UserRequestListResponse;
import com.zano.shareride.util.Constants;
import com.zano.shareride.util.parcelables.ParcelableUserRequest;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Zano on 10/09/2017, 18:43.
 */

public class RequestListActivity extends UserLoggedActivity {

    private BaseRecyclerAdapter adapter;
    private Map<String,UserRequest> map;
    private BaseRecyclerAdapter.DataClickListener<UserRequest> dataClickListener;

    @BindView(R.id.user_request_recycler_view) RecyclerView recyclerView;

    @Override
    protected int layoutId() {
        return R.layout.activity_request_list;
    }

    @Override
    protected String setTag() {
        return "RequestListActivity";
    }

    @Override
    protected void userReady() {
        String userId = user.getUid();
        NetworkController networkController = NetworkController.getInstance(this);
        UserRequestListRequest userRequestListRequest = new UserRequestListRequest();
        userRequestListRequest.setUserId(userId);
        showProgressDialog(R.string.dialog_loading);
        networkController.addUserRequestListRequest(userRequestListRequest,
                new Response.Listener<UserRequestListResponse>() {
                    @Override
                    public void onResponse(UserRequestListResponse response) {
                        map.clear();
                        map.putAll(response.getRequestMap());
                        adapter.notifyDataSetChanged();
                        closeProgressDialog();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgressDialog();
                        Log.e(TAG, "onUserRequestListResponse:" + error.getMessage(), error);
                        showToast(R.string.toast_error, false);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        map = new HashMap<>();
        dataClickListener = new BaseRecyclerAdapter.DataClickListener<UserRequest>() {
            @Override
            public void onDataClicked(UserRequest data) {
                Intent intent = new Intent(RequestListActivity.this, MapRequestDetailActivity.class);
                ParcelableUserRequest parcelableUserRequest = new ParcelableUserRequest(data);
                intent.putExtra(Constants.ParcelArgs.USER_REQUEST,parcelableUserRequest);
                startActivity(intent);
            }
        };
        adapter = new UserRequestListAdapter(RequestListActivity.this, map,dataClickListener);
        recyclerView.setAdapter(adapter);
    }
}
