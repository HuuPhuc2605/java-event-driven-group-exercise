package iuh.fit.se.nhom10.view.admin;

import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.dao.*;
import iuh.fit.se.nhom10.util.ColorPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.lang.String;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Báo Cáo & Thống Kê - Professional Reporting and Analytics Panel
 */
public class FrmQuanLyBaoCaoPanel extends JPanel {
    private TaiKhoanNhanVien adminHienTai;
    private JTabbedPane tabbedPane;
    private JPanel pnlRevenueDaily, pnlRevenueMonthly, pnlRevenueYearly;
    private JPanel pnlRevenueByFilm, pnlRevenueByScreening, pnlRevenueByEmployee;
    private JPanel pnlTopFilms, pnlTicketDaily, pnlCustomerStats, pnlPromotionStats;

    public FrmQuanLyBaoCaoPanel(TaiKhoanNhanVien admin) throws SQLException {
        this.adminHienTai = admin;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(ColorPalette.BACKGROUND_CONTENT);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header
        JPanel pnlHeader = createHeaderPanel();
        add(pnlHeader, BorderLayout.NORTH);

        // Tabbed Pane for different reports
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        tabbedPane.setBackground(ColorPalette.BACKGROUND_CONTENT);

        // Tab 1: Revenue by Time Period
        tabbedPane.addTab("Doanh Thu - Thời Gian", createRevenueByTimeTab());

        // Tab 2: Revenue by Category
        tabbedPane.addTab("Doanh Thu - Phân Loại", createRevenueByCategoryTab());

        // Tab 3: Other Statistics
        tabbedPane.addTab("Thống Kê Khác", createOtherStatisticsTab());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel pnl = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(ColorPalette.PRIMARY);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            }
        };
        pnl.setOpaque(false);
        pnl.setPreferredSize(new Dimension(0, 50));

        JLabel lblTitle = new JLabel("Báo Cáo & Thống Kê");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE, Font.BOLD));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(8, 25, 8, 15));
        pnl.add(lblTitle);

        return pnl;
    }

    private JPanel createRevenueByTimeTab() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Filter Panel
        JPanel pnlFilter = new JPanel();
        pnlFilter.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlFilter.setBackground(new Color(248, 250, 252));
        pnlFilter.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 235), 1));

        JLabel lblFilter = new JLabel("Lọc theo:");
        lblFilter.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        
        String[] filterOptions = {"Toàn Bộ", "Ngày", "Tháng", "Năm"};
        JComboBox<String> cbFilter = new JComboBox<>(filterOptions);
        cbFilter.setPreferredSize(new Dimension(150, 35));
        cbFilter.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        cbFilter.setBackground(Color.WHITE);
        cbFilter.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));

        pnlFilter.add(lblFilter);
        pnlFilter.add(cbFilter);
        pnl.add(pnlFilter, BorderLayout.NORTH);

        DefaultTableModel modelRevenue = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        modelRevenue.addColumn("Kỳ");
        modelRevenue.addColumn("Số Vé");
        modelRevenue.addColumn("Tổng Doanh Thu");
        modelRevenue.addColumn("Trung Bình/Vé");

        JTable tblRevenue = new JTable(modelRevenue);
        styleRevenueTable(tblRevenue);

        // Load data based on filter selection
        cbFilter.addActionListener(e -> {
            modelRevenue.setRowCount(0);
            int selectedIndex = cbFilter.getSelectedIndex();
            try {
                if (selectedIndex == 1) loadRevenueByDay(modelRevenue);
                else if (selectedIndex == 2) loadRevenueByMonth(modelRevenue);
                else if (selectedIndex == 3) loadRevenueByYear(modelRevenue);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });

        // Load day data by default
        try {
            loadRevenueByDay(modelRevenue);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(tblRevenue);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        pnl.add(scrollPane, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel createRevenueByCategoryTab() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Filter Panel
        JPanel pnlFilter = new JPanel();
        pnlFilter.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlFilter.setBackground(new Color(248, 250, 252));
        pnlFilter.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 235), 1));

        JLabel lblFilter = new JLabel("Xem theo:");
        lblFilter.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        
        String[] filterOptions = {"Phim", "Lịch Chiếu", "Nhân Viên"};
        JComboBox<String> cbFilter = new JComboBox<>(filterOptions);
        cbFilter.setPreferredSize(new Dimension(150, 35));
        cbFilter.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        cbFilter.setBackground(Color.WHITE);
        cbFilter.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));

        pnlFilter.add(lblFilter);
        pnlFilter.add(cbFilter);
        pnl.add(pnlFilter, BorderLayout.NORTH);

        DefaultTableModel modelRevenue = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable tblRevenue = new JTable(modelRevenue);
        styleRevenueTable(tblRevenue);

        // Load data based on category selection
        cbFilter.addActionListener(e -> {
            modelRevenue.setRowCount(0);
            modelRevenue.setColumnCount(0);
            
            int selectedIndex = cbFilter.getSelectedIndex();
            try {
                if (selectedIndex == 0) {
                    // Film
                    modelRevenue.addColumn("Mã Phim");
                    modelRevenue.addColumn("Tên Phim");
                    modelRevenue.addColumn("Số Vé");
                    modelRevenue.addColumn("Doanh Thu");
                    loadRevenueByFilm(modelRevenue);
                } else if (selectedIndex == 1) {
                    // Screening
                    modelRevenue.addColumn("Lịch Chiếu");
                    modelRevenue.addColumn("Phim");
                    modelRevenue.addColumn("Ngày");
                    modelRevenue.addColumn("Giờ");
                    modelRevenue.addColumn("Số Vé");
                    modelRevenue.addColumn("Doanh Thu");
                    loadRevenueByScreening(modelRevenue);
                } else if (selectedIndex == 2) {
                    // Employee
                    modelRevenue.addColumn("Mã NV");
                    modelRevenue.addColumn("Tên Nhân Viên");
                    modelRevenue.addColumn("Số Hóa Đơn");
                    modelRevenue.addColumn("Doanh Thu");
                    loadRevenueByEmployee(modelRevenue);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });

        // Initialize with Film data
        modelRevenue.addColumn("Mã Phim");
        modelRevenue.addColumn("Tên Phim");
        modelRevenue.addColumn("Số Vé");
        modelRevenue.addColumn("Doanh Thu");
        try {
            loadRevenueByFilm(modelRevenue);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(tblRevenue);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        pnl.add(scrollPane, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel createOtherStatisticsTab() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Sub-tabs for different statistics
        JTabbedPane subTabs = new JTabbedPane();
        subTabs.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        subTabs.setBackground(ColorPalette.BACKGROUND_CONTENT);

        subTabs.addTab("Top Phim", createTopFilmsPanel());
        subTabs.addTab("Vé Hàng Ngày", createTicketDailyPanel());
        subTabs.addTab("Khách Hàng", createCustomerStatsPanel());
        subTabs.addTab("Khuyến Mãi", createPromotionStatsPanel());

        pnl.add(subTabs, BorderLayout.CENTER);
        return pnl;
    }

    private JPanel createTopFilmsPanel() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("Top 5 Phim Bán Chạy Nhất");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE + 2, Font.BOLD));
        lblTitle.setForeground(new Color(244, 63, 94));
        pnl.add(lblTitle, BorderLayout.NORTH);

        DefaultTableModel modelTop = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        modelTop.addColumn("Xếp Hạng");
        modelTop.addColumn("Tên Phim");
        modelTop.addColumn("Số Vé");
        modelTop.addColumn("Doanh Thu");

        JTable tblTop = new JTable(modelTop);
        styleRevenueTable(tblTop);

        try {
            loadTopFilmsData(modelTop);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(tblTop);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.ACCENT, 1));
        pnl.add(scrollPane, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel createTicketDailyPanel() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("Số Vé Bán Theo Ngày");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE + 2, Font.BOLD));
        lblTitle.setForeground(ColorPalette.PRIMARY);
        pnl.add(lblTitle, BorderLayout.NORTH);

        DefaultTableModel modelTicket = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        modelTicket.addColumn("Ngày");
        modelTicket.addColumn("Số Vé");
        modelTicket.addColumn("Doanh Thu");

        JTable tblTicket = new JTable(modelTicket);
        styleRevenueTable(tblTicket);

        try {
            loadTicketDaily(modelTicket);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(tblTicket);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        pnl.add(scrollPane, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel createCustomerStatsPanel() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("Top 10 Khách Hàng Mua Nhiều Nhất");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE + 2, Font.BOLD));
        lblTitle.setForeground(new Color(59, 130, 246));
        pnl.add(lblTitle, BorderLayout.NORTH);

        DefaultTableModel modelCustomer = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        modelCustomer.addColumn("Mã KH");
        modelCustomer.addColumn("Tên Khách");
        modelCustomer.addColumn("Số Vé");
        modelCustomer.addColumn("Tổng Chi");
        modelCustomer.addColumn("Mua Gần Nhất");

        JTable tblCustomer = new JTable(modelCustomer);
        styleRevenueTable(tblCustomer);

        try {
            loadCustomerStats(modelCustomer);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(tblCustomer);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        pnl.add(scrollPane, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel createPromotionStatsPanel() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("Top 10 Khuyến Mãi Được Dùng Nhiều");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE + 2, Font.BOLD));
        lblTitle.setForeground(new Color(168, 85, 247));
        pnl.add(lblTitle, BorderLayout.NORTH);

        DefaultTableModel modelPromo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        modelPromo.addColumn("Mã KM");
        modelPromo.addColumn("Tên KM");
        modelPromo.addColumn("Lần Dùng");
        modelPromo.addColumn("Tiền Giảm");
        modelPromo.addColumn("Tỉ Lệ %");

        JTable tblPromo = new JTable(modelPromo);
        styleRevenueTable(tblPromo);

        try {
            loadPromotionStats(modelPromo);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(tblPromo);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        pnl.add(scrollPane, BorderLayout.CENTER);

        return pnl;
    }

    private void styleRevenueTable(JTable table) {
        table.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        table.setRowHeight(35);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(220, 225, 235));
        table.setShowGrid(true);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        header.setBackground(ColorPalette.PRIMARY);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        
        header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                                                          boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(ColorPalette.PRIMARY);
                setForeground(Color.WHITE);
                setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
                setHorizontalAlignment(CENTER);
                setOpaque(true);
                setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));
                return this;
            }
        });
        
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(248, 250, 252));
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                } else {
                    c.setBackground(ColorPalette.PRIMARY);
                    c.setForeground(Color.WHITE);
                }
                
                setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 8, 5, 8));
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });
    }

    private void loadRevenueByDay(DefaultTableModel model) throws SQLException {
        HoaDonDAO dao = new HoaDonDAO();
        Map<LocalDate, BigDecimal[]> data = dao.getRevenueByDay();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        data.entrySet().stream()
            .sorted((a, b) -> b.getKey().compareTo(a.getKey()))
            .forEach(entry -> {
                BigDecimal[] values = entry.getValue();
                BigDecimal average = values[1].intValue() > 0 ? values[0].divide(values[1], 0, java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO;
                model.addRow(new Object[]{
                    entry.getKey().format(formatter),
                    String.format("%,d", values[1].intValue()),
                    String.format("%,d", values[0].longValue()) + " VNĐ",
                    String.format("%,d", average.longValue()) + " VNĐ"
                });
            });
    }

    private void loadRevenueByMonth(DefaultTableModel model) throws SQLException {
        HoaDonDAO dao = new HoaDonDAO();
        Map<String, BigDecimal[]> data = dao.getRevenueByMonth();

        data.entrySet().stream()
            .sorted((a, b) -> b.getKey().compareTo(a.getKey()))
            .forEach(entry -> {
                BigDecimal[] values = entry.getValue();
                BigDecimal average = values[1].intValue() > 0 ? values[0].divide(values[1], 0, java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO;
                model.addRow(new Object[]{
                    entry.getKey(),
                    String.format("%,d", values[1].intValue()),
                    String.format("%,d", values[0].longValue()) + " VNĐ",
                    String.format("%,d", average.longValue()) + " VNĐ"
                });
            });
    }

    private void loadRevenueByYear(DefaultTableModel model) throws SQLException {
        HoaDonDAO dao = new HoaDonDAO();
        Map<Integer, BigDecimal[]> data = dao.getRevenueByYear();

        data.entrySet().stream()
            .sorted((a, b) -> b.getKey().compareTo(a.getKey()))
            .forEach(entry -> {
                BigDecimal[] values = entry.getValue();
                BigDecimal average = values[1].intValue() > 0 ? values[0].divide(values[1], 0, java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO;
                model.addRow(new Object[]{
                    entry.getKey(),
                    String.format("%,d", values[1].intValue()),
                    String.format("%,d", values[0].longValue()) + " VNĐ",
                    String.format("%,d", average.longValue()) + " VNĐ"
                });
            });
    }

    private void loadRevenueByFilm(DefaultTableModel model) throws SQLException {
        HoaDonDAO dao = new HoaDonDAO();
        dao.getRevenueByFilmWithId().forEach((filmName, values) -> {
            model.addRow(new Object[]{
                values[0],
                values[1],
                String.format("%,d", ((Number)values[2]).longValue()),
                String.format("%,d", ((BigDecimal)values[3]).longValue()) + " VNĐ"
            });
        });
    }

    private void loadRevenueByScreening(DefaultTableModel model) throws SQLException {
        HoaDonDAO dao = new HoaDonDAO();
        dao.getRevenueByScreening().forEach((key, values) -> {
            model.addRow(new Object[]{
                key[0],
                key[1],
                key[2],
                key[3],
                String.format("%,d", values[1].intValue()),
                String.format("%,d", values[0].longValue()) + " VNĐ"
            });
        });
    }

    private void loadRevenueByEmployee(DefaultTableModel model) throws SQLException {
        HoaDonDAO dao = new HoaDonDAO();
        dao.getRevenueByEmployeeWithId().forEach((name, values) -> {
            model.addRow(new Object[]{
                values[0],
                values[1],
                String.format("%,d", ((Number)values[2]).longValue()),
                String.format("%,d", ((BigDecimal)values[3]).longValue()) + " VNĐ"
            });
        });
    }

    private void loadTopFilmsData(DefaultTableModel model) throws SQLException {
        HoaDonDAO dao = new HoaDonDAO();
        var topFilms = dao.getTopSellingFilms(5);
        int rank = 1;
        for (var film : topFilms) {
            BigDecimal revenue = (BigDecimal) film[2];
            model.addRow(new Object[]{
                rank++,
                film[0],
                String.format("%,d", ((Number)film[1]).longValue()),
                String.format("%,d", revenue.longValue()) + " VNĐ"
            });
        }
    }

    private void loadTicketDaily(DefaultTableModel model) throws SQLException {
        HoaDonDAO dao = new HoaDonDAO();
        dao.getTicketSalesByDay().forEach((date, values) -> {
            long ticketCount = (long) values[0];
            BigDecimal revenue = (BigDecimal) values[1];
            model.addRow(new Object[]{
                date,
                String.format("%,d", ticketCount),
                String.format("%,d", revenue.longValue()) + " VNĐ"
            });
        });
    }

    private void loadCustomerStats(DefaultTableModel model) throws SQLException {
        HoaDonDAO dao = new HoaDonDAO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dao.getTopCustomers().forEach(customer -> {
            String maKH = (String) customer[0];
            String tenKH = (String) customer[1];
            int soMua = (Integer) customer[2];
            BigDecimal tongChi = (BigDecimal) customer[3];
            String lanMua = (String) customer[4];
            
            model.addRow(new Object[]{
                maKH,
                tenKH,
                String.format("%,d", soMua),
                String.format("%,d", tongChi.longValue()) + " VNĐ",
                lanMua
            });
        });
    }

    private void loadPromotionStats(DefaultTableModel model) throws SQLException {
        HoaDonDAO dao = new HoaDonDAO();
        dao.getTopPromotions().forEach(promo -> {
            String maKM = (String) promo[0];
            String tenKM = (String) promo[1];
            int soLan = (Integer) promo[2];
            BigDecimal tongGiam = (BigDecimal) promo[3];
            BigDecimal tiLeGiam = (BigDecimal) promo[4];
            
            model.addRow(new Object[]{
                maKM,
                tenKM,
                String.format("%,d", soLan),
                String.format("%,d", tongGiam.longValue()) + " VNĐ",
                tiLeGiam.toPlainString() + "%"
            });
        });
    }
}
