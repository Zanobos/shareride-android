package com.zano.shareride.activities;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.zano.shareride.R;
import com.zano.shareride.adapters.ExampleAdapter;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.textView) TextView textView;
    @BindView(R.id.my_recycler_view) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Map<String,String> map = new LinkedHashMap<>();
        map.put("1","one");
        map.put("2","two");
        map.put("3","three");
        ExampleAdapter adapter = new ExampleAdapter(this,R.layout.listitem_example,map);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected String setTag() {
        return "MainActivity";
    }


}
