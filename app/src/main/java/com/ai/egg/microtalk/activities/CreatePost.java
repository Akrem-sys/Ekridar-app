package com.ai.egg.microtalk.activities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.ai.egg.microtalk.BackendApp;
import com.ai.egg.microtalk.House;
import com.ai.egg.microtalk.R;
import com.ai.egg.microtalk.Utils;
import com.ai.egg.microtalk.adapters.SlideAdapter;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.google.android.material.navigation.NavigationView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreatePost extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    final int REQUEST_EXTERNAL_STORAGE = 100;
    private DrawerLayout Draw;
    private NavigationView Slide;
    private Toolbar toolbar;
    private View Loading;
    private EditText Price,City,Desc;
    private View LoginForm;
    private Button Post;
    private String[] GeneratedName;
    private TextView Loadingtxt;
    private Spinner Region,In,Rooms;
    public static SliderView Slyd;
    List<String> Regions,Ins;
    private List<Uri> MyUris;
    public static SlideAdapter MySlideAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        //rooms spinner
        Rooms = findViewById(R.id.create_post_room);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.numberofrooms, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Rooms.setAdapter(adapter);

        Post = findViewById(R.id.btnPost);
        Price = findViewById(R.id.create_post_price);
        City = findViewById(R.id.city_edit);
        Desc = findViewById(R.id.create_post_desc);

        MyUris = new ArrayList<>();
        Slyd = findViewById(R.id.create_post_slider);
        MySlideAdapter = new SlideAdapter(this, BackendApp.MyUris);
        Slyd.setSliderAdapter(MySlideAdapter);
        Slyd.setIndicatorAnimation(IndicatorAnimationType.DROP);

        try {
            Regions= Utils.JsonListFromArrayName(BackendApp.REGIONJSON,"Regions");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Spinners widgets
        Region = findViewById(R.id.region_spinner);
        In =  findViewById(R.id.in_spinner);
        //Loading Progress widgets
        Loading = findViewById(R.id.loading_login);
        LoginForm = findViewById(R.id.drawer);
        Loadingtxt = findViewById(R.id.loadingtxt_login);
        Draw = findViewById(R.id.drawer);

        //Toolbar Widgets
        Slide = findViewById(R.id.nav);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Nouvelle annonce");
        Slide.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,Draw,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        Draw.addDrawerListener(toggle);
        toggle.syncState();
        Slide.setNavigationItemSelectedListener(this);

        //Spinners function when item selected
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,Regions );

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        Region.setAdapter(dataAdapter);
        Region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = Region.getSelectedItem().toString();
                if (item.equals("Selectionner")){
                    List<String> Empty = new ArrayList<>();
                    ArrayAdapter<String> Adapt = new ArrayAdapter<String>
                            (CreatePost.this,
                                    android.R.layout.simple_spinner_item,
                                    Empty);

                    Adapt.setDropDownViewResource
                            (android.R.layout.simple_spinner_dropdown_item);
                    In.setAdapter(Adapt);
                }
                else{
                try {
                    Ins= Utils.JsonListFromArrayName(BackendApp.INJSON,item);
                    ArrayAdapter<String> Adapt = new ArrayAdapter<String>
                            (CreatePost.this,
                                    android.R.layout.simple_spinner_item,
                                    Ins);

                    Adapt.setDropDownViewResource
                            (android.R.layout.simple_spinner_dropdown_item);
                    In.setAdapter(Adapt);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }}

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = findViewById(R.id.radioButton4);
                RadioButton radioButton2 = findViewById(R.id.meuble);
                if (BackendApp.bitmaps.size() == 0) {
                    Log.d("SIZE", String.valueOf(BackendApp.bitmaps.size()));
                    Toast.makeText(CreatePost.this, "Selectionner des images", Toast.LENGTH_LONG).show();
                }else if(Desc.getText().toString().trim().isEmpty()){
                    Toast.makeText(CreatePost.this, "Ajouter une description..!", Toast.LENGTH_LONG).show();
                }else if(City.getText().toString().trim().isEmpty()){
                    Toast.makeText(CreatePost.this, "Ajouter ton ville..!", Toast.LENGTH_LONG).show();
                }else if(Price.getText().toString().trim().isEmpty()){
                    Toast.makeText(CreatePost.this, "Ajouter ton prix..!", Toast.LENGTH_LONG).show();
                }else if(Region.getSelectedItem().toString().trim().equals("Selectionner")){
                    Toast.makeText(CreatePost.this, "Selectionner une Gouvernorat", Toast.LENGTH_LONG).show();
                }else if(In.getSelectedItem().toString().trim().equals("Selectionner")){
                    Toast.makeText(CreatePost.this, "Selectionner une Delegation", Toast.LENGTH_LONG).show();
                }
                else {
                    GeneratedName = new String[BackendApp.bitmaps.size()];
                    showProgress(true);
                    for (int i=0;i<BackendApp.bitmaps.size();i++){
                        GeneratedName[i]=GenerateString() + ".png";
                    }
                    for(int i=0;i<BackendApp.bitmaps.size();i++){
                        Backendless.Files.Android.upload(BackendApp.bitmaps.get(i),
                                Bitmap.CompressFormat.JPEG,
                                80,
                                GeneratedName[i],
                                "Images",
                                new AsyncCallback<BackendlessFile>() {
                                    @Override
                                    public void handleResponse(BackendlessFile response) {
                                        if (response.getFileURL().indexOf(GeneratedName[GeneratedName.length-1])!=-1){
                                            Toast.makeText(CreatePost.this,"Annonce a été ajoutée",Toast.LENGTH_LONG).show();
                                            showProgress(false);
                                        }
                                        Log.d("UploadingLogs", "image Succesfully uploaded");
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Log.d("UploadingLogs", fault.getMessage());
                                    }
                                    });
                    }
                    House Items = new House();
                        Items.setAvatarURL(BackendApp.AVATARURL);
                        Items.setOwner(BackendApp.NAME);
                        Items.setImgURL(ImageCollector(GeneratedName));
                        Items.setPhone(BackendApp.PHONE);
                        Items.setRegion(Region.getSelectedItem().toString().trim());
                        Items.setGov(In.getSelectedItem().toString().trim());
                        Items.setCity(City.getText().toString().trim());
                        Items.setPrice(Price.getText().toString().trim());
                        Items.setResidence(radioButton.isChecked());
                        Items.setDescription(Desc.getText().toString().trim());
                        Items.setRoomNumbers(Rooms.getSelectedItem().toString());
                        Items.setMeuble(radioButton2.isChecked());
                        Backendless.Data.of(House.class).save(Items, new AsyncCallback<House>() {
                            @Override
                            public void handleResponse(House response) {
                                Region.setSelection(0);
                                City.setText("");
                                Price.setText("");
                                Desc.setText("");
                                Rooms.setSelection(0);
                                radioButton.setChecked(true);
                                radioButton2.setChecked(true);
                                BackendApp.bitmaps.clear();
                                BackendApp.MyUris.clear();
                                BackendApp.MyUris.add(null);
                                BackendApp.update(CreatePost.this);
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(CreatePost.this,"Error"+fault.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                }
            }
            });
    }
    //Collect all images
    public String ImageCollector(String[] Links){
        String ch="";
        for(String i:Links){
            ch+=i+"+";
        }
        return ch;
    }
    //Generate random string
    public String GenerateString() {

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString;
    }
    // Picking images function
    public void launchGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_EXTERNAL_STORAGE);
    }
    //Permission to get photos
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    launchGalleryIntent();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
    //Collecting photos from user
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EXTERNAL_STORAGE && resultCode == RESULT_OK) {

            final List<Bitmap> bitmaps = new ArrayList<>();
            ClipData clipData = data.getClipData();
            if (BackendApp.MyUris.get(0)==null){
                BackendApp.MyUris.remove(0);
            }
            if (clipData != null) {
                //multiple images selecetd
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    Log.d("URI", imageUri.toString());
                    BackendApp.MyUris.add(imageUri);
                    try {
                        BackendApp.bitmaps.add(MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri));
                        Log.d("BITMAPADDED", String.valueOf(bitmaps.size()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //single image selected
                Uri imageUri = data.getData();
                BackendApp.MyUris.add(imageUri);
                Log.d("URI", imageUri.toString());
                try {
                    BackendApp.bitmaps.add(MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri));
                    Log.d("BITMAPADDED",String.valueOf(bitmaps.size()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }BackendApp.update(CreatePost.this);
        }}
    //When not pressing anything
    @Override
    public void onBackPressed() {
        if(Draw.isDrawerOpen(GravityCompat.START)){
            Draw.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }
    //Navigating the drawer layout
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.item_logout:
                showProgress(true);
                Backendless.UserService.logout(new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void response) {
                        startActivity(new Intent(CreatePost.this, MainActivity.class));
                        showProgress(false);
                        CreatePost.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(CreatePost.this,fault.getMessage(),Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                });
                break;
            case R.id.nav_home:
                startActivity(new Intent(CreatePost.this,Home.class));
                break;
        }
        return true;
    }
    //Progress bar function
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
