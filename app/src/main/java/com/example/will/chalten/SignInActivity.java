package com.example.will.chalten;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SignInActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Button mSignIn;
    private Button mSignUp;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private Communicator communicator;
    private final static String TAG = "SignInActivity";
    public static Bus bus;
    private String mEmailString;
    private String mPasswordString;
    private TextView information, extraInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        communicator = new Communicator();

        mEmail = (EditText) findViewById(R.id.inputEmail);
        mPassword = (EditText) findViewById(R.id.inputPassword);
        mPassword.setTransformationMethod(new PasswordTransformationMethod());
        mSignIn = (Button) findViewById(R.id.toMainButton);
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmailString = mEmail.getText().toString();
                mPasswordString = mPassword.getText().toString();
                usePost(mEmailString, mPasswordString);
            }
        });
        mSignUp = (Button) findViewById(R.id.toSignUpButton);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmailString = mEmail.getText().toString();
                mPasswordString = mPassword.getText().toString();
                useGet(mEmailString, mPasswordString);
            }
        });

        information = (TextView)findViewById(R.id.information);
        extraInformation = (TextView)findViewById(R.id.extraInformation);
    }

    private void usePost(String email, String password){
        communicator.loginPost(email, password);
    }

    private void useGet(String email, String password){
        communicator.loginGet(email, password);
    }

    @Override
    public void onResume(){
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onServerEvent(ServerEvent serverEvent){
        Toast.makeText(this, ""+serverEvent.getServerResponse().getMessage(), Toast.LENGTH_SHORT).show();
        if(serverEvent.getServerResponse().getUsername() != null){
            information.setText("Username: "+serverEvent.getServerResponse().getUsername() + " || Password: "+serverEvent.getServerResponse().getPassword());
        }
        extraInformation.setText("" + serverEvent.getServerResponse().getMessage());
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent){
        Toast.makeText(this,""+errorEvent.getErrorMsg(),Toast.LENGTH_SHORT).show();
    }


    /*
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toMainButton:
                Toast toastMain = Toast.makeText(this, "Main", Toast.LENGTH_LONG);
                toastMain.show();
                //checkLogin();
                break;
            case R.id.toSignUpButton:
                Toast toastSignUp = Toast.makeText(this, "Sign Up", Toast.LENGTH_LONG);
                toastSignUp.show();
                break;
        }
    }
    */

    /*
    public void checkLogin() {

        // Get text from email and passord field
        final String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();

        // Initialize  AsyncLogin() class with email and password
        new AsyncLogin().execute(email,password);

    }

    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(SignInActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {


                url = new URL("https://chalten.000webhostapp.com/login.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {

                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");


                conn.setDoInput(true);
                conn.setDoOutput(true);


                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1]);
                String query = builder.build().getEncodedQuery();


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();


                if (response_code == HttpURLConnection.HTTP_OK) {


                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }


                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute (String result){



            pdLoading.dismiss();

            if (result.equalsIgnoreCase("true")) {


                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                SignInActivity.this.finish();

            } else if (result.equalsIgnoreCase("false")) {


                Toast.makeText(SignInActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(SignInActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            }
        }
    }
    */
}
