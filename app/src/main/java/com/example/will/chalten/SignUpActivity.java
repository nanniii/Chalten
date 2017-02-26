package com.example.will.chalten;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mPassword;
    private Button mSignUp;
    private String mFirstNameString;
    private String mLastNameString;
    private String mEmailString;
    private String mPasswordString;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirstName = (EditText) findViewById(R.id.firstnameTextbox);
        mLastName = (EditText) findViewById(R.id.lastnameTextbox);
        mEmail = (EditText) findViewById(R.id.inputEmail);
        mPassword = (EditText) findViewById(R.id.inputPassword);
        mSignUp = (Button) findViewById(R.id.SignUpButton);
        mSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.SignUpButton:
                mFirstNameString = mFirstName.getText().toString();
                mLastNameString = mLastName.getText().toString();
                mEmailString = mEmail.getText().toString();
                mPasswordString = mPassword.getText().toString();

                break;

        }
    }

}
