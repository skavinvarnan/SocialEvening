package com.kavin.socialevening.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.kavin.socialevening.R;
import com.kavin.socialevening.helper.BitmapHelper;
import com.kavin.socialevening.helper.GpsHelper;
import com.kavin.socialevening.interfaces.GpsLocationListener;
import com.kavin.socialevening.views.RoundedImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateTeamScreen extends BaseActivity implements GpsLocationListener {

    @Bind(R.id.image)
    protected RoundedImageView mProfileImage;

    @Bind(R.id.address)
    protected TextView mAddress;

    @Bind(R.id.list)
    protected ListView mList;

    private BitmapHelper mBitmapHelper;

    private static final int CAPTURE_IMAGE = 1001;
    private static final int PICK_CONTACT = 1002;

    public static final String EMAIL = "email";
    public static final String NAME = "name";

    private GpsHelper mGpsHelper;

    private double mObtainedLatitude;
    private double mObtainedLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team_screen);
        ButterKnife.bind(this);
        setColors(R.color.color_primary, R.color.color_primary, R.color.color_primary);
        mBitmapHelper = new BitmapHelper(this);
        mGpsHelper = new GpsHelper(this);
        mGpsHelper.setGpsLocationListener(this);
        mGpsHelper.checkIfGpsOnAndRequestLocationInfo();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Create Team");
        }
    }

    @OnClick(R.id.add_person)
    protected void addPerson() {
        startActivityForResult(new Intent(this, PickContactScreen.class), PICK_CONTACT);
    }

    @OnClick(R.id.image)
    protected void uploadSelfie() {
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mBitmapHelper.setImageUri());
        startActivityForResult(intent, CAPTURE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE) {
                mProfileImage.setImageBitmap(mBitmapHelper.rotateImage(mBitmapHelper.getImagePath()));
            } else if (requestCode == PICK_CONTACT) {
                if (data != null && data.getExtras() != null && data.getExtras().getString(EMAIL) != null) {
                    String email = data.getExtras().getString(EMAIL);
                    String name = data.getExtras().getString(NAME);
                    Log.d("kacin", email);
                }
            }
        }

    }


    @Override
    public void onGpsLocationObtainedListener(String location, double latitude, double longitude) {
        mObtainedLatitude = latitude;
        mObtainedLongitude = longitude;
        mAddress.setText("@ " + location);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
