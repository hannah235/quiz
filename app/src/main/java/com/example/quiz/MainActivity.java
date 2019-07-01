package com.example.quiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private RadioButton radioeasy;
    private RadioButton radiohard;
    private TextView textView;
    private int mode;
    private Button startbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioeasy = findViewById(R.id.radio_easy);
        radiohard = findViewById(R.id.radio_hard);
        textView = findViewById(R.id.textview_mode);
        startbtn = findViewById(R.id.btn_mode);
    }

    public void onStartClick(View v){
        Intent i = new Intent(MainActivity.this, QuizActivity.class);
        i.putExtra("mode",mode);
        startActivity(i);
    }

    public void onSetting(View v){
        startActivity(new Intent(this, QuestionActivity.class));
    }

    public void onModeClick(View v){
        if (radioeasy.isChecked()){
            mode = 0;
            textView.setText("Easy 선택");
        }else if(radiohard.isChecked()){
            mode = 1;
            textView.setText("Hard 선택");
        }else{
            textView.setText("모드를 선택해주세요.");
        }
    }
}
