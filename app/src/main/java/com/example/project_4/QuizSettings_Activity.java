package com.example.project_4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    //Quiz quiz;

    private EditText subject;
    private EditText ansChoice1;
    private EditText ansChoice2;
    private EditText ansChoice3;
    private EditText ansChoice4;
    private EditText ansCorrect;
    private EditText topic;
    private EditText question;
    private EditText timer;
    private Button cancel;
    private Button submitQuestion;
    private Button createQuiz;
    private String getQuestionsString;
    private int getQuestions;
    Button generate;

    private ArrayList<Question> questions;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    //ArrayList questions;
    EditText EDIT_TEXT_question_numbers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_settings);
        getCurrentUser();
        questions = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();

        subject = findViewById(R.id.subject);
        ansChoice1 = findViewById(R.id.ansChoice1);
        ansChoice2 = findViewById(R.id.ansChoice2);
        ansChoice3 = findViewById(R.id.ansChoice3);
        ansChoice4 = findViewById(R.id.ansChoice4);
        ansCorrect = findViewById(R.id.ansCorrect);
        question = findViewById(R.id.question_id);
        submitQuestion = findViewById(R.id.submitQuestion);
        createQuiz = findViewById(R.id.createQuiz);
        generate = findViewById(R.id.createQuiz);
        topic = findViewById(R.id.topic_id);
        timer = findViewById(R.id.timer_id);
        cancel = findViewById(R.id.cancel_id);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questions.clear();
                Toast.makeText(getApplicationContext(), "Questions are now cleared",
                        Toast.LENGTH_SHORT).show();


            }
        });

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectSubmit = subject.getText().toString();
                subject.setText(subjectSubmit);
                subject.setEnabled(false);
            }
        });

        submitQuestion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ArrayList<String> answerChoices = new ArrayList<>();
                answerChoices.add(ansChoice1.getText().toString());
                answerChoices.add(ansChoice2.getText().toString());
                answerChoices.add(ansChoice3.getText().toString());
                answerChoices.add(ansChoice4.getText().toString());

                // make sure an answer choice matches the correct answer
                Boolean match = false;
                for (int i = 0; i < answerChoices.size(); i++){
                    if (answerChoices.get(i).compareTo(ansCorrect.getText().toString()) == 0) {
                        match = true;
                    }
                }

                if (match) {
                    questions.add(new Question(question.getText().toString(), answerChoices
                            , ansCorrect.getText().toString()));

                    ansChoice1.getText().clear();
                    ansChoice2.getText().clear();
                    ansChoice3.getText().clear();
                    ansChoice4.getText().clear();
                    ansCorrect.getText().clear();
                    question.getText().clear();

                    Toast.makeText(getApplicationContext(), "question successfully added to quiz",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "an answer choice must match the correct answer",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        createQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countdown = timer.getText().toString();
                databaseReference = firebaseDatabase.getReference(subject.getText().toString());

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Quiz quiz = new Quiz(questions, subject.getText().toString(),  topic.getText().toString(), Integer.parseInt(countdown));
                        FirebaseDatabase.getInstance().getReference("Quizzes")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push()
                                .setValue(quiz).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "quiz added!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed to add quiz", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        //databaseReference.setValue(quiz);
                        //Toast.makeText(getApplicationContext(), "quiz added!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Database error!", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_settings, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.quiz_id:
                Intent quizIntent = new Intent(getApplicationContext(), QuizSettings_Activity.class);
                startActivity(quizIntent);
                break;
            case R.id.assignstudent_id:
                Intent assignIntent = new Intent(getApplicationContext(), AssignStudent_Activity.class);
                startActivity(assignIntent);
                break;
        }
        return true;
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