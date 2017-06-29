package com.motivationselfie.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.motivationselfie.R;
import com.motivationselfie.utils.Container;
import com.motivationselfie.utils.Utilities;

@SuppressWarnings("ALL")
public class ShowImageFragment extends BaseFragment {
    public static final String TAG = ShowImageFragment.class.getSimpleName();
    private AdView mAdView;
    private AdView mAdView2;

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

        showBannerAd(view);
    }

    // Show Banner Ads
    private void showBannerAd(View view) {
        mAdView = (AdView) view.findViewById(R.id.adView);
        mAdView2 = (AdView) view.findViewById(R.id.adView2);
        mAdView.loadAd(new AdRequest.Builder().build());
        mAdView2.loadAd(new AdRequest.Builder().build());
        mAdView.setAdListener(new AdListener() {
            public void onAdLoaded() {
                Log.e("ShowImageFragment>>Banner", "onAdLoaded");
            }

            @Override
            public void onAdClosed() {
                Log.e("ShowImageFragment>>Banner", "onAdClosed");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e("ShowImageFragment>>Banner", "onAdFailedToLoad>>" + errorCode);
            }

            @Override
            public void onAdLeftApplication() {
                Log.e("ShowImageFragment>>Banner", "onAdLeftApplication");
            }

            @Override
            public void onAdOpened() {
                Log.e("ShowImageFragment>>Banner", "onAdOpened");
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdView != null) {
            mAdView.pause();
        }

        if (mAdView2 != null) {
            mAdView2.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdView != null) {
            mAdView.destroy();
        }

        if (mAdView2 != null) {
            mAdView2.destroy();
        }
    }
}