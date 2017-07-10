package com.zano.shareride.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.zano.shareride.R;
import com.zano.shareride.adapters.BaseRecyclerAdapter;
import com.zano.shareride.adapters.ExampleAdapter;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    private BaseRecyclerAdapter adapter;

    @BindView(R.id.my_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.my_button) Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Map<String, String> map = new LinkedHashMap<>();
        adapter = new ExampleAdapter(this, R.layout.listitem_example, map);
        recyclerView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
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
