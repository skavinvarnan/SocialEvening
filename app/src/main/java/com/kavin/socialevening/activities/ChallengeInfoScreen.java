package com.kavin.socialevening.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kavin.socialevening.R;
import com.kavin.socialevening.server.dto.PushDto;
import com.kavin.socialevening.utils.Constants;
import com.kavin.socialevening.utils.JsonUtils;
import com.kavin.socialevening.views.RoundedImageView;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChallengeInfoScreen extends BaseActivity {

    @Bind(R.id.challenge1)
    protected RoundedImageView mChallenge1;
    @Bind(R.id.challenge2)
    protected RoundedImageView mChallenge2;

    @Bind(R.id.team_name1)
    protected TextView mTeamName1;
    @Bind(R.id.team_name2)
    protected TextView mTeamName2;

    @Bind(R.id.created_at)
    protected TextView mCreatedAt;

    @Bind(R.id.requested)
    protected LinearLayout mRequested;

    @Bind(R.id.accept)
    protected ImageView mAccept;

    @Bind(R.id.reject)
    protected ImageView mReject;

    @Bind(R.id.main_layout)
    protected LinearLayout mMainLayout;

    @Bind(R.id.progress_bar)
    protected ProgressBar mProgressBar;


    private ParseObject mChallengeObject;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_info_screen);
        ButterKnife.bind(this);
        setColors(R.color.color_primary, R.color.color_primary_dark, R.color.color_primary);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getString(Constants.Intent.OBJECT_ID) != null) {
            mMainLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Parse.Challenge.CHALLENGE);
            query.include(Constants.Parse.Challenge.CHALLENGE_FROM);
            query.include(Constants.Parse.Challenge.CHALLENGE_TO);
            query.getInBackground(getIntent().getExtras().getString(Constants.Intent.OBJECT_ID), new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        mChallengeObject = object;
                        mMainLayout.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                        initViews();
                    } else {
                        mProgressBar.setVisibility(View.GONE);
                        showQuickDialog("Error", "Unable to fetch data from server", true);
                    }
                }
            });
        } else {
            mProgressBar.setVisibility(View.GONE);
            showQuickDialog("Error", "Unknown error occurred", true);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Challenge Info");
        }

    }

    private void initViews() {

        List<String> challengeToFriends = (List<String>) mChallengeObject.getParseObject(Constants.Parse.Challenge.CHALLENGE_TO)
                .get(Constants.Parse.Team.JOINED_FRIENDS);

        List<String> challengeFromFriends = (List<String>) mChallengeObject.getParseObject(Constants.Parse.Challenge.CHALLENGE_FROM)
                .get(Constants.Parse.Team.JOINED_FRIENDS);

        if (challengeFromFriends.contains(ParseUser.getCurrentUser().getEmail())) {
            ParseFile imageFile1 = mChallengeObject.getParseObject(Constants.Parse.Challenge.CHALLENGE_FROM).getParseFile(Constants.Parse.Team.PICTURE);
            Picasso.with(this).load(imageFile1.getUrl()).error(R.drawable.team_avatar).placeholder(R.drawable.team_avatar)
                    .into(mChallenge1);
            mTeamName1.setText(mChallengeObject.getParseObject(Constants.Parse.Challenge.CHALLENGE_FROM).getString(Constants.Parse.Team.NAME));

            ParseFile imageFile2 = mChallengeObject.getParseObject(Constants.Parse.Challenge.CHALLENGE_TO).getParseFile(Constants.Parse.Team.PICTURE);
            Picasso.with(this).load(imageFile2.getUrl()).error(R.drawable.team_avatar).placeholder(R.drawable.team_avatar)
                    .into(mChallenge2);
            mTeamName2.setText(mChallengeObject.getParseObject(Constants.Parse.Challenge.CHALLENGE_TO).getString(Constants.Parse.Team.NAME));

            mCreatedAt.setText("Your team has made the challenge");
        } else if (challengeToFriends.contains(ParseUser.getCurrentUser().getEmail())) {
            ParseFile imageFile1 = mChallengeObject.getParseObject(Constants.Parse.Challenge.CHALLENGE_TO).getParseFile(Constants.Parse.Team.PICTURE);
            Picasso.with(this).load(imageFile1.getUrl()).error(R.drawable.team_avatar).placeholder(R.drawable.team_avatar)
                    .into(mChallenge2);
            mTeamName2.setText(mChallengeObject.getParseObject(Constants.Parse.Challenge.CHALLENGE_TO).getString(Constants.Parse.Team.NAME));


            ParseFile imageFile2 = mChallengeObject.getParseObject(Constants.Parse.Challenge.CHALLENGE_FROM).getParseFile(Constants.Parse.Team.PICTURE);
            Picasso.with(this).load(imageFile2.getUrl()).error(R.drawable.team_avatar).placeholder(R.drawable.team_avatar)
                    .into(mChallenge1);
            mTeamName1.setText(mChallengeObject.getParseObject(Constants.Parse.Challenge.CHALLENGE_FROM).getString(Constants.Parse.Team.NAME));
            mCreatedAt.setText("Your have been challenged");

            if (!mChallengeObject.getBoolean(Constants.Parse.Challenge.ACCEPTED)) {
                mRequested.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick(R.id.accept)
    protected void accepted() {
        mProgressDialog.show();
        mChallengeObject.remove(Constants.Parse.Challenge.ACCEPTED);
        mChallengeObject.put(Constants.Parse.Challenge.ACCEPTED, true);
        mChallengeObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    mProgressDialog.dismiss();

                    List<String> challengeFromFriends = (List<String>) mChallengeObject.getParseObject(Constants.Parse.Challenge.CHALLENGE_FROM)
                            .get(Constants.Parse.Team.JOINED_FRIENDS);

                    ParseQuery pushQuery = ParseInstallation.getQuery();
                    pushQuery.whereContainedIn(Constants.Parse.User.EMAIL, challengeFromFriends);

                    ParsePush push = new ParsePush();
                    push.setQuery(pushQuery);
                    PushDto pushDto = new PushDto();
                    pushDto.setPushType(Constants.PushType.CHALLENGE_ACCEPTED);
                    if (ParseUser.getCurrentUser().get(Constants.Parse.User.FB_NAME) != null) {
                        pushDto.setMessage(mChallengeObject.getParseObject(Constants.Parse.Challenge.CHALLENGE_TO).getString(Constants.Parse.Team.NAME) + " has " +
                                " accepted your challenge");
                    }
                    push.setData(JsonUtils.convertObjectToJSONObject(pushDto));
                    push.sendInBackground();

                    Toast.makeText(getApplicationContext(), "You have accepted the challenge."
                            , Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    mProgressDialog.dismiss();
                }
            }
        });
    }

    @OnClick(R.id.reject)
    protected void rejected() {
        mProgressDialog.show();
        mChallengeObject.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    mProgressDialog.dismiss();

                    List<String> challengeFromFriends = (List<String>) mChallengeObject.getParseObject(Constants.Parse.Challenge.CHALLENGE_FROM)
                            .get(Constants.Parse.Team.JOINED_FRIENDS);

                    ParseQuery pushQuery = ParseInstallation.getQuery();
                    pushQuery.whereContainedIn(Constants.Parse.User.EMAIL, challengeFromFriends);

                    ParsePush push = new ParsePush();
                    push.setQuery(pushQuery);
                    PushDto pushDto = new PushDto();
                    pushDto.setPushType(Constants.PushType.CHALLENGE_DECLINED);
                    if (ParseUser.getCurrentUser().get(Constants.Parse.User.FB_NAME) != null) {
                        pushDto.setMessage(mChallengeObject.getParseObject(Constants.Parse.Challenge.CHALLENGE_TO).getString(Constants.Parse.Team.NAME) + " has " +
                                " rejected your challenge");
                    }
                    push.setData(JsonUtils.convertObjectToJSONObject(pushDto));
                    push.sendInBackground();

                    Toast.makeText(getApplicationContext(), "You have rejected the challenge."
                            , Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    mProgressDialog.dismiss();
                }
            }
        });
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
