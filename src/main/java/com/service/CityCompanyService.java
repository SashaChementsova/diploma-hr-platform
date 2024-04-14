package com.service;

import com.model.CityCompany;

import java.util.List;

public interface CityCompanyService {

    public CityCompany addAndUpdateCityCompany(CityCompany cityCompany);
    public List<CityCompany> getCityCompanies();

    public CityCompany findCityCompanyById(int id);
    public void deleteCityCompany(int id);
    public void initializeCityCompany();

    public List<CityCompany> findCityCompaniesByNameCity(String nameCity);
}
