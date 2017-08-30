package com.lst.lscourier.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.lst.lscourier.R;
import com.lst.lscourier.app.App;
import com.lst.lscourier.parmas.MyJsonObjectRequest;
import com.lst.lscourier.parmas.ParmasUrl;
import com.lst.lscourier.photo.MultipartRequest;
import com.lst.lscourier.utils.FileUtils;
import com.lst.lscourier.utils.IDCardUtils;
import com.lst.lscourier.utils.SharePrefUtil;
import com.lst.lscourier.utils.ToastUtils;
import com.lst.lscourier.utils.VolleyErrorHelper;
import com.lst.lscourier.view.FlowRadioGroup;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 完善资料
 */

public class DataFillingActivity extends Activity implements View.OnClickListener {
    private Button upload_front, upload_back, data_filling_next_step;
    private FlowRadioGroup radioGroup;
    private TextView educational_background_et, postgraduate, undergraduate, junior_college, senior_high_school,
            junior_high_school, primary_school, popwindow_Item_gallery, popwindow_Item_camera, cancle;
    private EditText et_name, card_number, profession, emergency_contact, emergency_contact_number, et_current_address;
    private ImageView identification_photo, img_upload_back, img_upload_front;
    private LinearLayout educational_background, popwindowBackground;
    private PopupWindow popupWindow_educational_background, headerpopuWindow,popupwindow_more;
    private String tripMode = "";
    private File headFile;
    private File frontFile;
    private File backFile;
    private PopupWindow mPopupwindow;
    private final String mMode = "01";
    private String msg = "";
    private ImageView more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_data_filling);
        initView();
        initTitle();

    }

    private void initTitle() {
        more = (ImageView) findViewById(R.id.more);
        TextView title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText(R.string.data_filling);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPopupWindow_more();
            }
        });
    }

    private void initPopupWindow_more() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_more_background, null);
        //TODO popupWindow在屏幕显示的宽度设置
        popupwindow_more = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        LinearLayout lin_exit = (LinearLayout) contentView.findViewById(R.id.lin_exit);

        lin_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DataFillingActivity.this, LoginActivity.class));
                SharePrefUtil.saveBoolean(DataFillingActivity.this, "isLogin",
                        false);
                App.getInstance().exit();
                popupwindow_more.dismiss();
                DataFillingActivity.this.finish();
            }
        });

        popupwindow_more.setOutsideTouchable(true);
        popupwindow_more.setFocusable(true);

        popupwindow_more.setBackgroundDrawable(new BitmapDrawable());
        popupwindow_more.showAsDropDown(more,0,35);
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
                    perData();

                }
                break;
            case R.id.popwindow_gallery:
                if (identification_photo.isSelected()) {
                    gallery();
                } else if (upload_front.isSelected()) {
                    gallery1();
                } else if (upload_back.isSelected()) {
                    gallery2();
                }
                headerpopuWindow.dismiss();

                break;
            case R.id.popwindow_camera:
                if (identification_photo.isSelected()) {
                    camera();
                } else if (upload_front.isSelected()) {
                    camera1();
                } else if (upload_back.isSelected()) {
                    camera2();
                }
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

    private void perData() {

        Map<String, String> map = new HashMap<>();
        map.put("id", SharePrefUtil.getString(DataFillingActivity.this, "userid", ""));
        map.put("name", et_name.getText().toString());
        map.put("id_number", card_number.getText().toString());
        map.put("school", educational_background_et.getText().toString());
        map.put("profession", profession.getText().toString());
        map.put("urgent", emergency_contact.getText().toString());
        map.put("urgent_phone", emergency_contact_number.getText().toString());
        map.put("trip_mode", tripMode);
        map.put("address", et_current_address.getText().toString());
        List<File> files = new ArrayList<>();
        files.add(headFile);
        files.add(frontFile);
        files.add(backFile);
        List<String> strings = new ArrayList<>();
        strings.add("pic");
        strings.add("id_cardz");
        strings.add("id_cardf");
        MultipartRequest mRequest = new MultipartRequest(ParmasUrl.per_data, new MyErrorListener(),
                new MyListener(),
                strings, files,
                map);
        App.getHttpQueue().add(mRequest);
    }

    private class MyListener implements Response.Listener<String> {
        @Override
        public void onResponse(String s) {
            try {
                Log.e("updatpic======", s.toString());
                JSONObject object = new JSONObject(s);
                if (object.getString("code").equals("200")) {
                    creatPopWindow("1000");
                    if (mPopupwindow != null && mPopupwindow.isShowing()) {
                        mPopupwindow.dismiss();
                    } else {
                        mPopupwindow.showAtLocation(data_filling_next_step, Gravity.BOTTOM, 0, 0);
                    }
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
            startActivityForResult(takeIntent, 10);
        }
    }

    public void camera1() {
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 下面这句指定调用相机拍照后的照片存储的路径
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(FileUtils.makeFile1()));
        if (FileUtils.insertDummyContactWrapper(
                this, Manifest.permission.CAMERA)) {
            startActivityForResult(takeIntent, 11);
        }
    }

    public void camera2() {
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 下面这句指定调用相机拍照后的照片存储的路径
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(FileUtils.makeFile2()));
        if (FileUtils.insertDummyContactWrapper(
                this, Manifest.permission.CAMERA)) {
            startActivityForResult(takeIntent, 12);
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
            this.startActivityForResult(pickIntent, 20);
        }
    }

    public void gallery1() {
        // 图库选择
        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
        // 也可以直接写如："image/jpeg 、 image/png等的类型"
        pickIntent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        if (FileUtils.insertDummyContactWrapper(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            this.startActivityForResult(pickIntent, 21);
        }
    }

    public void gallery2() {
        // 图库选择
        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
        // 也可以直接写如："image/jpeg 、 image/png等的类型"
        pickIntent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        if (FileUtils.insertDummyContactWrapper(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            this.startActivityForResult(pickIntent, 22);
        }
    }

    private void creatPopWindow(String allPay) {
        View customView = DataFillingActivity.this.getLayoutInflater()
                .inflate(R.layout.popmenu_confirnorder, null, false);
        ImageView back = (ImageView) customView.findViewById(R.id.pop_back);

        TextView goods_all_pay = (TextView) customView
                .findViewById(R.id.goods_all_pay);
        Button goto_pay = (Button) customView.findViewById(R.id.goto_pay);
        goods_all_pay.setText(allPay);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        mPopupwindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
//        mPopupwindow.setBackgroundDrawable(new BitmapDrawable());
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        mPopupwindow.setBackgroundDrawable(dw);
        // 刷新状态（必须刷新否则无效）
        mPopupwindow.update();

        goto_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPaySn();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mPopupwindow.dismiss();
            }
        });

        customView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (mPopupwindow != null && mPopupwindow.isShowing()) {
                    mPopupwindow.dismiss();
                }
                return false;
            }
        });
    }

    //获取银行流水号
    private void getPaySn() {
        String url = ParmasUrl.foregift;
        Map<String, String> map = new HashMap<>();
        map.put("user_id", SharePrefUtil.getString(DataFillingActivity.this, "userid", ""));
        map.put("money", "1000");
        // 创建StringRequest，定义字符串请求的请求方式为POST，
        MyJsonObjectRequest jsonObjectRequest = new MyJsonObjectRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                try {
                    Log.d("foregift======", object.toString());
                    if (object.getString("code").equals("200")) {
                        String sign = object.getString("data");

                        UPPayAssistEx.startPayByJAR(
                                DataFillingActivity.this,
                                PayActivity.class, null, null, sign,
                                mMode);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(DataFillingActivity.this, VolleyErrorHelper.getMessage(volleyError, DataFillingActivity.this), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setTag("loginPost");
        // 将请求添加到队列中
        App.getHttpQueue().add(jsonObjectRequest);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Activity.RESULT_OK:
                if (requestCode == 10) {
                    startPhotoZoom(Uri.fromFile(FileUtils.makeFile()));
                } else if (requestCode == 11) {
                    startPhotoZoom1(Uri.fromFile(FileUtils.makeFile1()));
                } else if (requestCode == 12) {
                    startPhotoZoom2(Uri.fromFile(FileUtils.makeFile2()));
                } else if (requestCode == 30) {
                    DrawableRequestBuilder<String> transform = Glide.with(this).load("file://" + FileUtils.makeFile())
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .crossFade();
                    transform.error(R.mipmap.head_tmp);
                    transform.into(new GlideDrawableImageViewTarget(identification_photo));
                    headFile = FileUtils.makeFile();
                } else if (requestCode == 31) {
                    DrawableRequestBuilder<String> transform = Glide.with(this).load("file://" + FileUtils.makeFile1())
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .crossFade();
                    transform.error(R.mipmap.id_card_a_tmp);
                    transform.into(new GlideDrawableImageViewTarget(img_upload_front));
                    frontFile = FileUtils.makeFile1();
                } else if (requestCode == 32) {
                    DrawableRequestBuilder<String> transform = Glide.with(this).load("file://" + FileUtils.makeFile2())
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .crossFade();
                    transform.error(R.mipmap.idcard_b_tmp);
                    transform.into(new GlideDrawableImageViewTarget(img_upload_back));
                    backFile = FileUtils.makeFile2();
                } else if (requestCode == 20) {
                    startPhotoZoom(data.getData());
                } else if (requestCode == 21) {
                    startPhotoZoom1(data.getData());
                } else if (requestCode == 22) {
                    startPhotoZoom2(data.getData());
                } else if (requestCode == 40) {
                    startPhotoZoom(data.getData());
                } else if (requestCode == 41) {
                    startPhotoZoom1(data.getData());
                } else if (requestCode == 42) {
                    startPhotoZoom2(data.getData());
                } else {

                    /*************************************************
                     * 步骤3：处理银联手机支付控件返回的支付结果
                     ************************************************/
                    if (data == null) {
                        return;
                    }


			/*
             * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
			 */
                    String str = data.getExtras().getString("pay_result");
                    if (str.equalsIgnoreCase("success")) {
                        // 支付成功后，extra中如果存在result_data，取出校验
                        // result_data结构见c）result_data参数说明
                        if (data.hasExtra("result_data")) {
                            String result = data.getExtras().getString("result_data");
                            try {
                                JSONObject resultJson = new JSONObject(result);
                                String sign = resultJson.getString("sign");
                                String dataOrg = resultJson.getString("data");
                                // 验签证书同后台验签证书
                                // 此处的verify，商户需送去商户后台做验签
                                boolean ret = verify(dataOrg, sign, mMode);
                                if (ret) {
                                    // 验证通过后，显示支付结果
                                    msg = "支付成功！";
                                } else {
                                    // 验证不通过后的处理
                                    // 建议通过商户后台查询支付结果
                                    msg = "支付失败！";
                                }
                            } catch (JSONException e) {
                            }
                        } else {
                            // 未收到签名信息
                            // 建议通过商户后台查询支付结果
                            msg = "支付成功！";
                        }
                    } else if (str.equalsIgnoreCase("fail")) {
                        msg = "支付失败！";
                    } else if (str.equalsIgnoreCase("cancel")) {
                        msg = "用户取消了支付";
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("支付结果通知");
                    builder.setMessage(msg);
                    builder.setInverseBackgroundForced(true);
                    // builder.setCustomTitle();
                    builder.setNegativeButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (msg.equals("支付成功！")) {
                                        perData();
                                    }
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                }
        }
    }


    private boolean verify(String msg, String sign64, String mode) {
        // 此处的verify，商户需送去商户后台做验签
        return true;

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
        startActivityForResult(intent, 30);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom1(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(FileUtils.makeFile1()));
        startActivityForResult(intent, 31);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom2(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(FileUtils.makeFile2()));
        startActivityForResult(intent, 32);
    }
}
