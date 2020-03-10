package com.example.springReactive.service;

import java.time.Duration;

import com.example.springReactive.model.Employee;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

/**
 * EmployeeService
 */
@Service
public class EmployeeService {

    
    public Mono<Employee> getEmployeeServiceById(int id){
        
        Employee ramesh=Employee.builder()
                              .id(1001)
                               .name("ramesh kumar") 
                               .department("department") 
                               .build();
        return Mono.just(ramesh).delayElement(Duration.ofSeconds(1));
    }
    


}