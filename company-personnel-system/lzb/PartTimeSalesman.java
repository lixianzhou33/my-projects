// 兼职推销员类
class PartTimeSalesman extends Person {
    double sales;
    double commission;

    public PartTimeSalesman(String name, double sales, double commission) {
        super(name);
        this.sales = sales;
        this.commission = commission;
    }

    @Override
    public double calculateSalary() {
        return sales * commission;
    }
}