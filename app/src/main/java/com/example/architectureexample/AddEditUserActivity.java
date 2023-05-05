package com.example.architectureexample;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.TypeConverter;

import com.bumptech.glide.Glide;
import com.example.architectureexample.databinding.ActivityAddEditUserBinding;
import com.example.architectureexample.databinding.ActivityMainBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AddEditUserActivity extends AppCompatActivity {

    ActivityAddEditUserBinding activityXml;
    public static final String EXTRA_ID =
            "com.treeleaf.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.treeleaf.EXTRA_TITLE";
    public static final String EXTRA_EMAIL =
            "com.treeleaf.EXTRA_EMAIL";
    public static final String EXTRA_GENDER =
            "com.treeleaf.EXTRA_GENDER";
    public static final String EXTRA_PHONE =
            "com.treeleaf.EXTRA_PHONE";
    public static final String EXTRA_COUNTRY =
            "com.treeleaf.EXTRA_COUNTRY";
    public static final String EXTRA_PHOTO =
            "com.treeleaf.EXTRA_PHOTO";
    public static final String EXTRA_CODE =
            "com.treeleaf.EXTRA_CODE";
    public static final String EXTRA_DESCRIPTION =
            "com.treeleaf.EXTRA_DESCRIPTION";

    ImageView profileImage;
    FloatingActionButton profileImageButton;
    private EditText editTextTitle;
    private EditText editTextEmail;
    private EditText editTextGender;
    private EditText editTextPhone;
    private EditText editTextCountry;
    private EditText editTextCountryCode;
    private EditText editTextPhoto;
    private EditText editTextDescription;
//    private byte[] photo;
//    private Object bitmap;

    File imageFile;
    String photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityXml = ActivityAddEditUserBinding.inflate(getLayoutInflater());
        setContentView(activityXml.getRoot());

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextGender = findViewById(R.id.edit_text_gender);
        editTextPhone = findViewById(R.id.edit_text_phone);
        editTextCountry = findViewById(R.id.edit_text_country);
        editTextCountryCode = findViewById(R.id.edit_text_country_code);
        editTextDescription = findViewById(R.id.edit_text_description);

        profileImage = findViewById(R.id.profile_image);
        profileImageButton = findViewById(R.id.floatingActionButton_user_profile);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit User");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            editTextEmail.setText(intent.getStringExtra(EXTRA_EMAIL));
            editTextGender.setText(intent.getStringExtra(EXTRA_GENDER));
            editTextPhone.setText(intent.getStringExtra(EXTRA_PHONE));
            editTextCountry.setText(intent.getStringExtra(EXTRA_COUNTRY));
            editTextCountryCode.setText(intent.getStringExtra(EXTRA_CODE));

            // get the file path of the image from your Room database
            String imagePath = intent.getStringExtra(EXTRA_PHOTO);

            if (imagePath!=null) {
// load the image from the file using Picasso
                Glide.with(this).load(new File(imagePath)).into(profileImage);
            }

        } else {
            setTitle("Add User");
        }

        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_" + new Random().nextInt(1000) + ".jpg";

// create a file to save the selected image to
                imageFile = new File(getCacheDir(), imageFileName);

                ImagePicker.Companion.with(AddEditUserActivity.this)
                        .compress(1024) // compress the selected image
                        .maxResultSize(1080, 1080) // set maximum image resolution
                        .saveDir(imageFile) // save the selected image to the file
                        .start(10); // start image selection process
            }
        });

    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String email = editTextEmail.getText().toString();
        String gender = editTextGender.getText().toString();
        String phone = editTextPhone.getText().toString();
        String country = editTextCountry.getText().toString();
        String countryCode = String.valueOf(editTextCountryCode.getText().toString());


        if (title.trim().isEmpty() || email.trim().isEmpty() || gender.trim().isEmpty() || phone.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a name,email,gender and phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (country.trim().isEmpty()) {
            country = "-";
        }
        if (String.valueOf(countryCode).trim().isEmpty()) {
            countryCode = "-";
        }
        if (description.trim().isEmpty()) {
            description = "-";
        }



        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_EMAIL, email);
        data.putExtra(EXTRA_GENDER, gender);
        data.putExtra(EXTRA_PHONE, phone);
        data.putExtra(EXTRA_PHOTO, photo);
        data.putExtra(EXTRA_COUNTRY, country);
        data.putExtra(EXTRA_CODE, countryCode);
        data.putExtra(EXTRA_DESCRIPTION, description);


        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            Uri uri = data.getData();
            profileImage.setImageURI(uri);

            imageFile = new File(uri.getPath());

            photo = imageFile.getAbsolutePath();

/*
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                profileImage.setImageBitmap((Bitmap) bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            photo = getStringFromBitmap((Bitmap) bitmap);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            // handle any errors that occur during the image selection process
            Toast.makeText(this, "Error selecting image: " , Toast.LENGTH_SHORT).show();
        } else {
            // handle other non-successful results
            Toast.makeText(this, "Image selection cancelled", Toast.LENGTH_SHORT).show();
        }
  */
        }

  /*@TypeConverter
    public static byte[] getStringFromBitmap(Bitmap bitmapPicture) {
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, 0,byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        return b;
  }

  @TypeConverter
    public static Bitmap getBitMapFromStr(byte[] arrr)
  {
      return BitmapFactory.decodeByteArray(arrr,0,arrr.length);
  }
*/
    }
}