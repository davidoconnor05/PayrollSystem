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
            System.out.println("2. Employee info ");
            System.out.println("3. Exit");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createEmployee(scanner);
                    break;
                case "3":
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    return;
                case "2":
                    employeeDetails(scanner);
                    break;
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

        // Set the hire date as the current date
        LocalDate hireDate = LocalDate.now();

        // Handle full-time vs. part-time employee details
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
            double salary;
            try {
                salary = CSVHandler.readSalary(jobTitle, salaryPoint);
            } catch (Exception e) {
                System.out.println("Error reading salary from file: " + e.getMessage());
                return;
            }

            // Create a new full-time employee
            Employee newEmployee = new Employee(name, employeeId, employeeType, jobTitle, salary, salaryPoint, hireDate);

            // Write the employee to CSV
            CSVHandler.writeEmployeeToCSV(newEmployee);

            // Print confirmation
            System.out.println("\nFull-time employee created successfully:");
            System.out.println(newEmployee);

        } else if (employeeType == Employee.EmployeeType.PART_TIME) {
            // Get specific details for part-time employees
            System.out.print("Enter the employees hourly rate: ");
            double hourlyRate;
            try {
                hourlyRate = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid hourly rate. Please enter a number.");
                return;
            }

            // Create a new part-time employee
            PartTimeEmployee newPartTimeEmployee = new PartTimeEmployee(
                    name, employeeId, jobTitle, hourlyRate, 0, hireDate
            );

            // Write the part-time employee to CSV
            //CSVHandler.writeEmployeeToCSV(newPartTimeEmployee);

            // Print confirmation
            System.out.println("\nPart-time employee created successfully:");
            System.out.println(newPartTimeEmployee);
        } else {
            System.out.println("Unknown employee type.");
        }
    }
    private static void employeeDetails(Scanner scanner) {
            System.out.print("Enter the employee ID: ");
            try {
                int employeeId = Integer.parseInt(scanner.nextLine());
                // Load employee details to a list
                List<Employee> employees = CSVHandler.readEmployeesFromCSV();
                System.out.println(Employee.getEmployeeById(employees, employeeId));
                return;
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID. Please enter a number.");
                return;
            }

    }
}
