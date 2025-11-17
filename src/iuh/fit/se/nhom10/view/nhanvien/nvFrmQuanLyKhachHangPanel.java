package iuh.fit.se.nhom10.view.nhanvien;

import iuh.fit.se.nhom10.model.KhachHang;
import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.service.nvKhachHangService;
import iuh.fit.se.nhom10.util.ColorPalette;
import iuh.fit.se.nhom10.util.ButtonStyle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Panel quản lý khách hàng cho nhân viên
 */
public class nvFrmQuanLyKhachHangPanel extends JPanel {
    private TaiKhoanNhanVien nhanVienHienTai;
    private nvKhachHangService khachHangService;
    private JTable tblKhachHang;
    private DefaultTableModel modelTable;
    private JTextField txtMaKH, txtTenKH, txtSoDienThoai, txtTimKiem;
    private KhachHang khachHangDangChon;

    public nvFrmQuanLyKhachHangPanel(TaiKhoanNhanVien nhanVien) throws Exception {
        this.nhanVienHienTai = nhanVien;
        this.khachHangService = new nvKhachHangService();
        setupUI();
        loadDuLieu();
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(ColorPalette.BACKGROUND_CONTENT);

        // Panel công cụ tìm kiếm
        JPanel pnlSearch = createSearchPanel();
        add(pnlSearch, BorderLayout.NORTH);

        // Panel bảng dữ liệu
        JPanel pnlTable = createTablePanel();
        add(pnlTable, BorderLayout.CENTER);

        // Panel form nhập liệu
        JPanel pnlForm = createFormPanel();
        add(pnlForm, BorderLayout.SOUTH);
    }

    private JPanel createSearchPanel() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(BorderFactory.createTitledBorder("Tìm kiếm khách hàng"));

        JLabel lbl = new JLabel("Tìm kiếm:");
        lbl.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        lbl.setForeground(ColorPalette.TEXT_LABEL);

        txtTimKiem = new JTextField(20);
        txtTimKiem.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));

        JButton btnTim = ButtonStyle.createPrimaryButton("Tìm");
        JButton btnLamMoi = ButtonStyle.createSecondaryButton("Làm mới");

        btnTim.addActionListener(e -> timKiemKhachHang());
        btnLamMoi.addActionListener(e -> {
            txtTimKiem.setText("");
            loadDuLieu();
        });

        pnl.add(lbl);
        pnl.add(txtTimKiem);
        pnl.add(btnTim);
        pnl.add(btnLamMoi);

        return pnl;
    }

    private JPanel createTablePanel() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

        String[] columns = {"Mã KH", "Tên Khách Hàng", "Số Điện Thoại"};
        modelTable = new DefaultTableModel(columns, 0);
        tblKhachHang = new JTable(modelTable);
        tblKhachHang.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));

        tblKhachHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblKhachHang.getSelectedRow();
                if (row >= 0) {
                    String maKH = (String) modelTable.getValueAt(row, 0);
                    khachHangDangChon = khachHangService.layKhachHangTheoMa(maKH);
                    if (khachHangDangChon != null) {
                        txtMaKH.setText(khachHangDangChon.getMaKH());
                        txtTenKH.setText(khachHangDangChon.getTenKH());
                        txtSoDienThoai.setText(khachHangDangChon.getSoDienThoai());
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tblKhachHang);
        pnl.add(scroll, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel createFormPanel() {
        // 3 hàng, 4 cột cho dễ sắp xếp
        JPanel pnl = new JPanel(new GridLayout(3, 4, 10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(BorderFactory.createTitledBorder("Thông Tin Khách Hàng"));

        // ----- HÀNG 1 -----
        // MÃ KH
        JLabel lblMaKH = new JLabel("Mã KH:");
        txtMaKH = new JTextField();
        txtMaKH.setEditable(true); // cho phép nhập/sửa mã KH

        // TÊN KH
        JLabel lblTenKH = new JLabel("Tên Khách Hàng:");
        txtTenKH = new JTextField();

        pnl.add(lblMaKH);
        pnl.add(txtMaKH);
        pnl.add(lblTenKH);
        pnl.add(txtTenKH);

        // ----- HÀNG 2 -----
        // SĐT
        JLabel lblSoDT = new JLabel("Số Điện Thoại:");
        txtSoDienThoai = new JTextField();

        pnl.add(lblSoDT);
        pnl.add(txtSoDienThoai);
        pnl.add(new JLabel()); // ô trống
        pnl.add(new JLabel()); // ô trống

        // ----- HÀNG 3 - BUTTONS -----
        pnl.add(new JLabel()); // trống bên trái

        JButton btnThem = ButtonStyle.createPrimaryButton("Thêm Mới");
        btnThem.addActionListener(e -> themKhachHang());
        pnl.add(btnThem);

        JButton btnSua = ButtonStyle.createSecondaryButton("Sửa");
        btnSua.addActionListener(e -> suaKhachHang());
        pnl.add(btnSua);

        JButton btnXoa = ButtonStyle.createSecondaryButton("Xóa");
        btnXoa.addActionListener(e -> xoaKhachHang());
        pnl.add(btnXoa);

        return pnl;
    }

    private void loadDuLieu() {
        modelTable.setRowCount(0);
        List<KhachHang> list = khachHangService.layTatCaKhachHang();
        for (KhachHang kh : list) {
            modelTable.addRow(new Object[]{kh.getMaKH(), kh.getTenKH(), kh.getSoDienThoai()});
        }
    }

    private void timKiemKhachHang() {
        String keyword = txtTimKiem.getText().trim();
        modelTable.setRowCount(0);
        List<KhachHang> list = khachHangService.timKiemKhachHang(keyword);
        for (KhachHang kh : list) {
            modelTable.addRow(new Object[]{kh.getMaKH(), kh.getTenKH(), kh.getSoDienThoai()});
        }
    }

    private void themKhachHang() {
        String maKH = txtMaKH.getText().trim();
        String tenKH = txtTenKH.getText().trim();
        String soDienThoai = txtSoDienThoai.getText().trim();

        // BẮT BUỘC NHẬP MÃ KH
        if (maKH.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập mã khách hàng",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tenKH.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập tên khách hàng",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        KhachHang kh = new KhachHang(maKH, tenKH, soDienThoai);
        if (khachHangService.themKhachHang(kh)) {
            JOptionPane.showMessageDialog(this,
                    "Thêm khách hàng thành công (Mã: " + maKH + ")",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
            loadDuLieu();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Thêm khách hàng thất bại",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaKhachHang() {
        if (khachHangDangChon == null) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn khách hàng cần sửa",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        khachHangDangChon.setMaKH(txtMaKH.getText().trim()); // nếu muốn cho sửa mã luôn
        khachHangDangChon.setTenKH(txtTenKH.getText().trim());
        khachHangDangChon.setSoDienThoai(txtSoDienThoai.getText().trim());

        if (khachHangService.suaKhachHang(khachHangDangChon)) {
            JOptionPane.showMessageDialog(this,
                    "Cập nhật khách hàng thành công",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
            loadDuLieu();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Cập nhật khách hàng thất bại",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaKhachHang() {
        if (khachHangDangChon == null) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn khách hàng cần xóa",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa khách hàng: "
                        + khachHangDangChon.getTenKH()
                        + " (Mã: " + khachHangDangChon.getMaKH() + ")?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        boolean ok = khachHangService.xoaKhachHang(khachHangDangChon.getMaKH());

        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Xóa khách hàng thành công",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
            loadDuLieu();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Xóa khách hàng thất bại",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtMaKH.setText("");
        txtTenKH.setText("");
        txtSoDienThoai.setText("");
        txtTimKiem.setText("");
        khachHangDangChon = null;
    }
}
