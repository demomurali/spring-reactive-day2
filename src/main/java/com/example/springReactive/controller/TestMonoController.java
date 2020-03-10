package com.example.springReactive.controller;


import com.example.springReactive.model.Employee;
import com.example.springReactive.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;



/**
 * TestMonoController
 */

@RestController
@RequestMapping("/mono")
public class TestMonoController {

    @GetMapping("/hello")
    public Mono<String> sayHello(){
       Mono<String> publisher=Mono.just("hello");
       return publisher;
    }


    @GetMapping("/error")
    public Mono sayexcep(){
       Mono publisher=Mono.error(new Exception("hello"));
       return publisher;
    }

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/object")
    public Mono<Employee> sayObject(){
      Mono<Employee> pub= employeeService.getEmployeeServiceById(1001);
      System.out.println("hello");
      return pub;
   }


   
    @GetMapping("/response")
    public Mono<ResponseEntity> sayResponse(){
       Employee ramesh=Employee.builder()
                              .id(1001)
                               .name("ramesh kumar") 
                               .department("department") 
                               .build();
       Mono publisher=Mono.just(ResponseEntity.ok(ramesh));
       return publisher;
    }


    
}