package iuh.fit.se.nhom10.view;

import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Form menu chính cho admin - Giao diện hướng đối tượng
 */
public class FrmAdminMenu extends JFrame {
    private TaiKhoanNhanVien adminHienTai;
    
    public FrmAdminMenu(TaiKhoanNhanVien admin) {
        this.adminHienTai = admin;
        setupUI();
    }

    private void setupUI() {
        setTitle("Hệ Thống Quản Lý Rạp Chiếu Phim - " + adminHienTai.getNhanVien().getTenNV());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel chính
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BorderLayout());
        pnlMain.setBackground(new Color(240, 245, 250));

        // Header
        JPanel pnlHeader = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(25, 55, 115), getWidth(), 0, new Color(40, 100, 200));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        pnlHeader.setPreferredSize(new Dimension(900, 100));
        pnlHeader.setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("MENU QUẢN LÝ RẠP CHIẾU PHIM");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblAdminInfo = new JLabel("Chào " + adminHienTai.getNhanVien().getTenNV() + " (" + adminHienTai.getVaiTro() + ")");
        lblAdminInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAdminInfo.setForeground(new Color(200, 220, 255));
        lblAdminInfo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        lblAdminInfo.setHorizontalAlignment(JLabel.RIGHT);

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(lblAdminInfo, BorderLayout.EAST);

        // Panel nội dung - Grid layout cho các nút menu
        JPanel pnlContent = new JPanel();
        pnlContent.setLayout(new GridLayout(2, 2, 30, 30));
        pnlContent.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        pnlContent.setBackground(new Color( 240, 245, 250));

        // Nút Thêm Phim
        JButton btnThemPhim = createMenuButton("Thêm Phim Mới", "add_movie", new Color(10, 34, 139, 34));
        btnThemPhim.addActionListener(e -> openFrmThemPhim());

        // Nút Quản Lý Phim
        JButton btnQuanLyPhim = createMenuButton("Quản Lý Phim", "manage_movie", new Color(15, 25, 100, 200));
        btnQuanLyPhim.addActionListener(e -> openFrmQuanLyPhim());

        // Nút Quản Lý Lịch Chiếu
        JButton btnQuanLyLichChieu = createMenuButton("Quản Lý Lịch Chiếu", "schedule", new Color(12, 200, 100, 25));
        btnQuanLyLichChieu.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng đang phát triển", "Thông báo", JOptionPane.INFORMATION_MESSAGE));

        // Nút Báo Cáo Doanh Thu
        JButton btnBaoCao = createMenuButton("Báo Cáo Doanh Thu", "report", new Color(10, 150, 50, 200));
        btnBaoCao.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng đang phát triển", "Thông báo", JOptionPane.INFORMATION_MESSAGE));

        pnlContent.add(btnThemPhim);
        pnlContent.add(btnQuanLyPhim);
        pnlContent.add(btnQuanLyLichChieu);
        pnlContent.add(btnBaoCao);

        // Panel footer
        JPanel pnlFooter = new JPanel();
        pnlFooter.setLayout(new BorderLayout());
        pnlFooter.setBackground(new Color(50, 50, 50));
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton btnDangXuat = new JButton("Đăng Xuất");
        btnDangXuat.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDangXuat.setBackground(new Color(200, 50, 50));
        btnDangXuat.setForeground(Color.WHITE);
        btnDangXuat.setFocusPainted(false);
        btnDangXuat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDangXuat.addActionListener(e -> handleLogout());

        JLabel lblCopyright = new JLabel("© 2025 Hệ Thống Quản Lý Rạp Chiếu Phim - Nhóm 10");
        lblCopyright.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCopyright.setForeground(new Color(180, 180, 180));

        pnlFooter.add(lblCopyright, BorderLayout.WEST);
        pnlFooter.add(btnDangXuat, BorderLayout.EAST);

        // Thêm các panel vào main
        pnlMain.add(pnlHeader, BorderLayout.NORTH);
        pnlMain.add(pnlContent, BorderLayout.CENTER);
        pnlMain.add(pnlFooter, BorderLayout.SOUTH);

        add(pnlMain);
    }

    /**
     * Tạo nút menu với giao diện đẹp
     */
    private JButton createMenuButton(String text, String icon, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // changed border to visible white border
        btn.setContentAreaFilled(true); // ensure background is filled properly
        btn.setOpaque(true); // make button opaque to show background color
        btn.setPreferredSize(new Dimension(200, 120));
        
        // Hiệu ứng hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(Math.min(color.getRed() + 30, 255),
                                           Math.min(color.getGreen() + 30, 255),
                                           Math.min(color.getBlue() + 30, 255)));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(color);
            }
        });
        
        return btn;
    }

    private void openFrmThemPhim() {
        try {
            FrmThemPhim frmThemPhim = new FrmThemPhim(adminHienTai);
            frmThemPhim.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openFrmQuanLyPhim() {
        try {
            FrmQuanLyPhim frmQuanLyPhim = new FrmQuanLyPhim(adminHienTai);
            frmQuanLyPhim.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLogout() {
        int result = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn đăng xuất?",
            "Xác nhận đăng xuất",
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            try {
                FrmDangNhap frmDangNhap = new FrmDangNhap();
                frmDangNhap.setVisible(true);
                this.dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public TaiKhoanNhanVien getAdminHienTai() {
        return adminHienTai;
    }
}
