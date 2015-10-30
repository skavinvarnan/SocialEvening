package com.kavin.socialevening.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

import com.kavin.socialevening.R;
import com.kavin.socialevening.helper.BitmapHelper;
import com.kavin.socialevening.views.RoundedImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateTeamScreen extends BaseActivity {

    @Bind(R.id.image)
    protected RoundedImageView imgFromCameraOrGallery;

    private BitmapHelper mBitmapHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team_screen);
        ButterKnife.bind(this);
        setColors(R.color.color_primary, R.color.color_primary, R.color.color_primary);
        mBitmapHelper = new BitmapHelper(this);
    }

    @OnClick(R.id.image)
    protected void uploadSelfie() {
        //dispatchTakePictureIntent();
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mBitmapHelper.setImageUri());
        startActivityForResult(intent, CAPTURE_IMAGE);
    }
    final private int CAPTURE_IMAGE = 2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE) {
                imgFromCameraOrGallery.setImageBitmap(mBitmapHelper.rotateImage(mBitmapHelper.getImagePath()));
            }
        }

    }



}
