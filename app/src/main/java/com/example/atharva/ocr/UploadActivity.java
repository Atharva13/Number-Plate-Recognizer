package com.example.atharva.ocr;




import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.content.*;
import android.widget.Toast;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class UploadActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.atharva.tryocr";

    ImageView imageView;
    Button btnProcess, btnUpload;
    TextView txtResult;
    private static final int  PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    private int STORAGE_PERMISSION_CODE=1;
    String output;

    private boolean checkConnection()
    {
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork = cm.getActiveNetworkInfo();
        return activenetwork != null && activenetwork.isConnectedOrConnecting();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        imageView = (ImageView)findViewById(R.id.image_view);
        btnProcess=(Button)findViewById(R.id.button_process);
        btnUpload =(Button)findViewById(R.id.button_upload);
        txtResult=(TextView)findViewById(R.id.textview_result);



        if(ContextCompat.checkSelfPermission(UploadActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            //Toast.makeText(MainActivity.this,"ALREADY GRANTED PERMISSION",Toast.LENGTH_SHORT).show();
            uploadPic();

        }else{
            requestStoragePermission();
        }



        //     Bitmap bitmap = BitmapFactory.decodeResource(
        //              getApplicationContext().getResources(),
//                R.drawable.helloworld
        //       );
//        imageView.setImageBitmap(bitmap);



        //  recognise the text
        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextRecognizer textRecognizer= new TextRecognizer.Builder(getApplicationContext()).build();


                if(!textRecognizer.isOperational())
                {
                    Log.w("MainActivity","Detector dependencies are not yet available");
                }
                else{
                    Frame frame= new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = textRecognizer.detect(frame);
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i=0;i<items.size();++i){
                        TextBlock item = items.valueAt(i);
                        stringBuilder.append(item.getValue());
                        stringBuilder.append("\n");
                    }
                    output = stringBuilder.toString();
                    txtResult.setText(stringBuilder.toString());
                }


            }
        });

        // upload from gallery

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkConnection())
                {
                    Toast.makeText(getApplicationContext(),"Please Check your Internet Connection...",Toast.LENGTH_SHORT).show();
                    return;
                }
                switch_to_user_info(view);

            }
        });



    }

    private  void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("PERMISSION NEEDED")
                    .setMessage("THIS PERMISSION IS NEEDED")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            ActivityCompat.requestPermissions(UploadActivity.this,new  String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }
        else {
            ActivityCompat.requestPermissions(this,new  String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            InputStream inputStream;

            try {
                inputStream =getContentResolver().openInputStream(uri);

                bitmap = BitmapFactory.decodeStream(inputStream);
                // Log.d(TAG, String.valueOf(bitmap));
                ImageView imageView = (ImageView) findViewById(R.id.image_view);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this,"Unable to open image",Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"PERMISSION GRANTED",Toast.LENGTH_SHORT).show();

                uploadPic();



            } else {
                //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                Toast.makeText(this,"PERMISSION DENIED",Toast.LENGTH_SHORT).show();
            }

        }


    }

    public void uploadPic(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        Uri data = Uri.parse(pictureDirectoryPath);
//// Show only images, no videos or anything else
        intent.setDataAndType(data,"image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }


    public void switch_to_user_info(View v){
        Intent intent = new Intent(this, User_Info.class);
        String message = output;
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        //recreate();
    }



}
