import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.time.LocalDate;

public class CSVHandler {

    public static List<Employee> readEmployeesFromCSV() {
        List<Employee> employees = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("Employees.csv"))) {
            String line;

            // Skip the header line if present
            if ((line = br.readLine()) != null && line.startsWith("name")) {
                // Continue reading the next lines
            }

            while ((line = br.readLine()) != null) {
                // Split the line into parts by comma
                String[] parts = line.split(",");

                if (parts.length == 8) {
                    // Parse each field
                    int employeeId = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    Employee.EmployeeType employeeType = Employee.EmployeeType.valueOf(parts[2].trim());
                    String jobTitle = parts[3].trim();
                    double salary = Double.parseDouble(parts[4].trim());
                    int salaryPoint = Integer.parseInt(parts[5].trim());
                    LocalDate hireDate = LocalDate.parse(parts[6].trim());
                    LocalDate lastPromotionDate = LocalDate.parse(parts[7].trim());

                    // Create the Employee object and add it to the list
                    Employee employee = new Employee(name, employeeId, employeeType, jobTitle, salary, salaryPoint, hireDate);
                    employee.updateSalary(salary); // Ensures salary matches in case constructor defaulted
                    employees.add(employee);
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading employees from file: " + e.getMessage());
        }

        return employees;
    }

    // Returns lowest possible unique employee ID
    public static int getLowestUniqueId() {
        Set<Integer> existingIds = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader("Employees.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip header or empty lines
                if (line.isBlank() || line.startsWith("name")) {
                    continue;
                }

                // Split the line by commas
                String[] parts = line.split(",");

                // Parse and add the employeeId (first column) to the set
                if (parts.length > 1) {
                    try {
                        int employeeId = Integer.parseInt(parts[0].trim());
                        existingIds.add(employeeId);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing employee ID: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        // Find the lowest unique ID
        int id = 1;
        while (existingIds.contains(id)) {
            id++;
        }
        return id;
    }

    // Writes type Employee to a CSV file.
    public static void writeEmployeeToCSV(Employee employee) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Employees.csv", true))) {
            // Format the employee data as a CSV line
            String line = String.format("%d,%s,%s,%s,%.2f,%d,%s,%s",
                    employee.getEmployeeId(),
                    employee.getName(),
                    employee.getEmployeeType(),
                    employee.getEmployeePosition(),
                    employee.getSalary(),
                    employee.getSalaryPoint(),
                    employee.getHireDate(),
                    employee.getLastPromotionDate());

            // Write the line to the file
            writer.write(line);
            writer.newLine();

            System.out.println("Employee data written to CSV successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    // Method to read payslips from CSV
    /*
    public List<Payslip> readPayslips() {
        // Code goes here
        return payslips;
    }
    */

    // Method to write payslips to CSV
    public void writePayslips(List<Payslip> payslips) {
        // Code goes here
    }

    // Method to read salary from CSV
    public static double readSalary(String employeePosition, int salaryPoint) {
        try (BufferedReader br = new BufferedReader(new FileReader("ULSalaryInformation.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip empty lines or header rows
                if (line.isBlank() || line.startsWith("Category")) {
                    continue;
                }

                // Split the line into parts by comma
                String[] parts = line.split(",");

                // Trim whitespace and check if the line has at least 3 parts
                if (parts.length >= 3) {
                    String fileEmployeePosition = parts[0].trim(); // Position
                    String fileSalaryPointStr = parts[1].trim();  // Scale Point
                    String fileSalaryStr = parts[2].trim();       // Salary

                    // Validate salary point is a number
                    if (fileEmployeePosition.equalsIgnoreCase(employeePosition)
                            && Integer.parseInt(fileSalaryPointStr) == salaryPoint) {
                        // Parse and return the salary
                        return Double.parseDouble(fileSalaryStr);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing scale point or salary: " + e.getMessage());
        }

        // If not found, return -1 as a default value indicating salary not found
        return -1;
    }
}