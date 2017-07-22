package com.lst.lscourier.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lst.lscourier.LruCacheUtils.ImageCacheManager;
import com.lst.lscourier.R;
import com.lst.lscourier.app.App;
import com.lst.lscourier.bean.UserBean;
import com.lst.lscourier.parmas.ParmasUrl;
import com.lst.lscourier.photo.MultipartRequest;
import com.lst.lscourier.photo.PictureUtil;
import com.lst.lscourier.utils.SharePrefUtil;
import com.lst.lscourier.utils.VolleyErrorHelper;
import com.lst.lscourier.view.RoundImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * 个人中心
 */
public class PersonalCenterActivity extends FragmentActivity implements View.OnClickListener {
    private RoundImageView my_face;
    private TextView tv_grade, tv_credit, phone_number, tv_grade_show;
    private Bitmap myBitmap;
    private File file;
    private UserBean userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_personal);
        initTitleData();
        initView();
//        getUserDetail();
    }

    public void initTitleData() {
        ImageView title_back = (ImageView) findViewById(R.id.title_back);
        TextView title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText("个人中心");
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonalCenterActivity.this.finish();
            }
        });
    }

    private void initView() {
        my_face = (RoundImageView) findViewById(R.id.my_face);
        tv_grade = (TextView) findViewById(R.id.tv_grade);
        tv_credit = (TextView) findViewById(R.id.tv_credit);
        phone_number = (TextView) findViewById(R.id.phone_number);
        tv_grade_show = (TextView) findViewById(R.id.tv_grade_show);

        tv_grade_show.setOnClickListener(this);
        my_face.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_grade_show:
                Intent webIntent = new Intent(PersonalCenterActivity.this, WebActivity.class);
                webIntent.putExtra("weburl", "file:///android_asset/dengji.html");
                webIntent.putExtra("title", "等级说明");
                startActivity(webIntent);
                break;
            case R.id.my_face:
                PictureUtil.doPickPhotoAction(PersonalCenterActivity.this);
                break;
            default:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent("com.android.camera.action.CROP");
        Uri data2 = null;
        if (data == null) {
            data2 = PictureUtil.getImageUri();
        } else {
            data2 = data.getData();
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureUtil.PHOTO_PICKED_WITH_DATA:
                    intent.setDataAndType(data2, "image/*");
                    intent.putExtra("crop", true);
                    // 设置裁剪尺寸
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("outputX", 160);
                    intent.putExtra("outputY", 130);
                    intent.putExtra("return-data", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            PictureUtil.getImageCaiUri());
                    startActivityForResult(intent, PictureUtil.PHOTO_CROP);
                    break;
                case PictureUtil.CAMERA_WITH_DATA:
                    intent.setDataAndType(data2, "image/*");
                    intent.putExtra("crop", true);
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("outputX", 160);
                    intent.putExtra("outputY", 130);
                    intent.putExtra("return-data", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            PictureUtil.getImageCaiUri());
                    startActivityForResult(intent, PictureUtil.PHOTO_CROP);
                    break;
                case PictureUtil.PHOTO_CROP:
                    Bundle bundle = data.getExtras();
                    myBitmap = (Bitmap) bundle.get("data");
                    Bitmap bitImage = PictureUtil.comp(myBitmap);
                    String fileName = PictureUtil.getCharacterAndNumber();
                    file = new File(PictureUtil.PHOTO_DIR, fileName + ".png");
                    PictureUtil.saveMyBitmap(bitImage, file);
                    my_face.setImageBitmap(bitImage);
                    //上传图片
                    Map<String, String> params = new HashMap<>();
                    params.put("id", userBean.getUserid());
                    MultipartRequest mRequest = new MultipartRequest(ParmasUrl.editpic, new MyErrorListener(),
                            new MyListener(),
                            "pic", file,
                            params);
                    App.getHttpQueue().add(mRequest);

                    break;

                default:
                    break;
            }
        }
    }

    public void getUserDetail() {
        userBean = (UserBean) SharePrefUtil.getObj(PersonalCenterActivity.this, "User");
        String text = userBean.getPhone();
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
            phone_number.setText(sb.toString());
        }
        if (userBean.getPic() != null && !userBean.getPic().equals("null")) {
//            ImageLoader.getInstance().displayImage(userBean.getPic(), my_face);
            ImageCacheManager.loadImage(userBean.getPic(), my_face, ImageCacheManager.getBitmapFromRes(PersonalCenterActivity.this, R.mipmap.default_user_head),
                    ImageCacheManager.getBitmapFromRes(PersonalCenterActivity.this, R.mipmap.default_user_head));
        }
        tv_grade.setText("等级:LV"+userBean.getLevel());
        tv_credit.setText("信誉分:"+Float.valueOf(userBean.getIntegral()));
    }

    private class MyErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(PersonalCenterActivity.this, VolleyErrorHelper.getMessage(volleyError, PersonalCenterActivity.this), Toast.LENGTH_SHORT).show();
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
                    //上传成功后传回
                    Bitmap bit = ((BitmapDrawable) my_face.getDrawable())
                            .getBitmap();
                    Intent intent2 = new Intent();
                    intent2.putExtra("bitmap", bit);
                    PersonalCenterActivity.this.setResult(11, intent2);
                }
                Toast.makeText(PersonalCenterActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
