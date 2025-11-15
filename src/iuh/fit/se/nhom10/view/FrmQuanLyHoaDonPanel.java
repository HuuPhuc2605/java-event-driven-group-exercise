package iuh.fit.se.nhom10.view;

import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.model.HoaDon;
import iuh.fit.se.nhom10.model.ThanhToan;
import iuh.fit.se.nhom10.model.ChiTietHoaDon;
import iuh.fit.se.nhom10.model.NhanVien;
import iuh.fit.se.nhom10.model.KhachHang;
import iuh.fit.se.nhom10.dao.HoaDonDAO;
import iuh.fit.se.nhom10.dao.ThanhToanDAO;
import iuh.fit.se.nhom10.dao.ChiTietHoaDonDAO;
import iuh.fit.se.nhom10.dao.NhanVienDAO;
import iuh.fit.se.nhom10.dao.KhachHangDAO;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Professional Invoice & Payment Management Panel
 * Manages: Hóa Đơn (Invoices), Chi Tiết Hóa Đơn (Invoice Details), Thanh Toán (Payments)
 */
public class FrmQuanLyHoaDonPanel extends JPanel {
    private TaiKhoanNhanVien adminHienTai;
    private HoaDonDAO hoaDonDAO;
    private ThanhToanDAO thanhToanDAO;
    private ChiTietHoaDonDAO chiTietHoaDonDAO;
    private NhanVienDAO nhanVienDAO;
    private KhachHangDAO khachHangDAO;
    
    private JTable tblHoaDon;
    private DefaultTableModel modelHoaDon;
    private JTextField txtTimKiem;
    private JPanel pnlDetailArea;
    private JLabel lblInvoiceStatus;

    public FrmQuanLyHoaDonPanel(TaiKhoanNhanVien admin) throws SQLException {
        this.adminHienTai = admin;
        this.hoaDonDAO = new HoaDonDAO();
        this.thanhToanDAO = new ThanhToanDAO();
        this.chiTietHoaDonDAO = new ChiTietHoaDonDAO();
        this.nhanVienDAO = new NhanVienDAO();
        this.khachHangDAO = new KhachHangDAO();
        setupUI();
        loadInvoiceData();
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(ColorPalette.BACKGROUND_CONTENT);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Search bar panel
        JPanel pnlSearchBar = createSearchBar();
        add(pnlSearchBar, BorderLayout.NORTH);

        // Main content - split between invoice list and detail view
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setBackground(ColorPalette.BACKGROUND_CONTENT);
        splitPane.setDividerLocation(600);

        // Invoice list panel (left side)
        JPanel pnlInvoiceList = createInvoiceListPanel();
        splitPane.setLeftComponent(pnlInvoiceList);

        // Detail area (right side)
        pnlDetailArea = createDetailPanel();
        splitPane.setRightComponent(pnlDetailArea);

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createSearchBar() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BorderLayout(10, 0));
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
        pnlTitle.setPreferredSize(new Dimension(400, 45));

        JLabel lblTitle = new JLabel("Quản Lý Hóa Đơn & Thanh Toán");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE - 4, Font.BOLD));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(8, 25, 8, 15));
        pnlTitle.add(lblTitle);

        JPanel pnlSearchInput = new JPanel();
        pnlSearchInput.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pnlSearchInput.setBackground(ColorPalette.BACKGROUND_CONTENT);

        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(300, 38));
        txtTimKiem.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtTimKiem.setToolTipText("Tìm kiếm theo mã hóa đơn, khách hàng hoặc nhân viên");
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

    private JPanel createInvoiceListPanel() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

        modelHoaDon = new DefaultTableModel(0, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelHoaDon.addColumn("Mã HĐ");
        modelHoaDon.addColumn("Ngày Lập");
        modelHoaDon.addColumn("Tổng Tiền");
        modelHoaDon.addColumn("Thanh Toán");
        modelHoaDon.addColumn("Nhân Viên");
        modelHoaDon.addColumn("Khách Hàng");

        tblHoaDon = new JTable(modelHoaDon);
        tblHoaDon.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        tblHoaDon.setRowHeight(35);
        tblHoaDon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblHoaDon.setGridColor(new Color(220, 225, 235));
        tblHoaDon.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblHoaDon.getSelectedRow();
                if (selectedRow >= 0) {
                    String maHD = (String) modelHoaDon.getValueAt(selectedRow, 0);
                    showInvoiceDetail(maHD);
                }
            }
        });

        JTableHeader header = tblHoaDon.getTableHeader();
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

        tblHoaDon.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblHoaDon.getColumnModel().getColumn(1).setPreferredWidth(120);
        tblHoaDon.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblHoaDon.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblHoaDon.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblHoaDon.getColumnModel().getColumn(5).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(tblHoaDon);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        pnl.add(scrollPane, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel createDetailPanel() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblPlaceholder = new JLabel("Chọn hóa đơn để xem chi tiết");
        lblPlaceholder.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.ITALIC));
        lblPlaceholder.setForeground(ColorPalette.TEXT_PLACEHOLDER);
        lblPlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
        pnl.add(lblPlaceholder, BorderLayout.CENTER);

        return pnl;
    }

    private void showInvoiceDetail(String maHD) {
        try {
            HoaDon hoaDon = hoaDonDAO.getHoaDonByMa(maHD);
            if (hoaDon == null) return;

            pnlDetailArea.removeAll();
            pnlDetailArea.setLayout(new BorderLayout(10, 10));

            String tenNV = "N/A";
            NhanVien nv = nhanVienDAO.getNhanVienByMa(hoaDon.getMaNV());
            if (nv != null) tenNV = nv.getTenNV();

            String tenKH = hoaDon.coKhachHang() ? "(Khách lẻ)" : "N/A";
            if (hoaDon.coKhachHang()) {
                KhachHang kh = khachHangDAO.getKhachHangByMa(hoaDon.getMaKH());
                if (kh != null) tenKH = kh.getTenKH();
            }

            // Invoice header info
            JPanel pnlHeader = createInvoiceHeader(hoaDon, tenNV, tenKH);
            pnlDetailArea.add(pnlHeader, BorderLayout.NORTH);

            // Tabbed pane for details and payment
            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
            tabbedPane.setBackground(ColorPalette.BACKGROUND_CONTENT);

            // Invoice items tab
            JPanel pnlItems = createInvoiceItemsPanel(hoaDon);
            tabbedPane.addTab("Chi Tiết Vé", pnlItems);

            // Payment tab
            JPanel pnlPayment = createPaymentPanel(hoaDon);
            tabbedPane.addTab("Thanh Toán", pnlPayment);

            pnlDetailArea.add(tabbedPane, BorderLayout.CENTER);
            pnlDetailArea.revalidate();
            pnlDetailArea.repaint();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createInvoiceHeader(HoaDon hoaDon, String tenNV, String tenKH) {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBackground(new Color(245, 248, 255));
        pnl.setBorder(BorderFactory.createLineBorder(ColorPalette.PRIMARY, 2));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblMaHD = new JLabel("Mã HĐ: " + hoaDon.getMaHD());
        lblMaHD.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblMaHD.setForeground(ColorPalette.PRIMARY);
        pnl.add(lblMaHD);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        JLabel lblNgayLap = new JLabel("Ngày lập: " + hoaDon.getNgayLap().format(formatter));
        lblNgayLap.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        lblNgayLap.setForeground(ColorPalette.TEXT_BODY);
        pnl.add(lblNgayLap);

        JLabel lblNhanVien = new JLabel("Nhân viên: " + tenNV);
        lblNhanVien.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        lblNhanVien.setForeground(ColorPalette.TEXT_BODY);
        pnl.add(lblNhanVien);

        JLabel lblKhachHang = new JLabel("Khách hàng: " + tenKH);
        lblKhachHang.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        lblKhachHang.setForeground(ColorPalette.TEXT_BODY);
        pnl.add(lblKhachHang);

        return pnl;
    }

    private JPanel createInvoiceItemsPanel(HoaDon hoaDon) throws SQLException {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BorderLayout());
        pnlHeader.setBackground(new Color(245, 248, 255));
        pnlHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ColorPalette.PRIMARY));
        pnlHeader.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel lblHeader = new JLabel("Chi Tiết Vé");
        lblHeader.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblHeader.setForeground(ColorPalette.PRIMARY);
        pnlHeader.add(lblHeader, BorderLayout.WEST);
        
        pnl.add(pnlHeader, BorderLayout.NORTH);

        List<ChiTietHoaDon> chiTietList = chiTietHoaDonDAO.getChiTietByMaHoaDon(hoaDon.getMaHD());

        DefaultTableModel modelItems = new DefaultTableModel(0, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelItems.addColumn("Mã Vé");
        modelItems.addColumn("Đơn Giá");
      
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (ChiTietHoaDon ct : chiTietList) {
            modelItems.addRow(new Object[]{
                ct.getMaVe(),
                ct.getDonGia() != null ? ct.getDonGia().toPlainString() + " VNĐ" : "N/A"
            });
            totalPrice = totalPrice.add(ct.getDonGia() != null ? ct.getDonGia() : BigDecimal.ZERO);
        }

        JTable tblItems = new JTable(modelItems);
        tblItems.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        tblItems.setRowHeight(30);
        tblItems.setGridColor(new Color(220, 225, 235));

        JTableHeader header = tblItems.getTableHeader();
        header.setBackground(ColorPalette.PRIMARY);
        header.setForeground(Color.WHITE);
        header.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        
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

        JScrollPane scrollPane = new JScrollPane(tblItems);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        pnl.add(scrollPane, BorderLayout.CENTER);

        // Summary panel
        JPanel pnlSummary = new JPanel();
        pnlSummary.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        pnlSummary.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnlSummary.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, ColorPalette.BORDER_LIGHT));

        JLabel lblTotal = new JLabel("Tổng cộng: " + totalPrice.toPlainString() + " VNĐ");
        lblTotal.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblTotal.setForeground(ColorPalette.ACCENT);
        pnlSummary.add(lblTotal);

        pnl.add(pnlSummary, BorderLayout.SOUTH);

        return pnl;
    }

    private JPanel createPaymentPanel(HoaDon hoaDon) throws SQLException {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BorderLayout());
        pnlHeader.setBackground(new Color(245, 248, 255));
        pnlHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ColorPalette.PRIMARY));
        pnlHeader.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel lblHeader = new JLabel("Thanh Toán");
        lblHeader.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblHeader.setForeground(ColorPalette.PRIMARY);
        pnlHeader.add(lblHeader, BorderLayout.WEST);
        
        pnl.add(pnlHeader, BorderLayout.NORTH);

        ThanhToan thanhToan = thanhToanDAO.getThanhToanByMaHoaDon(hoaDon.getMaHD());

        if (thanhToan == null) {
            // No payment yet - show add payment form
            JPanel pnlForm = new JPanel();
            pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
            pnlForm.setBackground(ColorPalette.BACKGROUND_CONTENT);
            pnlForm.setBorder(new EmptyBorder(20, 20, 20, 20));

            JLabel lblInfo = new JLabel("Chưa có thanh toán cho hóa đơn này");
            lblInfo.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.ITALIC));
            lblInfo.setForeground(ColorPalette.TEXT_PLACEHOLDER);
            pnlForm.add(lblInfo);
            pnlForm.add(Box.createVerticalStrut(20));

            // Payment method selector
            JLabel lblPhuongThuc = new JLabel("Phương thức thanh toán:");
            lblPhuongThuc.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
            lblPhuongThuc.setForeground(ColorPalette.TEXT_LABEL);
            pnlForm.add(lblPhuongThuc);

            String[] paymentMethods = {"Tiền Mặt", "MoMo", "Chuyển Khoản", "Credit Card"};
            JComboBox<String> cboPhuongThuc = new JComboBox<>(paymentMethods);
            cboPhuongThuc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            cboPhuongThuc.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
            pnlForm.add(cboPhuongThuc);
            pnlForm.add(Box.createVerticalStrut(15));

            // Status selector
            JLabel lblTrangThai = new JLabel("Trạng thái:");
            lblTrangThai.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
            lblTrangThai.setForeground(ColorPalette.TEXT_LABEL);
            pnlForm.add(lblTrangThai);

            String[] statuses = {"Đã thanh toán", "Chờ thanh toán", "Thất bại"};
            JComboBox<String> cboTrangThai = new JComboBox<>(statuses);
            cboTrangThai.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            cboTrangThai.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
            pnlForm.add(cboTrangThai);

            pnlForm.add(Box.createVerticalGlue());

            // Add button
            JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            pnlButtons.setBackground(ColorPalette.BACKGROUND_CONTENT);

            JButton btnThem = new JButton("Thêm Thanh Toán");
            btnThem.setPreferredSize(new Dimension(150, 40));
            btnThem.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
            btnThem.setBackground(new Color(52, 211, 153));
            btnThem.setForeground(Color.WHITE);
            btnThem.setFocusPainted(false);
            btnThem.setBorderPainted(false);
            btnThem.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btnThem.addActionListener(e -> {
                try {
                    String maThanhToan = "TT" + System.currentTimeMillis();
                    String phuongThuc = (String) cboPhuongThuc.getSelectedItem();
                    String trangThai = (String) cboTrangThai.getSelectedItem();

                    ThanhToan newTT = new ThanhToan(maThanhToan, hoaDon.getMaHD(), phuongThuc, trangThai);
                    thanhToanDAO.createThanhToan(newTT);

                    JOptionPane.showMessageDialog(this, "Thêm thanh toán thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    showInvoiceDetail(hoaDon.getMaHD());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });

            pnlButtons.add(btnThem);
            pnlForm.add(pnlButtons);

            pnl.add(pnlForm, BorderLayout.CENTER);
        } else {
            // Payment exists - show payment details
            JPanel pnlInfo = new JPanel();
            pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
            pnlInfo.setBackground(new Color(240, 255, 240));
            pnlInfo.setBorder(BorderFactory.createLineBorder(ColorPalette.ACCENT, 2));
            pnlInfo.setBorder(new EmptyBorder(20, 20, 20, 20));

            JLabel lblMaTT = new JLabel("Mã Thanh Toán: " + thanhToan.getMaThanhToan());
            lblMaTT.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
            lblMaTT.setForeground(ColorPalette.ACCENT);
            pnlInfo.add(lblMaTT);

            JLabel lblPhuongThuc = new JLabel("Phương thức: " + thanhToan.getPhuongThuc());
            lblPhuongThuc.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
            lblPhuongThuc.setForeground(ColorPalette.TEXT_BODY);
            pnlInfo.add(lblPhuongThuc);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            JLabel lblNgayTT = new JLabel("Ngày thanh toán: " + thanhToan.getNgayThanhToan().format(formatter));
            lblNgayTT.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
            lblNgayTT.setForeground(ColorPalette.TEXT_BODY);
            pnlInfo.add(lblNgayTT);

            Color statusColor = ColorPalette.STATUS_INFO;
            if (thanhToan.getTrangThai().contains("Đã thanh toán")) {
                statusColor = new Color(34, 197, 94);
            } else if (thanhToan.getTrangThai().contains("Thất bại")) {
                statusColor = ColorPalette.STATUS_ERROR;
            }

            JLabel lblTrangThai = new JLabel("Trạng thái: " + thanhToan.getTrangThai());
            lblTrangThai.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
            lblTrangThai.setForeground(statusColor);
            pnlInfo.add(lblTrangThai);

            pnl.add(pnlInfo, BorderLayout.NORTH);

            // Edit payment button
            JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            pnlButtons.setBackground(ColorPalette.BACKGROUND_CONTENT);

            JButton btnSua = new JButton("Cập Nhật");
            btnSua.setPreferredSize(new Dimension(120, 40));
            btnSua.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
            btnSua.setBackground(ColorPalette.PRIMARY);
            btnSua.setForeground(Color.WHITE);
            btnSua.setFocusPainted(false);
            btnSua.setBorderPainted(false);
            btnSua.setCursor(new Cursor(Cursor.HAND_CURSOR));

            final ThanhToan finalThanhToan = thanhToan;
            btnSua.addActionListener(e -> {
                try {
                    showEditPaymentDialog(finalThanhToan);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });

            pnlButtons.add(btnSua);
            pnl.add(pnlButtons, BorderLayout.SOUTH);
        }

        return pnl;
    }

    private void showEditPaymentDialog(ThanhToan thanhToan) throws SQLException {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Cập Nhật Thanh Toán");
        dialog.setSize(400, 280);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnl.setBackground(Color.WHITE);

        JLabel lblPhuongThuc = new JLabel("Phương thức:");
        lblPhuongThuc.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        lblPhuongThuc.setForeground(ColorPalette.TEXT_LABEL);
        pnl.add(lblPhuongThuc);

        String[] paymentMethods = {"Tiền Mặt", "MoMo", "Chuyển Khoản", "Credit Card"};
        JComboBox<String> cboPhuongThuc = new JComboBox<>(paymentMethods);
        cboPhuongThuc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cboPhuongThuc.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        cboPhuongThuc.setSelectedItem(thanhToan.getPhuongThuc());
        pnl.add(cboPhuongThuc);
        pnl.add(Box.createVerticalStrut(15));

        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        lblTrangThai.setForeground(ColorPalette.TEXT_LABEL);
        pnl.add(lblTrangThai);

        String[] statuses = {"Đã thanh toán", "Chờ thanh toán", "Thất bại"};
        JComboBox<String> cboTrangThai = new JComboBox<>(statuses);
        cboTrangThai.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cboTrangThai.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        cboTrangThai.setSelectedItem(thanhToan.getTrangThai());
        pnl.add(cboTrangThai);
        pnl.add(Box.createVerticalStrut(20));

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlButtons.setBackground(Color.WHITE);

        JButton btnLuu = new JButton("Lưu");
        btnLuu.setPreferredSize(new Dimension(100, 40));
        btnLuu.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnLuu.setBackground(ColorPalette.PRIMARY);
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFocusPainted(false);
        btnLuu.setBorderPainted(false);
        btnLuu.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLuu.addActionListener(e -> {
            try {
                thanhToan.setPhuongThuc((String) cboPhuongThuc.getSelectedItem());
                thanhToan.setTrangThai((String) cboTrangThai.getSelectedItem());
                thanhToanDAO.updateThanhToan(thanhToan);

                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                showInvoiceDetail(thanhToan.getMaHD());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnHuy = new JButton("Hủy");
        btnHuy.setPreferredSize(new Dimension(100, 40));
        btnHuy.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
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

    private void loadInvoiceData() {
        modelHoaDon.setRowCount(0);
        try {
            List<HoaDon> hoaDonList = hoaDonDAO.getAllHoaDon();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for (HoaDon hd : hoaDonList) {
                String tenNV = "N/A";
                NhanVien nv = nhanVienDAO.getNhanVienByMa(hd.getMaNV());
                if (nv != null) tenNV = nv.getTenNV();

                String tenKH = hd.coKhachHang() ? "(Khách lẻ)" : "N/A";
                if (hd.coKhachHang()) {
                    KhachHang kh = khachHangDAO.getKhachHangByMa(hd.getMaKH());
                    if (kh != null) tenKH = kh.getTenKH();
                }

                modelHoaDon.addRow(new Object[]{
                    hd.getMaHD(),
                    hd.getNgayLap().format(formatter),
                    hd.getTongTien() != null ? hd.getTongTien().toPlainString() + " VNĐ" : "0 VNĐ",
                    hd.getThanhToan() != null ? hd.getThanhToan().toPlainString() + " VNĐ" : "0 VNĐ",
                    tenNV,
                    tenKH
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSearch() {
        String keyword = txtTimKiem.getText().trim();
        if (keyword.isEmpty()) {
            loadInvoiceData();
            return;
        }

        modelHoaDon.setRowCount(0);
        try {
            List<HoaDon> hoaDonList = hoaDonDAO.searchHoaDon(keyword);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (HoaDon hd : hoaDonList) {
                String tenNV = "N/A";
                NhanVien nv = nhanVienDAO.getNhanVienByMa(hd.getMaNV());
                if (nv != null) tenNV = nv.getTenNV();

                String tenKH = hd.coKhachHang() ? "(Khách lẻ)" : "N/A";
                if (hd.coKhachHang()) {
                    KhachHang kh = khachHangDAO.getKhachHangByMa(hd.getMaKH());
                    if (kh != null) tenKH = kh.getTenKH();
                }

                modelHoaDon.addRow(new Object[]{
                    hd.getMaHD(),
                    hd.getNgayLap().format(formatter),
                    hd.getTongTien() != null ? hd.getTongTien().toPlainString() + " VNĐ" : "0 VNĐ",
                    hd.getThanhToan() != null ? hd.getThanhToan().toPlainString() + " VNĐ" : "0 VNĐ",
                    tenNV,
                    tenKH
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
