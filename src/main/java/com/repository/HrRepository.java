package com.repository;

import com.model.Hr;
import com.model.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HrRepository extends JpaRepository<Hr,Integer> {
    public Hr findByUserDetail(UserDetail userDetail);
}
