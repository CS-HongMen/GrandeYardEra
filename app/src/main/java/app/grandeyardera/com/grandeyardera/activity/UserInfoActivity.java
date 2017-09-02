package app.grandeyardera.com.grandeyardera.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import app.grandeyardera.com.grandeyardera.BuildConfig;
import app.grandeyardera.com.grandeyardera.R;
import app.grandeyardera.com.grandeyardera.model.GradenYardEraDB;
import app.grandeyardera.com.grandeyardera.model.User;
import app.grandeyardera.com.grandeyardera.util.FileStorage;
import app.grandeyardera.com.grandeyardera.util.PermissionsActivity;
import app.grandeyardera.com.grandeyardera.util.PermissionsChecker;

import static android.content.ContentValues.TAG;

/**
 * Created by 13118467271 on 2017/8/24.
 */

public class UserInfoActivity extends Activity implements View.OnClickListener {
    //final Context context = this;
    private static final int REQUEST_PICK_IMAGE = 1; //相册选取
    private static final int REQUEST_CAPTURE = 2;  //拍照
    private static final int REQUEST_PICTURE_CUT = 3;  //剪裁图片
    private static final int REQUEST_PERMISSION = 4;  //权限请求
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private Uri imageUri;//原图保存地址
    private boolean isClickCamera;
    private String imagePath;

    private ImageView headPortrait;
    private Button changeHeadPortrait;
    private Button userBackMain;
    private Button logOut;

    private TextView userInfoName;
    private TextView userInfoSchool;
    private TextView userInfoEmail;
    private TextView userInfoNumber;

    private String userInfoNameText;
    private String userInfoSchoolText;
    private String userInfoEmailText;
    private String userInfoNumberText;

   // private Uri imageUri;
    public static final int TAKE_PHOTO = 1;
    public  static final int CROP_PHOTO = 2;
    private AlertDialog dialog = null;
    private GradenYardEraDB gradenYardEraDB;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo);
        headPortrait = (ImageView) findViewById(R.id.head_portrait);
        changeHeadPortrait = (Button) findViewById(R.id.change_head_portrait);
        userBackMain = (Button) findViewById(R.id.user_info_back);
        logOut = (Button)findViewById(R.id.log_out);
        //compileUser = (Button) findViewById(R.id.compile);
        userInfoName = (TextView) findViewById(R.id.user_info_name);
        userInfoEmail = (TextView) findViewById(R.id.user_info_email);
        userInfoSchool = (TextView) findViewById(R.id.user_info_school);
        userInfoNumber = (TextView)findViewById(R.id.user_info_number);
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        if (pref != null) {
            userInfoEmailText = pref.getString("email","");
            userInfoNameText = pref.getString("name","");
            userInfoNumberText = pref.getString("number","");
            userInfoSchoolText = pref.getString("school","");
            userInfoEmail.setText(userInfoEmailText);
            userInfoName.setText(userInfoNameText);
            userInfoSchool.setText(userInfoSchoolText);
            userInfoNumber.setText(userInfoNumberText);
        }
        if (getBitmapFromSharedPreferences() != null){
            headPortrait .setImageBitmap(getBitmapFromSharedPreferences());
        }



/**
        gradenYardEraDB = new GradenYardEraDB(this);
        String[] userMessage = new String[5];
        userMessage = gradenYardEraDB.loadAllUser();
        userInfoName.setText(userMessage[0]);
        userInfoSchool.setText(userMessage[4]);
        userInfoEamil.setText(userMessage[1]);
        userInfoNumber.setText(userMessage[3]);
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


        changeHeadPortrait.setOnClickListener(this);
        userBackMain.setOnClickListener(this);
        logOut.setOnClickListener(this);
        mPermissionsChecker = new PermissionsChecker(this);
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
            case R.id.log_out:
               // gradenYardEraDB.clearPassword("");
                SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("password","");
                editor.commit();
                Intent userIntentLogin = new Intent(UserInfoActivity.this,LoginActivity.class);
                startActivity(userIntentLogin);
                finish();
                break;
            default:
                 break;
        }
    }

    public void popupChose(){
        final String[] items = new String[]{"从本地选择","拍照"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择照片").setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:

                        //检查权限(6.0以上做权限判断)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                                startPermissionsActivity();
                            } else {
                                openCamera();
                            }
                        } else {
                            openCamera();
                        }
                        isClickCamera = true;

                        break;
                    case 1:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                                startPermissionsActivity();
                            } else {
                                selectFromAlbum();
                            }
                        } else {
                            selectFromAlbum();
                        }
                        isClickCamera = false;

                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }
    private void openCamera() {
        File file = new FileStorage().createIconFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(UserInfoActivity.this, "app.grandeyardera.com.grandeyardera.fileprovider", file);//通过FileProvider创建一个content类型的Uri
        } else {
            imageUri = Uri.fromFile(file);
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, REQUEST_CAPTURE);
    }
    private void selectFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }
    private void cropPhoto() {
        File file = new FileStorage().createCropFile();
        Uri outputUri = Uri.fromFile(file);//缩略图保存地址
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_PICTURE_CUT);
    }
    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_PERMISSION,
                PERMISSIONS);
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        imagePath = null;
        imageUri = data.getData();
        if (DocumentsContract.isDocumentUri(this, imageUri)) {
            //如果是document类型的uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(imageUri);
            if ("com.android.providers.media.documents".equals(imageUri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.downloads.documents".equals(imageUri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(imageUri, null);
        } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            imagePath = imageUri.getPath();
        }

        cropPhoto();
    }
    private void handleImageBeforeKitKat(Intent intent) {
        imageUri = intent.getData();
        imagePath = getImagePath(imageUri, null);
        cropPhoto();
    }
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection老获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PICK_IMAGE://从相册选择
                if (Build.VERSION.SDK_INT >= 19) {
                    handleImageOnKitKat(data);
                } else {
                    handleImageBeforeKitKat(data);
                }
                break;
            case REQUEST_CAPTURE://拍照
                if (resultCode == RESULT_OK) {
                    cropPhoto();
                }
                break;
            case REQUEST_PICTURE_CUT://裁剪完成
                Bitmap bitmap = null;
                try {
                    if (isClickCamera) {

                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        saveBitmapToSharedPreferences(bitmap);
                    } else {
                        bitmap = BitmapFactory.decodeFile(imagePath);
                    }
                    headPortrait.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_PERMISSION://权限请求
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    finish();
                } else {
                    if (isClickCamera) {
                        openCamera();
                    } else {
                        selectFromAlbum();
                    }
                }
                break;
        }
    }
    private void saveBitmapToSharedPreferences(Bitmap bitmapImage){
        Bitmap bitmap= bitmapImage;
        //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
        byte[] byteArray=byteArrayOutputStream.toByteArray();
        String imageString=new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //第三步:将String保持至SharedPreferences
        SharedPreferences sharedPreferences=getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("image", imageString);
        editor.commit();
    }
    private Bitmap getBitmapFromSharedPreferences(){
        SharedPreferences sharedPreferences=getSharedPreferences("testSP", Context.MODE_PRIVATE);
        //第一步:取出字符串形式的Bitmap
        String imageString=sharedPreferences.getString("image", "");
        //第二步:利用Base64将字符串转换为ByteArrayInputStream
        byte[] byteArray=Base64.decode(imageString, Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArray);
        //第三步:利用ByteArrayInputStream生成Bitmap
        Bitmap bitmap=BitmapFactory.decodeStream(byteArrayInputStream);
        return bitmap;
    }

}
