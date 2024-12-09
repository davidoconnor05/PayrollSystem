import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
/**
 * Command Line Interface (CLI) for interacting with the Payroll System.
 * <p>This class provides an interface for employees, administrators, and human resources
 * personnel to perform various tasks such as viewing details, submitting requests, managing employees, and generating payslips.</p>
 *
 * @author Conor Power
 */
public class CLI {
    /**
     * Entry point for the Payroll System CLI application.
     * <p>Provides a main menu for different types of users: Employee, Admin, and Human Resources.</p>
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Payroll System");

        while (true) {
            System.out.println("\nLogin as:");
            System.out.println("1. Employee");
            System.out.println("2. Admin");
            System.out.println("3. Human Resources");
            System.out.println("4. Exit");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    employeeMenu(scanner);
                    break;
                case "2":
                    adminMenu(scanner);
                    break;
                case "3":
                    hrMenu(scanner);
                    break;
                case "4":
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    /**
     * Displays the Employee Menu and handles employee-related actions.
     * <p>Allows employees to view their details, most recent payslip, historical payslips, and submit payment requests(part time only).</p>
     *
     * <p>If an invalid Employee ID is entered, the method will notify the user and return
     *  to the main menu. If an employee has no payslips, appropriate messages are displayed.</p>
     *
     * @param scanner The {@link Scanner} object for reading user input.
     */
    // Employee Menu
    private static void employeeMenu(Scanner scanner) {
        System.out.println("\n--- Employee Menu ---");

        System.out.print("Enter your Employee ID: ");
        try {
            int employeeId = Integer.parseInt(scanner.nextLine().trim());
            List<Employee> employees = CSVHandler.readEmployeesFromCSV();
            Employee employee = Employee.getEmployeeById(employees, employeeId);

            // Load the employee's payslips from CSV or data source
            List<Payslip> payslips = CSVHandler.readPayslipsForEmployee(employeeId);
            employee.setPayslips(payslips);

            while (true) {
                System.out.println("\nChoose an option:");
                System.out.println("1. View Details");
                System.out.println("2. View Most Recent Payslip");
                System.out.println("3. View Historical Payslips");
                System.out.println("4. Submit Payment Request");
                System.out.println("5. Logout");
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        System.out.println("\nEmployee Details:");
                        System.out.println(employee);
                        break;
                    case "2":
                        System.out.println("\nMost Recent Payslip:");
                        if (!employee.getPayslips().isEmpty()) {
                            System.out.println(employee.getPayslips().get(employee.getPayslips().size() - 1));
                        } else {
                            System.out.println("No payslips available.");
                        }
                        break;
                    case "3":
                        if (employee.getPayslips().isEmpty()) {
                            System.out.println("\nNo payslips available.");
                        } else {
                            System.out.println("\nHistorical Payslips:");
                            employee.getPayslips().forEach(System.out::println);
                        }
                        break;
                    case "4":
                        if (employee instanceof PartTimeEmployee) {
                            submitPaymentRequest((PartTimeEmployee) employee, scanner);
                        } else {
                            System.out.println("Invalid option for this employee type.");
                        }
                        break;
                    case "5":
                        System.out.println("Logging out...");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid Employee ID. Returning to main menu.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Submits a payment request for a part-time employee.
     * <p>The payment request requires the number of hours worked to calculate the total payment amount.and updates the employee's record in the system. If the employee has already submitted
     *  a payment request, the method notifies the user and terminates the process.</p>
     *
     *  <p>Once the payment request is successfully submitted, the employee's hours worked
     *   and request status are updated, and the changes are saved to the "PartTimeEmployees.csv" file.</p>
     *
     * @param employee The {@link PartTimeEmployee} submitting the request.
     * @param scanner The {@link Scanner} object for reading user input.
     */
    // Submit a payment request for a part-time employee
    private static void submitPaymentRequest(PartTimeEmployee employee, Scanner scanner) {
        if (employee.isPaymentRequestSubmitted()) {
            System.out.println("A payment request has already been submitted.");
            return;
        }

        System.out.print("Enter the number of hours worked this month: ");
        try {
            int hoursWorked = Integer.parseInt(scanner.nextLine().trim());

            if (hoursWorked <= 0) {
                System.out.println("Invalid number of hours. Payment request not submitted.");
                return;
            }

            double paymentAmount = hoursWorked * employee.getHourlyRate();
            System.out.printf("Payment request for €%.2f submitted successfully.%n", paymentAmount);
            employee.setHoursWorked(hoursWorked);
            employee.submitPaymentRequest();
            CSVHandler.updateEmployeeInCSV(employee);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Payment request not submitted.");
        }
    }
    /**
     * Displays the Admin Menu and handles administrator-related actions.
     * <p>Allows administrators to create new employees and perform administrative tasks.</p>
     *
     * @param scanner The {@link Scanner} object for reading user input.
     */
    // Admin Menu
    private static void adminMenu(Scanner scanner) {
        System.out.println("\n--- Admin Menu ---");

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Create a New Employee");
            System.out.println("2. Logout");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createEmployee(scanner);
                    break;
                case "2":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    /**
     * Creates a new employee and saves the details to the system.
     * <p>Supports both full-time and part-time employees.</p>
     *
     * @param scanner The {@link Scanner} object for reading user input.
     */
     // Create a new employee
    private static void createEmployee(Scanner scanner) {
        System.out.println("\n--- Create New Employee ---");

        try {
            // Get employee name
            System.out.print("Enter name: ");
            String name = scanner.nextLine().trim();

            // Get employee type
            System.out.print("Enter employee type (1 for FULL_TIME, 2 for PART_TIME): ");
            int typeChoice = Integer.parseInt(scanner.nextLine().trim());
            Employee.EmployeeType employeeType = (typeChoice == 1) ? Employee.EmployeeType.FULL_TIME : Employee.EmployeeType.PART_TIME;

            // Get position
            System.out.print("Enter position: ");
            String position = scanner.nextLine().trim();

            double payRate = 0.0; // This will be the hourly rate for part-time employees, or the salary for full-time employees
            int scalePoint = 0;   // This will be used for salary scale points (only for full-time employees)
            double healthInsuranceRate = 0.0; // For full-time employees

            // For part-time employees, ask for hourly rate
            if (employeeType == Employee.EmployeeType.PART_TIME) {
                System.out.print("Enter hourly rate: ");
                payRate = Double.parseDouble(scanner.nextLine().trim());
            } else {
                // For full-time employees, get salary scale point and retrieve the salary from CSVHandler
                System.out.print("Enter salary scale point: ");
                scalePoint = Integer.parseInt(scanner.nextLine().trim());
                double salary = CSVHandler.readSalary(position, scalePoint);
                payRate = salary;

                System.out.print("Enter health insurance rate (as a %): ");
                healthInsuranceRate = Double.parseDouble(scanner.nextLine().trim());
            }

            // Set default last promotion date
            LocalDate lastPromotionDate = LocalDate.now();

            // Create the new employee based on type
            Employee newEmployee;
            if (employeeType == Employee.EmployeeType.FULL_TIME) {
                // Create a full-time employee
                newEmployee = new Employee(name, CSVHandler.getLowestUniqueId(), employeeType, position, payRate, scalePoint, lastPromotionDate, healthInsuranceRate);
            } else {
                // Create a part-time employee
                newEmployee = new PartTimeEmployee(name, CSVHandler.getLowestUniqueId(), position, payRate, 0, lastPromotionDate, false);
            }

            // Write employee to CSV
            CSVHandler.writeEmployeeToCSV(newEmployee);
            System.out.println("New employee created successfully!");
        } catch (Exception e) {
            System.out.println("Error creating employee. Please try again.");
            e.printStackTrace(); // Optional: for debugging purposes
        }
    }

    /**
     * Displays the Human Resources (HR) Menu and handles HR-related actions.
     * <p>Allows HR personnel to promote employees and generate monthly payslips.</p>
     *
     * @param scanner The {@link Scanner} object for reading user input.
     */
    // Human Resources Menu
    private static void hrMenu(Scanner scanner) {
        System.out.println("\n--- Human Resources Menu ---");

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Promote Full-Time Employee");
            System.out.println("2. Generate Monthly Payslips");
            System.out.println("3. Logout");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    promoteEmployee(scanner);
                    break;
                case "2":
                    generateMonthlyPayslipsForAll();
                    break;
                case "3":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    /**
     * Generates monthly payslips for all employees.
     * <p>Payslips are generated only on the 25th of the month. If the current date is not the 25th,
     *   this method will notify the user and skip the generation process. Employees who have not
     *  submitted payment requests (for part-time employees) are excluded from the payroll</p>
     *
     *  <p>The method reads employee data from the "Employees.csv" file, generates payslips using
     *   the {@link PayrollSystem}, and writes the generated payslips to "Payslips.csv".</p>
     */
    // Generate monthly payslips for all employees on the 25th
    private static void generateMonthlyPayslipsForAll() {
        List<Employee> employees = CSVHandler.readEmployeesFromCSV();
        LocalDate today = LocalDate.now();

        if (today.getDayOfMonth() == 25) {
            for (Employee employee : employees) {
                Payslip payslip = PayrollSystem.generateMonthlyPayslips(employee); // Generate payslip for each employee
                CSVHandler.writePayslipToCSV(payslip);  // Write each payslip to CSV
            }
            System.out.println("Payslips generated successfully for all employees.");
            System.out.println("Employees who did not submit a payment request have not been paid.");

        } else {
            System.out.println("Payslips can only be generated on the 25th of the month.");
        }
    }

    /**
     * Promotes a full-time employee to a new job title and salary scale point.
     * <p>Updates the employee's position, salary, and last promotion date.The new salary is retrieved from the "ULSalaryInformation.csv" file based on the provided job
     *  title and salary scale point.</p>
     *
     * <p>Note: Only full-time employees are eligible for promotion. If the employee is part-time,
     *   an error message will be displayed, and the promotion process will terminate.</p>
     *
     * @param scanner The {@link Scanner} object for reading user input.
     */
    // Promote a full-time employee
    private static void promoteEmployee(Scanner scanner) {
        try {
            System.out.print("Enter the Employee ID to promote: ");
            int employeeId = Integer.parseInt(scanner.nextLine().trim());

            List<Employee> employees = CSVHandler.readEmployeesFromCSV();
            Employee employee = Employee.getEmployeeById(employees, employeeId);

            if (employee.getEmployeeType() != Employee.EmployeeType.FULL_TIME) {
                System.out.println("Only full-time employees can be promoted.");
                return;
            }

            System.out.print("Enter the new job title: ");
            String newJobTitle = scanner.nextLine().trim();

            System.out.print("Enter the new salary scale point: ");
            int newScalePoint = Integer.parseInt(scanner.nextLine().trim());

            double newSalary = CSVHandler.readSalary(newJobTitle, newScalePoint);
            if (newSalary < 0) {
                System.out.println("Invalid salary scale point for the given job title.");
                return;
            }

            employee.setEmployeePosition(newJobTitle);
            employee.setSalaryPoint(newScalePoint);
            employee.setSalary(newSalary);
            employee.setLastPromotionDate(LocalDate.now());
            CSVHandler.updateEmployeeInCSV(employee);

            System.out.println("Promotion applied successfully.");
        } catch (Exception e) {
            System.out.println("Error during promotion. Please try again.");
        }
    }
}
