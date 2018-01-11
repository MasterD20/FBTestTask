package com.testtask.fb.presenter.facebook;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.testtask.fb.model.UserInfo;
import com.testtask.fb.view.GalleryView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FBGetPhotos implements FBGetPhotosPresenter {

    private GalleryView galleryView;
    private ArrayList<String> Photo_list_id = new ArrayList<String>();

    public FBGetPhotos(GalleryView galleryView){
        this.galleryView = galleryView;
    }

    @Override
    public  void getPhotosRequest(){
        /*make API call*/
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),  //your fb AccessToken
                "/" + AccessToken.getCurrentAccessToken().getUserId() + "/photos",//user id of login use      ralbums
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d("TAG", "Facebook Albums: " + response.toString());
                        try {
                            if (response.getError() == null) {
                                JSONObject joMain = response.getJSONObject(); //convert GraphResponse response to JSONObject
                                if (joMain.has("data")) {
                                    JSONArray jaData = joMain.optJSONArray("data"); //find JSONArray from JSONObject
                                    // alFBAlbum = new ArrayList<>();
                                    for (int i = 0; i < jaData.length(); i++) {//find no. of album using jaData.length()
                                        JSONObject joAlbum = jaData.getJSONObject(i); //convert perticular album into JSONObject
                                        GetFacebookImages(joAlbum.optString("id")); //find Album ID and get All Images from album
                                        //GetFacebookImages(joAlbum.getString("id"));
                                    }
                                }
                            } else {
                                Log.d("Test", response.getError().toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();}

    public void GetFacebookImages(final String albumId) {
        //  String url = "https://graph.facebook.com/" + "me" + "/"+albumId+"/photos?access_token=" + AccessToken.getCurrentAccessToken() + "&fields=images";
        Bundle parameters = new Bundle();
        parameters.putString("fields", "images");
        parameters.putString("limit", "100");

        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + albumId + "/photos",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {

                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        Log.v("TAG", "Facebook Photos response: " + response);

                        try {
                            if (response.getError() == null) {
                                JSONObject joMain = response.getJSONObject();
                                JSONObject joAlbum = new JSONObject();

                                if (joMain.has("data")) {
                                    JSONArray jaData = joMain.optJSONArray("data");

                                    for (int a = 0; a < jaData.length(); a++){//Get no. of images {
                                        joAlbum = jaData.getJSONObject(a);
                                        JSONArray jaImages=joAlbum.getJSONArray("images"); //get images Array in JSONArray format

                                        String tempSourceImage = jaImages.getJSONObject(0).getString("source");

                                        Photo_list_id.add(tempSourceImage);
                                        galleryView.notifyDataSetChanged();
                                    }
                                    UserInfo userModelSingleton = UserInfo.getInstance();
                                    userModelSingleton.setPhotos(Photo_list_id);
                                    galleryView.setImagesToView();
                                }


                            } else {
                                Log.v("TAG", response.getError().toString());
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                }).executeAsync();
    }

    @Override
    public void onDestroy() {
        galleryView = null;
    }
}
