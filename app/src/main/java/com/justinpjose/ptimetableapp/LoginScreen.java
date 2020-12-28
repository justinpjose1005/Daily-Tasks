package com.justinpjose.ptimetableapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginScreen extends AppCompatActivity {
    TextInputEditText loginScreenEmail, loginScreenPassword;
    FirebaseAuth firebaseAuth;
    ProgressBar loginScreenProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        setTitle("Login");

        loginScreenEmail = findViewById(R.id.loginScreenEmail);
        loginScreenPassword = findViewById(R.id.loginScreenPassword);
        loginScreenProgressBar = findViewById(R.id.loginScreenProgressBar);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void callsRegisterScreen(View view) {
        loginScreenProgressBar.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loginScreenProgressBar.setVisibility(View.GONE);
                Intent intent = new Intent(LoginScreen.this, RegisterScreen.class);
                startActivity(intent);
            }
        }, 2700);
    }

    public void loginTheUserToTheApp(View view) {
        loginScreenProgressBar.setVisibility(View.VISIBLE);
        String email = loginScreenEmail.getText().toString();
        String password = loginScreenPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            loginScreenProgressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    loginScreenProgressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginScreen.this, "Successfully Logged In", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginScreen.this, MainScreen.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginScreen.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void callsForgotPasswordScreen(View view) {
        Intent intent = new Intent(this, ForgotPasswordScreen.class);
        startActivity(intent);
    }
}