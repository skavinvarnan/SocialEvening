package com.kavin.socialevening.activities;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.kavin.socialevening.R;
import com.kavin.socialevening.helper.BitmapHelper;
import com.kavin.socialevening.views.RoundedImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateTeamScreen extends BaseActivity {

    @Bind(R.id.image)
    protected RoundedImageView mProfileImage;

    private BitmapHelper mBitmapHelper;

    final private int CAPTURE_IMAGE = 2;

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
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        final NotificationCompat.Builder notif = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("title")
                .setContentText("text")
                        //      .setTicker(getString(R.string.tick)) removed, seems to not show at all
                        //      .setWhen(System.currentTimeMillis()) removed, match default
                        //      .setContentIntent(contentIntent) removed, I don't neet it
                .setColor(getResources().getColor(R.color.color_primary)) //ok
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.facebook_login_blue) //ok

                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.team_avatar))
                        //      .setCategory(Notification.CATEGORY_CALL) does not seem to make a difference
                .setPriority(Notification.DEFAULT_VIBRATE); //does not seem to make a difference
        //      .setVisibility(Notification.VISIBILITY_PRIVATE); //does not seem to make a difference


        //Yes intent
        Intent yesReceive = new Intent();
        yesReceive.setAction("yes");
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(this, 12345, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        notif.addAction(R.drawable.ic_done_black, "Yes", pendingIntentYes);

//Maybe intent
        Intent maybeReceive = new Intent();
        maybeReceive.setAction("maybe");
        PendingIntent pendingIntentMaybe = PendingIntent.getBroadcast(this, 12345, maybeReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        notif.addAction(R.drawable.ic_clear_black, "Partly", pendingIntentMaybe);




        notificationManager.notify(123, notif.build());
    }

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        String action = intent.getAction();
//
//        if("yes".equals(action)) {
//            Log.v("shuffTest", "Pressed YES");
//        } else if("maybe".equals(action)) {
//            Log.v("shuffTest","Pressed NO");
//        } else if("no_action".equals(action)) {
//            Log.v("shuffTest","Pressed MAYBE");
//        }
//    }




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
            }
        }

    }



}
