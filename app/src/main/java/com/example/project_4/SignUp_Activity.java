package com.example.project_4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class SignUp_Activity extends AppCompatActivity {

    private TextView signup_username, signup_password, signup_password2, signup_email;
    Button signup_button;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        signup_username = findViewById(R.id.signup_username);
        signup_password = findViewById(R.id.signup_password);
        signup_password2 = findViewById(R.id.signup_password2);
        signup_email = findViewById(R.id.signup_email);
        signup_button = findViewById(R.id.signup_button);

        signup_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });


    }
}