package com.bridgelabz.employeepayroll;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollFileIOService {
    public static String PAYROll_FILE_NAME = "payroll-file.txt";

    public void writeData(List<EmployeePayrollData> employeePayrollList) {
        StringBuffer buffer = new StringBuffer();
        employeePayrollList.forEach(emp -> {
            String empDataString = emp.toString().concat("\n");
            buffer.append(empDataString);
        });

        try {
            Files.write(Paths.get(PAYROll_FILE_NAME), buffer.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printData() {
        try {
            Files.lines(new File(PAYROll_FILE_NAME).toPath()).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long countEntries() {
        long entries = 0;
        try {
            entries = Files.lines(new File(PAYROll_FILE_NAME).toPath()).count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entries;
    }

    public List<EmployeePayrollData> readData() {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            Files.lines(new File(PAYROll_FILE_NAME).toPath()).map(line -> line.trim())
                    .forEach(line -> System.out.println(line));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }
}
