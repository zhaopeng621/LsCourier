package com.lst.lscourier.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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


/**
 * Created by lst719 on 2017/7/27.
 */

public class MySendFlashActivity extends Activity implements View.OnClickListener{

    private TextView popwindow_Item_gallery,popwindow_Item_camera, cancle;
    private ImageView head_portrait;
    private PopupWindow headerpopuWindow;
    private View view;
    private LinearLayout popwindowBackground,my_order_ll,cash_account_ll,
            rewards_and_punishment_record_ll,promotion_record_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_send_flash);
        initTitle();
        initView();
    }

    private void initView() {
        head_portrait= (ImageView) findViewById(R.id.head_portrait);
        my_order_ll= (LinearLayout) findViewById(R.id.my_order_ll);
        cash_account_ll= (LinearLayout) findViewById(R.id.cash_account_ll);
        rewards_and_punishment_record_ll= (LinearLayout) findViewById(R.id.
                rewards_and_punishment_record_ll);
        promotion_record_ll= (LinearLayout) findViewById(R.id.promotion_record_ll);
        head_portrait.setOnClickListener(this);
        my_order_ll.setOnClickListener(this);
        cash_account_ll.setOnClickListener(this);
        rewards_and_punishment_record_ll.setOnClickListener(this);
        promotion_record_ll.setOnClickListener(this);
        Glide.with(this).load("file://"+ FileUtils.makeFile())
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .crossFade().placeholder(R.mipmap.ic_launcher)
                .transform(new GlideCircleTransform(this))
                .into(head_portrait);
    }

    private void initTitle() {
        ImageView title_back = (ImageView) findViewById(R.id.title_back);
        TextView title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText(R.string.my_send_flash);
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySendFlashActivity.this.finish();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        initPopupWindow();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.my_order_ll:
                break;
            case R.id.cash_account_ll:
                break;
            case R.id.rewards_and_punishment_record_ll:
                break;
            case R.id.promotion_record_ll:
                break;
            case R.id.head_portrait:
                headerpopuWindow.showAtLocation(head_portrait,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
        }
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
                            .error(R.mipmap.ic_launcher)
                            .into(new GlideDrawableImageViewTarget(head_portrait));
                } else if (requestCode == 2) {
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
