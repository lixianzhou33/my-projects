// 销售经理类
class SalesManager extends Person {
    double fixedSalary;
    double comm;

    public SalesManager(String name, double fixedSalary, double comm) {
        super(name);
        this.fixedSalary = fixedSalary;
        this.comm = comm;
    }

    @Override
    public double calculateSalary() {
        return fixedSalary + comm;
    }
}