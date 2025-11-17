package iuh.fit.se.nhom10.view.admin;

import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.model.Phim;
import iuh.fit.se.nhom10.service.PhimService;
import iuh.fit.se.nhom10.service.DashboardService;
import iuh.fit.se.nhom10.util.ColorPalette;
import iuh.fit.se.nhom10.view.FrmDangNhap;
import iuh.fit.se.nhom10.util.ButtonStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
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
    private DashboardService dashboardService;

    public FrmAdminMenu(TaiKhoanNhanVien admin) {
        this.adminHienTai = admin;
        try {
            this.dashboardService = new DashboardService();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setupUI();
        startDateTimeUpdater();
    }
    private ImageIcon loadIcon(String fileName, int size) {
        try {
            Image img = new ImageIcon("src/icons/" + fileName).getImage();
            Image scaled = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            
            return new ImageIcon(scaled);
        } catch (Exception e) {
            System.out.println("Không tìm thấy icon: " + fileName);
            return null;
        }
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
        pnlHeader.setPreferredSize(new Dimension(1400, 95));
        pnlHeader.setLayout(new BorderLayout(20, 0));
        pnlHeader.setBorder(new EmptyBorder(15, 25, 15, 25));

        // Left side - Title
        JPanel pnlLeft = new JPanel();
        pnlLeft.setOpaque(false);
        pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
        
        JLabel lblSystem = new JLabel("HỆ THỐNG QUẢN LÝ RẠP CHIẾU PHIM");
        lblSystem.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE + 2, Font.BOLD));
        lblSystem.setForeground(Color.WHITE);
        
        JLabel lblSubtitle = new JLabel("Phần mềm quản lý và điều hành chuyên nghiệp");
        lblSubtitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.PLAIN));
        lblSubtitle.setForeground(new Color(200, 210, 230));
        
        pnlLeft.add(lblSystem);
        pnlLeft.add(Box.createVerticalStrut(3));
        pnlLeft.add(lblSubtitle);

        // Right side - Admin info
        JPanel pnlRight = new JPanel();
        pnlRight.setOpaque(false);
        pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.Y_AXIS));
        pnlRight.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel lblAdminLabel = new JLabel("NGƯỜI QUẢN TRỊ:");
        lblAdminLabel.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.PLAIN));
        lblAdminLabel.setForeground(new Color(200, 210, 230));
        lblAdminLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblAdminName = new JLabel(adminHienTai.getNhanVien().getTenNV());
        lblAdminName.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        lblAdminName.setForeground(Color.WHITE);
        lblAdminName.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblAdminRole = new JLabel("Vai trò: " + adminHienTai.getVaiTro());
        lblAdminRole.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.PLAIN));
        lblAdminRole.setForeground(new Color(200, 210, 230));
        lblAdminRole.setAlignmentX(Component.RIGHT_ALIGNMENT);

        pnlRight.add(lblAdminLabel);
        pnlRight.add(Box.createVerticalStrut(2));
        pnlRight.add(lblAdminName);
        pnlRight.add(Box.createVerticalStrut(2));
        pnlRight.add(lblAdminRole);

        pnlHeader.add(pnlLeft, BorderLayout.WEST);
        pnlHeader.add(pnlRight, BorderLayout.EAST);

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
     * Tạo sidebar menu bên trái - CẢI TIẾN
     * Enhanced sidebar with better visual hierarchy, rounded buttons, and improved spacing
     */
    private JPanel createSidebarPanel() {
        JPanel pnlSidebar = new JPanel();
        pnlSidebar.setLayout(new BoxLayout(pnlSidebar, BoxLayout.Y_AXIS));
        pnlSidebar.setBackground(new Color(25, 35, 65));
        pnlSidebar.setBorder(BorderFactory.createLineBorder(new Color(50, 70, 120), 1));
        pnlSidebar.setPreferredSize(new Dimension(240, 0));
        pnlSidebar.setBorder(new EmptyBorder(20, 12, 20, 12));

        // Logo/Brand area
        JPanel pnlBrand = new JPanel();
        pnlBrand.setOpaque(false);
        pnlBrand.setLayout(new BoxLayout(pnlBrand, BoxLayout.Y_AXIS));
        pnlBrand.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblBrandIcon = new JLabel("🎬");
        lblBrandIcon.setFont(new Font("Arial", Font.BOLD, 32));
        lblBrandIcon.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblBrandName = new JLabel("CINEMA PRO");
        lblBrandName.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblBrandName.setForeground(ColorPalette.ACCENT);
        lblBrandName.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        pnlBrand.add(lblBrandIcon);
        pnlBrand.add(Box.createVerticalStrut(5));
        pnlBrand.add(lblBrandName);
        
        pnlSidebar.add(pnlBrand);
        pnlSidebar.add(Box.createVerticalStrut(25));

        // Separator
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(50, 70, 120));
        separator.setMaximumSize(new Dimension(200, 1));
        pnlSidebar.add(separator);
        pnlSidebar.add(Box.createVerticalStrut(15));

        // Menu section label
        JLabel lblMenuTitle = new JLabel("MENU CHÍNH");
        lblMenuTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.BOLD));
        lblMenuTitle.setForeground(new Color(150, 160, 180));
        lblMenuTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlSidebar.add(lblMenuTitle);
        pnlSidebar.add(Box.createVerticalStrut(12));

        String[][] menuItems = {
            {"Trang Chủ", "dashboard", "dashboard.png"},
            {"Quản Lý Phim", "phim", "phim.png"},
            {"Phòng Chiếu", "phong_chieu", "phong_chieu.png"},
            {"Lịch Chiếu", "lich_chieu", "lich_chieu.png"},
            {"Nhân Viên", "nhan_vien", "nhan_vien.png"},
            {"Khách Hàng", "khach_hang", "khach_hang.png"},
            {"Hóa Đơn", "hoa_don", "hoa_don.png"},
            {"Khuyến Mãi", "khuyen_mai", "khuyen_mai.png"},
            {"Báo Cáo", "bao_cao", "bao_cao.png"}
        };

        for (String[] item : menuItems) {
            JButton btn = createModernSidebarButton(item[0], item[1], item[2]);
            pnlSidebar.add(btn);
            pnlSidebar.add(Box.createVerticalStrut(6));
        }

        pnlSidebar.add(Box.createVerticalGlue());
        
        // Footer info in sidebar
        JSeparator separatorBottom = new JSeparator();
        separatorBottom.setForeground(new Color(50, 70, 120));
        separatorBottom.setMaximumSize(new Dimension(200, 1));
        pnlSidebar.add(separatorBottom);
        pnlSidebar.add(Box.createVerticalStrut(10));
        
        JLabel lblVersion = new JLabel("v1.0.0");
        lblVersion.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.PLAIN));
        lblVersion.setForeground(new Color(100, 110, 130));
        lblVersion.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlSidebar.add(lblVersion);
        
        return pnlSidebar;
    }

    /**
     * Tạo nút sidebar hiện đại với hover effects
     * Modern button design with smooth hover transitions and better visual feedback
     */
    private JButton createModernSidebarButton(String text, String action, String iconFile) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(210, 44));
        btn.setMinimumSize(new Dimension(210, 44));
        btn.setPreferredSize(new Dimension(210, 44));
        btn.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        btn.setBackground(new Color(45, 60, 100));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setHorizontalTextPosition(SwingConstants.RIGHT);
        btn.setVerticalAlignment(SwingConstants.CENTER);
        btn.setBorderPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setIconTextGap(12);
        
        ImageIcon icon = loadIcon(iconFile, 22);
        if (icon != null) {
            btn.setIcon(icon);
        }

        // Enhanced mouse listener for smooth hover effects
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn != selectedButton) {
                    btn.setBackground(new Color(60, 80, 140));
                }
                btn.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn == selectedButton) {
                    btn.setBackground(ColorPalette.PRIMARY_DARK);
                } else {
                    btn.setBackground(new Color(45, 60, 100));
                }
                btn.repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                btn.setBackground(ColorPalette.PRIMARY);
                btn.repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (btn == selectedButton) {
                    btn.setBackground(ColorPalette.PRIMARY_DARK);
                } else {
                    btn.setBackground(new Color(45, 60, 100));
                }
                btn.repaint();
            }
        });

        btn.addActionListener(e -> {
            if (selectedButton != null) {
                selectedButton.setBackground(new Color(45, 60, 100));
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
     * Tạo dashboard panel với card-based layout
     * Modern card-based dashboard with better visual organization and spacing
     */
    private JPanel createDashboardPanel() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new GridLayout(2, 3, 20, 20));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(15, 0, 15, 0));

        try {
            pnl.add(createModernStatWidget("Phim Đang Chiếu", createMovieShowcaseWidget(), new Color(70, 130, 180)));
            pnl.add(createModernStatWidget("Doanh Thu Hôm Nay", createRevenueWidget(), new Color(76, 175, 80)));
            pnl.add(createModernStatWidget("Vé Bán Hôm Nay", createTicketsSoldWidget(), new Color(255, 140, 0)));
            pnl.add(createModernStatWidget("Tổng Số Phim", createTotalMoviesWidget(), new Color(156, 39, 176)));
            pnl.add(createModernStatWidget("Thống Kê Nhanh", createQuickStatsWidget(), new Color(233, 30, 99)));
            pnl.add(createModernStatWidget("Hoạt Động Gần Đây", createRecentActivityWidget(), new Color(0, 150, 136)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pnl;
    }

    /**
     * Wrapper cho widget với header bar
     * Modern widget wrapper with colored header bar for better visual hierarchy
     */
    private JPanel createModernStatWidget(String title, JPanel contentPanel, Color headerColor) {
        JPanel pnlWrapper = new JPanel(new BorderLayout());
        pnlWrapper.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnlWrapper.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        pnlWrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(0, 0, 0, 0)
        ));

        // Header dengan color bar
        JPanel pnlHeader = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(headerColor);
                g2d.fillRect(0, 0, 6, getHeight());
            }
        };
        pnlHeader.setBackground(new Color(248, 248, 250));
        pnlHeader.setPreferredSize(new Dimension(0, 50));
        pnlHeader.setLayout(new BorderLayout());
        pnlHeader.setBorder(new EmptyBorder(12, 15, 12, 15));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE, Font.BOLD));
        lblTitle.setForeground(ColorPalette.TEXT_LABEL);
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        pnlWrapper.add(pnlHeader, BorderLayout.NORTH);
        pnlWrapper.add(contentPanel, BorderLayout.CENTER);

        return pnlWrapper;
    }

    /**
     * Tạo footer panel cải tiến
     * Enhanced footer with better spacing and improved button styling
     */
    private JPanel createFooterPanel() {
        JPanel pnlFooter = new JPanel();
        pnlFooter.setLayout(new BorderLayout(15, 0));
        pnlFooter.setBackground(new Color(30, 40, 70));
        pnlFooter.setBorder(new EmptyBorder(12, 25, 12, 25));

        lblDateTime = new JLabel();
        lblDateTime.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.PLAIN));
        lblDateTime.setForeground(new Color(180, 190, 210));

        JLabel lblCopyright = new JLabel("© 2025 Nhóm 10 - Hệ Thống Quản Lý Rạp Chiếu Phim");
        lblCopyright.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.PLAIN));
        lblCopyright.setForeground(new Color(180, 190, 210));

        JButton btnLogout = new JButton("ĐĂNG XUẤT");
        btnLogout.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL + 1, Font.BOLD));
        btnLogout.setBackground(new Color(220, 80, 80));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setContentAreaFilled(true);
        btnLogout.setOpaque(true);
        btnLogout.setBorderPainted(false);
        btnLogout.setPreferredSize(new Dimension(120, 38));
        btnLogout.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        btnLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogout.setBackground(new Color(240, 100, 100));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnLogout.setBackground(new Color(220, 80, 80));
            }
            @Override
            public void mousePressed(MouseEvent e) {
                btnLogout.setBackground(new Color(200, 60, 60));
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                btnLogout.setBackground(new Color(220, 80, 80));
            }
        });
        btnLogout.addActionListener(e -> handleLogout());

        pnlFooter.add(lblDateTime, BorderLayout.WEST);
        pnlFooter.add(lblCopyright, BorderLayout.CENTER);
        pnlFooter.add(btnLogout, BorderLayout.EAST);

        return pnlFooter;
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

        JLabel lblTitle = new JLabel("Phim Đang Chiếu");
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
     * Widget hiển thị doanh thu hôm nay - từ database
     */
    private JPanel createRevenueWidget() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBackground(new Color(240, 255, 240));
        pnl.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 2));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("Doanh Thu Hôm Nay");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE + 2, Font.BOLD));
        lblTitle.setForeground(ColorPalette.ACCENT);
        pnl.add(lblTitle);
        pnl.add(Box.createVerticalStrut(10));

        JLabel lblRevenue = new JLabel("0 VNĐ");
        lblRevenue.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE, Font.BOLD));
        lblRevenue.setForeground(ColorPalette.ACCENT);
        
        try {
            BigDecimal revenue = dashboardService.getRevenueTodayFull();
            if (revenue != null) {
                long revenueValue = revenue.longValue();
                lblRevenue.setText(String.format("%,d VNĐ", revenueValue));
            } else {
                lblRevenue.setText("0 VNĐ");
            }
        } catch (Exception e) {
            lblRevenue.setText("Lỗi tải");
            lblRevenue.setForeground(ColorPalette.STATUS_ERROR);
        }
        
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
     * Widget hiển thị số vé bán hôm nay - từ database
     */
    private JPanel createTicketsSoldWidget() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBackground(new Color(255, 248, 240));
        pnl.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 2));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("Vé Bán Hôm Nay");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE + 2, Font.BOLD));
        lblTitle.setForeground(new Color(255, 140, 0));
        pnl.add(lblTitle);
        pnl.add(Box.createVerticalStrut(10));

        JLabel lblTickets = new JLabel("0");
        lblTickets.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE, Font.BOLD));
        lblTickets.setForeground(new Color(255, 140, 0));
        
        try {
            long ticketsSold = dashboardService.getTicketsSoldTodayFull();
            lblTickets.setText(String.valueOf(ticketsSold));
        } catch (Exception e) {
            lblTickets.setText("0");
            lblTickets.setForeground(ColorPalette.STATUS_ERROR);
        }
        
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
     * Widget hiển thị tổng số phim - từ database
     */
    private JPanel createTotalMoviesWidget() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBackground(new Color(245, 240, 255));
        pnl.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 2));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("Tổng Số Phim");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE + 2, Font.BOLD));
        lblTitle.setForeground(ColorPalette.PRIMARY);
        pnl.add(lblTitle);
        pnl.add(Box.createVerticalStrut(10));

        JLabel lblCount = new JLabel("0");
        lblCount.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE, Font.BOLD));
        lblCount.setForeground(ColorPalette.PRIMARY);
        
        try {
            int totalMovies = dashboardService.getTotalMovies();
            lblCount.setText(String.valueOf(totalMovies));
        } catch (Exception e) {
            lblCount.setText("0");
            lblCount.setForeground(ColorPalette.STATUS_ERROR);
        }
        
        pnl.add(lblCount);
        pnl.add(Box.createVerticalGlue());
        return pnl;
    }

    /**
     * Widget thống kê nhanh - lấy dữ liệu từ database
     * Updated to use proper icons and display data correctly
     */
    private JPanel createQuickStatsWidget() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBackground(new Color(255, 240, 245));
        pnl.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 2));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("Thống Kê Nhanh");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE + 2, Font.BOLD));
        lblTitle.setForeground(ColorPalette.ACCENT);
        pnl.add(lblTitle);
        pnl.add(Box.createVerticalStrut(10));

        try {
            int totalScreens = dashboardService.getTotalScreens();
            int totalEmployees = dashboardService.getTotalEmployees();
            int totalCustomers = dashboardService.getTotalCustomers();
            
            String[] stats = {
                "Phòng: " + totalScreens,
                "Nhân viên: " + totalEmployees,
                "Khách hàng: " + totalCustomers
            };
            
            for (String stat : stats) {
                JLabel lbl = new JLabel(stat);
                lbl.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
                lbl.setForeground(ColorPalette.TEXT_BODY);
                pnl.add(lbl);
                pnl.add(Box.createVerticalStrut(5));
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
     * Widget lịch sử hoạt động - lấy dữ liệu từ service
     * Updated to load recent activities from database instead of hardcoded values
     */
    private JPanel createRecentActivityWidget() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBackground(new Color(240, 255, 255));
        pnl.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 2));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("Hoạt Động Gần Đây");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE + 2, Font.BOLD));
        lblTitle.setForeground(ColorPalette.STATUS_INFO);
        pnl.add(lblTitle);
        pnl.add(Box.createVerticalStrut(10));

        try {
            List<String> activities = dashboardService.getRecentActivities();
            for (String activity : activities) {
                JLabel lbl = new JLabel(activity);
                lbl.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
                lbl.setForeground(ColorPalette.TEXT_BODY);
                pnl.add(lbl);
                pnl.add(Box.createVerticalStrut(5));
            }
        } catch (Exception e) {
            JLabel lblError = new JLabel("Lỗi tải hoạt động");
            lblError.setForeground(ColorPalette.STATUS_ERROR);
            pnl.add(lblError);
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
        
        pnlContentArea.removeAll();
        
        // Create title bar
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
            case "dashboard":
                // Hiển thị dashboard mặc định
                pnlDashboard = createDashboardPanel();
                pnlContentWrapper.add(pnlDashboard, BorderLayout.CENTER);
                break;
            case "phim":
                try {
                    // Load FrmQuanLyPhim as content panel
                    FrmQuanLyPhimPanel pnlQuanLyPhim = new FrmQuanLyPhimPanel(adminHienTai);
                    pnlContentWrapper.add(pnlQuanLyPhim, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel lblError = new JLabel("Lỗi tải trang quản lý phim: " + e.getMessage());
                    lblError.setForeground(ColorPalette.STATUS_ERROR);
                    pnlContentWrapper.add(lblError, BorderLayout.CENTER);
                }
                break;
            case "phong_chieu":
                try {
                    // Load FrmQuanLyPhongChieuPanel as content panel
                    FrmQuanLyPhongChieuPanel pnlQuanLyPhongChieu = new FrmQuanLyPhongChieuPanel(adminHienTai);
                    pnlContentWrapper.add(pnlQuanLyPhongChieu, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel lblError = new JLabel("Lỗi tải trang quản lý phòng chiếu: " + e.getMessage());
                    lblError.setForeground(ColorPalette.STATUS_ERROR);
                    pnlContentWrapper.add(lblError, BorderLayout.CENTER);
                }
                break;
            case "lich_chieu":
                try {
                    // Load FrmQuanLyPhongChieuPanel as content panel
                    FrmQuanLyLichChieuPanel pnlQuanLyLichChieuPanel = new FrmQuanLyLichChieuPanel(adminHienTai);
                    pnlContentWrapper.add(pnlQuanLyLichChieuPanel, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel lblError = new JLabel("Lỗi tải trang quản lý lịch chiếu: " + e.getMessage());
                    lblError.setForeground(ColorPalette.STATUS_ERROR);
                    pnlContentWrapper.add(lblError, BorderLayout.CENTER);
                }
                break;
            case "khuyen_mai":
                try {
                    // Load FrmQuanLyPhongChieuPanel as content panel
                    FrmQuanLyKhuyenMaiPanel pnlQuanLyKhuyenMaiPanel = new FrmQuanLyKhuyenMaiPanel(adminHienTai);
                    pnlContentWrapper.add(pnlQuanLyKhuyenMaiPanel, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel lblError = new JLabel("Lỗi tải trang quản lý khuyến mại: " + e.getMessage());
                    lblError.setForeground(ColorPalette.STATUS_ERROR);
                    pnlContentWrapper.add(lblError, BorderLayout.CENTER);
                }
                break;
            case "nhan_vien":
                try {
                    FrmQuanLyNhanVienPanel pnlQuanLyNhanVien = new FrmQuanLyNhanVienPanel(adminHienTai);
                    pnlContentWrapper.add(pnlQuanLyNhanVien, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel lblError = new JLabel("Lỗi tải trang quản lý nhân viên: " + e.getMessage());
                    lblError.setForeground(ColorPalette.STATUS_ERROR);
                    pnlContentWrapper.add(lblError, BorderLayout.CENTER);
                }
                break;
            case "khach_hang":
                try {
                    FrmQuanLyKhachHangPanel pnlQuanLyKhachHang = new FrmQuanLyKhachHangPanel(adminHienTai);
                    pnlContentWrapper.add(pnlQuanLyKhachHang, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel lblError = new JLabel("Lỗi tải trang quản lý khách hàng: " + e.getMessage());
                    lblError.setForeground(ColorPalette.STATUS_ERROR);
                    pnlContentWrapper.add(lblError, BorderLayout.CENTER);
                }
                break;
            case "hoa_don":
                try {
                    FrmQuanLyHoaDonPanel pnlQuanLyHoaDon = new FrmQuanLyHoaDonPanel(adminHienTai);
                    pnlContentWrapper.add(pnlQuanLyHoaDon, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel lblError = new JLabel("Lỗi tải trang quản lý hóa đơn: " + e.getMessage());
                    lblError.setForeground(ColorPalette.STATUS_ERROR);
                    pnlContentWrapper.add(lblError, BorderLayout.CENTER);
                }
                break;
            case "bao_cao":
                try {
                    FrmQuanLyBaoCaoPanel pnlBaoCao = new FrmQuanLyBaoCaoPanel(adminHienTai);
                    pnlContentWrapper.add(pnlBaoCao, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel lblError = new JLabel("Lỗi tải trang báo cáo: " + e.getMessage());
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
     * Xử lý đăng xuất - Đóng frame hiện tại và mở login
     */
    private void handleLogout() {
        int result = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn đăng xuất?",
            "Xác nhận đăng xuất",
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            try {
                this.dispose(); // Đóng frame admin trước
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

    public TaiKhoanNhanVien getAdminHienTai() {
        return adminHienTai;
    }
}
