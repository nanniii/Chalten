package com.example.will.chalten;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.media.session.IMediaControllerCallback;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CameraBridgeViewBase.CvCameraViewListener2 {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "MainActivity";
    BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS: {
                    mJavaCameraView.enableView();
                    break;
                }
                default: {
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };

    private Button mButtonPhoto;
    private Button mButtonDetect;
    private Button mButtonAssign;

    Mat mRgba, mImgGray, mImgCanny;

    int num;

    private JavaCameraView mJavaCameraView;

    private ImageView mImageView;

    static {
        if (OpenCVLoader.initDebug()){
            Log.i(TAG, "OpenCV loaded successfully! :)");
        }
        else {
            Log.i(TAG, "OpenCV did not load successfully! :'(");
        }

    }


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

        mJavaCameraView = (JavaCameraView) findViewById(R.id.superCamera);
        mJavaCameraView.setVisibility(SurfaceView.VISIBLE);
        mJavaCameraView.setCvCameraViewListener(this);

    }

    @Override
    protected void onPause(){
        super.onPause();
        if (mJavaCameraView!=null){
            mJavaCameraView.disableView();
        }
    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();
        if (mJavaCameraView!=null){
            mJavaCameraView.disableView();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (OpenCVLoader.initDebug()){
            Log.i(TAG, "OpenCV loaded successfully! :)");
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else {
            Log.i(TAG, "OpenCV did not load successfully! :'(");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallBack);
        }
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

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mImgGray= new Mat(height, width, CvType.CV_8UC1);
        mImgCanny = new Mat(height, width, CvType.CV_8UC1);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();

        Imgproc.cvtColor(mRgba, mImgGray, Imgproc.COLOR_RGB2GRAY);
        Imgproc.Canny(mImgGray, mImgCanny, 50, 150);

        return mImgGray;
    }
}
