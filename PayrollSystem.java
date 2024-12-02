import java.time.LocalDate;

public class PayrollSystem {

    // Generate payslip for an individual employee
    public static Payslip generateMonthlyPayslips(Employee employee) {
            double grossPay = employee.getDeductionsCalculator().getMonthlyEarnings(employee);  // Assuming the gross pay is simply the salary for now
            double totalDeductions = employee.getDeductionsCalculator().calculateTotalDeductions(employee);
            double netPay = grossPay - totalDeductions;
        LocalDate payDate = LocalDate.now();
        return new Payslip(employee.getEmployeeId(), employee.getName(), payDate, grossPay, netPay);
    }

}