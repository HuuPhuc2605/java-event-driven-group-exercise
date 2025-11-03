package iuh.fit.se.nhom10.view;

import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.service.TaiKhoanNhanVienService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Form đăng nhập admin
 */
public class FrmDangNhap extends JFrame {
    private JTextField txtTenDangNhap;
    private JPasswordField txtMatKhau;
    private JButton btnDangNhap;
    private JButton btnThoat;
    private JLabel lblThongBao;
    private JCheckBox chkGhiNho;
    
    private TaiKhoanNhanVienService service;
    private TaiKhoanNhanVien adminHienTai;

    public FrmDangNhap() throws SQLException {
        service = new TaiKhoanNhanVienService();
        setupUI();
    }

    /**
     * Thiết lập giao diện
     */
    private void setupUI() {
        setTitle("Đăng Nhập Hệ Thống Quản Lý Rạp Phim");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel chính
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BorderLayout());
        pnlMain.setBackground(new Color(240, 240, 240));

        // Header
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(30, 60, 120));
        pnlHeader.setPreferredSize(new Dimension(500, 60));
        
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle);

        // Panel nội dung
        JPanel pnlContent = new JPanel();
        pnlContent.setLayout(null);
        pnlContent.setBackground(Color.WHITE);
        pnlContent.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Label và TextField cho Tên đăng nhập
        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUsername.setBounds(0, 40, 150, 25);
        
        txtTenDangNhap = new JTextField();
        txtTenDangNhap.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTenDangNhap.setBounds(150, 40, 250, 30);
        txtTenDangNhap.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Label và PasswordField cho Mật khẩu
        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        lblPassword.setBounds(0, 90, 150, 25);
        
        txtMatKhau = new JPasswordField();
        txtMatKhau.setFont(new Font("Arial", Font.PLAIN, 14));
        txtMatKhau.setBounds(150, 90, 250, 30);
        txtMatKhau.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Checkbox ghi nhớ
        chkGhiNho = new JCheckBox("Ghi nhớ mật khẩu");
        chkGhiNho.setFont(new Font("Arial", Font.PLAIN, 12));
        chkGhiNho.setBounds(150, 135, 150, 20);

        // Label thông báo
        lblThongBao = new JLabel();
        lblThongBao.setFont(new Font("Arial", Font.PLAIN, 12));
        lblThongBao.setForeground(Color.RED);
        lblThongBao.setBounds(150, 160, 250, 20);

        // Button Đăng nhập
        btnDangNhap = new JButton("Đăng Nhập");
        btnDangNhap.setFont(new Font("Arial", Font.BOLD, 14));
        btnDangNhap.setBackground(Color.BLUE);
       
        btnDangNhap.setBounds(150, 200, 110, 40);
        

        // Button Thoát
        btnThoat = new JButton("Thoát");
        btnThoat.setFont(new Font("Arial", Font.BOLD, 14));
        btnThoat.setBackground(Color.DARK_GRAY);
        
        btnThoat.setBounds(280, 200, 110, 40);
      

        // Thêm components vào panel
        pnlContent.add(lblUsername);
        pnlContent.add(txtTenDangNhap);
        pnlContent.add(lblPassword);
        pnlContent.add(txtMatKhau);
        pnlContent.add(chkGhiNho);
        pnlContent.add(lblThongBao);
        pnlContent.add(btnDangNhap);
        pnlContent.add(btnThoat);

        // Thêm panel vào frame
        pnlMain.add(pnlHeader, BorderLayout.NORTH);
        pnlMain.add(pnlContent, BorderLayout.CENTER);
        add(pnlMain);

        // Gán sự kiện
        attachEvents();
    }

    /**
     * Gắn các sự kiện cho các component
     */
    private void attachEvents() {
        btnDangNhap.addActionListener(e -> handleLogin());
        btnThoat.addActionListener(e -> handleExit());
        
        // Enter key để đăng nhập
        txtMatKhau.addActionListener(e -> handleLogin());
    }

    /**
     * Xử lý đăng nhập
     */
    private void handleLogin() {
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword());

        // Kiểm tra input
        if (tenDangNhap.isEmpty() || matKhau.isEmpty()) {
            lblThongBao.setText("Vui lòng nhập tên đăng nhập và mật khẩu!");
            return;
        }

        // Gọi service để kiểm tra
        adminHienTai = service.authenticateAdmin(tenDangNhap, matKhau);

        if (adminHienTai != null) {
            // Đăng nhập thành công
            lblThongBao.setText("Đăng nhập thành công!");
            lblThongBao.setForeground(new Color(0, 150, 0));
            
            // Delay một chút rồi mở form chính
            Timer timer = new Timer(1500, e -> openMainForm());
            timer.setRepeats(false);
            timer.start();
        } else {
            // Đăng nhập thất bại
            lblThongBao.setText("Sai tên đăng nhập hoặc mật khẩu!");
            lblThongBao.setForeground(Color.RED);
            txtMatKhau.setText("");
            txtMatKhau.requestFocus();
        }
    }

    /**
     * Mở form chính sau khi đăng nhập thành công
     */
    private void openMainForm() {
        // TODO: Mở form chính của ứng dụng
        // Ví dụ: new FrmMain(adminHienTai).setVisible(true);
        JOptionPane.showMessageDialog(this, 
            "Chào mừng " + adminHienTai.getNhanVien().getTenNV() + "\nVai trò: " + adminHienTai.getVaiTro(),
            "Đăng nhập thành công", 
            JOptionPane.INFORMATION_MESSAGE);
        
        // Ẩn form đăng nhập
        setVisible(false);
    }

    /**
     * Xử lý thoát
     */
    private void handleExit() {
        int result = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn thoát?",
            "Xác nhận thoát",
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    /**
     * Lấy admin hiện tại
     */
    public TaiKhoanNhanVien getAdminHienTai() {
        return adminHienTai;
    }
}
