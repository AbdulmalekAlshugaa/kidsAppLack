package com.example.user.kidsapplocksystem;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //new NukeSSLCerts().nuke();
        setContentView(R.layout.activity_signup);
        setTitle("Sign Up");
        final EditText etName=(EditText) findViewById(R.id.etName);
        final TextView etType=(TextView) findViewById(R.id.etType);
        final EditText etAge=(EditText) findViewById(R.id.etAge);

        final EditText etEmail=(EditText) findViewById(R.id.etEmail);
       final TextView textView = (TextView)findViewById(R.id.text);
        //valid email
// onClick of button perform this simplest code.
// until here
        final EditText etPhoneNO=(EditText) findViewById(R.id.etPhoneON);
        final EditText etUsername=(EditText) findViewById(R.id.etUsername);
        final EditText etPassword=(EditText) findViewById(R.id.etPassword);
        final Button bSignup=(Button) findViewById(R.id.bSignup);
        // so now  we need  to  get  the infromation  from the texts in  the signup from and
        // pass them  into signuprequest to execute the request
        // so we have to create listtenr
        bSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to get  the information  when  the user  clik sign  upe
                System.out.println("button clicked");
                final String name=etName.getText().toString();
                final String type=getType();
                final int age=Integer.parseInt(etAge.getText().toString());
                final String phone_no=etPhoneNO.getText().toString();
                final String email=etEmail.getText().toString();
                final String username=etUsername.getText().toString();
                final String password=etPassword.getText().toString();
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
                    Response.Listener<String> responseListner = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("the response is " + response);
                            // convert  to  jason object
                            // try {
                            /*JSONObject jsonResponse=new JSONObject(response);
                            boolean success=jsonResponse.getBoolean("success");*/

                            if (response.equals("1")) {
                                // create  new  intent  to  move  to the login page  if  the  registration correct
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                SignupActivity.this.startActivity(intent);
                            } else {// to create alter dialog to  display error measg if  the sign up  wrong
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                builder.setMessage("Singing  up  is Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();

                            }

                       /* } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        }

                    };
                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            System.out.println("there is response error " + error.getMessage().toString());
                        }
                    };
                    HashMap<String, String> params = getParams(name, type, age, phone_no, email, username, password);
                    // create  the request by the sign up request
                    String url = Utils.url + "adduser.php";
                    networkRequest networkRequest = new networkRequest(params, responseListner, errorListener, url);
                    // create request queue and get the queue  from  volley
                    RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
                    queue.add(networkRequest);
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setMessage("Please correct your data")
                            .setNegativeButton("OK", null)
                            .create()
                            .show();
                }


            }
        });




    }
    private HashMap<String,String> getParams(String name,String type,int age,String phone_no,String email,String username,String password)
    {
        HashMap<String,String>params= new HashMap<>();
        // put  the data to  the  hash  map
        params.put("name",name);
        params.put("type",type);
        params.put("age",age +"");
        params.put("phone_no",phone_no +"");
        params.put("email",email);
        params.put("username",username);
        params.put("password",password);
        return params;
    }
private String getType()
{
    RadioGroup rg = (RadioGroup)findViewById(R.id.radioGrp);
    String radiovalue = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();
    return radiovalue;
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
