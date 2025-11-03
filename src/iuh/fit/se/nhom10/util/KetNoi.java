package iuh.fit.se.nhom10.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lớp kết nối cơ sở dữ liệu SQL Server
 */
public class KetNoi {
    private static KetNoi instance;
    private Connection connection;
    
    // Thông tin kết nối database
    private static final String SERVER = "localhost";
    private static final String DATABASE = "QLRCP";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "260504";
    private static final int PORT = 1433;

    private KetNoi() {
        try {
            // Load driver SQL Server
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            String url = String.format(
                "jdbc:sqlserver://%s:%d;databaseName=%s;trustServerCertificate=true",
                SERVER, PORT, DATABASE
            );
            
            connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
            System.out.println("Kết nối database thành công!");
        } catch (ClassNotFoundException e) {
            System.out.println("Không tìm thấy driver: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối: " + e.getMessage());
        }
    }

    /**
     * Lấy instance singleton
     */
    public static KetNoi getInstance() {
        if (instance == null) {
            instance = new KetNoi();
        }
        return instance;
    }

    /**
     * Lấy connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Đóng connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Đóng kết nối thành công!");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi đóng kết nối: " + e.getMessage());
        }
    }
}
