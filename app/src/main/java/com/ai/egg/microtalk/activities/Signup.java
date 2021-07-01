package com.ai.egg.microtalk.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ai.egg.microtalk.BackendApp;
import com.ai.egg.microtalk.R;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class Signup extends AppCompatActivity {

    private View Loading;
    private View LoginForm;
    private TextView Loadingtxt;
    private Button Signup;
    private EditText Fullname,Email,Phone,Password,Password1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        Signup = findViewById(R.id.login_btn);
        LoginForm = findViewById(R.id.signup_form);
        Loading = findViewById(R.id.loading);
        Loadingtxt = findViewById(R.id.loadingtxt);
        Fullname = findViewById(R.id.login_email);
        Email = findViewById(R.id.Email);
        Phone = findViewById(R.id.Phone);
        Password = findViewById(R.id.login_password);
        Password1 = findViewById(R.id.Password1);


        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ListChoice, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Fullname.getText().toString().isEmpty()){
                    Toast.makeText(Signup.this,"Fill down your full name",Toast.LENGTH_SHORT).show();
                }
                else if (Email.getText().toString().isEmpty() || !Email.getText().toString().contains("@")){
                    Toast.makeText(Signup.this,"Fill down the email adress field",Toast.LENGTH_SHORT).show();
                }
                else if (Phone.getText().toString().isEmpty()){
                    Toast.makeText(Signup.this,"Fill down the phone number",Toast.LENGTH_SHORT).show();
                }
                else if (Password.getText().toString().isEmpty()){
                    Toast.makeText(Signup.this,"Fill down the password field",Toast.LENGTH_SHORT).show();
                }
                else if (Password1.getText().toString().isEmpty()){
                    Toast.makeText(Signup.this,"Fill down the repeat password field",Toast.LENGTH_SHORT).show();
                }
                else{
                    if (Password.getText().toString().equals(Password1.getText().toString())){
                        showProgress(true);
                        BackendlessUser user = new BackendlessUser();
                        user.setEmail(Email.getText().toString().trim());
                        user.setPassword(Password.getText().toString().trim());
                        user.setProperty("sexe",spinner.getSelectedItem().toString().trim());
                        user.setProperty("phone_number",Phone.getText().toString().trim());
                        user.setProperty("name",Fullname.getText().toString().trim());
                        if (spinner.getSelectedItem().toString().trim().equals("Femme")){
                            user.setProperty("avatarurl", BackendApp.DEFAULTF);
                        }
                        else{
                            user.setProperty("avatarurl",BackendApp.DEFAULTM);
                        }

                        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(BackendlessUser response) {
                                showProgress(false);
                                Toast.makeText(Signup.this,"Account added successfuly",Toast.LENGTH_SHORT).show();
                                Signup.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                showProgress(false);
                                String Error=fault.getMessage();
                                Toast.makeText(Signup.this, fault.getDetail(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else{
                        Toast.makeText(Signup.this,"Password fields should be identical",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
