package iuh.fit.se.nhom10.view;

import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.model.NhanVien;
import iuh.fit.se.nhom10.dao.NhanVienDAO;
import iuh.fit.se.nhom10.dao.TaiKhoanNhanVienDAO;
import iuh.fit.se.nhom10.util.ColorPalette;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class FrmQuanLyNhanVienPanel extends JPanel {
    private TaiKhoanNhanVien adminHienTai;
    private NhanVienDAO nhanVienDAO;
    private TaiKhoanNhanVienDAO taiKhoanDAO;
    
    private JTabbedPane tabbedPane;
    private JTable tblNhanVien;
    private DefaultTableModel modelNhanVien;
    private JTextField txtTimKiemNhanVien;
    
    private JTable tblTaiKhoan;
    private DefaultTableModel modelTaiKhoan;
    private JTextField txtTimKiemTaiKhoan;

    public FrmQuanLyNhanVienPanel(TaiKhoanNhanVien admin) throws SQLException {
        this.adminHienTai = admin;
        this.nhanVienDAO = new NhanVienDAO();
        this.taiKhoanDAO = new TaiKhoanNhanVienDAO();
        setupUI();
        loadAllData();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(ColorPalette.BACKGROUND_MAIN);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        tabbedPane.setBackground(ColorPalette.BACKGROUND_MAIN);
        tabbedPane.setForeground(ColorPalette.TEXT_LABEL);
        
        tabbedPane.addTab("Quản Lý Nhân Viên", createNhanVienTab());
        tabbedPane.addTab("Quản Lý Tài Khoản", createTaiKhoanTab());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createNhanVienTab() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        pnl.add(createNhanVienSearchBar(), BorderLayout.NORTH);
        pnl.add(createNhanVienTablePanel(), BorderLayout.CENTER);
        pnl.add(createNhanVienButtonPanel(), BorderLayout.SOUTH);

        return pnl;
    }

    private JPanel createNhanVienSearchBar() {
        JPanel pnl = new JPanel(new BorderLayout(10, 0));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

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

        JLabel lblTitle = new JLabel("Quản Lý Nhân Viên");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE - 4, Font.BOLD));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(8, 15, 8, 15));
        pnlTitle.add(lblTitle);

        JPanel pnlSearchInput = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pnlSearchInput.setBackground(ColorPalette.BACKGROUND_CONTENT);

        txtTimKiemNhanVien = new JTextField();
        txtTimKiemNhanVien.setPreferredSize(new Dimension(300, 38));
        txtTimKiemNhanVien.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtTimKiemNhanVien.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtTimKiemNhanVien.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { handleSearchNhanVien(); }
            public void removeUpdate(DocumentEvent e) { handleSearchNhanVien(); }
            public void changedUpdate(DocumentEvent e) { handleSearchNhanVien(); }
        });

        pnlSearchInput.add(txtTimKiemNhanVien);
        pnl.add(pnlTitle, BorderLayout.WEST);
        pnl.add(pnlSearchInput, BorderLayout.EAST);

        return pnl;
    }

    private JPanel createNhanVienTablePanel() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

        modelNhanVien = new DefaultTableModel(0, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        modelNhanVien.addColumn("Mã NV");
        modelNhanVien.addColumn("Tên Nhân Viên");
        modelNhanVien.addColumn("Chức Vụ");
        modelNhanVien.addColumn("Lương");
        modelNhanVien.addColumn("SĐT");

        tblNhanVien = new JTable(modelNhanVien);
        tblNhanVien.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        tblNhanVien.setRowHeight(35);
        tblNhanVien.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblNhanVien.setGridColor(new Color(220, 225, 235));

        JTableHeader header = tblNhanVien.getTableHeader();
        header.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        header.setBackground(ColorPalette.PRIMARY);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
        
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

        tblNhanVien.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblNhanVien.getColumnModel().getColumn(1).setPreferredWidth(200);
        tblNhanVien.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblNhanVien.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblNhanVien.getColumnModel().getColumn(4).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(tblNhanVien);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        scrollPane.setColumnHeaderView(tblNhanVien.getTableHeader());
        pnl.add(scrollPane, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel createNhanVienButtonPanel() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

        JButton btnThem = new JButton("Thêm");
        btnThem.setPreferredSize(new Dimension(120, 40));
        btnThem.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnThem.setBackground(new Color(52, 211, 153));
        btnThem.setForeground(Color.WHITE);
        btnThem.setFocusPainted(false);
        btnThem.setBorderPainted(false);
        btnThem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnThem.addActionListener(e -> showNhanVienDialog(null));

        JButton btnSua = new JButton("Sửa");
        btnSua.setPreferredSize(new Dimension(120, 40));
        btnSua.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnSua.setBackground(ColorPalette.PRIMARY);
        btnSua.setForeground(Color.WHITE);
        btnSua.setFocusPainted(false);
        btnSua.setBorderPainted(false);
        btnSua.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSua.addActionListener(e -> {
            int row = tblNhanVien.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Chọn nhân viên để sửa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String maNV = (String) modelNhanVien.getValueAt(row, 0);
            try {
                NhanVien nv = nhanVienDAO.getNhanVienByMa(maNV);
                showNhanVienDialog(nv);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnXoa = new JButton("Xóa");
        btnXoa.setPreferredSize(new Dimension(120, 40));
        btnXoa.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnXoa.setBackground(new Color(239, 68, 68));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setFocusPainted(false);
        btnXoa.setBorderPainted(false);
        btnXoa.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXoa.addActionListener(e -> {
            int row = tblNhanVien.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Chọn nhân viên để xóa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String maNV = (String) modelNhanVien.getValueAt(row, 0);
            int result = JOptionPane.showConfirmDialog(this, "Xác nhận xóa nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                try {
                    nhanVienDAO.deleteNhanVien(maNV);
                    JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadNhanVienData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        pnl.add(btnThem);
        pnl.add(btnSua);
        pnl.add(btnXoa);

        return pnl;
    }

    private void showNhanVienDialog(NhanVien nv) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), nv == null ? "Thêm Nhân Viên" : "Sửa Nhân Viên");
        dialog.setSize(550, 450);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(25, 25, 25, 25));
        pnl.setBackground(Color.WHITE);

        JLabel lblMaNV = new JLabel("Mã Nhân Viên:");
        lblMaNV.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        JTextField txtMaNV = new JTextField();
        txtMaNV.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtMaNV.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtMaNV.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtMaNV.setEditable(nv == null);

        JLabel lblTenNV = new JLabel("Tên Nhân Viên:");
        lblTenNV.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        JTextField txtTenNV = new JTextField();
        txtTenNV.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtTenNV.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtTenNV.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        JLabel lblChucVu = new JLabel("Chức Vụ:");
        lblChucVu.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        JSpinner spinChucVu = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        spinChucVu.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel lblLuong = new JLabel("Lương:");
        lblLuong.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        JTextField txtLuong = new JTextField();
        txtLuong.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtLuong.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtLuong.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        JLabel lblSDT = new JLabel("Số Điện Thoại:");
        lblSDT.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        JTextField txtSDT = new JTextField();
        txtSDT.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtSDT.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtSDT.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        if (nv != null) {
            txtMaNV.setText(nv.getMaNV());
            txtTenNV.setText(nv.getTenNV());
            spinChucVu.setValue(nv.getMaChucVu());
            txtLuong.setText(nv.getLuong().toPlainString());
            txtSDT.setText(nv.getSoDienThoai() != null ? nv.getSoDienThoai() : "");
        }

        pnl.add(lblMaNV);
        pnl.add(txtMaNV);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblTenNV);
        pnl.add(txtTenNV);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblChucVu);
        pnl.add(spinChucVu);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblLuong);
        pnl.add(txtLuong);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblSDT);
        pnl.add(txtSDT);
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
                NhanVien newNV = new NhanVien(
                    txtMaNV.getText().trim(),
                    txtTenNV.getText().trim(),
                    (int) spinChucVu.getValue(),
                    new BigDecimal(txtLuong.getText().trim()),
                    txtSDT.getText().trim()
                );

                if (nv == null) {
                    nhanVienDAO.createNhanVien(newNV);
                } else {
                    nhanVienDAO.updateNhanVien(newNV);
                }

                dialog.dispose();
                JOptionPane.showMessageDialog(FrmQuanLyNhanVienPanel.this, "Lưu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadNhanVienData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(FrmQuanLyNhanVienPanel.this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
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

        dialog.add(pnl);
        dialog.setVisible(true);
    }

    private JPanel createTaiKhoanTab() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        pnl.add(createTaiKhoanSearchBar(), BorderLayout.NORTH);
        pnl.add(createTaiKhoanTablePanel(), BorderLayout.CENTER);
        pnl.add(createTaiKhoanButtonPanel(), BorderLayout.SOUTH);

        return pnl;
    }

    private JPanel createTaiKhoanSearchBar() {
        JPanel pnl = new JPanel(new BorderLayout(10, 0));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

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

        JLabel lblTitle = new JLabel("Quản Lý Tài Khoản");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE - 4, Font.BOLD));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(8, 15, 8, 15));
        pnlTitle.add(lblTitle);

        JPanel pnlSearchInput = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pnlSearchInput.setBackground(ColorPalette.BACKGROUND_CONTENT);

        txtTimKiemTaiKhoan = new JTextField();
        txtTimKiemTaiKhoan.setPreferredSize(new Dimension(300, 38));
        txtTimKiemTaiKhoan.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtTimKiemTaiKhoan.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtTimKiemTaiKhoan.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { handleSearchTaiKhoan(); }
            public void removeUpdate(DocumentEvent e) { handleSearchTaiKhoan(); }
            public void changedUpdate(DocumentEvent e) { handleSearchTaiKhoan(); }
        });

        pnlSearchInput.add(txtTimKiemTaiKhoan);
        pnl.add(pnlTitle, BorderLayout.WEST);
        pnl.add(pnlSearchInput, BorderLayout.EAST);

        return pnl;
    }

    private JPanel createTaiKhoanTablePanel() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

        modelTaiKhoan = new DefaultTableModel(0, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        modelTaiKhoan.addColumn("Tên Đăng Nhập");
        modelTaiKhoan.addColumn("Mã NV");
        modelTaiKhoan.addColumn("Tên Nhân Viên");
        modelTaiKhoan.addColumn("Vai Trò");

        tblTaiKhoan = new JTable(modelTaiKhoan);
        tblTaiKhoan.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        tblTaiKhoan.setRowHeight(35);
        tblTaiKhoan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblTaiKhoan.setGridColor(new Color(220, 225, 235));

        JTableHeader header = tblTaiKhoan.getTableHeader();
        header.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        header.setBackground(ColorPalette.PRIMARY);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
        
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

        tblTaiKhoan.getColumnModel().getColumn(0).setPreferredWidth(150);
        tblTaiKhoan.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblTaiKhoan.getColumnModel().getColumn(2).setPreferredWidth(200);
        tblTaiKhoan.getColumnModel().getColumn(3).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(tblTaiKhoan);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        scrollPane.setColumnHeaderView(tblTaiKhoan.getTableHeader());
        pnl.add(scrollPane, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel createTaiKhoanButtonPanel() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

        JButton btnThem = new JButton("Tạo Tài Khoản");
        btnThem.setPreferredSize(new Dimension(140, 40));
        btnThem.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnThem.setBackground(new Color(52, 211, 153));
        btnThem.setForeground(Color.WHITE);
        btnThem.setFocusPainted(false);
        btnThem.setBorderPainted(false);
        btnThem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnThem.addActionListener(e -> showTaiKhoanDialog(null));

        JButton btnSua = new JButton("Sửa");
        btnSua.setPreferredSize(new Dimension(120, 40));
        btnSua.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnSua.setBackground(ColorPalette.PRIMARY);
        btnSua.setForeground(Color.WHITE);
        btnSua.setFocusPainted(false);
        btnSua.setBorderPainted(false);
        btnSua.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSua.addActionListener(e -> {
            int row = tblTaiKhoan.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Chọn tài khoản để sửa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String tenDangNhap = (String) modelTaiKhoan.getValueAt(row, 0);
            try {
                TaiKhoanNhanVien tk = taiKhoanDAO.getTaiKhoanByUsername(tenDangNhap);
                showTaiKhoanDialog(tk);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnXoa = new JButton("Xóa");
        btnXoa.setPreferredSize(new Dimension(120, 40));
        btnXoa.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnXoa.setBackground(new Color(239, 68, 68));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setFocusPainted(false);
        btnXoa.setBorderPainted(false);
        btnXoa.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXoa.addActionListener(e -> {
            int row = tblTaiKhoan.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Chọn tài khoản để xóa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String tenDangNhap = (String) modelTaiKhoan.getValueAt(row, 0);
            int result = JOptionPane.showConfirmDialog(this, "Xác nhận xóa tài khoản này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                try {
                    taiKhoanDAO.deleteTaiKhoan(tenDangNhap);
                    JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadTaiKhoanData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        pnl.add(btnThem);
        pnl.add(btnSua);
        pnl.add(btnXoa);

        return pnl;
    }

    private void showTaiKhoanDialog(TaiKhoanNhanVien tk) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), tk == null ? "Tạo Tài Khoản" : "Sửa Tài Khoản");
        dialog.setSize(550, 400);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(25, 25, 25, 25));
        pnl.setBackground(Color.WHITE);

        JLabel lblTenDN = new JLabel("Tên Đăng Nhập:");
        lblTenDN.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        JTextField txtTenDN = new JTextField();
        txtTenDN.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtTenDN.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtTenDN.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtTenDN.setEditable(tk == null);

        JLabel lblMatKhau = new JLabel("Mật Khẩu:");
        lblMatKhau.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        JPasswordField txtMatKhau = new JPasswordField();
        txtMatKhau.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtMatKhau.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtMatKhau.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        JLabel lblMaNV = new JLabel("Mã Nhân Viên:");
        lblMaNV.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        JComboBox<String> cboMaNV = new JComboBox<>();
        cboMaNV.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cboMaNV.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));

        JLabel lblVaiTro = new JLabel("Vai Trò:");
        lblVaiTro.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        JComboBox<String> cboVaiTro = new JComboBox<>(new String[]{"Admin", "Nhân Viên"});
        cboVaiTro.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cboVaiTro.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));

        try {
            List<NhanVien> nvList = nhanVienDAO.getAllNhanVien();
            for (NhanVien nv : nvList) {
                cboMaNV.addItem(nv.getMaNV() + " - " + nv.getTenNV());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (tk != null) {
            txtTenDN.setText(tk.getTenDangNhap());
            txtMatKhau.setText(tk.getMatKhau());
            cboMaNV.setSelectedItem(tk.getMaNV());
            cboVaiTro.setSelectedItem(tk.getVaiTro());
        }

        pnl.add(lblTenDN);
        pnl.add(txtTenDN);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblMatKhau);
        pnl.add(txtMatKhau);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblMaNV);
        pnl.add(cboMaNV);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblVaiTro);
        pnl.add(cboVaiTro);
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
                String maNV = ((String) cboMaNV.getSelectedItem()).split(" - ")[0];
                TaiKhoanNhanVien newTK = new TaiKhoanNhanVien(
                    txtTenDN.getText().trim(),
                    new String(txtMatKhau.getPassword()),
                    (String) cboVaiTro.getSelectedItem(),
                    maNV
                );

                if (tk == null) {
                    taiKhoanDAO.createTaiKhoan(newTK);
                } else {
                    taiKhoanDAO.createTaiKhoan(newTK);
                }

                dialog.dispose();
                JOptionPane.showMessageDialog(FrmQuanLyNhanVienPanel.this, "Lưu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadTaiKhoanData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(FrmQuanLyNhanVienPanel.this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
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

        dialog.add(pnl);
        dialog.setVisible(true);
    }

    private void loadAllData() {
        loadNhanVienData();
        loadTaiKhoanData();
    }

    private void loadNhanVienData() {
        modelNhanVien.setRowCount(0);
        try {
            List<NhanVien> list = nhanVienDAO.getAllNhanVien();
            for (NhanVien nv : list) {
                modelNhanVien.addRow(new Object[]{
                    nv.getMaNV(),
                    nv.getTenNV(),
                    nv.getMaChucVu(),
                    nv.getLuong().toPlainString() + " VNĐ",
                    nv.getSoDienThoai() != null ? nv.getSoDienThoai() : "N/A"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTaiKhoanData() {
        modelTaiKhoan.setRowCount(0);
        try {
            List<TaiKhoanNhanVien> list = taiKhoanDAO.getAllTaiKhoan();
            for (TaiKhoanNhanVien tk : list) {
                NhanVien nv = nhanVienDAO.getNhanVienByMa(tk.getMaNV());
                modelTaiKhoan.addRow(new Object[]{
                    tk.getTenDangNhap(),
                    tk.getMaNV(),
                    nv != null ? nv.getTenNV() : "N/A",
                    tk.getVaiTro()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSearchNhanVien() {
        String keyword = txtTimKiemNhanVien.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadNhanVienData();
            return;
        }

        modelNhanVien.setRowCount(0);
        try {
            List<NhanVien> list = nhanVienDAO.getAllNhanVien();
            for (NhanVien nv : list) {
                if (nv.getMaNV().toLowerCase().contains(keyword) || nv.getTenNV().toLowerCase().contains(keyword)) {
                    modelNhanVien.addRow(new Object[]{
                        nv.getMaNV(),
                        nv.getTenNV(),
                        nv.getMaChucVu(),
                        nv.getLuong().toPlainString() + " VNĐ",
                        nv.getSoDienThoai() != null ? nv.getSoDienThoai() : "N/A"
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSearchTaiKhoan() {
        String keyword = txtTimKiemTaiKhoan.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadTaiKhoanData();
            return;
        }

        modelTaiKhoan.setRowCount(0);
        try {
            List<TaiKhoanNhanVien> list = taiKhoanDAO.getAllTaiKhoan();
            for (TaiKhoanNhanVien tk : list) {
                if (tk.getTenDangNhap().toLowerCase().contains(keyword) || tk.getMaNV().toLowerCase().contains(keyword)) {
                    NhanVien nv = nhanVienDAO.getNhanVienByMa(tk.getMaNV());
                    modelTaiKhoan.addRow(new Object[]{
                        tk.getTenDangNhap(),
                        tk.getMaNV(),
                        nv != null ? nv.getTenNV() : "N/A",
                        tk.getVaiTro()
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
