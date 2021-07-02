package com.example.project_4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.CountDownTimer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Activity_Student_Quiz extends AppCompatActivity {

    private Button next;
    private TextView subject;
    private TextView topic;
    private TextView timerView;
    private TextView question;
    private RadioGroup radioGroup;
    private RadioButton radioA;
    private RadioButton radioB;
    private RadioButton radioC;
    private RadioButton radioD;
    private DatabaseReference reference;
    private DatabaseReference reference2;
    private DatabaseReference reference3;
    private DatabaseReference reference4;
    private DatabaseReference reference5;
    private DatabaseReference reference6;
    private String username;
    private String topicStr;
    private CountDownTimer cdt;
    private String allQuizzes;
    private String eachQuiz;
    private String completed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_quiz);

        next = findViewById(R.id.btnNextId);
        this.subject = findViewById(R.id.subjectQuizId);
        this.topic = findViewById(R.id.topicQuizId);
        this.timerView = findViewById(R.id.timerQuizId);
        question = findViewById(R.id.questionQuizId);
        radioGroup = findViewById(R.id.radioGroup);
        radioA = findViewById(R.id.radioA);
        radioB = findViewById(R.id.radioB);
        radioC = findViewById(R.id.radioC);
        radioD = findViewById(R.id.radioD);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        topicStr = intent.getStringExtra("topic_data");

        question.setText("When did World War II ended?");
        radioA.setText("1945");
        radioB.setText("1946");
        radioC.setText("1944");
        radioD.setText("1943");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    String parents = snapshot1.getKey();
                    System.out.println("This is first reference: " + parents);
                    String children = snapshot1.getValue().toString();
                    if (children.contains(username)){
                        reference2 = FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(parents).child("Assigned Quizzes");

                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                for(DataSnapshot snapshot2: snapshot.getChildren()) {
                                    allQuizzes = snapshot2.getKey();
                                    System.out.println("This is nxtlevel: " + allQuizzes);
                                    reference3 = FirebaseDatabase.getInstance().getReference()
                                            .child("Users").child(parents).child("Assigned Quizzes")
                                            .child(allQuizzes);
                                    for(DataSnapshot snapshot3: snapshot.getChildren()){
                                        collectQuizInfo((Map<String, Object>) snapshot3.getValue(), topicStr);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                        //System.out.println(username);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fill
            }
        });
    }

    public void collectQuizInfo(Map<String, Object> quizInfo, String topic){
        Object questionArrayList;
        for(Map.Entry<String, Object> entry: quizInfo.entrySet()){
            Map singleUser = (Map) entry.getValue();
            if(singleUser.get("topic").equals(topic)){
                String subject = singleUser.get("subject").toString();
                questionArrayList = singleUser.get("questionList");
                String timeLimitString = singleUser.get("time").toString();
                String check  = singleUser.get("completed").toString();
                int timeLimit = Integer.parseInt(timeLimitString);
                this.subject.setText(subject);
                this.topic.setText(topic);
                this.timerView.setText(timeLimitString);
                System.out.println("This is entry.getValue: " + entry.getValue());


                System.out.println("This is subject: " + subject);
                System.out.println("This is questionArrayList: " + questionArrayList);
                System.out.println("This is timeLimit: " + timeLimit);
                System.out.println("This is check: " + check);

            }
        }
    }

    public static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[])obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>)obj);
        }
        System.out.println("THIS IS LIST: " + list);
        for(int i = 0; i < list.size(); i++){
            System.out.println("List at:" + i + " " + list.get(i));
        }
        return list;
    }

    public void getQuestions(Object questions){

    }
}