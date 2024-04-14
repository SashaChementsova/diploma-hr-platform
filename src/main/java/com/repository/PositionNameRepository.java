package com.repository;

import com.model.PositionName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionNameRepository extends JpaRepository<PositionName,Integer> {
}
