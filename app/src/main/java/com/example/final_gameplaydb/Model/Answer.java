package com.example.final_gameplaydb.Model;

public class Answer {
    String option;
    boolean isCorrect;

    public Answer(String option, boolean isCorrect) {
        this.option = option;
        this.isCorrect = isCorrect;
    }

    public String getOption() {
        return option;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}
