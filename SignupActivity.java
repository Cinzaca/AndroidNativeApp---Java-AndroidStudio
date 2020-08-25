package com.example.holdmycards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    ImageView art2, emailFieldIconSignup, usernameFieldIconSignup, passwordFieldIconSignup, backButton;
    EditText userEmailFieldSignup, usernameFieldSignup, passwordFieldSignup;
    TextView textCreateAccount;
    View viewSignup;
    Button signupButton;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    Animation fromBottom2, fromRight, fromLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        viewSignup = (View) findViewById(R.id.viewSignup);
        userEmailFieldSignup = (EditText) findViewById(R.id.userEmailFieldSignup);
        emailFieldIconSignup = (ImageView) findViewById(R.id.emailFieldIconSignup);
        usernameFieldSignup = (EditText) findViewById(R.id.usernameFieldSignup);
        usernameFieldIconSignup = (ImageView) findViewById(R.id.usernameFieldIconSignup);
        passwordFieldSignup = (EditText) findViewById(R.id.passwordFieldSignup);
        passwordFieldIconSignup = (ImageView) findViewById(R.id.passwordFieldIconSignup);
        art2 = (ImageView) findViewById(R.id.art2);
        textCreateAccount = (TextView) findViewById(R.id.textcreateaccount);
        signupButton = (Button) findViewById(R.id.signupButton);
        backButton = (ImageView) findViewById(R.id.backButton);
        fAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        fromBottom2 = AnimationUtils.loadAnimation(this, R.anim.frombottom2);
        fromRight = AnimationUtils.loadAnimation(this, R.anim.fromright);
        fromLeft = AnimationUtils.loadAnimation(this, R.anim.fromleft);

        viewSignup.startAnimation(fromBottom2);
        userEmailFieldSignup.startAnimation(fromLeft);
        emailFieldIconSignup.startAnimation(fromRight);
        usernameFieldSignup.startAnimation(fromLeft);
        usernameFieldIconSignup.startAnimation(fromRight);
        passwordFieldSignup.startAnimation(fromLeft);
        passwordFieldIconSignup.startAnimation(fromRight);
        art2.startAnimation(fromBottom2);
        textCreateAccount.startAnimation(fromLeft);
        signupButton.startAnimation(fromRight);
        backButton.startAnimation(fromRight);


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userEmailFieldSignup.getText().toString().trim();
                String username = usernameFieldSignup.getText().toString().trim();
                String password = passwordFieldSignup.getText().toString().trim();

                if(TextUtils.isEmpty(email)) {
                    emailFieldIconSignup.setVisibility(View.INVISIBLE);
                    userEmailFieldSignup.setError("Email is required!");
                    return;
                }
                if(TextUtils.isEmpty(username)) {
                    usernameFieldIconSignup.setVisibility(View.INVISIBLE);
                    usernameFieldSignup.setError("Username is required!");
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    passwordFieldIconSignup.setVisibility(View.INVISIBLE);
                    passwordFieldSignup.setError("Password is required!");
                    return;
                }

                if(password.length() < 6) {
                    passwordFieldIconSignup.setVisibility(View.INVISIBLE);
                    passwordFieldSignup.setError("Password must have at least 6 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Account was created!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else {
                            Toast.makeText(SignupActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToLogin();
                    }
        });
    }

    public void backToLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}

