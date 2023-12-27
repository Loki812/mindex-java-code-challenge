package com.mindex.challenge.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {
    
    private String compensationUrl;
    private String compensationIdUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Before
    public void setup() {
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
        compensationUrl = "http://localhoost:" + port + "/compensation";
    }

    @Test
    public void testCreateRead() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
        // employee id set inside of here as to only test one service at a time. Not posting employee to add id
        testEmployee.setEmployeeId(UUID.randomUUID().toString());

        Date testDate = new Date();

        Compensation testComp = new Compensation();
        testComp.setEmployee(testEmployee);
        testComp.setSalary(100);
        testComp.setEffectiveDate(testDate);

        // create checks
        Compensation createdComp = restTemplate.postForEntity(compensationUrl, testComp, Compensation.class).getBody();
        assertCompensationEquivalance(testComp, createdComp);
        

        // read checks
        Compensation readComp = restTemplate.getForEntity(compensationIdUrl, Compensation.class, testComp.getEmployeeId()).getBody();
        assertCompensationEquivalance(testComp, readComp);

    }

    public static void assertCompensationEquivalance(Compensation expected, Compensation actual) {
        assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
        assertEquals(expected.getSalary(), actual.getSalary());
    }
}
