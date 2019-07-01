package com.example.quiz;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.quiz.Model.QuestionBean;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    private ToggleButton toggleButton;
    private EditText editText_que;
    private EditText editText_num;
    private QuestionDBHelper dbHelper;
    private ArrayList<QuestionBean> data;
    private Button add;
    private ImageButton remove;
    private RadioGroup radioGroup;

    private String answer;
    String[] choice = new String[4];
    private EditText[] editTexts;
    private RadioButton[] radioButtons;
    private ImageView[] imageViews;

    QuestionBean qb = new QuestionBean();

    int type = 1;
    int idx;
    private ImageView clickImage;
    private final int GET_GALLERY_IMAGE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        editTexts = new EditText[]{findViewById(R.id.answer1), findViewById(R.id.answer2), findViewById(R.id.answer3), findViewById(R.id.answer4)};
        radioButtons = new RadioButton[]{findViewById(R.id.radioButton1), findViewById(R.id.radioButton2), findViewById(R.id.radioButton3), findViewById(R.id.radioButton4)};
        imageViews = new ImageView[] {findViewById(R.id.imageView),findViewById(R.id.imageView2),findViewById(R.id.imageView3),findViewById(R.id.imageView4)};
        toggleButton = findViewById(R.id.tgb_type);
        editText_num = findViewById(R.id.edittext_num);
        editText_que = findViewById(R.id.editText_que);
        radioGroup = findViewById(R.id.radioGroup);
        remove = findViewById(R.id.btn_remove);
        add = findViewById(R.id.btn_save);
        answer = findViewById(R.id.answer1).toString();

        dbHelper = new QuestionDBHelper(this,"quiz",null,1);
        data = dbHelper.select();
        setResult(Activity.RESULT_CANCELED);
        idx = getIntent().getIntExtra("idx",-1);

        if (idx > -1) {
            qb = data.get(idx);
            if (qb != null){
                editText_que.setText(qb.getQuestion());
                editText_num.setText(String.valueOf(qb.getScore()));
                if (qb.getType() == 0){
                    type=0;
                    findViewById(R.id.layout_choice).setVisibility(View.VISIBLE);
                    findViewById(R.id.layout_img).setVisibility(View.GONE);
                    for (int i=0;i<4;i++){
                        editTexts[i].setText(qb.getChoice()[i]);
                        if (qb.getAnswer()!=null){
                            if (qb.getAnswer().equals(editTexts[i].getText().toString())){
                                radioButtons[i].setChecked(true);
                            }
                        }
                    }
                }else{
                    type=1;
                    findViewById(R.id.layout_img).setVisibility(View.VISIBLE);
                    findViewById(R.id.layout_choice).setVisibility(View.GONE);
                    for (int i=0;i<4;i++){
                       try{
                           Uri selectedImageUri = Uri.parse(qb.getChoice()[i]);
                           Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImageUri);
                           Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, 200, true);
                           imageViews[i].setImageBitmap(scaled);
                           choice[i] = selectedImageUri.toString();
                       }catch (Exception e){
                           e.printStackTrace();
                       }
                    }
                    if (qb.getAnswer() != null){
                        for (int j =0 ;j<4 ;j++){
                            if (qb.getAnswer().equals(qb.getChoice()[j])){
                                radioButtons[j].setChecked(true);
                            }
                        }
                    }
                }
                add.setText("수정");
            }
        }else{
            add.setText("저장");
            remove.setEnabled(false);
        }
    }

    public void onSave(View v) {

        if(editText_que.getText().toString().equals("")){
            Toast.makeText(this, "문제를 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        if (editText_num.getText().toString().equals("")){
            Toast.makeText(this, "배점을 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        if (type == 0){
            for (int i = 0; i < 4; i++) {
                if (editTexts[i].getText().toString().equals("")){
                    Toast.makeText(this, "보기를 입력해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }else if (type == 1){
            for (int i=0;i<4;i++){
                if (choice[i] == null){
                    Toast.makeText(this, "보기를 입력해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }


        String title = editText_que.getText().toString().trim();
        int num = Integer.parseInt(editText_num.getText().toString().trim());
        QuestionBean quiz = new QuestionBean();

        quiz.setType(type);
        quiz.setScore(num);
        quiz.setQuestion(title);
        quiz.setCreated(System.currentTimeMillis());

        if (type == 0){
            for (int i = 0; i < 4; i++) {
                choice[i] = editTexts[i].getText().toString().trim();
            }
        }

        quiz.setChoice(choice);
        quiz.setAnswer(answer);

        if (add.getText().equals("저장")){
            dbHelper.insert(quiz);
            QuestionActivity.data.add(quiz);
        }else if(add.getText().equals("수정")){
            dbHelper.update(qb.getId(),quiz);
            QuestionActivity.data.set(idx,quiz);
        }

        Intent i = new Intent();
        i.putExtra("insert",idx);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    public void onRemove(View v){
        dbHelper.delete(qb.getId());
        data.remove(idx);
        QuestionActivity.data.remove(idx);
        Intent i = new Intent();
        i.putExtra("result", "OK");
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    public void onToggleClick(View v){
        if (toggleButton.isChecked()){
            Log.i("Mode","텍스트");
            type = 0;
            findViewById(R.id.layout_choice).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_img).setVisibility(View.GONE);
        }else{
            Log.i("Mode","사진");
            type =1;
            findViewById(R.id.layout_img).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_choice).setVisibility(View.GONE);
        }
    }

    public void onRadioClick(View v){
        if (type == 0){
            for (int i=0 ;i<4;i++){
                if (radioButtons[i].isChecked()){
                    answer = editTexts[i].getText().toString();
                }
            }
        }else if (type == 1){
            for (int i=0 ;i<4;i++){
                if (radioButtons[i].isChecked()){
                    answer = choice[i];
                }
            }
        }
    }

    public void onImageSelect(View v){
        clickImage = (ImageView) v;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(intent, GET_GALLERY_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try{
                Uri selectedImageUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImageUri);
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, 200, true);
                clickImage.setImageBitmap(scaled);
                for (int i=0;i<4;i++){
                    if (imageViews[i] == clickImage){
                        choice[i] = selectedImageUri.toString();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}


