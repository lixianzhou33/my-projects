// Person.java
abstract class Person {
    int id;
    String name;
    double salary;
    int level;

    public Person(String name) {
        this.id = CompanySystem.getNextPersonId(); // 使用公共方法获取ID
        this.name = name;
        this.level = 1;
    }

    public abstract double calculateSalary();

    public String showInfo() {
        return "ID: " + id + ", 姓名: " + name + ", 月薪: " + salary + ", 级别: " + level;
    }

    public void promoteLevel() {
        level++;
    }
}