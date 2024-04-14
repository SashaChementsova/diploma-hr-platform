package com.dto;

import com.model.PositionTestQuestion;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
public class PositionQuestionDto {
    private String question;
    private List<String> answers;

    public PositionQuestionDto(PositionTestQuestion positionTestQuestion) {
        question=positionTestQuestion.getQuestion();
        answers=new ArrayList<>();
        answers.add(positionTestQuestion.getRightAnswer());
        answers.add(positionTestQuestion.getAnswer2());
        answers.add(positionTestQuestion.getAnswer3());
        answers.add(positionTestQuestion.getAnswer4());
        Collections.shuffle(answers);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}
