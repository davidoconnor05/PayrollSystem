import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class CLI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Employee Management System");

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Create a new employee");
            System.out.println("2. View employee details");
            System.out.println("3. Exit");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createEmployee(scanner);
                    break;
                case "2":
                    employeeDetails(scanner);
                    break;
                case "3":
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Gathers input from the user and creates a new employee.
     *
     * @param scanner the Scanner object for reading user input.
     */
    private static void createEmployee(Scanner scanner) {
        System.out.print("Enter the employee's name: ");
        String name = scanner.nextLine();

        System.out.print("Enter the employee's type (FULL_TIME or PART_TIME): ");
        String employeeTypeInput = scanner.nextLine().toUpperCase();
        Employee.EmployeeType employeeType;
        try {
            employeeType = Employee.EmployeeType.valueOf(employeeTypeInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid employee type. Please enter FULL_TIME or PART_TIME.");
            return;
        }

        System.out.print("Enter the job title: ");
        String jobTitle = scanner.nextLine();

        // Generate a unique employee ID
        int employeeId = CSVHandler.getLowestUniqueId();

        // Set the hire date and last promotion date as the current date
        LocalDate hireDate = LocalDate.now();
        LocalDate lastPromotionDate = LocalDate.now();

        System.out.print("Enter the health insurance rate (as a percentage): ");
        double healthInsuranceRate;
        try {
            healthInsuranceRate = Double.parseDouble(scanner.nextLine());
            if (healthInsuranceRate < 0 || healthInsuranceRate > 100) {
                throw new IllegalArgumentException("Health insurance rate must be between 0 and 100.");
            }
        } catch (Exception e) {
            System.out.println("Invalid health insurance rate. Please enter a number between 0 and 100.");
            return;
        }

        if (employeeType == Employee.EmployeeType.FULL_TIME) {
            System.out.print("Enter the salary scale point: ");
            int salaryPoint;
            try {
                salaryPoint = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid scale point. Please enter a number.");
                return;
            }

            // Calculate salary for full-time employees
            double salary = CSVHandler.readSalary(jobTitle, salaryPoint);
            if (salary < 0) {
                System.out.println("Salary information not found for the given job title and scale point.");
                return;
            }

            // Create a new full-time employee
            Employee newEmployee = new Employee(
                    name, employeeId, employeeType, jobTitle, salary,
                    salaryPoint, lastPromotionDate, healthInsuranceRate
            );

            // Write the employee to CSV
            CSVHandler.writeEmployeeToCSV(newEmployee);

            // Print confirmation
            System.out.println("\nFull-time employee created successfully:");
            System.out.println(newEmployee);

        } else if (employeeType == Employee.EmployeeType.PART_TIME) {
            System.out.print("Enter the hourly rate: ");
            double hourlyRate;
            try {
                hourlyRate = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid hourly rate. Please enter a number.");
                return;
            }

            // Assume the payment request is not submitted initially
            boolean hasSubmittedPaymentRequest = false;

            // Create a new PartTimeEmployee
            PartTimeEmployee newEmployee = new PartTimeEmployee(
                    name, employeeId, jobTitle, hourlyRate, 0, lastPromotionDate, hasSubmittedPaymentRequest
            );

            // Write the employee to CSV (you may need to implement a separate method for part-time employees)
            CSVHandler.writeEmployeeToCSV(newEmployee);

            // Print confirmation
            System.out.println("\nPart-time employee created successfully:");
            System.out.println(newEmployee);
        }
        else {
            System.out.println("Unknown employee type.");
        }
    }

    /**
     * Displays details of an employee by ID.
     *
     * @param scanner the Scanner object for reading user input.
     */
    private static void employeeDetails(Scanner scanner) {
        System.out.print("Enter the employee ID: ");
        try {
            int employeeId = Integer.parseInt(scanner.nextLine());

            List<Employee> employees = CSVHandler.readEmployeesFromCSV();
            Employee employee = Employee.getEmployeeById(employees, employeeId);
            System.out.println("\nEmployee Details:");
            System.out.println(employee);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
