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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
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
import java.util.Map;

public class StudentLogin_Activity extends AppCompatActivity {


    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference reference2;

    Spinner choose_quiz;

    private String userID;


    TextView email_TEXTVIEW;
    TextView username_TEXTVIEW;
    TextView topic_selected;
    Button select_quiz;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        email_TEXTVIEW = findViewById(R.id.student_email_id);
        username_TEXTVIEW = findViewById(R.id.student_username_id);
        choose_quiz = findViewById(R.id.choose_quiz_id);
        topic_selected = findViewById(R.id.topic_selected_id);
        select_quiz = findViewById(R.id.take_quiz_id);
        getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    String parent = snapshot1.getKey();
                    String children  = snapshot1.getValue().toString();
                    System.out.println("This is parent: " + parent);
                    if(children.contains(username_TEXTVIEW.getText().toString())) {
                        reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(parent).child("Assigned Quizzes");
                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                                    String parent2 = snapshot2.getKey();
                                    getTopic((Map<String, Object>) snapshot2.getValue());
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        select_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You will be taking: " +
                        topic_selected.getText().toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), Activity_Student_Quiz.class);
                intent.putExtra("topic_data", topic_selected.getText().toString());
                startActivity(intent);


            }
        });

    }

    public void getTopic(Map<String, Object> quizInfo){
        //Object questionArrayList;
        ArrayList<String> allTopics = new ArrayList<>();
        for(Map.Entry<String, Object> entry: quizInfo.entrySet()){
            Map singleUser = (Map) entry.getValue();
            allTopics.add(singleUser.get("topic").toString());
            //System.out.println("This is topic: " + singleUser.get("topic"));
        }

        ArrayAdapter<String> aa = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, allTopics);
        choose_quiz.setAdapter(aa);
        choose_quiz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String currentTopic = allTopics.get(position);
                topic_selected.setText(currentTopic);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_settings, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        return true;
    }




    public void getCurrentUser(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String username = userProfile.username;
                    String email = userProfile.email;
                    email_TEXTVIEW.setText(email);
                    username_TEXTVIEW.setText(username);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentLogin_Activity.this, "Somethign went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}