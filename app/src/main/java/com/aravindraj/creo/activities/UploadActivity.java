package com.aravindraj.creo.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aravindraj.creo.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by AravindRaj on 24-04-2016.
 */
public class UploadActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView image;
    private EditText desp;
    private Button upload;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_CAMERA = 1888;
    String uriString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        image = (ImageView) findViewById(R.id.upload_preview);
        desp = (EditText) findViewById(R.id.desp_upload);
        upload = (Button) findViewById(R.id.upload_btn);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            File exStore = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                            String pictureName = getPictureName();
                            File imageFile = new File(exStore, pictureName);
                            Uri uri = Uri.fromFile(imageFile);

                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            uriString = uri.toString();

                            startActivityForResult(intent, REQUEST_CAMERA);

                        } else if (items[item].equals("Choose from Library")) {
                            Intent i = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, RESULT_LOAD_IMAGE);

                        } else if (items[item].

                                equals("Cancel")

                                )

                        {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });
    }

    private String getPictureName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());

        return "creo" + timestamp + ".jpg";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            final String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.upload_preview);

            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            imageView.setImageURI(selectedImage);

        } else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && null != data) {

            final Uri test = Uri.parse(uriString);
            image.setImageURI(test);

//            upload.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//
//    }
        }
    }

    private byte[] readInFile(String path) throws IOException {
        // TODO Auto-generated method stub
        byte[] data = null;
        File file = new File(path);
        InputStream input_stream = new BufferedInputStream(new FileInputStream(
                file));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        data = new byte[16384]; // 16K
        int bytes_read;
        while ((bytes_read = input_stream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytes_read);
        }
        input_stream.close();
        return buffer.toByteArray();

    }

    @Override
    public void onClick(View v) {

        Toast.makeText(UploadActivity.this, "Wait", Toast.LENGTH_LONG).show();

//        Log.d(TAG, "onClick() called with: " + "v = [" + v + "]");
        final ProgressDialog dlg = new ProgressDialog(UploadActivity.this);
        dlg.setTitle("Please wait.");
        dlg.setMessage("Uploading");
        dlg.show();


        upload.setEnabled(false);
        upload.setBackgroundColor(getResources().getColor(R.color.button_material_dark));

        // Locate the image in res >
        /////////////////////////////////////////////////////////////
        Bitmap bitmap = BitmapFactory.decodeFile("");
        // Convert it to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, stream);

        Object image = null;
        try {
            String path = null;
            image = readInFile(path);
        } catch (Exception e) {
            e.printStackTrace();
        }


        byte[] data = stream.toByteArray();
        // Create the ParseFile
        ParseFile file = new ParseFile("post_file" + ".jpg", data);
        // Upload the image into Parse Cloud
        file.saveInBackground();

        // Create a New Class called "ImageUpload" in Parse


        ParseObject parseObject = new ParseObject("Post");
        String username = ParseUser.getCurrentUser().getUsername();
        // Create a column named "ImageName" and set the string
        parseObject.put("PostImage", username + "avatar_name" + ".jpg");


        // Create a column named "ProfilePic" and insert the image
        parseObject.put("postImage", file);

        // Create the class and the columns
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    dlg.dismiss();
                    Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    dlg.dismiss();
                    Toast.makeText(UploadActivity.this, "Profile Picture updated!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(UploadActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP));


                }
            }
        });

    }
}



