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
            if ((line = br.readLine()) != null && line.startsWith("employeeId")) {
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

                    if (employeeType == Employee.EmployeeType.FULL_TIME) {
                        // Create and add a full-time Employee object
                        Employee employee = new Employee(name, employeeId, employeeType, jobTitle, salary, salaryPoint, hireDate);
                        employee.updateSalary(salary); // Ensure salary is correct
                        employees.add(employee);
                    } else if (employeeType == Employee.EmployeeType.PART_TIME) {
                        // Read additional data for part-time employees
                        PartTimeEmployee partTimeEmployee = readPartTimeEmployeeData(employeeId, name, jobTitle, salaryPoint, hireDate);
                        if (partTimeEmployee != null) {
                            employees.add(partTimeEmployee);
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading employees from file: " + e.getMessage());
        }

        return employees;
    }

    private static PartTimeEmployee readPartTimeEmployeeData(int employeeId, String name, String jobTitle, int salaryPoint, LocalDate hireDate) {
        try (BufferedReader br = new BufferedReader(new FileReader("PartTimeEmployees.csv"))) {
            String line;

            while ((line = br.readLine()) != null) {
                // Skip empty lines
                if (line.isBlank()) {
                    continue;
                }

                // Split the line into parts by comma
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int partTimeEmployeeId = Integer.parseInt(parts[0].trim());
                    if (partTimeEmployeeId == employeeId) {
                        // Parse additional part-time employee fields
                        double hourlyRate = Double.parseDouble(parts[1].trim());
                        int hoursWorked = Integer.parseInt(parts[2].trim());
                        boolean hasSubmittedPaymentRequest = Boolean.parseBoolean(parts[3].trim());

                        // Create and return a PartTimeEmployee object
                        PartTimeEmployee partTimeEmployee = new PartTimeEmployee(
                                name,
                                employeeId,
                                jobTitle,
                                hourlyRate,
                                hoursWorked,
                                hireDate
                        );

                        if (hasSubmittedPaymentRequest) {
                            partTimeEmployee.submitPaymentRequest();
                        }

                        return partTimeEmployee;
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading part-time employee data: " + e.getMessage());
        }

        return null; // Return null if the part-time employee data is not found
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
            // Ensure the line is written with a newline
            String line = String.format("%d,%s,%s,%s,%.2f,%d,%s,%s",
                    employee.getEmployeeId(),
                    employee.getName(),
                    employee.getEmployeeType(),
                    employee.getEmployeePosition(),
                    employee.getSalary(),
                    employee.getSalaryPoint(),
                    employee.getHireDate(),
                    employee.getLastPromotionDate());

            writer.write(line);
            writer.newLine(); // This ensures a newline is added after every employee

            System.out.println("Employee data written to Employees.csv successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to Employees.csv file: " + e.getMessage());
            return;
        }

        // If the employee is part-time, write to PartTimeEmployees.csv
        if (employee instanceof PartTimeEmployee) {
            writePartTimeEmployeeToCSV((PartTimeEmployee) employee);
        }
    }

    private static void writePartTimeEmployeeToCSV(PartTimeEmployee partTimeEmployee) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("PartTimeEmployees.csv", true))) {
            // Ensure the line is written with a newline
            String line = String.format("%d,%.2f,%d,%b",
                    partTimeEmployee.getEmployeeId(),
                    partTimeEmployee.getHourlyRate(),
                    partTimeEmployee.getHoursWorked(),
                    partTimeEmployee.isPaymentRequestSubmitted());

            writer.write(line);
            writer.newLine(); // This ensures a newline is added after every part-time employee

            System.out.println("Part-time employee data written to PartTimeEmployees.csv successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to PartTimeEmployees.csv file: " + e.getMessage());
        }
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