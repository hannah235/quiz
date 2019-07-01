package com.example.quiz;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quiz.Model.QuestionBean;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity implements ItemClickListener  {

    private String password = "1234";

    private QuestionDBHelper helper;
    private RecyclerView quizlist;
    private QuizAdapter adapter;
    public static ArrayList<QuestionBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        helper = new QuestionDBHelper(this,"quiz",null,1);

        data = helper.select();
        adapter = new QuizAdapter(data,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        quizlist = findViewById(R.id.que_item);
        quizlist.setLayoutManager(layoutManager);
        quizlist.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == QuestionBean.REQ_NUM){
            adapter.notifyDataSetChanged();
        }
    }

    public void onClick(View v){
        EditText editText  = findViewById(R.id.editText_admin);
        if (editText.getText().toString().equals(password)){
            findViewById(R.id.layout_pwd).setVisibility(View.GONE);
        }else{
            return;
        }
    }

    public void onAddClick(View v){
        Intent i = new Intent(this,SettingActivity.class);
        startActivityForResult(i, QuestionBean.REQ_NUM);
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View v, int position) {
        Intent i = new Intent(this,SettingActivity.class);
        i.putExtra("idx", position);
        startActivityForResult(i,QuestionBean.REQ_NUM);
    }
}
