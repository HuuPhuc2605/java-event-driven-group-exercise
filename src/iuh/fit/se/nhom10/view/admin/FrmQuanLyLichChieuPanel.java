package iuh.fit.se.nhom10.view.admin;

import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.model.LichChieu;
import iuh.fit.se.nhom10.model.Phim;
import iuh.fit.se.nhom10.model.PhongChieu;
import iuh.fit.se.nhom10.service.LichChieuService;
import iuh.fit.se.nhom10.dao.LichChieuDAO;
import iuh.fit.se.nhom10.dao.PhimDAO;
import iuh.fit.se.nhom10.dao.PhongChieuDAO;
import iuh.fit.se.nhom10.util.ColorPalette;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Professional Screening Management Panel
 * Manages: LichChieu (Screenings) - Create, Read, Update, Delete
 * Hỗ trợ: Tạo lịch chiếu, Sửa lịch chiếu, Xóa lịch chiếu (nếu không có vé)
 */
public class FrmQuanLyLichChieuPanel extends JPanel {
    private TaiKhoanNhanVien adminHienTai;
    private LichChieuService lichChieuService;
    private LichChieuDAO lichChieuDAO;
    private PhimDAO phimDAO;
    private PhongChieuDAO phongChieuDAO;
    
    private JTable tblLichChieu;
    private DefaultTableModel modelLichChieu;
    private JTextField txtTimKiemLichChieu;
    
    // Cache for Phim and PhongChieu names
    private Map<String, String> phimCache;
    private Map<String, String> phongCache;

    public FrmQuanLyLichChieuPanel(TaiKhoanNhanVien admin) throws SQLException {
        this.adminHienTai = admin;
        this.lichChieuService = new LichChieuService();
        this.lichChieuDAO = new LichChieuDAO();
        this.phimDAO = new PhimDAO();
        this.phongChieuDAO = new PhongChieuDAO();
        this.phimCache = new HashMap<>();
        this.phongCache = new HashMap<>();
        
        setupUI();
        loadAllData();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(ColorPalette.BACKGROUND_MAIN);

        JPanel pnlSearchBar = createSearchBar();
        add(pnlSearchBar, BorderLayout.NORTH);

        JPanel pnlTable = createTablePanel();
        add(pnlTable, BorderLayout.CENTER);

        JPanel pnlButtons = createButtonPanel();
        add(pnlButtons, BorderLayout.SOUTH);
    }

    private JPanel createSearchBar() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BorderLayout(10, 0));
        pnl.setBackground(ColorPalette.BACKGROUND_MAIN);
        pnl.setBorder(new EmptyBorder(15, 15, 0, 15));

        JPanel pnlTitle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(ColorPalette.PRIMARY);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            }
        };
        pnlTitle.setOpaque(false);
        pnlTitle.setPreferredSize(new Dimension(350, 45));

        JLabel lblTitle = new JLabel("Quản Lý Lịch Chiếu");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE - 4, Font.BOLD));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(8, 15, 8, 15));
        pnlTitle.add(lblTitle);

        JPanel pnlSearchInput = new JPanel();
        pnlSearchInput.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pnlSearchInput.setBackground(ColorPalette.BACKGROUND_MAIN);

        txtTimKiemLichChieu = new JTextField();
        txtTimKiemLichChieu.setPreferredSize(new Dimension(300, 38));
        txtTimKiemLichChieu.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtTimKiemLichChieu.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        txtTimKiemLichChieu.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { handleSearch(); }
            @Override
            public void removeUpdate(DocumentEvent e) { handleSearch(); }
            @Override
            public void changedUpdate(DocumentEvent e) { handleSearch(); }
        });

        pnlSearchInput.add(txtTimKiemLichChieu);

        pnl.add(pnlTitle, BorderLayout.WEST);
        pnl.add(pnlSearchInput, BorderLayout.EAST);

        return pnl;
    }

    private JPanel createTablePanel() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(15, 15, 0, 15));

        modelLichChieu = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelLichChieu.addColumn("Mã Lịch");
        modelLichChieu.addColumn("Tên Phim");
        modelLichChieu.addColumn("Phòng");
        modelLichChieu.addColumn("Ngày Chiếu");
        modelLichChieu.addColumn("Giờ Bắt Đầu");
        modelLichChieu.addColumn("Giờ Kết Thúc");

        tblLichChieu = new JTable(modelLichChieu);
        tblLichChieu.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        tblLichChieu.setRowHeight(35);
        tblLichChieu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblLichChieu.setGridColor(new Color(220, 225, 235));

        // Style header
        JTableHeader header = tblLichChieu.getTableHeader();
        header.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        header.setBackground(ColorPalette.PRIMARY);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                                                          boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(ColorPalette.PRIMARY);
                setForeground(Color.WHITE);
                setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
                setHorizontalAlignment(CENTER);
                return this;
            }
        });

        tblLichChieu.getColumnModel().getColumn(0).setPreferredWidth(100);
        tblLichChieu.getColumnModel().getColumn(1).setPreferredWidth(250);
        tblLichChieu.getColumnModel().getColumn(2).setPreferredWidth(80);
        tblLichChieu.getColumnModel().getColumn(3).setPreferredWidth(120);
        tblLichChieu.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblLichChieu.getColumnModel().getColumn(5).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(tblLichChieu);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        pnl.add(scrollPane, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel createButtonPanel() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_MAIN);
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JButton btnThemLichChieu = new JButton("Thêm Lịch Chiếu");
        btnThemLichChieu.setPreferredSize(new Dimension(150, 40));
        btnThemLichChieu.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnThemLichChieu.setBackground(new Color(52, 211, 153));
        btnThemLichChieu.setForeground(Color.WHITE);
        btnThemLichChieu.setFocusPainted(false);
        btnThemLichChieu.setBorderPainted(false);
        btnThemLichChieu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnThemLichChieu.addActionListener(e -> handleThemLichChieu());

        JButton btnSuaLichChieu = new JButton("Sửa");
        btnSuaLichChieu.setPreferredSize(new Dimension(120, 40));
        btnSuaLichChieu.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnSuaLichChieu.setBackground(ColorPalette.PRIMARY);
        btnSuaLichChieu.setForeground(Color.WHITE);
        btnSuaLichChieu.setFocusPainted(false);
        btnSuaLichChieu.setBorderPainted(false);
        btnSuaLichChieu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSuaLichChieu.addActionListener(e -> handleSuaLichChieu());

        JButton btnXoaLichChieu = new JButton("Xóa");
        btnXoaLichChieu.setPreferredSize(new Dimension(120, 40));
        btnXoaLichChieu.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnXoaLichChieu.setBackground(new Color(239, 68, 68));
        btnXoaLichChieu.setForeground(Color.WHITE);
        btnXoaLichChieu.setFocusPainted(false);
        btnXoaLichChieu.setBorderPainted(false);
        btnXoaLichChieu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXoaLichChieu.addActionListener(e -> handleXoaLichChieu());

        pnl.add(btnThemLichChieu);
        pnl.add(btnSuaLichChieu);
        pnl.add(btnXoaLichChieu);

        return pnl;
    }

    private void loadAllData() {
        loadLichChieuData();
    }

    private void loadLichChieuData() {
        modelLichChieu.setRowCount(0);
        phimCache.clear();
        phongCache.clear();
        
        try {
            List<LichChieu> lichChieuList = lichChieuService.getAllLichChieu();
            for (LichChieu lc : lichChieuList) {
                String tenPhim = getPhimName(lc.getMaPhim());
                String tenPhong = getPhongName(lc.getMaPhong());
                
                modelLichChieu.addRow(new Object[]{
                    lc.getMaLich(),
                    tenPhim,
                    tenPhong,
                    lc.getNgayChieu(),
                    formatTime(lc.getGioBatDau()),
                    formatTime(lc.getGioKetThuc())
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu lịch chiếu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getPhimName(String maPhim) {
        if (phimCache.containsKey(maPhim)) {
            return phimCache.get(maPhim);
        }
        try {
            Phim phim = phimDAO.getPhimByMa(maPhim);
            String tenPhim = phim != null ? phim.getTenPhim() : "N/A";
            phimCache.put(maPhim, tenPhim);
            return tenPhim;
        } catch (Exception e) {
            return "N/A";
        }
    }

    private String getPhongName(String maPhong) {
        if (phongCache.containsKey(maPhong)) {
            return phongCache.get(maPhong);
        }
        try {
            PhongChieu phong = phongChieuDAO.getPhongByMa(maPhong);
            String tenPhong = phong != null ? phong.getTenPhong() : "N/A";
            phongCache.put(maPhong, tenPhong);
            return tenPhong;
        } catch (Exception e) {
            return "N/A";
        }
    }

    private String formatTime(Time time) {
        if (time == null) return "N/A";
        return time.toString();
    }

    private void handleSearch() {
        String keyword = txtTimKiemLichChieu.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadLichChieuData();
            return;
        }

        modelLichChieu.setRowCount(0);
        try {
            List<LichChieu> lichChieuList = lichChieuService.getAllLichChieu();
            for (LichChieu lc : lichChieuList) {
                String tenPhim = getPhimName(lc.getMaPhim());
                String tenPhong = getPhongName(lc.getMaPhong());
                
                if (lc.getMaLich().toLowerCase().contains(keyword) || 
                    tenPhim.toLowerCase().contains(keyword) ||
                    tenPhong.toLowerCase().contains(keyword) ||
                    lc.getNgayChieu().toString().contains(keyword)) {
                    
                    modelLichChieu.addRow(new Object[]{
                        lc.getMaLich(),
                        tenPhim,
                        tenPhong,
                        lc.getNgayChieu(),
                        formatTime(lc.getGioBatDau()),
                        formatTime(lc.getGioKetThuc())
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleThemLichChieu() {
        try {
            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Thêm Lịch Chiếu Mới");
            dialog.setSize(700, 600);
            dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            
            JPanel pnl = createLichChieuForm(null, dialog, () -> loadLichChieuData());
            dialog.add(pnl);
            dialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSuaLichChieu() {
        int selectedRow = tblLichChieu.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lịch chiếu để sửa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            String maLich = (String) modelLichChieu.getValueAt(selectedRow, 0);
            LichChieu lichChieu = lichChieuService.getLichChieuByMa(maLich);
            
            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Sửa Lịch Chiếu");
            dialog.setSize(700, 600);
            dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            
            JPanel pnl = createLichChieuForm(lichChieu, dialog, () -> loadLichChieuData());
            dialog.add(pnl);
            dialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleXoaLichChieu() {
        int selectedRow = tblLichChieu.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lịch chiếu để xóa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            String maLich = (String) modelLichChieu.getValueAt(selectedRow, 0);
            
            // Note: LichChieuDAO.deleteLichChieu() automatically cascades delete to VeXemPhim
            int result = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn xóa lịch chiếu này?\n(Các vé liên quan sẽ bị xóa)", 
                "Xác nhận xóa", 
                JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                if (lichChieuDAO.deleteLichChieu(maLich)) {
                    JOptionPane.showMessageDialog(this, "Xóa lịch chiếu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadLichChieuData();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa lịch chiếu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createLichChieuForm(LichChieu lichChieu, JDialog dialog, Runnable onSuccess) {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(25, 25, 25, 25));
        pnl.setBackground(Color.WHITE);

        // Mã Lịch
        JLabel lblMaLich = new JLabel("Mã Lịch:");
        lblMaLich.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblMaLich.setForeground(ColorPalette.TEXT_LABEL);
        JTextField txtMaLich = new JTextField();
        txtMaLich.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtMaLich.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT + 1, Font.PLAIN));
        txtMaLich.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtMaLich.setEditable(lichChieu == null);

        // Phim
        JLabel lblPhim = new JLabel("Phim:");
        lblPhim.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblPhim.setForeground(ColorPalette.TEXT_LABEL);
        JComboBox<String> cboPhim = new JComboBox<>();
        cboPhim.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cboPhim.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT + 1, Font.PLAIN));

        // Phòng Chiếu
        JLabel lblPhong = new JLabel("Phòng Chiếu:");
        lblPhong.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblPhong.setForeground(ColorPalette.TEXT_LABEL);
        JComboBox<String> cboPhong = new JComboBox<>();
        cboPhong.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cboPhong.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT + 1, Font.PLAIN));

        // Ngày Chiếu
        JLabel lblNgayChieu = new JLabel("Ngày Chiếu:");
        lblNgayChieu.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblNgayChieu.setForeground(ColorPalette.TEXT_LABEL);
        
        JPanel pnlNgayChieu = new JPanel(new BorderLayout(5, 0));
        pnlNgayChieu.setBackground(Color.WHITE);
        pnlNgayChieu.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        java.util.Date today = new java.util.Date();
        JSpinner spinNgayChieu = new JSpinner(new SpinnerDateModel(today, today, null, java.util.Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor editorNgay = new JSpinner.DateEditor(spinNgayChieu, "yyyy-MM-dd");
        spinNgayChieu.setEditor(editorNgay);
        spinNgayChieu.setPreferredSize(new Dimension(200, 38));
        spinNgayChieu.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT + 1, Font.PLAIN));
        
        JButton btnChonNgay = new JButton("📅 Chọn");
        btnChonNgay.setPreferredSize(new Dimension(90, 38));
        btnChonNgay.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.BOLD));
        btnChonNgay.setBackground(ColorPalette.ACCENT);
        btnChonNgay.setForeground(Color.WHITE);
        btnChonNgay.setFocusPainted(false);
        btnChonNgay.setBorderPainted(false);
        btnChonNgay.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChonNgay.addActionListener(e -> showDatePickerDialog(spinNgayChieu, dialog));
        
        pnlNgayChieu.add(spinNgayChieu, BorderLayout.CENTER);
        pnlNgayChieu.add(btnChonNgay, BorderLayout.EAST);

        // Giờ Bắt Đầu
        JLabel lblGioBatDau = new JLabel("Giờ Bắt Đầu:");
        lblGioBatDau.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblGioBatDau.setForeground(ColorPalette.TEXT_LABEL);
        
        JPanel pnlGioBatDau = new JPanel(new BorderLayout(5, 0));
        pnlGioBatDau.setBackground(Color.WHITE);
        pnlGioBatDau.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        java.util.Date defaultStartTime = java.sql.Time.valueOf("09:00:00");
        JSpinner spinGioBatDau = new JSpinner(new SpinnerDateModel(defaultStartTime, null, null, java.util.Calendar.HOUR_OF_DAY));
        JSpinner.DateEditor editorGioBatDau = new JSpinner.DateEditor(spinGioBatDau, "HH:mm:ss");
        spinGioBatDau.setEditor(editorGioBatDau);
        spinGioBatDau.setPreferredSize(new Dimension(200, 38));
        spinGioBatDau.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT + 1, Font.PLAIN));
        
        JButton btnChonGioBatDau = new JButton("⏰ Chọn");
        btnChonGioBatDau.setPreferredSize(new Dimension(90, 38));
        btnChonGioBatDau.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.BOLD));
        btnChonGioBatDau.setBackground(ColorPalette.PRIMARY);
        btnChonGioBatDau.setForeground(Color.WHITE);
        btnChonGioBatDau.setFocusPainted(false);
        btnChonGioBatDau.setBorderPainted(false);
        btnChonGioBatDau.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JSpinner spinGioKetThuc = new JSpinner(new SpinnerDateModel(java.sql.Time.valueOf("11:30:00"), null, null, java.util.Calendar.HOUR_OF_DAY));
        JSpinner.DateEditor editorGioKetThuc = new JSpinner.DateEditor(spinGioKetThuc, "HH:mm:ss");
        spinGioKetThuc.setEditor(editorGioKetThuc);
        
        btnChonGioBatDau.addActionListener(e -> showTimePickerDialog(spinGioBatDau, spinGioKetThuc, dialog, true));
        
        pnlGioBatDau.add(spinGioBatDau, BorderLayout.CENTER);
        pnlGioBatDau.add(btnChonGioBatDau, BorderLayout.EAST);

        // Giờ Kết Thúc
        JLabel lblGioKetThuc = new JLabel("Giờ Kết Thúc:");
        lblGioKetThuc.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblGioKetThuc.setForeground(ColorPalette.TEXT_LABEL);
        
        JPanel pnlGioKetThuc = new JPanel(new BorderLayout(5, 0));
        pnlGioKetThuc.setBackground(Color.WHITE);
        pnlGioKetThuc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        spinGioKetThuc.setPreferredSize(new Dimension(200, 38));
        spinGioKetThuc.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT + 1, Font.PLAIN));
        
        JButton btnChonGioKetThuc = new JButton("⏰ Chọn");
        btnChonGioKetThuc.setPreferredSize(new Dimension(90, 38));
        btnChonGioKetThuc.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.BOLD));
        btnChonGioKetThuc.setBackground(ColorPalette.PRIMARY);
        btnChonGioKetThuc.setForeground(Color.WHITE);
        btnChonGioKetThuc.setFocusPainted(false);
        btnChonGioKetThuc.setBorderPainted(false);
        btnChonGioKetThuc.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChonGioKetThuc.addActionListener(e -> showTimePickerDialog(spinGioKetThuc, spinGioBatDau, dialog, false));
        
        pnlGioKetThuc.add(spinGioKetThuc, BorderLayout.CENTER);
        pnlGioKetThuc.add(btnChonGioKetThuc, BorderLayout.EAST);

        // Load Phim data
        try {
            List<Phim> phimList = phimDAO.getAllPhim();
            for (Phim p : phimList) {
                cboPhim.addItem(p.getMaPhim() + " - " + p.getTenPhim());
            }

            List<PhongChieu> phongList = phongChieuDAO.getAllPhongChieu();
            for (PhongChieu pc : phongList) {
                cboPhong.addItem(pc.getMaPhong() + " - " + pc.getTenPhong());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog, "Lỗi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        // Populate fields if editing
        if (lichChieu != null) {
            txtMaLich.setText(lichChieu.getMaLich());
            spinNgayChieu.setValue(new java.util.Date(lichChieu.getNgayChieu().getTime()));
            spinGioBatDau.setValue(new java.util.Date(lichChieu.getGioBatDau().getTime()));
            spinGioKetThuc.setValue(new java.util.Date(lichChieu.getGioKetThuc().getTime()));
            
            try {
                Phim phim = phimDAO.getPhimByMa(lichChieu.getMaPhim());
                PhongChieu phong = phongChieuDAO.getPhongByMa(lichChieu.getMaPhong());
                cboPhim.setSelectedItem(lichChieu.getMaPhim() + " - " + (phim != null ? phim.getTenPhim() : "N/A"));
                cboPhong.setSelectedItem(lichChieu.getMaPhong() + " - " + (phong != null ? phong.getTenPhong() : "N/A"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Add components to panel
        pnl.add(lblMaLich);
        pnl.add(txtMaLich);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblPhim);
        pnl.add(cboPhim);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblPhong);
        pnl.add(cboPhong);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblNgayChieu);
        pnl.add(pnlNgayChieu);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblGioBatDau);
        pnl.add(pnlGioBatDau);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblGioKetThuc);
        pnl.add(pnlGioKetThuc);
        pnl.add(Box.createVerticalStrut(25));
        
        // Buttons
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlButtons.setBackground(Color.WHITE);
        
        JButton btnLuu = new JButton("Lưu");
        btnLuu.setPreferredSize(new Dimension(110, 42));
        btnLuu.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        btnLuu.setBackground(ColorPalette.PRIMARY);
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFocusPainted(false);
        btnLuu.setBorderPainted(false);
        btnLuu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLuu.addActionListener(e -> {
            try {
                String maLich = txtMaLich.getText().trim();
                String phimStr = (String) cboPhim.getSelectedItem();
                String phongStr = (String) cboPhong.getSelectedItem();
                String maPhim = phimStr.split(" - ")[0];
                String maPhong = phongStr.split(" - ")[0];
                
                if (maLich.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Mã lịch không được để trống!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (lichChieu == null && lichChieuDAO.isLichChieuExists(maLich)) {
                    JOptionPane.showMessageDialog(dialog, "Mã lịch '" + maLich + "' đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                java.util.Date ngayChieuUtil = (java.util.Date) spinNgayChieu.getValue();
                java.util.Date gioBatDauUtil = (java.util.Date) spinGioBatDau.getValue();
                java.util.Date gioKetThucUtil = (java.util.Date) spinGioKetThuc.getValue();
                
                Date ngayChieu = new Date(ngayChieuUtil.getTime());
                Time gioBatDau = new Time(gioBatDauUtil.getTime());
                Time gioKetThuc = new Time(gioKetThucUtil.getTime());

                if (lichChieu == null) {
                    lichChieuService.addLichChieu(maLich, ngayChieu, gioBatDau, gioKetThuc, maPhim, maPhong);
                } else {
                    lichChieuService.updateLichChieu(maLich, ngayChieu, gioBatDau, gioKetThuc, maPhim, maPhong);
                }

                dialog.dispose();
                JOptionPane.showMessageDialog(FrmQuanLyLichChieuPanel.this, "Lưu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                onSuccess.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(FrmQuanLyLichChieuPanel.this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnHuy = new JButton("Hủy");
        btnHuy.setPreferredSize(new Dimension(110, 42));
        btnHuy.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        btnHuy.setBackground(ColorPalette.BUTTON_SECONDARY_BG);
        btnHuy.setForeground(ColorPalette.BUTTON_SECONDARY_TEXT);
        btnHuy.setFocusPainted(false);
        btnHuy.setBorderPainted(false);
        btnHuy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHuy.addActionListener(e -> dialog.dispose());

        pnlButtons.add(btnLuu);
        pnlButtons.add(btnHuy);
        pnl.add(pnlButtons);

        return pnl;
    }

    private void showTimePickerDialog(JSpinner spinnerTarget, JSpinner spinnerOther, JDialog parentDialog, boolean isStartTime) {
        JPanel pnlTimePicker = new JPanel(new GridBagLayout());
        pnlTimePicker.setBackground(Color.WHITE);
        pnlTimePicker.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        
        // Hours
        JLabel lblHour = new JLabel("Giờ:");
        lblHour.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        gbc.gridx = 0; gbc.gridy = 0;
        pnlTimePicker.add(lblHour, gbc);
        
        java.util.Date currentTime = (java.util.Date) spinnerTarget.getValue();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(currentTime);
        int hour = cal.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = cal.get(java.util.Calendar.MINUTE);
        int second = cal.get(java.util.Calendar.SECOND);
        
        JSpinner spinHour = new JSpinner(new SpinnerNumberModel(hour, 0, 23, 1));
        spinHour.setPreferredSize(new Dimension(80, 35));
        spinHour.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 3, Font.BOLD));
        gbc.gridx = 1; gbc.gridy = 0;
        pnlTimePicker.add(spinHour, gbc);
        
        // Minutes
        JLabel lblMinute = new JLabel("Phút:");
        lblMinute.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        gbc.gridx = 2; gbc.gridy = 0;
        pnlTimePicker.add(lblMinute, gbc);
        
        JSpinner spinMinute = new JSpinner(new SpinnerNumberModel(minute, 0, 59, 15));
        spinMinute.setPreferredSize(new Dimension(80, 35));
        spinMinute.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 3, Font.BOLD));
        gbc.gridx = 3; gbc.gridy = 0;
        pnlTimePicker.add(spinMinute, gbc);
        
        // Seconds
        JLabel lblSecond = new JLabel("Giây:");
        lblSecond.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        gbc.gridx = 4; gbc.gridy = 0;
        pnlTimePicker.add(lblSecond, gbc);
        
        JSpinner spinSecond = new JSpinner(new SpinnerNumberModel(second, 0, 59, 1));
        spinSecond.setPreferredSize(new Dimension(80, 35));
        spinSecond.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 3, Font.BOLD));
        gbc.gridx = 5; gbc.gridy = 0;
        pnlTimePicker.add(spinSecond, gbc);
        
        int result = JOptionPane.showConfirmDialog(parentDialog, pnlTimePicker, "Chọn Giờ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            java.util.Calendar newCal = java.util.Calendar.getInstance();
            newCal.setTime(currentTime);
            newCal.set(java.util.Calendar.HOUR_OF_DAY, (Integer) spinHour.getValue());
            newCal.set(java.util.Calendar.MINUTE, (Integer) spinMinute.getValue());
            newCal.set(java.util.Calendar.SECOND, (Integer) spinSecond.getValue());
            spinnerTarget.setValue(newCal.getTime());
        }
    }

    private void showDatePickerDialog(JSpinner spinnerTarget, JDialog parentDialog) {
        JPanel pnlDatePicker = new JPanel();
        pnlDatePicker.setLayout(new BoxLayout(pnlDatePicker, BoxLayout.Y_AXIS));
        pnlDatePicker.setBackground(Color.WHITE);
        pnlDatePicker.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        java.util.Date currentDate = (java.util.Date) spinnerTarget.getValue();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(currentDate);
        
        int year = cal.get(java.util.Calendar.YEAR);
        int month = cal.get(java.util.Calendar.MONTH);
        int day = cal.get(java.util.Calendar.DAY_OF_MONTH);
        
        JPanel pnlMonthYear = new JPanel();
        pnlMonthYear.setBackground(Color.WHITE);
        pnlMonthYear.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        JComboBox<String> cboMonth = new JComboBox<>(months);
        cboMonth.setSelectedIndex(month);
        cboMonth.setPreferredSize(new Dimension(80, 35));
        cboMonth.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        
        JSpinner spinYear = new JSpinner(new SpinnerNumberModel(year, 2020, 2100, 1));
        spinYear.setEditor(new JSpinner.NumberEditor(spinYear, "#"));

        spinYear.setPreferredSize(new Dimension(80, 35));
        spinYear.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        
        JSpinner spinDay = new JSpinner(new SpinnerNumberModel(day, 1, 31, 1));
        spinDay.setPreferredSize(new Dimension(80, 35));
        spinDay.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        
        pnlMonthYear.add(new JLabel("Ngày:"));
        pnlMonthYear.add(spinDay);
        pnlMonthYear.add(new JLabel("Tháng:"));
        pnlMonthYear.add(cboMonth);
        pnlMonthYear.add(new JLabel("Năm:"));
        pnlMonthYear.add(spinYear);
        
        pnlDatePicker.add(pnlMonthYear);
        
        int result = JOptionPane.showConfirmDialog(parentDialog, pnlDatePicker, "Chọn Ngày", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            java.util.Calendar newCal = java.util.Calendar.getInstance();
            newCal.set(java.util.Calendar.YEAR, (Integer) spinYear.getValue());
            newCal.set(java.util.Calendar.MONTH, cboMonth.getSelectedIndex());
            newCal.set(java.util.Calendar.DAY_OF_MONTH, (Integer) spinDay.getValue());
            spinnerTarget.setValue(newCal.getTime());
        }
    }
}
