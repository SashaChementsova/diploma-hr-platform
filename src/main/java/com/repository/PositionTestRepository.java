package com.repository;

import com.model.PositionTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionTestRepository extends JpaRepository<PositionTest,Integer> {
}
