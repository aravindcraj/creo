package com.aravindraj.creo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aravindraj.creo.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

/**
 * Created by AravindRaj on 24-04-2016.
 */
public class SignInActivity extends AppCompatActivity {

    private EditText login_mailid;
    private EditText login_pass;
    private Button login;
    private TextView fgtpass;
    private Button signup;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        login_mailid = (EditText) findViewById(R.id.login_uname);
        login_pass = (EditText) findViewById(R.id.login_pass);
        login = (Button) findViewById(R.id.login_submit);
        signup = (Button) findViewById(R.id.login_signup);
        fgtpass = (TextView) findViewById(R.id.fgt_pass);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));


            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean validationError = false;
                StringBuilder validationErrorMessage =
                        new StringBuilder(getResources().getString(R.string.error_intro));
                if (isEmpty(login_mailid)) {
                    validationError = true;
                    validationErrorMessage.append(getResources().getString(R.string.valid_uname));
                }
                if (isEmpty(login_pass)) {
                    if (validationError) {
                        validationErrorMessage.append(getResources().getString(R.string.error_join));
                    }
                    validationError = true;
                    validationErrorMessage.append(getResources().getString(R.string.valid_pass));
                }
                validationErrorMessage.append(getResources().getString(R.string.error_end));

                // If there is a validation error, display the error
                if (validationError) {

                    Snackbar.make(coordinatorLayout, validationErrorMessage.toString(), Snackbar.LENGTH_LONG).show();
                    return;
                }

                final ProgressDialog dlg = new ProgressDialog(SignInActivity.this);
//                dlg.setTitle("Please wait.");
                dlg.setMessage("Logging into, creo");
                dlg.show();

                ParseUser.logInInBackground(login_mailid.getText().toString(), login_pass.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {


                        dlg.dismiss();
                        if (e != null) {
                            Snackbar.make(coordinatorLayout, e.getMessage(), Snackbar.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(SignInActivity.this, DispatchActivity.class);
                            intent.addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
        fgtpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.requestPasswordResetInBackground(login_mailid.getText().toString(), new RequestPasswordResetCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            login_mailid.setHint("email");
                            Snackbar.make(coordinatorLayout, e.getMessage(), Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(coordinatorLayout, "Reset password information has been sent", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    }
