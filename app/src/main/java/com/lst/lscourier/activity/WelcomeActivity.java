package com.lst.lscourier.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.lst.lscourier.LruCacheUtils.ImageCacheManager;
import com.lst.lscourier.R;
import com.lst.lscourier.adapter.GuidePageAdapter;
import com.lst.lscourier.app.App;
import com.lst.lscourier.parmas.ParmasUrl;
import com.lst.lscourier.utils.SharePrefUtil;
import com.lst.lscourier.utils.Tools;
import com.lst.lscourier.utils.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 启动页
 */
public class WelcomeActivity extends Activity implements
        ViewPager.OnPageChangeListener {
    protected static final int CHECKUPDATE = 0X5645;
    protected static final int GETDOWNURL = 0x8878;
    protected static final int DOWN_ERROR = 0x4554;
    protected static final long SPLASH_DISPLAY_LENGHT = 3000;
    private ViewPager vp;
    //    private int[] imageIdArray;// 图片资源的数组  固定数据
    private List<String> imageList = new ArrayList<>();// 图片资源的集合 zzf 网络获取
    private List<View> viewList;// 图片资源的集合
    private ViewGroup vg;// 放置圆点
    // 实例化原点View
    private ImageView iv_point;
    private ImageView[] ivPointArray;
    // 最后一页的按钮
    private ImageButton ib_start;
    private TextView tv_version;
    private String versonNow;
    private String recNo;
    private String apkUrl;
    private boolean isNet;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GETDOWNURL:
                    apkUrl = (String) msg.obj;
                    if (imageList.size() != 1) {
                        if (recNo != null && !recNo.equals(versonNow)) {
                            showUpdateDialog();
                        }
                    } else {
                        ib_start.setVisibility(View.VISIBLE);
                        tv_version.setVisibility(View.VISIBLE);
                        tv_version.setText("V : " + versonNow);
                        if (recNo != null && !recNo.equals(versonNow)) {
                            showUpdateDialog();
                        } else {
                            //等待几秒后跳转
                            Timer timer = new Timer();
                            TimerTask tast = new TimerTask() {
                                @Override
                                public void run() {
                                    if (WelcomeActivity.this.isFinishing()) {
                                        return;
                                    } else {
                                        enterHome();
                                    }
                                }
                            };
                            timer.schedule(tast, SPLASH_DISPLAY_LENGHT);
                        }
                    }
                    break;
                case DOWN_ERROR:
                    Toast.makeText(WelcomeActivity.this, "服务器忙，请稍后重试", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        versonNow = getVersion();
        isNet = Tools.isConnnected(WelcomeActivity.this);
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setVisibility(View.VISIBLE);
        ib_start = (ImageButton) findViewById(R.id.guide_ib_start);
        ib_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePrefUtil.saveBoolean(WelcomeActivity.this, "isFirst",
                        false);
                enterHome();
            }
        });
        String week = getWeek();
        if (SharePrefUtil.getBoolean(WelcomeActivity.this, "isFirst", true)) {
            if (isNet) {
//            checkUpdate();
                getImgs();
            } else {
                showSettingDialog();
            }
        } else {
            if (!isNet) {
                showSettingDialog();
            } else {
                //            checkUpdate();
                if (week.equals("星期一")){
                    getImgs();
                    return;
                }
            }
            imageList = (List<String>) SharePrefUtil.getObj(WelcomeActivity.this, "ImgsList");
//            int size = imageList.size();
//            for (int i = 0; i < size; i++) {
//                if (i == (size - 1)) {
//                    String s = imageList.get(i);
//                    imageList.clear();
//                    imageList.add(s);
//                }
//            }
            // 加载ViewPager
            initViewPager();
            // 加载底部圆点
            initPoint();
        }

    }

    protected void downLoadApk() {
        final ProgressDialog pd; // 进度条对话框
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(apkUrl, pd);
                    System.out.println("_____apkUrl_______" + apkUrl);
                    sleep(3000);
                    installApk(file);
                    pd.dismiss(); // 结束掉进度条对话框
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = DOWN_ERROR;
                    mHandler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    protected void installApk(File file) {
        Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        // 执行的数据类型
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");// 编者按：此处Android应为android，否则造成安装不了
        startActivity(intent);
    }

    public static File getFileFromServer(String path, ProgressDialog pd)
            throws Exception {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            // 获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(),
                    "LstShopWeb.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                // 获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    protected void showUpdateDialog() {
        Builder builer = new Builder(this);
        builer.setTitle("版本升级");
        builer.setMessage("检查到有新的版本，请尽快升级");
        // 当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downLoadApk();
            }
        });
        // 当点取消按钮时进行登录
        builer.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builer.create();
        dialog.show();
    }


    private void checkUpdate() {
        // 请求地址
        String url = "http://www.360lst.com/mobile/index.php?act=index&op=apk_version";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject
                            obj = jsonObject.getJSONObject("datas");
                    recNo = obj.getString("version");
                    String url = obj.getString("url");
                    Message msg = mHandler.obtainMessage();
                    msg.what = GETDOWNURL;
                    msg.obj = url;
                    mHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(WelcomeActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setTag("abcPost");
        // 将请求添加到队列中
        App.getHttpQueue().add(jsonObjectRequest);
    }

    private void enterHome() {
        Intent intent = new Intent();
//        intent.setClass(WelcomeActivity.this, MainActivity.class);
        intent.setClass(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
        WelcomeActivity.this.finish();
    }

    private void showSettingDialog() {
        Builder builder = new Builder(WelcomeActivity.this);
        builder.setTitle("提示");
        builder.setCancelable(false);
        builder.setMessage("网络异常，请检查网络");
        builder.setNegativeButton("设置", new OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setPositiveButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String getVersion() {
        // 获得一个系统包管理器
        PackageManager pm = getPackageManager();
        // 获得包管理器
        try {
            // 获得功能清单文件
            PackageInfo packInfo = pm.getPackageInfo(getPackageName(), 0);
            String version = packInfo.versionName;
            System.out.println("oldversion----" + version);
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            // 不可能发生的异常
            return "";
        }
    }

    /**
     * 加载底部圆点
     */
    private void initPoint() {
        // 这里实例化LinearLayout

        vg = (ViewGroup) findViewById(R.id.guide_ll_point);
        // 根据ViewPager的item数量实例化数组
        ivPointArray = new ImageView[viewList.size()];
        // 循环新建底部圆点ImageView，将生成的ImageView保存到数组中
        int size = viewList.size();
        for (int i = 0; i < size; i++) {
            iv_point = new ImageView(this);
            iv_point.setLayoutParams(new ViewGroup.LayoutParams(20, 20));
            iv_point.setPadding(30, 0, 30, 0);// left,top,right,bottom
            ivPointArray[i] = iv_point;
            // 第一个页面需要设置为选中状态，这里采用两张不同的图片
            if (i == 0) {
                iv_point.setBackgroundResource(R.mipmap.login_point_selected);
            } else {
                iv_point.setBackgroundResource(R.mipmap.login_point);
            }
            // 将数组中的ImageView加入到ViewGroup
            vg.addView(ivPointArray[i]);
        }

    }

    /**
     * 加载图片ViewPager
     */
    private void initViewPager() {
        vp = (ViewPager) findViewById(R.id.guide_vp);
        // 实例化图片资源
//        if (SharePrefUtil.getBoolean(WelcomeActivity.this, "isFirst", true)) {
//           imageIdArray = new int[]{R.mipmap.one, R.mipmap.two,
//                   R.mipmap.three, R.mipmap.four};
//          imageIdArray = new int[]{R.mipmap.four};
//        } else {
//           imageIdArray = new int[]{R.mipmap.four};
//        }

        viewList = new ArrayList<View>();
        // 获取一个Layout参数，设置为全屏
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
               this.getWindowManager().getDefaultDisplay().getWidth(),
                this.getWindowManager().getDefaultDisplay().getHeight());

        // 循环创建View并加入到集合中
        int len = imageList.size();
        for (int i = 0; i < len; i++) {
            // new ImageView并设置全屏和图片资源
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(params);
            if (!isNet && SharePrefUtil.getBoolean(WelcomeActivity.this, "isFirst", true)) {
                imageView.setBackgroundResource(R.mipmap.ic_launcher);
            } else {
                ImageCacheManager.loadImage(imageList.get(i), imageView, ImageCacheManager.getBitmapFromRes(WelcomeActivity.this, R.mipmap.ic_launcher),
                        ImageCacheManager.getBitmapFromRes(WelcomeActivity.this, R.mipmap.ic_launcher));
            }
            Log.d("--------", imageList.get(i).toString());
            // 将ImageView加入到集合中
            viewList.add(imageView);
        }

        // View集合初始化好后，设置Adapter
        vp.setAdapter(new GuidePageAdapter(viewList)

        );
        // 设置滑动监听
        vp.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {

    }

    /**
     * 滑动后的监听
     *
     * @param position
     */
    public void onPageSelected(int position) {
        // 循环设置当前页的标记图
        int length = imageList.size();
        for (int i = 0; i < length; i++) {
            ivPointArray[position]
                    .setBackgroundResource(R.mipmap.login_point_selected);
            if (position != i) {
                ivPointArray[i].setBackgroundResource(R.mipmap.login_point);
            }
        }

        // 判断是否是最后一页，若是则显示按钮
        if (position == imageList.size() - 1) {
            ib_start.setVisibility(View.VISIBLE);
            tv_version.setVisibility(View.VISIBLE);
            tv_version.setText("V : " + versonNow);
        } else {
            ib_start.setVisibility(View.GONE);
            tv_version.setVisibility(View.GONE);
        }

    }

    public void onPageScrollStateChanged(int state) {

    }

    public void getImgs() {
        String url = ParmasUrl.select_banner;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("welcome===", s);
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if (code.equals("200")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            imageList.add(data.getJSONObject(i).getString("url"));
                        }
                        SharePrefUtil.saveObj(WelcomeActivity.this, "ImgsList", imageList);
                        // 加载ViewPager
                        initViewPager();
                        // 加载底部圆点
                        initPoint();
                    }
                    Toast.makeText(WelcomeActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(WelcomeActivity.this, VolleyErrorHelper.getMessage(volleyError, WelcomeActivity.this), Toast.LENGTH_SHORT).show();
            }
        });
     App.getHttpQueue().add(stringRequest);
    }

    public String getWeek() {
        long time=System.currentTimeMillis();
        Date date=new Date(time);
        SimpleDateFormat format=new SimpleDateFormat("EEEE");
        String format1 = format.format(date);
        return format1;
    }
}