import java.time.LocalDate;

public class Payslip {
    private int employeeId; // Added employeeId for precise mapping
    private String employeeName;
    private LocalDate payDate;
    private double grossPay;
    private double netPay;

    // Constructs a Payslip
    public Payslip(int employeeId, String employeeName, LocalDate payDate, double grossPay, double netPay) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.payDate = payDate;
        this.grossPay = grossPay;
        this.netPay = netPay;
    }

    // Getters and Setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public LocalDate getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }

    public double getGrossPay() {
        return grossPay;
    }

    public void setGrossPay(double grossPay) {
        this.grossPay = grossPay;
    }

    public double getNetPay() {
        return netPay;
    }

    public void setNetPay(double netPay) {
        this.netPay = netPay;
    }

    @Override
    public String toString() {
        return "Payslip [Employee ID: " + employeeId + ", Name: " + employeeName + ", Date: " + payDate +
                ", Gross Pay: €" + String.format("%.2f", grossPay) + ", Net Pay: €" + String.format("%.2f", netPay) + "]";
    }
}
