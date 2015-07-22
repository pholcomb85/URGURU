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

    private ImageView imageView_urguruTitle;
    private EditText editText_email;
    private String email;
    private EditText editText_password;
    private String password;
    private Button button_signIn;
    private Button button_newAccount;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        imageView_urguruTitle = (ImageView) findViewById(R.id.imageView_urguruTitle);
        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_password = (EditText) findViewById(R.id.editText_password);
        button_signIn = (Button) findViewById(R.id.button_signIn);
        button_newAccount = (Button) findViewById(R.id.button_newAccount);
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


    public void onButtonClickTestParse(View view) {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "Jn6YoSmyPjSTSML4dQ6jxtkgeBSLludHCNYvqofO", "TOisNXKgLHai957Ji5hFNtrp1j0kTC4ljRWxfJO9");
        //Test Parse
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
    }

    public void onButtonClickSignIn(View view) {
        //todo check email and password with parse database
        //if it doesn't exist send error
        //if it does, sign in
        setContentView(R.layout.activity_profile);
    }

    public void onButtonClickNewAccount(View view) {
        //todo check email and password with parse database
        //if it already exists send error
        //if not create

        Intent intent = new Intent(this, CreateProfile.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);


        setContentView(R.layout.activity_create_profile);
    }






}
