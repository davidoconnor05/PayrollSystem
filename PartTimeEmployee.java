import java.time.LocalDate;

public class PartTimeEmployee extends Employee {
    private int hoursWorked;
    private double hourlyRate;
    private boolean hasSubmittedPaymentRequest;

    public PartTimeEmployee(String name, int employeeId, String jobTitle,
                            double hourlyRate, int hoursWorked,
                            LocalDate lastPromotionDate, boolean hasSubmittedPaymentRequest) {
        super(name, employeeId, EmployeeType.PART_TIME, jobTitle, 0, 0, lastPromotionDate, 0);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
        this.hasSubmittedPaymentRequest = hasSubmittedPaymentRequest;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public boolean isPaymentRequestSubmitted() {
        return hasSubmittedPaymentRequest;
    }

    public void submitPaymentRequest() {
        this.hasSubmittedPaymentRequest = true;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\n\n Part-Time Info:" +
                "\n Hours Worked = " + hoursWorked +
                ",\n Hourly Rate = " + hourlyRate +
                ",\n Payment Request Submitted = " + hasSubmittedPaymentRequest;
    }
}
