package com.service.serviceImpl;

import com.comparators.CityCompanyComparator;
import com.model.CityCompany;
import com.model.Employee;
import com.repository.CityCompanyRepository;
import com.service.CityCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class CityCompanyServiceImpl implements CityCompanyService {
    private final CityCompanyRepository cityCompanyRepository;
    @Autowired
    public CityCompanyServiceImpl(CityCompanyRepository cityCompanyRepository){
        this.cityCompanyRepository = cityCompanyRepository;
    }
    @Override
    public CityCompany addAndUpdateCityCompany(CityCompany cityCompany){
        return cityCompanyRepository.save(cityCompany);
    }
    @Override
    public List<CityCompany> getCityCompanies(){
        List<CityCompany> cityCompanies= cityCompanyRepository.findAll();
        cityCompanies.sort(new CityCompanyComparator());
        return cityCompanies;
    }
    @Override
    public CityCompany findCityCompanyById(int id){

        return cityCompanyRepository.findById(id).orElse(null);
    }
    @Override
    public void deleteCityCompany(int id){
        cityCompanyRepository.deleteById(id);
    }

    @Override
    public void initializeCityCompany(){
        cityCompanyRepository.save(new CityCompany("Минск"));
    }

    @Override
    public List<CityCompany> findCityCompaniesByNameCity(String nameCity){
        List<CityCompany> cityCompanies=getCityCompanies();
        List<CityCompany> cityCompanies1=new ArrayList<>();
        if (cityCompanies != null) {
            if(!(cityCompanies.isEmpty())){
                for (CityCompany cityCompany:cityCompanies) {
                    if(cityCompany.getNameCity().contains(nameCity)){
                        cityCompanies1.add(cityCompany);
                    }
                }
            }
        }
        return cityCompanies1;
    }
}
