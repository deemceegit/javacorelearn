package deemcee;

abstract class Employee {
    // Thuộc tính (Properties)
    private String id;
    private String name;
    protected double salary;

    public Employee() {}

    // Constructor (Hàm khởi tạo)
    public Employee(String id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    // Phương thức (Method)
    abstract double calculateSalary();
    public double getSalary() {
        return salary;
    }
    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void info() {
        System.out.println("ID: " + id + " | Name: " + name + " | Salary: " + calculateSalary());
    }

    // Các hàm Getter/Setter (để truy cập thuộc tính private)
    public String getName() { return name; }
}
