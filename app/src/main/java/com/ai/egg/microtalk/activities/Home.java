package com.ai.egg.microtalk.activities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.egg.microtalk.BackendApp;
import com.ai.egg.microtalk.House;
import com.ai.egg.microtalk.R;
import com.ai.egg.microtalk.adapters.PostAdapter;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout Draw;
    private NavigationView Slide;
    private Toolbar toolbar;
    private View Loading;
    private View LoginForm;
    private TextView Loadingtxt;
    private RecyclerView recyclerView;
    private  RecyclerView.Adapter adapter;
    private List<House> Home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Recyclerview
        Home = new ArrayList<>();
        recyclerView = findViewById(R.id.post_rec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Home.this));
        String whereClause = "Number = 1";
        DataQueryBuilder Build = DataQueryBuilder.create();
        Build.setWhereClause(whereClause);
        Loading = findViewById(R.id.loading_login);
        LoginForm = findViewById(R.id.drawer);
        Loadingtxt = findViewById(R.id.loadingtxt_login);
        Draw = findViewById(R.id.drawer);
        Slide = findViewById(R.id.nav);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        Slide.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,Draw,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        Draw.addDrawerListener(toggle);
        toggle.syncState();
        Slide.setNavigationItemSelectedListener(this);
        adapter = new PostAdapter(Home,Home.this);
        GetRecyclerviewData();

    }

    private void GetRecyclerviewData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BackendApp.URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    try {
                        Log.d("MYJSON",myResponse);
                        JSONObject  jsonobject = new JSONObject("{\"data\":"+myResponse+"}");
                        JSONArray array = jsonobject.getJSONArray("data");
                        for(int i=0; i<array.length();i++){
                            JSONObject o = array.getJSONObject(i);
                            House Item = new House();
                            Item.setOwner(o.getString("owner"));
                            Item.setImgURL(o.getString("imgURL"));
                            Item.setAvatarURL(o.getString("avatarURL"));
                            Item.setDescription(o.getString("description"));
                            Item.setCity(o.getString("city"));
                            Item.setGov(o.getString("gov"));
                            Item.setRegion(o.getString("region"));
                            Item.setPrice(o.getString("price"));
                            Item.setPhone(o.getString("phone"));
                            Item.setRoomNumbers(o.getString("roomNumbers"));
                            Item.setResidence(o.getBoolean("residence"));
                            Item.setMeuble(o.getBoolean("meuble"));
                            Item.setDate(o.getLong("created"));
                            Item.setGender(o.getString("gender"));
                            Item.setID(o.getString("ownerId"));
                            Home.add(Item);
                        }
                        BackendApp.Profile.clear();
                        BackendApp.Profile.addAll(Home);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setAdapter(adapter);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
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
                        startActivity(new Intent(Home.this, MainActivity.class));
                        showProgress(false);
                        Home.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(Home.this,fault.getMessage(),Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                });
                break;

            case R.id.nav_home:
                Draw.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_create_post:
                startActivity(new Intent(this,CreatePost.class));
                break;
            case R.id.nav_profile:
                BackendApp.OwnerForProfile=BackendApp.NAME;
                startActivity(new Intent(this,Profile.class));
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
