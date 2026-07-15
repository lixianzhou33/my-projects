// 兼职技术人员类
class PartTimeTech extends Person {
    int hours;
    double rate;

    public PartTimeTech(String name, int hours, double rate) {
        super(name);
        this.hours = hours;
        this.rate = rate;
    }

    @Override
    public double calculateSalary() {
        return hours * rate;
    }
}