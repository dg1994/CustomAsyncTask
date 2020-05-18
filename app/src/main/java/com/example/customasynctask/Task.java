package com.example.customasynctask;

public interface Task<T> {
    T onExecuteTask();
    void onTaskComplete(T value);
}
