package com.testtask.fb;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.testtask.fb.model.Constants;
import com.testtask.fb.model.UserInfo;
import com.testtask.fb.presenter.facebook.FBGetInfo;
import com.testtask.fb.presenter.facebook.FBGetInfoPresenter;
import com.testtask.fb.view.ProfileView;

public class Profile extends AppCompatActivity implements ProfileView, PopupMenu.OnMenuItemClickListener {

    private FBGetInfoPresenter fbGetInfoPresenter;
    public TextView userName;
    public TextView userEmail;
    public TextView userbday;;
    private UserInfo userDataModel;
    private ImageView userAvatar;
    private ImageLoader imageLoader;
    private ImageView photoBackground;
    private UserInfo userModelSingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //user_profile_name
        userName = (TextView)findViewById(R.id.user_profile_name);
        userEmail = (TextView)findViewById(R.id.user_profile_short_bio);
        userbday = (TextView) findViewById(R.id.user_bday);
        userAvatar = (ImageView) findViewById(R.id.user_profile_photo);
        photoBackground = (ImageView) findViewById(R.id.header_cover_image);

        //get FB profile
        userModelSingleton = UserInfo.getInstance();
        if(userModelSingleton.getService() == Constants.FB_SERVICE){
            fbGetInfoPresenter = new FBGetInfo(this);
            fbGetInfoPresenter.getMyProfileRequest();
        }else{

            setInfoToView ( );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.edit_profile:
                Toast.makeText(getBaseContext(), "You selected EditProfile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.out:
                Toast.makeText(getBaseContext(), "You selected Out", Toast.LENGTH_SHORT).show();
                break;
            case R.id.gallery:
                Toast.makeText(getBaseContext(), "You selected Gallery", Toast.LENGTH_SHORT).show();
                break;
            case R.id.exit_app:
                Toast.makeText(getBaseContext(), "You selected Exit", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void setInfoToView ( ) {
        userDataModel = UserInfo.getInstance();
        userName.setText(userDataModel.getUser_name());
        userEmail.setText(userDataModel.getUser_email());
        userbday.setText("Birthday: ");
        userbday.append(userDataModel.getUser_bday());

        imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        imageLoader.init(config.build());

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        imageLoader.displayImage(userDataModel.getAvatarURL(), userAvatar, options);
        imageLoader.displayImage(userDataModel.getAvatarURL(), photoBackground, options);

    }


    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.items, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    @Override
    public void onBackPressed()
    {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userModelSingleton.saveCash(this);
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
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.out:
                userModelSingleton.setLogin_status(Constants.LOGIN_OUT);
                userModelSingleton.saveCash(this);
                LoginManager.getInstance().logOut();
                Intent goToEditSign = new Intent( Profile.this, Main.class);
                startActivity(goToEditSign);
                finish();
                return true;
            case R.id.edit_profile:
                Intent goToEditProfile = new Intent( Profile.this, Edit.class);
                startActivity(goToEditProfile);
                return true;
            case R.id.gallery:
                //showToast(userDataModel.getLogin_status()+"");
                Intent goToGallery = new Intent( Profile.this, Photos.class);
                startActivity(goToGallery);
                return true;
            case R.id.exit_app:
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                return true;
            default:
                return false;
        }}
}
