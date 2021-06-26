package com.example.project_4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

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
        signup_button = findViewById(R.id.signup_complete);

        signup_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mAuth.createUserWithEmailAndPassword(signup_email.getText().toString(), signup_password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    User user = new User(signup_username.getText().toString(), signup_password.getText().toString());
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(SignUp_Activity.this, "User has been registered Successfully", Toast.LENGTH_LONG).show();
                                            }
                                            else{
                                                Toast.makeText(SignUp_Activity.this, "Failed to register", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                }
                                else{
                                    Toast.makeText(SignUp_Activity.this, "Failed to register", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });


    }
}