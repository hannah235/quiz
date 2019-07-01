package com.example.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.example.quiz.Model.QuestionBean;

import java.util.ArrayList;

public class QuestionDBHelper extends SQLiteOpenHelper {
    private String table = "question";
    private ArrayList<QuestionBean> questionBeans;
    public QuestionDBHelper(@Nullable Context context, @Nullable String name,
                            @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        questionBeans = new ArrayList<>();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder builder = new StringBuilder();
        builder.append("create table " + table);
        builder.append("(id integer primary key autoincrement, ");
        builder.append("question text, score integer,");
        builder.append("answer integer, choice1 text, ");
        builder.append("choice2 text, choice3 text, choice4 text, type integer, created text)");
        String sql = builder.toString();
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="drop table question";
        db.execSQL(sql);
        onCreate(db);
    }

    public long insert(QuestionBean qb){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("question",qb.getQuestion());
        values.put("score",qb.getScore());
        values.put("answer",qb.getAnswer());
        values.put("created",qb.getCreated());
        for (int i=0 ;i<qb.getChoice().length ;i++){
            int index = i +1;
            values.put("choice" + index, qb.getChoice()[i]);
        }
        values.put("type",qb.getType());
        return db.insert(table,null, values);
    }

    public QuestionBean select(int id){
        SQLiteDatabase db = getWritableDatabase();
        //Cursor query문 반환형
        Cursor cursor = db.query(table,null,"id=?",new String[] {String.valueOf(id)},null,null,null);
        if (cursor.moveToNext()){
            QuestionBean qb = new QuestionBean();
            qb.setId(cursor.getInt(cursor.getColumnIndex("id")));
            qb.setAnswer(cursor.getString(cursor.getColumnIndex("answer")));
            qb.setQuestion(cursor.getString(cursor.getColumnIndex("question")));
            qb.setCreated(cursor.getLong(cursor.getColumnIndex("created")));
            qb.setScore(cursor.getInt(cursor.getColumnIndex("score")));
            qb.setType(cursor.getInt(cursor.getColumnIndex("type")));
            String[] choices = new String[4];
            for (int i=0;i<5;i++){
                choices[i] = cursor.getString(cursor.getColumnIndex("choice"+i));
            }
            qb.setChoice(choices);
            return qb;
        }else{
            return null;
        }
    }

    public ArrayList<QuestionBean> selectMode(int type){
        SQLiteDatabase db = getWritableDatabase();
        //Cursor query문 반환형
        Cursor cursor = db.query(table,null,"type=?",new String[] {String.valueOf(type)},null,null,null);
        while (cursor.moveToNext()) {
            QuestionBean qb = new QuestionBean();
            qb.setId(cursor.getInt(cursor.getColumnIndex("id")));
            qb.setAnswer(cursor.getString(cursor.getColumnIndex("answer")));
            qb.setQuestion(cursor.getString(cursor.getColumnIndex("question")));
            qb.setCreated(cursor.getLong(cursor.getColumnIndex("created")));
            qb.setScore(cursor.getInt(cursor.getColumnIndex("score")));
            qb.setType(cursor.getInt(cursor.getColumnIndex("type")));
            String[] choices = new String[4];
            for (int i=1;i<5;i++){
                choices[i-1] = cursor.getString(cursor.getColumnIndex("choice"+i));
            }
            qb.setChoice(choices);
            questionBeans.add(qb);
        }
        return questionBeans;

    }

    public ArrayList<QuestionBean> select(){
        SQLiteDatabase db = getWritableDatabase();
        //Cursor query문 반환형
        Cursor cursor = db.query(table,null,null,null,null,null,null);
        questionBeans.clear();
        while (cursor.moveToNext()){
            QuestionBean qb = new QuestionBean();
            qb.setId(cursor.getInt(cursor.getColumnIndex("id")));
            qb.setAnswer(cursor.getString(cursor.getColumnIndex("answer")));
            qb.setQuestion(cursor.getString(cursor.getColumnIndex("question")));
            qb.setCreated(cursor.getLong(cursor.getColumnIndex("created")));
            qb.setScore(cursor.getInt(cursor.getColumnIndex("score")));
            qb.setType(cursor.getInt(cursor.getColumnIndex("type")));
            String[] choices = new String[4];
            for (int i=1;i<5;i++){
                choices[i-1] = cursor.getString(cursor.getColumnIndex("choice"+i));
            }
            qb.setChoice(choices);
            questionBeans.add(qb);
        }
        return questionBeans;
    }

    public int update(int idx,QuestionBean qb){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("question",qb.getQuestion());
        values.put("score",qb.getScore());
        values.put("answer",qb.getAnswer());
        values.put("type",qb.getType());
        for (int i=0 ;i<qb.getChoice().length ;i++){
            int index = i +1;
            values.put("choice" + index, qb.getChoice()[i]);
        }
        return db.update(table, values, "id=?", new String[]{String.valueOf(idx)});
    }

    public int delete(int id){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(table,"id=?",new String[]{String.valueOf(id)});
    }
}
