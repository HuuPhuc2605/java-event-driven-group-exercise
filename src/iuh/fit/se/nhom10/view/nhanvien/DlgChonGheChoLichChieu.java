package iuh.fit.se.nhom10.view.nhanvien;

import iuh.fit.se.nhom10.dao.GheNgoiDAO;
import iuh.fit.se.nhom10.model.GheNgoi;
import iuh.fit.se.nhom10.model.LichChieu;
import iuh.fit.se.nhom10.model.Phim;
import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.util.ColorPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Dialog chọn ghế theo lịch chiếu để bán vé.
 * - Hiển thị sơ đồ ghế giống rạp phim (nút ghế theo hàng).
 * - KHÔNG sử dụng trạng thái ghế nữa.
 */
public class DlgChonGheChoLichChieu extends JDialog {

    private static final long serialVersionUID = 1L;

    // Màu ghế
    private static final Color SEAT_AVAILABLE_COLOR = ColorPalette.SEAT_EMPTY;
    private static final Color SEAT_SELECTED_COLOR = ColorPalette.SEAT_SELECTED;

    private final TaiKhoanNhanVien nhanVienHienTai;
    private final Phim phim;
    private final LichChieu lichChieu;

    // Sơ đồ ghế
    private JPanel pnlSoDoGhe;
    private Set<GheNgoi> gheDangChonSet;
    private Map<String, JButton> seatButtonMap;

    private JButton btnBanVe;
    private JButton btnDong;

    private GheNgoiDAO gheNgoiDAO;

    public DlgChonGheChoLichChieu(
            Window owner,
            TaiKhoanNhanVien nhanVienHienTai,
            Phim phim,
            LichChieu lichChieu
    ) {
        super(owner, "Chọn ghế - " + (phim != null ? phim.getTenPhim() : ""), ModalityType.APPLICATION_MODAL);
        this.nhanVienHienTai = nhanVienHienTai;
        this.phim = phim;
        this.lichChieu = lichChieu;

        this.gheNgoiDAO = new GheNgoiDAO();
        
        this.gheDangChonSet = new HashSet<>();
        this.seatButtonMap = new HashMap<>();

        setupUI();
        loadDanhSachGhe();

        setLocationRelativeTo(owner);
    }

    /* ======================= UI ======================= */

    private void setupUI() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(ColorPalette.BACKGROUND_CONTENT);

        JLabel lblTitle = new JLabel("Chọn ghế cho lịch chiếu");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE, Font.BOLD));
        lblTitle.setForeground(ColorPalette.PRIMARY);

        String infoPhim = (phim != null ? phim.getTenPhim() : "") +
                " | Lịch: " + (lichChieu != null ? lichChieu.getMaLich() : "");
        JLabel lblInfo = new JLabel(infoPhim);
        lblInfo.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        lblInfo.setForeground(ColorPalette.TEXT_PLACEHOLDER);

        pnlHeader.add(lblTitle, BorderLayout.NORTH);
        pnlHeader.add(lblInfo, BorderLayout.SOUTH);

        JPanel pnlSelectedInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSelectedInfo.setBackground(ColorPalette.BACKGROUND_CONTENT);
        JLabel lblSelectedCount = new JLabel("Ghế được chọn: 0");
        lblSelectedCount.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        lblSelectedCount.setForeground(ColorPalette.PRIMARY);
        pnlSelectedInfo.add(lblSelectedCount);

        // Panel sơ đồ ghế (giống rạp phim)
        pnlSoDoGhe = new JPanel();
        pnlSoDoGhe.setLayout(new BoxLayout(pnlSoDoGhe, BoxLayout.Y_AXIS));
        pnlSoDoGhe.setOpaque(false);

        JScrollPane scr = new JScrollPane(pnlSoDoGhe);
        scr.getViewport().setBackground(ColorPalette.BACKGROUND_CONTENT);
        scr.setBorder(null);

        // Label màn hình
        JLabel lblManHinh = new JLabel("MÀN HÌNH", SwingConstants.CENTER);
        lblManHinh.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        lblManHinh.setForeground(ColorPalette.TEXT_PLACEHOLDER);
        lblManHinh.setBorder(new EmptyBorder(8, 0, 8, 0));

        JPanel pnlScreen = new JPanel(new BorderLayout());
        pnlScreen.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnlScreen.add(lblManHinh, BorderLayout.NORTH);

        // Footer buttons
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pnlFooter.setBackground(ColorPalette.BACKGROUND_CONTENT);

        btnBanVe = createMenuLikeButton("Bán vé");
        btnDong = createMenuLikeButton("Đóng");

        pnlFooter.add(btnBanVe);
        pnlFooter.add(btnDong);

        // Để sơ đồ ghế chiếm trung tâm, dùng BorderLayout lồng:
        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnlCenter.add(pnlScreen, BorderLayout.NORTH);
        pnlCenter.add(scr, BorderLayout.CENTER);

        pnlMain.add(pnlHeader, BorderLayout.NORTH);
        pnlMain.add(pnlSelectedInfo, BorderLayout.BEFORE_FIRST_LINE);
        pnlMain.add(pnlCenter, BorderLayout.CENTER);
        pnlMain.add(pnlFooter, BorderLayout.SOUTH);

        add(pnlMain, BorderLayout.CENTER);

        // Events
        btnDong.addActionListener(e -> dispose());
        btnBanVe.addActionListener(e -> moManHinhBanVe());
    }

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
        btn.setPreferredSize(new Dimension(120, 32));
        btn.setBorder(new EmptyBorder(4, 12, 4, 12));
        return btn;
    }

    /* ======================= DATA & SƠ ĐỒ GHẾ ======================= */

    private void loadDanhSachGhe() {
        if (lichChieu == null) return;

        String maPhong = lichChieu.getMaPhong();

        try {
            List<GheNgoi> allSeats = gheNgoiDAO.getGheByPhong(maPhong);

            // ==== LẤY DANH SÁCH GHẾ ĐÃ ĐẶT CHO LỊCH CHIẾU NÀY ==== // NEW
            List<String> bookedSeats = gheNgoiDAO.getBookedSeatsForScreening(lichChieu.getMaLich());

            // Xóa sơ đồ cũ
            pnlSoDoGhe.removeAll();
            gheDangChonSet.clear();
            seatButtonMap.clear();

            if (allSeats == null || allSeats.isEmpty()) {
                JLabel lblEmpty = new JLabel("Không có ghế nào trong phòng.", SwingConstants.CENTER);
                lblEmpty.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
                lblEmpty.setForeground(ColorPalette.TEXT_PLACEHOLDER);
                pnlSoDoGhe.add(lblEmpty);
            } else {
                // Gom ghế theo hàng, giữ thứ tự xuất hiện
                Map<String, JPanel> rowPanels = new LinkedHashMap<>();

                for (GheNgoi ghe : allSeats) {
                    String hang = ghe.getHang();

                    JPanel rowPanel = rowPanels.get(hang);
                    if (rowPanel == null) {
                        rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
                        rowPanel.setOpaque(false);

                        // Label hàng (A, B, C...)
                        JLabel lblHang = new JLabel(hang);
                        lblHang.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
                        lblHang.setForeground(ColorPalette.TEXT_PLACEHOLDER);
                        lblHang.setPreferredSize(new Dimension(30, 30));

                        rowPanel.add(lblHang);
                        rowPanels.put(hang, rowPanel);
                        pnlSoDoGhe.add(rowPanel);
                    }

                    // Tạo nút ghế
                    JButton btnSeat = createSeatButton(ghe);

                    // ===== ĐÁNH DẤU GHẾ ĐÃ ĐẶT & KHÔNG CHO ĐẶT LẠI ===== // NEW
                    boolean isBooked = bookedSeats != null && bookedSeats.contains(ghe.getMaGhe());
                    if (isBooked) {
                        // Màu xám cho ghế đã đặt (đã định nghĩa trong ColorPalette)
                        btnSeat.setBackground(ColorPalette.SEAT_BOOKED);
                        btnSeat.setForeground(new Color(255, 255, 255)); // White text on gray background for visibility
                        btnSeat.setEnabled(false); // không cho click/đặt lại
                        btnSeat.setToolTipText(btnSeat.getToolTipText() + " (ĐÃ ĐẶT)");
                    } else {
                        seatButtonMap.put(ghe.getMaGhe(), btnSeat);
                    }

                    rowPanel.add(btnSeat);
                }
            }

            pnlSoDoGhe.revalidate();
            pnlSoDoGhe.repaint();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tải danh sách ghế: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createSeatButton(GheNgoi ghe) {
        String text = ghe.getHang() + ghe.getCot();
        JButton btn = new JButton(text);

        btn.setFont(new Font("Segoe UI", Font.BOLD, 11)); // Bold font for better text visibility
        btn.setFocusPainted(false);
        btn.setMargin(new Insets(2, 2, 2, 2));
        btn.setPreferredSize(new Dimension(45, 38)); // Slightly larger for better visibility
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setToolTipText("Hàng " + ghe.getHang() + " - Ghế " + ghe.getCot());

        // Style ghế mặc định (chưa chọn) -> Xanh lá nhạt với text đen
        btn.setBackground(ColorPalette.SEAT_EMPTY);
        btn.setForeground(new Color(0, 0, 0)); // Black text for contrast on light green background
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));

        btn.addActionListener(e -> {
            if (gheDangChonSet.contains(ghe)) {
                // Bỏ chọn ghế
                gheDangChonSet.remove(ghe);
                btn.setBackground(ColorPalette.SEAT_EMPTY);
                btn.setForeground(new Color(0, 0, 0)); // Black text
                btn.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
            } else {
                // Chọn ghế
                gheDangChonSet.add(ghe);
                btn.setBackground(ColorPalette.SEAT_SELECTED);
                btn.setForeground(Color.WHITE); // White text on darker green background
                btn.setBorder(BorderFactory.createLineBorder(ColorPalette.PRIMARY, 2)); // Thicker border for selected
            }
            
            updateSelectedSeatsLabel();
        });

        return btn;
    }
    
    private void updateSelectedSeatsLabel() {
        // This will be called to update the label, but we need access to it
        // For now, we'll just print for debugging
        System.out.println("[v0] Selected seats: " + gheDangChonSet.size());
    }

    /* ======================= ACTION: MỞ MÀN HÌNH BÁN VÉ ======================= */

    private void moManHinhBanVe() {
        if (gheDangChonSet == null || gheDangChonSet.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn ít nhất 1 ghế để bán vé.",
                    "Chưa chọn ghế",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // This allows booking multiple tickets at once
            for (GheNgoi ghe : gheDangChonSet) {
                // Đảm bảo ghế có mã phòng hiện tại
                ghe.setMaPhong(lichChieu.getMaPhong());

                nvFrmBanVePanel pnlBanVe = new nvFrmBanVePanel(nhanVienHienTai, phim, lichChieu, ghe);

                JDialog dialog = new JDialog(this, "Bán vé - " + (phim != null ? phim.getTenPhim() : ""), ModalityType.APPLICATION_MODAL);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setContentPane(pnlBanVe);
                dialog.pack();
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi mở màn hình bán vé: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
