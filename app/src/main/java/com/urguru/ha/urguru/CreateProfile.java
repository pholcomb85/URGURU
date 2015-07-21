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


public class CreateProfile extends ActionBarActivity {

    private int REQUEST_CAMERA;
    private int SELECT_FILE;
    ImageView imageView_picture = (ImageView) findViewById(R.id.imageView_picture);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        View textView_creatProfile = (View) findViewById(R.id.textView_createProfile);

        EditText editText_realName = (EditText) findViewById(R.id.editText_realName);
        final String realName = editText_realName.getText().toString();

        RadioGroup RadioGroupSex = (RadioGroup) findViewById(R.id.radioGroup_sex);
        RadioGroupSex.addView(findViewById(R.id.radioButton_male));
        RadioGroupSex.addView(findViewById(R.id.radioButton_female));
        final int sex = RadioGroupSex.getCheckedRadioButtonId();

        View textView_whenBorn = (View) findViewById(R.id.textView_whenBorn);

        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        final int birth_day = datePicker.getDayOfMonth();
        final int birth_month = datePicker.getMonth();
        final int birth_year = datePicker.getYear();

        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        final int birth_hour = timePicker.getCurrentHour();  //0-23
        final int birth_minute = timePicker.getCurrentMinute();


        View textView_Where = (View) findViewById(R.id.textView_whereLive);

        EditText editText_country = (EditText) findViewById(R.id.editText_country);
        final String country = editText_country.getText().toString();

        EditText editText_city = (EditText) findViewById(R.id.editText_city);
        final String city = editText_city.getText().toString();

        View textView_avatarName = (View) findViewById(R.id.textView_avatarName);

        EditText editText_avatarName = (EditText) findViewById(R.id.editText_avatarName);
        final String avatarName = editText_avatarName.getText().toString();


        Button button_avatarImage = (Button) findViewById(R.id.button_avatarImage);
        button_avatarImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectImage();

                //TODO let the user select an image from their files
                //TODO help the user crop the image to fit inside the avatar space
                //TODO save the new image
                //TODO show the user the new image
                //TODO allow the user to accept the avatar image or retry
            }
        });

        ImageView imageView_avatarImage = (ImageView) findViewById(R.id.imageView_avatarImage);


        String objectID = getIntent().getStringExtra("objectID");

        ParseQuery<ParseUser> query = ParseQuery.getQuery("user");
        query.getInBackground(objectID, new GetCallback<ParseUser>() {
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    user.put("realName", realName);
                    user.put("sex", sex);
                    user.put("birth_day", birth_day);
                    user.put("birth_month", birth_month);
                    user.put("birth_year", birth_year);
                    user.put("birth_hour", birth_hour);
                    user.put("birth_minute", birth_minute);
                    user.put("country", country);
                    user.put("city", city);
                    user.put("avatarName", avatarName);
                    //TODO save the avatar image to the parse user object

                    user.saveInBackground();
                } else {
                    // something went wrong
                }
            }
        });

        Button button_finish = (Button) findViewById(R.id.button_finish);
        button_finish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.activity_profile);
            }
        });
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
}


