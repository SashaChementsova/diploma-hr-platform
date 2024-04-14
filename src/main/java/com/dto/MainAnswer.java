package com.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
public class MainAnswer {
    private List<Answer> answerList;

    public MainAnswer(List<Answer> answerList) {
        this.answerList = answerList;
    }


    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }
}
