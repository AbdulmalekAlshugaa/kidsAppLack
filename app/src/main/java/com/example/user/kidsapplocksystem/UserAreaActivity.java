package com.example.user.kidsapplocksystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class UserAreaActivity extends AppCompatActivity {

    Button bLogout;
    Button bEditProfile;
    Button bShownotification;
    Button ManageApps;
    TextView welcomeMessage;

    EditText etName, etType, etAge, etEmail, etPhoneNO, etUsername, etPassword;

    @Override
    protected void onResume() {
        super.onResume();
        try{ setWelcomName();}
        catch (Exception ex){}
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);
        setTitle("Home");
        // coping
        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etType = (EditText) findViewById(R.id.etType);
        final EditText etAge = (EditText) findViewById(R.id.etAge);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPhoneNO = (EditText) findViewById(R.id.etPhoneON);
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        // bLogout= Button find


// buttons

        final Button bEditPropfile = (Button) findViewById(R.id.bEditProfile);
        final Button bManageApps = (Button) findViewById(R.id.bManageApps);
        final Button bLogout = (Button) findViewById(R.id.bLogout);
        final Button bShownotification = (Button) findViewById(R.id.bShownotification);

        //set  the onclick lisner  for  the  for  the  edit profile  button
        bEditPropfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EditprofileIntent = new Intent(UserAreaActivity.this, Editprofile.class);
                UserAreaActivity.this.startActivity(EditprofileIntent);

            }
        });
        //set the clicklistener  for  the manage apps buttons
        bManageApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ManageappsIntent = new Intent(UserAreaActivity.this, Manageapps.class);
                UserAreaActivity.this.startActivity(ManageappsIntent);


            }
        });
        // set the click listener for  notification button
        bShownotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ShownotificationIntent = new Intent(UserAreaActivity.this, Shownotification.class);
                UserAreaActivity.this.startActivity(ShownotificationIntent);

            }
        });
        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("refs", MODE_PRIVATE).edit();
                editor.putBoolean("loggedIn", false);
                editor.commit();
                Intent UserIntent = new Intent(UserAreaActivity.this, LoginActivity.class);
                UserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(UserIntent);

            }
        });
        welcomeMessage = (TextView) findViewById(R.id.tvWelcomeMsg);
        setWelcomName();


    }

    private void setWelcomName() {
        SharedPreferences prefs = getSharedPreferences("refs", MODE_PRIVATE);
        String name = prefs.getString("name", null);
        welcomeMessage.setText("Welcome " + name);
    }

}
