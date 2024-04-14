package com.repository;

import com.model.CityCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityCompanyRepository extends JpaRepository<CityCompany,Integer> {
}
