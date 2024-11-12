import java.time.LocalDate;

public class Payslip {
    private LocalDate payDate;
    private double grossPay;
    private double netPay;
    CSVHandler csvHandler = new CSVHandler();


    // Call the readSalary method and capture the result
    double salary = csvHandler.readSalary(jobTitle, scalePoint, filePath);

    /**
     * Constructs a Payslip with specified details.
     *
     * payDate: the date of the payslip
     * grossPay: the gross pay amount
     * deductions: the amount deducted from gross pay
     * netPay: the net pay after deductions
     */
    public Payslip(LocalDate payDate, double grossPay, double netPay) {
        this.payDate = payDate;
        this.grossPay = grossPay;
        this.netPay = netPay;
    }

    /**
     * Gets the pay date of the payslip.
     *
     * @return the pay date
     */
    public LocalDate getPayDate() {
        return payDate;
    }

    /**
     * Gets the gross pay of the payslip.
     *
     * @return the gross pay amount
     */
    public double getGrossPay() {

        return grossPay;
    }

    /**
     * Gets the net pay of the payslip.
     *
     * @return the net pay amount
     */
    public double getNetPay() {
        return netPay;
    }

    /**
     * Returns a string representation of the Payslip for easy display.
     *
     * @return formatted string with payslip details
     */
    @Override
    public String toString() {
        return "Payslip [Date: " + payDate + ", Gross Pay: €" + grossPay +
                 ", Net Pay: €" + netPay + "]";
    }
}
