package deemcee;

public class Developer extends Employee {
    private int OTHour;

    public Developer() {
        super();
    };
    public Developer(String id, String name, double salary, int OTHour) {
        super(id, name, salary);
        this.OTHour = OTHour;
    };

    public int getOTHour() {
        return OTHour;
    }
    public void setOTHour(int OTHour) {
        this.OTHour = OTHour;
    }

    @Override
    public double calculateSalary() {
        return super.getSalary() + (OTHour*20);
    }
}
