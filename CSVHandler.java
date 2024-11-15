import java.io.*;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CSVHandler {
    private static final String EMPLOYEE_FILE_PATH = "employees.csv";
    private static final String PAYSLIP_FILE_PATH = "payslips.csv";

    // Method to read employees from CSV
    /*
    public List<Employee> readEmployees() {
        // Code goes here
        return employees;
    }
    */

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

    // Writes a list of employees to a CSV file.
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
