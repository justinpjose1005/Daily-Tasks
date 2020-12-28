package com.justinpjose.ptimetableapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordScreen extends AppCompatActivity {
    TextInputEditText forgotScreenEmail;
    ProgressBar forgotPasswordScreenProgressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_screen);
        setTitle("Forgot Password");

        forgotScreenEmail = findViewById(R.id.forgotScreenEmail);
        forgotPasswordScreenProgressBar = findViewById(R.id.forgotPasswordScreenProgressBar);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void sendResetLinkToUserEmail(View view) {
        forgotPasswordScreenProgressBar.setVisibility(View.VISIBLE);
        String email = forgotScreenEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            forgotPasswordScreenProgressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Please enter email id", Toast.LENGTH_LONG).show();
        } else {
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    forgotPasswordScreenProgressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPasswordScreen.this, "Reset link sent to your email", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(ForgotPasswordScreen.this, "Unable to send reset mail", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}