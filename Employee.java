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
    private LocalDate hireDate;
    private LocalDate lastPromotionDate;
    private List<Payslip> payslips;

    public Employee(String name, int employeeId, EmployeeType employeeType, String employeePosition,
                     double salary, int salaryPoint, LocalDate hireDate) {
        this.employeePosition=employeePosition;
        this.employeeId=employeeId;
        this.name = name;
        this.employeeType = employeeType;
        this.salary = salary;
        this.salaryPoint = salaryPoint;
        this.hireDate = hireDate;
        this.lastPromotionDate = hireDate;
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

    public LocalDate getHireDate() {
        return hireDate;
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
        return 5;  // temporary return value
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
                ",\n HireDate = " + hireDate +
                ",\n LastPromotionDate = " + lastPromotionDate;
    }
}
