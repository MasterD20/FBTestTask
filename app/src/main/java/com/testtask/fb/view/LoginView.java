package com.testtask.fb.view;

import android.content.Context;

public interface LoginView {
    void startProfileActivity();
    Context getContext();
    void showToast(String msg);
}
