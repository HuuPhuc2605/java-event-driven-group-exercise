package iuh.fit.se.nhom10.main;

import iuh.fit.se.nhom10.view.FrmDangNhap;

import java.sql.SQLException;

import javax.swing.*;

/**
 * Lớp chính để khởi động ứng dụng
 */
public class App {
    
    public static void main(String[] args) {
        // Sử dụng EDT (Event Dispatch Thread) để tạo UI an toàn
        SwingUtilities.invokeLater(() -> {
            try {
                // Thiết lập Look and Feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Tạo và hiển thị form đăng nhập
            FrmDangNhap frmDangNhap = null;
			try {
				frmDangNhap = new FrmDangNhap();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            frmDangNhap.setVisible(true);
        });
    }
}
