package com.ai.egg.microtalk.activities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Person;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.ai.egg.microtalk.Utils;
import com.ai.egg.microtalk.activities.Home;
import com.ai.egg.microtalk.activities.MainActivity;
import com.ai.egg.microtalk.adapters.PostAdapter;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout Draw;
    private NavigationView Slide;
    private Toolbar toolbar;
    private View Loading;
    private View LoginForm;
    private ImageButton ChAvatar,ChName,ChGender,ChPhone;
    private TextView Loadingtxt;
    private TextView Name,Gender,Phone;
    private ImageView Avatar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    List<House> Person;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //Recycler view
        ChAvatar = findViewById(R.id.profile_avatar_btn);
        ChName = findViewById(R.id.profile_name_btn);
        ChGender = findViewById(R.id.profile_sexe_button);
        ChPhone = findViewById(R.id.profile_phone_btn);
        Name = findViewById(R.id.profile_name);
        Phone = findViewById(R.id.profile_phone);
        Gender = findViewById(R.id.profile_sexe);
        Avatar = findViewById(R.id.profile_avatar);
        Person = new ArrayList<>();
        if (!BackendApp.NAME.equals(BackendApp.OwnerForProfile)){
            ChAvatar.setVisibility(View.GONE);
            ChName.setVisibility(View.GONE);
            ChGender.setVisibility(View.GONE);
            ChPhone.setVisibility(View.GONE);
        }
        Person.addAll(Utils.GetInfo());
        Picasso.with(this).load(Person.get(0).getAvatarURL()).into(Avatar);
        Name.setText(Person.get(0).getOwner());
        Phone.setText(Person.get(0).getPhone());
        Gender.setText(Person.get(0).getGender());
        recyclerView = findViewById(R.id.post_rec);
        recyclerView.setHasFixedSize(true);
        adapter = new PostAdapter(Person,this);
        recyclerView.setAdapter(adapter);


        recyclerView.setLayoutManager(new LinearLayoutManager(Profile.this));
        Loading = findViewById(R.id.loading_login);
        LoginForm = findViewById(R.id.los);
        Loadingtxt = findViewById(R.id.loadingtxt_login);
        Draw = findViewById(R.id.drawer);
        Slide = findViewById(R.id.nav);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        Slide.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,Draw,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        Draw.addDrawerListener(toggle);
        toggle.syncState();
        Slide.setNavigationItemSelectedListener(this);
        ChName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog("Nouveau nom et prenom");
            }

        });
        ChPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog("Nouveau numero de telephone");
            }
        });
        ChGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog("Ton sexe");
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


    void showCustomDialog(String var) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.customdialogue);
        TextView title = dialog.findViewById(R.id.dialogue_title);
        title.setText(var);
        EditText input = dialog.findViewById(R.id.dialogue_input);
        Button btn = dialog.findViewById(R.id.dialogue_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String InputField = input.getText().toString().trim();
                if (InputField.isEmpty()){
                    Toast.makeText(Profile.this,"Saisir ton "+var.toLowerCase(),Toast.LENGTH_LONG).show();
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String owner=Name.getText().toString();
                            Name.setText(InputField);
                            Map<String, Object> changes = new HashMap<>();
                            changes.put("owner",InputField);
                            Backendless.Data.of(House.class).update("ownerId="+Person.get(0).getID(), changes, new AsyncCallback<Integer>() {
                                @Override
                                public void handleResponse(Integer response) {
                                    Log.d("N:Changes",String.valueOf(response));
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Toast.makeText(Profile.this,fault.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                    dialog.dismiss();
                }

            }
        });
        dialog.show();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.item_logout:
                showProgress(true);
                Backendless.UserService.logout(new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void response) {
                        startActivity(new Intent(Profile.this, MainActivity.class));
                        showProgress(false);
                        Profile.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(Profile.this,fault.getMessage(),Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                });
                break;
            case R.id.nav_home:
                startActivity(new Intent(Profile.this, Home.class));
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
