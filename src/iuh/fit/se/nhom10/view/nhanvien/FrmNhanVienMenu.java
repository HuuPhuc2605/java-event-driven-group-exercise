package iuh.fit.se.nhom10.view.nhanvien;

import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.util.ColorPalette;
import iuh.fit.se.nhom10.view.FrmDangNhap;
import iuh.fit.se.nhom10.view.nhanvien.nvFrmHoaDonPanel;
import iuh.fit.se.nhom10.view.nhanvien.nvFrmQuanLyKhachHangPanel;
import iuh.fit.se.nhom10.view.nhanvien.nvFrmThanhToanPanel;
import iuh.fit.se.nhom10.view.nhanvien.nvFrmXemPhimPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Menu chính cho nhân viên bán vé
 * Chỉ hiển thị các chức năng được phép: Bán vé, Quản lý khách hàng, Hóa đơn, Thanh toán, Xem phim
 */
public class FrmNhanVienMenu extends JFrame {
    private TaiKhoanNhanVien nhanVienHienTai;
    private JLabel lblDateTime;
    private JPanel pnlContentArea;
    private JLabel lblCurrentModule;
    private JButton selectedButton;

    public FrmNhanVienMenu(TaiKhoanNhanVien nhanVien) {
        this.nhanVienHienTai = nhanVien;
        setupUI();
        startDateTimeUpdater();
    }

    private void setupUI() {
        setTitle("Hệ Thống Bán Vé - Nhân Viên " + nhanVienHienTai.getNhanVien().getTenNV());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BorderLayout(0, 0));
        pnlMain.setBackground(ColorPalette.BACKGROUND_MAIN);

        JPanel pnlHeader = createHeaderPanel();
        JPanel pnlContainer = createMainContainer();
        JPanel pnlFooter = createFooterPanel();

        pnlMain.add(pnlHeader, BorderLayout.NORTH);
        pnlMain.add(pnlContainer, BorderLayout.CENTER);
        pnlMain.add(pnlFooter, BorderLayout.SOUTH);

        add(pnlMain);
    }

    /**
     * Tạo header với gradient
     */
    private JPanel createHeaderPanel() {
        JPanel pnlHeader = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                    0, 0, ColorPalette.PRIMARY, 
                    getWidth(), 0, ColorPalette.PRIMARY_DARK
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        pnlHeader.setPreferredSize(new Dimension(1400, 80));
        pnlHeader.setLayout(new BorderLayout());
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("HỆ THỐNG BÁN VÉ RẠP CHIẾU PHIM");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE, Font.BOLD));
        lblTitle.setForeground(ColorPalette.TEXT_HEADER_TITLE);

        JPanel pnlAdminInfo = new JPanel();
        pnlAdminInfo.setOpaque(false);
        pnlAdminInfo.setLayout(new BoxLayout(pnlAdminInfo, BoxLayout.Y_AXIS));

        JLabel lblAdminName = new JLabel(nhanVienHienTai.getNhanVien().getTenNV());
        lblAdminName.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE, Font.BOLD));
        lblAdminName.setForeground(ColorPalette.TEXT_HEADER_TITLE);

        JLabel lblAdminRole = new JLabel("Vai trò: " + nhanVienHienTai.getVaiTro());
        lblAdminRole.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        lblAdminRole.setForeground(ColorPalette.TEXT_HEADER_SUBTITLE);

        pnlAdminInfo.add(lblAdminName);
        pnlAdminInfo.add(lblAdminRole);

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(pnlAdminInfo, BorderLayout.EAST);

        return pnlHeader;
    }

    /**
     * Tạo container chính với sidebar + content area
     */
    private JPanel createMainContainer() {
        JPanel pnlContainer = new JPanel(new BorderLayout(10, 0));
        pnlContainer.setBackground(ColorPalette.BACKGROUND_MAIN);
        pnlContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel pnlSidebar = createSidebarPanel();
        pnlContentArea = createContentPanel();

        pnlContainer.add(pnlSidebar, BorderLayout.WEST);
        pnlContainer.add(pnlContentArea, BorderLayout.CENTER);

        return pnlContainer;
    }

    /**
     * Tạo sidebar menu bên trái - Chỉ hiển thị menu cho nhân viên
     */
    private JPanel createSidebarPanel() {
        JPanel pnlSidebar = new JPanel();
        pnlSidebar.setLayout(new BoxLayout(pnlSidebar, BoxLayout.Y_AXIS));
        pnlSidebar.setBackground(ColorPalette.BACKGROUND_MAIN);
        pnlSidebar.setPreferredSize(new Dimension(250, 0));

        JPanel pnlBrand = new JPanel();
        pnlBrand.setLayout(new BoxLayout(pnlBrand, BoxLayout.Y_AXIS));
        pnlBrand.setBackground(ColorPalette.PRIMARY);
        pnlBrand.setBorder(new EmptyBorder(20, 16, 20, 16));
        pnlBrand.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblBrandIcon = new JLabel("🎬");
        lblBrandIcon.setFont(ColorPalette.getFont(28, Font.BOLD));
        lblBrandIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblBrandName = new JLabel("CinemaHub");
        lblBrandName.setFont(ColorPalette.getFont(18, Font.BOLD));
        lblBrandName.setForeground(Color.WHITE);
        lblBrandName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblBrandSubtitle = new JLabel("Nhân Viên");
        lblBrandSubtitle.setFont(ColorPalette.getFont(11, Font.PLAIN));
        lblBrandSubtitle.setForeground(new Color(255, 255, 255, 200));
        lblBrandSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlBrand.add(lblBrandIcon);
        pnlBrand.add(Box.createVerticalStrut(8));
        pnlBrand.add(lblBrandName);
        pnlBrand.add(Box.createVerticalStrut(4));
        pnlBrand.add(lblBrandSubtitle);

        JPanel pnlInner = new JPanel();
        pnlInner.setLayout(new BoxLayout(pnlInner, BoxLayout.Y_AXIS));
        pnlInner.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnlInner.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1),
                        new EmptyBorder(20, 12, 20, 12)
                )
        );
        pnlInner.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSectionTitle = new JLabel("CHỨC NĂNG CHÍNH");
        lblSectionTitle.setFont(ColorPalette.getFont(10, Font.BOLD));
        lblSectionTitle.setForeground(new Color(180, 180, 180));
        lblSectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlInner.add(lblSectionTitle);
        pnlInner.add(Box.createVerticalStrut(12));

        String[][] menuItems = {
                {"Xem Phim", "xem_phim", "🎞️"},
                {"Bán Vé", "ban_ve", "🎫"},
                {"Quản Lý KH", "khach_hang", "👥"},
                {"Hóa Đơn", "hoa_don", "📋"},
                {"Thanh Toán", "thanh_toan", "💳"}
        };

        for (String[] item : menuItems) {
            JButton btn = createEnhancedSidebarButton(item[0], item[1], item[2]);
            pnlInner.add(btn);
            pnlInner.add(Box.createVerticalStrut(8));
        }

        pnlInner.add(Box.createVerticalGlue());
        JLabel lblVersion = new JLabel("v1.0.0");
        lblVersion.setFont(ColorPalette.getFont(9, Font.PLAIN));
        lblVersion.setForeground(new Color(180, 180, 180));
        lblVersion.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlInner.add(lblVersion);

        pnlSidebar.add(pnlBrand);
        pnlSidebar.add(Box.createVerticalStrut(8));
        pnlSidebar.add(pnlInner);
        pnlSidebar.add(Box.createVerticalGlue());

        return pnlSidebar;
    }

    /**
     * Tạo nút sidebar cải tiến với icon
     */
    private JButton createEnhancedSidebarButton(String text, String action, String icon) {
        JButton btn = new JButton(icon + " " + text);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);

        Font baseFont = ColorPalette.getFont(13, Font.PLAIN);
        btn.setFont(baseFont);

        Dimension size = new Dimension(220, 48);
        btn.setMaximumSize(size);
        btn.setPreferredSize(size);
        btn.setMinimumSize(size);

        btn.setBackground(ColorPalette.BUTTON_PRIMARY_BG);
        btn.setForeground(ColorPalette.BUTTON_PRIMARY_TEXT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setVerticalAlignment(SwingConstants.CENTER);
        btn.setIconTextGap(12);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(8, 16, 8, 12)
        ));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn != selectedButton) {
                    btn.setBackground(ColorPalette.BUTTON_PRIMARY_BG_HOVER);
                    btn.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(ColorPalette.PRIMARY, 2),
                            new EmptyBorder(8, 16, 8, 12)
                    ));
                    btn.repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn != selectedButton) {
                    btn.setBackground(ColorPalette.BUTTON_PRIMARY_BG);
                    btn.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                            new EmptyBorder(8, 16, 8, 12)
                    ));
                    btn.repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                btn.setBackground(ColorPalette.BUTTON_PRIMARY_BG_PRESS);
                btn.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (btn == selectedButton) {
                    btn.setBackground(ColorPalette.PRIMARY_DARK);
                    btn.setForeground(Color.WHITE);
                } else {
                    btn.setBackground(ColorPalette.BUTTON_PRIMARY_BG_HOVER);
                    btn.setForeground(ColorPalette.BUTTON_PRIMARY_TEXT);
                }
                btn.repaint();
            }
        });

        btn.addActionListener(e -> {
            if (selectedButton != null && selectedButton != btn) {
                selectedButton.setBackground(ColorPalette.BUTTON_PRIMARY_BG);
                selectedButton.setForeground(ColorPalette.BUTTON_PRIMARY_TEXT);
                selectedButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                        new EmptyBorder(8, 16, 8, 12)
                ));
                selectedButton.repaint();
            }
            selectedButton = btn;
            btn.setBackground(ColorPalette.PRIMARY_DARK);
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColorPalette.PRIMARY, 2),
                    new EmptyBorder(8, 16, 8, 12)
            ));
            btn.repaint();

            switchContent(action, text);
        });

        return btn;
    }

    /**
     * Tạo panel content chính
     */
    private JPanel createContentPanel() {
        JPanel pnlContent = new JPanel(new BorderLayout());
        pnlContent.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnlContent.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel pnlTitleBar = new JPanel();
        pnlTitleBar.setLayout(new BorderLayout());
        pnlTitleBar.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnlTitleBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ColorPalette.BORDER_LIGHT));
        pnlTitleBar.setBorder(new EmptyBorder(0, 0, 15, 0));

        lblCurrentModule = new JLabel("Xem Phim");
        lblCurrentModule.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE + 4, Font.BOLD));
        lblCurrentModule.setForeground(ColorPalette.TEXT_LABEL);
        pnlTitleBar.add(lblCurrentModule, BorderLayout.WEST);

        pnlContent.add(pnlTitleBar, BorderLayout.NORTH);

        JPanel pnlWelcome = createWelcomePanel();
        pnlContent.add(pnlWelcome, BorderLayout.CENTER);

        return pnlContent;
    }

    /**
     * Tạo panel chào mừng
     */
    private JPanel createWelcomePanel() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnl.setAlignmentY(Component.CENTER_ALIGNMENT);

        JPanel pnlCard = new JPanel();
        pnlCard.setLayout(new BoxLayout(pnlCard, BoxLayout.Y_AXIS));
        pnlCard.setBackground(Color.WHITE);
        pnlCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1),
                new EmptyBorder(40, 40, 40, 40)
        ));
        pnlCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlCard.setMaximumSize(new Dimension(600, 300));

        JLabel lblWelcomeIcon = new JLabel("🎬");
        lblWelcomeIcon.setFont(ColorPalette.getFont(48, Font.BOLD));
        lblWelcomeIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblWelcome = new JLabel("Chào mừng " + nhanVienHienTai.getNhanVien().getTenNV());
        lblWelcome.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE, Font.BOLD));
        lblWelcome.setForeground(ColorPalette.PRIMARY);
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblInfo = new JLabel("Chọn chức năng từ menu bên trái để bắt đầu");
        lblInfo.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE, Font.PLAIN));
        lblInfo.setForeground(ColorPalette.TEXT_PLACEHOLDER);
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblRole = new JLabel("Vai trò: " + nhanVienHienTai.getVaiTro());
        lblRole.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        lblRole.setForeground(new Color(150, 150, 150));
        lblRole.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlCard.add(lblWelcomeIcon);
        pnlCard.add(Box.createVerticalStrut(15));
        pnlCard.add(lblWelcome);
        pnlCard.add(Box.createVerticalStrut(10));
        pnlCard.add(lblInfo);
        pnlCard.add(Box.createVerticalStrut(15));
        pnlCard.add(lblRole);

        pnl.add(Box.createVerticalGlue());
        pnl.add(pnlCard);
        pnl.add(Box.createVerticalGlue());

        return pnl;
    }

    /**
     * Chuyển đổi nội dung center
     */
    private void switchContent(String action, String moduleTitle) {
        lblCurrentModule.setText(moduleTitle);
        
        pnlContentArea.removeAll();
        
        JPanel pnlTitleBar = new JPanel();
        pnlTitleBar.setLayout(new BorderLayout());
        pnlTitleBar.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnlTitleBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ColorPalette.BORDER_LIGHT));
        pnlTitleBar.setBorder(new EmptyBorder(0, 0, 15, 0));

        lblCurrentModule.setText(moduleTitle);
        pnlTitleBar.add(lblCurrentModule, BorderLayout.WEST);
        
        pnlContentArea.setLayout(new BorderLayout());
        pnlContentArea.add(pnlTitleBar, BorderLayout.NORTH);
        
        JPanel pnlContentWrapper = new JPanel(new BorderLayout());
        pnlContentWrapper.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnlContentWrapper.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        switch (action) {
            case "xem_phim":
                try {
                    nvFrmXemPhimPanel pnlXemPhim = new nvFrmXemPhimPanel(nhanVienHienTai);
                    pnlContentWrapper.add(pnlXemPhim, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel lblError = new JLabel("Lỗi tải trang xem phim: " + e.getMessage());
                    lblError.setForeground(ColorPalette.STATUS_ERROR);
                    pnlContentWrapper.add(lblError, BorderLayout.CENTER);
                }
                break;
            case "ban_ve":
                try {
                    nvFrmThanhToanPanel pnlBanVe = new nvFrmThanhToanPanel(nhanVienHienTai);
                    pnlContentWrapper.add(pnlBanVe, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel lblError = new JLabel("Lỗi tải trang bán vé: " + e.getMessage());
                    lblError.setForeground(ColorPalette.STATUS_ERROR);
                    pnlContentWrapper.add(lblError, BorderLayout.CENTER);
                }
                break;
            case "khach_hang":
                try {
                    nvFrmQuanLyKhachHangPanel pnlQuanLyKH = new nvFrmQuanLyKhachHangPanel(nhanVienHienTai);
                    pnlContentWrapper.add(pnlQuanLyKH, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel lblError = new JLabel("Lỗi tải trang quản lý khách hàng: " + e.getMessage());
                    lblError.setForeground(ColorPalette.STATUS_ERROR);
                    pnlContentWrapper.add(lblError, BorderLayout.CENTER);
                }
                break;
            case "hoa_don":
                try {
                    nvFrmHoaDonPanel pnlHoaDon = new nvFrmHoaDonPanel(nhanVienHienTai);
                    pnlContentWrapper.add(pnlHoaDon, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel lblError = new JLabel("Lỗi tải trang hóa đơn: " + e.getMessage());
                    lblError.setForeground(ColorPalette.STATUS_ERROR);
                    pnlContentWrapper.add(lblError, BorderLayout.CENTER);
                }
                break;
            case "thanh_toan":
                try {
                    nvFrmThanhToanPanel pnlThanhToan = new nvFrmThanhToanPanel(nhanVienHienTai);
                    pnlContentWrapper.add(pnlThanhToan, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel lblError = new JLabel("Lỗi tải trang thanh toán: " + e.getMessage());
                    lblError.setForeground(ColorPalette.STATUS_ERROR);
                    pnlContentWrapper.add(lblError, BorderLayout.CENTER);
                }
                break;
            default:
                JLabel lblDev = new JLabel("Chức năng " + moduleTitle + " đang phát triển");
                lblDev.setForeground(ColorPalette.TEXT_PLACEHOLDER);
                pnlContentWrapper.add(lblDev, BorderLayout.CENTER);
                break;
        }
        
        pnlContentArea.add(pnlContentWrapper, BorderLayout.CENTER);
        pnlContentArea.revalidate();
        pnlContentArea.repaint();
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

        JButton btnLogout = new JButton("Đăng Xuất");
        btnLogout.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        btnLogout.setBackground(ColorPalette.BUTTON_DANGER_BG);
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setContentAreaFilled(true);
        btnLogout.setOpaque(true);
        btnLogout.setBorderPainted(false);
        btnLogout.setPreferredSize(new Dimension(100, 40));
        btnLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogout.setBackground(ColorPalette.BUTTON_DANGER_BG_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnLogout.setBackground(ColorPalette.BUTTON_DANGER_BG);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                btnLogout.setBackground(ColorPalette.BUTTON_DANGER_BG_PRESS);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                btnLogout.setBackground(ColorPalette.BUTTON_DANGER_BG);
            }
        });
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
     * Xử lý đăng xuất
     */
    private void handleLogout() {
        int result = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn đăng xuất?",
            "Xác nhận đăng xuất",
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            try {
                this.dispose();
                new FrmDangNhap().setVisible(true);
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
        lblDateTime.setText("\u23F0 " + now.format(formatter));
    }

    public TaiKhoanNhanVien getNhanVienHienTai() {
        return nhanVienHienTai;
    }
}
