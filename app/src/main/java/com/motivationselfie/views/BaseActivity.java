package com.motivationselfie.views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.motivationselfie.R;
import com.motivationselfie.modals.QuotesAssests;
import com.motivationselfie.utils.Container;
import com.motivationselfie.utils.Preference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@SuppressWarnings("ALL")
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    ArrayList<QuotesAssests> list;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInterstitialAd();
    }

    public ArrayList<QuotesAssests> parseJSONQuotes() {

        list = new ArrayList<>();

        String json;
        try {
            InputStream is = getAssets().open("quotes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            // Parse Json quotes from assets folder
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonQuotes = jsonObject.getJSONObject("quotes");
            JSONArray jsonArray = jsonQuotes.getJSONArray("cd");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonInner = jsonArray.getJSONObject(i);
                QuotesAssests quote = new QuotesAssests();
                quote.setId(Integer.parseInt(jsonInner.getString("id")));
                quote.setAuthor(jsonInner.getString("author"));
                quote.setQuotes(jsonInner.getString("text"));
                list.add(quote);
            }

            Container.setQuote_list(list);

            // Set InApp Purchase Preferences to TRUE For Remove InApp Purchase in the App.
            Preference.setInAppFromPref(this, true);

        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // Initialize InterstitialAd
    private void initInterstitialAd() {
        mInterstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        loadInterstitialAds();
    }

    // Show Interstitial Ads
    public void showInterstitial() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Show Ads
                if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    loadInterstitialAds();
                }
            }
        });

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                Log.e("Interstitial", "onAdLoaded");
            }

            @Override
            public void onAdClosed() {
                Log.e("Interstitial", "onAdClosed");
                // Load the next interstitial.
                loadInterstitialAds();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e("Interstitial", "onAdFailedToLoad>>" + errorCode);
            }

            @Override
            public void onAdLeftApplication() {
                Log.e("Interstitial", "onAdLeftApplication");
            }

            @Override
            public void onAdOpened() {
                Log.e("Interstitial", "onAdOpened");
            }
        });
    }

    // Load Interstitial Ads
    private void loadInterstitialAds() {
        // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
    }
}