package com.bridgelabz.employeepayroll;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static com.bridgelabz.employeepayroll.EmployeePayrollService.IOService.REST_IO;

public class EmployeePayrollTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
    }

    private EmployeePayrollData[] getEmployeeList() {
        Response response = RestAssured.get("/employee_payroll");
        System.out.println("EMPLOYEE PAYROLL ENTRIES IN THE JSON SEVER:\n" + response.asString());
        EmployeePayrollData[] arrayOfEmps=new Gson().fromJson(response.asString(), EmployeePayrollData[].class);
        return arrayOfEmps;
    }

    private Response addEmployeeToJsonServer(EmployeePayrollData employeePayrollData) {
        String empJson = new Gson().toJson(employeePayrollData);
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(empJson);
        return request.post("/employee_payroll");
    }

    @Test
    public void givenEmployeeDataInJSONServer_WhenRetrieved_ShouldMatchTheCount() {
        EmployeePayrollData[] arrayOfEmps = getEmployeeList();
        EmployeePayrollService employeePayrollService;
        employeePayrollService=new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        long entries = employeePayrollService.countEntries(REST_IO);
        Assert.assertEquals(3,entries);
    }

    @Test
    public void givenNewEmployee_WhenAdded_ShouldMatch201ResponseAndCount() {
        EmployeePayrollService employeePayrollService;
        EmployeePayrollData[] arrayOfEmps = getEmployeeList();
        employeePayrollService=new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        EmployeePayrollData employeePayrollData = null;
        employeePayrollData = new EmployeePayrollData(0, "Mark Zuckerberg", "M", 3000000.0, LocalDate.now());
        Response response = addEmployeeToJsonServer(employeePayrollData);
        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
        Assert.assertEquals(201, statusCode);

        employeePayrollData = new Gson().fromJson( response.asString(), EmployeePayrollData.class);
        System.out.println(employeePayrollData);
        employeePayrollService.addEmployeeToPayroll(employeePayrollData, REST_IO);
        long entries = employeePayrollService.countEntries(REST_IO);
        Assert.assertEquals(4,entries);
    }


}












