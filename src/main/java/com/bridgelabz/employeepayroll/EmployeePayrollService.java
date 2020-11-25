package com.bridgelabz.employeepayroll;

import java.time.LocalDate;
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
        System.out.println(employeePayrollList);
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

    public void addEmployeeToPayroll(EmployeePayrollData employeePayrollData, IOService ioService) {
        if (ioService.equals(IOService.DB_IO)) {
            this.addEmployeeToPayroll(employeePayrollData.name, employeePayrollData.salary, employeePayrollData.startDate, employeePayrollData.gender);
        }
        else {
            boolean add = employeePayrollList.add(employeePayrollData);
            System.out.println(add);
        }
    }
    public void addEmployeeToPayroll(String name, double salary, LocalDate startDate, String gender) {
        employeePayrollList.add(employeePayrollDBService.addEmployeeToPayroll(name, salary, startDate, gender));
    }

    public void updateEmployeeSalary(String name, double salary, IOService ioService){
        if (ioService.equals(IOService.DB_IO)) {
            int result = employeePayrollDBService.updateEmployeeData(name, salary);
            if (result == 0) return;
        }
        EmployeePayrollData employeePayrollData=this.getEmployeePayrollData(name);
        if (employeePayrollData != null) employeePayrollData.salary = salary;
    }

    public EmployeePayrollData getEmployeePayrollData(String name) {
        return this.employeePayrollList.stream()
                .filter(employeePayrollData -> employeePayrollData.name.equals(name))
                .findFirst()
                .orElse(null);
    }

}
