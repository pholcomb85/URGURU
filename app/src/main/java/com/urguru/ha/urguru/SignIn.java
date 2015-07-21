package com.urguru.ha.urguru;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;



public class SignIn extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "Jn6YoSmyPjSTSML4dQ6jxtkgeBSLludHCNYvqofO", "TOisNXKgLHai957Ji5hFNtrp1j0kTC4ljRWxfJO9");

        //Test Parse
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();

        ImageView imageView_urguruTitle = (ImageView) findViewById(R.id.imageView_urguruTitle);


        EditText editText_email = (EditText) findViewById(R.id.editText_email);
        final String email = editText_email.getText().toString();


        EditText editText_password = (EditText) findViewById(R.id.editText_password);
        final String password = editText_password.getText().toString();

        Button button_signIn = (Button) findViewById(R.id.button_signIn);
        button_signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO check if both fields are full
                //TODO check if it is an email
                //TODO check if email exists in parse database
                //TODO check if the password is correct

                //then we can go to Profile
                setContentView(R.layout.activity_profile);
            }
        });

        //creates a new user with Parse
        ParseUser user = new ParseUser();
        user.setEmail(email);
        user.setUsername(email);
        user.setPassword(password);

        //save the Parse objectID as an intent to use with each CreateProfile
        String objectID = user.getObjectId();
        Intent intent = new Intent(SignIn.this, CreateProfile.class);
        intent.putExtra("objectID", objectID);

        user.signUpInBackground();

        Button button_newAccount = (Button) findViewById(R.id.button_newAccount);
        button_newAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.activity_create_profile);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
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
