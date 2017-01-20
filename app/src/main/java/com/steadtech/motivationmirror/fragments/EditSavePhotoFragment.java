package com.steadtech.motivationmirror.fragments;


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

import com.steadtech.motivationmirror.R;
import com.steadtech.motivationmirror.adapters.FrameAdapter;
import com.steadtech.motivationmirror.interfaces.AddQuoteInterface;
import com.steadtech.motivationmirror.interfaces.InterfaceContainer;
import com.steadtech.motivationmirror.modals.QuotesAssests;
import com.steadtech.motivationmirror.utils.Container;
import com.steadtech.motivationmirror.utils.HorizontalListView;
import com.steadtech.motivationmirror.utils.ImageParameters;
import com.steadtech.motivationmirror.utils.ImageUtility;
import com.steadtech.motivationmirror.utils.Prefrence;
import com.steadtech.motivationmirror.utils.Utilities;
import com.steadtech.motivationmirror.views.CameraActivity;
import com.steadtech.motivationmirror.views.RuntimePermissionActivity;

import java.util.ArrayList;


/**
 *
 */
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
    private Bitmap resultBitmap;
    public static Bitmap screenShotBitmap;
    private ImageView photoImageView;
    private ImageView frame_photo;
    private byte[] array;
    private RelativeLayout relate_up;
    private ImageButton forward;
    private ImageButton cancel;
    private HorizontalListView lv_frames;
    private Button btn_quotes;
    private Button btn_frames;
    private Button btn_likes;
    private Button btn_count_bg;
    private TextView tv_count;
    private View view_frames;
    private View view_button;
    private FrameAdapter adapter;
    private TypedArray imgs;
    private Boolean isframevisible = false;
    private Boolean isframeSelect = false;
    private View _rootView;
    private ArrayList<Integer> list_Fav_Id;
    private ArrayList<QuotesAssests> lisFavSelf;

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

        } else {
            // Do not inflate the layout again.
            // The returned View of onCreateView will be added into the fragment.
            // However it is not allowed to be added twice even if the parent is same.
            // So we must remove _rootView from the existing parent view group
            // in onDestroyView() (it will be added back).
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
        list_Fav_Id = new ArrayList<Integer>();
        if (Prefrence.getfavArrayListPref(getActivity()) != null) {
            list_Fav_Id = Prefrence.getfavArrayListPref(getActivity());
        }

        // Get Self ArrayList
        lisFavSelf = new ArrayList<QuotesAssests>();
        if (Prefrence.getfavSelfArrayListPref(getActivity()) != null && Prefrence.getfavSelfArrayListPref(getActivity()).size() > 0) {
            lisFavSelf = Prefrence.getfavSelfArrayListPref(getActivity());
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, width);


        tv_quotes_edit = (TextView) view.findViewById(R.id.tv_quotes_edit);
        tv_quotes_edit.setText(Utilities.getQuoteUsingId(Container.getQuote_list(), lisFavSelf, Prefrence.getQuoteIdFromPref(getActivity()))
                + "\n" + getResources().getString(R.string.hash_message));
        tv_quotes_edit.setOnTouchListener(this);

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

        btn_quotes = (Button) view_button.findViewById(R.id.btn_quotes);
        btn_frames = (Button) view_button.findViewById(R.id.btn_frames);
        btn_likes = (Button) view_button.findViewById(R.id.btn_likes);

        getTextAndCompare();

        btn_count_bg = (Button) view_button.findViewById(R.id.btn_count_bg);
        tv_count = (TextView) view_button.findViewById(R.id.tv_count);

        // Check InApp Purchase Done Or Not To Change Text On TextView For Count Down...
        if (Prefrence.getInAppFromPref(getActivity())) {
            tv_count.setText("");
        }

        cancel = (ImageButton) view.findViewById(R.id.cancel);

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

                } else if (isframevisible && isframeSelect) {
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

                    if (!Prefrence.getInAppFromPref(getActivity()) && list_Fav_Id != null) {
                        if (list_Fav_Id.size() < 3) {

                            list_Fav_Id.add(Prefrence.getQuoteIdFromPref(getActivity()));
                            Prefrence.insertArrayListPref(getActivity(), list_Fav_Id);

                        } else {

                            list_Fav_Id.remove(list_Fav_Id.size() - 1);
                            list_Fav_Id.add(0, Prefrence.getQuoteIdFromPref(getActivity()));
                            Prefrence.insertArrayListPref(getActivity(), list_Fav_Id);
                        }

                    } else if (Prefrence.getInAppFromPref(getActivity()) && list_Fav_Id != null && lisFavSelf != null) {
                        if (lisFavSelf.size() > 0) {
                            if ((list_Fav_Id.size() + lisFavSelf.size()) < 21) {

                                list_Fav_Id.add(Prefrence.getQuoteIdFromPref(getActivity()));
                                Prefrence.insertArrayListPref(getActivity(), list_Fav_Id);

                            } else {
                                for (int i = 0; i < lisFavSelf.size(); i++) {
                                    if (list_Fav_Id.get(list_Fav_Id.size() - 1).equals(lisFavSelf.get(i).getId())) {
                                        lisFavSelf.remove(i);
                                        Prefrence.insertSelfArrayListPref(getActivity(), lisFavSelf);
                                    }
                                }
                                list_Fav_Id.remove(list_Fav_Id.size() - 1);
                                list_Fav_Id.add(0, Prefrence.getQuoteIdFromPref(getActivity()));
                                Prefrence.insertArrayListPref(getActivity(), list_Fav_Id);
                            }

                        } else {
                            if (list_Fav_Id.size() < 21) {

                                list_Fav_Id.add(Prefrence.getQuoteIdFromPref(getActivity()));
                                Prefrence.insertArrayListPref(getActivity(), list_Fav_Id);

                            } else {
                                list_Fav_Id.remove(list_Fav_Id.size() - 1);
                                list_Fav_Id.add(0, Prefrence.getQuoteIdFromPref(getActivity()));
                                Prefrence.insertArrayListPref(getActivity(), list_Fav_Id);
                            }
                        }
                    }


                } else if ((Integer) btn_likes.getTag() == R.drawable.like_heart) {

                    btn_likes.setTag(R.drawable.heart_icon);
                    btn_likes.setBackgroundResource(R.drawable.heart_icon);

                    if (Prefrence.getfavArrayListPref(getActivity()) != null) {
                        for (int i = 0; i < Prefrence.getfavArrayListPref(getActivity()).size(); i++) {
                            if (Prefrence.getQuoteIdFromPref(getActivity()) == Prefrence.getfavArrayListPref(getActivity()).get(i)) {
                                list_Fav_Id = Prefrence.getfavArrayListPref(getActivity());
                                list_Fav_Id.remove(i);
                                Prefrence.insertArrayListPref(getActivity(), list_Fav_Id);
                            }
                        }
                    }

                    if (lisFavSelf != null && lisFavSelf.size() > 0) {
                        for (int i = 0; i < lisFavSelf.size(); i++) {
                            if (Prefrence.getQuoteIdFromPref(getActivity()) == lisFavSelf.get(i).getId()) {
                                lisFavSelf.remove(i);
                                Prefrence.insertSelfArrayListPref(getActivity(), lisFavSelf);

                            }
                        }

                        Prefrence.setDateFromPref(getActivity(), Utilities.getCurrentDate());
                        Prefrence.setQuoteIdFromPref(getActivity(), -1);
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

                    Prefrence.setDateFromPref(getActivity(), Utilities.getCurrentDate());
                    Prefrence.setQuoteIdFromPref(getActivity(), -1);
                    tv_quotes_edit.setText(Utilities.getRandomQuote(getActivity(), Container.getQuote_list()) + "\n" + getResources().getString(R.string.hash_message));
                    getTextAndCompare();

                } else if (tv_count.getText().equals("")) {

                    Prefrence.setDateFromPref(getActivity(), Utilities.getCurrentDate());
                    Prefrence.setQuoteIdFromPref(getActivity(), -1);
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
                if (!Prefrence.getInAppFromPref(getActivity())) {
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

    }

    // get text from text view and then compare with favourite list and main quote list and set favourite heart on button red...
    private void getTextAndCompare() {
        // Here To Check favourite Quotes Compare With Text as shown on text view...
        if (list_Fav_Id != null && list_Fav_Id.size() > 0) {
            for (int j = 0; j < list_Fav_Id.size(); j++) {
                for (int i = 0; i < Container.getQuote_list().size(); i++) {
                    if (tv_quotes_edit.getText().toString().equals(Container.getQuote_list().get(i).getQuotes() + "\n" + Container.getQuote_list().get(i).getAuthor() + "\n" + getResources().getString(R.string.hash_message))) {
                        if (Container.getQuote_list().get(i).getId().equals(list_Fav_Id.get(j))) {
                            Prefrence.setBackStackFromPref(getActivity(), true);
                        }
                    }
                }
            }
        }

        if (lisFavSelf != null && lisFavSelf.size() > 0) {
            for (int i = 0; i < lisFavSelf.size(); i++) {
                if (Prefrence.getQuoteIdFromPref(getActivity()) == lisFavSelf.get(i).getId()) {
                    Prefrence.setBackStackFromPref(getActivity(), true);
                }
            }
        }

        if (Prefrence.getBackStackFromPref(getActivity())) {
            btn_likes.setTag(R.drawable.like_heart);
            btn_likes.setBackgroundResource(R.drawable.like_heart);
            Prefrence.setBackStackFromPref(getActivity(), false);
        } else {
            btn_likes.setTag(R.drawable.heart_icon);
            btn_likes.setBackgroundResource(R.drawable.heart_icon);
            Prefrence.setBackStackFromPref(getActivity(), false);
        }
    }

    private void rotatePicture(int rotation, byte[] data, ImageView photoImageView, String tag) {
        Bitmap bitmap = null;
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
        adapter = new FrameAdapter(getActivity(), imgs, bitmap);
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
            resultBitmap = Bitmap.createBitmap(v1.getDrawingCache(true));
            try {
                resultBitmap = Bitmap.createScaledBitmap(resultBitmap, v1.getMeasuredWidth(), v1.getMeasuredHeight(), false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            v1.setDrawingCacheEnabled(false);

            screenShotBitmap = null;
            screenShotBitmap = resultBitmap;
            Container.setBitmap(screenShotBitmap);

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
        int height = displayMetrics.heightPixels;


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

        mHelper.launchPurchaseFlow(getActivity(), ITEM_SKU, 10001,
                mPurchaseFinishedListener, "mypurchasetoken");

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
}
