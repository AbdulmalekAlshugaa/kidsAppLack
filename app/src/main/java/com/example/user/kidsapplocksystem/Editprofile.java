package com.example.user.kidsapplocksystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Editprofile extends AppCompatActivity {
    Button bUpdate;
    Button bBack;
    SharedPreferences perfrencs;
    int userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        setTitle("Edit Profile");
        perfrencs=getSharedPreferences("refs", MODE_PRIVATE);
        userID=perfrencs.getInt("userId",0);

        String name=perfrencs.getString("name","");
        String type=perfrencs.getString("type","");
        String age=perfrencs.getString("age","");
        String phone_no=perfrencs.getString("phone_no","");
        String email=perfrencs.getString("email","");
        String username=perfrencs.getString("username","");
        String password=perfrencs.getString("password","");
        System.out.println("the id is "+userID);
        final EditText etName=(EditText) findViewById(R.id.etName);
        etName.setText(name);
        final EditText etType=(EditText) findViewById(R.id.etType);
        etType.setText(type);
        final EditText etAge=(EditText) findViewById(R.id.etAge);
        etAge.setText(age);
        final EditText etEmail=(EditText) findViewById(R.id.etEmail);
        etEmail.setText(email);
        final EditText etPhoneNO=(EditText) findViewById(R.id.etPhoneON);
        etPhoneNO.setText(phone_no);
        final EditText etUsername=(EditText) findViewById(R.id.etUsername);
        etUsername.setText(username);
        final EditText etPassword=(EditText) findViewById(R.id.etPassword);
        etPassword.setText(password);
        final Button bUpdate=(Button) findViewById(R.id.bUpdate);

        bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("button clicked");

                    final String name = etName.getText().toString();
                    final String type = etType.getText().toString();
                    final int age = Integer.parseInt(etAge.getText().toString());
                    final String phone_no = etPhoneNO.getText().toString();
                    final String email = etEmail.getText().toString();
                    final String username = etUsername.getText().toString();
                    final String password = etPassword.getText().toString();
                Boolean validData=true;

                // create the response lisner to  pass it  to  the signup request
                // System.out.println("is valid email is "+
                boolean validEmail=isValidEmail(email);
                if(!validEmail)
                {
                    validData=false;
                    etEmail.setError("Invalid Email");
                }

                if(!checkAgeSize(age)) {
                    validData=false;
                    etAge.setError("Age should be more than 18");
                }
                if(validData) {
                    Response.Listener<String>responseListner=new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonResponse= null;
                            try {

                                jsonResponse = new JSONObject(response);
                                String status=jsonResponse.getString("status");
                                JSONObject user=jsonResponse.getJSONObject("user");
                                int userId=user.getInt("userID");
                                System.out.println("the user id is "+userId);
                                String name=user.getString("name");
                                String type=user.getString("type");
                                String age=user.getString("age");
                                String phone_no=user.getString("phone_no");
                                String email=user.getString("email");
                                String username=user.getString("username");
                                String password=user.getString("password");
                                SharedPreferences.Editor editor = getSharedPreferences("refs", MODE_PRIVATE).edit();
                                editor.putString("name", name);
                                editor.putInt("userId", userId);
                                editor.putString("type",type);
                                editor.putString("age",age);
                                editor.putString("phone_no",phone_no);
                                editor.putString("email",email);
                                editor.putString("username",username);
                                editor.putString("password",password);
                                editor.commit();
                                if (status.equals("success")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Editprofile.this);
                                    builder.setMessage("Update  is Success")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            })
                                            .create()
                                            .show();



                                } else {// to create alter dialog to  display error measg if  the sign up  wrong
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Editprofile.this);
                                    builder.setMessage("Update up  is Failed")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        }
                    };
                    Response.ErrorListener errorListener= new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            System.out.println("there is response error "+error.toString());
                        }
                    };
                    HashMap<String,String> params=getParams(userID,name,type,age,phone_no,email,username,password);
                    // create  the request by the sign up request
                    String url=Utils.url+"Edit.php";
                    networkRequest networkRequest = new networkRequest(params,responseListner,errorListener,url);
                    // create request queue and get the queue  from  volley
                    RequestQueue queue= Volley.newRequestQueue(Editprofile.this);
                    queue.add(networkRequest);
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Editprofile.this);
                    builder.setMessage("correct your data")
                            .setNegativeButton("OK", null)
                            .create()
                            .show();
                }








            }
        });


    }
    private HashMap<String,String> getParams(int id,String name,String type,int age,String phone_no,String email,String username,String password)
    {
        HashMap<String,String>params= new HashMap<>();

        // put  the data to  the  hash  map
        params.put("id",String.valueOf(id));
        params.put("name",name);
        params.put("type",type);
        params.put("age",age +"");
        params.put("phone_no",phone_no +"");
        params.put("email",email);
        params.put("username",username);
        params.put("password",password);
        return params;
    }
    private Boolean checkAgeSize(int age) {
        if (age >18) {
            return true;
        }   else
        {
            return false;
        }

    }
    public final static boolean isValidEmail(CharSequence target) {
        System.out.println("the email is "+target);
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
