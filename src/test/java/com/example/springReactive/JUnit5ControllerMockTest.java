package com.example.springReactive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import com.example.springReactive.controller.TestFluxController;
import com.example.springReactive.dao.EmployeeDao;
import com.example.springReactive.model.Employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class JUnit5ControllerMockTest {
    private WebTestClient client;


    private List<Employee> expectedList;

    @MockBean
    private EmployeeDao employeeDao;


    @BeforeEach
    void beforeEach() {
        this.client =
                WebTestClient
                        .bindToController(new TestFluxController(employeeDao))
                        .configureClient()
                        .baseUrl("/flux")
                        .build();
        
        Employee ramesh=Employee.builder()
                        .id(1001)
                         .name("ramesh kumar") 
                         .department("department") 
                         .build();

        this.expectedList = Arrays.asList(ramesh);
    }

    @Test
    void testGetAllEmployees() {
        when(employeeDao.findAll()).thenReturn(Flux.fromIterable(this.expectedList));

           client.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Employee.class).isEqualTo(expectedList);
    }

    @Test
    void testEmployeeInvalidIdNotFound() {
        int id = 353;
        when(employeeDao.findById(id)).thenReturn(Mono.empty());

        client
                .get()
                .uri("/{id}", id)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testEmployeeIdFound() {
        Employee expectedProduct = this.expectedList.get(0);
        when(employeeDao.findById(expectedProduct.getId())).thenReturn(Mono.just(expectedProduct));

        client
                .get()
                .uri("/{id}", expectedProduct.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Employee.class).isEqualTo(expectedProduct);
    }
   
} 