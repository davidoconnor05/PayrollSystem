import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CSVHandler {

    public static List<Employee> readEmployeesFromCSV() {
        List<Employee> employees = new ArrayList<>();
        List<String[]> partTimeData = new ArrayList<>();

        // Read part-time employee data first
        try (BufferedReader br = new BufferedReader(new FileReader("PartTimeEmployees.csv"))) {
            br.readLine(); // Skip the header line

            String line;
            while ((line = br.readLine()) != null) {
                partTimeData.add(line.split(","));
            }
        } catch (IOException e) {
            System.err.println("Error reading PartTimeEmployees.csv: " + e.getMessage());
        }

        // Read employees from Employees.csv
        try (BufferedReader br = new BufferedReader(new FileReader("Employees.csv"))) {
            br.readLine(); // Skip the header line

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                String name = parts[0].trim();
                int employeeId = Integer.parseInt(parts[1].trim());
                Employee.EmployeeType employeeType = Employee.EmployeeType.valueOf(parts[2].trim());
                String employeePosition = parts[3].trim();
                double salary = Double.parseDouble(parts[4].trim());
                int salaryPoint = Integer.parseInt(parts[5].trim());
                LocalDate lastPromotionDate = LocalDate.parse(parts[6].trim());
                double healthInsuranceRate = Double.parseDouble(parts[7].trim());

                if (employeeType == Employee.EmployeeType.PART_TIME) {
                    // Find the corresponding part-time data
                    for (String[] partTimeParts : partTimeData) {
                        int partTimeId = Integer.parseInt(partTimeParts[0].trim());
                        if (partTimeId == employeeId) {
                            double hourlyRate = Double.parseDouble(partTimeParts[1].trim());
                            int hoursWorked = Integer.parseInt(partTimeParts[2].trim());
                            boolean hasSubmittedPaymentRequest = Boolean.parseBoolean(partTimeParts[3].trim());

                            // Create and add a PartTimeEmployee
                            PartTimeEmployee partTimeEmployee = new PartTimeEmployee(
                                    name,            // name of the employee
                                    employeeId,      // unique employee ID
                                    employeePosition, // job title
                                    hourlyRate,      // hourly rate
                                    hoursWorked,     // hours worked
                                    LocalDate.now(), // assuming current date as the last promotion date
                                    hasSubmittedPaymentRequest // payment request status
                            );

                            employees.add(partTimeEmployee);
                            break; // Stop searching once found
                        }
                    }
            } else {
                    // Create and add a full-time employee
                    employees.add(new Employee(
                            name, employeeId, employeeType, employeePosition,
                            salary, salaryPoint, lastPromotionDate, healthInsuranceRate
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading Employees.csv: " + e.getMessage());
        }

        return employees;
    }


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

                // Parse and add the employeeId (second column) to the set
                if (parts.length > 1) {
                    try {
                        int employeeId = Integer.parseInt(parts[1].trim());
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

    public static void writeEmployeeToCSV(Employee employee) {
        // Write to Employees.csv
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Employees.csv", true))) {
            String line = String.format("%s,%d,%s,%s,%.2f,%d,%s,%.2f",
                    employee.getName(),
                    employee.getEmployeeId(),
                    employee.getEmployeeType(),
                    employee.getEmployeePosition(),
                    employee.getSalary(),
                    employee.getSalaryPoint(),
                    employee.getLastPromotionDate(),
                    employee.getHealthInsuranceRate());

            writer.write(line);
            writer.newLine(); // Ensure a newline is added after every employee entry
            System.out.println("Employee data written to Employees.csv successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to Employees.csv file: " + e.getMessage());
        }

        // If the employee is part-time, write to PartTimeEmployees.csv
        if (employee instanceof PartTimeEmployee) {
            PartTimeEmployee partTimeEmployee = (PartTimeEmployee) employee;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("PartTimeEmployees.csv", true))) {
                String partTimeLine = String.format("%d,%.2f,%d,%b",
                        partTimeEmployee.getEmployeeId(),
                        partTimeEmployee.getHourlyRate(),
                        partTimeEmployee.getHoursWorked(),
                        partTimeEmployee.isPaymentRequestSubmitted());

                writer.write(partTimeLine);
                writer.newLine(); // Ensure a newline is added after every part-time employee entry
                System.out.println("Part-time employee data written to PartTimeEmployees.csv successfully.");
            } catch (IOException e) {
                System.out.println("Error writing to PartTimeEmployees.csv file: " + e.getMessage());
            }
        }
    }


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
