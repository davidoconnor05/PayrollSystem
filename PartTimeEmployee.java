import java.time.LocalDate;

public class PartTimeEmployee extends Employee {
    private int hoursWorked;
    private double hourlyRate;
    private boolean hasSubmittedPaymentRequest;

    public PartTimeEmployee(String name, int employeeId, String employeePosition,
                            double hourlyRate, int hoursWorked, LocalDate hireDate) {
        super(name, employeeId, EmployeeType.PART_TIME, employeePosition, 0, 0, hireDate);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = 0;
        this.hasSubmittedPaymentRequest = false;
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
                "\n\n Hours Worked = " + hoursWorked +
                ",\n Hourly Rate = " + hourlyRate +
                ",\n Payment Request Submitted = " + hasSubmittedPaymentRequest;
    }
}
