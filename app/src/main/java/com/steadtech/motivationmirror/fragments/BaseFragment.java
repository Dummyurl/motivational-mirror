package com.steadtech.motivationmirror.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.steadtech.motivationmirror.InAppUtils.IabHelper;
import com.steadtech.motivationmirror.InAppUtils.IabResult;
import com.steadtech.motivationmirror.InAppUtils.Inventory;
import com.steadtech.motivationmirror.InAppUtils.Purchase;
import com.steadtech.motivationmirror.R;
import com.steadtech.motivationmirror.interfaces.InterfaceContainer;
import com.steadtech.motivationmirror.utils.Prefrence;
import com.steadtech.motivationmirror.views.BaseActivity;

public class BaseFragment extends Fragment {
    public static BaseActivity mActivity;
    public static IabHelper mHelper;
    //public static String ITEM_SKU = "android.test.purchased";
    public static String ITEM_SKU = "motivationmirror";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new IabHelper(mActivity, getString(R.string.base64EncodedPublicKey));
        // enable debug logging (for a production application, you should set
        // this to false).
        mHelper.enableDebugLogging(true);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.e("startSetup", "isSuccessU" + result.isSuccess());
                } else {
                    Log.e("startSetup", "isSuccessD" + result.isSuccess());
                }
            }
        });
    }

    public IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                // Handle error
                Log.e("mPurchaseFinished", "isFailure?? " + result.isSuccess());
                return;

            } else if (purchase.getSku().equals(ITEM_SKU)) {
                // If You Are Not Getting Accurate Response With Test Product Use Below Link This Will Help You A Lot.
                // http://stackoverflow.com/questions/19732025/android-in-app-billing-purchase-verification-failed

                Log.e("mPurchaseFinished", "?? " + result.isSuccess());
                consumeItem();
            }
        }
    };

    public void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {
                // Handle failure
                Log.e("mReceivedInventory", "isFailure?? " + result.isSuccess());
            } else {

                Log.e("mReceivedInventory", "?? " + result.isSuccess());
                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
                        mConsumeFinishedListener);
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {

                        Log.e("Success", "True?? " + result.isSuccess());
                        Log.e("Purchase", "JSON_Accurate?? " + purchase.getOriginalJson().toString());

                        // If InApp Purchase Success Is True Then it Changes The UI and all things are free to the user's.
                        InterfaceContainer.quto.InAppSuccess(true);
                        Prefrence.setInAppFromPref(getActivity(), true);

                    } else {
                        // handle error
                        Log.e("mConsumeFinished", "mConsumeFinished?? " + result.isSuccess());
                    }
                }
            };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    // Reload Fragment...
    public static void reloadFragmenmt(Fragment fragment) {
        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
        ft.detach(fragment).attach(fragment).commit();
    }
}
