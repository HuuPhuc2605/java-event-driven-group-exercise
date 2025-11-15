package iuh.fit.se.nhom10.view;

import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.service.TaiKhoanNhanVienService;
import iuh.fit.se.nhom10.util.ButtonStyle;
import iuh.fit.se.nhom10.util.ColorPalette;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.sql.SQLException;
import java.util.prefs.Preferences;

/**
 * Form đăng nhập admin - Thiết kế chuyên nghiệp
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
    
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASSWORD = "password";
    private static final String PREF_REMEMBER = "remember";
    private Preferences prefs = Preferences.userNodeForPackage(FrmDangNhap.class);

    public FrmDangNhap() throws SQLException {
        service = new TaiKhoanNhanVienService();
        setupUI();
        loadSavedCredentials();
    }

    /**
     * Thiết lập giao diện chuyên nghiệp
     */
    private void setupUI() {
        setTitle("Đăng Nhập Hệ Thống Quản Lý Rạp Phim");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(ColorPalette.BACKGROUND_MAIN);

        // Panel chính
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BorderLayout(0, 0));
        pnlMain.setBackground(ColorPalette.BACKGROUND_MAIN);

        JPanel pnlHeader = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, ColorPalette.HEADER_GRADIENT_TOP, 0, getHeight(), ColorPalette.HEADER_GRADIENT_BOTTOM);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        pnlHeader.setPreferredSize(new Dimension(600, 120));
        
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE, Font.BOLD));
        lblTitle.setForeground(ColorPalette.TEXT_HEADER_TITLE);
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel lblSubtitle = new JLabel("Quản Lý Rạp Chiếu Phim");
        lblSubtitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE, Font.PLAIN));
        lblSubtitle.setForeground(ColorPalette.TEXT_HEADER_SUBTITLE);
        lblSubtitle.setHorizontalAlignment(JLabel.CENTER);
        
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
        pnlHeader.add(Box.createVerticalStrut(20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlHeader.add(lblTitle);
        pnlHeader.add(Box.createVerticalStrut(5));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlHeader.add(lblSubtitle);
        pnlHeader.add(Box.createVerticalStrut(15));

        // Panel nội dung
        JPanel pnlContent = new JPanel();
        pnlContent.setLayout(new BoxLayout(pnlContent, BoxLayout.Y_AXIS));
        pnlContent.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnlContent.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 60));

        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        lblUsername.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        lblUsername.setForeground(ColorPalette.TEXT_LABEL);
        lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtTenDangNhap = createStyledTextField();
        txtTenDangNhap.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtTenDangNhap.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            
            @Override
            public void keyPressed(KeyEvent e) {}
            
            @Override
            public void keyReleased(KeyEvent e) {
                handleUsernameChange();
            }
        });

        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        lblPassword.setForeground(ColorPalette.TEXT_LABEL);
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtMatKhau = new JPasswordField();
        txtMatKhau.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtMatKhau.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 2),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        txtMatKhau.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtMatKhau.setBackground(ColorPalette.BACKGROUND_INPUT);
        txtMatKhau.setForeground(ColorPalette.TEXT_BODY);
        txtMatKhau.setCaretColor(ColorPalette.TEXT_BODY);
        txtMatKhau.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        addFocusBorderListener(txtMatKhau);

        chkGhiNho = new JCheckBox("Ghi nhớ mật khẩu");
        chkGhiNho.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.PLAIN));
        chkGhiNho.setBackground(ColorPalette.BACKGROUND_CONTENT);
        chkGhiNho.setForeground(ColorPalette.TEXT_BODY);
        chkGhiNho.setFocusPainted(false);
        chkGhiNho.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblThongBao = new JLabel();
        lblThongBao.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.PLAIN));
        lblThongBao.setForeground(ColorPalette.STATUS_ERROR);
        lblThongBao.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Thêm các component vào panel
        pnlContent.add(lblUsername);
        pnlContent.add(Box.createVerticalStrut(8));
        pnlContent.add(txtTenDangNhap);
        pnlContent.add(Box.createVerticalStrut(25));
        pnlContent.add(lblPassword);
        pnlContent.add(Box.createVerticalStrut(8));
        pnlContent.add(txtMatKhau);
        pnlContent.add(Box.createVerticalStrut(15));
        pnlContent.add(chkGhiNho);
        pnlContent.add(Box.createVerticalStrut(12));
        pnlContent.add(lblThongBao);
        pnlContent.add(Box.createVerticalStrut(25));

        JPanel pnlButtons = new JPanel();
        pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.X_AXIS));
        pnlButtons.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnlButtons.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnDangNhap = new JButton("Đăng Nhập");
        ButtonStyle.stylePrimaryButton(btnDangNhap);
        btnDangNhap.addActionListener(e -> handleLogin());

        btnThoat = new JButton("Thoát");
        ButtonStyle.styleSecondaryButton(btnThoat);
        btnThoat.addActionListener(e -> handleExit());

        pnlButtons.add(btnDangNhap);
        pnlButtons.add(Box.createHorizontalStrut(15));
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
     * Tạo TextField với styling chuyên nghiệp
     */
    private JTextField createStyledTextField() {
        JTextField txt = new JTextField();
        txt.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 2),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txt.setBackground(ColorPalette.BACKGROUND_INPUT);
        txt.setForeground(ColorPalette.TEXT_BODY);
        txt.setCaretColor(ColorPalette.TEXT_BODY);
        
        addFocusBorderListener(txt);
        return txt;
    }
    
    /**
     * Thêm focus border listener để hiệu ứng focus
     */
    private void addFocusBorderListener(JTextField txt) {
        txt.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColorPalette.BORDER_FOCUS, 2),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 2),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)
                ));
            }
        });
    }

    private void loadSavedCredentials() {
        boolean remember = prefs.getBoolean(PREF_REMEMBER, false);
        if (remember) {
            String savedUsername = prefs.get(PREF_USERNAME, "");
            String savedPassword = prefs.get(PREF_PASSWORD, "");
            
            if (!savedUsername.isEmpty() && !savedPassword.isEmpty()) {
                txtTenDangNhap.setText(savedUsername);
                txtMatKhau.setText(savedPassword);
                chkGhiNho.setSelected(true);
                lblThongBao.setText("Thông tin đã được khôi phục");
                lblThongBao.setForeground(ColorPalette.STATUS_INFO);
            }
        }
    }

    private void saveCredentials() {
        if (chkGhiNho.isSelected()) {
            String tenDangNhap = txtTenDangNhap.getText().trim();
            String matKhau = new String(txtMatKhau.getPassword());
            
            prefs.put(PREF_USERNAME, tenDangNhap);
            prefs.put(PREF_PASSWORD, matKhau);
            prefs.putBoolean(PREF_REMEMBER, true);
        } else {
            // Nếu checkbox không được tích, xóa thông tin đã lưu
            prefs.remove(PREF_USERNAME);
            prefs.remove(PREF_PASSWORD);
            prefs.putBoolean(PREF_REMEMBER, false);
        }
    }

    /**
     * Handle username change - autocomplete password
     * Khi người dùng nhập username, tự động lấy mật khẩu từ CSDL và gợi ý
     */
    private void handleUsernameChange() {
        String tenDangNhap = txtTenDangNhap.getText().trim();
        
        if (!chkGhiNho.isSelected()) {
            return;
        }
        
        // Nếu username rỗng, xóa password
        if (tenDangNhap.isEmpty()) {
            txtMatKhau.setText("");
            return;
        }
        
        // Lấy tài khoản từ CSDL dựa trên username
        TaiKhoanNhanVien taiKhoan = service.getTaiKhoanByUsername(tenDangNhap);
        
        if (taiKhoan != null) {
            // Nếu tìm thấy, tự động điền mật khẩu
            txtMatKhau.setText(taiKhoan.getMatKhau());
            lblThongBao.setText("Mật khẩu đã được gợi ý");
            lblThongBao.setForeground(ColorPalette.STATUS_INFO);
        } else {
            // Nếu không tìm thấy, xóa password field
            txtMatKhau.setText("");
            lblThongBao.setText("");
        }
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
            lblThongBao.setForeground(ColorPalette.STATUS_ERROR);
            return;
        }

        // Gọi service để kiểm tra
        adminHienTai = service.authenticateAdmin(tenDangNhap, matKhau);

        if (adminHienTai != null) {
            saveCredentials();
            
            // Đăng nhập thành công
            lblThongBao.setText("✓ Đăng nhập thành công! Chào " + adminHienTai.getNhanVien().getTenNV());
            lblThongBao.setForeground(ColorPalette.STATUS_SUCCESS);
            
            // Delay một chút rồi mở form chính
            Timer timer = new Timer(1500, e -> openMainForm());
            timer.setRepeats(false);
            timer.start();
        } else {
            // Đăng nhập thất bại
            lblThongBao.setText("✗ Sai tên đăng nhập hoặc mật khẩu!");
            lblThongBao.setForeground(ColorPalette.STATUS_ERROR);
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
