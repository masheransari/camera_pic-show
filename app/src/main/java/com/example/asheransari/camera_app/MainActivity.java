package com.example.asheransari.camera_app;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button take_pic, show, save;
    int CAMERA_REQUEST = 1000;
    ImageView imageView;
    Bitmap pic = null;
    int saveCount = 0;
    ImageButton btnCancel;
    ImageView dialogImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image_from_camera);
        take_pic = (Button) findViewById(R.id.take_image_from_camera);
        show = (Button) findViewById(R.id.show_btn);
        save = (Button) findViewById(R.id.save_btn);
        take_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pic == null) {
                    Toast.makeText(MainActivity.this, "No Pic Found!!", Toast.LENGTH_SHORT).show();
                } else {
                    if (saveCount == 0) {
                        SaveImage(pic);
                        ++saveCount;
                    } else {
                        Toast.makeText(MainActivity.this, "Already saved..!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pic != null) {
                    showCustomDialog(pic);
                    customDialog.show();
                } else {
                    Toast.makeText(MainActivity.this, "Pic not Found..!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //    private void SaveImage(Bitmap finalBitmap) {
//
//        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
//        File myDir = new File(root + "/saved_images");
//        if (!myDir.exists()){
//            if (myDir.mkdir()){
//
//            }
//            else{
//
//            }
//        }
//        myDir.mkdirs();
//
//        String fname = "Image-" + getRandomNumber() +".jpg";
//        File file = new File(myDir, fname);
//        if (file.exists()) file.delete();
//        try {
//            FileOutputStream out = new FileOutputStream(file);
//            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//            out.flush();
//            out.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public void SaveImage(Bitmap finalBitmap) {

        if (requestPermission(this)) {
            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
            Log.e("root", root);
            File myDir = new File(Environment.getExternalStorageDirectory() + "/Image_Task");

            if (!myDir.exists()) {
                myDir.mkdir();
            }
            Random generator = new Random();
            Log.e("FIle", myDir.getAbsolutePath());
            int n = 10000000;
            n = generator.nextInt(n);
            String fname = "Image-" + n + ".jpg";
            File file = new File(myDir, fname);
            Toast.makeText(this, "Image Save at: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            file.setExecutable(true);
            file.setReadable(true);
            file.setWritable(true);
            try {
                FileOutputStream out = new FileOutputStream(file);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
//            Toast.makeText(this, "Permission Not Granted..!!", Toast.LENGTH_SHORT).show();
        }
    }

    //    private int getRandomNumber() {
//        return (int) ((Math.random() * 9000000) + 1000000);
//    }
    private boolean requestPermission(Activity context) {
        boolean hasPermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
            return false;
        } else {
            // hamen allow ho gya hia.. ok ki report
            return true;
        }
    }

    private Dialog customDialog;

    private void showCustomDialog(Bitmap bitmap) {
        customDialog = new Dialog(this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.image_dialog);
        btnCancel = (ImageButton) customDialog.findViewById(R.id.imageCancelBtn);
        btnCancel.setOnClickListener(this);
        dialogImageView = (ImageView) customDialog.findViewById(R.id.dialog_image);
//        Glide.with(this).load(bitmap).into(dialogImageView);
        dialogImageView.setImageBitmap(bitmap);
//        MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, "" + imageId
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            pic = null;
            pic = (Bitmap) data.getExtras().get("data");
            if (pic != null) {
                saveCount = 0;
            }
//            imageView.setImageBitmap(pic);
        }
    }

    public static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "The app was allowed to write to your storage!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.imageCancelBtn:
                customDialog.dismiss();
                break;
        }
    }
}