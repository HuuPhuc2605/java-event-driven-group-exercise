package iuh.fit.se.nhom10.view;

import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.model.Phim;
import iuh.fit.se.nhom10.service.PhimService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Form quản lý phim - Xem danh sách các phim
 */
public class FrmQuanLyPhim extends JFrame {
    private TaiKhoanNhanVien adminHienTai;
    private PhimService phimService;
    private JTable tblPhim;
    private DefaultTableModel tableModel;

    public FrmQuanLyPhim(TaiKhoanNhanVien admin) throws SQLException {
        this.adminHienTai = admin;
        this.phimService = new PhimService();
        setupUI();
        loadPhimData();
    }

    private void setupUI() {
        setTitle("Quản Lý Phim");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setResizable(true);

        // Panel chính
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BorderLayout());
        pnlMain.setBackground(new Color(240, 245, 250));

        // Header
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(25, 100, 200));
        pnlHeader.setPreferredSize(new Dimension(900, 60));
        pnlHeader.setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Danh Sách Phim");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        pnlHeader.add(lblTitle, BorderLayout.CENTER);

        // Table
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Mã Phim");
        tableModel.addColumn("Tên Phim");
        tableModel.addColumn("Mã Thể Loại");
        tableModel.addColumn("Thời Lượng (phút)");
        tableModel.addColumn("Mã Đạo Diễn");

        tblPhim = new JTable(tableModel);
        tblPhim.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tblPhim.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblPhim.setRowHeight(25);
        tblPhim.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tblPhim);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Panel buttons
        JPanel pnlButtons = new JPanel();
        pnlButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlButtons.setBackground(new Color(240, 245, 250));

        JButton btnThem = new JButton("Thêm Phim");
        btnThem.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnThem.setBackground(new Color(34, 139, 34));
        btnThem.setForeground(Color.WHITE);
        btnThem.setFocusPainted(false);
        btnThem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnThem.addActionListener(e -> {
            try {
                FrmThemPhim frmThemPhim = new FrmThemPhim(adminHienTai);
                frmThemPhim.setVisible(true);
                loadPhimData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnDong = new JButton("Đóng");
        btnDong.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDong.setBackground(new Color(150, 150, 150));
        btnDong.setForeground(Color.WHITE);
        btnDong.setFocusPainted(false);
        btnDong.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDong.addActionListener(e -> this.dispose());

        pnlButtons.add(btnThem);
        pnlButtons.add(btnDong);

        // Thêm các panel vào main
        pnlMain.add(pnlHeader, BorderLayout.NORTH);
        pnlMain.add(scrollPane, BorderLayout.CENTER);
        pnlMain.add(pnlButtons, BorderLayout.SOUTH);

        add(pnlMain);
    }

    private void loadPhimData() {
        tableModel.setRowCount(0);
        List<Phim> phimList = phimService.getAllPhim();
		for (Phim phim : phimList) {
		    tableModel.addRow(new Object[]{
		        phim.getMaPhim(),
		        phim.getTenPhim(),
		        phim.getMaTheLoai(),
		        phim.getThoiLuong(),
		        phim.getMaDD()
		    });
		}
    }
}
