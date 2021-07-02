package com.example.project_4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class StudentLogin_Activity extends AppCompatActivity {


    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference reference2;

    private String userID;

    private ListView lv;

    TextView email_TEXTVIEW;
    TextView username_TEXTVIEW;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        email_TEXTVIEW = findViewById(R.id.student_email_id);
        username_TEXTVIEW = findViewById(R.id.student_username_id);
        getCurrentUser();
        lv = findViewById(R.id.list);

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

    }

    public void getTopic(Map<String, Object> quizInfo){
        //Object questionArrayList;
        for(Map.Entry<String, Object> entry: quizInfo.entrySet()){
            Map singleUser = (Map) entry.getValue();
            System.out.println("This is topic: " + singleUser.get("topic"));
        }
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