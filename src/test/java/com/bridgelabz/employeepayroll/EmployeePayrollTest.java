package com.bridgelabz.employeepayroll;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

    @Test
    public void givenEmployeeDataInJSONServer_WhenRetrieved_ShouldMatchTheCount() {
        EmployeePayrollData[] arrayOfEmps = getEmployeeList();
        EmployeePayrollService employeePayrollService;
        employeePayrollService=new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        long entries = employeePayrollService.countEntries(REST_IO);
        Assert.assertEquals(2,entries);
    }

}












