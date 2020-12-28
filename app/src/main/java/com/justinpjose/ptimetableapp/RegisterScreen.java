package com.justinpjose.ptimetableapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterScreen extends AppCompatActivity {
    TextInputEditText registerScreenEmail, registerScreenPassword;
    ProgressBar registerScreenProgressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        setTitle("Register");

        registerScreenEmail = findViewById(R.id.registerScreenEmail);
        registerScreenPassword = findViewById(R.id.registerScreenPassword);
        registerScreenProgressBar = findViewById(R.id.registerScreenProgressBar);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void registerTheUserToFirebase(View view) {
        registerScreenProgressBar.setVisibility(View.VISIBLE);
        String email = registerScreenEmail.getText().toString();
        String password = registerScreenPassword.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            registerScreenProgressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show();
        }
        else{
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    registerScreenProgressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterScreen.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterScreen.this, MainScreen.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(RegisterScreen.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}