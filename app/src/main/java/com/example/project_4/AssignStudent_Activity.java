package com.example.project_4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import java.util.Map;

public class AssignStudent_Activity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference reference2;
    private DatabaseReference reference3;
    private DatabaseReference reference4;
    private DatabaseReference reference5;
    private String userID;
    private ArrayList<String> all_users = new ArrayList<>();
    private Spinner user_spinner;
    private Spinner quiz_spinner;
    private Button getInfo;
    private TextView setusername_view;
    private TextView setquiz_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_student);
        getCurrentUser();

        getInfo = findViewById(R.id.getinfo_id);
        user_spinner = findViewById(R.id.professor_assign_student);
        quiz_spinner = findViewById(R.id.professor_assign_quiz);
        setusername_view = findViewById(R.id.setUsername_id);
        setquiz_view = findViewById(R.id.setQuiz_id);




        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                collectUsernames((Map<String, Object>) snapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference2 = FirebaseDatabase.getInstance().getReference().child("Quizzes").child("NFz6WjdN1AcLERH06IK8sZ1VguC2");
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                collectQuizzes((Map<String, Object>) snapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getQuiz = setquiz_view.getText().toString();
                String getName = setusername_view.getText().toString();
                Toast.makeText(getApplicationContext(), "Quiz: " + getQuiz + "| User: " + getName, Toast.LENGTH_LONG).show();
                reference3 = FirebaseDatabase.getInstance().getReference().child("Quizzes").child("NFz6WjdN1AcLERH06IK8sZ1VguC2");
                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        collectQuizInfo((Map<String, Object>) snapshot.getValue(), getQuiz, getName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    public void collectQuizInfo(Map<String, Object> quizInfo, String topic, String username){
        Object questionArrayList;
        for(Map.Entry<String, Object> entry: quizInfo.entrySet()){
            Map singleUser = (Map) entry.getValue();
            if(singleUser.get("topic").equals(topic)){
                String subject = singleUser.get("subject").toString();
                questionArrayList = singleUser.get("questionList");
                String timeLimitString = singleUser.get("timeLimit").toString();
                int timeLimit = Integer.parseInt(timeLimitString);
                assignQuiz(topic, subject, timeLimit, questionArrayList, username);
            }
        }
    }


    public void assignQuiz(String topic, String subject, int time, Object questionList, String username){
        System.out.println("assignQuiz");
        System.out.println("This is topic: " + topic);
        System.out.println("This is time: " + time);
        System.out.println("This is questionList" + questionList.toString());
        reference4 = FirebaseDatabase.getInstance().getReference().child("Users");
        reference4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("THIS IS ALL USERS KEYS");
                for(DataSnapshot childsnaphot: snapshot.getChildren()) {
                    String parent = childsnaphot.getKey();
                    String children = childsnaphot.getValue().toString();
                    if (children.contains(username)){
                        StudentQuiz studentQuiz = new StudentQuiz(topic, subject, time, questionList, false);
                        FirebaseDatabase.getInstance().getReference("Users").child(parent).child("Assigned Quizzes")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push()
                                .setValue(studentQuiz).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Assigned Quiz!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Quiz was not assigned!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void collectQuizzes(Map<String, Object> Users){
        ArrayList<String> topics = new ArrayList<>();
        for(Map.Entry<String, Object> entry: Users.entrySet()){
            Map singleUser = (Map) entry.getValue();
            System.out.println(singleUser.get("topic"));
            topics.add((String) singleUser.get("topic"));
        }

        System.out.println("topics: " + topics);

        ArrayAdapter<String> aa = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, topics);
        quiz_spinner.setAdapter(aa);
        quiz_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String currentTopic = topics.get(position);
                Toast.makeText(getApplicationContext(), currentTopic, Toast.LENGTH_SHORT).show();
                setquiz_view.setText(currentTopic);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void collectUsernames(Map<String, Object> Users){
        ArrayList<String> usernames = new ArrayList<>();
        for(Map.Entry<String, Object> entry: Users.entrySet()){
            Map singleUser = (Map) entry.getValue();
            usernames.add((String) singleUser.get("username"));
        }

        System.out.println("usernames: " + usernames);
        this.all_users = usernames;

        ArrayAdapter<String> aa = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, usernames);
        user_spinner.setAdapter(aa);
        user_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String currentUser = usernames.get(position);
                Toast.makeText(getApplicationContext(), currentUser, Toast.LENGTH_SHORT).show();
                setusername_view.setText(currentUser);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AssignStudent_Activity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}