package iuh.fit.se.nhom10.view.nhanvien;

import iuh.fit.se.nhom10.dao.LichChieuDAO;
import iuh.fit.se.nhom10.dao.PhongChieuDAO;
import iuh.fit.se.nhom10.model.DaoDien;
import iuh.fit.se.nhom10.model.LichChieu;
import iuh.fit.se.nhom10.model.Phim;
import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.service.DashboardService;
import iuh.fit.se.nhom10.service.PhimService;

import iuh.fit.se.nhom10.util.ColorPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel xem thông tin phim dạng card grid cho nhân viên.
 * Hiển thị phim dưới dạng các thẻ nhỏ gọn, khi click sẽ hiển thị chi tiết và lịch chiếu.
 */
public class nvFrmXemPhimPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int MOVIE_GRID_COLUMNS = 4;

    private TaiKhoanNhanVien nhanVienHienTai;
    private PhimService phimService;
    private DashboardService dashboardService;
    private LichChieuDAO lichChieuDAO;
    private PhongChieuDAO phongChieuDAO;
 

    private JPanel pnlMovieGrid;
    private JPanel pnlScheduleDetail;

    private Phim phimDaChon;
    private Map<String, String> daoDienMap;

    public nvFrmXemPhimPanel(TaiKhoanNhanVien nhanVien) throws Exception {
        this.nhanVienHienTai = nhanVien;
        this.phimService = new PhimService();
        this.dashboardService = new DashboardService();
        this.lichChieuDAO = new LichChieuDAO();
        this.phongChieuDAO = new PhongChieuDAO();
        this.daoDienMap = new HashMap<>();

        setupUI();
        loadDuLieu();
    }

    // Constructor rỗng cho designer
    public nvFrmXemPhimPanel() {
    }

    /* ======================= UI SETUP ======================= */

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(ColorPalette.BACKGROUND_CONTENT);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel pnlSearch = createSearchPanel();
        add(pnlSearch, BorderLayout.NORTH);

        JPanel pnlCenterContent = new JPanel(new BorderLayout(10, 10));
        pnlCenterContent.setBackground(ColorPalette.BACKGROUND_CONTENT);

        // Grid phim
        pnlMovieGrid = new JPanel(new GridLayout(0, MOVIE_GRID_COLUMNS, 15, 15));
        pnlMovieGrid.setBackground(ColorPalette.BACKGROUND_CONTENT);

        JScrollPane scrollPane = new JScrollPane(pnlMovieGrid);
        scrollPane.setBackground(ColorPalette.BACKGROUND_CONTENT);
        scrollPane.getViewport().setBackground(ColorPalette.BACKGROUND_CONTENT);
        scrollPane.setBorder(null);
        pnlCenterContent.add(scrollPane, BorderLayout.CENTER);

        // Panel lịch chiếu bên phải
        pnlScheduleDetail = createScheduleDetailPanel();
        pnlCenterContent.add(pnlScheduleDetail, BorderLayout.EAST);

        add(pnlCenterContent, BorderLayout.CENTER);
    }

    /**
     * Tạo button style giống các nút MENU bên trái.
     */
    private JButton createMenuLikeButton(String text) {
        JButton btn = new JButton(text);

        // Ép kiểu button của FlatLaf (rất quan trọng để có nền màu)
        btn.putClientProperty("JButton.buttonType", "roundRect");

        btn.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        btn.setBackground(ColorPalette.BUTTON_PRIMARY_BG);   // cùng màu với menu
        btn.setForeground(Color.WHITE);

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.setPreferredSize(new Dimension(130, 40));
        btn.setMaximumSize(new Dimension(130, 40));
        btn.setBorder(new EmptyBorder(8, 12, 8, 12));

        return btn;
    }

    /**
     * Thanh tìm kiếm + các nút Tìm / Làm mới
     * (nút đã dùng style giống MENU).
     */
    private JPanel createSearchPanel() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ColorPalette.BORDER_LIGHT));

        JLabel lblSearch = new JLabel("Tìm kiếm phim:");
        lblSearch.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        lblSearch.setForeground(ColorPalette.TEXT_LABEL);

        JTextField txtSearch = new JTextField(20);
        txtSearch.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        txtSearch.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        txtSearch.setPreferredSize(new Dimension(260, 38));

        // Button Tìm (giống menu)
        JButton btnSearch = createMenuLikeButton("Tìm");
        btnSearch.addActionListener(e -> searchMovies(txtSearch.getText()));

        // Button Làm mới (giống menu)
        JButton btnReset = createMenuLikeButton("Làm mới");
        btnReset.addActionListener(e -> {
            txtSearch.setText("");
            loadDuLieu();
        });

        pnl.add(lblSearch);
        pnl.add(txtSearch);
        pnl.add(btnSearch);
        pnl.add(btnReset);

        return pnl;
    }

    private JPanel createScheduleDetailPanel() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_MAIN);
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));
        pnl.setPreferredSize(new Dimension(350, 0));

        JLabel lblTitle = new JLabel("Lịch Chiếu");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE, Font.BOLD));
        lblTitle.setForeground(ColorPalette.TEXT_LABEL);

        JPanel pnlScheduleContent = new JPanel();
        pnlScheduleContent.setLayout(new BoxLayout(pnlScheduleContent, BoxLayout.Y_AXIS));
        pnlScheduleContent.setBackground(ColorPalette.BACKGROUND_MAIN);

        JLabel lblPlaceholder = new JLabel("Chọn phim để xem lịch chiếu");
        lblPlaceholder.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        lblPlaceholder.setForeground(ColorPalette.TEXT_PLACEHOLDER);
        lblPlaceholder.setName("lblScheduleContent");

        pnlScheduleContent.add(lblPlaceholder);

        JScrollPane scrollSchedule = new JScrollPane(pnlScheduleContent);
        scrollSchedule.setBackground(ColorPalette.BACKGROUND_MAIN);
        scrollSchedule.getViewport().setBackground(ColorPalette.BACKGROUND_MAIN);
        scrollSchedule.setBorder(null);

        pnl.add(lblTitle, BorderLayout.NORTH);
        pnl.add(scrollSchedule, BorderLayout.CENTER);

        return pnl;
    }

    /* ======================= DATA LOADING ======================= */

    private void loadDuLieu() {
        pnlMovieGrid.removeAll();
        daoDienMap.clear();

        
        
        
        try {
            List<Phim> phimList = phimService.getAllPhim();
            List<DaoDien> daoDienList = dashboardService.layTatCaDaoDien();

            if (daoDienList != null) {
                for (DaoDien dd : daoDienList) {
                    daoDienMap.put(dd.getMaDD(), dd.getTenDD());
                }
            }

            if (phimList != null && !phimList.isEmpty()) {
                for (Phim phim : phimList) {
                    pnlMovieGrid.add(createMovieCard(phim));
                }
            } else {
                JLabel lblNoMovies = new JLabel("Không có phim nào");
                lblNoMovies.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
                lblNoMovies.setForeground(ColorPalette.TEXT_PLACEHOLDER);
                pnlMovieGrid.add(lblNoMovies);
            }
        } catch (Exception e) {
            JLabel lblError = new JLabel("Lỗi tải phim: " + e.getMessage());
            lblError.setForeground(ColorPalette.STATUS_ERROR);
            pnlMovieGrid.add(lblError);
        }

        pnlMovieGrid.revalidate();
        pnlMovieGrid.repaint();
    }

    private void searchMovies(String keyword) {
        pnlMovieGrid.removeAll();

        try {
            List<Phim> phimList = phimService.getAllPhim();

            if (phimList != null) {
                String lowerKeyword = keyword == null ? "" : keyword.toLowerCase();
                phimList.removeIf(phim -> !phim.getTenPhim().toLowerCase().contains(lowerKeyword));

                if (phimList.isEmpty()) {
                    JLabel lblNoMovies = new JLabel("Không tìm thấy phim: " + keyword);
                    lblNoMovies.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
                    lblNoMovies.setForeground(ColorPalette.TEXT_PLACEHOLDER);
                    pnlMovieGrid.add(lblNoMovies);
                } else {
                    for (Phim phim : phimList) {
                        pnlMovieGrid.add(createMovieCard(phim));
                    }
                }
            }
        } catch (Exception e) {
            JLabel lblError = new JLabel("Lỗi tìm kiếm: " + e.getMessage());
            lblError.setForeground(ColorPalette.STATUS_ERROR);
            pnlMovieGrid.add(lblError);
        }

        pnlMovieGrid.revalidate();
        pnlMovieGrid.repaint();
    }

    /* ======================= MOVIE CARD ======================= */

    private JPanel createMovieCard(Phim phim) {
        JPanel pnlCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (phim.equals(phimDaChon)) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(ColorPalette.PRIMARY);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                }
            }
        };

        pnlCard.setLayout(new BorderLayout(10, 10));
        pnlCard.setBackground(ColorPalette.BACKGROUND_MAIN);
        pnlCard.setBorder(new EmptyBorder(15, 12, 15, 12));
        pnlCard.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Top section - Movie code
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlTop.setBackground(ColorPalette.BACKGROUND_MAIN);

        JLabel lblCode = new JLabel("MÃ: " + phim.getMaPhim());
        lblCode.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.BOLD));
        lblCode.setForeground(ColorPalette.PRIMARY);
        pnlTop.add(lblCode);

        // Middle section - Movie info
        JPanel pnlMiddle = new JPanel();
        pnlMiddle.setLayout(new BoxLayout(pnlMiddle, BoxLayout.Y_AXIS));
        pnlMiddle.setBackground(ColorPalette.BACKGROUND_MAIN);

        JLabel lblTitle = new JLabel(phim.getTenPhim());
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        lblTitle.setForeground(ColorPalette.TEXT_LABEL);

        JLabel lblDuration = new JLabel("Thời lượng: " + phim.getThoiLuong() + " phút");
        lblDuration.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.PLAIN));
        lblDuration.setForeground(ColorPalette.TEXT_PLACEHOLDER);

        String tenDD = daoDienMap.getOrDefault(phim.getMaDD(), "Chưa cập nhật");
        JLabel lblDirector = new JLabel("Đạo diễn: " + tenDD);
        lblDirector.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.PLAIN));
        lblDirector.setForeground(ColorPalette.TEXT_PLACEHOLDER);

        pnlMiddle.add(lblTitle);
        pnlMiddle.add(Box.createVerticalStrut(5));
        pnlMiddle.add(lblDuration);
        pnlMiddle.add(lblDirector);

        pnlCard.add(pnlTop, BorderLayout.NORTH);
        pnlCard.add(pnlMiddle, BorderLayout.CENTER);

        // Add click listener
        pnlCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectMovie(phim);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!phim.equals(phimDaChon)) {
                    pnlCard.setBackground(ColorPalette.BACKGROUND_CONTENT);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!phim.equals(phimDaChon)) {
                    pnlCard.setBackground(ColorPalette.BACKGROUND_MAIN);
                }
            }
        });

        return pnlCard;
    }

    private void selectMovie(Phim phim) {
        this.phimDaChon = phim;

        try {
            List<LichChieu> lichChieuList = lichChieuDAO.getLichChieuByPhim(phim.getMaPhim());
            displaySchedule(lichChieuList);
        } catch (Exception e) {
            displayScheduleError("Lỗi tải lịch chiếu: " + e.getMessage());
        }

        pnlMovieGrid.repaint();
    }

    /* ======================= SCHEDULE DISPLAY ======================= */

    private JPanel getScheduleContentPanel() {
        for (Component comp : pnlScheduleDetail.getComponents()) {
            if (comp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) comp;
                Component view = scrollPane.getViewport().getView();
                if (view instanceof JPanel) {
                    return (JPanel) view;
                }
            }
        }
        return null;
    }

    private void displaySchedule(List<LichChieu> lichChieuList) {
        JPanel pnlContent = getScheduleContentPanel();
        if (pnlContent == null) {
            return;
        }

        pnlContent.removeAll();

        if (lichChieuList == null || lichChieuList.isEmpty()) {
            JLabel lblNoSchedule = new JLabel("Không có lịch chiếu");
            lblNoSchedule.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
            lblNoSchedule.setForeground(ColorPalette.TEXT_PLACEHOLDER);
            pnlContent.add(lblNoSchedule);
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            for (LichChieu lc : lichChieuList) {
                JPanel pnlScheduleItem = new JPanel();
                pnlScheduleItem.setLayout(new BoxLayout(pnlScheduleItem, BoxLayout.Y_AXIS));
                pnlScheduleItem.setBackground(ColorPalette.BACKGROUND_CONTENT);
                pnlScheduleItem.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));

                JLabel lblDate = new JLabel("Ngày: " + dateFormat.format(lc.getNgayChieu()));
                lblDate.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.BOLD));
                lblDate.setForeground(ColorPalette.PRIMARY);

                JLabel lblTime = new JLabel(
                        "Giờ: " + timeFormat.format(lc.getGioBatDau()) + " - " + timeFormat.format(lc.getGioKetThuc()));
                lblTime.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.PLAIN));
                lblTime.setForeground(ColorPalette.TEXT_LABEL);

                JLabel lblRoom = new JLabel("Phòng: " + lc.getMaPhong());
                lblRoom.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.PLAIN));
                lblRoom.setForeground(ColorPalette.TEXT_PLACEHOLDER);

                JLabel lblScheduleId = new JLabel("ID: " + lc.getMaLich());
                lblScheduleId.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.PLAIN));
                lblScheduleId.setForeground(ColorPalette.TEXT_PLACEHOLDER);

                // Nút chọn ghế & bán vé
                JButton btnChonGhe = createMenuLikeButton("Chọn ghế & bán vé");
                btnChonGhe.addActionListener(e -> {
                    if (phimDaChon == null) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Vui lòng chọn một phim ở danh sách bên trái trước.",
                                "Chưa chọn phim",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    Window owner = SwingUtilities.getWindowAncestor(this);
                    DlgChonGheChoLichChieu dlg = new DlgChonGheChoLichChieu(
                            owner,
                            nhanVienHienTai,
                            phimDaChon,
                            lc
                    );
                    dlg.setVisible(true);
                });

                pnlScheduleItem.add(lblDate);
                pnlScheduleItem.add(Box.createVerticalStrut(5));
                pnlScheduleItem.add(lblTime);
                pnlScheduleItem.add(lblRoom);
                pnlScheduleItem.add(lblScheduleId);
                pnlScheduleItem.add(Box.createVerticalStrut(8));
                pnlScheduleItem.add(btnChonGhe);

                pnlContent.add(pnlScheduleItem);
                pnlContent.add(Box.createVerticalStrut(10));
            }
        }

        pnlContent.revalidate();
        pnlContent.repaint();
    }

    private void displayScheduleError(String errorMessage) {
        JPanel pnlContent = getScheduleContentPanel();
        if (pnlContent == null) {
            return;
        }

        pnlContent.removeAll();

        JLabel lblError = new JLabel(errorMessage);
        lblError.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        lblError.setForeground(ColorPalette.STATUS_ERROR);

        pnlContent.add(lblError);
        pnlContent.revalidate();
        pnlContent.repaint();
    }
}
