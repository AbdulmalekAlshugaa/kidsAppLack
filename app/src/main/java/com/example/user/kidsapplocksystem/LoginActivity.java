package com.example.user.kidsapplocksystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
// the login activity
public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        setTitle("Login");





//        SharedPreferences prefs = getSharedPreferences("refs", MODE_PRIVATE);
//        Boolean loggedIn = prefs.getBoolean("loggedIn", false);
//        if (loggedIn) {
//            login();
//        }


        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);

        final Button bLogin = (Button) findViewById(R.id.bLogin);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                login(username,password);

//
//                // create the response lisner to  pass it  to  the signup request
//                Response.Listener<String> responseListner = new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        System.out.println("the response is " + response);
//                        // convert  to  jason object
//                        try {
//                            JSONObject jsonResponse = new JSONObject(response);
//                            String status = jsonResponse.getString("status");
//                            if (status.equals("success")) {
//                                JSONObject user = jsonResponse.getJSONObject("user");
//                                int userId = user.getInt("userID");
//                                System.out.println("the user id is " + userId);
//                                String name = user.getString("name");
//                                String type = user.getString("type");
//                                String age = user.getString("age");
//                                String phone_no = user.getString("phone_no");
//                                String email = user.getString("email");
//                                String username = user.getString("username");
//                                String password = user.getString("password");
//                                SharedPreferences.Editor editor = getSharedPreferences("refs", MODE_PRIVATE).edit();
//                                editor.putString("name", name);
//                                editor.putInt("userId", userId);
//                                editor.putString("type", type);
//                                editor.putString("age", age);
//                                editor.putString("phone_no", phone_no);
//                                editor.putString("email", email);
//                                editor.putString("username", username);
//                                editor.putString("password", password);
//                                editor.putBoolean("loggedIn", true);
//                                editor.commit();
//                                login();
//                                // create  new  intent  to  move  to the login page  if  the  registration correct
//                            } else {// to create alter dialog to  display error measg if  the sign up  wrong
//                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//                                builder.setMessage("LOGIN up  is Failed")
//                                        .setNegativeButton("Retry", null)
//                                        .create()
//                                        .show();
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                };
//                Response.ErrorListener errorListener = new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        System.out.println("there is response error " + error.getMessage().toString());
//                    }
//                };
//                HashMap<String, String> params = getParams(username, password);
//                // create  the request by the sign up request
//                String url = Utils.url + "Login.php";
//                networkRequest networkRequest = new networkRequest(params, responseListner, errorListener, url);
//                // create request queue and get the queue  from  volley
//                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
//                queue.add(networkRequest);
            }
        });




        TextView signupLink = (TextView) findViewById(R.id.tvSignupHere);
        signupLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent signupIntent = new Intent(LoginActivity.this, SignupActivity.class);
                LoginActivity.this.startActivity(signupIntent);

            }
        });
        final TextView homeLink = (TextView) findViewById(R.id.tvhomeHere);
        homeLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent homeIntent = new Intent(LoginActivity.this, UserAreaActivity.class);
                LoginActivity.this.startActivity(homeIntent);

            }
        });
    }

    void login(String emialAddress, String Password){
        firebaseAuth.signInWithEmailAndPassword(emialAddress, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "onComplete: "+"Login successfuly ");
                }
            }
        });
    }

    private HashMap<String, String> getParams(String username, String password) {
        HashMap<String, String> params = new HashMap<>();
        // put  the data to  the  hash  map

        params.put("username", username);
        params.put("password", password);
        return params;
    }

    private void login() {
        Intent UserIntent = new Intent(LoginActivity.this, UserAreaActivity.class);
        UserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(UserIntent);
    }
}
