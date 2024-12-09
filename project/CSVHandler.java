import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * Provides utilities for reading and writing employee and payslip data to and from CSV files.
 * <p>The class handles operations related to the of employee data, including
 * part-time and full-time employees, as well as payslip generation and retrieval.</p>
 *
 * @author Conor Power
 */
public class CSVHandler {
    /**
     * Reads employee data from CSV files.
     * <p>Reads full-time employee data from "Employees.csv" and part-time employee data from "PartTimeEmployees.csv".</p>
     *
     * @return A list of {@link Employee} objects populated with data from the CSV files.
     */
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
                                    LocalDate.now(), // current date as the last promotion date
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

    /**
     * Finds the lowest unique employee ID not currently used.
     * <p>Reads existing employee IDs from "Employees.csv" to determine the next available unique ID.</p>
     *
     * @return The lowest unique employee ID.
     */
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
    /**
     * Writes an employee's data to the corresponding CSV files.
     * <p>If the employee is part-time, their data is also written to "PartTimeEmployees.csv".</p>
     *
     * @param employee The {@link Employee} object to write to the CSV files.
     */
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

    /**
     * Reads the salary for a specific job position and salary point.
     * <p>The salary data is retrieved from "ULSalaryInformation.csv".</p>
     *
     * @param employeePosition The position of the employee.
     * @param salaryPoint The salary point for the employee.
     * @return The salary corresponding to the position and salary point, or -1 if not found.
     */
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

    /**
     * Updates an employee's record in the CSV files.
     * <p>Finds and updates the employee's data in both "Employees.csv" and "PartTimeEmployees.csv" (if applicable).</p>
     *
     * @param updatedEmployee The {@link Employee} object containing the updated information.
     */
    public static void updateEmployeeInCSV(Employee updatedEmployee) {
        // Read all employees (both full-time and part-time) from Employees.csv
        List<Employee> employees = readEmployeesFromCSV();
        List<String[]> partTimeData = new ArrayList<>();

        // Load part-time-specific data from PartTimeEmployees.csv
        try (BufferedReader br = new BufferedReader(new FileReader("PartTimeEmployees.csv"))) {
            br.readLine(); // Skip the header line
            String line;
            while ((line = br.readLine()) != null) {
                partTimeData.add(line.split(","));
            }
        } catch (IOException e) {
            System.err.println("Error reading PartTimeEmployees.csv: " + e.getMessage());
        }

        // Iterate through employees to find the one to update
        boolean employeeFound = false;
        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            if (employee.getEmployeeId() == updatedEmployee.getEmployeeId()) {
                // Update the employee's data
                employees.set(i, updatedEmployee);
                employeeFound = true;
                break;
            }
        }

        // If the employee was found, update both CSV files
        if (employeeFound) {
            try {
                // Write the updated employees to Employees.csv
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("Employees.csv"))) {
                    writer.write("name,employeeId,employeeType,employeePosition,salary,salaryPoint,lastPromotionDate,healthInsuranceRate\n");
                    for (Employee employee : employees) {
                        writer.write(String.format("%s,%d,%s,%s,%.2f,%d,%s,%.2f\n",
                                employee.getName(),
                                employee.getEmployeeId(),
                                employee.getEmployeeType(),
                                employee.getEmployeePosition(),
                                employee.getSalary(),
                                employee.getSalaryPoint(),
                                employee.getLastPromotionDate(),
                                employee.getHealthInsuranceRate()));
                    }
                }

                // Update part-time-specific data in PartTimeEmployees.csv
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("PartTimeEmployees.csv"))) {
                    writer.write("employeeId,hourlyRate,hoursWorked,paymentRequestSubmitted\n");

                    boolean updated = false; // Track if part-time employee was updated
                    for (String[] partTimeParts : partTimeData) {
                        int partTimeId = Integer.parseInt(partTimeParts[0].trim());
                        if (partTimeId == updatedEmployee.getEmployeeId() && updatedEmployee instanceof PartTimeEmployee) {
                            // Write updated part-time employee details
                            PartTimeEmployee partTimeEmployee = (PartTimeEmployee) updatedEmployee;
                            writer.write(String.format("%d,%.2f,%d,%b\n",
                                    partTimeEmployee.getEmployeeId(),
                                    partTimeEmployee.getHourlyRate(),
                                    partTimeEmployee.getHoursWorked(),
                                    partTimeEmployee.isPaymentRequestSubmitted()));
                            updated = true;
                        } else {
                            // Write original data for other part-time employees
                            writer.write(String.join(",", partTimeParts) + "\n");
                        }
                    }

                    // If the part-time employee wasn't found, add them
                    if (!updated && updatedEmployee instanceof PartTimeEmployee) {
                        PartTimeEmployee partTimeEmployee = (PartTimeEmployee) updatedEmployee;
                        writer.write(String.format("%d,%.2f,%d,%b\n",
                                partTimeEmployee.getEmployeeId(),
                                partTimeEmployee.getHourlyRate(),
                                partTimeEmployee.getHoursWorked(),
                                partTimeEmployee.isPaymentRequestSubmitted()));
                    }
                }

                System.out.println("Employee data updated successfully!");
            } catch (IOException e) {
                System.err.println("Error updating the CSV files: " + e.getMessage());
            }
        } else {
            System.out.println("Employee with ID " + updatedEmployee.getEmployeeId() + " not found.");
        }
    }
    /**
     * Writes a payslip's data to the "Payslips.csv" file.
     *
     * @param payslip The {@link Payslip} object to write to the CSV file.
     */
    public static void writePayslipToCSV(Payslip payslip) {
        String filePath = "Payslips.csv";
        File file = new File(filePath);
        boolean writeHeader = !file.exists() || file.length() == 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            if (writeHeader) {
                writer.write("employeeId,employeeName,payDate,grossPay,netPay");
                writer.newLine();
            }

            String line = String.format("%d,%s,%s,%.2f,%.2f",
                    payslip.getEmployeeId(),
                    payslip.getEmployeeName(),
                    payslip.getPayDate(),
                    payslip.getGrossPay(),
                    payslip.getNetPay());

            writer.write(line);
            writer.newLine();
            System.out.println("Payslip written to Payslips.csv successfully.");
        } catch (IOException e) {
            System.err.println("Error writing Payslip to Payslips.csv: " + e.getMessage());
        }
    }
    /**
     * Reads payslips for a specific employee from "Payslips.csv".
     *
     * @param employeeId The unique ID of the employee.
     * @return A list of {@link Payslip} objects associated with the specified employee ID.
     */
    public static List<Payslip> readPayslipsForEmployee(int employeeId) {
        List<Payslip> payslips = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("payslips.csv"))) {
            String line;

            // Skip the header
            line = br.readLine(); // Read the first line (header) and do nothing with it

            // Process remaining lines
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length != 5) {
                    System.out.println("Skipping invalid line: " + line);
                    continue; // Skip malformed rows
                }

                int id = Integer.parseInt(fields[0]); // employeeId
                if (id == employeeId) {
                    // Parse remaining fields
                    String employeeName = fields[1]; // Not used
                    LocalDate payDate = LocalDate.parse(fields[2]); // payDate
                    double grossPay = Double.parseDouble(fields[3]); // grossPay
                    double netPay = Double.parseDouble(fields[4]);   // netPay

                    // Create a new Payslip object
                    payslips.add(new Payslip(id, employeeName, payDate, grossPay, netPay));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading payslips: " + e.getMessage());
        }
        return payslips;
    }
}



