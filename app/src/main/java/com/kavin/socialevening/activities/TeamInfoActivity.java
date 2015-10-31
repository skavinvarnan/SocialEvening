package com.kavin.socialevening.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kavin.socialevening.R;
import com.kavin.socialevening.utils.Constants;
import com.kavin.socialevening.views.RoundedImageView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TeamInfoActivity extends BaseActivity {

    private static final int PICK_CONTACT = 1002;

    public static final String EMAIL = "email";

    private ParseObject mTeamObject;

    @Bind(R.id.image)
    protected ImageView mTeamImage;

    @Bind(R.id.address)
    protected TextView mAddress;

    @Bind(R.id.list)
    protected ListView mList;

    @Bind(R.id.requested)
    protected LinearLayout mRequested;

    @Bind(R.id.accept)
    protected ImageView mAccept;

    @Bind(R.id.reject)
    protected ImageView mReject;

    @Bind(R.id.main_layout)
    protected RelativeLayout mMainLayout;

    @Bind(R.id.progress_bar)
    protected ProgressBar mProgressBar;

    private List<String> mJoinedFriends;
    private List<String> mFriendsList;
    private ProgressDialog mProgressDialog;

    private TeamInfoMemberAdapter mTeamInfoMemberAdapter;

    private boolean mShowAddPersonButton = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        ButterKnife.bind(this);
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getString(Constants.Intent.OBJECT_ID) != null) {
            mMainLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Parse.Team.TEAM);
            query.getInBackground(getIntent().getExtras().getString(Constants.Intent.OBJECT_ID), new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        mTeamObject = object;
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
            getSupportActionBar().setTitle("Team");
        }
    }

    private void initViews() {
        ParseUser teamAdmin = mTeamObject.getParseUser(Constants.Parse.Team.TEAM_ADMIN);
        if (!teamAdmin.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
            List<String> friends = (List<String>) mTeamObject.get(Constants.Parse.Team.JOINED_FRIENDS);
            if (!friends.contains(ParseUser.getCurrentUser().getEmail())) {
                mShowAddPersonButton = false;
                mRequested.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();
            }
        }

        ParseFile imageFile = mTeamObject.getParseFile(Constants.Parse.Team.PICTURE);
        Picasso.with(this).load(imageFile.getUrl()).error(R.drawable.team_avatar).placeholder(R.drawable.team_avatar)
                .into(mTeamImage);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mTeamObject.getString(Constants.Parse.Team.NAME));
        }

        mAddress.setText("@ " + mTeamObject.getString(Constants.Parse.Team.LOCATION_NAME));

        mJoinedFriends = (List<String>) mTeamObject.get(Constants.Parse.Team.JOINED_FRIENDS);
        mFriendsList = (List<String>) mTeamObject.get(Constants.Parse.Team.FRIENDS_LIST);

        mTeamInfoMemberAdapter = new TeamInfoMemberAdapter();

        mList.setAdapter(mTeamInfoMemberAdapter);


        Log.d("asdf", "sadf");
    }

    @OnClick(R.id.accept)
    protected void accepted() {
        mProgressDialog.show();
        mJoinedFriends.add(ParseUser.getCurrentUser().getEmail());
        mTeamObject.remove(Constants.Parse.Team.JOINED_FRIENDS);
        mTeamObject.put(Constants.Parse.Team.JOINED_FRIENDS, mJoinedFriends);
        mTeamObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Awesome. You are joined " + mTeamObject.getString(Constants.Parse.Team.NAME)
                            , Toast.LENGTH_SHORT).show();
                } else {
                    mProgressDialog.dismiss();
                }
            }
        });
    }

    @OnClick(R.id.reject)
    protected void rejected() {
        mProgressDialog.show();
        mFriendsList.remove(ParseUser.getCurrentUser().getEmail());
        mTeamObject.remove(Constants.Parse.Team.FRIENDS_LIST);
        mTeamObject.put(Constants.Parse.Team.FRIENDS_LIST, mFriendsList);
        mTeamObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "You have rejected the request."
                            , Toast.LENGTH_SHORT).show();
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
            case 1001:
                startActivityForResult(new Intent(this, PickContactScreen.class), PICK_CONTACT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_CONTACT) {
                if (data != null && data.getExtras() != null && data.getExtras().getString(EMAIL) != null) {
                    final String email = data.getExtras().getString(EMAIL);
                    mProgressDialog.setMessage("Adding " + email);
                    mProgressDialog.show();
                    mFriendsList.add(email);
                    mTeamObject.remove(Constants.Parse.Team.FRIENDS_LIST);
                    mTeamObject.put(Constants.Parse.Team.FRIENDS_LIST, mFriendsList);
                    mTeamObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                mTeamInfoMemberAdapter.notifyDataSetChanged();
                                mFriendsList = (List<String>) mTeamObject.get(Constants.Parse.Team.FRIENDS_LIST);
                                mProgressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Added " + email
                                        , Toast.LENGTH_SHORT).show();

                            } else {
                                mProgressDialog.dismiss();
                            }
                        }
                    });
                    mProgressDialog.setMessage("Please wait...");
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mShowAddPersonButton) {
            menu.add(Menu.NONE, 1001, Menu.NONE, "Create").setIcon(R.drawable.ic_person_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return true;
    }

    class TeamInfoMemberAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return mFriendsList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(TeamInfoActivity.this).inflate(R.layout.team_info_list_item, null);
                viewHolder.image = (RoundedImageView) convertView.findViewById(R.id.image);
                viewHolder.teamName = (TextView) convertView.findViewById(R.id.name);
                viewHolder.status = (TextView) convertView.findViewById(R.id.status);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.teamName.setText("Loading");
            viewHolder.status.setText("Loading");
            ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Parse.User.USER);
            query.whereEqualTo(Constants.Parse.User.EMAIL, mFriendsList.get(position));
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        if (object.getString(Constants.Parse.User.FB_NAME) != null) {
                            viewHolder.teamName.setText(object.getString(Constants.Parse.User.FB_NAME));
                            Picasso.with(TeamInfoActivity.this).load("https://graph.facebook.com/" + object.getString(Constants.Parse.User.FB_ID) +
                                            "/picture?type=normal"
                                    )
                                    .error(R.drawable.ic_face_black).placeholder(R.drawable.ic_face_black)
                                    .into(viewHolder.image);
                        } else {
                            viewHolder.teamName.setText(object.getString(Constants.Parse.User.NAME));
                        }
                        ParseUser teamAdmin = mTeamObject.getParseUser(Constants.Parse.Team.TEAM_ADMIN);
                        if (teamAdmin.getObjectId().equals(object.getObjectId())) {
                            viewHolder.status.setText("Team admin");
                        } else {
                            if (mJoinedFriends.contains(object.getString(Constants.Parse.User.EMAIL))) {
                                viewHolder.status.setText("Member");
                            } else {
                                viewHolder.status.setText("Requested to join");
                            }
                        }
                    } else {
                        viewHolder.teamName.setText(mFriendsList.get(position));
                        viewHolder.status.setText("Not on Social Evening");
                    }
                }
            });


            return convertView;
        }

        class ViewHolder {
            public RoundedImageView image;
            public TextView teamName;
            public TextView createdAt;
            public TextView status;
        }
    }
}
