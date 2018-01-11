package com.testtask.fb.presenter.facebook;

import android.content.Intent;

public interface FBSignInPresenter {
    void initSignInFB();
    void onActivityResult (int requestCode, int resultCode, Intent data);
    void onDestroy();
    void logIn();
    void logIn (FBSignIn.GO go);
}
