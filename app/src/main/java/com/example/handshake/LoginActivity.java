package com.example.handshake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText LoginEmail;
    Button LoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // When clicking the login, it should check that the email entered is VALID, as well as the PASSWORD and that they have an account already.
        TextView loginBtn = findViewById(R.id.LoginButton);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                validateEmailAddress(LoginEmail);
            }
        });


        // When clicking the don't have an account yet, we should switch to the create account activity.
        TextView btn = findViewById(R.id.goToSignupPage);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    // Create a function to make sure that the Email Address written is VALID
    private boolean validateEmailAddress(EditText LoginEmail) {
        String emailAd = LoginEmail.getText().toString();
        if (!emailAd.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailAd).matches()) {
            return true;
        } else {
            Toast.makeText(this, "Invalid Email Address!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


}