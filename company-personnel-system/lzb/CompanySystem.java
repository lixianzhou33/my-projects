// CompanySystem.java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CompanySystem {
    // 数据库连接信息 - 适配 MySQL 5.7
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sys?" +
            "useSSL=false&" +                     // 禁用 SSL（测试环境）
            "allowPublicKeyRetrieval=true&" +     // 允许公钥检索
            "serverTimezone=UTC";                // 指定时区

    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // 请根据实际情况填写密码

    private static Connection connection = null;
    private static List<Person> personList = new ArrayList<>();
    private static List<User> userList = new ArrayList<>(); // 存储用户信息

    // 添加公共静态方法获取下一个ID
    public static int getNextPersonId() {
        return personList.size();
    }

    public static void main(String[] args) {
        try {
            // 加载 MySQL 5.7 驱动类
            Class.forName("com.mysql.jdbc.Driver");

            // 打印连接信息（调试用）
            System.out.println("尝试连接数据库: " + DB_URL);

            // 建立数据库连接
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("数据库连接成功！");

            // 初始化数据库表
            initializeDatabase();

            // 从数据库加载人员数据
            loadPersonsFromDatabase();

            // 从数据库加载用户数据
            loadUsersFromDatabase();

        } catch (ClassNotFoundException e) {
            System.out.println("错误：未找到 MySQL 驱动类，请添加 mysql-connector-java 依赖。");
            System.out.println("MySQL 5.7 应使用 5.x 版本的驱动（如 mysql-connector-java:5.1.49）");
            e.printStackTrace();
            return;
        } catch (SQLException e) {
            System.out.println("数据库连接失败！错误详情：");
            e.printStackTrace();
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.println("1. 注册");
            System.out.println("2. 登录");
            System.out.println("3. 退出");
            System.out.print("请选择操作：");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            switch (choice) {
                case 1:
                    register(scanner);
                    break;
                case 2:
                    loggedIn = login(scanner);
                    break;
                case 3:
                    closeDatabaseConnection();
                    System.out.println("系统退出成功！");
                    return;
                default:
                    System.out.println("无效选择，请重新输入！");
            }
        }

        boolean running = true;

        while (running) {
            System.out.println("\n===== 公司人员管理系统 =====");
            System.out.println("1. 添加人员");
            System.out.println("2. 查找人员");
            System.out.println("3. 提升级别");
            System.out.println("4. 显示所有人员");
            System.out.println("5. 退出系统");
            System.out.print("请选择操作：");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            switch (choice) {
                case 1:
                    addPerson(scanner);
                    break;
                case 2:
                    searchPerson(scanner);
                    break;
                case 3:
                    promotePerson(scanner);
                    break;
                case 4:
                    displayAllPersons();
                    break;
                case 5:
                    running = false;
                    closeDatabaseConnection();
                    System.out.println("系统退出成功！");
                    break;
                default:
                    System.out.println("无效选择，请重新输入！");
            }
        }
    }

    // 初始化数据库表
    private static void initializeDatabase() throws SQLException {
        // 创建用户表
        String createUserTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(50) NOT NULL UNIQUE," +
                "password VARCHAR(50) NOT NULL" +
                ")";

        // 创建人员表
        String createPersonTableSQL = "CREATE TABLE IF NOT EXISTS persons (" +
                "id INT PRIMARY KEY," +
                "name VARCHAR(50) NOT NULL," +
                "type VARCHAR(20) NOT NULL," +
                "level INT NOT NULL," +
                "salary DOUBLE," +
                "fixed_salary DOUBLE," +
                "hours INT," +
                "rate DOUBLE," +
                "sales DOUBLE," +
                "commission DOUBLE," +
                "sales_commission DOUBLE" +
                ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createUserTableSQL);
            stmt.executeUpdate(createPersonTableSQL);
            System.out.println("数据库表初始化完成！");
        }
    }

    // 从数据库加载人员数据
    private static void loadPersonsFromDatabase() {
        String selectSQL = "SELECT * FROM persons";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                int level = rs.getInt("level");

                Person person = null;

                switch (type) {
                    case "Manager":
                        double fixedSalary = rs.getDouble("fixed_salary");
                        person = new Manager(name, fixedSalary);
                        break;
                    case "PartTimeTech":
                        int hours = rs.getInt("hours");
                        double rate = rs.getDouble("rate");
                        person = new PartTimeTech(name, hours, rate);
                        break;
                    case "PartTimeSalesman":
                        double sales = rs.getDouble("sales");
                        double commission = rs.getDouble("commission");
                        person = new PartTimeSalesman(name, sales, commission);
                        break;
                    case "SalesManager":
                        fixedSalary = rs.getDouble("fixed_salary");
                        double salesCommission = rs.getDouble("sales_commission");
                        person = new SalesManager(name, fixedSalary, salesCommission);
                        break;
                }

                if (person != null) {
                    person.id = id;
                    person.level = level;
                    personList.add(person);
                }
            }

            System.out.println("从数据库加载了 " + personList.size() + " 条人员数据。");

        } catch (SQLException e) {
            System.out.println("从数据库加载人员数据失败！");
            e.printStackTrace();
        }
    }

    // 从数据库加载用户数据
    private static void loadUsersFromDatabase() {
        String selectSQL = "SELECT * FROM users";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                userList.add(new User(username, password));
            }

            System.out.println("从数据库加载了 " + userList.size() + " 条用户数据。");

        } catch (SQLException e) {
            System.out.println("从数据库加载用户数据失败！");
            e.printStackTrace();
        }
    }

    // 保存人员到数据库
    private static void savePersonToDatabase(Person person) {
        String insertSQL = "INSERT INTO persons " +
                "(id, name, type, level, salary, fixed_salary, hours, rate, sales, commission, sales_commission) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setInt(1, person.id);
            pstmt.setString(2, person.name);

            if (person instanceof Manager) {
                pstmt.setString(3, "Manager");
                Manager m = (Manager) person;
                pstmt.setInt(4, m.level);
                pstmt.setDouble(5, m.calculateSalary());
                pstmt.setDouble(6, m.fixedSalary);
                pstmt.setNull(7, Types.INTEGER);
                pstmt.setNull(8, Types.DOUBLE);
                pstmt.setNull(9, Types.DOUBLE);
                pstmt.setNull(10, Types.DOUBLE);
                pstmt.setNull(11, Types.DOUBLE);
            } else if (person instanceof PartTimeTech) {
                pstmt.setString(3, "PartTimeTech");
                PartTimeTech tt = (PartTimeTech) person;
                pstmt.setInt(4, tt.level);
                pstmt.setDouble(5, tt.calculateSalary());
                pstmt.setNull(6, Types.DOUBLE);
                pstmt.setInt(7, tt.hours);
                pstmt.setDouble(8, tt.rate);
                pstmt.setNull(9, Types.DOUBLE);
                pstmt.setNull(10, Types.DOUBLE);
                pstmt.setNull(11, Types.DOUBLE);
            } else if (person instanceof PartTimeSalesman) {
                pstmt.setString(3, "PartTimeSalesman");
                PartTimeSalesman ps = (PartTimeSalesman) person;
                pstmt.setInt(4, ps.level);
                pstmt.setDouble(5, ps.calculateSalary());
                pstmt.setNull(6, Types.DOUBLE);
                pstmt.setNull(7, Types.INTEGER);
                pstmt.setNull(8, Types.DOUBLE);
                pstmt.setDouble(9, ps.sales);
                pstmt.setDouble(10, ps.commission);
                pstmt.setNull(11, Types.DOUBLE);
            } else if (person instanceof SalesManager) {
                pstmt.setString(3, "SalesManager");
                SalesManager sm = (SalesManager) person;
                pstmt.setInt(4, sm.level);
                pstmt.setDouble(5, sm.calculateSalary());
                pstmt.setDouble(6, sm.fixedSalary);
                pstmt.setNull(7, Types.INTEGER);
                pstmt.setNull(8, Types.DOUBLE);
                pstmt.setNull(9, Types.DOUBLE);
                pstmt.setNull(10, Types.DOUBLE);
                pstmt.setDouble(11, sm.comm);
            }

            pstmt.executeUpdate();
            System.out.println("人员数据已保存到数据库！");

        } catch (SQLException e) {
            System.out.println("保存人员数据到数据库失败！");
            e.printStackTrace();
        }
    }

    // 更新人员级别到数据库
    private static void updatePersonLevelInDatabase(int id, int level) {
        String updateSQL = "UPDATE persons SET level = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setInt(1, level);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("人员级别已更新到数据库！");
        } catch (SQLException e) {
            System.out.println("更新人员级别到数据库失败！");
            e.printStackTrace();
        }
    }

    // 保存用户到数据库
    private static void saveUserToDatabase(String username, String password) {
        String insertSQL = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            System.out.println("用户数据已保存到数据库！");
        } catch (SQLException e) {
            System.out.println("保存用户数据到数据库失败！");
            e.printStackTrace();
        }
    }

    // 关闭数据库连接
    private static void closeDatabaseConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("数据库连接已关闭。");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 注册功能
    private static void register(Scanner scanner) {
        System.out.print("请输入用户名：");
        String username = scanner.nextLine();
        System.out.print("请输入密码：");
        String password = scanner.nextLine();
        userList.add(new User(username, password));
        saveUserToDatabase(username, password);
        System.out.println("注册成功！");
    }

    // 登录功能
    private static boolean login(Scanner scanner) {
        System.out.print("请输入用户名：");
        String username = scanner.nextLine();
        System.out.print("请输入密码：");
        String password = scanner.nextLine();

        for (User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                System.out.println("登录成功！");
                return true;
            }
        }
        System.out.println("用户名或密码错误，请重新输入！");
        return false;
    }

    // 添加人员功能
    private static void addPerson(Scanner scanner) {
        System.out.println("\n=== 添加人员 ===");
        System.out.println("请选择人员类型：");
        System.out.println("1. 经理");
        System.out.println("2. 兼职技术人员");
        System.out.println("3. 兼职推销员");
        System.out.println("4. 销售经理");
        int type = scanner.nextInt();
        scanner.nextLine();

        Person person = null;

        switch (type) {
            case 1:
                System.out.print("输入经理姓名：");
                String mName = scanner.nextLine();
                System.out.print("输入固定月薪：");
                double mSalary = scanner.nextDouble();
                person = new Manager(mName, mSalary);
                break;
            case 2:
                System.out.print("输入技术人员姓名：");
                String ttName = scanner.nextLine();
                System.out.print("输入工作小时数：");
                int hours = scanner.nextInt();
                System.out.print("输入时薪：");
                double rate = scanner.nextDouble();
                person = new PartTimeTech(ttName, hours, rate);
                break;
            case 3:
                System.out.print("输入推销员姓名：");
                String saName = scanner.nextLine();
                System.out.print("输入销售额：");
                double sales = scanner.nextDouble();
                System.out.print("输入提成比例（如0.1）：");
                double commission = scanner.nextDouble();
                person = new PartTimeSalesman(saName, sales, commission);
                break;
            case 4:
                System.out.print("输入销售经理姓名：");
                String smName = scanner.nextLine();
                System.out.print("输入固定月薪：");
                double fixed = scanner.nextDouble();
                System.out.print("输入销售提成：");
                double comm = scanner.nextDouble();
                person = new SalesManager(smName, fixed, comm);
                break;
            default:
                System.out.println("无效类型！");
                return;
        }

        personList.add(person);
        savePersonToDatabase(person);
        System.out.println("人员添加成功！");
    }

    // 查找人员功能（按姓名或编号）
    private static void searchPerson(Scanner scanner) {
        System.out.println("\n=== 查找人员 ===");
        System.out.print("请输入姓名或编号：");
        String keyword = scanner.nextLine();

        for (Person p : personList) {
            if (p.name.equals(keyword) || String.valueOf(p.id).equals(keyword)) {
                System.out.println(p.showInfo());
                return;
            }
        }
        System.out.println("未找到对应人员！");
    }

    // 提升级别功能
    private static void promotePerson(Scanner scanner) {
        System.out.println("\n=== 提升级别 ===");
        System.out.print("请输入需要提升级别的人员编号：");
        int id = scanner.nextInt();

        for (Person p : personList) {
            if (p.id == id) {
                p.promoteLevel();
                updatePersonLevelInDatabase(p.id, p.level);
                System.out.println("级别提升成功！新级别：" + p.level);
                return;
            }
        }
        System.out.println("未找到该编号人员！");
    }

    // 显示所有人员信息
    private static void displayAllPersons() {
        System.out.println("\n=== 所有人员信息 ===");
        for (Person p : personList) {
            p.salary = p.calculateSalary(); // 计算月薪
            System.out.println(p.showInfo());
        }
        // 计算月薪总额
        double total = personList.stream().mapToDouble(p -> p.calculateSalary()).sum();
        System.out.println("\n月薪总额：" + total + " 元");
    }
}