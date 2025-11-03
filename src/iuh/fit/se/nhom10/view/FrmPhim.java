package iuh.fit.se.nhom10.view;

import iuh.fit.se.nhom10.model.Phim;
import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.service.PhimService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

/**
 * Form quản lý phim - chỉ admin có thể truy cập
 */
public class FrmPhim extends JFrame {
    private JTextField txtMaPhim;
    private JTextField txtTenPhim;
    private JTextField txtMaTheLoai;
    private JTextField txtThoiLuong;
    private JTextField txtMaDD;
    
    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private JButton btnThoat;
    
    private JTable tblPhim;
    private DefaultTableModel tableModel;
    
    private PhimService service;
    private TaiKhoanNhanVien adminHienTai;

    public FrmPhim(TaiKhoanNhanVien adminHienTai) throws SQLException {
        this.adminHienTai = adminHienTai;
        
        // Kiểm tra quyền admin
        if (adminHienTai == null || !"Admin".equalsIgnoreCase(adminHienTai.getVaiTro())) {
            JOptionPane.showMessageDialog(null, 
                "Bạn không có quyền truy cập form này! Chỉ Admin có thể quản lý phim.",
                "Cảnh báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        this.service = new PhimService();
        setupUI();
        loadDataToTable();
    }

    /**
     * Thiết lập giao diện
     */
    private void setupUI() {
        setTitle("Quản Lý Phim - Admin");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setResizable(true);

        // Panel chính
        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlMain.setBackground(new Color(245, 245, 245));

        // Panel form nhập liệu
        JPanel pnlForm = createFormPanel();
        pnlMain.add(pnlForm, BorderLayout.NORTH);

        // Panel bảng
        JPanel pnlTable = createTablePanel();
        pnlMain.add(pnlTable, BorderLayout.CENTER);

        // Panel button
        JPanel pnlButtons = createButtonPanel();
        pnlMain.add(pnlButtons, BorderLayout.SOUTH);

        add(pnlMain);
    }

    /**
     * Tạo panel nhập liệu
     */
    private JPanel createFormPanel() {
        JPanel pnl = new JPanel(new GridLayout(3, 4, 10, 10));
        pnl.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            "Thông tin phim", 1, 2));
        pnl.setBackground(Color.WHITE);

        // Mã phim
        pnl.add(new JLabel("Mã Phim:"));
        txtMaPhim = new JTextField();
        txtMaPhim.setFont(new Font("Arial", Font.PLAIN, 12));
        pnl.add(txtMaPhim);

        // Tên phim
        pnl.add(new JLabel("Tên Phim:"));
        txtTenPhim = new JTextField();
        txtTenPhim.setFont(new Font("Arial", Font.PLAIN, 12));
        pnl.add(txtTenPhim);

        // Mã thể loại
        pnl.add(new JLabel("Mã Thể Loại:"));
        txtMaTheLoai = new JTextField();
        txtMaTheLoai.setFont(new Font("Arial", Font.PLAIN, 12));
        pnl.add(txtMaTheLoai);

        // Thời lượng
        pnl.add(new JLabel("Thời Lượng (phút):"));
        txtThoiLuong = new JTextField();
        txtThoiLuong.setFont(new Font("Arial", Font.PLAIN, 12));
        pnl.add(txtThoiLuong);

        // Mã đạo diễn
        pnl.add(new JLabel("Mã Đạo Diễn:"));
        txtMaDD = new JTextField();
        txtMaDD.setFont(new Font("Arial", Font.PLAIN, 12));
        pnl.add(txtMaDD);

        pnl.add(new JLabel()); // Spacer
        pnl.add(new JLabel()); // Spacer
        pnl.add(new JLabel()); // Spacer

        return pnl;
    }

    /**
     * Tạo panel bảng dữ liệu
     */
    private JPanel createTablePanel() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            "Danh sách phim", 1, 2));

        String[] columnNames = {"Mã Phim", "Tên Phim", "Mã Thể Loại", "Thời Lượng", "Mã Đạo Diễn"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tblPhim = new JTable(tableModel);
        tblPhim.setFont(new Font("Arial", Font.PLAIN, 11));
        tblPhim.setRowHeight(25);
        tblPhim.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Thêm sự kiện chọn hàng để hiển thị dữ liệu
        tblPhim.getSelectionModel().addListSelectionListener(e -> {
            int row = tblPhim.getSelectedRow();
            if (row >= 0) {
                txtMaPhim.setText((String) tblPhim.getValueAt(row, 0));
                txtTenPhim.setText((String) tblPhim.getValueAt(row, 1));
                txtMaTheLoai.setText(tblPhim.getValueAt(row, 2).toString());
                txtThoiLuong.setText(tblPhim.getValueAt(row, 3).toString());
                txtMaDD.setText((String) tblPhim.getValueAt(row, 4));
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblPhim);
        pnl.add(scrollPane, BorderLayout.CENTER);

        return pnl;
    }

    /**
     * Tạo panel button
     */
    private JPanel createButtonPanel() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnl.setBackground(new Color(245, 245, 245));

        btnThem = new JButton("Thêm Phim");
        btnThem.setFont(new Font("Arial", Font.BOLD, 12));
        btnThem.setBackground(new Color(0, 150, 0));
        btnThem.setForeground(Color.WHITE);
        btnThem.setPreferredSize(new Dimension(120, 35));
        btnThem.addActionListener(e -> handleAddPhim());

        btnSua = new JButton("Sửa Phim");
        btnSua.setFont(new Font("Arial", Font.BOLD, 12));
        btnSua.setBackground(new Color(0, 100, 200));
        btnSua.setForeground(Color.WHITE);
        btnSua.setPreferredSize(new Dimension(120, 35));
        btnSua.addActionListener(e -> handleUpdatePhim());

        btnXoa = new JButton("Xóa Phim");
        btnXoa.setFont(new Font("Arial", Font.BOLD, 12));
        btnXoa.setBackground(new Color(200, 0, 0));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setPreferredSize(new Dimension(120, 35));
        btnXoa.addActionListener(e -> handleDeletePhim());

        btnLamMoi = new JButton("Làm Mới");
        btnLamMoi.setFont(new Font("Arial", Font.BOLD, 12));
        btnLamMoi.setBackground(new Color(150, 150, 150));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setPreferredSize(new Dimension(120, 35));
        btnLamMoi.addActionListener(e -> resetForm());

        btnThoat = new JButton("Thoát");
        btnThoat.setFont(new Font("Arial", Font.BOLD, 12));
        btnThoat.setBackground(new Color(100, 100, 100));
        btnThoat.setForeground(Color.WHITE);
        btnThoat.setPreferredSize(new Dimension(120, 35));
        btnThoat.addActionListener(e -> dispose());

        pnl.add(btnThem);
        pnl.add(btnSua);
        pnl.add(btnXoa);
        pnl.add(btnLamMoi);
        pnl.add(btnThoat);

        return pnl;
    }

    /**
     * Xử lý thêm phim
     */
    private void handleAddPhim() {
        try {
            String maPhim = txtMaPhim.getText().trim();
            String tenPhim = txtTenPhim.getText().trim();
            String maTheLoai = txtMaTheLoai.getText().trim();
            String thoiLuong = txtThoiLuong.getText().trim();
            String maDD = txtMaDD.getText().trim();

            if (maPhim.isEmpty() || tenPhim.isEmpty() || maTheLoai.isEmpty() || thoiLuong.isEmpty() || maDD.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Phim phim = new Phim(maPhim, tenPhim, Integer.parseInt(maTheLoai), Integer.parseInt(thoiLuong), maDD);
            
            if (service.addPhim(phim, adminHienTai)) {
                JOptionPane.showMessageDialog(this, "Thêm phim thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadDataToTable();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm phim thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Thời lượng và mã thể loại phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Xử lý sửa phim
     */
    private void handleUpdatePhim() {
        try {
            String maPhim = txtMaPhim.getText().trim();
            
            if (maPhim.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phim cần sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String tenPhim = txtTenPhim.getText().trim();
            String maTheLoai = txtMaTheLoai.getText().trim();
            String thoiLuong = txtThoiLuong.getText().trim();
            String maDD = txtMaDD.getText().trim();

            if (tenPhim.isEmpty() || maTheLoai.isEmpty() || thoiLuong.isEmpty() || maDD.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Phim phim = new Phim(maPhim, tenPhim, Integer.parseInt(maTheLoai), Integer.parseInt(thoiLuong), maDD);
            
            if (service.updatePhim(phim, adminHienTai)) {
                JOptionPane.showMessageDialog(this, "Sửa phim thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadDataToTable();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Sửa phim thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Thời lượng và mã thể loại phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Xử lý xóa phim
     */
    private void handleDeletePhim() {
        String maPhim = txtMaPhim.getText().trim();
        
        if (maPhim.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phim cần xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa phim '" + txtTenPhim.getText() + "'?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            if (service.deletePhim(maPhim, adminHienTai)) {
                JOptionPane.showMessageDialog(this, "Xóa phim thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadDataToTable();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa phim thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Đổi dữ liệu vào bảng
     */
    private void loadDataToTable() {
        tableModel.setRowCount(0);
        List<Phim> phimList = service.getAllPhim();
        
        for (Phim phim : phimList) {
            Object[] row = {
                phim.getMaPhim(),
                phim.getTenPhim(),
                phim.getMaTheLoai(),
                phim.getThoiLuong(),
                phim.getMaDD()
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Xóa dữ liệu form
     */
    private void resetForm() {
        txtMaPhim.setText("");
        txtTenPhim.setText("");
        txtMaTheLoai.setText("");
        txtThoiLuong.setText("");
        txtMaDD.setText("");
        tblPhim.clearSelection();
    }
}
