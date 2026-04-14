package deemcee;

public class Manager extends Employee implements IQuanLy {

    private double allowance;

    public Manager() { super(); }
    public Manager(String id, String name, double salary, double allowance) {
        super(id, name, salary);                                                    
        this.allowance = allowance;
    }

    public double calculateSalary() {
        return super.getSalary() + allowance;
    }

    @Override
    public void employeeEvaluation(Employee emp) {
        System.out.println("Overall Evaluation");
    }

    @Override
    public void employeeEvaluation(Employee emp, double kpi) throws Exception {
        if (kpi < 0 || kpi > 100) {
            throw new Exception("Invalid KPI!");
        }else{
            System.out.println("Overall Evaluation: " + kpi);
        }
    }
}
