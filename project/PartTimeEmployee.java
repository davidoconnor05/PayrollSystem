import java.time.LocalDate;
/**
 * Represents a part-time employee in the University.
 * <p>This class extends the {@link Employee} class and adds specific attributes and methods
 * for managing part-time employees, like their hourly rate, hours worked, and payment request status.</p>
 *
 * @author Conor Power
 */

public class PartTimeEmployee extends Employee {
    private int hoursWorked;
    private double hourlyRate;
    private boolean hasSubmittedPaymentRequest;
    /**
     * Constructs a {@code PartTimeEmployee} with the specified details.
     *
     * @param name The name of the employee.
     * @param employeeId The unique ID of the employee.
     * @param jobTitle The job title of the employee.
     * @param hourlyRate The hourly pay rate for the employee.
     * @param hoursWorked The total hours worked by the employee.
     * @param lastPromotionDate The date of the employee's last promotion.
     * @param hasSubmittedPaymentRequest Whether the employee has submitted a payment request.
     */
    public PartTimeEmployee(String name, int employeeId, String jobTitle,
                            double hourlyRate, int hoursWorked,
                            LocalDate lastPromotionDate, boolean hasSubmittedPaymentRequest) {
        super(name, employeeId, EmployeeType.PART_TIME, jobTitle, 0, 0, lastPromotionDate, 0);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
        this.hasSubmittedPaymentRequest = hasSubmittedPaymentRequest;
        setDeductionsCalculator(new PartTimeEmployeeDeductionsCalculator());
    }
    /**
     * Gets the total hours worked.
     *
     * @return The total hours worked.
     */
    public int getHoursWorked() {
        return hoursWorked;
    }
    /**
     * Sets the total hours worked.
     *
     * @param hoursWorked The total hours worked.
     */
    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }
    /**
     * Gets the hourly pay rate.
     *
     * @return The hourly pay rate.
     */
    public double getHourlyRate() {
        return hourlyRate;
    }
    /**
     * Sets the hourly pay rate.
     *
     * @param hourlyRate The hourly pay rate.
     */
    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
    /**
     * Checks if the employee has submitted a payment request.
     *
     * @return {@code true} if the payment request has been submitted; {@code false} otherwise.
     */
    public boolean isPaymentRequestSubmitted() {
        return hasSubmittedPaymentRequest;
    }
    /**
     * Marks the payment request as submitted for the employee.
     */
    public void submitPaymentRequest() {
        this.hasSubmittedPaymentRequest = true;
    }
    /**
     * Adds a payslip for the employee and resets relevant fields.
     * <p>This method resets the hours worked and payment request status after adding a payslip.</p>
     *
     * @param payslip The payslip to be added. Must not be {@code null}.
     * @throws IllegalArgumentException If the payslip is {@code null}.
     */
    @Override
    public void addPayslip(Payslip payslip) {
        if (payslip == null) {
            throw new IllegalArgumentException("Payslip cannot be null.");
        }
        // Reset hoursWorked and paymentRequestSubmitted
        this.hoursWorked = 0;
        this.hasSubmittedPaymentRequest = false;
    }
    /**
     * Returns a string representation of the part-time employee,
     * with their hours worked, hourly rate, and payment request status.
     *
     * @return A string containing the employee's details.
     */
    @Override
    public String toString() {
        return super.toString() +
                "\n\n Part-Time Info:" +
                "\n Hours Worked = " + hoursWorked +
                ",\n Hourly Rate = " + hourlyRate +
                ",\n Payment Request Submitted = " + hasSubmittedPaymentRequest;
    }
}
