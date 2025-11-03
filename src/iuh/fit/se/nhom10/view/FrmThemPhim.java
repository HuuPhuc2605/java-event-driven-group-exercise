package iuh.fit.se.nhom10.view;

import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.model.Phim;
import iuh.fit.se.nhom10.service.PhimService;
import iuh.fit.se.nhom10.dao.DaoDienDAO;
import iuh.fit.se.nhom10.dao.TheLoaiDAO;
import iuh.fit.se.nhom10.model.DaoDien;
import iuh.fit.se.nhom10.model.TheLoai;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Form thêm phim mới - Chỉ dành cho admin
 */
public class FrmThemPhim extends JFrame {
    private TaiKhoanNhanVien adminHienTai;
    private PhimService phimService;
    private DaoDienDAO daoDienDAO;
    private TheLoaiDAO theLoaiDAO;
    
    // Input fields
    private JTextField txtMaPhim;
    private JTextField txtTenPhim;
    private JComboBox<String> cboTheLoai;
    private JTextField txtThoiLuong;
    private JComboBox<String> cboDaoDien;
    private JLabel lblThongBao;
    private JButton btnLuu;
    private JButton btnHuy;

    public FrmThemPhim(TaiKhoanNhanVien admin) throws SQLException {
        this.adminHienTai = admin;
        this.phimService = new PhimService();
        this.daoDienDAO = new DaoDienDAO();
        this.theLoaiDAO = new TheLoaiDAO();
        setupUI();
        loadComboBoxData();
    }

    private void setupUI() {
        setTitle("Thêm Phim Mới");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel chính
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BorderLayout());
        pnlMain.setBackground(new Color(240, 245, 250));

        // Header
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(34, 139, 34));
        pnlHeader.setPreferredSize(new Dimension(600, 60));
        pnlHeader.setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Thêm Phim Mới");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        pnlHeader.add(lblTitle, BorderLayout.CENTER);

        // Panel nội dung
        JPanel pnlContent = new JPanel();
        pnlContent.setLayout(new BoxLayout(pnlContent, BoxLayout.Y_AXIS));
        pnlContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlContent.setBackground(Color.WHITE);

        // Mã Phim
        pnlContent.add(createLabelField("Mã Phim:", null, true));
        txtMaPhim = new JTextField();
        txtMaPhim.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        pnlContent.add(txtMaPhim);
        pnlContent.add(Box.createVerticalStrut(15));

        // Tên Phim
        pnlContent.add(createLabelField("Tên Phim:", null, true));
        txtTenPhim = new JTextField();
        txtTenPhim.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        pnlContent.add(txtTenPhim);
        pnlContent.add(Box.createVerticalStrut(15));

        // Thể Loại
        pnlContent.add(createLabelField("Thể Loại:", null, true));
        cboTheLoai = new JComboBox<>();
        cboTheLoai.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        pnlContent.add(cboTheLoai);
        pnlContent.add(Box.createVerticalStrut(15));

        // Thời Lượng
        pnlContent.add(createLabelField("Thời Lượng (phút):", null, true));
        txtThoiLuong = new JTextField();
        txtThoiLuong.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        pnlContent.add(txtThoiLuong);
        pnlContent.add(Box.createVerticalStrut(15));

        // Đạo Diễn
        pnlContent.add(createLabelField("Đạo Diễn:", null, true));
        cboDaoDien = new JComboBox<>();
        cboDaoDien.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        pnlContent.add(cboDaoDien);
        pnlContent.add(Box.createVerticalStrut(20));

        // Thông báo
        lblThongBao = new JLabel();
        lblThongBao.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblThongBao.setForeground(Color.RED);
        pnlContent.add(lblThongBao);

        // Panel buttons
        JPanel pnlButtons = new JPanel();
        pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.X_AXIS));
        pnlButtons.setBackground(Color.WHITE);

        btnLuu = new JButton("Lưu");
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLuu.setBackground(new Color(34, 139, 34));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFocusPainted(false);
        btnLuu.setPreferredSize(new Dimension(100, 40));
        btnLuu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLuu.addActionListener(e -> handleLuu());

        btnHuy = new JButton("Hủy");
        btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnHuy.setBackground(new Color(150, 150, 150));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setFocusPainted(false);
        btnHuy.setPreferredSize(new Dimension(100, 40));
        btnHuy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHuy.addActionListener(e -> this.dispose());

        pnlButtons.add(btnLuu);
        pnlButtons.add(Box.createHorizontalStrut(10));
        pnlButtons.add(btnHuy);

        pnlContent.add(pnlButtons);

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(pnlContent);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Thêm các panel vào main
        pnlMain.add(pnlHeader, BorderLayout.NORTH);
        pnlMain.add(scrollPane, BorderLayout.CENTER);

        add(pnlMain);
    }

    private JLabel createLabelField(String text, String tooltip, boolean required) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(50, 50, 50));
        if (required) {
            label.setText(text + " *");
            label.setForeground(new Color(200, 0, 0));
        }
        if (tooltip != null) {
            label.setToolTipText(tooltip);
        }
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void loadComboBoxData() {
        // Load thể loại
		List<TheLoai> theLoaiList = theLoaiDAO.getAllTheLoai();
		for (TheLoai tl : theLoaiList) {
		    cboTheLoai.addItem(tl.getMaTheLoai() + " - " + tl.getTenTheLoai());
		}

		// Load đạo diễn
		List<DaoDien> daoDienList = daoDienDAO.getAllDaoDien();
		for (DaoDien dd : daoDienList) {
		    cboDaoDien.addItem(dd.getMaDD() + " - " + dd.getTenDD());
		}
    }

    private void handleLuu() {
        // Kiểm tra validation
        if (txtMaPhim.getText().trim().isEmpty()) {
            lblThongBao.setText("Mã phim không được để trống!");
            lblThongBao.setForeground(Color.RED);
            return;
        }

        if (txtTenPhim.getText().trim().isEmpty()) {
            lblThongBao.setText("Tên phim không được để trống!");
            lblThongBao.setForeground(Color.RED);
            return;
        }

        if (cboTheLoai.getSelectedItem() == null || cboTheLoai.getSelectedIndex() < 0) {
            lblThongBao.setText("Vui lòng chọn thể loại!");
            lblThongBao.setForeground(Color.RED);
            return;
        }

        if (txtThoiLuong.getText().trim().isEmpty()) {
            lblThongBao.setText("Thời lượng không được để trống!");
            lblThongBao.setForeground(Color.RED);
            return;
        }

        if (cboDaoDien.getSelectedItem() == null || cboDaoDien.getSelectedIndex() < 0) {
            lblThongBao.setText("Vui lòng chọn đạo diễn!");
            lblThongBao.setForeground(Color.RED);
            return;
        }

        try {
            int thoiLuong = Integer.parseInt(txtThoiLuong.getText().trim());
            if (thoiLuong <= 0) {
                lblThongBao.setText("Thời lượng phải lớn hơn 0!");
                lblThongBao.setForeground(Color.RED);
                return;
            }

            // Lấy mã thể loại từ combobox
            String theLoaiStr = (String) cboTheLoai.getSelectedItem();
            int maTheLoai = Integer.parseInt(theLoaiStr.split(" - ")[0]);

            // Lấy mã đạo diễn từ combobox
            String daoDienStr = (String) cboDaoDien.getSelectedItem();
            String maDD = daoDienStr.split(" - ")[0];

            // Tạo object phim
            Phim phim = new Phim();
            phim.setMaPhim(txtMaPhim.getText().trim());
            phim.setTenPhim(txtTenPhim.getText().trim());
            phim.setMaTheLoai(maTheLoai);
            phim.setThoiLuong(thoiLuong);
            phim.setMaDD(maDD);

            // Gọi service để thêm phim
            boolean result = phimService.addPhim(phim, adminHienTai);

            if (result) {
                lblThongBao.setText("Thêm phim thành công!");
                lblThongBao.setForeground(new Color(0, 150, 0));
                
                // Reset form
                txtMaPhim.setText("");
                txtTenPhim.setText("");
                txtThoiLuong.setText("");
                cboTheLoai.setSelectedIndex(0);
                cboDaoDien.setSelectedIndex(0);

                // Delay rồi đóng
                Timer timer = new Timer(1500, e -> this.dispose());
                timer.setRepeats(false);
                timer.start();
            } else {
                lblThongBao.setText("Thêm phim thất bại!");
                lblThongBao.setForeground(Color.RED);
            }
        } catch (NumberFormatException e) {
            lblThongBao.setText("Thời lượng phải là số nguyên!");
            lblThongBao.setForeground(Color.RED);
        }
    }
}
