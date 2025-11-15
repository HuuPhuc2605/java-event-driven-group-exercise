package iuh.fit.se.nhom10.view;

import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.model.KhachHang;
import iuh.fit.se.nhom10.dao.KhachHangDAO;
import iuh.fit.se.nhom10.util.ColorPalette;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class FrmQuanLyKhachHangPanel extends JPanel {
    private TaiKhoanNhanVien adminHienTai;
    private KhachHangDAO khachHangDAO;
    
    private JTable tblKhachHang;
    private DefaultTableModel modelKhachHang;
    private JTextField txtTimKiem;

    public FrmQuanLyKhachHangPanel(TaiKhoanNhanVien admin) throws SQLException {
        this.adminHienTai = admin;
        this.khachHangDAO = new KhachHangDAO();
        setupUI();
        loadKhachHangData();
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(ColorPalette.BACKGROUND_CONTENT);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        add(createSearchBar(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createSearchBar() {
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

        JLabel lblTitle = new JLabel("Quản Lý Khách Hàng");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE - 4, Font.BOLD));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(8, 15, 8, 15));
        pnlTitle.add(lblTitle);

        JPanel pnlSearchInput = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pnlSearchInput.setBackground(ColorPalette.BACKGROUND_CONTENT);

        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(300, 38));
        txtTimKiem.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtTimKiem.setToolTipText("Tìm kiếm theo mã hoặc tên khách hàng");
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { handleSearch(); }
            public void removeUpdate(DocumentEvent e) { handleSearch(); }
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

        modelKhachHang = new DefaultTableModel(0, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        modelKhachHang.addColumn("Mã KH");
        modelKhachHang.addColumn("Tên Khách Hàng");
        modelKhachHang.addColumn("Số Điện Thoại");

        tblKhachHang = new JTable(modelKhachHang);
        tblKhachHang.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        tblKhachHang.setRowHeight(35);
        tblKhachHang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblKhachHang.setGridColor(new Color(220, 225, 235));

        JTableHeader header = tblKhachHang.getTableHeader();
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

        tblKhachHang.getColumnModel().getColumn(0).setPreferredWidth(120);
        tblKhachHang.getColumnModel().getColumn(1).setPreferredWidth(300);
        tblKhachHang.getColumnModel().getColumn(2).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(tblKhachHang);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        scrollPane.setColumnHeaderView(tblKhachHang.getTableHeader());
        pnl.add(scrollPane, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel createButtonPanel() {
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
        btnThem.addActionListener(e -> showKhachHangDialog(null));

        JButton btnSua = new JButton("Sửa");
        btnSua.setPreferredSize(new Dimension(120, 40));
        btnSua.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnSua.setBackground(ColorPalette.PRIMARY);
        btnSua.setForeground(Color.WHITE);
        btnSua.setFocusPainted(false);
        btnSua.setBorderPainted(false);
        btnSua.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSua.addActionListener(e -> {
            int row = tblKhachHang.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Chọn khách hàng để sửa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String maKH = (String) modelKhachHang.getValueAt(row, 0);
            try {
                KhachHang kh = khachHangDAO.getKhachHangByMa(maKH);
                showKhachHangDialog(kh);
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
            int row = tblKhachHang.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Chọn khách hàng để xóa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String maKH = (String) modelKhachHang.getValueAt(row, 0);
            int result = JOptionPane.showConfirmDialog(this, "Xác nhận xóa khách hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                try {
                    khachHangDAO.deleteKhachHang(maKH);
                    JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadKhachHangData();
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

    private void showKhachHangDialog(KhachHang kh) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), kh == null ? "Thêm Khách Hàng" : "Sửa Khách Hàng");
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(25, 25, 25, 25));
        pnl.setBackground(Color.WHITE);

        JLabel lblMaKH = new JLabel("Mã Khách Hàng:");
        lblMaKH.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        JTextField txtMaKH = new JTextField();
        txtMaKH.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtMaKH.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtMaKH.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtMaKH.setEditable(kh == null);

        JLabel lblTenKH = new JLabel("Tên Khách Hàng:");
        lblTenKH.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        JTextField txtTenKH = new JTextField();
        txtTenKH.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtTenKH.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtTenKH.setBorder(BorderFactory.createCompoundBorder(
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

        if (kh != null) {
            txtMaKH.setText(kh.getMaKH());
            txtTenKH.setText(kh.getTenKH());
            txtSDT.setText(kh.getSoDienThoai() != null ? kh.getSoDienThoai() : "");
        }

        pnl.add(lblMaKH);
        pnl.add(txtMaKH);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblTenKH);
        pnl.add(txtTenKH);
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
                KhachHang newKH = new KhachHang(
                    txtMaKH.getText().trim(),
                    txtTenKH.getText().trim(),
                    txtSDT.getText().trim()
                );

                if (kh == null) {
                    khachHangDAO.createKhachHang(newKH);
                } else {
                    khachHangDAO.updateKhachHang(newKH);
                }

                dialog.dispose();
                JOptionPane.showMessageDialog(FrmQuanLyKhachHangPanel.this, "Lưu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadKhachHangData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(FrmQuanLyKhachHangPanel.this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
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

    private void loadKhachHangData() {
        modelKhachHang.setRowCount(0);
        try {
            List<KhachHang> list = khachHangDAO.getAllKhachHang();
            for (KhachHang kh : list) {
                modelKhachHang.addRow(new Object[]{
                    kh.getMaKH(),
                    kh.getTenKH(),
                    kh.getSoDienThoai() != null ? kh.getSoDienThoai() : "N/A"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSearch() {
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadKhachHangData();
            return;
        }

        modelKhachHang.setRowCount(0);
        try {
            List<KhachHang> list = khachHangDAO.getAllKhachHang();
            for (KhachHang kh : list) {
                if (kh.getMaKH().toLowerCase().contains(keyword) || kh.getTenKH().toLowerCase().contains(keyword)) {
                    modelKhachHang.addRow(new Object[]{
                        kh.getMaKH(),
                        kh.getTenKH(),
                        kh.getSoDienThoai() != null ? kh.getSoDienThoai() : "N/A"
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
