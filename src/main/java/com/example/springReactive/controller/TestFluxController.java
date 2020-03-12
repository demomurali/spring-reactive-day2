package com.example.springReactive.controller;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.example.springReactive.dao.EmployeeDao;
import com.example.springReactive.model.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.ExistingWebApplicationScopes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * TestMonoController
 */

@RestController
@RequestMapping("/flux")
public class TestFluxController {

    @Autowired
    private EmployeeDao employeeDao;

    @GetMapping("/{id}")
    public Mono<ResponseEntity> sayHello(@PathVariable int id){
         return employeeDao.findById(id)
                          .map(employee->{
                                  int a=5/0;
                                  System.out.println("error");
                                 return employee;
                              })
               .map(employee->getSuccessMessage(employee))
               .onErrorResume(Exception.class,error->getErrorMessage(error));
        }   

       

      private Mono<ResponseEntity<String>> getErrorMessage(Throwable error){
         System.out.println(error);
         return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(error.getMessage()));
      }  

      private ResponseEntity<String> getErrorMessage(){
         return ResponseEntity.status(HttpStatus.CONFLICT).body("Database Issue");
      } 
      
      private ResponseEntity getSuccessMessage(Employee employee){
         System.out.println(employee);
         return ResponseEntity.ok().body(employee);
      }

      @GetMapping
       public Flux sayHello() {
             return employeeDao.findAll();
                   //.doOnError(error->System.out.println(error))
                   //.onErrorMap((exception)->new Exception(exception));
      }     

      @PostMapping
      public Mono<ResponseEntity<String>> insertEmployee(@RequestBody Employee employee){
           return Mono.just(employee)
               .flatMap(employee1->employeeDao.save(employee))
               .map(employee1->ResponseEntity.status(HttpStatus.OK).body("employee inserted"))
               .doOnError(error->System.out.println(error))
               .onErrorResume(error->getErrorMessage(error));

      }


      @PatchMapping("/{id}")
      public Mono<ResponseEntity<String>> updateEmployee(@PathVariable int id,
               @RequestBody Employee employee){
        return employeeDao.findById(id)
                  .flatMap(existingEmployee->{
                     existingEmployee.setName(employee.getName());
                     existingEmployee.setSalary(employee.getSalary());
                     existingEmployee.setDepartment(employee.getDepartment());
                     return employeeDao.save(existingEmployee);
                  })
                  .map(employee1->ResponseEntity.status(HttpStatus.OK).body("successfully updated"+employee.getId())) 
                  .onErrorResume(error->getErrorMessage(error))
                  .defaultIfEmpty(ResponseEntity.status(HttpStatus.OK).body("no employee availble for this id"));      
            }

            @DeleteMapping("/{id}")
            public Mono<ResponseEntity<String>> deleteEmployee(@PathVariable int id){
              return employeeDao.deleteById(id)
                        .then(Mono.just(ResponseEntity.status(HttpStatus.OK).body("successfully delted")))
                        .defaultIfEmpty(ResponseEntity.status(HttpStatus.OK).body("no employee availble for this id"));      
                     }  




}