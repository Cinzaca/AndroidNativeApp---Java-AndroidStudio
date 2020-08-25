package com.example.holdmycards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.media.ImageWriter;
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

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    ImageView art, fArt, welcomeText, niceText, arrow, desk, emailFieldIcon, passwordFieldIcon;
    Animation frombottom, fromrighttomiddle, fromlefttomiddle;
    EditText userEmailField, userPasswordField;
    TextView membertext, signup, loginText;
    View viewLogin;
    Button loginButton;
    ProgressBar progressBarLogin;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        art = (ImageView) findViewById(R.id.art);
        fArt = (ImageView) findViewById(R.id.flippedart);
        welcomeText = (ImageView) findViewById(R.id.welcometext);
        niceText = (ImageView) findViewById(R.id.nicetext);
        arrow = (ImageView) findViewById(R.id.arrow);
        desk = (ImageView) findViewById(R.id.desk);
        membertext = (TextView) findViewById(R.id.membertext);
        signup = (TextView) findViewById(R.id.signup);
        viewLogin = (View) findViewById(R.id.viewLogin);
        userEmailField = (EditText) findViewById(R.id.userEmailField);
        emailFieldIcon = (ImageView) findViewById(R.id.emailFieldIcon);
        userPasswordField = (EditText) findViewById(R.id.userPasswordField);
        passwordFieldIcon = (ImageView) findViewById(R.id.passwordFieldIcon);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginText = (TextView) findViewById(R.id.loginText);
        progressBarLogin = (ProgressBar) findViewById(R.id.progressBar);

        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        fromrighttomiddle = AnimationUtils.loadAnimation(this, R.anim.fromrighttomiddle);
        fromlefttomiddle = AnimationUtils.loadAnimation(this, R.anim.fromlefttomiddle);

        art.animate().translationY(-1290).setDuration(800).setStartDelay(300);
        fArt.animate().translationY(-2200).setDuration(800).setStartDelay(300);
        welcomeText.animate().translationX(-700).alpha(0).setDuration(350).setStartDelay(300);
        niceText.animate().translationX(1400).alpha(0).setDuration(350).setStartDelay(300);
        arrow.animate().translationY(-2200).alpha(0).setDuration(800).setStartDelay(300);
        desk.animate().alpha(0).setDuration(400).setStartDelay(170);
        membertext.startAnimation(frombottom);
        signup.startAnimation(frombottom);
        viewLogin.startAnimation(frombottom);
        userEmailField.startAnimation(fromlefttomiddle);
        emailFieldIcon.startAnimation(fromrighttomiddle);
        userPasswordField.startAnimation(fromrighttomiddle);
        passwordFieldIcon.startAnimation(fromrighttomiddle);
        loginButton.startAnimation(fromlefttomiddle);
        loginText.startAnimation(fromlefttomiddle);
        progressBarLogin.startAnimation(fromlefttomiddle);
        fAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userEmailField.getText().toString().trim();
                String password = userPasswordField.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailFieldIcon.setVisibility(View.INVISIBLE);
                    userEmailField.setError("Email is required!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    passwordFieldIcon.setVisibility(View.INVISIBLE);
                    userPasswordField.setError("Password is required!");
                    return;
                }

                if (password.length() < 6) {
                    passwordFieldIcon.setVisibility(View.INVISIBLE);
                    userPasswordField.setError("Password must have at least 6 characters");
                    return;
                }

                progressBarLogin.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                         if(task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            openBankActivity();
                        }else {
                            Toast.makeText(MainActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBarLogin.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignupActivity();
            }
        });

    }
    public void openBankActivity() {
        Intent intent = new Intent(this, BankActivity.class);
        startActivity(intent);
    }

    public void openSignupActivity() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
