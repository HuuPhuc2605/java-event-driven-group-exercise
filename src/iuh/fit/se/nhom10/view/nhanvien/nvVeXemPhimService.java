package iuh.fit.se.nhom10.view.nhanvien;

import iuh.fit.se.nhom10.model.GheNgoi;
import iuh.fit.se.nhom10.model.LichChieu;
import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.util.ColorPalette;
import iuh.fit.se.nhom10.view.FrmDangNhap;
import iuh.fit.se.nhom10.service.LichChieuService;
import iuh.fit.se.nhom10.dao.GheNgoiDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Menu cho nhân viên bán vé
 */
public class nvVeXemPhimService extends JFrame {
    private TaiKhoanNhanVien nhanVienHienTai;
    private JLabel lblCurrentModule;
    private JPanel pnlContentArea;
    private JButton selectedButton;

    public nvVeXemPhimService(TaiKhoanNhanVien nhanVien) {
        this.nhanVienHienTai = nhanVien;
        setupUI();
    }

    private void setupUI() {
        setTitle("Hệ Thống Bán Vé - Nhân Viên: " + nhanVienHienTai.getNhanVien().getTenNV());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BorderLayout(0, 0));
        pnlMain.setBackground(ColorPalette.BACKGROUND_MAIN);

        JPanel pnlHeader = createHeaderPanel();
        JPanel pnlContainer = createMainContainer();

        pnlMain.add(pnlHeader, BorderLayout.NORTH);
        pnlMain.add(pnlContainer, BorderLayout.CENTER);

        add(pnlMain);
    }

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
        pnlHeader.setPreferredSize(new Dimension(1200, 60));
        pnlHeader.setLayout(new BorderLayout());
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("GIAO DIỆN BÁN VÉ RẠP CHIẾU PHIM");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE, Font.BOLD));
        lblTitle.setForeground(ColorPalette.TEXT_HEADER_TITLE);

        pnlHeader.add(lblTitle, BorderLayout.WEST);

        return pnlHeader;
    }

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

    private JPanel createSidebarPanel() {
        JPanel pnlSidebar = new JPanel();
        pnlSidebar.setLayout(new BoxLayout(pnlSidebar, BoxLayout.Y_AXIS));
        pnlSidebar.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnlSidebar.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        pnlSidebar.setPreferredSize(new Dimension(150, 0));
        pnlSidebar.setBorder(new EmptyBorder(15, 10, 15, 10));

        JLabel lblTitle = new JLabel("MENU");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        pnlSidebar.add(lblTitle);
        pnlSidebar.add(Box.createVerticalStrut(15));

        String[][] menuItems = {
            {"Bán Vé", "ban_ve"},
            {"Khách Hàng", "khach_hang"},
            {"Hóa Đơn", "hoa_don"},
            {"Thanh Toán", "thanh_toan"}
        };

        for (String[] item : menuItems) {
            JButton btn = createSidebarButton(item[0], item[1]);
            pnlSidebar.add(btn);
            pnlSidebar.add(Box.createVerticalStrut(8));
        }

        pnlSidebar.add(Box.createVerticalGlue());

        JButton btnLogout = new JButton("Đăng Xuất");
        btnLogout.addActionListener(e -> handleLogout());
        pnlSidebar.add(btnLogout);

        return pnlSidebar;
    }

    private JButton createSidebarButton(String text, String action) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(130, 40));
        btn.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedButton != null) {
                    selectedButton.setBackground(ColorPalette.BUTTON_PRIMARY_BG);
                }
                selectedButton = btn;
                btn.setBackground(ColorPalette.PRIMARY_DARK);
                switchContent(action, text);
            }
        });

        return btn;
    }

    private JPanel createContentPanel() {
        JPanel pnlContent = new JPanel(new BorderLayout());
        pnlContent.setBackground(ColorPalette.BACKGROUND_CONTENT);

        lblCurrentModule = new JLabel("Bán Vé");
        lblCurrentModule.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE, Font.BOLD));
        pnlContent.add(lblCurrentModule, BorderLayout.NORTH);

        return pnlContent;
    }

    private void switchContent(String action, String title) {
        lblCurrentModule.setText(title);
        pnlContentArea.removeAll();

        JPanel pnlTitleBar = new JPanel();
        pnlTitleBar.setLayout(new BorderLayout());
        pnlTitleBar.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnlTitleBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ColorPalette.BORDER_LIGHT));
        pnlTitleBar.setBorder(new EmptyBorder(0, 0, 10, 0));

        lblCurrentModule.setText(title);
        pnlTitleBar.add(lblCurrentModule, BorderLayout.WEST);
        
        pnlContentArea.add(pnlTitleBar, BorderLayout.NORTH);

        switch (action) {
            case "ban_ve":
                try {
                    nvFrmBanVePanel pnl = new nvFrmBanVePanel(nhanVienHienTai);
                    pnlContentArea.add(pnl, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel lbl = new JLabel("Lỗi: " + e.getMessage());
                    pnlContentArea.add(lbl, BorderLayout.CENTER);
                }
                break;
            case "khach_hang":
                try {
                    nvFrmQuanLyKhachHangPanel pnl = new nvFrmQuanLyKhachHangPanel(nhanVienHienTai);
                    pnlContentArea.add(pnl, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel lbl = new JLabel("Lỗi: " + e.getMessage());
                    pnlContentArea.add(lbl, BorderLayout.CENTER);
                }
                break;
            case "hoa_don":
                try {
                    nvFrmHoaDonPanel pnl = new nvFrmHoaDonPanel(nhanVienHienTai);
                    pnlContentArea.add(pnl, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel lbl = new JLabel("Lỗi: " + e.getMessage());
                    pnlContentArea.add(lbl, BorderLayout.CENTER);
                }
                break;
            case "thanh_toan":
                try {
                    nvFrmThanhToanPanel pnl = new nvFrmThanhToanPanel(nhanVienHienTai);
                    pnlContentArea.add(pnl, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel lbl = new JLabel("Lỗi: " + e.getMessage());
                    pnlContentArea.add(lbl, BorderLayout.CENTER);
                }
                break;
        }

        pnlContentArea.revalidate();
        pnlContentArea.repaint();
    }

    private void handleLogout() {
        int result = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            this.dispose();
            try {
                new FrmDangNhap().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<LichChieu> layTatCaLichChieu() {
        try {
            LichChieuService lichChieuService = new LichChieuService();
            return lichChieuService.getAllLichChieu();
        } catch (Exception e) {
            System.out.println("Lỗi lấy lịch chiếu: " + e.getMessage());
            return null;
        }
    }

    public List<GheNgoi> layDanhSachGheTheoLichChieu(String maLich) {
        try {
            LichChieuService lichChieuService = new LichChieuService();
            LichChieu lc = lichChieuService.getLichChieuByMa(maLich);
            
            if (lc != null) {
                GheNgoiDAO gheNgoiDAO = new GheNgoiDAO();
                List<GheNgoi> allSeats = gheNgoiDAO.getGheByPhong(lc.getMaPhong());
                List<String> bookedSeats = gheNgoiDAO.getBookedSeatsForScreening(maLich);
            }
               
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách ghế: " + e.getMessage());
        }
        return null;
    }
}
