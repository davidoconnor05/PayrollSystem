import java.time.LocalDate;

public class PayrollSystem {
    /**
     * Represents a payroll system that handles the generation of payslips for employees.
     * <p>This class provides functionality to calculate the monthly gross pay, deductions,
     * and net pay for an individual employee, and then generate a payslip with the details.</p>
     *
     * @author Conor Power
     */
    // Generate payslip for an individual employee
    public static Payslip generateMonthlyPayslips(Employee employee) {
            double grossPay = employee.getDeductionsCalculator().getMonthlyEarnings(employee);  // Assuming the gross pay is simply the salary for now
            double totalDeductions = employee.getDeductionsCalculator().calculateTotalDeductions(employee);
            double netPay = grossPay - totalDeductions;
        LocalDate payDate = LocalDate.now();
        return new Payslip(employee.getEmployeeId(), employee.getName(), payDate, grossPay, netPay);
    }

}