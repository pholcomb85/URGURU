package com.urguru.ha.urguru;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class CreateProfile extends SignIn {
    private View textView_createProfile;
    private EditText editText_realName;
    private String realName;
    private RadioGroup radioGroup_sex;
    private int sex;
    private View textView_whenBorn;
    private DatePicker datePicker;
    private int birth_day;
    private int birth_month;
    private int birth_year;
    private TimePicker timePicker;
    private int birth_hour;
    private int birth_minute;
    private View textView_where;
    private EditText editText_country;
    private String country;
    private EditText editText_city;
    private String city;
    private View textView_avatarName;
    private EditText editText_avatarName;
    private String avatarName;
    private Button button_avatarImage;
    private ImageView imageView_avatarImage;
    private ImageView imageView_picture;
    private Button button_finish;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        textView_createProfile = (View) findViewById(R.id.textView_createProfile);
        editText_realName = (EditText) findViewById(R.id.editText_realName);
        radioGroup_sex = (RadioGroup) findViewById(R.id.radioGroup_sex);
        radioGroup_sex.addView(findViewById(R.id.radioButton_male));
        radioGroup_sex.addView(findViewById(R.id.radioButton_female));
        textView_whenBorn = (View) findViewById(R.id.textView_whenBorn);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        textView_where = (View) findViewById(R.id.textView_where);
        editText_country = (EditText) findViewById(R.id.editText_country);
        editText_city = (EditText) findViewById(R.id.editText_city);
        textView_avatarName = (View) findViewById(R.id.textView_avatarName);
        editText_avatarName = (EditText) findViewById(R.id.editText_avatarName);
        button_avatarImage = (Button) findViewById(R.id.button_avatarImage);
        imageView_avatarImage = (ImageView) findViewById(R.id.imageView_avatarImage);
        imageView_picture = (ImageView) findViewById(R.id.imageView_picture);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView_picture.setImageBitmap(thumbnail);
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = { MediaStore.MediaColumns.DATA };
                Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                        null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                imageView_picture.setImageBitmap(bm);
            }
        }
    }

    public void onButtonClickGetPicture(View view) {
        selectImage();
    }

    public void onButtonClickFinishProfile(View view) {
        String objectID = getIntent().getStringExtra("objectID");

        ParseQuery<ParseUser> query = ParseQuery.getQuery("user");
        query.getInBackground(objectID, new GetCallback<ParseUser>() {
            public void done(ParseUser user, ParseException e) {
                if (e == null) {

                    email = getIntent().getStringExtra("email");
                    password = getIntent().getStringExtra("password");
                    realName = editText_realName.getText().toString();
                    user.put("realName", realName);
                    sex = radioGroup_sex.getCheckedRadioButtonId();
                    user.put("sex", sex);
                    birth_day = datePicker.getDayOfMonth();
                    user.put("birth_day", birth_day);
                    birth_month = datePicker.getMonth();
                    user.put("birth_month", birth_month);
                    birth_year = datePicker.getYear();
                    user.put("birth_year", birth_year);
                    birth_hour = timePicker.getCurrentHour();  //0-23
                    user.put("birth_hour", birth_hour);
                    birth_minute = timePicker.getCurrentMinute();
                    user.put("birth_minute", birth_minute);
                    country = editText_country.getText().toString();
                    user.put("country", country);
                    city = editText_city.getText().toString();
                    user.put("city", city);
                    avatarName = editText_avatarName.getText().toString();
                    user.put("avatarName", avatarName);
                    //TODO save the avatar image to the parse user object

                    user.saveInBackground();
                } else {
                    // something went wrong
                }
            }
        });

        setContentView(R.layout.activity_profile);

    }





}


