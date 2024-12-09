import java.time.LocalDate;


/**
 * Abstract class that provides methods to calculate various deductions for employees.
 * <p>The deductions include health insurance, union fees, PRSI, USC, and income tax.
 * Subclasses  implement the {@code getMonthlyEarnings} method to define how
 * monthly earnings are calculated for different types of employees.</p>
 *
 * <p>Deductions are calculated as follows:
 * <ul>
 *   <li>Health Insurance: A percentage of monthly earnings.</li>
 *   <li>Union Fees: 0.8% of monthly earnings.</li>
 *   <li>PRSI: 4.1% of earnings if weekly income exceeds €352, otherwise 0%.</li>
 *   <li>USC: Progressive rates based on annual earnings.--Less than €12,012: 0.5% USC rate
 * Income between €12,012 and €25,760: 2% USC rate
 * Income between €25,760 and €70,044: 4% USC rate
 * Balance of Income over €70,044: 8% USC rate</li>
 *   <li>Income Tax: 20% of monthly earnings.</li>
 * </ul>
 * </p>
 *
 * @author Conor Power
 */
public abstract class DeductionsCalculator {
    /**
     * Abstract method to retrieve an employee's monthly earnings.
     * Subclasses implement this method to define how monthly earnings are calculated.
     *
     * @param employee The employee whose monthly earnings are to be calculated.
     * @return The monthly earnings of the employee.
     */
    // Abstract method to get employee's monthly earnings
    protected abstract double getMonthlyEarnings(Employee employee);
    /**
     * Calculates the health insurance deduction.
     *
     * @param employee The employee whose health insurance is to be calculated.
     * @return The health insurance deduction amount.
     */
    // Method to calculate health insurance
    protected double calculateHealthInsurance(Employee employee){
        return employee.getHealthInsuranceRate() / 100 * getMonthlyEarnings(employee);
    }
    /**
     * Calculates the union fee deduction.
     *
     * @param employee The employee whose union fees are to be calculated.
     * @return The union fees deduction amount (0.8% of monthly earnings).
     */
    // Method to calculate Union Fees (0.8% of earnings)
    protected double calculateUnionFees(Employee employee){
        return 0.008 * getMonthlyEarnings(employee);
    }
    /**
     * Calculates the PRSI deduction.
     *
     * @param employee The employee whose PRSI is to be calculated.
     * @return The PRSI deduction amount.
     */
    // Method to calculate PRSI
    protected double calculatePRSI(Employee employee){
        double monthlyEarnings = getMonthlyEarnings(employee);
        // Convert monthly earnings to weekly earnings for the PRSI calculation
        double weeklyEarnings = monthlyEarnings / 4.33;  // Approximate number of weeks in a month
        if (weeklyEarnings <= 352) {
            return 0.0;  // No PRSI for earnings <= €352 per week
        }
        return 0.041 * monthlyEarnings; // 4.1% PRSI
    }
    /**
     * Calculates the Universal Social Charge (USC) deduction.
     *
     * @param employee The employee whose USC is to be calculated.
     * @return The USC deduction amount.
     */
    // Method to calculate USC
    protected double calculateUSC(Employee employee){
        double yearlyEarnings = getMonthlyEarnings(employee) * 12; // Annualize the monthly earnings
        double usc = 0.0;

        if (yearlyEarnings < 12012) {
            usc = 0.005 * yearlyEarnings; // 0.5% USC
        } else if (yearlyEarnings <= 25760) {
            usc = 0.02 * (yearlyEarnings - 12012) + 0.005 * 12012; // 2% on earnings between €12,012 and €25,760
        } else if (yearlyEarnings <= 70044) {
            usc = 0.04 * (yearlyEarnings - 25760) + 0.02 * (25760 - 12012) + 0.005 * 12012; // 4% on earnings between €25,760 and €70,044
        } else {
            usc = 0.08 * (yearlyEarnings - 70044) + 0.04 * (70044 - 25760) + 0.02 * (25760 - 12012) + 0.005 * 12012; // 8% on earnings above €70,044
        }

        return usc / 12; // Convert to monthly USC
    }
    /**
     * Calculates the income tax deduction.
     *
     * @param employee The employee whose income tax is to be calculated.
     * @return The income tax deduction amount (20% of monthly earnings).
     */
    // Method to calculate Income Tax (20%)
    protected double calculateIncomeTax(Employee employee){
        return 0.20 * getMonthlyEarnings(employee); // 20% of monthly earnings
    }

    /**
     * Calculates the total deductions.
     *
     * @param employee The employee whose total deductions are to be calculated.
     * @return The total deduction amount, including all applicable deductions.
     */
    // Method to calculate total deductions
    public double calculateTotalDeductions(Employee employee){
        double healthInsurance = calculateHealthInsurance(employee);
        double unionFees = calculateUnionFees(employee);
        double prsi = calculatePRSI(employee);
        double usc = calculateUSC(employee);
        double incomeTax = calculateIncomeTax(employee);

        return healthInsurance + unionFees + prsi + usc + incomeTax;
    }


}
/**
 * Deduction calculator for full-time employees.
 * <p>Monthly earnings are calculated based on the employee's annual salary.</p>
 */
class FullTimeEmployeeDeductionsCalculator extends DeductionsCalculator {
    /**
     * Retrieves the monthly earnings for a full-time employee.
     *
     * @param employee The full-time employee.
     * @return The monthly earnings of the employee.
     */
    @Override
    protected double getMonthlyEarnings(Employee employee) {
        return employee.getSalary() / 12; // Full-time employee gets monthly salary
    }
}
/**
 * Deduction calculator for part-time employees.
 * <p>Monthly earnings are calculated based on the hourly rate and hours worked.</p>
 */
class PartTimeEmployeeDeductionsCalculator extends DeductionsCalculator {
    /**
     * Retrieves the monthly earnings for a part-time employee.
     *
     * @param employee The part-time employee.
     * @return The monthly earnings of the employee.
     */
    @Override
    protected double getMonthlyEarnings(Employee employee) {
        PartTimeEmployee partTimeEmployee = (PartTimeEmployee) employee;
        return partTimeEmployee.getHourlyRate() * partTimeEmployee.getHoursWorked(); // Part-time employee earns based on hourly rate and hours worked
    }
}
