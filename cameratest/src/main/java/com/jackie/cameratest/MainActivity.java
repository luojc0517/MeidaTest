package com.jackie.cameratest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static final int TAKE_PHOTO = 1;
    public static final int SHOW_PHOTO = 2;
    private Button btnTP;
    private Button btnChoose;
    private ImageView ivPhoto;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTP = (Button) findViewById(R.id.btnTP);
        btnChoose = (Button) findViewById(R.id.btnChoose);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        btnTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建图片的暂时存储文件
                File photo = new File(Environment.getExternalStorageDirectory(), "tempImage.jpg");
                if (photo.exists()) {
                    //如果已经有图片文件则删除
                    photo.delete();
                }
                try {
                    //记得要有这句文件才算创建成功
                    photo.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //调用系统摄像头拍照
                imageUri = Uri.fromFile(photo);//将File对象转换为Uri
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");//调用系统拍照功能
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//指定拍摄后图片的输出地址
                startActivityForResult(intent, TAKE_PHOTO);//requestCode为TAKE_PHOTO
            }
        });
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File photo = new File(Environment.getExternalStorageDirectory(), "tempImage.jpg");
                if (photo.exists()) {
                    photo.delete();
                }
                try {
                    photo.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*
                   bug0001：
                   before：Uri imageUri = Uri.fromFile(photo);
                   这里应该用全局变量imageUri
                 */
                imageUri = Uri.fromFile(photo);
                //调用系统任务
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                /*
                   bug0002：
                   before：intent.putExtra("crop", true);
                   这里应该用字符串的true
                   并且选择的图片要小，300、400kb的样子吧
                 */
                intent.putExtra("crop", "true");
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                //这里复用了代码，选择完图片直接进入显示图片的逻辑
                startActivityForResult(intent, SHOW_PHOTO);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");//调用裁剪图片功能
                    intent.setDataAndType(imageUri, "image/*");//指定裁剪所需的图片来源
                    intent.putExtra("scale", true);//裁剪的时候保留原比例
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//指定裁剪后图片的输出地址
                    startActivityForResult(intent, SHOW_PHOTO);
                }
                break;
            case SHOW_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //将jpg图片解析成Bitmap
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        //给ImageView设置Bitmap
                        ivPhoto.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }
}
