package com.kavin.socialevening.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.kavin.socialevening.R;
import com.kavin.socialevening.helper.BitmapHelper;
import com.kavin.socialevening.helper.GpsHelper;
import com.kavin.socialevening.interfaces.GpsLocationListener;
import com.kavin.socialevening.utils.Constants;
import com.kavin.socialevening.views.RoundedImageView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

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

    @Bind(R.id.no_users)
    protected TextView mNoUser;

    @Bind(R.id.team_name)
    protected EditText mTeamName;

    private BitmapHelper mBitmapHelper;

    private Bitmap mSelectedImage = null;

    private static final int CAPTURE_IMAGE = 1001;
    private static final int PICK_CONTACT = 1002;

    public static final String EMAIL = "email";
    public static final String NAME = "name";

    private GpsHelper mGpsHelper;

    private double mObtainedLatitude;
    private double mObtainedLongitude;

    private List<Friend> mFriendList = new ArrayList<Friend>();

    private MyTeamAdapter mMyTeamAdapter;
    private String mObtainedLocation;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        setContentView(R.layout.activity_create_team_screen);
        ButterKnife.bind(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setColors(R.color.color_primary, R.color.color_primary_dark, R.color.color_primary);
        mBitmapHelper = new BitmapHelper(this);
        mGpsHelper = new GpsHelper(this);
        mGpsHelper.setGpsLocationListener(this);
        mGpsHelper.checkIfGpsOnAndRequestLocationInfo();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Create Team");
        }
        mMyTeamAdapter = new MyTeamAdapter();
        mList.setAdapter(mMyTeamAdapter);
        mList.setVisibility(View.GONE);
        mNoUser.setVisibility(View.VISIBLE);

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
                mSelectedImage = mBitmapHelper.rotateImage(mBitmapHelper.getImagePath());
                mProfileImage.setImageBitmap(mSelectedImage);
            } else if (requestCode == PICK_CONTACT) {
                if (data != null && data.getExtras() != null && data.getExtras().getString(EMAIL) != null) {
                    String email = data.getExtras().getString(EMAIL);
                    String name = data.getExtras().getString(NAME);
                    if (checkIfAlreadyAdded(email)) {
                        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                        alertDialog.setTitle("Information");
                        alertDialog.setMessage("This user has already been added");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                        alertDialog.show();
                    } else {
                        Friend friend = new Friend();
                        friend.name = name;
                        friend.email = email;
                        mFriendList.add(friend);
                        mMyTeamAdapter.notifyDataSetChanged();
                    }

                    if (mFriendList.size() == 0) {
                        mList.setVisibility(View.GONE);
                        mNoUser.setVisibility(View.VISIBLE);
                    } else {
                        mList.setVisibility(View.VISIBLE);
                        mNoUser.setVisibility(View.GONE);
                    }
                }
            }
        }

    }

    private boolean checkIfAlreadyAdded(String email) {
        for (Friend friend : mFriendList) {
            if (friend.email.equals(email)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onGpsLocationObtainedListener(String location, double latitude, double longitude) {
        mObtainedLatitude = latitude;
        mObtainedLongitude = longitude;
        mAddress.setText("@ " + location);
        mObtainedLocation = location;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            case 1001:
                createTeam();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createTeam() {
        if (validate()) {
            if (mFriendList.size() == 0) {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("No Friends added");
                alertDialog.setMessage("Are you sure you dont want to invite friends?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        makeCreateTeamCall();
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                alertDialog.show();
            } else {
                makeCreateTeamCall();
            }
        }
    }

    Bitmap bitmap;

    private void makeCreateTeamCall() {
        mProgressDialog.show();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mSelectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        mProgressDialog.setMessage("Uploading image");
        final ParseFile parseFile = new ParseFile("photo.jpg", byteArray);
        parseFile.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    saveTeam(parseFile);
                } else {
                    mProgressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new ProgressCallback() {
            public void done(Integer percentDone) {
                mProgressDialog.setMessage("Uploading image: " + percentDone);
            }
        });

    }

    private void saveTeam(ParseFile parseFile) {
        mProgressDialog.setMessage("Image uploaded. Saving team");
        List<String> strings = new ArrayList<String>();
        for (Friend friend: mFriendList) {
            strings.add(friend.email);
        }

        List<String> joinedFriends = new ArrayList<String>();
        strings.add(ParseUser.getCurrentUser().getEmail());
        joinedFriends.add(ParseUser.getCurrentUser().getEmail());

        ParseObject team = new ParseObject(Constants.Parse.Team.TEAM);
        team.put(Constants.Parse.Team.PICTURE, parseFile);
        team.put(Constants.Parse.Team.NAME, mTeamName.getText().toString().trim());
        team.put(Constants.Parse.Team.LOCATION_NAME, mObtainedLocation);
        team.put(Constants.Parse.Team.LATITUDE, mObtainedLatitude);
        team.put(Constants.Parse.Team.LONGITUDE, mObtainedLongitude);
        team.put(Constants.Parse.Team.FRIENDS_LIST, strings);
        team.put(Constants.Parse.Team.JOINED_FRIENDS, joinedFriends);
        team.put(Constants.Parse.Team.TEAM_ADMIN, ParseUser.getCurrentUser());
        team.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    mProgressDialog.dismiss();
                } else {
                    mProgressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean validate() {
        mTeamName.setError(null);
        if (TextUtils.isEmpty(mTeamName.getText().toString().trim())) {
            mTeamName.setError("Enter team name");
            return false;
        } else if (mSelectedImage == null) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Picture needed");
            alertDialog.setMessage("Please take a selfie, by tapping the avatar icon");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            alertDialog.show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1001, Menu.NONE, "Create").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    class MyTeamAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mFriendList.size();
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
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(CreateTeamScreen.this).inflate(R.layout.invite_item, null);
                viewHolder.name = (TextView) convertView.findViewById(R.id.title);
                viewHolder.email = (TextView) convertView.findViewById(R.id.sub_title);
                viewHolder.remove = (ImageButton) convertView.findViewById(R.id.add);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (mFriendList.get(position).name == null) {
                viewHolder.name.setText("(no name)");
            } else {
                viewHolder.name.setText(mFriendList.get(position).name);
            }
            viewHolder.email.setText(mFriendList.get(position).email);
            viewHolder.remove.setImageResource(R.drawable.ic_clear_black_sm);

            viewHolder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFriendList.remove(position);
                    mMyTeamAdapter.notifyDataSetChanged();

                    if (mFriendList.size() == 0) {
                        mList.setVisibility(View.GONE);
                        mNoUser.setVisibility(View.VISIBLE);
                    } else {
                        mList.setVisibility(View.VISIBLE);
                        mNoUser.setVisibility(View.GONE);
                    }
                }
            });

            return convertView;
        }

        class ViewHolder {
            public TextView name;
            public TextView email;
            public ImageButton remove;

        }
    }

    class Friend {
        public String name;
        public String email;
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, HomeScreen.class));
    }
}
