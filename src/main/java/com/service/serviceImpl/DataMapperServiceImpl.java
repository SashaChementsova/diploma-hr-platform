package com.service.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model.Admin;
import com.model.Employee;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;


@Service
public class DataMapperServiceImpl {

    public Context setData(Employee empolyee, Admin admin) {

        Context context = new Context();

        Map<String, Object> data = new HashMap<>();

        data.put("employee", empolyee);
        data.put("admin", admin);

        context.setVariables(data);

        return context;
    }
}
