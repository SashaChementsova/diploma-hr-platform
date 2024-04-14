package com.repository;

import com.model.LevelLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelLanguageRepository extends JpaRepository<LevelLanguage,Integer> {
}
