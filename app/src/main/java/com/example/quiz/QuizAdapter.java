package com.example.quiz;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quiz.Model.QuestionBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QuizAdapter extends RecyclerView.Adapter<QuizItemViewHolder> {
    private ArrayList<QuestionBean> quizData;
    private ItemClickListener listener;

    public QuizAdapter(ArrayList<QuestionBean> quizData, ItemClickListener listener) {
        this.quizData = quizData;
        this.listener = listener;
    }

    public QuizAdapter(ArrayList<QuestionBean> quizData) {
        this.quizData = quizData;
    }

    @NonNull
    @Override
    public QuizItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_quiz, viewGroup,false);
        return new QuizItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizItemViewHolder quizItemViewHolder, int i) {
        QuestionBean questionBean = quizData.get(i);
        quizItemViewHolder.TextViewTitle.setText(questionBean.getQuestion());

        if (questionBean.getType() == 1){
            quizItemViewHolder.TextViewMode.setText("Hard");
        }else{
            quizItemViewHolder.TextViewMode.setText("Easy");
        }
        Date date = new Date(questionBean.getCreated());
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        quizItemViewHolder.TextViewDate.setText(sdfNow.format(date));

        final int idx =i;
        quizItemViewHolder.itemView.setOnClickListener(v  -> {
            listener.onItemClick(v,idx);
        });

    }

    @Override
    public int getItemCount() {
        if(quizData == null){
            return 0;
        }else{
            return quizData.size();
        }
    }
}
