package com.dto;

import com.model.LanguageTestQuestion;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
public class LanguageQuestionDto {
    private String question;
    private List<String> answers;

    public LanguageQuestionDto(LanguageTestQuestion languageTestQuestion) {
        question=languageTestQuestion.getQuestion();
        answers=new ArrayList<>();
        answers.add(languageTestQuestion.getRightAnswer());
        answers.add(languageTestQuestion.getAnswer2());
        answers.add(languageTestQuestion.getAnswer3());
        answers.add(languageTestQuestion.getAnswer4());
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
