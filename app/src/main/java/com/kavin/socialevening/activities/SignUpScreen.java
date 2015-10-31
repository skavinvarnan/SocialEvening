package com.kavin.socialevening.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.kavin.socialevening.R;
import com.kavin.socialevening.utils.Constants;
import com.kavin.socialevening.utils.Utils;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpScreen extends BaseActivity {

    @Bind(R.id.email)
    protected EditText mEmail;
    @Bind(R.id.password)
    protected EditText mPassword;
    @Bind(R.id.name)
    protected EditText mName;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Signing up...");
        mProgressDialog.setCancelable(false);
        ButterKnife.bind(this);
        setColors(R.color.color_primary, R.color.color_primary, R.color.color_primary);
    }

    @OnClick(R.id.sign_up_button)
    protected void signUp() {
        if (Utils.isOnline()) {
            if (validate()) {
                mProgressDialog.show();
                ParseUser user = new ParseUser();
                user.setUsername(mEmail.getText().toString().trim());
                user.setPassword(mPassword.getText().toString());
                user.setEmail(mEmail.getText().toString().trim());
                user.put(Constants.Parse.User.NAME, mName.getText().toString().trim());
                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            proceedToNextScreen();
                        } else {
                            mProgressDialog.dismiss();
                            showQuickDialog("Error", "Error occurred while saving details to the server", false);
                        }
                    }
                });
            }
        } else {
            showQuickDialog("No internet", "Please make sure that you are connected to the internet", false);
        }
    }

    private boolean validate() {
        mEmail.setError(null);
        mPassword.setError(null);
        mName.setError(null);

        if (TextUtils.isEmpty(mName.getText().toString().trim())) {
            mName.setError("Enter your name");
            return false;
        }

        if (TextUtils.isEmpty(mEmail.getText().toString().trim())) {
            mEmail.setError("Email id is required");
            return false;
        }

        if (!Utils.isValidEmail(mEmail.getText().toString().trim())) {
            mEmail.setError("Enter a valid email id");
            return false;
        }

        if (TextUtils.isEmpty(mPassword.getText().toString())) {
            mPassword.setError("Password is required");
            return false;
        }

        return true;
    }

    @OnClick(R.id.login_with_fb)
    protected void loginWithFb() {
        if (Utils.isOnline()) {
            mProgressDialog.show();
            final List<String> permissions = Arrays.asList("public_profile", "email");
            ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException err) {
                    if (user == null) {
                        mProgressDialog.dismiss();
                        showQuickDialog("Facebook login", "User cancelled the facebook login", false);
                    } else if (user.isNew()) {
                        fetchMoreDetailsFromFacebook();
                    } else {
                        fetchMoreDetailsFromFacebook();
                    }
                }
            });
        } else {
            showQuickDialog("No internet", "Please make sure that you are connected to the internet", false);
        }
    }

    private void fetchMoreDetailsFromFacebook() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            String id = object.getString("id");
                            String name = Profile.getCurrentProfile().getName();
                            String email = object.getString("email");
                            saveMoreInfoToParse(id, name, email);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void saveMoreInfoToParse(String id, String name, String email) {
        ParseUser user = ParseUser.getCurrentUser();
        user.setEmail(email);
        user.put(Constants.Parse.User.FB_NAME, name);
        user.put(Constants.Parse.User.FB_ID, id);
        user.saveInBackground(new SaveCallback() {
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    proceedToNextScreen();
                } else {
                    mProgressDialog.dismiss();
                    showQuickDialog("Error", "Error occurred while saving details to the server", false);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this, SplashScreen.class));
    }

    private void proceedToNextScreen() {
        finish();
        mProgressDialog.dismiss();
        startActivity(new Intent(this, WelcomeScreen.class));
    }
}
