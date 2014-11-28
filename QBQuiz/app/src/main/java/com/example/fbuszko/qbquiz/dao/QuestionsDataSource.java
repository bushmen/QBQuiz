package com.example.fbuszko.qbquiz.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fbuszko.qbquiz.database.MySQLiteHelper;
import com.example.fbuszko.qbquiz.model.QuizQuestion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionsDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String [] allColumns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_IMG_URL, MySQLiteHelper.COLUMN_ANSWER,
        MySQLiteHelper.COLUMN_IS_ANSWERED};

    public QuestionsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public QuizQuestion createQuestion(String imageUrl, String answer, int isAnswered){
        ContentValues values = questionToValues(imageUrl, answer, isAnswered);
        long insertId = database.insert(MySQLiteHelper.TABLE_QUESTIONS, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_QUESTIONS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        QuizQuestion question = cursorToQuestion(cursor);
        cursor.close();
        return question;
    }

    public void deleteQuestion(QuizQuestion question){
        long id = question.getId();
        database.delete(MySQLiteHelper.TABLE_QUESTIONS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public List<QuizQuestion> getAllQuestions() {
        List<QuizQuestion> questions = new ArrayList<QuizQuestion>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_QUESTIONS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            QuizQuestion question = cursorToQuestion(cursor);
            questions.add(question);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return questions;
    }

    public void updateQuestion(QuizQuestion question){
        ContentValues values = questionToValues(question.getImageUrl(), question.getAnswer(), question.isAnswered());
        database.update(MySQLiteHelper.TABLE_QUESTIONS, values, MySQLiteHelper.COLUMN_ID + "=" + question.getId(), null);
    }

    private ContentValues questionToValues(String imageUrl, String answer, int isAnswered) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_IMG_URL, imageUrl);
        values.put(MySQLiteHelper.COLUMN_ANSWER, answer);
        values.put(MySQLiteHelper.COLUMN_IS_ANSWERED, isAnswered);
        return values;
    }

    private QuizQuestion cursorToQuestion(Cursor cursor) {
        QuizQuestion question = new QuizQuestion();
        question.setId(cursor.getLong(0));
        question.setImageUrl(cursor.getString(1));
        question.setAnswer(cursor.getString(2));
        question.setAnswered(cursor.getInt(3));
        return question;
    }
}
