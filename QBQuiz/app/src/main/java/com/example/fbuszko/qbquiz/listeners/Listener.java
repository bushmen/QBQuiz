package com.example.fbuszko.qbquiz.listeners;

import com.example.fbuszko.qbquiz.model.QuizQuestion;

public interface Listener {
    void onCheckClicked(String answer, QuizQuestion question);
}