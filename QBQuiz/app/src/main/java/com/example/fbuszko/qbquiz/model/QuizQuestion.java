package com.example.fbuszko.qbquiz.model;

public class QuizQuestion {
    private long id;
    private String mImageUrl;
    private String mAnswer;
    private int mAnswered;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String mAnswer) {
        this.mAnswer = mAnswer;
    }

    public int isAnswered() {
        return mAnswered;
    }

    public void setAnswered(int mIsAnswered) {
        this.mAnswered = mIsAnswered;
    }
}
