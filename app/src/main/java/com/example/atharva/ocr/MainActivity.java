package com.example.atharva.ocr;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

   // private static int TIMEOUT = 4000;
    public static final String EXTRA_MESSAGE = "com.example.atharva.tryocr";
    SurfaceView cameraView;
    TextView textView;
    CameraSource cameraSource;
    final int RequestCameraPermissionID=1001;

    private boolean checkConnection()
    {
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork = cm.getActiveNetworkInfo();
        return activenetwork != null && activenetwork.isConnectedOrConnecting();
    }

    public  int flag;

    {
        flag = 0;
    }

    public String output = "";


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //  super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case RequestCameraPermissionID:
            {
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                        return;
                }
                try {
                    cameraSource.start(cameraView.getHolder());
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        Button ClickButton = (Button)findViewById(R.id.ClickButton);

        ClickButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        flag=1;
                    }
                }
        );

        Button GoButton = (Button)findViewById(R.id.GoButton);

        GoButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        if(!checkConnection())
                        {
                            Toast.makeText(getApplicationContext(),"Please Check your Internet Connection...",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        switch_to_user_info(v);
                    }
                }
        );



        cameraView = (SurfaceView)findViewById(R.id.surface_view);
        textView=(TextView)findViewById(R.id.text_view);

        TextRecognizer textRecognizer= new TextRecognizer.Builder(getApplicationContext()).build();

        if(!textRecognizer.isOperational())
        {
            Log.w("MainActivity","Detector dependencies are not yet available");
        }
        else{
            cameraSource = new CameraSource.Builder(getApplicationContext(),textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)//to select the back cam of device
                    .setRequestedPreviewSize(1600,1200) // width,ht of camera pixel frames
                    .setRequestedFps(2.0f) //sets the requested frame rate in fps
                    .setAutoFocusEnabled(true)// to be set to false

                    .build();

            // cameraSource.takePicture();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                //Access to the underlying surface is provided via the SurfaceHolder interface, which can be retrieved by calling getHolder().
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    try{

                        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA},
                                    RequestCameraPermissionID);

                            return;
                        }
                        cameraSource.start(cameraView.getHolder());

                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

                    cameraSource.stop();
                    // to test this!!!
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                //to be written in onpress btn
                public void receiveDetections(Detector.Detections<TextBlock> detections) {

                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if(items.size()!=0)
                    {   if(flag==0)
                    {
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < items.size(); ++i) {
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                                output = stringBuilder.toString();

                                textView.setText(output);
                            }
                        });
                    }
                    }

                }
            });

        }
    }


    public void switch_to_user_info(View v){
        Intent intent = new Intent(this, User_Info.class);
        String message = output;
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        recreate();
    }
}
