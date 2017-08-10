package com.lst.lscourier.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.lst.lscourier.R;
import com.lst.lscourier.utils.FileUtils;
import com.lst.lscourier.utils.GlideCircleTransform;
import com.lst.lscourier.utils.SharePrefUtil;


/**
 * Created by lst719 on 2017/7/24.
 */

public class DataFillingActivity extends Activity implements View.OnClickListener{
    private Button upload_front,upload_back, data_filling_next_step;
    private TextView postgraduate,undergraduate,junior_college,senior_high_school
            ,junior_high_school,primary_school,educational_background_et,bicycle,subway,bus
            ,car,electrombile,motorcycle, popwindow_Item_gallery, popwindow_Item_camera, cancle;
    private ImageView identification_photo;
    private LinearLayout educational_background,popwindowBackground;
    private PopupWindow popupWindow_educational_background,headerpopuWindow;
    private View view;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_data_filling);
        initView();
        initTitle();

    }
    private void initTitle() {
        ImageView title_back = (ImageView) findViewById(R.id.title_back);
        TextView title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText(R.string.data_filling);
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataFillingActivity.this.finish();
            }
        });
    }
    private void initView() {
        identification_photo= (ImageView) findViewById(R.id.identification_photo);
        upload_front= (Button) findViewById(R.id.upload_front);
        upload_back= (Button) findViewById(R.id.upload_back);
        data_filling_next_step = (Button) findViewById(R.id.data_filling_next_step);
        educational_background= (LinearLayout) findViewById(R.id.educational_background);
        educational_background_et= (TextView) findViewById(R.id.educational_background_et);
        bicycle= (TextView) findViewById(R.id.bicycle);
        subway= (TextView) findViewById(R.id.subway);
        bus= (TextView) findViewById(R.id.bus);
        car= (TextView) findViewById(R.id.car);
        electrombile= (TextView) findViewById(R.id.electrombile);
        motorcycle= (TextView) findViewById(R.id.motorcycle);
        identification_photo.setOnClickListener(this);
        upload_front.setOnClickListener(this);
        upload_back.setOnClickListener(this);
        data_filling_next_step.setOnClickListener(this);
        educational_background.setOnClickListener(this);
        bicycle.setOnClickListener(this);
        subway.setOnClickListener(this);
        bus.setOnClickListener(this);
        car.setOnClickListener(this);
        electrombile.setOnClickListener(this);
        motorcycle.setOnClickListener(this);
        setChosed(electrombile);
//        Glide.with(this).load("file://"+ FileUtils.makeFile())
//                .error(R.mipmap.head_tmp)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .crossFade().placeholder(R.mipmap.head_tmp)
//                .transform(new GlideCircleTransform(this))
//                .into(identification_photo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPopupWindow();
    }

    //点击改变出行方式textview的背景
    public void setChosed(View view){
        bicycle.setSelected(false);
        subway.setSelected(false);
        bus.setSelected(false);
        car.setSelected(false);
        electrombile.setSelected(false);
        motorcycle.setSelected(false);
        view.setSelected(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bicycle:
                setChosed(bicycle);
                break;
            case R.id.subway:
                setChosed(subway);
                break;
            case R.id.bus:
                setChosed(bus);
                break;
            case R.id.car:
                setChosed(car);
                break;
            case R.id.electrombile:
                setChosed(electrombile);
                break;
            case R.id.motorcycle:
                setChosed(motorcycle);
                break;

            case R.id.identification_photo:
                headerpopuWindow.showAtLocation(identification_photo,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.upload_front:
                break;
            case R.id.upload_back:
                break;
            case R.id.data_filling_next_step:
                SharePrefUtil.saveBoolean(DataFillingActivity.this,"isDataFilling",true);
                intent=new Intent().setClass(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.popwindow_gallery:
                gallery();
                headerpopuWindow.dismiss();
                break;
            case R.id.popwindow_camera:
                camera();
                headerpopuWindow.dismiss();
                break;
            case R.id.popwindow_cancle:
                headerpopuWindow.dismiss();
                break;
            case R.id.popwindow_choice_background:
                headerpopuWindow.dismiss();
                break;
            case R.id.educational_background:
                initPopupWindow_educational_background();
                break;
            case R.id.postgraduate:
                educational_background_et.setText(postgraduate.getText().toString());
                popupWindow_educational_background.dismiss();
                break;
            case R.id.undergraduate:
                educational_background_et.setText(undergraduate.getText().toString());
                popupWindow_educational_background.dismiss();
                break;
            case R.id.junior_college:
                educational_background_et.setText(junior_college.getText().toString());
                popupWindow_educational_background.dismiss();
                break;
            case R.id.senior_high_school:
                educational_background_et.setText(senior_high_school.getText().toString());
                popupWindow_educational_background.dismiss();
                break;
            case R.id.junior_high_school:
                educational_background_et.setText(junior_high_school.getText().toString());
                popupWindow_educational_background.dismiss();
                break;
            case R.id.primary_school:
                educational_background_et.setText(primary_school.getText().toString());
                popupWindow_educational_background.dismiss();
                break;
        }
    }


    private void initPopupWindow_educational_background(){
        View contentView= LayoutInflater.from(this).inflate(R.layout.list_educational_background,null);

        //TODO popupWindow在屏幕显示的宽度设置
        WindowManager manager=(WindowManager)getSystemService(DataFillingActivity.WINDOW_SERVICE);
        int width=manager.getDefaultDisplay().getWidth()/13*11;

        popupWindow_educational_background=new PopupWindow(contentView,width,
                ViewGroup.LayoutParams.WRAP_CONTENT,true);
        postgraduate= (TextView) contentView.findViewById(R.id.postgraduate);
        undergraduate= (TextView) contentView.findViewById(R.id.undergraduate);
        junior_college= (TextView) contentView.findViewById(R.id.junior_college);
        senior_high_school= (TextView) contentView.findViewById(R.id.senior_high_school);
        junior_high_school= (TextView) contentView.findViewById(R.id.junior_high_school);
        primary_school= (TextView) contentView.findViewById(R.id.primary_school);

        postgraduate.setOnClickListener(this);
        undergraduate.setOnClickListener(this);
        junior_college.setOnClickListener(this);
        senior_high_school.setOnClickListener(this);
        junior_high_school.setOnClickListener(this);
        primary_school.setOnClickListener(this);

        popupWindow_educational_background.setOutsideTouchable(true);
        popupWindow_educational_background.setFocusable(true);

        popupWindow_educational_background.setBackgroundDrawable(new BitmapDrawable());
        popupWindow_educational_background.showAsDropDown(educational_background);
    }


    private void initPopupWindow() {
        view = this.getLayoutInflater().inflate(R.layout.popwindow_userinfo_choice, null);
        headerpopuWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popwindow_Item_gallery = (TextView) view.findViewById(R.id.popwindow_gallery);
        popwindow_Item_camera = (TextView) view.findViewById(R.id.popwindow_camera);
        cancle = (TextView) view.findViewById(R.id.popwindow_cancle);
        popwindowBackground = (LinearLayout) view.findViewById(R.id.popwindow_choice_background);
        popwindow_Item_gallery.setOnClickListener(this);
        popwindow_Item_camera.setOnClickListener(this);
        cancle.setOnClickListener(this);
        popwindowBackground.setOnClickListener(this);
    }

    public void camera() {
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 下面这句指定调用相机拍照后的照片存储的路径
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(FileUtils.makeFile()));
        if (FileUtils.insertDummyContactWrapper(
                this, Manifest.permission.CAMERA)) {
            startActivityForResult(takeIntent, 1);
        }
    }

    public void gallery() {
        // 图库选择
        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
        // 也可以直接写如："image/jpeg 、 image/png等的类型"
        pickIntent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        if (FileUtils.insertDummyContactWrapper(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            this.startActivityForResult(pickIntent, 2);
        }
    }

    public void gallery_id() {
        // 图库选择
        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
        // 也可以直接写如："image/jpeg 、 image/png等的类型"
        pickIntent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        if (FileUtils.insertDummyContactWrapper(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            this.startActivityForResult(pickIntent, 4);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Activity.RESULT_OK:
                if (requestCode == 1) {
                    startPhotoZoom(Uri.fromFile(FileUtils.makeFile()));
                } else if (requestCode == 3) {
                    Glide.with(this).load("file://" + FileUtils.makeFile())
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .crossFade().transform(new GlideCircleTransform(this))
                            .error(R.mipmap.head_tmp)
                            .into(new GlideDrawableImageViewTarget(identification_photo));
                } else if (requestCode == 2) {
                    startPhotoZoom(data.getData());
                }else if(requestCode==4){
                    startPhotoZoom(data.getData());
                }
                break;
        }
    }


    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(FileUtils.makeFile()));
        startActivityForResult(intent, 3);
    }
}
