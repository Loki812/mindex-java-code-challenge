package com.mindex.challenge.data;


public class ReportingStructure {
    private Employee employee;
    private int numberOfReports;
    
    public ReportingStructure(Employee e, int numOfReports) {
        this.employee = e;
        this.numberOfReports = numOfReports;
    }

    public int getNumberOfReports() { return numberOfReports; }

    public Employee getEmployee() { return employee; }
}
