package com.example.springReactive.config;

import com.example.springReactive.dao.EmployeeDao;
import com.example.springReactive.model.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;

/**
 * ApplicationTestRunner
 */
//@Component
public class ApplicationTestRunner implements ApplicationRunner {

    @Autowired
    private EmployeeDao employeeDao;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Employee ramesh=Employee.builder()
                              .id(1001)
                               .name("ramesh kumar") 
                               .department("department") 
                               .build();
        Employee suresh=Employee.builder()
                               .id(1002)
                                .name("suresh kumar") 
                                .department("department") 
                                .build();
        Flux<Employee> employeeFlux=Flux.just(ramesh,suresh);
                      employeeFlux.flatMap(employee->employeeDao.save(employee))
                            .subscribe(employee1->System.out.println("employee name"+employee1.getName()));
        }


    
}