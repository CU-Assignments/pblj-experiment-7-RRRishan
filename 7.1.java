import java.sql.*;
import java.util.Scanner;

public class ProductCRUD {
    static final String DB_URL = "jdbc:sqlite:products.db";

    public static void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS Product (" +
                         "ProductID INTEGER PRIMARY KEY," +
                         "ProductName TEXT NOT NULL," +
                         "Price REAL NOT NULL," +
                         "Quantity INTEGER NOT NULL)";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    public static void createProduct(Scanner sc) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);
            System.out.print("Enter Product ID: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter Product Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Price: ");
            double price = sc.nextDouble();
            System.out.print("Enter Quantity: ");
            int qty = sc.nextInt();

            String sql = "INSERT INTO Product VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.setString(2, name);
                pstmt.setDouble(3, price);
                pstmt.setInt(4, qty);
                pstmt.executeUpdate();
                conn.commit();
                System.out.println("Product added successfully.");
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Insert Error: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection Error: " + e.getMessage());
        }
    }

    public static void readProducts() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Product")) {

            while (rs.next()) {
                System.out.printf("%d | %s | %.2f | %d\n",
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity"));
            }
        } catch (SQLException e) {
            System.out.println("Read Error: " + e.getMessage());
        }
    }

    public static void updateProduct(Scanner sc) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);
            System.out.print("Enter Product ID to update: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter new Product Name: ");
            String name = sc.nextLine();
            System.out.print("Enter new Price: ");
            double price = sc.nextDouble();
            System.out.print("Enter new Quantity: ");
            int qty = sc.nextInt();

            String sql = "UPDATE Product SET ProductName=?, Price=?, Quantity=? WHERE ProductID=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setDouble(2, price);
                pstmt.setInt(3, qty);
                pstmt.setInt(4, id);
                pstmt.executeUpdate();
                conn.commit();
                System.out.println("Product updated successfully.");
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Update Error: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection Error: " + e.getMessage());
        }
    }

    public static void deleteProduct(Scanner sc) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);
            System.out.print("Enter Product ID to delete: ");
            int id = sc.nextInt();
            String sql = "DELETE FROM Product WHERE ProductID=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                conn.commit();
                System.out.println("Product deleted successfully.");
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Delete Error: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        createTable();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Product Management ---");
            System.out.println("1. Create Product");
            System.out.println("2. Read Products");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> createProduct(sc);
                case 2 -> readProducts();
                case 3 -> updateProduct(sc);
                case 4 -> deleteProduct(sc);
                case 5 -> {
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
