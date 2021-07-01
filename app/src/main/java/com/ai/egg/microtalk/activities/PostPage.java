package com.ai.egg.microtalk.activities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.ai.egg.microtalk.BackendApp;
import com.ai.egg.microtalk.R;
import com.ai.egg.microtalk.Utils;
import com.ai.egg.microtalk.adapters.PostPageSliderAdapter;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.material.navigation.NavigationView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout Draw;
    private NavigationView Slide;
    private Toolbar toolbar;
    private View Loading;
    private View LoginForm;
    private TextView Loadingtxt;
    private ImageView Avatar,Check1,Check2,Check3,Check4;
    private TextView Name,Date,Price,Desc,Adress,Rooms,Phone;
    private SliderView Slyd;
    private PostPageSliderAdapter MySlideAdapter;
    public List<String> MyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_page);
        MyList = new ArrayList<>();
        Check1 = findViewById(R.id.check1);
        Check2 = findViewById(R.id.check2);
        Check3 = findViewById(R.id.check3);
        Check4 = findViewById(R.id.check4);
        Rooms = findViewById(R.id.post_page_rooms);
        Phone = findViewById(R.id.post_page_phone);
        Name = findViewById(R.id.post_page_name);
        Date = findViewById(R.id.post_page_date);
        Price = findViewById(R.id.post_page_price);
        Desc = findViewById(R.id.post_page_description);
        Adress = findViewById(R.id.post_page_adress);
        Avatar = findViewById(R.id.post_page_avatar);
        Loading = findViewById(R.id.loading_login);
        LoginForm = findViewById(R.id.drawer);
        Loadingtxt = findViewById(R.id.loadingtxt_login);
        Draw = findViewById(R.id.drawer);
        Slide = findViewById(R.id.nav);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Events");
        Slide.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,Draw,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        Draw.addDrawerListener(toggle);
        toggle.syncState();
        Slide.setNavigationItemSelectedListener(this);
        Name.setText(BackendApp.Item.getOwner());
        String[] data = Utils.getDate((BackendApp.Item.getDate()));
        Date.setText(data[1]);
        Price.setText(BackendApp.Item.getPrice()+"DT");
        Desc.setText(BackendApp.Item.getDescription());
        Picasso.with(this).load(BackendApp.Item.getAvatarURL()).into(Avatar);
        if (BackendApp.Item.getResidence()) {
            Check2.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_baseline_close_24));
        } else{
            Check1.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_baseline_close_24));
        }
        if (BackendApp.Item.getMeuble()){
            Check4.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_baseline_close_24));
        }
        else{
            Check3.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_baseline_close_24));
        }
        Adress.setText(BackendApp.Item.getRegion()+","+BackendApp.Item.getGov()+" "+BackendApp.Item.getCity());
        Rooms.setText(BackendApp.Item.getRoomNumbers());
        Phone.setText(BackendApp.Item.getPhone());
        String[] Links=BackendApp.Item.getImgURL().split("\\+",0);
        for(String i:Links){
            MyList.add(i);
        }
        Slyd = findViewById(R.id.create_post_slider);
        MySlideAdapter = new PostPageSliderAdapter(this,MyList);
        Slyd.setSliderAdapter(MySlideAdapter);
        Slyd.setIndicatorAnimation(IndicatorAnimationType.DROP);


    }

    @Override
    public void onBackPressed() {
        if(Draw.isDrawerOpen(GravityCompat.START)){
            Draw.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.item_logout:
                showProgress(true);
                Backendless.UserService.logout(new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void response) {
                        startActivity(new Intent(PostPage.this, MainActivity.class));
                        showProgress(false);
                        PostPage.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(PostPage.this,fault.getMessage(),Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                });
                break;
            case R.id.nav_home:
                startActivity(new Intent(PostPage.this,Home.class));
                break;

        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            LoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
            LoginForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    LoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            Loading.setVisibility(show ? View.VISIBLE : View.GONE);
            Loading.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Loading.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
            Loadingtxt.setVisibility(show ? View.VISIBLE : View.GONE);
            Loadingtxt.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Loadingtxt.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            Loading.setVisibility(show ? View.VISIBLE : View.GONE);
            Loadingtxt.setVisibility(show ? View.VISIBLE : View.GONE);
            LoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
