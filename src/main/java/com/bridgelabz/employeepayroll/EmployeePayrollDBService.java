package com.bridgelabz.employeepayroll;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {

    private PreparedStatement employeePayrollDataStatement;
    private static EmployeePayrollDBService employeePayrollDBService;
    private EmployeePayrollDBService() {
    }

    public static EmployeePayrollDBService getInstance() {
        if (employeePayrollDBService == null) {
            employeePayrollDBService = new EmployeePayrollDBService();
        }
        return employeePayrollDBService;
    }

    private  Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName = "root";
        String password = "root";
        Connection connection;
        System.out.println("Processing Thread"+Thread.currentThread().getName()+"Connecting to database with id: ");
        connection = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println("Processing thread: "+Thread.currentThread().getName()+ "Id : "+
                connection + " connection successful");
        return connection;
    }

    public List<EmployeePayrollData> readData() {
        String query = "SELECT * from employee_payroll where is_active=1;";
        return this.getEmployeePayrollDataUsingDB(query);
    }

    private List<EmployeePayrollData> getEmployeePayrollDataUsingDB(String query) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    public EmployeePayrollData addEmployeeToPayroll(String name, double salary, LocalDate startDate, String gender) {
        int employeeId=-1;
        EmployeePayrollData employeePayrollData = null;
        String sql = String.format("INSERT INTO employee_payroll (name,gender,salary,start)" +
                "VALUES ('%s','%s','%s','%s')", name, gender, salary, Date.valueOf(startDate));
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected==1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) employeeId = resultSet.getInt(1);
            }
            employeePayrollData = new EmployeePayrollData(employeeId, name, salary, startDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollData;
    }

    public int updateEmployeeData(String name, double salary) {
        try {
            return this.updateDataUsingStatement(name, salary);
        } catch (EmployeePayrollException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int updateDataUsingStatement(String name, double salary) throws EmployeePayrollException {
        String query = String.format("update employee_payroll set salary = %.2f where name= '%s' and is_active=1;", salary, name);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(query);
        }catch (SQLException e) {
            throw new EmployeePayrollException(e.getMessage(), EmployeePayrollException.ExceptionType.DatabaseException);
        }
    }
}
