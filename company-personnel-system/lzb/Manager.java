// 经理类
class Manager extends Person {
    double fixedSalary;

    public Manager(String name, double fixedSalary) {
        super(name);
        this.fixedSalary = fixedSalary;
    }

    @Override
    public double calculateSalary() {
        return fixedSalary;
    }
}