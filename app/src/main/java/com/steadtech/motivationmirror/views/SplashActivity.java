package com.steadtech.motivationmirror.views;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import com.steadtech.motivationmirror.Constant;
import com.steadtech.motivationmirror.R;
import com.steadtech.motivationmirror.marshmallowpermissions.MarshMallowPermission;

public class SplashActivity extends BaseActivity implements Constant {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.motivationalmirror__CameraFullScreenTheme);
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_splash);

        parseJSONQuotes();

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                if (marshMallowPermission.checkPermissionForCamera() == true
                        && marshMallowPermission.checkPermissionForWriteExternalStorage()
                        && marshMallowPermission.checkPermissionForReadExternalStorage()) {
                    nextActivity();


                } else {
                    marshMallowPermission.commonPermissionForApp();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    public void nextActivity() {
        // This method will be executed once the timer is over
        // Start your app main activity
        Intent i = new Intent(SplashActivity.this, CameraActivity.class);
        startActivity(i);
        // close this activity
        finish();
    }

    // Request Call Back Method To check permission is granted by user or not for MarshMallow...
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case COMMON_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    nextActivity();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    finish();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}

