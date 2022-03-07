package com.example.mailapp.util;

public interface OnAsyncEventListener {
    void onSuccess();
    void onFailure(Exception e);
}
