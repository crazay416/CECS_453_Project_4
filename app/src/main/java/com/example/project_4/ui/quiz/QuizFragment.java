package com.example.project_4.ui.quiz;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.project_4.R;

public class QuizFragment extends Fragment {

    private QuizViewModel quizViewModel;

    public static QuizFragment newInstance() {
        return new QuizFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        quizViewModel =
                new ViewModelProvider(this).get(QuizViewModel.class);
        View root = inflater.inflate(R.layout.quiz_fragment, container, false);
        final TextView textView = root.findViewById(R.id.text_quiz);
        quizViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);
        // TODO: Use the ViewModel
    }

}