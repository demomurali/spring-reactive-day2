package com.example.springReactive.dao;

import com.example.springReactive.model.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


/**
 * EmployeeDao
 */
public interface EmployeeDao extends ReactiveMongoRepository<Employee, Integer>{

}