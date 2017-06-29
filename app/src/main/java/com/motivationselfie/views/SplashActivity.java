package com.motivationselfie.views;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.motivationselfie.Constant;
import com.motivationselfie.R;
import com.motivationselfie.runtimepermissions.RuntimePermission;

@SuppressWarnings("ALL")
public class SplashActivity extends BaseActivity implements Constant {

    RuntimePermission runtimePermission = new RuntimePermission(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.motivationselfie_CameraFullScreenTheme);
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_splash);

        parseJSONQuotes();

        new Handler().postDelayed(new Runnable() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {

                if (runtimePermission.checkPermissionForCamera()
                        && runtimePermission.checkPermissionForWriteExternalStorage()
                        && runtimePermission.checkPermissionForReadExternalStorage()) {

                    nextActivity();

                } else {
                    runtimePermission.commonPermissionForApp();
                }
            }
        }, 2000);
    }

    public void nextActivity() {
        Intent i = new Intent(SplashActivity.this, CameraActivity.class);
        startActivity(i);
        finish();
    }

    // Request Call Back Method To check permission is granted by user or not for MarshMallow
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case COMMON_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    nextActivity();
                } else {
                    finish();
                }
            }
        }
    }
}