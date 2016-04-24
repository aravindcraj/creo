package com.aravindraj.creo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aravindraj.creo.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;

/**
 * Created by AravindRaj on 24-04-2016.
 */
public class SignUpActivity extends AppCompatActivity {

    private EditText username;
    private EditText pass;
    private EditText cpass;
    private EditText mailid;
    private Button signup,login;
    private CoordinatorLayout coordinatorLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            return;
        }

        setContentView(R.layout.activity_signup);

        // Set up the signup form.
        username = (EditText) findViewById(R.id.signup_username);
        mailid = (EditText) findViewById(R.id.signup_emailid);
        pass = (EditText) findViewById(R.id.signup_pass);
        cpass = (EditText) findViewById(R.id.signup_cpass);
        signup = (Button) findViewById(R.id.signup_submit);
        login = (Button) findViewById(R.id.signup_login);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.scoordinatorLayout);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });

        // Set up the submit button click handler
        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // Validate the sign up data
                boolean validationError = false;
                StringBuilder validationErrorMessage =
                        new StringBuilder(getResources().getString(R.string.error_intro));
                if (isEmpty(username)) {
                    validationError = true;
                    validationErrorMessage.append(getResources().getString(R.string.valid_uname));
                }
                if (isEmpty(mailid)) {
                    validationError = true;
                    validationErrorMessage.append(",");
                    validationErrorMessage.append(getResources().getString(R.string.valid_mailid));
                }

                if (isEmpty(pass)) {
                    if (validationError) {
                        validationErrorMessage.append(",");
                    }
                    validationError = true;
                    validationErrorMessage.append(getResources().getString(R.string.valid_pass));
                }
                if (isEmpty(cpass)) {
                    validationError = true;
                    validationErrorMessage.append(",");
                    validationErrorMessage.append(getResources().getString(R.string.valid_cpass));
                }
                if (!isMatching(pass, cpass)) {
                    if (validationError) {
                        validationErrorMessage.append(",");
                    }
                    validationError = true;

                    validationErrorMessage.append(getResources().getString(
                            R.string.valid_cpass));
                }
                validationErrorMessage.append(getResources().getString(R.string.error_end));

                // If there is a validation error, display the error
                if (validationError) {
                    Snackbar.make(coordinatorLayout, validationErrorMessage.toString(), Snackbar.LENGTH_LONG).show();
                    return;
                }


                // Set up a progress dialog
                final ProgressDialog dlg = new ProgressDialog(SignUpActivity.this);
                dlg.setTitle("Please wait");
                dlg.setMessage("Signing up..");
                dlg.show();

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                        R.mipmap.user);
                // Convert it to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Compress image to lower quality scale 1 - 100
                bitmap.compress(Bitmap.CompressFormat.PNG, 10, stream);
                byte[] image = stream.toByteArray();

                // Create the ParseFile
                final ParseFile file = new ParseFile(username.getText().toString() + ".png", image);
                // Upload the image into Parse Cloud
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                            // Set up a new Parse user
                            ParseUser user = new ParseUser();
                            user.setUsername(username.getText().toString());
                            user.setPassword(pass.getText().toString());
                            user.setEmail(mailid.getText().toString());
                            user.put("name", username.getText().toString());
                            user.put("profilePic",file);
                            user.put("access","null");


                            // Call the Parse signup method
                            user.signUpInBackground(new SignUpCallback() {

                                @Override
                                public void done(ParseException e) {
                                    dlg.dismiss();
                                    if (e != null) {
                                        // Show the error message
                                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    } else {
                                        // Start an intent for the dispatch activity
                                        Intent intent = new Intent(SignUpActivity.this, DispatchActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });


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

    private boolean isMatching(EditText etText1, EditText etText2) {
        if (etText1.getText().toString().equals(etText2.getText().toString())) {
            return true;
        } else {
            return false;
        }

    }
}
