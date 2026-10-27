
import java.sql.*;
import java.util.Scanner;

public class FacultyManagement {

    static final String URL =
            "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    static final String USER = "system";
    static final String PASSWORD = "password12345";

    static Connection con;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            Class.forName("oracle.jdbc.OracleDriver");

            con = DriverManager.getConnection(
                    URL, USER, PASSWORD);

            System.out.println("Oracle Database Connected!");

            createTable();

            int choice = 0;

            do {
                System.out.println("\n===== FACULTY MANAGEMENT SYSTEM =====");
                System.out.println("1. Insert Faculty Details");
                System.out.println("2. Search Faculty by ID");
                System.out.println("3. Update Designation");
                System.out.println("4. Update Salary");
                System.out.println("5. Display Faculty by Department");
                System.out.println("6. Display All Faculty");
                System.out.println("7. Exit");
                System.out.print("Enter your choice: ");

                try {
                    choice = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Enter a valid number!");
                    continue;
                }

                try {
                    switch (choice) {
                        case 1:
                            insertFaculty();
                            break;
                        case 2:
                            searchFaculty();
                            break;
                        case 3:
                            updateDesignation();
                            break;
                        case 4:
                            updateSalary();
                            break;
                        case 5:
                            displayByDepartment();
                            break;
                        case 6:
                            displayAllFaculty();
                            break;
                        case 7:
                            System.out.println("Exiting program...");
                            break;
                        default:
                            System.out.println("Invalid choice!");
                    }
                } catch (SQLException e) {
                    System.out.println("Database error: "
                            + e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid numeric input!");
                }

            } while (choice != 7);

        } catch (ClassNotFoundException e) {
            System.out.println(
                    "Oracle JDBC driver not found. Add the ojdbc JAR.");
        } catch (SQLException e) {
            System.out.println(
                    "Connection failed: " + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            sc.close();
        }
    }

    
    static void createTable() throws SQLException {

        String sql =
                "CREATE TABLE FACULTY (" +
                "FACULTY_ID NUMBER(10) PRIMARY KEY, " +
                "NAME VARCHAR2(100) NOT NULL, " +
                "DEPARTMENT VARCHAR2(50) NOT NULL, " +
                "DESIGNATION VARCHAR2(50), " +
                "SALARY NUMBER(10,2))";

        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
            System.out.println("FACULTY table created successfully!");
        } catch (SQLException e) {
        
            if (e.getErrorCode() == 955) {
                System.out.println("FACULTY table already exists.");
            } else {
                throw e;
            }
        }
    }

    
    static void insertFaculty() throws SQLException {

        String sql = "INSERT INTO FACULTY " +
                "(FACULTY_ID, NAME, DEPARTMENT, DESIGNATION, SALARY) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Faculty ID: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Department: ");
            String department = sc.nextLine();

            System.out.print("Enter Designation: ");
            String designation = sc.nextLine();

            System.out.print("Enter Salary: ");
            double salary = Double.parseDouble(sc.nextLine());

            if (salary < 0) {
                System.out.println("Salary cannot be negative!");
                return;
            }

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, department);
            ps.setString(4, designation);
            ps.setDouble(5, salary);

            ps.executeUpdate();

            System.out.println("Faculty inserted successfully!");
        }
    }

    
    static void searchFaculty() throws SQLException {

        String sql =
                "SELECT * FROM FACULTY WHERE FACULTY_ID = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Faculty ID: ");
            int id = Integer.parseInt(sc.nextLine());

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\nFaculty Details");
                    System.out.println("ID: "
                            + rs.getInt("FACULTY_ID"));
                    System.out.println("Name: "
                            + rs.getString("NAME"));
                    System.out.println("Department: "
                            + rs.getString("DEPARTMENT"));
                    System.out.println("Designation: "
                            + rs.getString("DESIGNATION"));
                    System.out.println("Salary: "
                            + rs.getBigDecimal("SALARY"));
                } else {
                    System.out.println("Faculty not found!");
                }
            }
        }
    }

    
    static void updateDesignation() throws SQLException {

        String sql =
                "UPDATE FACULTY SET DESIGNATION = ? " +
                "WHERE FACULTY_ID = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Faculty ID: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("Enter New Designation: ");
            String designation = sc.nextLine();

            ps.setString(1, designation);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Designation updated successfully!");
            } else {
                System.out.println("Faculty not found!");
            }
        }
    }

    
    static void updateSalary() throws SQLException {

        String sql =
                "UPDATE FACULTY SET SALARY = ? " +
                "WHERE FACULTY_ID = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Faculty ID: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("Enter New Salary: ");
            double salary = Double.parseDouble(sc.nextLine());

            if (salary < 0) {
                System.out.println("Salary cannot be negative!");
                return;
            }

            ps.setDouble(1, salary);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Salary updated successfully!");
            } else {
                System.out.println("Faculty not found!");
            }
        }
    }


    static void displayByDepartment() throws SQLException {

        String sql =
                "SELECT * FROM FACULTY " +
                "WHERE UPPER(DEPARTMENT) = UPPER(?) " +
                "ORDER BY FACULTY_ID";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Department: ");
            String department = sc.nextLine();

            ps.setString(1, department);

            try (ResultSet rs = ps.executeQuery()) {

                boolean found = false;

                System.out.printf("%-10s %-20s %-15s %-25s %12s%n",
                        "ID", "Name", "Department",
                        "Designation", "Salary");

                while (rs.next()) {
                    found = true;
                    printFaculty(rs);
                }

                if (!found) {
                    System.out.println(
                            "No faculty found in this department.");
                }
            }
        }
    }

    
    static void displayAllFaculty() throws SQLException {

        String sql =
                "SELECT * FROM FACULTY ORDER BY FACULTY_ID";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            boolean found = false;

            System.out.printf("%-10s %-20s %-15s %-25s %12s%n",
                    "ID", "Name", "Department",
                    "Designation", "Salary");

            while (rs.next()) {
                found = true;
                printFaculty(rs);
            }

            if (!found) {
                System.out.println("No faculty records available.");
            }
        }
    }

    
    static void printFaculty(ResultSet rs) throws SQLException {

        System.out.printf("%-10d %-20s %-15s %-25s %12s%n",
                rs.getInt("FACULTY_ID"),
                rs.getString("NAME"),
                rs.getString("DEPARTMENT"),
                rs.getString("DESIGNATION"),
                rs.getBigDecimal("SALARY"));
    }
}
