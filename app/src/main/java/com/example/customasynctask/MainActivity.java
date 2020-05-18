package com.example.customasynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private AppCompatImageView imageView1;
    private AppCompatButton button1;
    private AppCompatImageView imageView2;
    private AppCompatButton button2;

    private ServiceWorker serviceWorker1 = new ServiceWorker("service_worker_1");
    private ServiceWorker serviceWorker2 = new ServiceWorker("service_worker_2");

    public static final String IMAGE_1 = "https://static.independent.co.uk/s3fs-public/thumbnails/image/2020/04/01/14/gettyimages-1188056147.jpg";
    public static final String IMAGE_2 = "https://maps.gstatic.com/tactile/basepage/pegman_sherlock.png";

    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        okHttpClient = new OkHttpClient();
    }

    private void initViews() {
        imageView1 = findViewById(R.id.image1);
        button1 = findViewById(R.id.button1);
        imageView2 = findViewById(R.id.image2);
        button2 = findViewById(R.id.button2);

        button1.setOnClickListener(v -> {
            fetchImage1AndSet();
        });

        button2.setOnClickListener(v -> {
            fetchImage2AndSet();
        });
    }

    private void fetchImage1AndSet() {
        serviceWorker1.addTask(new Task<Bitmap>() {
            @Override
            public Bitmap onExecuteTask() {
                //Fetching image1 through okhttp
                Log.d(TAG, "service worker 1 : " + Thread.currentThread().getName());
                Request request = new Request.Builder().url(IMAGE_1).build();
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                return bitmap;
            }
            @Override
            public void onTaskComplete(Bitmap result) {
                //Set bitmap to image 1
                Log.d(TAG, "service worker 1 : " + Thread.currentThread().getName());
                imageView1.setImageBitmap(result);
            }
        });
    }

    private void fetchImage2AndSet() {
        serviceWorker2.addTask(new Task<Bitmap>() {
            @Override
            public Bitmap onExecuteTask() {
                //Fetching image2 through okhttp
                Log.d(TAG, "service worker 2 : " + Thread.currentThread().getName());
                Request request = new Request.Builder().url(IMAGE_2).build();
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                return bitmap;
            }

            @Override
            public void onTaskComplete(Bitmap result) {
                //Set bitmap to image 2
                Log.d(TAG, "service worker 2 : " + Thread.currentThread().getName());
                imageView2.setImageBitmap(result);
            }
        });
    }
}
