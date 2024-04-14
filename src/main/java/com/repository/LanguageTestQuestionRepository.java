package com.repository;

import com.model.LanguageTestQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageTestQuestionRepository extends JpaRepository<LanguageTestQuestion,Integer> {
}
