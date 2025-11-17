package iuh.fit.se.nhom10.view.admin;

import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.model.KhuyenMai;
import iuh.fit.se.nhom10.dao.KhuyenMaiDAO;
import iuh.fit.se.nhom10.util.ColorPalette;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FrmQuanLyKhuyenMaiPanel extends JPanel {
    private TaiKhoanNhanVien adminHienTai;
    private KhuyenMaiDAO khuyenMaiDAO;
    
    private JTable tblKhuyenMai;
    private DefaultTableModel modelKhuyenMai;
    private JTextField txtTimKiem;

    public FrmQuanLyKhuyenMaiPanel(TaiKhoanNhanVien admin) throws SQLException {
        this.adminHienTai = admin;
        this.khuyenMaiDAO = new KhuyenMaiDAO();
        setupUI();
        loadKhuyenMaiData();
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
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(15, 15, 10, 15));

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
        pnlTitle.setPreferredSize(new Dimension(300, 45));

        JLabel lblTitle = new JLabel("Quản Lý Khuyến Mãi");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE - 4, Font.BOLD));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(8, 15, 8, 15));
        pnlTitle.add(lblTitle);

        JPanel pnlSearchInput = new JPanel();
        pnlSearchInput.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pnlSearchInput.setBackground(ColorPalette.BACKGROUND_CONTENT);

        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(300, 38));
        txtTimKiem.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { handleSearch(); }
            @Override
            public void removeUpdate(DocumentEvent e) { handleSearch(); }
            @Override
            public void changedUpdate(DocumentEvent e) { handleSearch(); }
        });

        pnlSearchInput.add(txtTimKiem);

        pnl.add(pnlTitle, BorderLayout.WEST);
        pnl.add(pnlSearchInput, BorderLayout.EAST);

        return pnl;
    }

    private JPanel createTablePanel() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(10, 15, 10, 15));

        modelKhuyenMai = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelKhuyenMai.addColumn("Mã KM");
        modelKhuyenMai.addColumn("Tên Khuyến Mãi");
        modelKhuyenMai.addColumn("Mô Tả");
        modelKhuyenMai.addColumn("Tỉ Lệ Giảm (%)");
        modelKhuyenMai.addColumn("Ngày Bắt Đầu");
        modelKhuyenMai.addColumn("Ngày Kết Thúc");
        modelKhuyenMai.addColumn("Trạng Thái");

        tblKhuyenMai = new JTable(modelKhuyenMai);
        tblKhuyenMai.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        tblKhuyenMai.setRowHeight(35);
        tblKhuyenMai.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblKhuyenMai.setGridColor(new Color(220, 225, 235));

        JTableHeader header = tblKhuyenMai.getTableHeader();
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

        tblKhuyenMai.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblKhuyenMai.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblKhuyenMai.getColumnModel().getColumn(2).setPreferredWidth(200);
        tblKhuyenMai.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblKhuyenMai.getColumnModel().getColumn(4).setPreferredWidth(120);
        tblKhuyenMai.getColumnModel().getColumn(5).setPreferredWidth(120);
        tblKhuyenMai.getColumnModel().getColumn(6).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(tblKhuyenMai);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        pnl.add(scrollPane, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel createButtonPanel() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(10, 15, 15, 15));

        JButton btnThem = new JButton("Thêm Khuyến Mãi");
        btnThem.setPreferredSize(new Dimension(150, 40));
        btnThem.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnThem.setBackground(new Color(52, 211, 153));
        btnThem.setForeground(Color.WHITE);
        btnThem.setFocusPainted(false);
        btnThem.setBorderPainted(false);
        btnThem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnThem.addActionListener(e -> handleThemKhuyenMai());

        JButton btnSua = new JButton("Sửa");
        btnSua.setPreferredSize(new Dimension(120, 40));
        btnSua.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnSua.setBackground(ColorPalette.PRIMARY);
        btnSua.setForeground(Color.WHITE);
        btnSua.setFocusPainted(false);
        btnSua.setBorderPainted(false);
        btnSua.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSua.addActionListener(e -> handleSuaKhuyenMai());

        JButton btnXoa = new JButton("Xóa");
        btnXoa.setPreferredSize(new Dimension(120, 40));
        btnXoa.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnXoa.setBackground(new Color(239, 68, 68));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setFocusPainted(false);
        btnXoa.setBorderPainted(false);
        btnXoa.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXoa.addActionListener(e -> handleXoaKhuyenMai());

        pnl.add(btnThem);
        pnl.add(btnSua);
        pnl.add(btnXoa);

        return pnl;
    }

    private void loadKhuyenMaiData() {
        modelKhuyenMai.setRowCount(0);
        try {
            List<KhuyenMai> list = khuyenMaiDAO.getAllKhuyenMai();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (KhuyenMai km : list) {
                String trangThai = getTrangThai(km);
                modelKhuyenMai.addRow(new Object[]{
                    km.getMaKM(),
                    km.getTenKM(),
                    km.getMoTa() != null ? km.getMoTa() : "",
                    String.format("%.2f", km.getTiLeGiam()),
                    km.getNgayBatDau().format(formatter),
                    km.getNgayKetThuc().format(formatter),
                    trangThai
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getTrangThai(KhuyenMai km) {
        LocalDate today = LocalDate.now();
        if (today.isBefore(km.getNgayBatDau())) {
            return "Chưa áp dụng";
        } else if (today.isAfter(km.getNgayKetThuc())) {
            return "Hết hạn";
        } else {
            return "Đang áp dụng";
        }
    }

    private void handleSearch() {
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadKhuyenMaiData();
            return;
        }

        modelKhuyenMai.setRowCount(0);
        try {
            List<KhuyenMai> list = khuyenMaiDAO.searchKhuyenMai(keyword);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (KhuyenMai km : list) {
                String trangThai = getTrangThai(km);
                modelKhuyenMai.addRow(new Object[]{
                    km.getMaKM(),
                    km.getTenKM(),
                    km.getMoTa() != null ? km.getMoTa() : "",
                    String.format("%.2f", km.getTiLeGiam()),
                    km.getNgayBatDau().format(formatter),
                    km.getNgayKetThuc().format(formatter),
                    trangThai
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleThemKhuyenMai() {
        try {
            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Thêm Khuyến Mãi Mới");
            dialog.setSize(700, 600);
            dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            
            JPanel pnl = createKhuyenMaiForm(null, dialog, () -> loadKhuyenMaiData());
            dialog.add(pnl);
            dialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSuaKhuyenMai() {
        int selectedRow = tblKhuyenMai.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi để sửa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            String maKM = (String) modelKhuyenMai.getValueAt(selectedRow, 0);
            KhuyenMai km = khuyenMaiDAO.getKhuyenMaiByMa(maKM);
            
            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Sửa Khuyến Mãi");
            dialog.setSize(700, 600);
            dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            
            JPanel pnl = createKhuyenMaiForm(km, dialog, () -> loadKhuyenMaiData());
            dialog.add(pnl);
            dialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleXoaKhuyenMai() {
        int selectedRow = tblKhuyenMai.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi để xóa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String maKM = (String) modelKhuyenMai.getValueAt(selectedRow, 0);
        int result = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa khuyến mãi này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            if (khuyenMaiDAO.deleteKhuyenMai(maKM)) {
                JOptionPane.showMessageDialog(this, "Xóa khuyến mãi thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadKhuyenMaiData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa khuyến mãi thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createKhuyenMaiForm(KhuyenMai km, JDialog dialog, Runnable onSuccess) {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(25, 25, 25, 25));
        pnl.setBackground(Color.WHITE);

        JLabel lblMaKM = new JLabel("Mã Khuyến Mãi:");
        lblMaKM.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblMaKM.setForeground(ColorPalette.TEXT_LABEL);
        JTextField txtMaKM = new JTextField();
        txtMaKM.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtMaKM.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT + 1, Font.PLAIN));
        txtMaKM.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtMaKM.setEditable(km == null);

        JLabel lblTenKM = new JLabel("Tên Khuyến Mãi:");
        lblTenKM.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblTenKM.setForeground(ColorPalette.TEXT_LABEL);
        JTextField txtTenKM = new JTextField();
        txtTenKM.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtTenKM.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT + 1, Font.PLAIN));
        txtTenKM.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        JLabel lblMoTa = new JLabel("Mô Tả:");
        lblMoTa.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblMoTa.setForeground(ColorPalette.TEXT_LABEL);
        JTextArea txtMoTa = new JTextArea();
        txtMoTa.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        txtMoTa.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtMoTa.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        JScrollPane spMoTa = new JScrollPane(txtMoTa);
        spMoTa.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel lblTiLeGiam = new JLabel("Tỉ Lệ Giảm (%):");
        lblTiLeGiam.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblTiLeGiam.setForeground(ColorPalette.TEXT_LABEL);
        
        JComboBox<String> cboTiLeGiam = new JComboBox<>();
        cboTiLeGiam.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cboTiLeGiam.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT + 1, Font.PLAIN));
        cboTiLeGiam.setEditable(true);
        
        String[] tiLeOptions = {"5%", "10%", "15%", "20%", "25%", "30%", "35%", "40%", "50%", "Custom"};
        for (String option : tiLeOptions) {
            cboTiLeGiam.addItem(option);
        }
        cboTiLeGiam.setSelectedIndex(0);

        JLabel lblNgayBatDau = new JLabel("Ngày Bắt Đầu:");
        lblNgayBatDau.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblNgayBatDau.setForeground(ColorPalette.TEXT_LABEL);
        JPanel pnlNgayBatDau = new JPanel();
        pnlNgayBatDau.setLayout(new BorderLayout(5, 0));
        pnlNgayBatDau.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        JTextField txtNgayBatDau = new JTextField();
        txtNgayBatDau.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT + 1, Font.PLAIN));
        txtNgayBatDau.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        JButton btnChonNgayBatDau = new JButton("📅");
        btnChonNgayBatDau.setPreferredSize(new Dimension(40, 40));
        btnChonNgayBatDau.setBackground(new Color(76, 175, 80));
        btnChonNgayBatDau.setForeground(Color.WHITE);
        btnChonNgayBatDau.setBorderPainted(false);
        btnChonNgayBatDau.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChonNgayBatDau.addActionListener(e -> showDatePicker(txtNgayBatDau, dialog));
        pnlNgayBatDau.add(txtNgayBatDau, BorderLayout.CENTER);
        pnlNgayBatDau.add(btnChonNgayBatDau, BorderLayout.EAST);

        JLabel lblNgayKetThuc = new JLabel("Ngày Kết Thúc:");
        lblNgayKetThuc.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblNgayKetThuc.setForeground(ColorPalette.TEXT_LABEL);
        JPanel pnlNgayKetThuc = new JPanel();
        pnlNgayKetThuc.setLayout(new BorderLayout(5, 0));
        pnlNgayKetThuc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        JTextField txtNgayKetThuc = new JTextField();
        txtNgayKetThuc.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT + 1, Font.PLAIN));
        txtNgayKetThuc.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        JButton btnChonNgayKetThuc = new JButton("📅");
        btnChonNgayKetThuc.setPreferredSize(new Dimension(40, 40));
        btnChonNgayKetThuc.setBackground(new Color(76, 175, 80));
        btnChonNgayKetThuc.setForeground(Color.WHITE);
        btnChonNgayKetThuc.setBorderPainted(false);
        btnChonNgayKetThuc.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChonNgayKetThuc.addActionListener(e -> showDatePicker(txtNgayKetThuc, dialog));
        pnlNgayKetThuc.add(txtNgayKetThuc, BorderLayout.CENTER);
        pnlNgayKetThuc.add(btnChonNgayKetThuc, BorderLayout.EAST);

        if (km != null) {
            txtMaKM.setText(km.getMaKM());
            txtTenKM.setText(km.getTenKM());
            txtMoTa.setText(km.getMoTa() != null ? km.getMoTa() : "");
            double tiLeGiam = km.getTiLeGiam();
            String tiLeGiamStr = String.format("%.0f%%", tiLeGiam);
            int index = -1;
            for (int i = 0; i < tiLeOptions.length - 1; i++) {  // exclude "Custom" from search
                if (tiLeOptions[i].equals(tiLeGiamStr)) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                cboTiLeGiam.setSelectedItem("Custom"); // Select "Custom" option
                // Don't override txtMoTa - keep original description
            } else {
                cboTiLeGiam.setSelectedIndex(index);
            }
            txtNgayBatDau.setText(km.getNgayBatDau().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            txtNgayKetThuc.setText(km.getNgayKetThuc().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        pnl.add(lblMaKM);
        pnl.add(txtMaKM);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblTenKM);
        pnl.add(txtTenKM);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblMoTa);
        pnl.add(spMoTa);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblTiLeGiam);
        pnl.add(cboTiLeGiam);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblNgayBatDau);
        pnl.add(pnlNgayBatDau);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblNgayKetThuc);
        pnl.add(pnlNgayKetThuc);
        pnl.add(Box.createVerticalStrut(25));

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
                String maKM = txtMaKM.getText().trim();
                String tenKM = txtTenKM.getText().trim();
                String moTa = txtMoTa.getText().trim();
                String tiLeGiamStr = (String) cboTiLeGiam.getSelectedItem();
                
                if (maKM.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Mã khuyến mãi không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (tenKM.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Tên khuyến mãi không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Check for duplicate mã khuyến mãi when adding new
                if (km == null && khuyenMaiDAO.isKhuyenMaiExists(maKM)) {
                    JOptionPane.showMessageDialog(this, "Mã khuyến mãi này đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                double tiLeGiam;
                if ("Custom".equals(tiLeGiamStr)) {
                    String customValue = (String) cboTiLeGiam.getEditor().getItem();
                    if (customValue == null || customValue.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Vui lòng nhập tỉ lệ giảm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    tiLeGiam = Double.parseDouble(customValue.replace("%", "").trim());
                } else {
                    tiLeGiam = Double.parseDouble(tiLeGiamStr.replace("%", "").trim());
                }
                
                if (tiLeGiam < 0 || tiLeGiam > 100) {
                    JOptionPane.showMessageDialog(this, "Tỉ lệ giảm phải từ 0 đến 100%!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LocalDate ngayBatDau = LocalDate.parse(txtNgayBatDau.getText().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                LocalDate ngayKetThuc = LocalDate.parse(txtNgayKetThuc.getText().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                if (ngayBatDau.isAfter(ngayKetThuc)) {
                    JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải trước ngày kết thúc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                KhuyenMai newKM = new KhuyenMai(maKM, tenKM, moTa, tiLeGiam, ngayBatDau, ngayKetThuc);
                
                if (km == null) {
                    khuyenMaiDAO.createKhuyenMai(newKM);
                } else {
                    khuyenMaiDAO.updateKhuyenMai(newKM);
                }

                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Lưu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                onSuccess.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
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

    private void showDatePicker(JTextField txtTarget, JDialog parentDialog) {
        JDialog dateDialog = new JDialog(parentDialog, "Chọn Ngày", true);
        dateDialog.setSize(400, 350);
        dateDialog.setLocationRelativeTo(parentDialog);
        dateDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnl.setBackground(Color.WHITE);

        LocalDate currentDate = LocalDate.now();
        
        JPanel pnlDay = createDateSpinnerPanel("Ngày", 1, 31, currentDate.getDayOfMonth());
        JPanel pnlMonth = createDateSpinnerPanel("Tháng", 1, 12, currentDate.getMonthValue());
        JPanel pnlYear = createDateSpinnerPanel("Năm", 2000, 2050, currentDate.getYear());

        JSpinner spinDay = (JSpinner) pnlDay.getClientProperty("spinner");
        JSpinner spinMonth = (JSpinner) pnlMonth.getClientProperty("spinner");
        JSpinner spinYear = (JSpinner) pnlYear.getClientProperty("spinner");

        pnl.add(pnlDay);
        pnl.add(Box.createVerticalStrut(15));
        pnl.add(pnlMonth);
        pnl.add(Box.createVerticalStrut(15));
        pnl.add(pnlYear);
        pnl.add(Box.createVerticalStrut(25));

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        pnlButtons.setBackground(Color.WHITE);

        JButton btnOK = new JButton("Chọn");
        btnOK.setPreferredSize(new Dimension(100, 38));
        btnOK.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnOK.setBackground(ColorPalette.PRIMARY);
        btnOK.setForeground(Color.WHITE);
        btnOK.setBorderPainted(false);
        btnOK.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOK.addActionListener(e -> {
            int day = (Integer) spinDay.getValue();
            int month = (Integer) spinMonth.getValue();
            int year = (Integer) spinYear.getValue();
            LocalDate selectedDate = LocalDate.of(year, month, day);
            txtTarget.setText(selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            dateDialog.dispose();
        });

        JButton btnCancel = new JButton("Hủy");
        btnCancel.setPreferredSize(new Dimension(100, 38));
        btnCancel.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnCancel.setBackground(ColorPalette.BUTTON_SECONDARY_BG);
        btnCancel.setForeground(ColorPalette.BUTTON_SECONDARY_TEXT);
        btnCancel.setBorderPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> dateDialog.dispose());

        pnlButtons.add(btnOK);
        pnlButtons.add(btnCancel);
        pnl.add(pnlButtons);

        dateDialog.add(pnl);
        dateDialog.setVisible(true);
    }

    private JPanel createDateSpinnerPanel(String label, int min, int max, int initial) {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BorderLayout(10, 0));
        pnl.setBackground(Color.WHITE);
        pnl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel lbl = new JLabel(label + ":");
        lbl.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        lbl.setForeground(ColorPalette.TEXT_LABEL);
        lbl.setPreferredSize(new Dimension(80, 50));

        SpinnerNumberModel model = new SpinnerNumberModel(initial, min, max, 1);
        JSpinner spinner = new JSpinner(model);
        spinner.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, "00");
        spinner.setEditor(editor);

        pnl.putClientProperty("spinner", spinner);
        pnl.add(lbl, BorderLayout.WEST);
        pnl.add(spinner, BorderLayout.CENTER);

        return pnl;
    }
}
