package app.grandeyardera.com.grandeyardera.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import app.grandeyardera.com.grandeyardera.R;

/**
 * Created by 13118467271 on 2017/8/24.
 */

public class UserInfoActivity extends Activity implements View.OnClickListener {
    final Context context = this;
    private ImageView headPortrait;
    private Button changeHeadPortrait;
    private Button userBackMain;

    private TextView userInfoName;
    private TextView userInfoSchool;
    private TextView userInfoEamil;
    private TextView userInfoNumber;

    private Uri imageUri;
    public static final int TAKE_PHOTO = 1;
    public  static final int CROP_PHOTO = 2;
    private AlertDialog dialog = null;
    File outputImage = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        headPortrait = (ImageView) findViewById(R.id.head_portrait);
        changeHeadPortrait = (Button) findViewById(R.id.change_head_portrait);
        userBackMain = (Button) findViewById(R.id.user_info_back);
        //compileUser = (Button) findViewById(R.id.compile);
        userInfoName = (TextView) findViewById(R.id.user_info_name);
        userInfoEamil = (TextView) findViewById(R.id.user_info_email);
        userInfoSchool = (TextView) findViewById(R.id.user_info_school);
        userInfoNumber = (TextView)findViewById(R.id.user_info_number);
        Bitmap bt = BitmapFactory.decodeFile("head.jpg");// 从SD卡中找头像，转换成Bitmap
        if (bt != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
            headPortrait.setImageDrawable(drawable);
        } else {
            /**
             * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
             *
             */
        }
        changeHeadPortrait.setOnClickListener(this);
        userBackMain.setOnClickListener(this);
       // compileUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_head_portrait:
                popupChose();

                break;
            case R.id.user_info_back:
                Intent intentUserToMain = new Intent(UserInfoActivity.this,MainActivity.class);
                startActivity(intentUserToMain);
                finish();
                break;
            case R.id.take_photo:
                dialog.dismiss();
                 outputImage = new File(Environment.getExternalStorageDirectory(),"head.jpg");
                if (outputImage.exists()){
                    outputImage.delete();
                }
                try {
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
                break;
            case R.id.chose_photo:
                dialog.dismiss();
                outputImage = new File(Environment. getExternalStorageDirectory(), "head.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(outputImage);
                Intent intentChose = new Intent("android.intent.action. GET_CONTENT");
                intentChose.setType("image/*");
                intentChose.putExtra("crop", true);
                intentChose.putExtra("scale", true);
                intentChose.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intentChose, CROP_PHOTO);
                break;
               default:
                 break;
        }
    }
    public void popupChose(){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        builder.setView(inflater.inflate(R.layout.dialog_chose_photo,null));
        TextView takePhoto = (TextView) findViewById(R.id.take_photo);
        TextView chosePhoto = (TextView) findViewById(R.id.chose_photo);
        takePhoto.setOnClickListener(this);
        chosePhoto.setOnClickListener(this);
        dialog = builder.create();
        builder.show();
    }
    /**
    private void showTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_select_photo, null);
        TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                dialog.dismiss();
            }
        });
        tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
                startActivityForResult(intent2, 2);// 采用ForResult打开
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }
    */
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK){
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri,"image/*");
                    intent.putExtra("scale",true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(intent,CROP_PHOTO);
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK){
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        headPortrait.setImageBitmap(bitmap);
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
