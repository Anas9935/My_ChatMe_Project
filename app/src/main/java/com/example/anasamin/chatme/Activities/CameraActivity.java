package com.example.anasamin.chatme.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.camerakit.CameraKitView;
import com.example.anasamin.chatme.GeneralUtilities.cameraUtils;
import com.example.anasamin.chatme.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class CameraActivity extends AppCompatActivity {

//    Camera camera;
//    FrameLayout frameLayout;
//    cameraUtils camera_utils;
    CameraKitView cameraKitView;
    ImageView clickButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraKitView=findViewById(R.id.kit_view);
        clickButton=findViewById(R.id.camera_click);
//
//        frameLayout=findViewById(R.id.camera_frame);
//
//        camera=Camera.open();
//        camera_utils=new cameraUtils(this,camera);
//        frameLayout.addView(camera_utils);
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, byte[] bytes) {
                        File savedPhoto=new File(Environment.getExternalStorageDirectory()+"/"+new UUID(7,4).randomUUID().toString()+".jpeg");
                        try {
                            FileOutputStream fileOutputStream=new FileOutputStream(savedPhoto.getPath());
                            fileOutputStream.write(bytes);
                            fileOutputStream.close();
                            Toast.makeText(CameraActivity.this,"Photo saved",Toast.LENGTH_LONG).show();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
