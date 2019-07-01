package com.example.quiz;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quiz.Model.QuestionBean;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    int mode;
    private QuestionDBHelper helper;
    private QuizAdapter adapter;
    public static ArrayList<QuestionBean> data;

    private TextView tvquestion, tvscore;
    private TextView[] editTexts;
    private ImageView[] imageViews;
    private RadioButton[] radioButtons;
    String[] choice = new String[4];

    int idx=0;
    String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mode = getIntent().getIntExtra("mode",0);

        helper = new QuestionDBHelper(this,"quiz",null,1);

        radioButtons = new RadioButton[]{findViewById(R.id.radioButton1), findViewById(R.id.radioButton2), findViewById(R.id.radioButton3), findViewById(R.id.radioButton4)};
        editTexts = new TextView[]{findViewById(R.id.view1), findViewById(R.id.view2), findViewById(R.id.view3), findViewById(R.id.view4)};
        imageViews = new ImageView[] {findViewById(R.id.imageView),findViewById(R.id.imageView2),findViewById(R.id.imageView3),findViewById(R.id.imageView4)};

        tvquestion = findViewById(R.id.quecontent);
        tvscore = findViewById(R.id.textView_score);

        if (mode == 0){
            data = helper.selectMode(mode);
            findViewById(R.id.layout_easy).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_hard).setVisibility(View.GONE);
            SettingEasyQuiz();
            answer = editTexts[0].getText().toString();
        }else if (mode == 1){
            data = helper.select();
//            findViewById(R.id.layout_hard).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_easy).setVisibility(View.GONE);
            findViewById(R.id.radioGroup).setVisibility(View.GONE);
            SettingHardQuiz();
        }



    }

    public void SettingEasyQuiz() {
        tvquestion.setText(data.get(idx).getQuestion());
        for (int i = 0; i< 4 ;i++){
            editTexts[i].setText(data.get(idx).getChoice()[i]);
        }
    }

    public void SettingHardQuiz(){
        tvquestion.setText(data.get(idx).getQuestion());
        if (data.get(idx).getType() == 0){
            findViewById(R.id.editText_answer).setVisibility(View.VISIBLE);
            findViewById(R.id.radioGroup).setVisibility(View.GONE);
            findViewById(R.id.layout_hard).setVisibility(View.GONE);
        }else{
            findViewById(R.id.editText_answer).setVisibility(View.GONE);
            findViewById(R.id.radioGroup).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_hard).setVisibility(View.VISIBLE);
            for (int i=0;i<4;i++){
                try{
                    Uri selectedImageUri = Uri.parse(data.get(idx).getChoice()[i]);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImageUri);
                    Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, 200, true);
                    imageViews[i].setImageBitmap(scaled);
                    choice[i] = selectedImageUri.toString();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            answer = choice[0];
        }
    }

    public void onNext(View v){
        EditText editText = findViewById(R.id.editText_answer);
        int score = Integer.valueOf(tvscore.getText().toString());
        if(mode == 0){
            if (answer.equals(data.get(idx).getAnswer())){
                score = score + data.get(idx).getScore();
                tvscore.setText(String.valueOf(score));
                if ((idx+1) == data.size()){
                    Toast.makeText(this,"최종 " + String.valueOf(score) + "!!",Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }else{
                    Toast.makeText(this,"정답!\n" + String.valueOf(data.get(idx).getScore()) + "점 획득",Toast.LENGTH_SHORT).show();
                }
            }else{
                if ((idx+1) == data.size()){
                    Toast.makeText(this,"최종 " + String.valueOf(score) + "점!!",Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                Toast.makeText(this,"오답ㅠㅠㅠ",Toast.LENGTH_SHORT).show();
            }
            idx ++;
            SettingEasyQuiz();
            radioButtons[0].setChecked(true);
        }else{
           if (data.get(idx).getType() == 0){

               if(data.get(idx).getAnswer().equals(editText.getText().toString())){
                   score = score + data.get(idx).getScore();
                   tvscore.setText(String.valueOf(score));
                   Toast.makeText(this,"정답!\n" + String.valueOf(data.get(idx).getScore()) + "점 획득",Toast.LENGTH_SHORT).show();
               }else {
                   Toast.makeText(this,"오답ㅠㅠㅠ",Toast.LENGTH_SHORT).show();
               }
           }else if (data.get(idx).getType() == 1){
               if (answer.equals(data.get(idx).getAnswer())) {
                   score = score + data.get(idx).getScore();
                   tvscore.setText(String.valueOf(score));
                   Toast.makeText(this,"정답!\n" + String.valueOf(data.get(idx).getScore()) + "점 획득",Toast.LENGTH_SHORT).show();
               }else{
                   Toast.makeText(this,"오답ㅠㅠㅠ",Toast.LENGTH_SHORT).show();
               }
           }

            if ((idx+1) == data.size()){
                Toast.makeText(this,"최종 " + String.valueOf(score) + "점!!",Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            idx ++;
            SettingHardQuiz();
            radioButtons[0].setChecked(true);
            editText.setText("");
        }


    }

    public void onRadioClick(View v){
        if (data.get(idx).getType() == 0){
            for (int i=0 ;i<4;i++){
                if (radioButtons[i].isChecked()){
                    answer = editTexts[i].getText().toString();
                }
            }
        }else if (data.get(idx).getType() == 1){
            for (int i=0 ;i<4;i++){
                if (radioButtons[i].isChecked()){
                    answer = choice[i];
                }
            }
        }
    }
}
