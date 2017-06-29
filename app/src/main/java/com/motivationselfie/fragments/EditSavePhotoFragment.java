package com.motivationselfie.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.motivationselfie.R;
import com.motivationselfie.adapters.FrameAdapter;
import com.motivationselfie.interfaces.AddQuoteInterface;
import com.motivationselfie.interfaces.InterfaceContainer;
import com.motivationselfie.modals.QuotesAssests;
import com.motivationselfie.utils.Container;
import com.motivationselfie.utils.HorizontalListView;
import com.motivationselfie.utils.ImageParameters;
import com.motivationselfie.utils.ImageUtility;
import com.motivationselfie.utils.Preference;
import com.motivationselfie.utils.Utilities;
import com.motivationselfie.views.CameraActivity;
import com.motivationselfie.views.RuntimePermissionActivity;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class EditSavePhotoFragment extends BaseFragment implements View.OnTouchListener, AddQuoteInterface {

    public static final String TAG = EditSavePhotoFragment.class.getSimpleName();
    public static final String BITMAP_KEY = "bitmap_byte_array";
    public static final String ROTATION_KEY = "rotation";
    public static final String IMAGE_INFO = "image_info";
    public static final String QUOTE = "quote";
    public static final String TAG_NAME = "tag";

    private static final int REQUEST_STORAGE = 1;
    private TextView tv_quotes_edit;

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int touchState = NONE;
    private int _xDelta;
    private int _yDelta;

    public static Bitmap screenShotBitmap;
    private ImageView photoImageView;
    private ImageView frame_photo;
    private RelativeLayout relate_up;
    private ImageButton forward;
    private HorizontalListView lv_frames;
    private Button btn_likes;
    private TextView tv_count;
    private View view_frames;
    private View view_button;
    private TypedArray imgs;
    private Boolean isframevisible = false;
    private Boolean isframeSelect = false;
    private View _rootView;
    private ArrayList<Integer> list_Fav_Id;
    private ArrayList<QuotesAssests> lisFavSelf;

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    public static Fragment newInstance(byte[] bitmapByteArray, int rotation,
                                       @NonNull ImageParameters parameters, String quote, String tag) {

        Fragment fragment = new EditSavePhotoFragment();
        Bundle args = new Bundle();
        args.putByteArray(BITMAP_KEY, bitmapByteArray);
        args.putInt(ROTATION_KEY, rotation);
        args.putParcelable(IMAGE_INFO, parameters);
        args.putString(QUOTE, quote);
        args.putString(TAG_NAME, tag);
        fragment.setArguments(args);
        return fragment;
    }

    public EditSavePhotoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (_rootView == null) {
            _rootView = inflater.inflate(R.layout.fragment_edit_save_photo, container, false);
        }
        return _rootView;
    }

    @Override
    public void onDestroyView() {
        if (_rootView.getParent() != null) {
            ((ViewGroup) _rootView.getParent()).removeView(_rootView);
        }
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InterfaceContainer.quto = this;

        int rotation = getArguments().getInt(ROTATION_KEY);
        byte[] data = getArguments().getByteArray(BITMAP_KEY);
        String tag = getArguments().getString(TAG_NAME);
        ImageParameters imageParameters = getArguments().getParcelable(IMAGE_INFO);

        if (imageParameters == null) {
            return;
        }

        // Get Array List From SharePrefrences...
        list_Fav_Id = new ArrayList<>();
        if (Preference.getfavArrayListPref(getActivity()) != null) {
            list_Fav_Id = Preference.getfavArrayListPref(getActivity());
        }

        // Get Self ArrayList
        lisFavSelf = new ArrayList<>();
        if (Preference.getfavSelfArrayListPref(getActivity()) != null && Preference.getfavSelfArrayListPref(getActivity()).size() > 0) {
            lisFavSelf = Preference.getfavSelfArrayListPref(getActivity());
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, width);


        tv_quotes_edit = (TextView) view.findViewById(R.id.tv_quotes_edit);
        tv_quotes_edit.setText(Utilities.getQuoteUsingId(Container.getQuote_list(), lisFavSelf, Preference.getQuoteIdFromPref(getActivity()))
                + "\n" + getResources().getString(R.string.hash_message));
        tv_quotes_edit.setOnTouchListener(this);
        //tv_quotes_edit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_local_offer_black_24dp, 0, 0, 0);

        photoImageView = (ImageView) view.findViewById(R.id.photo);
        photoImageView.setLayoutParams(layoutParams);
        frame_photo = (ImageView) view.findViewById(R.id.frame_photo);
        frame_photo.setLayoutParams(layoutParams);

        relate_up = (RelativeLayout) view.findViewById(R.id.relate_up);

        view_frames = view.findViewById(R.id.include_frames_view);
        view_frames.setVisibility(View.GONE);
        view_button = view.findViewById(R.id.include_button_view);
        view_button.setVisibility(View.VISIBLE);

        imgs = getResources().obtainTypedArray(R.array.frame_array);
        lv_frames = (HorizontalListView) view_frames.findViewById(R.id.lv_frames);

        Button btn_quotes = (Button) view_button.findViewById(R.id.btn_quotes);
        Button btn_frames = (Button) view_button.findViewById(R.id.btn_frames);
        btn_likes = (Button) view_button.findViewById(R.id.btn_likes);

        getTextAndCompare();

        Button btn_count_bg = (Button) view_button.findViewById(R.id.btn_count_bg);
        tv_count = (TextView) view_button.findViewById(R.id.tv_count);

        // Check InApp Purchase Done Or Not To Change Text On TextView For Count Down...
        if (Preference.getInAppFromPref(getActivity())) {
            tv_count.setText("");
        }

        ImageButton cancel = (ImageButton) view.findViewById(R.id.cancel);

        forward = (ImageButton) view.findViewById(R.id.forward);
        forward.setTag(R.drawable.forward_icon);
        forward.setBackgroundResource(R.drawable.forward_icon);

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((Integer) forward.getTag() == R.drawable.forward_icon) {
                    isframevisible = false;
                    isframeSelect = false;
                    showPictureFragment();

                } else if ((Integer) forward.getTag() == R.drawable.accept_icon_white) {
                    forward.setTag(R.drawable.forward_icon);
                    forward.setBackgroundResource(R.drawable.forward_icon);
                    view_frames.setVisibility(View.GONE);
                    view_button.setVisibility(View.VISIBLE);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isframevisible && !isframeSelect) {

                    getActivity().getSupportFragmentManager().popBackStack();

                } else if (isframevisible && !isframeSelect) {
                    isframevisible = false;
                    isframeSelect = false;
                    view_frames.setVisibility(View.GONE);
                    view_button.setVisibility(View.VISIBLE);

                } else if (isframevisible) {
                    isframevisible = false;
                    frame_photo.setImageResource(0);
                    isframeSelect = false;
                    view_frames.setVisibility(View.GONE);
                    view_button.setVisibility(View.VISIBLE);
                }

            }
        });

        btn_quotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,
                                QuotesListFragment.newInstance(),
                                QuotesListFragment.TAG)
                        .addToBackStack(null)
                        .commit();

            }
        });

        btn_frames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isframevisible = true;
                isframeSelect = false;
                forward.setTag(R.drawable.accept_icon_white);
                forward.setBackgroundResource(R.drawable.accept_icon_white);
                view_frames.setVisibility(View.VISIBLE);
                view_button.setVisibility(View.GONE);
            }
        });

        btn_likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Integer) btn_likes.getTag() == R.drawable.heart_icon) {

                    btn_likes.setTag(R.drawable.like_heart);
                    btn_likes.setBackgroundResource(R.drawable.like_heart);

                    if (!Preference.getInAppFromPref(getActivity()) && list_Fav_Id != null) {
                        if (list_Fav_Id.size() < 3) {

                            list_Fav_Id.add(Preference.getQuoteIdFromPref(getActivity()));
                            Preference.insertArrayListPref(getActivity(), list_Fav_Id);

                        } else {

                            list_Fav_Id.remove(list_Fav_Id.size() - 1);
                            list_Fav_Id.add(0, Preference.getQuoteIdFromPref(getActivity()));
                            Preference.insertArrayListPref(getActivity(), list_Fav_Id);
                        }

                    } else if (Preference.getInAppFromPref(getActivity()) && list_Fav_Id != null && lisFavSelf != null) {
                        if (lisFavSelf.size() > 0) {
                            if ((list_Fav_Id.size() + lisFavSelf.size()) < 21) {

                                list_Fav_Id.add(Preference.getQuoteIdFromPref(getActivity()));
                                Preference.insertArrayListPref(getActivity(), list_Fav_Id);

                            } else {
                                for (int i = 0; i < lisFavSelf.size(); i++) {
                                    if (list_Fav_Id.get(list_Fav_Id.size() - 1).equals(lisFavSelf.get(i).getId())) {
                                        lisFavSelf.remove(i);
                                        Preference.insertSelfArrayListPref(getActivity(), lisFavSelf);
                                    }
                                }
                                list_Fav_Id.remove(list_Fav_Id.size() - 1);
                                list_Fav_Id.add(0, Preference.getQuoteIdFromPref(getActivity()));
                                Preference.insertArrayListPref(getActivity(), list_Fav_Id);
                            }

                        } else {
                            if (list_Fav_Id.size() < 21) {

                                list_Fav_Id.add(Preference.getQuoteIdFromPref(getActivity()));
                                Preference.insertArrayListPref(getActivity(), list_Fav_Id);

                            } else {
                                list_Fav_Id.remove(list_Fav_Id.size() - 1);
                                list_Fav_Id.add(0, Preference.getQuoteIdFromPref(getActivity()));
                                Preference.insertArrayListPref(getActivity(), list_Fav_Id);
                            }
                        }
                    }


                } else if ((Integer) btn_likes.getTag() == R.drawable.like_heart) {

                    btn_likes.setTag(R.drawable.heart_icon);
                    btn_likes.setBackgroundResource(R.drawable.heart_icon);

                    if (Preference.getfavArrayListPref(getActivity()) != null) {
                        for (int i = 0; i < Preference.getfavArrayListPref(getActivity()).size(); i++) {
                            if (Preference.getQuoteIdFromPref(getActivity()) == Preference.getfavArrayListPref(getActivity()).get(i)) {
                                list_Fav_Id = Preference.getfavArrayListPref(getActivity());
                                list_Fav_Id.remove(i);
                                Preference.insertArrayListPref(getActivity(), list_Fav_Id);
                            }
                        }
                    }

                    if (lisFavSelf != null && lisFavSelf.size() > 0) {
                        for (int i = 0; i < lisFavSelf.size(); i++) {
                            if (Preference.getQuoteIdFromPref(getActivity()) == lisFavSelf.get(i).getId()) {
                                lisFavSelf.remove(i);
                                Preference.insertSelfArrayListPref(getActivity(), lisFavSelf);

                            }
                        }

                        Preference.setDateFromPref(getActivity(), Utilities.getCurrentDate());
                        Preference.setQuoteIdFromPref(getActivity(), -1);
                        tv_quotes_edit.setText(Utilities.getRandomQuote(getActivity(), Container.getQuote_list()) + "\n" + getResources().getString(R.string.hash_message));
                    }
                }
            }
        });

        btn_count_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tv_count.getText().toString().equals("0") && !tv_count.getText().equals("")) {

                    int count = (Integer.parseInt(tv_count.getText().toString()) - 1);
                    tv_count.setText(String.valueOf(count));

                    Preference.setDateFromPref(getActivity(), Utilities.getCurrentDate());
                    Preference.setQuoteIdFromPref(getActivity(), -1);
                    tv_quotes_edit.setText(Utilities.getRandomQuote(getActivity(), Container.getQuote_list()) + "\n" + getResources().getString(R.string.hash_message));
                    getTextAndCompare();

                } else if (tv_count.getText().equals("")) {

                    Preference.setDateFromPref(getActivity(), Utilities.getCurrentDate());
                    Preference.setQuoteIdFromPref(getActivity(), -1);
                    tv_quotes_edit.setText(Utilities.getRandomQuote(getActivity(), Container.getQuote_list()) + "\n" + getResources().getString(R.string.hash_message));
                    getTextAndCompare();

                } else {

                    Utilities.showInAppDialog(getActivity(), getResources().getString(R.string.picture_InAppQuotes));

                }
            }
        });

        lv_frames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!Preference.getInAppFromPref(getActivity())) {
                    if (position >= 0 && position < 3) {
                        isframevisible = true;
                        isframeSelect = true;
                        frame_photo.setImageResource(0);
                        frame_photo.setImageResource(imgs.getResourceId(position, -1));
                    } else {

                        Utilities.showInAppDialog(getActivity(), getResources().getString(R.string.frames_InAppQuotes));

                    }
                } else {
                    isframevisible = true;
                    isframeSelect = true;
                    frame_photo.setImageResource(0);
                    frame_photo.setImageResource(imgs.getResourceId(position, -1));
                }
            }
        });

        imageParameters.mIsPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        rotatePicture(rotation, data, photoImageView, tag);

        showBannerAd(view);
        initInterstitialAd();
    }

    // get text from text view and then compare with favourite list and main quote list and set favourite heart on button red...
    private void getTextAndCompare() {
        // Here To Check favourite Quotes Compare With Text as shown on text view...
        if (list_Fav_Id != null && list_Fav_Id.size() > 0) {
            for (int j = 0; j < list_Fav_Id.size(); j++) {
                for (int i = 0; i < Container.getQuote_list().size(); i++) {
                    if (tv_quotes_edit.getText().toString().equals(Container.getQuote_list().get(i).getQuotes() + "\n" + Container.getQuote_list().get(i).getAuthor() + "\n" + getResources().getString(R.string.hash_message))) {
                        if (Container.getQuote_list().get(i).getId().equals(list_Fav_Id.get(j))) {
                            Preference.setBackStackFromPref(getActivity(), true);
                        }
                    }
                }
            }
        }

        if (lisFavSelf != null && lisFavSelf.size() > 0) {
            for (int i = 0; i < lisFavSelf.size(); i++) {
                if (Preference.getQuoteIdFromPref(getActivity()) == lisFavSelf.get(i).getId()) {
                    Preference.setBackStackFromPref(getActivity(), true);
                }
            }
        }

        if (Preference.getBackStackFromPref(getActivity())) {
            btn_likes.setTag(R.drawable.like_heart);
            btn_likes.setBackgroundResource(R.drawable.like_heart);
            Preference.setBackStackFromPref(getActivity(), false);
        } else {
            btn_likes.setTag(R.drawable.heart_icon);
            btn_likes.setBackgroundResource(R.drawable.heart_icon);
            Preference.setBackStackFromPref(getActivity(), false);
        }
    }

    private void rotatePicture(int rotation, byte[] data, ImageView photoImageView, String tag) {
        Bitmap bitmap;
        if (tag.equals("Camera")) {
            bitmap = ImageUtility.decodeSampledBitmapFromByte(getActivity(), data);
        } else {
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        }

        if (rotation != 0 && bitmap != null) {
            Bitmap oldBitmap = bitmap;

            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);

            bitmap = Bitmap.createBitmap(
                    oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, false
            );

            oldBitmap.recycle();
        }

        photoImageView.setImageBitmap(bitmap);

        // Set Adapter For Bitmap Image On Frames...
        FrameAdapter adapter = new FrameAdapter(getActivity(), imgs, bitmap);
        lv_frames.setAdapter(adapter);
    }

    private void savePicture() {
        requestForPermission();
    }

    private void requestForPermission() {
        RuntimePermissionActivity.startActivity(EditSavePhotoFragment.this,
                REQUEST_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK != resultCode) {
            return;
        }

        if (REQUEST_STORAGE == requestCode && data != null) {
            final boolean isGranted = data.getBooleanExtra(RuntimePermissionActivity.REQUESTED_PERMISSION, false);
            final View view = getView();
            if (isGranted && view != null) {
                ImageView photoImageView = (ImageView) view.findViewById(R.id.photo);
                Bitmap bitmap = ((BitmapDrawable) photoImageView.getDrawable()).getBitmap();
                Uri photoUri = ImageUtility.savePicture(getActivity(), bitmap);
                ((CameraActivity) getActivity()).returnPhotoUri(photoUri);
            }
        }
    }

    // Show Image Fragment...
    private void showPictureFragment() {
        try {
            View v1 = relate_up;
            v1.setDrawingCacheEnabled(true);
            Bitmap resultBitmap = Bitmap.createBitmap(v1.getDrawingCache(true));
            try {
                resultBitmap = Bitmap.createScaledBitmap(resultBitmap, v1.getMeasuredWidth(), v1.getMeasuredHeight(), false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            v1.setDrawingCacheEnabled(false);

            screenShotBitmap = null;
            screenShotBitmap = resultBitmap;
            Container.setBitmap(screenShotBitmap);
            showInterstitial();
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,
                            ShowImageFragment.newInstance(),
                            ShowImageFragment.TAG)
                    .addToBackStack(null)
                    .commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        //int height = displayMetrics.heightPixels;

        if (tv_quotes_edit.getText().toString().length() < 35) {
            RelativeLayout.LayoutParams layoutParamss = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            tv_quotes_edit.setLayoutParams(layoutParamss);
        } else {
            RelativeLayout.LayoutParams layoutParamss = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen._230sdp), RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParamss.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            tv_quotes_edit.setLayoutParams(layoutParamss);
        }

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                _xDelta = (int) (X - tv_quotes_edit.getX());
                _yDelta = (int) (Y - tv_quotes_edit.getY());

                touchState = DRAG;

                tv_quotes_edit.getParent().requestDisallowInterceptTouchEvent(false);

                break;

            case MotionEvent.ACTION_UP:

                view.getParent().requestDisallowInterceptTouchEvent(false);

                break;

            case MotionEvent.ACTION_POINTER_DOWN:

                touchState = ZOOM;

                break;

            case MotionEvent.ACTION_POINTER_UP:

                touchState = NONE;
                break;

            case MotionEvent.ACTION_MOVE:

                if (touchState == ZOOM) {

                    tv_quotes_edit.getParent().requestDisallowInterceptTouchEvent(false);

                } else if (touchState == DRAG) {

                    tv_quotes_edit.getParent().requestDisallowInterceptTouchEvent(false);

                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();

                    if ((X - _xDelta) > 0 && ((X - _xDelta) + tv_quotes_edit.getWidth()) <= width) {
                        tv_quotes_edit.setX(X - _xDelta);
                    }

                    if ((Y - _yDelta) > 0 && ((Y - _yDelta) + tv_quotes_edit.getHeight()) <= photoImageView.getHeight()) {
                        tv_quotes_edit.setY(Y - _yDelta);
                    }

                    tv_quotes_edit.setLayoutParams(layoutParams);
                }
                break;
        }

        return true;
    }


    // InApp Purchase Ok Dialog Button Click For Purchase
    @Override
    public void okButtonClick() {
    }

    // Here to check the success true and reload the current fragment
    @Override
    public void InAppSuccess(Boolean isSuccess) {
        if (isSuccess) {
            reloadFragmenmt(this);
        }
    }

    // This Call Back Methood is Not Used here but It is mendatory to place here...
    @Override
    public void addNewQuote(String quote) {
    }

    // Show Banner Ads
    private void showBannerAd(View view) {
        mAdView = (AdView) view.findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());
        mAdView.setAdListener(new AdListener() {
            public void onAdLoaded() {
                Log.e("Banner", "onAdLoaded");
            }

            @Override
            public void onAdClosed() {
                Log.e("Banner", "onAdClosed");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e("Banner", "onAdFailedToLoad>>" + errorCode);
            }

            @Override
            public void onAdLeftApplication() {
                Log.e("Banner", "onAdLeftApplication");
            }

            @Override
            public void onAdOpened() {
                Log.e("Banner", "onAdOpened");
            }
        });
    }

    // Initialize InterstitialAd
    private void initInterstitialAd() {
        mInterstitialAd = new InterstitialAd(getActivity());
        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        loadInterstitialAds();
    }

    // Show Interstitial Ads
    private void showInterstitial() {
        getActivity().runOnUiThread(new Runnable() {
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

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdView != null) {
            mAdView.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdView != null) {
            mAdView.destroy();
        }

        if (mInterstitialAd != null) {
            mInterstitialAd = null;
        }
    }
}