package com.motivationselfie.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.motivationselfie.R;

@SuppressWarnings("ALL")
public class SelectionButtonFragment extends BaseFragment {
    public static final String TAG = SelectionButtonFragment.class.getSimpleName();

    public static Fragment newInstance() {
        return new EditSavePhotoFragment();
    }

    public SelectionButtonFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_buttons, container, false);
    }
}