package com.bridgelabz.employeepayroll;

import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollService {
    public enum IOService{FILE_IO, DB_IO, REST_IO}
    private List<EmployeePayrollData> employeePayrollList;
    private static EmployeePayrollDBService employeePayrollDBService;

    public EmployeePayrollService() {
        employeePayrollDBService = EmployeePayrollDBService.getInstance();
    }

    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
        this();
        this.employeePayrollList = new ArrayList<>(employeePayrollList);
    }

    public long countEntries(EmployeePayrollService.IOService ioService) {
        if (ioService.equals(EmployeePayrollService.IOService.FILE_IO))
            return new EmployeePayrollFileIOService().countEntries();
        System.out.println(employeePayrollList.size());
        return employeePayrollList.size();
    }

    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioservice) {
        if (ioservice.equals(IOService.DB_IO)){
            this.employeePayrollList= employeePayrollDBService.readData();
        }
        return this.employeePayrollList;
    }
}
