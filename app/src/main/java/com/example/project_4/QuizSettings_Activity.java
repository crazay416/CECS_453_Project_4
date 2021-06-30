package com.example.project_4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuizSettings_Activity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private EditText question_numbers;
    private String getQuestionsString;
    private int getQuestions;
    Button generate;

    ArrayList questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_settings);
        getCurrentUser();


       /*
        question_numbers = findViewById(R.id.number_of_questions);
        generate = findViewById(R.id.generate_questions);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQuestionsString = question_numbers.getText().toString();
                getQuestions =  Integer.parseInt(getQuestionsString);
                System.out.println("Number of questions: " + getQuestions);
                questions = new ArrayList(getQuestions);
                ListAdapter adapter = SimpleAdapter(QuizSettings_Activity.this, questions,
                        R.layout.quiz_question, )
            }
        });
        */

    }


    public void getCurrentUser(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView email_TEXTVIEW = findViewById(R.id.professor_email_id);
        final TextView username_TEXTVIEW = findViewById(R.id.professor_username_id);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String username = userProfile.username;
                    String email = userProfile.email;
                    System.out.println("username: " + username + "| email: " + email);
                    //email_TEXTVIEW.setText(email);
                    //username_TEXTVIEW.setText(username);;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuizSettings_Activity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}