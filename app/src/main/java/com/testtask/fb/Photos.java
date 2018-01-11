package com.testtask.fb;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;


import com.testtask.fb.gallery.ImageViewAdapter;
import com.testtask.fb.model.Constants;
import com.testtask.fb.model.UserInfo;
import com.testtask.fb.presenter.facebook.FBGetPhotos;
import com.testtask.fb.presenter.facebook.FBGetPhotosPresenter;
import com.testtask.fb.view.GalleryView;

public class Photos extends AppCompatActivity implements GalleryView {

    private UserInfo userModelSingleton;
    private FBGetPhotosPresenter fbGetPhotosPresenter;
    private ImageViewAdapter imageViewAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        userModelSingleton = UserInfo.getInstance();
        viewPager = (ViewPager)findViewById(R.id.viewpager);

        if(userModelSingleton.getPhoto(0).equalsIgnoreCase(Constants.DEFAULT_STRING)){
            fbGetPhotosPresenter = new FBGetPhotos(this);
            fbGetPhotosPresenter.getPhotosRequest();
        }else{
            setImagesToView();
        }
    }

    @Override
    public void notifyDataSetChanged() {
        if (imageViewAdapter!= null)
            imageViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void setImagesToView() {
        if (imageViewAdapter == null){
            imageViewAdapter = new ImageViewAdapter(
                    Photos.this, userModelSingleton.getPhotos());
            viewPager.setAdapter(imageViewAdapter);
        }
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
    public void onBackPressed() {
        super.onBackPressed();
        //Intent goToProfile = new Intent( Photos.this, Profile.class);
        //startActivity(goToProfile);
        //finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userModelSingleton.saveCash(this);
    }
}
