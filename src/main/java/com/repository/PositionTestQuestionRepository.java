package com.repository;

import com.model.PositionTestQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionTestQuestionRepository extends JpaRepository<PositionTestQuestion,Integer> {
}
