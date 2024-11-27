import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Employee {
    public enum EmployeeType {
        FULL_TIME, PART_TIME
    }

    private int employeeId;
    private String name;
    private EmployeeType employeeType; // full time or part time
    private String employeePosition;
    private double salary;
    private int salaryPoint;
    private LocalDate lastPromotionDate;
    private double healthInsuranceRate; // percent of their income they pay to health insurance.
    private List<Payslip> payslips;

    public Employee(String name, int employeeId, EmployeeType employeeType, String employeePosition,
                     double salary, int salaryPoint, LocalDate lastPromotionDate, double healthInsuranceRate) {
        this.employeePosition=employeePosition;
        this.employeeId=employeeId;
        this.name = name;
        this.employeeType = employeeType;
        this.salary = salary;
        this.salaryPoint = salaryPoint;
        this.lastPromotionDate = lastPromotionDate;
        this.healthInsuranceRate = healthInsuranceRate;
        this.payslips = new ArrayList<>(); // Initialize as ArrayList of Payslip
    }

    public String getName() {
        return name;
    }

    public String getEmployeePosition() {
        return employeePosition;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public double getSalary() {
        return salary;
    }

    public int getSalaryPoint() {
        return salaryPoint;
    }

    public double getHealthInsuranceRate() {
        return healthInsuranceRate;
    }

    public LocalDate getLastPromotionDate() {
        return lastPromotionDate;
    }

    public List<Payslip> getPayslips() {
        return new ArrayList<>(payslips); // Returns a copy to prevent external modification
    }

    public void addPayslip(Payslip payslip) {
        payslips.add(payslip);
    }

    public void updateSalary(double newSalary) {
        this.salary = newSalary;
    }

    private int getMaxSalaryPointForJob(String employeePosition) {
            // code goes here
        return 4;  // temporary return value
    }

    // Get an employee by their ID
    public static Employee getEmployeeById(List<Employee> employees, int employeeId) {
        // Iterate through the list of employees
        for (Employee employee : employees) {
            if (employee.getEmployeeId() == employeeId) {
                return employee;
            }
        }
        // If no employee is found, throw an exception
        throw new IllegalArgumentException("No employee found with ID " + employeeId);

    }


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
