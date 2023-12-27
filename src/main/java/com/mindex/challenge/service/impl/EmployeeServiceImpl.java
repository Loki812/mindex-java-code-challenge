package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Stack;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    @Override
    public ReportingStructure readReport(String id) {
        LOG.debug("Reading report structure of employee with id [{}]", id);

        // assumption: Each employee only reports to one other Employee, making it a tree vs a graph
        Employee rootEmployee = employeeRepository.findByEmployeeId(id);
        int numberOfReports = 0;

        Stack<Employee> stack = new Stack<>();
        stack.push(rootEmployee);

        while (!stack.isEmpty()) {

            Employee current = stack.pop();
            if(current.getDirectReports() != null) {
                numberOfReports += current.getDirectReports().size();

                for (Employee e : current.getDirectReports()) {
                    stack.push(employeeRepository.findByEmployeeId(e.getEmployeeId()));
                }
            }
        }

        return new ReportingStructure(rootEmployee, numberOfReports);
    }
}
