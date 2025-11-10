package iuh.fit.se.nhom10.view;

import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.util.ColorPalette;
import iuh.fit.se.nhom10.util.ButtonStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Form menu chính cho admin - Giao diện hướng đối tượng chuyên nghiệp
 * Sử dụng ColorPalette và ButtonStyle để quản lý giao diện thống nhất
 */
public class FrmAdminMenu extends JFrame {
    private TaiKhoanNhanVien adminHienTai;
    private JLabel lblDateTime;
    
    public FrmAdminMenu(TaiKhoanNhanVien admin) {
        this.adminHienTai = admin;
        setupUI();
        startDateTimeUpdater();
    }

    private void setupUI() {
        setTitle("Hệ Thống Quản Lý Rạp Chiếu Phim - " + adminHienTai.getNhanVien().getTenNV());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BorderLayout(0, 0));
        pnlMain.setBackground(ColorPalette.BACKGROUND_MAIN);

        // ====== HEADER ======
        JPanel pnlHeader = createHeaderPanel();
        
        // ====== SIDEBAR + CONTENT ======
        JPanel pnlContainer = new JPanel(new BorderLayout(10, 0));
        pnlContainer.setBackground(ColorPalette.BACKGROUND_MAIN);
        pnlContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel pnlSidebar = createSidebarPanel();
        JPanel pnlContent = createContentPanel();
        
        pnlContainer.add(pnlSidebar, BorderLayout.WEST);
        pnlContainer.add(pnlContent, BorderLayout.CENTER);
        
        // ====== FOOTER ======
        JPanel pnlFooter = createFooterPanel();

        // Thêm các panel vào main
        pnlMain.add(pnlHeader, BorderLayout.NORTH);
        pnlMain.add(pnlContainer, BorderLayout.CENTER);
        pnlMain.add(pnlFooter, BorderLayout.SOUTH);

        add(pnlMain);
    }

    /**
     * Tạo header panel với gradient và thông tin admin
     */
    private JPanel createHeaderPanel() {
        JPanel pnlHeader = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                    0, 0, ColorPalette.HEADER_GRADIENT_TOP, 
                    getWidth(), 0, ColorPalette.HEADER_GRADIENT_BOTTOM
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        pnlHeader.setPreferredSize(new Dimension(1200, 100));
        pnlHeader.setLayout(new BorderLayout());
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Tiêu đề
        JLabel lblTitle = new JLabel("🎬 HỆ THỐNG QUẢN LÝ RẠP CHIẾU PHIM");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE, Font.BOLD));
        lblTitle.setForeground(ColorPalette.TEXT_HEADER_TITLE);

        // Panel thông tin admin
        JPanel pnlAdminInfo = new JPanel();
        pnlAdminInfo.setOpaque(false);
        pnlAdminInfo.setLayout(new BoxLayout(pnlAdminInfo, BoxLayout.Y_AXIS));

        JLabel lblAdminName = new JLabel("👤 " + adminHienTai.getNhanVien().getTenNV());
        lblAdminName.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE, Font.BOLD));
        lblAdminName.setForeground(ColorPalette.TEXT_HEADER_TITLE);

        JLabel lblAdminRole = new JLabel("Vai trò: " + adminHienTai.getVaiTro());
        lblAdminRole.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        lblAdminRole.setForeground(ColorPalette.TEXT_HEADER_SUBTITLE);

        pnlAdminInfo.add(lblAdminName);
        pnlAdminInfo.add(lblAdminRole);

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(pnlAdminInfo, BorderLayout.EAST);

        return pnlHeader;
    }

    /**
     * Tạo sidebar với danh sách các module quản lý
     */
    private JPanel createSidebarPanel() {
        JPanel pnlSidebar = new JPanel();
        pnlSidebar.setLayout(new BoxLayout(pnlSidebar, BoxLayout.Y_AXIS));
        pnlSidebar.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnlSidebar.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        pnlSidebar.setPreferredSize(new Dimension(180, 0));
        pnlSidebar.setBorder(new EmptyBorder(15, 10, 15, 10));

        // Tiêu đề sidebar
        JLabel lblSidebarTitle = new JLabel("MENU CHÍNH");
        lblSidebarTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        lblSidebarTitle.setForeground(ColorPalette.TEXT_LABEL);
        pnlSidebar.add(lblSidebarTitle);
        pnlSidebar.add(Box.createVerticalStrut(15));

        String[] menuItems = {
            "📽️ Quản Lý Phim",
            "🎯 Lịch Chiếu",
            "👥 Nhân Viên",
            "🤝 Khách Hàng",
            "📄 Hóa Đơn",
            "🏷️ Khuyến Mãi",
            "📊 Báo Cáo"
        };

        for (String menuItem : menuItems) {
            JButton btn = createSidebarButton(menuItem);
            pnlSidebar.add(btn);
            pnlSidebar.add(Box.createVerticalStrut(8));
        }

        pnlSidebar.add(Box.createVerticalGlue());
        
        return pnlSidebar;
    }

    /**
     * Tạo nút sidebar với styling đẹp
     */
    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(160, 35));
        btn.setMinimumSize(new Dimension(160, 35));
        btn.setPreferredSize(new Dimension(160, 35));
        btn.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        btn.setBackground(ColorPalette.BACKGROUND_CONTENT);
        btn.setForeground(ColorPalette.TEXT_LABEL);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(5, 10, 5, 10));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(ColorPalette.PRIMARY_LIGHT);
                btn.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(ColorPalette.BACKGROUND_CONTENT);
                btn.setForeground(ColorPalette.TEXT_LABEL);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                btn.setBackground(ColorPalette.PRIMARY_DARK);
            }
        });

        btn.addActionListener(e -> handleMenuAction(text));
        
        return btn;
    }

    /**
     * Tạo panel nội dung với các menu button lớn
     */
    private JPanel createContentPanel() {
        JPanel pnlContent = new JPanel();
        pnlContent.setLayout(new GridLayout(3, 3, 20, 20));
        pnlContent.setBackground(ColorPalette.BACKGROUND_MAIN);
        pnlContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        Object[][] menuData = {
            {"📽️ Quản Lý Phim", "quan_ly_phim", ColorPalette.PRIMARY},
            {"🎬 Thêm Phim Mới", "them_phim", ColorPalette.PRIMARY},
            {"🎯 Lịch Chiếu", "lich_chieu", ColorPalette.ACCENT},
            {"👨‍💼 Quản Lý Nhân Viên", "nhan_vien", ColorPalette.PRIMARY},
            {"🤝 Quản Lý Khách Hàng", "khach_hang", ColorPalette.PRIMARY},
            {"📄 Quản Lý Hóa Đơn", "hoa_don", ColorPalette.PRIMARY},
            {"🏷️ Khuyến Mãi", "khuyen_mai", ColorPalette.ACCENT},
            {"📊 Báo Cáo Doanh Thu", "bao_cao", ColorPalette.ACCENT},
            {"⚙️ Cài Đặt Hệ Thống", "settings", ColorPalette.BUTTON_SETTINGS_BG}
        };

        for (Object[] item : menuData) {
            JButton btn = createContentButton((String) item[0], (String) item[1], (Color) item[2]);
            pnlContent.add(btn);
        }

        return pnlContent;
    }

    /**
     * Tạo nút content với icon, text và hover effects
     */
    private JButton createContentButton(String text, String action, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_BUTTON + 2, Font.BOLD));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createLineBorder(color, 2));
        btn.setVerticalAlignment(SwingConstants.CENTER);
        btn.setHorizontalAlignment(SwingConstants.CENTER);

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(ColorPalette.lighten(color, 0.15f));
                btn.setBorder(BorderFactory.createLineBorder(ColorPalette.lighten(color, 0.15f), 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
                btn.setBorder(BorderFactory.createLineBorder(color, 2));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                btn.setBackground(ColorPalette.lighten(color, -0.15f));
            }
        });

        btn.addActionListener(e -> handleContentButtonAction(action));
        
        return btn;
    }

    /**
     * Tạo footer panel
     */
    private JPanel createFooterPanel() {
        JPanel pnlFooter = new JPanel();
        pnlFooter.setLayout(new BorderLayout());
        pnlFooter.setBackground(ColorPalette.TEXT_LABEL);
        pnlFooter.setBorder(new EmptyBorder(10, 20, 10, 20));

        lblDateTime = new JLabel();
        lblDateTime.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.PLAIN));
        lblDateTime.setForeground(Color.WHITE);

        JButton btnLogout = ButtonStyle.createDangerButton("Đăng Xuất");
        btnLogout.addActionListener(e -> handleLogout());

        JLabel lblCopyright = new JLabel("© 2025 Nhóm 10 - Hệ Thống Quản Lý Rạp Chiếu Phim");
        lblCopyright.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.PLAIN));
        lblCopyright.setForeground(Color.WHITE);

        pnlFooter.add(lblDateTime, BorderLayout.WEST);
        pnlFooter.add(lblCopyright, BorderLayout.CENTER);
        pnlFooter.add(btnLogout, BorderLayout.EAST);

        return pnlFooter;
    }

    /**
     * Xử lý các hành động từ sidebar
     */
    private void handleMenuAction(String menuItem) {
        JOptionPane.showMessageDialog(this, 
            "Chức năng: " + menuItem + " đang phát triển", 
            "Thông báo", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Xử lý các hành động từ content buttons
     */
    private void handleContentButtonAction(String action) {
        try {
            switch (action) {
                case "them_phim":
                    new FrmQuanLyPhim(adminHienTai).setVisible(true);
                    break;
                case "quan_ly_phim":
                    new FrmQuanLyPhim(adminHienTai).setVisible(true);
                    break;
                case "lich_chieu":
                case "nhan_vien":
                case "khach_hang":
                case "hoa_don":
                case "khuyen_mai":
                case "bao_cao":
                case "settings":
                    JOptionPane.showMessageDialog(this, 
                        "Chức năng đang phát triển", 
                        "Thông báo", 
                        JOptionPane.INFORMATION_MESSAGE);
                    break;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Xử lý đăng xuất
     */
    private void handleLogout() {
        int result = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn đăng xuất?",
            "Xác nhận đăng xuất",
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            try {
                new FrmDangNhap().setVisible(true);
                this.dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Lỗi: " + e.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Cập nhật ngày giờ tự động
     */
    private void startDateTimeUpdater() {
        Timer timer = new Timer(1000, e -> updateDateTime());
        timer.start();
    }

    private void updateDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        lblDateTime.setText("⏰ " + now.format(formatter));
    }

    public TaiKhoanNhanVien getAdminHienTai() {
        return adminHienTai;
    }
}
