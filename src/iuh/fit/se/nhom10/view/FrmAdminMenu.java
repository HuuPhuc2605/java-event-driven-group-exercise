package iuh.fit.se.nhom10.view;

import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.model.Phim;
import iuh.fit.se.nhom10.service.PhimService;
import iuh.fit.se.nhom10.util.ColorPalette;
import iuh.fit.se.nhom10.util.ButtonStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Menu admin hướng đối tượng - Layout split với sidebar + content area
 */
public class FrmAdminMenu extends JFrame {
    private TaiKhoanNhanVien adminHienTai;
    private JLabel lblDateTime;
    private JPanel pnlContentArea;
    private JLabel lblCurrentModule;
    private JPanel pnlDashboard;
    private JButton selectedButton;

    public FrmAdminMenu(TaiKhoanNhanVien admin) {
        this.adminHienTai = admin;
        setupUI();
        startDateTimeUpdater();
    }

    private void setupUI() {
        setTitle("Hệ Thống Quản Lý Rạp Chiếu Phim - " + adminHienTai.getNhanVien().getTenNV());
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

        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ RẠP CHIẾU PHIM");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE, Font.BOLD));
        lblTitle.setForeground(ColorPalette.TEXT_HEADER_TITLE);

        JPanel pnlAdminInfo = new JPanel();
        pnlAdminInfo.setOpaque(false);
        pnlAdminInfo.setLayout(new BoxLayout(pnlAdminInfo, BoxLayout.Y_AXIS));

        JLabel lblAdminName = new JLabel("\uD83D\uDC64 " + adminHienTai.getNhanVien().getTenNV());
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
     * Tạo sidebar menu bên trái
     */
    private JPanel createSidebarPanel() {
        JPanel pnlSidebar = new JPanel();
        pnlSidebar.setLayout(new BoxLayout(pnlSidebar, BoxLayout.Y_AXIS));
        pnlSidebar.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnlSidebar.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        pnlSidebar.setPreferredSize(new Dimension(200, 0));
        pnlSidebar.setBorder(new EmptyBorder(15, 10, 15, 10));

        JLabel lblSidebarTitle = new JLabel("MENU CHÍNH");
        lblSidebarTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        lblSidebarTitle.setForeground(ColorPalette.TEXT_LABEL);
        pnlSidebar.add(lblSidebarTitle);
        pnlSidebar.add(Box.createVerticalStrut(15));

        String[][] menuItems = {
            {"Trang Chủ", "dashboard"},
            {"Quản Lý Phim", "phim"},
            {"Quản Lý Phòng Chiếu", "phong_chieu"},
            {"Lịch Chiếu", "lich_chieu"},
            {"Nhân Viên", "nhan_vien"},
            {"Khách Hàng", "khach_hang"},
            {"Hóa Đơn", "hoa_don"},
            {"Khuyến Mãi", "khuyen_mai"},
            {"Báo Cáo", "bao_cao"}
        };

        for (String[] item : menuItems) {
            JButton btn = createSidebarButton(item[0], item[1]);
            pnlSidebar.add(btn);
            pnlSidebar.add(Box.createVerticalStrut(8));
        }

        pnlSidebar.add(Box.createVerticalGlue());
        
        return pnlSidebar;
    }

    /**
     * Tạo nút sidebar với ButtonStyle
     * Added tracking for selected button to highlight current menu item
     */
    private JButton createSidebarButton(String text, String action) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setMinimumSize(new Dimension(180, 40));
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btn.setBackground(ColorPalette.BUTTON_PRIMARY_BG);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setVerticalAlignment(SwingConstants.CENTER);
        btn.setBorderPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn != selectedButton) {
                    btn.setBackground(ColorPalette.BUTTON_PRIMARY_BG_HOVER);
                }
                btn.setForeground(Color.WHITE);
                btn.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn == selectedButton) {
                    btn.setBackground(ColorPalette.PRIMARY_DARK);
                } else {
                    btn.setBackground(ColorPalette.BUTTON_PRIMARY_BG);
                }
                btn.setForeground(Color.WHITE);
                btn.repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                btn.setBackground(ColorPalette.BUTTON_PRIMARY_BG_PRESS);
                btn.setForeground(Color.WHITE);
                btn.repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                btn.setBackground(ColorPalette.PRIMARY_DARK);
                btn.setForeground(Color.WHITE);
                btn.repaint();
            }
        });

        btn.addActionListener(e -> {
            if (selectedButton != null) {
                selectedButton.setBackground(ColorPalette.BUTTON_PRIMARY_BG);
            }
            selectedButton = btn;
            btn.setBackground(ColorPalette.PRIMARY_DARK);
            btn.repaint();
            switchContent(action, text);
        });
        
        return btn;
    }

    /**
     * Tạo panel content chính (hiển thị thông tin tổng quan)
     */
    private JPanel createContentPanel() {
        JPanel pnlContent = new JPanel(new BorderLayout());
        pnlContent.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnlContent.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel tiêu đề
        JPanel pnlTitleBar = new JPanel();
        pnlTitleBar.setLayout(new BorderLayout());
        pnlTitleBar.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnlTitleBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ColorPalette.BORDER_LIGHT));
        pnlTitleBar.setBorder(new EmptyBorder(0, 0, 15, 0));

        lblCurrentModule = new JLabel("Bảng Điều Khiển");
        lblCurrentModule.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE + 4, Font.BOLD));
        lblCurrentModule.setForeground(ColorPalette.TEXT_LABEL);
        pnlTitleBar.add(lblCurrentModule, BorderLayout.WEST);

        pnlContent.add(pnlTitleBar, BorderLayout.NORTH);

        // Panel chứa content chính - Use CardLayout to switch between views
        pnlDashboard = createDashboardPanel();
        pnlContent.add(pnlDashboard, BorderLayout.CENTER);

        return pnlContent;
    }

    /**
     * Tạo dashboard hiển thị thông tin tổng quan
     */
    private JPanel createDashboardPanel() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new GridLayout(2, 3, 15, 15));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(10, 0, 10, 0));

        try {
            // Widget 1: Phim đang chiếu hôm nay
            pnl.add(createMovieShowcaseWidget());
            
            // Widget 2: Doanh thu hôm nay
            pnl.add(createRevenueWidget());
            
            // Widget 3: Số vé bán hôm nay
            pnl.add(createTicketsSoldWidget());
            
            // Widget 4: Tổng số phim
            pnl.add(createTotalMoviesWidget());
            
            // Widget 5: Thống kê nhanh
            pnl.add(createQuickStatsWidget());
            
            // Widget 6: Lịch sử gần đây
            pnl.add(createRecentActivityWidget());
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pnl;
    }

    /**
     * Widget hiển thị phim đang chiếu
     */
    private JPanel createMovieShowcaseWidget() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBackground(new Color(240, 248, 255));
        pnl.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 2));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("\ud83c\udfac Phim Đang Chiếu");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE + 2, Font.BOLD));
        lblTitle.setForeground(ColorPalette.PRIMARY);
        pnl.add(lblTitle);
        pnl.add(Box.createVerticalStrut(10));

        try {
            PhimService phimService = new PhimService();
            List<Phim> phims = phimService.getAllPhim();
            
            if (phims.isEmpty()) {
                JLabel lblNoData = new JLabel("Không có phim nào");
                lblNoData.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.ITALIC));
                lblNoData.setForeground(ColorPalette.TEXT_PLACEHOLDER);
                pnl.add(lblNoData);
            } else {
                for (int i = 0; i < Math.min(3, phims.size()); i++) {
                    JLabel lbl = new JLabel("• " + phims.get(i).getTenPhim());
                    lbl.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
                    lbl.setForeground(ColorPalette.TEXT_BODY);
                    pnl.add(lbl);
                    pnl.add(Box.createVerticalStrut(5));
                }
                
                if (phims.size() > 3) {
                    JLabel lblMore = new JLabel("...và " + (phims.size() - 3) + " phim khác");
                    lblMore.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.ITALIC));
                    lblMore.setForeground(ColorPalette.TEXT_PLACEHOLDER);
                    pnl.add(lblMore);
                }
            }
        } catch (Exception e) {
            JLabel lblError = new JLabel("Lỗi tải dữ liệu");
            lblError.setForeground(ColorPalette.STATUS_ERROR);
            pnl.add(lblError);
        }

        pnl.add(Box.createVerticalGlue());
        return pnl;
    }

    /**
     * Widget hiển thị doanh thu hôm nay
     */
    private JPanel createRevenueWidget() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBackground(new Color(240, 255, 240));
        pnl.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 2));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("\ud83d\udcb0 Doanh Thu Hôm Nay");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE + 2, Font.BOLD));
        lblTitle.setForeground(ColorPalette.ACCENT);
        pnl.add(lblTitle);
        pnl.add(Box.createVerticalStrut(10));

        JLabel lblRevenue = new JLabel("0 VNĐ");
        lblRevenue.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE, Font.BOLD));
        lblRevenue.setForeground(ColorPalette.ACCENT);
        pnl.add(lblRevenue);
        pnl.add(Box.createVerticalStrut(5));

        JLabel lblStatus = new JLabel("Tính từ 00:00 - 23:59");
        lblStatus.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.ITALIC));
        lblStatus.setForeground(ColorPalette.TEXT_PLACEHOLDER);
        pnl.add(lblStatus);

        pnl.add(Box.createVerticalGlue());
        return pnl;
    }

    /**
     * Widget hiển thị số vé bán hôm nay
     */
    private JPanel createTicketsSoldWidget() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBackground(new Color(255, 248, 240));
        pnl.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 2));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("\ud83c\udf9f Vé Bán Hôm Nay");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE + 2, Font.BOLD));
        lblTitle.setForeground(new Color(255, 140, 0));
        pnl.add(lblTitle);
        pnl.add(Box.createVerticalStrut(10));

        JLabel lblTickets = new JLabel("0");
        lblTickets.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE, Font.BOLD));
        lblTickets.setForeground(new Color(255, 140, 0));
        pnl.add(lblTickets);
        pnl.add(Box.createVerticalStrut(5));

        JLabel lblUnit = new JLabel("vé");
        lblUnit.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.ITALIC));
        lblUnit.setForeground(ColorPalette.TEXT_PLACEHOLDER);
        pnl.add(lblUnit);

        pnl.add(Box.createVerticalGlue());
        return pnl;
    }

    /**
     * Widget hiển thị tổng số phim
     */
    private JPanel createTotalMoviesWidget() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBackground(new Color(245, 240, 255));
        pnl.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 2));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("\ud83d\udcda Tổng Số Phim");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE + 2, Font.BOLD));
        lblTitle.setForeground(ColorPalette.PRIMARY);
        pnl.add(lblTitle);
        pnl.add(Box.createVerticalStrut(10));

        try {
            PhimService phimService = new PhimService();
            int count = phimService.getAllPhim().size();
            
            JLabel lblCount = new JLabel(String.valueOf(count));
            lblCount.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE, Font.BOLD));
            lblCount.setForeground(ColorPalette.PRIMARY);
            pnl.add(lblCount);
        } catch (Exception e) {
            JLabel lblError = new JLabel("0");
            lblError.setForeground(ColorPalette.STATUS_ERROR);
            pnl.add(lblError);
        }

        pnl.add(Box.createVerticalGlue());
        return pnl;
    }

    /**
     * Widget thống kê nhanh
     */
    private JPanel createQuickStatsWidget() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBackground(new Color(255, 240, 245));
        pnl.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 2));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("\ud83d\udcc8 Thống Kê Nhanh");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE + 2, Font.BOLD));
        lblTitle.setForeground(ColorPalette.ACCENT);
        pnl.add(lblTitle);
        pnl.add(Box.createVerticalStrut(10));

        String[] stats = {"Phòng: 5", "Nhân viên: 10", "Thể loại: 8"};
        for (String stat : stats) {
            JLabel lbl = new JLabel("• " + stat);
            lbl.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
            lbl.setForeground(ColorPalette.TEXT_BODY);
            pnl.add(lbl);
            pnl.add(Box.createVerticalStrut(5));
        }

        pnl.add(Box.createVerticalGlue());
        return pnl;
    }

    /**
     * Widget lịch sử hoạt động
     */
    private JPanel createRecentActivityWidget() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBackground(new Color(240, 255, 255));
        pnl.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 2));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("\ud83d\udccb Hoạt Động Gần Đây");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE + 2, Font.BOLD));
        lblTitle.setForeground(ColorPalette.STATUS_INFO);
        pnl.add(lblTitle);
        pnl.add(Box.createVerticalStrut(10));

        String[] activities = {
            "✓ Thêm phim: Avengers",
            "✓ Cập nhật lịch chiếu",
            "✓ Bán vé: 25 vé"
        };
        for (String activity : activities) {
            JLabel lbl = new JLabel(activity);
            lbl.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
            lbl.setForeground(ColorPalette.TEXT_BODY);
            pnl.add(lbl);
            pnl.add(Box.createVerticalStrut(5));
        }

        pnl.add(Box.createVerticalGlue());
        return pnl;
    }

    /**
     * Chuyển đổi nội dung center
     * Enabled FrmQuanLyPhongChieuPanel loading
     */
    private void switchContent(String action, String moduleTitle) {
        lblCurrentModule.setText(moduleTitle);
        
        // Remove old content
        pnlContentArea.removeAll();
        
        // Create title bar
        JPanel pnlTitleBar = new JPanel();
        pnlTitleBar.setLayout(new BorderLayout());
        pnlTitleBar.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnlTitleBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ColorPalette.BORDER_LIGHT));
        pnlTitleBar.setBorder(new EmptyBorder(0, 0, 15, 0));

        lblCurrentModule.setText(moduleTitle);
        pnlTitleBar.add(lblCurrentModule, BorderLayout.WEST);
        
        pnlContentArea.add(pnlTitleBar, BorderLayout.NORTH);
        
        switch (action) {
            case "dashboard":
                // Hiển thị dashboard mặc định
                pnlDashboard = createDashboardPanel();
                pnlContentArea.add(pnlDashboard, BorderLayout.CENTER);
                break;
            case "phim":
                try {
                    // Load FrmQuanLyPhim as content panel
                    FrmQuanLyPhimPanel pnlQuanLyPhim = new FrmQuanLyPhimPanel(adminHienTai);
                    pnlContentArea.add(pnlQuanLyPhim, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel lblError = new JLabel("Lỗi tải trang quản lý phim: " + e.getMessage());
                    lblError.setForeground(ColorPalette.STATUS_ERROR);
                    pnlContentArea.add(lblError, BorderLayout.CENTER);
                }
                break;
            case "phong_chieu":
                try {
                    // Load FrmQuanLyPhongChieuPanel as content panel
                    FrmQuanLyPhongChieuPanel pnlQuanLyPhongChieu = new FrmQuanLyPhongChieuPanel(adminHienTai);
                    pnlContentArea.add(pnlQuanLyPhongChieu, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel lblError = new JLabel("Lỗi tải trang quản lý phòng chiếu: " + e.getMessage());
                    lblError.setForeground(ColorPalette.STATUS_ERROR);
                    pnlContentArea.add(lblError, BorderLayout.CENTER);
                }
                break;
            case "lich_chieu":
                try {
                    // Load FrmQuanLyPhongChieuPanel as content panel
                    FrmQuanLyLichChieuPanel pnlQuanLyLichChieuPanel = new FrmQuanLyLichChieuPanel(adminHienTai);
                    pnlContentArea.add(pnlQuanLyLichChieuPanel, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel lblError = new JLabel("Lỗi tải trang quản lý lịch chiếu: " + e.getMessage());
                    lblError.setForeground(ColorPalette.STATUS_ERROR);
                    pnlContentArea.add(lblError, BorderLayout.CENTER);
                }
                break;
            case "khuyen_mai":
                try {
                    FrmQuanLyKhuyenMaiPanel pnlQuanLyKhuyenMai = new FrmQuanLyKhuyenMaiPanel(adminHienTai);
                    pnlContentArea.add(pnlQuanLyKhuyenMai, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel lblError = new JLabel("Lỗi tải trang quản lý khuyến mãi: " + e.getMessage());
                    lblError.setForeground(ColorPalette.STATUS_ERROR);
                    pnlContentArea.add(lblError, BorderLayout.CENTER);
                }
                break;
            default:
                JLabel lblDev = new JLabel("Chức năng " + moduleTitle + " đang phát triển");
                lblDev.setForeground(ColorPalette.TEXT_PLACEHOLDER);
                pnlContentArea.add(lblDev, BorderLayout.CENTER);
                break;
        }
        
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
//        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        btnLogout.setContentAreaFilled(true);
//        btnLogout.setOpaque(true);
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
        lblDateTime.setText("\u23F0 " + now.format(formatter));
    }

    public TaiKhoanNhanVien getAdminHienTai() {
        return adminHienTai;
    }
}
