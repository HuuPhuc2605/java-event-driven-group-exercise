package iuh.fit.se.nhom10.view.nhanvien;

import iuh.fit.se.nhom10.model.HoaDon;
import iuh.fit.se.nhom10.model.ThanhToan;
import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.service.HoaDonService;
import iuh.fit.se.nhom10.service.ThanhToanService;
import iuh.fit.se.nhom10.util.ButtonStyle;
import iuh.fit.se.nhom10.util.ColorPalette;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Panel thanh toán cho nhân viên - Xử lý thanh toán, cập nhật trạng thái, xem lịch sử
 */
public class nvFrmThanhToanPanel extends JPanel {
    private TaiKhoanNhanVien nhanVienHienTai;
    private HoaDonService hoaDonService;
    private ThanhToanService thanhToanService;

    private JComboBox<HoaDon> cbHoaDon;
    private JComboBox<String> cbPhuongThuc; // hiện chưa dùng, giữ nguyên
    private JComboBox<String> cbTrangThai;
    private JTable tblThanhToan;
    private DefaultTableModel modelTable;
    private JLabel lblTongTien;
    private JLabel lblStatusMessage;
    private JButton btnCapNhatTrangThai;

    /* ======================= CONSTRUCTORS ======================= */

    public nvFrmThanhToanPanel(TaiKhoanNhanVien nhanVien) throws Exception {
        this.nhanVienHienTai = nhanVien;
        this.hoaDonService = new HoaDonService();
        this.thanhToanService = new ThanhToanService();
        setupUI();
        loadDuLieu();
    }

    /**
     * Constructor mở panel và tự chọn hóa đơn theo mã.
     * Dùng khi gọi từ nvFrmBanVePanel sau khi tạo hóa đơn mới.
     */
    public nvFrmThanhToanPanel(TaiKhoanNhanVien nhanVien, String maHDCanChon) throws Exception {
        this(nhanVien); // gọi lại constructor gốc để setup UI + load dữ liệu
        if (maHDCanChon != null && !maHDCanChon.trim().isEmpty()) {
            chonHoaDonTheoMa(maHDCanChon);
        }
    }

    /* ======================= UI SETUP ======================= */

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(ColorPalette.BACKGROUND_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlPayment = createPaymentPanel();
        add(pnlPayment, BorderLayout.NORTH);

        JPanel pnlTable = createTablePanel();
        add(pnlTable, BorderLayout.CENTER);
    }

    private JPanel createPaymentPanel() {
        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(BorderFactory.createTitledBorder("Cập Nhật Trạng Thái Thanh Toán"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1: Chọn hóa đơn
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        JLabel lblHoaDon = new JLabel("Chọn Hóa Đơn:");
        lblHoaDon.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        pnl.add(lblHoaDon, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        cbHoaDon = new JComboBox<>();
        cbHoaDon.setPreferredSize(new Dimension(300, 30));
        cbHoaDon.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                if (value instanceof HoaDon) {
                    value = ((HoaDon) value).getMaHD();
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        cbHoaDon.addActionListener(e -> loadHoaDonInfo());
        pnl.add(cbHoaDon, gbc);

        // Row 2: Trạng thái thanh toán
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        JLabel lblTrangThai = new JLabel("Trạng Thái Mới:");
        lblTrangThai.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        pnl.add(lblTrangThai, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        cbTrangThai = new JComboBox<>(new String[]{
                "Chờ thanh toán", "Đã thanh toán", "Thanh toán thất bại", "Hoàn tiền"
        });
        cbTrangThai.setPreferredSize(new Dimension(300, 30));
        cbTrangThai.setSelectedItem("Đã thanh toán");
        pnl.add(cbTrangThai, gbc);

        // Row 3: Tổng tiền
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        JLabel lblTienLabel = new JLabel("Tổng Tiền:");
        lblTienLabel.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        pnl.add(lblTienLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        lblTongTien = new JLabel("0 đ");
        lblTongTien.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblTongTien.setForeground(ColorPalette.PRIMARY);
        pnl.add(lblTongTien, gbc);

        // Row 4: Button - chỉ cập nhật trạng thái
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        btnCapNhatTrangThai = new JButton("Cập Nhật Trạng Thái");
        ButtonStyle.stylePrimaryButton(btnCapNhatTrangThai);
        btnCapNhatTrangThai.addActionListener(e -> capNhatTrangThai());
        pnl.add(btnCapNhatTrangThai, gbc);

        // Row 5: Status message
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        lblStatusMessage = new JLabel(" ");
        lblStatusMessage.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL - 2, Font.PLAIN));
        lblStatusMessage.setForeground(ColorPalette.STATUS_SUCCESS);
        pnl.add(lblStatusMessage, gbc);

        return pnl;
    }

    private JPanel createTablePanel() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(BorderFactory.createTitledBorder("Lịch Sử Thanh Toán"));

        String[] columns = {"Mã TT", "Mã HĐ", "Phương Thức", "Trạng Thái", "Ngày Thanh Toán", "Hành Động"};
        modelTable = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // chỉ cột hành động có thể click
            }
        };
        tblThanhToan = new JTable(modelTable);
        tblThanhToan.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        tblThanhToan.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tblThanhToan);
        pnl.add(scroll, BorderLayout.CENTER);

        return pnl;
    }

    /* ======================= LOAD DATA ======================= */

    private void loadDuLieu() {
        try {
            cbHoaDon.removeAllItems();
            List<HoaDon> list = hoaDonService.getAllHoaDon();
            if (list != null && !list.isEmpty()) {
                for (HoaDon hd : list) {
                    cbHoaDon.addItem(hd);
                }
                loadThanhToanList();
                lblStatusMessage.setText("Tải dữ liệu thành công");
                lblStatusMessage.setForeground(ColorPalette.STATUS_SUCCESS);
            } else {
                lblStatusMessage.setText("Không có hóa đơn nào");
                lblStatusMessage.setForeground(ColorPalette.STATUS_WARNING);
            }
        } catch (Exception e) {
            lblStatusMessage.setText("Lỗi tải hóa đơn: " + e.getMessage());
            lblStatusMessage.setForeground(ColorPalette.STATUS_ERROR);
        }
    }

    private void loadHoaDonInfo() {
        HoaDon hd = (HoaDon) cbHoaDon.getSelectedItem();
        if (hd != null && hd.getThanhToan() != null) {
            lblTongTien.setText(String.format("%,.0f đ", hd.getThanhToan().doubleValue()));
        } else {
            lblTongTien.setText("0 đ");
        }
    }

    private void loadThanhToanList() {
        modelTable.setRowCount(0);
        try {
            List<ThanhToan> list = thanhToanService.getAllThanhToan();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            if (list != null) {
                for (ThanhToan tt : list) {
                    String ngayTT = "";
                    if (tt.getNgayThanhToan() != null) {
                        ngayTT = sdf.format(java.sql.Timestamp.valueOf(tt.getNgayThanhToan()));
                    }
                    modelTable.addRow(new Object[]{
                            tt.getMaThanhToan(),
                            tt.getMaHD(),
                            tt.getPhuongThuc(),
                            tt.getTrangThai(),
                            ngayTT,
                            "Chi tiết"
                    });
                }
            }
        } catch (Exception e) {
            lblStatusMessage.setText("Lỗi tải lịch sử: " + e.getMessage());
            lblStatusMessage.setForeground(ColorPalette.STATUS_ERROR);
        }
    }

    /* ======================= HÀM HỖ TRỢ: CHỌN HÓA ĐƠN THEO MÃ ======================= */

    /**
     * Cho phép các màn khác (vd: nvFrmBanVePanel) yêu cầu panel này
     * tự chọn đúng hóa đơn trong combobox theo mã.
     */
    public void chonHoaDonTheoMa(String maHD) {
        if (maHD == null || maHD.trim().isEmpty()) return;

        for (int i = 0; i < cbHoaDon.getItemCount(); i++) {
            HoaDon hd = cbHoaDon.getItemAt(i);
            if (hd != null && maHD.equals(hd.getMaHD())) {
                cbHoaDon.setSelectedIndex(i);
                loadHoaDonInfo(); // cập nhật tổng tiền
                break;
            }
        }
    }

    /* ======================= CẬP NHẬT TRẠNG THÁI ======================= */

    private void capNhatTrangThai() {
        int selectedRow = tblThanhToan.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn một bản ghi thanh toán trong bảng để cập nhật",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String maThanhToan = (String) modelTable.getValueAt(selectedRow, 0);
        String trangThaiMoi = (String) cbTrangThai.getSelectedItem();

        try {
            boolean success = thanhToanService.updateTrangThai(maThanhToan, trangThaiMoi);
            if (success) {
                JOptionPane.showMessageDialog(
                        this,
                        "Cập nhật trạng thái thành công!",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE
                );
                lblStatusMessage.setText("Cập nhật trạng thái thành công");
                lblStatusMessage.setForeground(ColorPalette.STATUS_SUCCESS);
                loadThanhToanList();
                cbHoaDon.setSelectedIndex(-1);
                cbTrangThai.setSelectedItem("Đã thanh toán");
                lblTongTien.setText("0 đ");
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Cập nhật trạng thái thất bại",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                );
                lblStatusMessage.setText("Cập nhật trạng thái thất bại");
                lblStatusMessage.setForeground(ColorPalette.STATUS_ERROR);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Lỗi: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
            lblStatusMessage.setText("Lỗi: " + e.getMessage());
            lblStatusMessage.setForeground(ColorPalette.STATUS_ERROR);
        }
    }
}
