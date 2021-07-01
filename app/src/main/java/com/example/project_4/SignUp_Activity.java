package com.example.project_4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp_Activity extends AppCompatActivity {

    private EditText EDIT_TEXT_signup_username, EDIT_TEXT_signup_password, EDIT_TEXT_signup_password2,
            EDIT_TEXT_signup_email;
    Button EDIT_TEXT_signup_button;
    private FirebaseAuth mAuth;
    private boolean check;

    private String signup_username, signup_password, signup_password2, signup_email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        EDIT_TEXT_signup_username = findViewById(R.id.signup_username);
        EDIT_TEXT_signup_password = findViewById(R.id.signup_password);
        EDIT_TEXT_signup_password2 = findViewById(R.id.signup_password2);
        EDIT_TEXT_signup_email = findViewById(R.id.signup_email);
        EDIT_TEXT_signup_button = findViewById(R.id.signup_complete);





        EDIT_TEXT_signup_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                check = checkCredentials(EDIT_TEXT_signup_username, EDIT_TEXT_signup_password,
                        EDIT_TEXT_signup_password2, EDIT_TEXT_signup_email);
                if(check == true) {
                    mAuth.createUserWithEmailAndPassword(EDIT_TEXT_signup_email.getText().toString(), EDIT_TEXT_signup_password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        User user = new User(EDIT_TEXT_signup_username.getText().toString(), EDIT_TEXT_signup_email.getText().toString());
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SignUp_Activity.this, "User has been registered Successfully", Toast.LENGTH_LONG).show();
                                                    finish();
                                                } else {
                                                    Toast.makeText(SignUp_Activity.this, "Failed to register", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                                    } else {
                                        Toast.makeText(SignUp_Activity.this, "Failed to register", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

            }
        });


    }


    private boolean checkCredentials(EditText E_signup_username, EditText E_signup_password,
                                     EditText E_signup_password2, EditText E_signup_email) {
        signup_username = EDIT_TEXT_signup_username.getText().toString();
        signup_password = EDIT_TEXT_signup_password.getText().toString();
        signup_password2 = EDIT_TEXT_signup_password2.getText().toString();
        signup_email = EDIT_TEXT_signup_email.getText().toString();

        System.out.println("signup password: " + signup_password);
        System.out.println("signup password2: " + signup_password2);

        if(signup_password.length() < 5){
            Toast.makeText(getApplicationContext(), "Password is too short", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!signup_password.equals(signup_password2)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}