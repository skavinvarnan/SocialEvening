package com.kavin.socialevening.activities;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Copyright 2015 (C) Virtual Applets
 * Created on : 29/10/15
 * Author     : Kavin Varnan
 */
public class BaseActivity extends AppCompatActivity {

    boolean mDoubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            // Remove the shadow present between the actionBar and the main content
            getSupportActionBar().setElevation(0);
        }
    }

    /**
     * Override actionBar, statusBar and navigationBar color which was set on the theme of the activity
     * @param actionBar color of the actionBar
     * @param statusBar color of the statusBar
     * @param navBar color of the navigationBar if any
     */
    public void setColors(int actionBar, int statusBar, int navBar) {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (statusBar != 0) {
            window.setStatusBarColor(this.getResources().getColor(statusBar));
        }

        if (navBar != 0) {
            window.setNavigationBarColor(this.getResources().getColor(navBar));
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(actionBar)));
        }
    }


    /**
     * Show a basic dialog with appropriate message and title
     * @param title title of the dialog
     * @param message message of the dialog
     * @param shouldCloseActivity should the activity finish() when clicking on dismiss
     */
    public void showQuickDialog(String title, String message, final boolean shouldCloseActivity) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        if (shouldCloseActivity) {
            alertDialog.setCancelable(false);
        }
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (shouldCloseActivity) {
                            finish();
                        }
                    }
                });
        alertDialog.show();
    }

    public void doubleBackExit() {
        if (mDoubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.mDoubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mDoubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
