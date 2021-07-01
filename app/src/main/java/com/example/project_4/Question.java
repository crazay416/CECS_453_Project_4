package com.example.project_4;

import java.util.ArrayList;

public class Question {

    String question;
    ArrayList<String> answerChoices;
    String correctAnswer;



    public Question(){

    }

    public Question(String question, ArrayList<String>answerChoices, String correctAnswer){
        this.question = question;
        this.answerChoices = answerChoices;
        this.correctAnswer = correctAnswer;
    }

    public Question(String question){
        this.question = question;
    }

    public Question(ArrayList<String> answerChoices){
        this.answerChoices = answerChoices;
    }

    public Question(String question, ArrayList<String> answerChoices){
        this.question = question;
        this.answerChoices = answerChoices;
    }

    public void addAnswerChoice(String answerChoice){
        if(this.answerChoices.size()>3){

        }
        else{
            this.answerChoices.add(answerChoice);
        }

    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getAnswerChoices() {
        return answerChoices;
    }

    public void setAnswerChoices(ArrayList<String> answerChoices) {
        this.answerChoices = answerChoices;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}