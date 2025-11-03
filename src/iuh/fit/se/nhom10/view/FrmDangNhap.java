package iuh.fit.se.nhom10.view;

import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.service.TaiKhoanNhanVienService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Form đăng nhập admin - Giao diện cải thiện
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
     * Thiết lập giao diện cải thiện
     */
    private void setupUI() {
        setTitle("Đăng Nhập Hệ Thống Quản Lý Rạp Phim");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel chính
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BorderLayout(0, 0));
        pnlMain.setBackground(new Color(240, 240, 240));

        // Header với gradient effect
        JPanel pnlHeader = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(25, 55, 115), 0, getHeight(), new Color(15, 35, 75));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        pnlHeader.setPreferredSize(new Dimension(600, 100));
        
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel lblSubtitle = new JLabel("Quản Lý Rạp Chiếu Phim");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(200, 200, 200));
        lblSubtitle.setHorizontalAlignment(JLabel.CENTER);
        
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
        pnlHeader.add(Box.createVerticalStrut(15));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlHeader.add(lblTitle);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlHeader.add(lblSubtitle);
        pnlHeader.add(Box.createVerticalStrut(10));

        // Panel nội dung
        JPanel pnlContent = new JPanel();
        pnlContent.setLayout(new BoxLayout(pnlContent, BoxLayout.Y_AXIS));
        pnlContent.setBackground(Color.WHITE);
        pnlContent.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // Label và TextField cho Tên đăng nhập
        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUsername.setForeground(new Color(50, 50, 50));
        lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtTenDangNhap = new JTextField();
        txtTenDangNhap.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTenDangNhap.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtTenDangNhap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtTenDangNhap.setBackground(new Color(250, 250, 250));
        txtTenDangNhap.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Label và PasswordField cho Mật khẩu
        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPassword.setForeground(new Color(50, 50, 50));
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtMatKhau = new JPasswordField();
        txtMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMatKhau.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtMatKhau.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtMatKhau.setBackground(new Color(250, 250, 250));
        txtMatKhau.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Checkbox ghi nhớ
        chkGhiNho = new JCheckBox("Ghi nhớ mật khẩu");
        chkGhiNho.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        chkGhiNho.setBackground(Color.WHITE);
        chkGhiNho.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Label thông báo
        lblThongBao = new JLabel();
        lblThongBao.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblThongBao.setForeground(Color.RED);
        lblThongBao.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Thêm các component vào panel
        pnlContent.add(lblUsername);
        pnlContent.add(Box.createVerticalStrut(5));
        pnlContent.add(txtTenDangNhap);
        pnlContent.add(Box.createVerticalStrut(20));
        pnlContent.add(lblPassword);
        pnlContent.add(Box.createVerticalStrut(5));
        pnlContent.add(txtMatKhau);
        pnlContent.add(Box.createVerticalStrut(15));
        pnlContent.add(chkGhiNho);
        pnlContent.add(Box.createVerticalStrut(10));
        pnlContent.add(lblThongBao);
        pnlContent.add(Box.createVerticalStrut(20));

        // Panel button
        JPanel pnlButtons = new JPanel();
        pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.X_AXIS));
        pnlButtons.setBackground(Color.WHITE);
        
        btnDangNhap = new JButton("Đăng Nhập");
        btnDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDangNhap.setBackground(new Color(25, 100, 200));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFocusPainted(false);
        btnDangNhap.setPreferredSize(new Dimension(130, 40));
        btnDangNhap.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDangNhap.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnDangNhap.addActionListener(e -> handleLogin());

        btnThoat = new JButton("Thoát");
        btnThoat.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnThoat.setBackground(new Color(150, 150, 150));
        btnThoat.setForeground(Color.WHITE);
        btnThoat.setFocusPainted(false);
        btnThoat.setPreferredSize(new Dimension(130, 40));
        btnThoat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnThoat.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnThoat.addActionListener(e -> handleExit());

        pnlButtons.add(btnDangNhap);
        pnlButtons.add(Box.createHorizontalStrut(20));
        pnlButtons.add(btnThoat);
        
        pnlContent.add(pnlButtons);
        pnlContent.add(Box.createVerticalGlue());

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
            lblThongBao.setForeground(new Color(200, 0, 0));
            return;
        }

        // Gọi service để kiểm tra
        adminHienTai = service.authenticateAdmin(tenDangNhap, matKhau);

        if (adminHienTai != null) {
            // Đăng nhập thành công
            lblThongBao.setText("Đăng nhập thành công! Chào " + adminHienTai.getNhanVien().getTenNV());
            lblThongBao.setForeground(new Color(0, 150, 0));
            
            // Delay một chút rồi mở form chính
            Timer timer = new Timer(1500, e -> openMainForm());
            timer.setRepeats(false);
            timer.start();
        } else {
            // Đăng nhập thất bại
            lblThongBao.setText("Sai tên đăng nhập hoặc mật khẩu!");
            lblThongBao.setForeground(new Color(200, 0, 0));
            txtMatKhau.setText("");
            txtMatKhau.requestFocus();
        }
    }

    /**
     * Mở form chính sau khi đăng nhập thành công
     */
    private void openMainForm() {
        JOptionPane.showMessageDialog(this, 
            "Chào mừng " + adminHienTai.getNhanVien().getTenNV() + "\nVai trò: " + adminHienTai.getVaiTro(),
            "Đăng nhập thành công", 
            JOptionPane.INFORMATION_MESSAGE);
        
        try {
            // Mở form menu admin
            FrmAdminMenu frmAdminMenu = new FrmAdminMenu(adminHienTai);
            frmAdminMenu.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        
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
