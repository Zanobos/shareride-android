package com.zano.shareride.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Zano on 07/06/2017, 17:37.
 */

public abstract class BaseFragment extends Fragment {

    private static String TAG;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = setTag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId(), container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    protected abstract int layoutId();

    /**
     * @return the Tag of the activity. In this way at least I am forced to initialize it
     */
    protected abstract String setTag();
}
