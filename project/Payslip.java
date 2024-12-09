import java.time.LocalDate;
/**
 * Represents a payslip for an employee.
 * <p>This class contains details about an employee's monthly payment, including their gross pay,
 * deductions, net pay, and the date of payment.</p>
 *
 * @author Conor Power
 */
public class Payslip {
    private int employeeId; // Added employeeId for precise mapping
    private String employeeName;
    private LocalDate payDate;
    private double grossPay;
    private double netPay;
    /**
     * Constructs a {@code Payslip} with the specified details.
     *
     * @param employeeId The unique ID of the employee.
     * @param employeeName The name of the employee.
     * @param payDate The date of payment.
     * @param grossPay The amount for gross pay before deductions.
     * @param netPay The amount for net pay after deductions.
     */
    // Constructs a Payslip
    public Payslip(int employeeId, String employeeName, LocalDate payDate, double grossPay, double netPay) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.payDate = payDate;
        this.grossPay = grossPay;
        this.netPay = netPay;
    }

    // Getters and Setters
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
     * @param employeeId The unique ID of the employee.
     */
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    /**
     * Gets the employee's name.
     *
     * @return The name of the employee.
     */
    public String getEmployeeName() {
        return employeeName;
    }
    /**
     * Sets the employee's name.
     *
     * @param employeeName The name of the employee.
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    /**
     * Gets the payment date.
     *
     * @return The date of payment.
     */
    public LocalDate getPayDate() {
        return payDate;
    }
    /**
     * Sets the payment date.
     *
     * @param payDate The date of payment.
     */
    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }
    /**
     * Gets the gross pay amount before deductions.
     *
     * @return The gross pay amount.
     */
    public double getGrossPay() {
        return grossPay;
    }
    /**
     * Sets the gross pay amount before deductions.
     *
     * @param grossPay The gross pay amount.
     */
    public void setGrossPay(double grossPay) {
        this.grossPay = grossPay;
    }
    /**
     * Gets the net pay amount after deductions.
     *
     * @return The net pay amount.
     */
    public double getNetPay() {
        return netPay;
    }
    /**
     * Sets the net pay amount after deductions.
     *
     * @param netPay The net pay amount.
     */
    public void setNetPay(double netPay) {
        this.netPay = netPay;
    }
    /**
     * Returns a string representation of the payslip.
     *
     * @return A formatted string with the employee's ID, name, pay date, gross pay, and net pay.
     */
    @Override
    public String toString() {
        return "Payslip [Employee ID: " + employeeId + ", Name: " + employeeName + ", Date: " + payDate +
                ", Gross Pay: €" + String.format("%.2f", grossPay) + ", Net Pay: €" + String.format("%.2f", netPay) + "]";
    }
}
