package com.repository;

import com.model.LanguageTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageTestRepository extends JpaRepository<LanguageTest,Integer> {
}
