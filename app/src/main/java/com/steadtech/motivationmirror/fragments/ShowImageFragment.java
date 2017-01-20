package com.steadtech.motivationmirror.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.steadtech.motivationmirror.R;
import com.steadtech.motivationmirror.utils.Container;
import com.steadtech.motivationmirror.utils.Utilities;

public class ShowImageFragment extends BaseFragment  {
    public static final String TAG = ShowImageFragment.class.getSimpleName();

    public static Fragment newInstance() {
        Fragment fragment = new ShowImageFragment();
        return fragment;
    }

    public ShowImageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ImageView imgWishPost = (ImageView) view.findViewById(R.id.imgWishPost);
        imgWishPost.setImageBitmap(Container.getBitmap());

        final Button btn_share = (Button) view.findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utilities.shareBitmap(getActivity(), Container.getBitmap(), getString(R.string.app_name) + System.currentTimeMillis());

            }
        });
    }

}
