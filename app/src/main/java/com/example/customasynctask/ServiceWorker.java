package com.example.customasynctask;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class ServiceWorker {

    private static final String TAG = "ServiceWorker";

    private String name;
    private ExecutorService mExecutorService;
    private Callable<Bitmap> mCallable;
    private FutureTask<Bitmap> mFutureTask;
    private MyHandler myHandler;

    public ServiceWorker(String name) {
        this.name = name;
        mExecutorService = Executors.newSingleThreadExecutor();
        myHandler = new MyHandler(Looper.getMainLooper());
    }

    public void addTask(final Task<Bitmap> task) {

        mCallable = new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception {
                return task.onExecuteTask();
            }
        };
        mFutureTask = new FutureTask<Bitmap>(mCallable) {
            @Override
            public void done() {
                Log.d(TAG, "future done : " + Thread.currentThread().getName());

                myHandler.post(() -> {
                    try {
                        task.onTaskComplete(get());
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        };
        mExecutorService.execute(mFutureTask);
    }

    private static class MyHandler extends Handler {
        MyHandler(Looper looper) {
            super(looper);
        }
    }
}
