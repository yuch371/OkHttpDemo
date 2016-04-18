package demo.cn.okhttp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ImageView mImageView;
    //创建okHttpClient对象， 可以共用一个Client实例
    private final OkHttpClient mClient=new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView= ((ImageView) findViewById(R.id.imageView1));
    }

    public void downPhotoCallBack1(View v) {
        // 先设置图片为默认图片
        mImageView.setImageResource(R.mipmap.ic_launcher);

        // 产生一个Request的Builder的实例
        Request request = new Request.Builder()
                .url("http://www.pingwest.com/wp-content/uploads/2013/04/new_android_wallpaper.jpg")
                .build();

        //为请求产生一个调用Call实例，=>并加入到请求队列中，=>同时为本次请求指定一个调用回调Callback;
        // enqueue()的后台会在线程运行

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(MainActivity.this,
                        "访问" + request.urlString() + "异常:因为" + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                //显示一下服务器回应报文的Headers信息，便于学习
                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    Log.d("responseHeaders", responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                //获取返回数据中图片部分的正文内容
                byte[] data = response.body().bytes();
                final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                //因为OKHTTP的下载都是运行在子线程中的，
                // 而Volley和AsyncTask部分运行在子线程中，部分运行在主线程中,
                //注意显示图片时异同之处
                runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       mImageView.setImageBitmap(bitmap);
                   }
               });

            }
        });
    }

    /***
     * OK的
     */
    public void downPhotoCallBack2(View v) {
        // 先设置图片为默认图片
        mImageView.setImageResource(R.mipmap.ic_launcher);
        // 产生一个Request的Builder的实例
        Request request = new Request.Builder()
                .url("http://www.pingwest.com/wp-content/uploads/2013/04/new_android_wallpaper.jpg")
                .build();
        //为请求产生一个调用Call实例，=>并加入到请求队列中，=>同时为本次请求指定一个调用回调Callback;
        // enqueue()的后台会在线程运行

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(MainActivity.this,
                        "访问" + request.urlString() + "异常:因为" + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                //显示一下服务器回应报文的Headers信息，便于学习
                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    Log.d("responseHeaders", responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                //获取返回数据中图片部分的正文内容
                byte[] data = response.body().bytes();
                final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                //因为OKHTTP的下载都是运行在子线程中的，
                // 而Volley和AsyncTask部分运行在子线程中，部分运行在主线程中,
                //注意显示图片时异同之处
                mImageView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mImageView.setImageBitmap(bitmap);
                                    }
                                }
                );
            }
        });
    }

    public void downPhotoWithOutCallBack(View v) {
        // 先设置图片为默认图片
        mImageView.setImageResource(R.mipmap.ic_launcher);
        // 产生一个Request的Builder的实例

        Request request = new Request.Builder()
                .url("http://www.pingwest.com/wp-content/uploads/2013/04/new_android_wallpaper.jpg")
                .build();

        Response response = null;
        try {
            // execute（0必须在子线程中运行
            response = mClient.newCall(request).execute();
            if (response.isSuccessful()) {
                //显示一下服务器回应报文的Headers信息，便于学习
                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    Log.d("responseHeaders", responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                byte[] data = response.body().bytes();
                //获取返回数据中图片部分的正文内容
                final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                //异常因为非主线程
                mImageView.setImageBitmap(bitmap);
            } else {
                Toast.makeText(MainActivity.this,
                        "图片下载异常:code=" + response.code() + ",message=" + response.message(),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private  void okhttppost(){
        //okhttp默认UTF-8编码，
        //post使用案例
        RequestBody formBody = new FormEncodingBuilder()
                .addEncoded("methodName","methodNameValue")
                .addEncoded("args", "argsValue")
                .addEncoded("cookie", "1")
                .build();


        Request request = new Request.Builder()
                .url("http://www.pingwest.com/wp-content/uploads/2013/04/new_android_wallpaper.jpg")
                .post(formBody)
                .build();
    }
}
