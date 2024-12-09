import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * Represents an employee in the University.
 * <p>This class provides attributes and methods for managing employee information,
 * including their name, ID, employment type, salary details, and payslips.
 * It supports both full-time and part-time employees.</p>
 *
 * @author Conor Power
 * @version 1.0
 */
public class Employee {
    /**
     * Enum representing the type of employment.
     */

    enum EmployeeType {
        FULL_TIME,
        PART_TIME
    }

    private String name;
    private int employeeId;
    private EmployeeType employeeType;
    private String employeePosition;
    private double salary;
    private int salaryPoint;
    private LocalDate lastPromotionDate;
    private double healthInsuranceRate;
    private DeductionsCalculator deductionsCalculator;
    private List<Payslip> payslips;
    /**
     * Constructs a {@code Employee} with the specified details.
     *
     * @param name The name of the employee.
     * @param employeeId The unique ID of the employee.
     * @param employeeType The type of employment (full-time or part-time).
     * @param employeePosition The position of the employee.
     * @param salary The annual salary of the employee.
     * @param salaryPoint The salary point of the employee.
     * @param lastPromotionDate The date of the employee's last promotion.
     * @param healthInsuranceRate The health insurance rate for the employee.
     */
    // Constructor
    public Employee(String name, int employeeId, EmployeeType employeeType,
                    String employeePosition, double salary, int salaryPoint,
                    LocalDate lastPromotionDate, double healthInsuranceRate) {
        this.name = name;
        this.employeeId = employeeId;
        this.employeeType = employeeType;
        this.employeePosition = employeePosition;
        this.salary = salary;
        this.salaryPoint = salaryPoint;
        this.lastPromotionDate = lastPromotionDate;
        this.healthInsuranceRate = healthInsuranceRate;
        this.payslips = new ArrayList<>();  // Initialize the payslips list
        this.deductionsCalculator = new FullTimeEmployeeDeductionsCalculator();  // Set the appropriate deductions calculator

    }


    // Getters and Setters for Employee class
    /**
     * Gets the name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Gets the deductions calculator.
     *
     * @return The deductions calculator.
     */
    public DeductionsCalculator getDeductionsCalculator() {
        return deductionsCalculator;
    }
    /**
     * Sets the deductions calculator.
     *
     * @param deductionsCalculator The deductions calculator to set.
     */
    protected void setDeductionsCalculator(DeductionsCalculator deductionsCalculator) {
        this.deductionsCalculator = deductionsCalculator;
    }

    /**
     * Gets the employee ID.
     *
     * @return The unique ID of the employee.
     */
    public int getEmployeeId() {
        return employeeId;
    }
    /**
     * Sets the employee ID.
     *
     * @param employeeId The unique ID to set.
     */
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    /**
     * Gets the type of employment.
     *
     * @return The employment type.
     */
    public EmployeeType getEmployeeType() {
        return employeeType;
    }
    /**
     * Sets the type of employment.
     *
     * @param employeeType The employment type to set.
     */
    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }
    /**
     * Gets the position of the employee.
     *
     * @return The position of the employee.
     */
    public String getEmployeePosition() {
        return employeePosition;
    }
    /**
     * Sets the position of the employee.
     *
     * @param employeePosition The position to set.
     */
    public void setEmployeePosition(String employeePosition) {
        this.employeePosition = employeePosition;
    }
    /**
     * Gets the salary of the employee.
     *
     * @return The salary of the employee.
     */
    public double getSalary() {
        return salary;
    }
    /**
     * Sets the salary of the employee.
     *
     * @param salary The salary to set.
     */
    public void setSalary(double salary) {
        this.salary = salary;
    }
    /**
     * Gets the salary point.
     *
     * @return The salary point.
     */
    public int getSalaryPoint() {
        return salaryPoint;
    }
    /**
     * Sets the salary point.
     *
     * @param salaryPoint The salary point to set.
     */
    public void setSalaryPoint(int salaryPoint) {
        this.salaryPoint = salaryPoint;
    }
    /**
     * Gets the date of the employee's last promotion.
     *
     * @return The last promotion date of the employee.
     */
    public LocalDate getLastPromotionDate() {
        return lastPromotionDate;
    }
    /**
     * Sets the date of the employee's last promotion.
     *
     * @param lastPromotionDate The last promotion date to set.
     */
    public void setLastPromotionDate(LocalDate lastPromotionDate) {
        this.lastPromotionDate = lastPromotionDate;
    }
    /**
     * Gets the health insurance rate.
     *
     * @return The health insurance rate.
     */
    public double getHealthInsuranceRate() {
        return healthInsuranceRate;
    }
    /**
     * Sets the health insurance rate.
     *
     * @param healthInsuranceRate The health insurance rate to set.
     */
    public void setHealthInsuranceRate(double healthInsuranceRate) {
        this.healthInsuranceRate = healthInsuranceRate;
    }

    // Getter and setter for payslips
    /**
     * Gets the list of payslips.
     *
     * @return The list of {@link Payslip} objects.
     */
    public List<Payslip> getPayslips() {
        return payslips;
    }
    /**
     * Sets the list of payslips.
     *
     * @param payslips The list of {@link Payslip} objects to set.
     */
    public void setPayslips(List<Payslip> payslips) {
        this.payslips = payslips;
    }

    // Method to add a new payslip
    /**
     * Adds a payslip.
     *
     * @param payslip The {@link Payslip} object to add.
     */
    public void addPayslip(Payslip payslip) {
        this.payslips.add(payslip);
    }
    /**
     * Generates a monthly payslip.
     * <p>Uses the {@link PayrollSystem} to generate and store the payslip.</p>
     */
    public void generatePayslip() {
        PayrollSystem payrollSystem = new PayrollSystem();
        Payslip payslip = payrollSystem.generateMonthlyPayslips(this);
        payslips.add(payslip);
    }
    /**
     * Retrieves the most recent payslip.
     *
     * @return The most recent {@link Payslip}, or {@code null} if none exist.
     */
    public Payslip getMostRecentPayslip() {
        if (payslips.isEmpty()) {
            return null; // No payslips available
        }

        Payslip mostRecentPayslip = payslips.get(0);

        for (Payslip payslip : payslips) {
            if (payslip.getPayDate().isAfter(mostRecentPayslip.getPayDate())) {
                mostRecentPayslip = payslip;
            }
        }

        return mostRecentPayslip;
    }

    /**
     * Finds an employee by their ID in a list of employees.
     *
     * @param employees The list of employees.
     * @param employeeId The ID of the employee to find.
     * @return The {@link Employee} with the specified ID.
     * @throws IllegalArgumentException If no employee with the given ID is found.
     */
    // Get an employee by their ID
    public static Employee getEmployeeById(List<Employee> employees, int employeeId) {
        for (Employee employee : employees) {
            if (employee.getEmployeeId() == employeeId) {
                return employee;
            }
        }
        throw new IllegalArgumentException("No employee found with ID " + employeeId);
    }
    /**
     * Returns a string representation of the employee's details.
     * <p>The string includes the employee's ID, name, type, position, salary,
     * salary point, health insurance rate, and last promotion date.</p>
     *
     * @return A formatted string containing key details about the employee.
     */
    @Override
    public String toString() {
        return "Employee Info: " +
                "\n ID =  " + employeeId +
                ",\n Name = " + name +
                ",\n Type = " + employeeType +
                ",\n Position = " + employeePosition +
                ",\n Salary = " + salary +
                ",\n SalaryPoint = " + salaryPoint +
                ",\n HealthInsuranceRate = " + healthInsuranceRate +
                ",\n LastPromotionDate = " + lastPromotionDate;
    }
}
