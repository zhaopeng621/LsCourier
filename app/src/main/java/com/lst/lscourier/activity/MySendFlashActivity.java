package com.lst.lscourier.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.lst.lscourier.R;
import com.lst.lscourier.app.App;
import com.lst.lscourier.bean.UserBean;
import com.lst.lscourier.parmas.MyJsonObjectRequest;
import com.lst.lscourier.parmas.ParmasUrl;
import com.lst.lscourier.photo.MultipartRequest;
import com.lst.lscourier.utils.FileUtils;
import com.lst.lscourier.utils.GlideCircleTransform;
import com.lst.lscourier.utils.SharePrefUtil;
import com.lst.lscourier.utils.VolleyErrorHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//import com.lst.lscourier.view.RoundImageView;


/**
 * 我的闪送
 */

public class MySendFlashActivity extends Activity implements View.OnClickListener {

    private TextView popwindow_Item_gallery, popwindow_Item_camera, cancle, exit, username, general_income, monthly_income, yesterday_income, order, cash_account;
    private ImageView head_portrait;
    private PopupWindow headerpopuWindow;
    private View view;
    private LinearLayout popwindowBackground, my_order_ll, cash_account_ll,
            rewards_and_punishment_record_ll, promotion_record_ll;
    private UserBean userBean = new UserBean();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    userBean = (UserBean) msg.obj;
                    getUserDetail();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_send_flash);
        initTitle();
        initView();
        getUserBean(SharePrefUtil.getString(MySendFlashActivity.this, "userid",
                ""));
    }

    private void initView() {
        username = (TextView) findViewById(R.id.username);
        exit = (TextView) findViewById(R.id.exit);
        general_income = (TextView) findViewById(R.id.general_income);
        monthly_income = (TextView) findViewById(R.id.monthly_income);
        yesterday_income = (TextView) findViewById(R.id.yesterday_income);
        order = (TextView) findViewById(R.id.order);
        cash_account = (TextView) findViewById(R.id.cash_account);
        head_portrait = (ImageView) findViewById(R.id.head_portrait);
        my_order_ll = (LinearLayout) findViewById(R.id.my_order_ll);
        cash_account_ll = (LinearLayout) findViewById(R.id.cash_account_ll);
        rewards_and_punishment_record_ll = (LinearLayout) findViewById(R.id.
                rewards_and_punishment_record_ll);
        promotion_record_ll = (LinearLayout) findViewById(R.id.promotion_record_ll);
        head_portrait.setOnClickListener(this);
        my_order_ll.setOnClickListener(this);
        cash_account_ll.setOnClickListener(this);
        rewards_and_punishment_record_ll.setOnClickListener(this);
        promotion_record_ll.setOnClickListener(this);
        exit.setOnClickListener(this);
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

    public void getUserDetail() {
        String text = userBean.getUsername();
        if (!TextUtils.isEmpty(text) && text.length() > 6) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            username.setText(sb.toString());
        }
        if (userBean.getPic() != null && !userBean.getPic().equals("null")) {
//            ImageCacheManager.loadImage(userBean.getPic(), head_portrait, ImageCacheManager.getBitmapFromRes(MySendFlashActivity.this, R.mipmap.default_user_head),
//                    ImageCacheManager.getBitmapFromRes(MySendFlashActivity.this, R.mipmap.default_user_head));
            Glide.with(MySendFlashActivity.this).load(userBean.getPic())
                    .error(R.mipmap.default_user_head)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .crossFade().placeholder(R.mipmap.default_user_head)
                    .transform(new GlideCircleTransform(this))
                    .into(head_portrait);
        }
        general_income.setText(userBean.getAll_money());
        monthly_income.setText(userBean.getMonth_money());
        yesterday_income.setText(userBean.getY_day_money());
        order.setText(userBean.getOrder());
        cash_account.setText(userBean.getMoney());
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPopupWindow();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exit:
                startActivity(new Intent(MySendFlashActivity.this, LoginActivity.class));
                SharePrefUtil.saveBoolean(MySendFlashActivity.this, "isLogin",
                        false);
                App.getInstance().exit();
                MySendFlashActivity.this.finish();
                break;
            case R.id.my_order_ll:
                startActivity(new Intent(MySendFlashActivity.this, MyOrderActivity.class));
                break;

            case R.id.cash_account_ll:
                startActivity(new Intent(MySendFlashActivity.this, MyWalletActivity.class));
                break;
            case R.id.rewards_and_punishment_record_ll:
                startActivity(new Intent(MySendFlashActivity.this, DisciplinaryRecordsActivity.class));
                break;
            case R.id.promotion_record_ll:
                startActivity(new Intent(MySendFlashActivity.this, AboutMeActivity.class));
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
                            .error(R.mipmap.default_user_head)
                            .into(new GlideDrawableImageViewTarget(head_portrait));
                    //上传图片
                    Map<String, String> params = new HashMap<>();
                    params.put("id", userBean.getId());
                    MultipartRequest mRequest = new MultipartRequest(ParmasUrl.editpic, new MyErrorListener(),
                            new MyListener(),
                            "pic", FileUtils.makeFile(),
                            params);
                    App.getHttpQueue().add(mRequest);
                } else if (requestCode == 2) {
                    startPhotoZoom(data.getData());
                }
                break;
        }
    }

    private class MyErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(MySendFlashActivity.this, VolleyErrorHelper.getMessage(volleyError, MySendFlashActivity.this), Toast.LENGTH_SHORT).show();
        }
    }

    private class MyListener implements Response.Listener<String> {
        @Override
        public void onResponse(String s) {
            try {
                Log.d("updatpic======", s.toString());
                JSONObject object = new JSONObject(s);
                if (object.getString("code").equals("200")) {
                    userBean.setPic(object.getString("url"));
                    SharePrefUtil.saveObj(MySendFlashActivity.this, "User", userBean);
                }
                Toast.makeText(MySendFlashActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

    /*
       * 获取用户信息
       */
    @SuppressLint("NewApi")
    private void getUserBean(String mId) {
        String url = ParmasUrl.showuser;
        Map<String, String> map = new HashMap<>();
        map.put("id", mId);
        // 创建StringRequest，定义字符串请求的请求方式为POST，
        MyJsonObjectRequest jsonObjectRequest = new MyJsonObjectRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                Log.d("getUserBean======", object.toString());
                try {
                    if (object.getString("code").equals("200")) {
                        JSONObject data = object.getJSONObject("data");
                        JSONObject obj = data.getJSONObject("0");
                        UserBean userBean = new UserBean();
                        userBean.setId(obj.getString("id"));
                        userBean.setUsername(obj.getString("username"));
                        userBean.setPic(obj.getString("pic"));
//                        userBean.setId_card(obj.getString("id_card"));
//                        userBean.setStatus(obj.getString("status"));
                        userBean.setIs_pay(obj.getString("is_pay"));
                        userBean.setMoney(obj.getString("money"));
                        userBean.setToday_money(data.getString("today_money"));
                        userBean.setY_day_money(data.getString("y_day_money"));
                        userBean.setAll_money(data.getString("all_money"));
                        userBean.setExit_order(data.getString("exit_order"));
                        userBean.setOrder(data.getString("order"));
                        userBean.setMonth_money(data.getString("month_money"));
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        msg.obj = userBean;
                        handler.sendMessage(msg);
                    }
                    Toast.makeText(MySendFlashActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MySendFlashActivity.this, VolleyErrorHelper.getMessage(volleyError, MySendFlashActivity.this), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setTag("loginPost");
        // 将请求添加到队列中
        App.getHttpQueue().add(jsonObjectRequest);

    }

}
