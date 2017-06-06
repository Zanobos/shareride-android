package com.zano.shareride.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by Zano on 06/06/2017, 17:31.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        ButterKnife.bind(this);
    }

    /**
     * @return the id referencing the layout to use for this activity
     */
    protected abstract int layoutId();
}
