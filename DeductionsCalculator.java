import java.time.LocalDate;


/*
Employees pay the following deductions: Health Insurance, Union Fees, PRSI, USC, and Income Tax.

If an employee are earning €352 or less per week (before tax is deducted), they will not pay any social insurance. Otherwise, if they earn over €352 per week, they pay 4.1% PRSI on all their earnings.

The USC brackets are as follows:

Less than €12,012: 0.5% USC rate
Income between €12,012 and €25,760: 2% USC rate
Income between €25,760 and €70,044: 4% USC rate
Balance of Income over €70,044: 8% USC rate

Employees pay 20% in income tax.

Employees pay 0.8% in union fees, and how much they pay in health insurance is a parameter of the Employee object.

 */
public abstract class DeductionsCalculator {

    // Abstract method to get employee's monthly earnings
    protected abstract double getMonthlyEarnings(Employee employee);

    // Method to calculate health insurance
    protected double calculateHealthInsurance(Employee employee){
        return employee.getHealthInsuranceRate() / 100 * getMonthlyEarnings(employee);
    }

    // Method to calculate Union Fees (0.8% of earnings)
    protected double calculateUnionFees(Employee employee){
        return 0.008 * getMonthlyEarnings(employee);
    }

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

    // Method to calculate Income Tax (20%)
    protected double calculateIncomeTax(Employee employee){
        return 0.20 * getMonthlyEarnings(employee); // 20% of monthly earnings
    }

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

class FullTimeEmployeeDeductionsCalculator extends DeductionsCalculator {

    @Override
    protected double getMonthlyEarnings(Employee employee) {
        return employee.getSalary() / 12; // Full-time employee gets monthly salary
    }
}

class PartTimeEmployeeDeductionsCalculator extends DeductionsCalculator {

    @Override
    protected double getMonthlyEarnings(Employee employee) {
        PartTimeEmployee partTimeEmployee = (PartTimeEmployee) employee;
        return partTimeEmployee.getHourlyRate() * partTimeEmployee.getHoursWorked(); // Part-time employee earns based on hourly rate and hours worked
    }
}
