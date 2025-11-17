package iuh.fit.se.nhom10.view.nhanvien;

import iuh.fit.se.nhom10.dao.KhachHangDAO;
import iuh.fit.se.nhom10.dao.KhuyenMaiDAO;
import iuh.fit.se.nhom10.model.ChiTietHoaDon;
import iuh.fit.se.nhom10.model.GheNgoi;
import iuh.fit.se.nhom10.model.HoaDon;
import iuh.fit.se.nhom10.model.KhachHang;
import iuh.fit.se.nhom10.model.KhuyenMai;
import iuh.fit.se.nhom10.model.LichChieu;
import iuh.fit.se.nhom10.model.Phim;
import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.service.nvHoaDonService;
import iuh.fit.se.nhom10.util.ColorPalette;
import iuh.fit.se.nhom10.util.KetNoi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

/**
 * Panel THÔNG TIN ĐẶT VÉ
 *
 * - Không còn danh sách phim/lịch/ghế bên trái.
 * - Chỉ hiển thị form thông tin vé + khách hàng.
 * - Dữ liệu phim + lịch chiếu + ghế có thể được truyền từ màn khác.
 */
public class nvFrmBanVePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private TaiKhoanNhanVien nhanVienHienTai;

    // Thông tin phim / lịch / ghế
    private JTextField txtMaPhim;
    private JTextField txtTenPhim;
    private JTextField txtMaLich;
    private JTextField txtNgayChieu;
    private JTextField txtGioBatDau;
    private JTextField txtPhong;

    private JTextField txtMaGhe;

    // Thông tin vé / khách -> DÙNG COMBOBOX
    private JComboBox<String> cboDonGia;   // đơn giá
    private JComboBox<String> cboMaKM;     // mã khuyến mãi
    private JComboBox<String> cboMaKH;     // mã khách hàng
    private JCheckBox chkKhachVangLai;

    // Nút hành động
    private JButton btnDatVe;

    // Định dạng ngày/giờ
    private SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm");

    /* ======================= CONSTRUCTORS ======================= */

    /**
     * Dùng khi mở từ MENU "Bán vé" trong FrmNhanVienMenuFrame.
     */
    public nvFrmBanVePanel(TaiKhoanNhanVien nhanVienHienTai) throws Exception {
        this.nhanVienHienTai = nhanVienHienTai;
        setupUI();
        loadComboDataFromDatabase();   // load dữ liệu cho combobox
        initEvents();
    }

    // Constructor rỗng cho designer nếu cần
    public nvFrmBanVePanel() {
    }

    /**
     * Dùng khi mở từ "Chọn ghế & bán vé" ở màn xem phim.
     * Tự động set thông tin phim + lịch + ghế vào form.
     */
    public nvFrmBanVePanel(TaiKhoanNhanVien nhanVienHienTai,
                           Phim phim,
                           LichChieu lichChieu,
                           GheNgoi ghe) throws Exception {
        this(nhanVienHienTai);
        setThongTinVe(phim, lichChieu, ghe);
    }

    /**
     * Cho phép các lớp khác đẩy dữ liệu phim + lịch chiếu + ghế vào form.
     */
    public void setThongTinVe(Phim phim, LichChieu lichChieu, GheNgoi ghe) {
        if (phim != null) {
            txtMaPhim.setText(phim.getMaPhim());
            txtTenPhim.setText(phim.getTenPhim());
        }

        if (lichChieu != null) {
            txtMaLich.setText(lichChieu.getMaLich());
            try {
                if (lichChieu.getNgayChieu() != null) {
                    txtNgayChieu.setText(dfDate.format(lichChieu.getNgayChieu()));
                }
                if (lichChieu.getGioBatDau() != null) {
                    txtGioBatDau.setText(dfTime.format(lichChieu.getGioBatDau()));
                }
            } catch (Exception ignored) {}
            txtPhong.setText(lichChieu.getMaPhong());
        }

        if (ghe != null) {
            txtMaGhe.setText(ghe.getMaGhe());
            // Không dùng trạng thái ghế nữa
        }
    }

    /* ======================= UI SETUP ======================= */

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(ColorPalette.BACKGROUND_CONTENT);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnlHeader.setBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, ColorPalette.BORDER_LIGHT)
        );

        JLabel lblTitle = new JLabel("🎟️ THÔNG TIN ĐẶT VÉ");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE + 2, Font.BOLD));
        lblTitle.setForeground(ColorPalette.PRIMARY);

        String tenNV = nhanVienHienTai != null ? nhanVienHienTai.getNhanVien().getTenNV() : "";
        JLabel lblNV = new JLabel("Nhân viên: " + tenNV);
        lblNV.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        lblNV.setForeground(ColorPalette.TEXT_PLACEHOLDER);

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(lblNV, BorderLayout.EAST);

        add(pnlHeader, BorderLayout.NORTH);

        // CENTER: card thông tin đặt vé
        JPanel pnlCenter = new JPanel();
        pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
        pnlCenter.setBackground(ColorPalette.BACKGROUND_CONTENT);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(ColorPalette.BACKGROUND_MAIN);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel lblInfo = new JLabel("Chi tiết vé xem phim");
        lblInfo.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE, Font.BOLD));
        lblInfo.setForeground(ColorPalette.TEXT_LABEL);

        card.add(lblInfo);
        card.add(Box.createVerticalStrut(10));

        // ----- THÔNG TIN PHIM / LỊCH -----
        JPanel pnlInfoPhimLich = new JPanel(new GridLayout(3, 2, 8, 8));
        pnlInfoPhimLich.setOpaque(false);

        pnlInfoPhimLich.add(createFormLabel("Mã phim:"));
        txtMaPhim = createFormTextField(false);
        pnlInfoPhimLich.add(txtMaPhim);

        pnlInfoPhimLich.add(createFormLabel("Tên phim:"));
        txtTenPhim = createFormTextField(false);
        pnlInfoPhimLich.add(txtTenPhim);

        pnlInfoPhimLich.add(createFormLabel("Mã lịch chiếu:"));
        txtMaLich = createFormTextField(false);
        pnlInfoPhimLich.add(txtMaLich);

        pnlInfoPhimLich.add(createFormLabel("Ngày chiếu:"));
        txtNgayChieu = createFormTextField(false);
        pnlInfoPhimLich.add(txtNgayChieu);

        pnlInfoPhimLich.add(createFormLabel("Giờ bắt đầu:"));
        txtGioBatDau = createFormTextField(false);
        pnlInfoPhimLich.add(txtGioBatDau);

        pnlInfoPhimLich.add(createFormLabel("Phòng chiếu:"));
        txtPhong = createFormTextField(false);
        pnlInfoPhimLich.add(txtPhong);

        card.add(pnlInfoPhimLich);
        card.add(Box.createVerticalStrut(10));

        // ----- THÔNG TIN GHẾ -----
        JPanel pnlInfoGhe = new JPanel(new GridLayout(1, 2, 8, 8));
        pnlInfoGhe.setOpaque(false);

        pnlInfoGhe.add(createFormLabel("Mã ghế:"));
        txtMaGhe = createFormTextField(false);
        pnlInfoGhe.add(txtMaGhe);

        card.add(pnlInfoGhe);
        card.add(Box.createVerticalStrut(10));

        // ----- GIÁ & KHUYẾN MÃI (DÙNG COMBOBOX) -----
        JPanel pnlGia = new JPanel(new GridLayout(2, 2, 8, 8));
        pnlGia.setOpaque(false);

        pnlGia.add(createFormLabel("Đơn giá (VNĐ):"));
        cboDonGia = createComboBox();
        pnlGia.add(cboDonGia);

        pnlGia.add(createFormLabel("Mã khuyến mãi (nếu có):"));
        cboMaKM = createComboBox();
        pnlGia.add(cboMaKM);

        card.add(pnlGia);
        card.add(Box.createVerticalStrut(10));

        // ----- KHÁCH HÀNG (DÙNG COMBOBOX) -----
        JPanel pnlKH = new JPanel();
        pnlKH.setLayout(new BoxLayout(pnlKH, BoxLayout.Y_AXIS));
        pnlKH.setOpaque(false);

        JPanel rowKH = new JPanel(new BorderLayout(8, 0));
        rowKH.setOpaque(false);

        rowKH.add(createFormLabel("Mã khách hàng:"), BorderLayout.WEST);
        cboMaKH = createComboBox();
        rowKH.add(cboMaKH, BorderLayout.CENTER);

        JPanel rowVangLai = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rowVangLai.setOpaque(false);
        chkKhachVangLai = new JCheckBox("Khách vãng lai");
        chkKhachVangLai.setOpaque(false);
        chkKhachVangLai.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        chkKhachVangLai.setForeground(ColorPalette.TEXT_LABEL);
        rowVangLai.add(chkKhachVangLai);

        pnlKH.add(rowKH);
        pnlKH.add(Box.createVerticalStrut(4));
        pnlKH.add(rowVangLai);

        card.add(pnlKH);
        card.add(Box.createVerticalStrut(15));

        // ----- BUTTONS -----
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pnlButtons.setOpaque(false);

        btnDatVe = createMenuLikeButton("Đặt vé");
        pnlButtons.add(btnDatVe);

        card.add(pnlButtons);

        pnlCenter.add(card);
        pnlCenter.add(Box.createVerticalGlue());

        add(pnlCenter, BorderLayout.CENTER);
    }

    /* ======================= UI HELPERS ======================= */

    private JButton createMenuLikeButton(String text) {
        JButton btn = new JButton(text);
        btn.putClientProperty("JButton.buttonType", "roundRect");
        btn.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        btn.setBackground(ColorPalette.BUTTON_PRIMARY_BG);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 36));
        btn.setBorder(new EmptyBorder(6, 16, 6, 16));
        return btn;
    }

    private JLabel createFormLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        lbl.setForeground(ColorPalette.TEXT_LABEL);
        return lbl;
    }

    private JTextField createFormTextField(boolean editable) {
        JTextField txt = new JTextField();
        txt.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        txt.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        txt.setEditable(editable);
        txt.setBackground(editable ? Color.WHITE : ColorPalette.BACKGROUND_CONTENT);
        return txt;
    }

    private JComboBox<String> createComboBox() {
        JComboBox<String> cbo = new JComboBox<>();
        cbo.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        cbo.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        cbo.setBackground(Color.WHITE);
        cbo.setEditable(false);
        return cbo;
    }

    /* ======================= LOAD DATA COMBOBOX TỪ DATABASE ======================= */

    private void loadComboDataFromDatabase() {
        // ===== ĐƠN GIÁ (lấy DISTINCT từ bảng VeXemPhim) =====
        cboDonGia.removeAllItems();
        try {
            Connection conn = KetNoi.getInstance().getConnection();
            String sqlGia = "SELECT DISTINCT donGia FROM VeXemPhim ORDER BY donGia";
            try (PreparedStatement ps = conn.prepareStatement(sqlGia);
                 ResultSet rs = ps.executeQuery()) {
                boolean hasData = false;
                while (rs.next()) {
                    String gia = rs.getBigDecimal("donGia").toPlainString();
                    cboDonGia.addItem(gia);
                    hasData = true;
                }
                if (!hasData) {
                    cboDonGia.addItem("80000");
                    cboDonGia.addItem("90000");
                    cboDonGia.addItem("100000");
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi tải đơn giá: " + e.getMessage());
            if (cboDonGia.getItemCount() == 0) {
                cboDonGia.addItem("80000");
                cboDonGia.addItem("90000");
                cboDonGia.addItem("100000");
            }
        }

        // ===== KHUYẾN MÃI =====
        cboMaKM.removeAllItems();
        cboMaKM.addItem(""); // dòng đầu: không áp dụng khuyến mãi
        try {
            KhuyenMaiDAO kmDao = new KhuyenMaiDAO();
            for (KhuyenMai km : kmDao.getAllKhuyenMai()) {
                cboMaKM.addItem(km.getMaKM());
            }
        } catch (Exception e) {
            System.out.println("Lỗi tải khuyến mãi: " + e.getMessage());
        }

        // ===== KHÁCH HÀNG =====
        cboMaKH.removeAllItems();
        try {
            KhachHangDAO khDao = new KhachHangDAO();
            for (KhachHang kh : khDao.getAllKhachHang()) {
                cboMaKH.addItem(kh.getMaKH());
            }
        } catch (Exception e) {
            System.out.println("Lỗi tải khách hàng: " + e.getMessage());
        }
    }

    /* ======================= EVENTS ======================= */

    private void initEvents() {
        chkKhachVangLai.addActionListener(e -> {
            boolean vangLai = chkKhachVangLai.isSelected();
            cboMaKH.setEnabled(!vangLai);
            if (vangLai) {
                cboMaKH.setSelectedIndex(-1); // bỏ chọn khách hàng khi là khách vãng lai
            }
        });

        // Bấm "Đặt vé" -> xử lý và MỞ HÓA ĐƠN
        btnDatVe.addActionListener(e -> datVe(true));
    }

    /* ======================= ĐẶT VÉ (LOGIC CHUNG) ======================= */

    private void datVe(boolean createHoaDon) {
        String maPhim = txtMaPhim.getText().trim();
        String maLich = txtMaLich.getText().trim();
        String maGhe = txtMaGhe.getText().trim();

        String donGiaStr = cboDonGia.getSelectedItem() != null
                ? cboDonGia.getSelectedItem().toString().trim()
                : "";

        String maKM = cboMaKM.getSelectedItem() != null
                ? cboMaKM.getSelectedItem().toString().trim()
                : "";

        String maKH = (!chkKhachVangLai.isSelected() && cboMaKH.getSelectedItem() != null)
                ? cboMaKH.getSelectedItem().toString().trim()
                : null;

        // ====== VALIDATE CƠ BẢN ======
        if (maPhim.isEmpty() || maLich.isEmpty() || maGhe.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng điền đầy đủ thông tin phim, lịch chiếu và ghế trước khi đặt vé.",
                    "Thiếu thông tin",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (donGiaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn đơn giá vé.",
                    "Thiếu thông tin",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double donGiaDouble;
        try {
            donGiaDouble = Double.parseDouble(donGiaStr);
            if (donGiaDouble <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Đơn giá không hợp lệ.",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!chkKhachVangLai.isSelected() && (maKH == null || maKH.isEmpty())) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn mã khách hàng hoặc chọn 'Khách vãng lai'.",
                    "Thiếu thông tin",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            BigDecimal donGia = BigDecimal.valueOf(donGiaDouble);
            BigDecimal tongTien = donGia;
            
            BigDecimal giamGia = BigDecimal.ZERO;
            if (maKM != null && !maKM.isEmpty()) {
                try {
                    KhuyenMaiDAO kmDao = new KhuyenMaiDAO();
                    KhuyenMai km = kmDao.getKhuyenMaiByMa(maKM);
                    
                    if (km != null && km.isHoatDong()) {
                        // Calculate discount: tiLeGiam is a percentage (0-100)
                        double tiLeGiam = km.getTiLeGiam();
                        giamGia = donGia.multiply(BigDecimal.valueOf(tiLeGiam / 100.0));
                    }
                } catch (Exception e) {
                    System.out.println("Lỗi tính giảm giá: " + e.getMessage());
                }
            }
            
            BigDecimal thanhToan = tongTien.subtract(giamGia);

            // ====== TẠO MÃ VÉ & MÃ HÓA ĐƠN ======
            String maVe = generateMaVe();
            String maHDVuaTao = generateMaHD();

            // ====== 1. LƯU VÉ VÀO BẢNG VeXemPhim ======
            Connection conn = null;
            try {
                conn = KetNoi.getInstance().getConnection();   // KHÔNG bọc trong try(...)

                String sqlVe = "INSERT INTO VeXemPhim (maVe, maLich, maGhe, donGia) " +
                               "VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sqlVe)) {
                    ps.setString(1, maVe);
                    ps.setString(2, maLich);
                    ps.setString(3, maGhe);
                    ps.setBigDecimal(4, donGia);
                    ps.executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e; // hoặc xử lý theo ý anh
            }
            // KHÔNG được conn.close() ở đây vì connection là singleton dùng chung


            // ====== 2. TẠO HÓA ĐƠN + CHI TIẾT HÓA ĐƠN ======
            nvHoaDonService hdService = new nvHoaDonService();

            HoaDon hd = new HoaDon();
            hd.setMaHD(maHDVuaTao);
            hd.setNgayLap(LocalDateTime.now());
            hd.setTongTien(tongTien);
            hd.setGiamGia(giamGia);
            hd.setThanhToan(thanhToan);

            if (nhanVienHienTai != null && nhanVienHienTai.getNhanVien() != null) {
                hd.setMaNV(nhanVienHienTai.getNhanVien().getMaNV());
            }
            hd.setMaKH(maKH); // có thể null nếu khách vãng lai
            hd.setMaKM(maKM != null && !maKM.isEmpty() ? maKM : null);

            // Lưu hóa đơn
            hdService.taoHoaDon(hd);

            // Chi tiết hóa đơn: 1 vé
            ChiTietHoaDon ct = new ChiTietHoaDon();
            ct.setMaHD(maHDVuaTao);
            ct.setMaVe(maVe);
            ct.setDonGia(donGia);
            hdService.themChiTietHoaDon(ct);

            // ====== THÔNG BÁO ======
            JOptionPane.showMessageDialog(this,
                    "Đặt vé thành công.\nMã hóa đơn: " + maHDVuaTao + "\nMã vé: " + maVe,
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);

            // ====== 3. MỞ MÀN HÌNH HÓA ĐƠN VÀ CHỌN DÒNG VỪA TẠO ======
            if (createHoaDon) {
                nvFrmHoaDonPanel pnlHD = new nvFrmHoaDonPanel(nhanVienHienTai, maHDVuaTao);

                JFrame frame = new JFrame("Hóa đơn");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setContentPane(pnlHD);
                frame.pack();
                frame.setLocationRelativeTo(this);
                frame.setVisible(true);

                // Nếu panel đang nằm trong dialog thì đóng dialog đặt vé
                java.awt.Window w = SwingUtilities.getWindowAncestor(this);
                if (w instanceof JDialog) {
                    w.dispose();
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi đặt vé: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /* ======================= HÀM HỖ TRỢ SINH MÃ ======================= */

    // Ví dụ: cột maHD là CHAR(8) -> "HD" + 6 số = 8 ký tự
    private String generateMaHD() {
        long millis = System.currentTimeMillis();
        // Lấy 6 số cuối cùng của millis
        String sixDigits = String.format("%06d", (millis % 1_000_000L));
        return "HD" + sixDigits;   // VD: HD123456 (8 ký tự)
    }


    private String generateMaVe() {
        long millis = System.currentTimeMillis();
        // lấy 6 số cuối của millis
        String sixDigits = String.format("%06d", (millis % 1_000_000L));
        return "VE" + sixDigits;   // "VE" + 6 số = 8 ký tự
    }

    /* ======================= HỖ TRỢ MỞ DẠNG DIALOG ======================= */

    public static void showAsDialog(TaiKhoanNhanVien nv,
                                    Phim phim,
                                    LichChieu lich,
                                    GheNgoi ghe) throws Exception {

        nvFrmBanVePanel panel = new nvFrmBanVePanel(nv, phim, lich, ghe);

        JDialog dialog = new JDialog((Frame) null, "Đặt vé", true); // modal = true
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(panel);
        dialog.setSize(600, 700);
        dialog.setLocationRelativeTo(null); // mở giữa màn hình
        dialog.setVisible(true);
    }
}
