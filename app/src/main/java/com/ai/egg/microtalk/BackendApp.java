package com.ai.egg.microtalk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.ai.egg.microtalk.activities.CreatePost;
import com.backendless.Backendless;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;

import java.util.ArrayList;
import java.util.List;


public class BackendApp extends Application
{
    public static final String URL = "https://eu-api.backendless.com/2FE78A52-4611-4F6B-8B76-6E8D3FFCA90B/D2974892-B721-4919-BF62-8ACA3775D547/data/House";
    public static final String APPLICATION_ID = "2FE78A52-4611-4F6B-8B76-6E8D3FFCA90B";
    public static final String URLFORIMAGE="https://eu.backendlessappcontent.com/2FE78A52-4611-4F6B-8B76-6E8D3FFCA90B/console/sgljtwoftpveslzfuqihphpvdeeiecupmhzc/files/view/Images/";
    public static final String API_KEY = "AF9EC4FB-3D4C-4700-9334-DA191499FFD9";
    public static final String SERVER_URL = "https://eu-api.backendless.com";
    public static final String DEFAULTF = "https://i.imgur.com/0dsm4Q6.jpg";
    public static final String DEFAULTM= "https://i.imgur.com/ZAQnoNQ.jpg";
    public static String NAME ="";
    public static String AVATARURL ="";
    public static String PHONE="";
    public static String IMAGEURL="";
    public static String DESCRIPTION;
    public static String REGIONJSON="";
    public static String INJSON="";
    public static List<House> Profile;
    public static List<Uri> MyUris;
    public static List<Bitmap> bitmaps;
    public static String OwnerForProfile="";
    public static House Item;
    @Override
    public void onCreate() {
        super.onCreate();
        Profile = new ArrayList<>();
        bitmaps = new ArrayList<>();
        MyUris = new ArrayList<>();
        MyUris.add(null);
        REGIONJSON=Utils.getJsonFromAssets(getApplicationContext(),"regions.json");
        INJSON= Utils.getJsonFromAssets(getApplicationContext(),"hello.json");
        Backendless.setUrl(SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );
    }
    public static void update(Context context){
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CreatePost.Slyd.setSliderAdapter(CreatePost.MySlideAdapter);
                CreatePost.Slyd.setIndicatorAnimation(IndicatorAnimationType.DROP);
            }
        });
    }
}
