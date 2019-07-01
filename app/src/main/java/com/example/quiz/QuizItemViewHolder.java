package com.example.quiz;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class QuizItemViewHolder extends RecyclerView.ViewHolder {

    TextView TextViewTitle;
    TextView TextViewMode;
    TextView TextViewDate;

    public QuizItemViewHolder(@NonNull View itemView) {
        super(itemView);
        TextViewTitle =itemView.findViewById(R.id.textViewTitle);
        TextViewMode = itemView.findViewById(R.id.textViewMode);
        TextViewDate = itemView.findViewById(R.id.textViewDate);
    }
}
