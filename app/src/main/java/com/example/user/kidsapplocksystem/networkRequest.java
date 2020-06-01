package com.example.user.kidsapplocksystem;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 11/18/2016.
 */
//to  allow to  make request to php file in the  server and  get  response as  string
public class networkRequest extends StringRequest {
DefaultRetryPolicy policy=new DefaultRetryPolicy(
        10000,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    // static  is  not  going  change

    private Map<String ,String> params;
    //create the constructor  for  the  class and  it  is  the  first  method execued  when  the class created and  it asks  for  the atruibits liks name
 public networkRequest(HashMap<String,String>passedParams, Response.Listener<String>listener, Response.ErrorListener errorListener,String link){
      super(Method.POST,link,listener,errorListener);
     //super post used to  send  some data to  register php file and  it  get  response
     // we  need  a way to make  a volley librarary  to  pass the infromation to  the  requuest  so  the way  is  create prams
    this.params=passedParams;
 }
// getparams created  to  let  the volley to access the data later
    @Override
    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        return policy;
    }
}

// the sumrization  of  this class  is  when  the signuprequest  is executed ,the  volley will call getparams
// and  getparams will  rturn params  which we  have  defined  the field  that needed












