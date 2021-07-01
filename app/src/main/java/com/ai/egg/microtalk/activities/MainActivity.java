package com.ai.egg.microtalk.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ai.egg.microtalk.BackendApp;
import com.ai.egg.microtalk.R;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserIdStorageFactory;

public class MainActivity extends AppCompatActivity {

    private View Loading;
    private View LoginForm;
    private TextView Loadingtxt;
    private TextView Signup,Reset;
    private CheckBox Box;
    private Boolean StayLogged = false;
    private Button Loginbtn;
    private EditText Email,Password;
    private final String MSG = "Vous n\'avez pas de compte? S\'inscrire";
    private final String MSG2 ="Mot de passe oublié ? Reinitlialiser";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Loading = findViewById(R.id.loading_login);
        Box = findViewById(R.id.stay_logged);
        LoginForm = findViewById(R.id.login_form);
        Loadingtxt = findViewById(R.id.loadingtxt_login);
        Email=findViewById(R.id.login_email);
        Password=findViewById(R.id.login_password);
        Loginbtn=findViewById(R.id.login_btn);
        Signup=findViewById(R.id.login_signupview);
        Reset=findViewById(R.id.login_resetview);

        SpannableString Link = new SpannableString(MSG);
        ClickableSpan FirstClick = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(MainActivity.this,Signup.class));
            }
        };
        Link.setSpan(FirstClick,27,37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Signup.setText(Link);
        Signup.setMovementMethod(LinkMovementMethod.getInstance());
        //Reset
        SpannableString ResetLink = new SpannableString(MSG2);
        ClickableSpan FirstClickLink = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //Develop this later to reset password
            }
        };
        ResetLink.setSpan(FirstClickLink,22,36, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Reset.setText(ResetLink);
        Reset.setMovementMethod(LinkMovementMethod.getInstance());
        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Email.getText().toString().trim().isEmpty()){
                    Toast.makeText(MainActivity.this,"Taper ton adresse email",Toast.LENGTH_LONG).show();
                }
                else if(Password.getText().toString().trim().isEmpty()){
                    Toast.makeText(MainActivity.this,"Taper ton Mot de passe",Toast.LENGTH_LONG).show();
                }
                else{
                    if (Box.isChecked()){
                        StayLogged=true;
                    }
                    showProgress(true);
                    Backendless.UserService.login(Email.getText().toString().trim(), Password.getText().toString(), new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            Toast.makeText(MainActivity.this, "Connexion réussie",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this,Home.class));
                            getUsername();
                            showProgress(false);
                            MainActivity.this.finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                                Toast.makeText(MainActivity.this,"Error:"+fault.getMessage(),Toast.LENGTH_LONG).show();
                                showProgress(false);
                        }
                    },StayLogged);
                }
            }
        });
        //if the user already connected and remembered
        showProgress(true);
        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean response) {
                if(response){
                    String ID = UserIdStorageFactory.instance().getStorage().get();
                    Backendless.Data.of(BackendlessUser.class).findById(ID, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            BackendApp.NAME=response.getProperty("name").toString();
                            BackendApp.AVATARURL=response.getProperty("avatarurl").toString();
                            BackendApp.PHONE=response.getProperty("phone_number").toString();
                            startActivity(new Intent(MainActivity.this,Home.class));
                            MainActivity.this.finish();
                           showProgress(false);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(MainActivity.this,"Error"+fault.getMessage(),Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });
                }
                else{
                    showProgress(false);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(MainActivity.this,"Error"+fault.getMessage(),Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });
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
    private void getUsername(){
        String ID = UserIdStorageFactory.instance().getStorage().get();
        Backendless.Data.of(BackendlessUser.class).findById(ID, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                BackendApp.NAME=response.getProperty("name").toString();
                BackendApp.AVATARURL=response.getProperty("avatarurl").toString();
                BackendApp.PHONE=response.getProperty("phone_number").toString();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d("Username Fetch Error",fault.getMessage());
            }
        });
    }
}