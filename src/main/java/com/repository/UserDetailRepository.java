package com.repository;

import com.model.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail,Integer> {

    UserDetail findByEmail(String email);
    UserDetail findByPhone(String phone);
}
