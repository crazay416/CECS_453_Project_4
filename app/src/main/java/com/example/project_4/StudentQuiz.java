package com.example.project_4;

public class StudentQuiz {
    public String topic;
    public String subject;
    public int time;
    public Object questionList;
    public boolean completed;

    public StudentQuiz(String topic, String subject, int time, Object questionList, boolean completed){
        this.topic = topic;
        this.subject = subject;
        this.time = time;
        this.questionList = questionList;
        this.completed = completed;
    }
}
