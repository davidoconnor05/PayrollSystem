import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Employee {

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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeductionsCalculator getDeductionsCalculator() {
        return deductionsCalculator;
    }

    protected void setDeductionsCalculator(DeductionsCalculator deductionsCalculator) {
        this.deductionsCalculator = deductionsCalculator;
    }


    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public String getEmployeePosition() {
        return employeePosition;
    }

    public void setEmployeePosition(String employeePosition) {
        this.employeePosition = employeePosition;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getSalaryPoint() {
        return salaryPoint;
    }

    public void setSalaryPoint(int salaryPoint) {
        this.salaryPoint = salaryPoint;
    }

    public LocalDate getLastPromotionDate() {
        return lastPromotionDate;
    }

    public void setLastPromotionDate(LocalDate lastPromotionDate) {
        this.lastPromotionDate = lastPromotionDate;
    }

    public double getHealthInsuranceRate() {
        return healthInsuranceRate;
    }

    public void setHealthInsuranceRate(double healthInsuranceRate) {
        this.healthInsuranceRate = healthInsuranceRate;
    }

    // Getter and setter for payslips
    public List<Payslip> getPayslips() {
        return payslips;
    }

    public void setPayslips(List<Payslip> payslips) {
        this.payslips = payslips;
    }

    // Method to add a new payslip
    public void addPayslip(Payslip payslip) {
        this.payslips.add(payslip);
    }

    public void generatePayslip() {
        PayrollSystem payrollSystem = new PayrollSystem();
        Payslip payslip = payrollSystem.generateMonthlyPayslips(this);
        payslips.add(payslip);
    }

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


    // Get an employee by their ID
    public static Employee getEmployeeById(List<Employee> employees, int employeeId) {
        for (Employee employee : employees) {
            if (employee.getEmployeeId() == employeeId) {
                return employee;
            }
        }
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
