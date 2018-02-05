package com.example.kkkk.helloworld.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.kkkk.helloworld.App;
import com.example.kkkk.helloworld.R;
import com.example.kkkk.helloworld.http.RetrofitHttp;
import com.rengwuxian.materialedittext.MaterialEditText;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;


public class sendnoticeActivity extends BaseAppActivity implements View.OnClickListener {
    @BindView(R.id.back)
    ImageButton back;
    @BindView(R.id.sendnotice)
    TextView sendnotice;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.img3)
    ImageView img3;
    @BindView(R.id.set)
    ImageView set;
    AlertDialog alertDialog;
    Uri tempUri;
    Bitmap mBitmap;
    Bitmap obmp;
    int log = -1;
    ProgressDialog mDialog;
    @BindView(R.id.title)
    MaterialEditText title;
    @BindView(R.id.content)
    MaterialEditText content;

    String title_;
    String content_;
    ArrayList<Uri> list = new ArrayList<>();
    List<File> fileList = new ArrayList<>();
    List<MultipartBody.Part> partList;
    public static final int CHOOSE_PICTURE = 0;
    public static final int TAKE_PICTURE = 1;
    public static final int CROP_SMALL_PRCIURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendnotice);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        mDialog = new ProgressDialog(sendnoticeActivity.this);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage("请稍等");
        mDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        mDialog.setCancelable(true);
    }

    @OnClick({R.id.back, R.id.set, R.id.img1, R.id.img2, R.id.img3, R.id.sendnotice})
    void click(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.set:
                if (img1.getDrawable() == null) {
                    log = 1;
                } else if (img2.getDrawable() == null) {
                    log = 2;
                } else if (img3.getDrawable() == null) {
                    log = 3;
                }
                if (log > 3) {
                    Toast.makeText(this, "最多上传三张照片", Toast.LENGTH_SHORT).show();
                    return;
                }
                showChoosePicDialog();
                break;
            case R.id.img1:
                if (img1.getDrawable() == null) {
                    return;
                }
                showDialog(img1);
                break;
            case R.id.img2:
                if (img2.getDrawable() == null) {
                    return;
                }
                showDialog(img2);
                break;
            case R.id.img3:
                if (img3.getDrawable() == null) {
                    return;
                }
                showDialog(img3);
                break;
            case R.id.sendnotice:
                title_ = title.getText().toString().trim();
                content_ = content.getText().toString().trim();
                loadImage(title_,content_,"1");
                break;
            default:
                break;
        }
    }

    protected void showChoosePicDialog() {
        alertDialog = new AlertDialog.Builder(sendnoticeActivity.this).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_sex);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottom_menu_animation);
        TextView tv_man = (TextView) window.findViewById(R.id.sex1);
        tv_man.setText("相册");
        TextView tv_woman = (TextView) window.findViewById(R.id.sex2);
        tv_woman.setText("拍照");
        TextView tv_cannel = (TextView) window.findViewById(R.id.cannel);
        tv_cannel.setText("取消");
        tv_man.setOnClickListener(this);
        tv_woman.setOnClickListener(this);
        tv_cannel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sex1:
                Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                openAlbumIntent.setType("image/*");
                startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                break;
            case R.id.sex2:
                Intent openCamerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                tempUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp_image.jpg"));
                openCamerIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                startActivityForResult(openCamerIntent, TAKE_PICTURE);
                break;
            case R.id.cannel:
                alertDialog.dismiss();
                break;
        }
    }

    /**
     * 裁剪图片
     */
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("err", "cutImage: ");
        }
        tempUri = uri;
        list.add(uri);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PRCIURE);
    }

    /**
     * 显示图片
     */
    protected void setImageView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            switch (log) {
                case 1:
                    img1.setImageBitmap(mBitmap);
                    break;
                case 2:
                    img2.setImageBitmap(mBitmap);
                    break;
                case 3:
                    img3.setImageBitmap(mBitmap);
                    log = 4;
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == userinfoActivity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    cutImage(tempUri);
                    break;
                case CHOOSE_PICTURE:
                    cutImage(data.getData());
                    break;
                case CROP_SMALL_PRCIURE:
                    setImageView(data);
            }
            alertDialog.dismiss();

        }
        //this.data=data;
    }

    /*回收图片资源*/
    public static void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    private void showDialog(final ImageView img) {
        LemonHello.getWarningHello("您确定要删除图片吗？", "")
                .addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        helloView.hide();
                    }
                }))
                .addAction(new LemonHelloAction("我要删除", Color.RED, new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        helloView.hide();
                        img.setImageDrawable(null);
                        releaseImageViewResouce(img);
                    }
                }))
                .show(sendnoticeActivity.this);
    }

    private void loadImage(String title, String content, String urgency) {
        mDialog.show();
        Map<String, RequestBody> params = new HashMap<>();
        //以下参数是伪代码，参数需要换成自己服务器支持的
        params.put("title", convertToRequestBody(title));
        params.put("content", convertToRequestBody(content));
        params.put("urgency", convertToRequestBody(urgency));

        for (int i = 0; i < list.size(); i++) {
            fileList.add(new File(String.valueOf(list.get(i))));
            //RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), new File(String.valueOf(list.get(i))));
            //file[i] = MultipartBody.Part.createFormData("file", new File(String.valueOf(list.get(i))).getName(), requestFile);
            partList = filesToMultipartBodyParts(fileList);
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RetrofitHttp.getRetrofit(builder.build()).LoadImage(App.getInstance().getMyToken(),params,partList)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response) {
                        mDialog.dismiss();
                        try {
                            String result = response.body().string();
                            JSONObject jsonObject = JSON.parseObject(result);
                            String msg = jsonObject.getString("message");
                            //String data = jsonObject.getString("data");
                            Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        mDialog.dismiss();
                        Toast.makeText(getBaseContext(), "未知错误", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private RequestBody convertToRequestBody(String param) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), param);
        return requestBody;
    }

    private List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("images", file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }


}
