package com.example.project_4;

import java.util.ArrayList;

public class Quiz {

    ArrayList<Question> questionList;
    String subject;
    String topic;
    int timeLimit;


    public Quiz(){

    }

    public Quiz(ArrayList<Question> questionList, String subject, String topic, int timeLimit){
        this.questionList = questionList;
        this.subject = subject;
        this.topic = topic;
        this.timeLimit = timeLimit;
    }

    public ArrayList<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(ArrayList<Question> questionList) {
        this.questionList = questionList;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }
}