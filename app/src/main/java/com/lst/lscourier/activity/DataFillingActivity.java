package com.lst.lscourier.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.lst.lscourier.R;
import com.lst.lscourier.app.App;
import com.lst.lscourier.parmas.ParmasUrl;
import com.lst.lscourier.photo.MultipartRequest;
import com.lst.lscourier.utils.FileUtils;
import com.lst.lscourier.utils.IDCardUtils;
import com.lst.lscourier.utils.ToastUtils;
import com.lst.lscourier.utils.VolleyErrorHelper;
import com.lst.lscourier.view.FlowRadioGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by lst719 on 2017/7/24.
 */

public class DataFillingActivity extends Activity implements View.OnClickListener {
    private Button upload_front, upload_back, data_filling_next_step;
    private FlowRadioGroup radioGroup;
    private TextView educational_background_et, postgraduate, undergraduate, junior_college, senior_high_school,
            junior_high_school, primary_school, popwindow_Item_gallery, popwindow_Item_camera, cancle;
    private EditText et_name, card_number, profession, emergency_contact, emergency_contact_number, et_current_address;
    private ImageView identification_photo, img_upload_back, img_upload_front;
    private LinearLayout educational_background, popwindowBackground;
    private PopupWindow popupWindow_educational_background, headerpopuWindow;
    private Intent intent;
    private String tripMode = "";
    private File headFile = new File("");
    private File frontFile = new File("");
    private File backFile = new File("");


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
        identification_photo = (ImageView) findViewById(R.id.identification_photo);
        img_upload_front = (ImageView) findViewById(R.id.img_upload_front);
        img_upload_back = (ImageView) findViewById(R.id.img_upload_back);
        et_name = (EditText) findViewById(R.id.et_name);
        card_number = (EditText) findViewById(R.id.card_number);
        educational_background_et = (TextView) findViewById(R.id.educational_background_et);
        profession = (EditText) findViewById(R.id.profession);
        emergency_contact = (EditText) findViewById(R.id.emergency_contact);
        emergency_contact_number = (EditText) findViewById(R.id.emergency_contact_number);
        et_current_address = (EditText) findViewById(R.id.et_current_address);
        radioGroup = (FlowRadioGroup) findViewById(R.id.radioGroup);

        upload_front = (Button) findViewById(R.id.upload_front);
        upload_back = (Button) findViewById(R.id.upload_back);
        data_filling_next_step = (Button) findViewById(R.id.data_filling_next_step);
        educational_background = (LinearLayout) findViewById(R.id.educational_background);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                //获取变更后的选中项的ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton) DataFillingActivity.this.findViewById(radioButtonId);
                //更新文本内容，以符合选中项
                tripMode = rb.getText().toString();
            }
        });
        identification_photo.setOnClickListener(this);
        upload_front.setOnClickListener(this);
        upload_back.setOnClickListener(this);
        data_filling_next_step.setOnClickListener(this);
        educational_background.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initPopupWindow();
    }

    private void reSelected() {
        identification_photo.setSelected(false);
        upload_front.setSelected(false);
        upload_back.setSelected(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.identification_photo:
                headerpopuWindow.showAtLocation(identification_photo,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                reSelected();
                identification_photo.setSelected(true);
                break;
            case R.id.upload_front:
                headerpopuWindow.showAtLocation(upload_front,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                reSelected();
                upload_front.setSelected(true);
                break;
            case R.id.upload_back:
                headerpopuWindow.showAtLocation(upload_back,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                reSelected();
                upload_back.setSelected(true);
                break;
            case R.id.data_filling_next_step:

                if (!headFile.exists()) {
                    ToastUtils.showToast(DataFillingActivity.this, "请上传证件照");
                } else if (et_name.getText().toString().equals("")) {
                    ToastUtils.showToast(DataFillingActivity.this, "真实姓名不能为空");
                } else if (card_number.getText().toString().equals("")) {
                    ToastUtils.showToast(DataFillingActivity.this, "身份证号码不能为空");
                } else if (!IDCardUtils.IDCardValidate(card_number.getText().toString()).equals("")) {
                    ToastUtils.showToast(DataFillingActivity.this,
                            IDCardUtils.IDCardValidate(card_number.getText().toString()));
                } else if (!frontFile.exists() || !backFile.exists()) {
                    ToastUtils.showToast(DataFillingActivity.this, "请上传身份证");
                } else if (educational_background_et.getText().toString().equals("")) {
                    ToastUtils.showToast(DataFillingActivity.this, "请选择学历");
                } else if (profession.getText().toString().equals("")) {
                    ToastUtils.showToast(DataFillingActivity.this, "请填写职业");
                } else if (emergency_contact.getText().toString().equals("")) {
                    ToastUtils.showToast(DataFillingActivity.this, "紧急联系人不能为空");
                } else if (emergency_contact_number.getText().toString().equals("")) {
                    ToastUtils.showToast(DataFillingActivity.this, "紧急联系人电话不能为空");
                } else if (et_current_address.getText().toString().equals("")) {
                    ToastUtils.showToast(DataFillingActivity.this, "请填写现居住地地址");
                } else if (tripMode.equals("")) {
                    tripMode = "自行车";
                } else {
                    perfectInformation();
                }
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

    /*
       * 完善信息
       */

    private void perfectInformation() {
        Map<String, String> map = new HashMap<>();
        map.put("username", et_name.getText().toString());
        map.put("password", card_number.getText().toString());
        map.put("password", educational_background_et.getText().toString());
        map.put("password", profession.getText().toString());
        map.put("password", emergency_contact.getText().toString());
        map.put("password", emergency_contact_number.getText().toString());
        map.put("password", tripMode);
        List<File> files = new ArrayList<>();
        files.add(headFile);
        files.add(frontFile);
        files.add(backFile);
        MultipartRequest mRequest = new MultipartRequest(ParmasUrl.editpic, new MyErrorListener(),
                new MyListener(),
                "pic", files,
                map);
        App.getHttpQueue().add(mRequest);
    }

    private class MyListener implements Response.Listener<String> {
        @Override
        public void onResponse(String s) {
            try {
                Log.d("updatpic======", s.toString());
                JSONObject object = new JSONObject(s);
                if (object.getString("code").equals("200")) {
//                intent = new Intent().setClass(this, MainActivity.class);
//                startActivity(intent);
//                finish();
                }
                Toast.makeText(DataFillingActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class MyErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(DataFillingActivity.this, VolleyErrorHelper.getMessage(volleyError, DataFillingActivity.this), Toast.LENGTH_SHORT).show();
        }
    }

    private void initPopupWindow_educational_background() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.list_educational_background, null);

        //TODO popupWindow在屏幕显示的宽度设置
        WindowManager manager = (WindowManager) getSystemService(DataFillingActivity.WINDOW_SERVICE);
        int width = manager.getDefaultDisplay().getWidth() / 13 * 11;

        popupWindow_educational_background = new PopupWindow(contentView, width,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        postgraduate = (TextView) contentView.findViewById(R.id.postgraduate);
        undergraduate = (TextView) contentView.findViewById(R.id.undergraduate);
        junior_college = (TextView) contentView.findViewById(R.id.junior_college);
        senior_high_school = (TextView) contentView.findViewById(R.id.senior_high_school);
        junior_high_school = (TextView) contentView.findViewById(R.id.junior_high_school);
        primary_school = (TextView) contentView.findViewById(R.id.primary_school);

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
        View view = this.getLayoutInflater().inflate(R.layout.popwindow_userinfo_choice, null);
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
                    DrawableRequestBuilder<String> transform = Glide.with(this).load("file://" + FileUtils.makeFile())
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .crossFade();
                    if (identification_photo.isSelected()) {
                        transform.error(R.mipmap.head_tmp);
                        transform.into(new GlideDrawableImageViewTarget(identification_photo));
                        headFile = FileUtils.makeFile();
                    } else if (upload_front.isSelected()) {
                        transform.error(R.mipmap.id_card_a_tmp);
                        transform.into(new GlideDrawableImageViewTarget(img_upload_front));
                        frontFile = FileUtils.makeFile();
                    } else if (upload_back.isSelected()) {
                        transform.error(R.mipmap.idcard_b_tmp);
                        transform.into(new GlideDrawableImageViewTarget(img_upload_back));
                        backFile = FileUtils.makeFile();
                    }
                } else if (requestCode == 2) {
                    startPhotoZoom(data.getData());
                } else if (requestCode == 4) {
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
