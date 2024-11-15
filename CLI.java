import java.time.LocalDate;
import java.util.Scanner;

public class CLI {
    private static int nextEmployeeId = 1; // For generating unique employee IDs

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Employee Management System");

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Create a new employee");
            System.out.println("2. Exit");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createEmployee(scanner);
                    break;
                case "2":
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

        System.out.print("Enter the salary scale point: ");
        int salaryPoint;
        try {
            salaryPoint = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid scale point. Please enter a number.");
            return;
        }

        // Calculate salary
        double salary;
        try {
            salary = CSVHandler.readSalary(jobTitle, salaryPoint);
        } catch (Exception e) {
            System.out.println("Error reading salary from file: " + e.getMessage());
            return;
        }

        // Generate a unique employee ID
        int employeeId = CSVHandler.getLowestUniqueId();

        // Set the hire date as the current date
        LocalDate hireDate = LocalDate.now();

        // Create the new employee
        Employee newEmployee = new Employee(name, employeeId, employeeType, jobTitle, salary, salaryPoint, hireDate);

        // Write employees to CSV
        CSVHandler.writeEmployeeToCSV(newEmployee);

        // Print confirmation
        System.out.println("\nEmployee created successfully:");
        System.out.println(newEmployee);

        // Add logic here to save the employee to a database, file, or in-memory list
    }
}
