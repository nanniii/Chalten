package com.example.will.chalten;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.media.session.IMediaControllerCallback;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private Button mButtonPhoto;
    private Button mButtonDetect;
    private Button mButtonAssign;

    int num;

    private ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonPhoto = (Button) findViewById(R.id.photo);
        mButtonPhoto.setOnClickListener(this);

        mButtonDetect = (Button) findViewById(R.id.detect);
        mButtonDetect.setOnClickListener(this);

        mButtonAssign = (Button) findViewById(R.id.assign);
        mButtonAssign.setOnClickListener(this);

        mImageView = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.photo:
                dispatchTakePictureIntent();
                break;
            case R.id.detect:
                break;
            case R.id.assign:
                break;
        }
    }
}
