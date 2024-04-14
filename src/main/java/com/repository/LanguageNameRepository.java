package com.repository;

import com.model.LanguageName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageNameRepository extends JpaRepository<LanguageName,Integer> {
}
