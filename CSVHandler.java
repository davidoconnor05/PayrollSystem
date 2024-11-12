import java.io.*;
import java.nio.file.*;
import java.util.List;

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

    // Method to write employees to CSV
    public void writeEmployees(List<Employee> employees) {
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
    public static double readSalary(String employeePosition, int salaryPoint, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line into parts by comma
                String[] parts = line.split(", ");

                // Check if the line has at least 3 parts
                if (parts.length >= 3) {
                    // Parse job title and scale point
                    String fileEmployeePosition = parts[0];
                    int fileSalaryPoint = Integer.parseInt(parts[1]);

                    // Check if job title and scale point match
                    if (fileEmployeePosition.equalsIgnoreCase(employeePosition) && fileSalaryPoint == salaryPoint) {
                        // Parse and return the salary
                        return Double.parseDouble(parts[2]);
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
