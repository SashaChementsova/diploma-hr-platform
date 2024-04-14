package com.repository;

import com.model.LanguageTestHasQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageTestHasQuestionRepository extends JpaRepository<LanguageTestHasQuestion,Integer> {
}
