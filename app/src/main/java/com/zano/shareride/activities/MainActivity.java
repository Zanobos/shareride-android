package com.zano.shareride.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.zano.shareride.R;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.textView) TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }


}
