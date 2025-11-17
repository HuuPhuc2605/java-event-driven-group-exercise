package iuh.fit.se.nhom10.view.nhanvien;

import iuh.fit.se.nhom10.model.HoaDon;
import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.service.nvHoaDonService;
import iuh.fit.se.nhom10.util.ColorPalette;
import iuh.fit.se.nhom10.util.ButtonStyle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Panel quản lý hóa đơn cho nhân viên
 * Chỉ HIỂN THỊ DANH SÁCH HÓA ĐƠN (theo đúng UI bạn gửi hình):
 * - Thanh tìm kiếm
 * - Bảng hóa đơn
 * - Dòng trạng thái
 */
public class nvFrmHoaDonPanel extends JPanel {

    private TaiKhoanNhanVien nhanVienHienTai;
    private nvHoaDonService hoaDonService;

    private JTable tblHoaDon;
    private DefaultTableModel modelTable;
    private JLabel lblStatusMessage;

    /* ======================= CONSTRUCTORS ======================= */

    // Dùng bình thường từ menu
    public nvFrmHoaDonPanel(TaiKhoanNhanVien nhanVien) throws Exception {
        this.nhanVienHienTai = nhanVien;
        this.hoaDonService = new nvHoaDonService();
        setupUI();
        loadDuLieu();
    }

    // Dùng khi vừa tạo xong 1 hóa đơn mới và muốn focus luôn vào hóa đơn đó
    public nvFrmHoaDonPanel(TaiKhoanNhanVien nhanVien, String maHDCanChon) throws Exception {
        this(nhanVien); // setup UI + load dữ liệu
        if (maHDCanChon != null && !maHDCanChon.trim().isEmpty()) {
            chonHoaDonTheoMa(maHDCanChon);
        }
    }

    /* ======================= UI SETUP ======================= */

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(ColorPalette.BACKGROUND_CONTENT);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel pnlSearch = createSearchPanel();
        add(pnlSearch, BorderLayout.NORTH);

        JPanel pnlTable = createTablePanel();
        add(pnlTable, BorderLayout.CENTER);

        JPanel pnlStatus = createStatusPanel();
        add(pnlStatus, BorderLayout.SOUTH);
    }

    /* ======================= SEARCH PANEL ======================= */

    private JPanel createSearchPanel() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ColorPalette.BORDER_LIGHT));

        JLabel lblSearch = new JLabel("Tìm kiếm (Mã HĐ / Mã KH / Mã NV):");
        lblSearch.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));

        JTextField txtSearch = new JTextField(15);
        txtSearch.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));

        JButton btnSearch = ButtonStyle.createPrimaryButton("Tìm");
        btnSearch.addActionListener(e -> searchHoaDon(txtSearch.getText().trim()));

        JButton btnRefresh = ButtonStyle.createSecondaryButton("Làm mới");
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadDuLieu();
        });

        pnl.add(lblSearch);
        pnl.add(txtSearch);
        pnl.add(btnSearch);
        pnl.add(btnRefresh);

        return pnl;
    }

    /* ======================= TABLE PANEL ======================= */

    private JPanel createTablePanel() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

        String[] columns = {"Mã HĐ", "Mã NV", "Mã KH", "Ngày Lập", "Tổng Tiền", "Giảm Giá", "Thanh Toán"};
        modelTable = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblHoaDon = new JTable(modelTable);
        tblHoaDon.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        tblHoaDon.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tblHoaDon);
        scroll.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.add(scroll, BorderLayout.CENTER);

        return pnl;
    }

    /* ======================= STATUS PANEL ======================= */

    private JPanel createStatusPanel() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, ColorPalette.BORDER_LIGHT));

        lblStatusMessage = new JLabel("Sẵn sàng");
        lblStatusMessage.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        lblStatusMessage.setForeground(ColorPalette.TEXT_LABEL);
        pnl.add(lblStatusMessage);

        return pnl;
    }

    /* ======================= LOAD & SEARCH ======================= */

    private void loadDuLieu() {
        try {
            modelTable.setRowCount(0);
            List<HoaDon> list = hoaDonService.layTatCaHoaDon();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            if (list != null && !list.isEmpty()) {
                for (HoaDon hd : list) {
                    String ngayLap = "";
                    if (hd.getNgayLap() != null) {
                        ngayLap = sdf.format(java.sql.Timestamp.valueOf(hd.getNgayLap()));
                    }
                    double tong = hd.getTongTien() != null ? hd.getTongTien().doubleValue() : 0;
                    double giam = hd.getGiamGia() != null ? hd.getGiamGia().doubleValue() : 0;
                    double tt = hd.getThanhToan() != null ? hd.getThanhToan().doubleValue() : 0;

                    modelTable.addRow(new Object[]{
                            hd.getMaHD(),
                            hd.getMaNV() != null ? hd.getMaNV() : "",
                            hd.getMaKH() != null ? hd.getMaKH() : "Khách vãng lai",
                            ngayLap,
                            String.format("%,.0f", tong),
                            String.format("%,.0f", giam),
                            String.format("%,.0f", tt)
                    });
                }
                lblStatusMessage.setText("Tải dữ liệu thành công: " + list.size() + " hóa đơn");
                lblStatusMessage.setForeground(ColorPalette.STATUS_SUCCESS);
            } else {
                lblStatusMessage.setText("Không có hóa đơn nào");
                lblStatusMessage.setForeground(ColorPalette.STATUS_WARNING);
            }
        } catch (Exception e) {
            lblStatusMessage.setText("Lỗi: " + e.getMessage());
            lblStatusMessage.setForeground(ColorPalette.STATUS_ERROR);
        }
    }

    private void searchHoaDon(String keyword) {
        keyword = keyword == null ? "" : keyword.trim();
        if (keyword.isEmpty()) {
            loadDuLieu();
            return;
        }

        try {
            modelTable.setRowCount(0);
            List<HoaDon> list = hoaDonService.layTatCaHoaDon();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            int count = 0;
            if (list != null) {
                for (HoaDon hd : list) {
                    boolean match =
                            (hd.getMaHD() != null && hd.getMaHD().contains(keyword)) ||
                            (hd.getMaKH() != null && hd.getMaKH().contains(keyword)) ||
                            (hd.getMaNV() != null && hd.getMaNV().contains(keyword));

                    if (match) {
                        String ngayLap = "";
                        if (hd.getNgayLap() != null) {
                            ngayLap = sdf.format(java.sql.Timestamp.valueOf(hd.getNgayLap()));
                        }
                        double tong = hd.getTongTien() != null ? hd.getTongTien().doubleValue() : 0;
                        double giam = hd.getGiamGia() != null ? hd.getGiamGia().doubleValue() : 0;
                        double tt = hd.getThanhToan() != null ? hd.getThanhToan().doubleValue() : 0;

                        modelTable.addRow(new Object[]{
                                hd.getMaHD(),
                                hd.getMaNV() != null ? hd.getMaNV() : "",
                                hd.getMaKH() != null ? hd.getMaKH() : "Khách vãng lai",
                                ngayLap,
                                String.format("%,.0f", tong),
                                String.format("%,.0f", giam),
                                String.format("%,.0f", tt)
                        });
                        count++;
                    }
                }
            }

            if (count == 0) {
                lblStatusMessage.setText("Không tìm thấy hóa đơn với từ khóa: " + keyword);
                lblStatusMessage.setForeground(ColorPalette.STATUS_WARNING);
            } else {
                lblStatusMessage.setText("Tìm thấy " + count + " hóa đơn");
                lblStatusMessage.setForeground(ColorPalette.STATUS_SUCCESS);
            }
        } catch (Exception e) {
            lblStatusMessage.setText("Lỗi tìm kiếm: " + e.getMessage());
            lblStatusMessage.setForeground(ColorPalette.STATUS_ERROR);
        }
    }

    /* ======================= HỖ TRỢ TỪ MÀN KHÁC ======================= */

    /**
     * Cho phép chọn hóa đơn theo mã trên bảng (dùng sau khi vừa tạo HĐ xong).
     */
    public void chonHoaDonTheoMa(String maHD) {
        if (maHD == null || maHD.trim().isEmpty()) return;

        for (int i = 0; i < modelTable.getRowCount(); i++) {
            Object val = modelTable.getValueAt(i, 0); // cột 0 = Mã HĐ
            if (val != null && maHD.equals(val.toString())) {
                final int row = i;
                SwingUtilities.invokeLater(() -> {
                    tblHoaDon.setRowSelectionInterval(row, row);
                    tblHoaDon.scrollRectToVisible(tblHoaDon.getCellRect(row, 0, true));
                });
                break;
            }
        }
    }

    public List<HoaDon> layTatCaHoaDon() {
        return hoaDonService.layTatCaHoaDon();
    }
}
