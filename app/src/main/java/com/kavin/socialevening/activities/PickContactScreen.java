package com.kavin.socialevening.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.kavin.socialevening.R;
import com.kavin.socialevening.adapter.ContactsAdapter;
import com.kavin.socialevening.interfaces.ContactSelectedListener;
import com.kavin.socialevening.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PickContactScreen extends BaseActivity implements ContactSelectedListener {

    @Bind(R.id.email_id)
    protected EditText mEmailId;
    @Bind(R.id.add)
    protected ImageButton mAdd;
    @Bind(R.id.or)
    protected TextView mOr;
    @Bind(R.id.contact_list)
    protected TextView mContactList;
    @Bind(R.id.list)
    protected ListView mList;

    private ContactsAdapter mContactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_contact_screen);
        setColors(R.color.color_primary, R.color.color_primary_dark, R.color.color_primary);
        ButterKnife.bind(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add Friend");
        }
        mContactsAdapter = new ContactsAdapter(this);
        mContactsAdapter.setContactSelectedListener(this);
        mList.setAdapter(mContactsAdapter);
    }

    private void selectedContactEmail(String email, String name) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(CreateTeamScreen.EMAIL, email);
        returnIntent.putExtra(CreateTeamScreen.NAME, name);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            case 1001:
                String shareBody = "Hey guys! Come and join my team on Social Evening. Its a fantastic" +
                        " app which allow you to take challenges. Download it here https://www.dropbox.com/s/zv9urucwrloxz3e/app-debug.apk?dl=0";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Invite Friends");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1001, Menu.NONE, "Just Invite").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @OnClick(R.id.add)
    protected void addEmailId() {
        if (TextUtils.isEmpty(mEmailId.getText().toString().trim())) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Information");
            alertDialog.setMessage("An email address is required");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            alertDialog.show();
        } else if (!Utils.isValidEmail(mEmailId.getText().toString().trim())) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Information");
            alertDialog.setMessage("Enter a valid email id");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            alertDialog.show();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Would you like " + mEmailId.getText().toString().trim() + " to be your in your team?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    selectedContactEmail(mEmailId.getText().toString().trim(), null);
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            alertDialog.show();
        }
    }


    @Override
    public void onContactSelected(final String email, final String name) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Would you like " + name + " to be your in your team?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                selectedContactEmail(email, name);
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        alertDialog.show();
    }
}
