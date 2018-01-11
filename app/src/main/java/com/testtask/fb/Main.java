package com.testtask.fb;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.widget.LoginButton;
import com.testtask.fb.model.Constants;
import com.testtask.fb.model.UserInfo;
import com.testtask.fb.presenter.facebook.FBSignIn;
import com.testtask.fb.presenter.facebook.FBSignInPresenter;
import com.testtask.fb.view.LoginView;

public class Main extends AppCompatActivity implements LoginView {

    private FBSignInPresenter signInFBPresenter;
    private UserInfo userModelSingelton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FB
        signInFBPresenter = new FBSignIn(this);
        signInFBPresenter.initSignInFB();

        userModelSingelton = UserInfo.getInstance();
        userModelSingelton.readCash(this);
        //showToast(UserInfo.getInstance().getLogin_status() + "");
        switch (userModelSingelton.getLogin_status()){
            case Constants.LOGIN_IN:
                startProfileActivity();
                break;
        }
        setContentView(R.layout.activity_main);

        LoginButton buttonFB = (LoginButton) findViewById(R.id.facebook_button);
        buttonFB.setReadPermissions("email", "public_profile","user_birthday", "user_photos");
        // buttonFB.sr
        buttonFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInFBPresenter.logIn();
                //showToast(UserInfo.getInstance().getLogin_status() + "");
            }
        });

    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        signInFBPresenter.onActivityResult( requestCode, resultCode, data);
    }


    @Override
    public void startProfileActivity() {
        Intent goToProfile = new Intent(Main.this, Profile.class);
        startActivity(goToProfile);
        finish();
    }





    @Override
    public Context getContext() {
        return this.getApplicationContext();
    }


    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg ,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed(){
        // moveTaskToBack(true);
        // super.onBackPressed();
        // finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //activityLive = false;
        signInFBPresenter.onDestroy();
    }
}
