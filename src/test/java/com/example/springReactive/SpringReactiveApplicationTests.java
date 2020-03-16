package com.example.springReactive;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import com.example.springReactive.dao.EmployeeDao;
import com.example.springReactive.model.Employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SpringReactiveApplicationTests {
	private WebTestClient client;

    private List<Employee> expectedList;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private ApplicationContext context;

    @BeforeEach
    void beforeEach() {
        this.client =
                WebTestClient
                        .bindToApplicationContext(context)
                        .configureClient()
                        .baseUrl("/flux")
                        .build();

        this.expectedList =
                employeeDao.findAll().collectList().block();
                System.out.println(this.expectedList);
    }

    @Test
    void testGetAllEmployeee() {
        client
                .get()
                .uri("/")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Employee.class)
                .isEqualTo(expectedList);
    }

    @Test
    void testEmployeeInvalidIdNotFound() {
        client
                .get()
                .uri("/11")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void testEmployeeIdFound() {
        Employee expectedEmployee = expectedList.get(0);
        System.out.println(expectedEmployee);

        client
                .get()
                .uri("/{id}", expectedEmployee.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Employee.class)
                .isEqualTo(expectedEmployee);
    }

	@Test
    public void testDeleteEmployeeById() {
        client.delete()
                .uri("/1009")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("successfully delted");
    }

    @Test
    public void testCreateEmployee() {
		Employee employee=Employee.builder()
		.id(1010)
		 .name("Ganesh kumar") 
		 .department("department") 
		 .build();    
		 
		 client.post()
                .uri("/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employee), Employee.class)
                .exchange()
				.expectStatus().isOk()
				.expectBody(String.class)
                .isEqualTo("employee inserted");
				
				
    }


    @Test
    public void testEmployeeStreamValues() {
        Employee expectedEmployee = expectedList.get(0);
        FluxExchangeResult<Employee> result =client
                .get()
                .uri("/stream/values")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Employee.class);

        StepVerifier.create(result.getResponseBody())
                .expectNext(expectedEmployee)
                .thenCancel()
                .verify();
    }
}