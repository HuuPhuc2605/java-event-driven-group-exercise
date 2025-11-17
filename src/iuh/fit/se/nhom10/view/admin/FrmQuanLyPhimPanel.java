package iuh.fit.se.nhom10.view.admin;

import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.model.Phim;
import iuh.fit.se.nhom10.model.DaoDien;
import iuh.fit.se.nhom10.model.TheLoai;
import iuh.fit.se.nhom10.service.PhimService;
import iuh.fit.se.nhom10.dao.DaoDienDAO;
import iuh.fit.se.nhom10.dao.TheLoaiDAO;
import iuh.fit.se.nhom10.dao.PhimDAO;
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

/**
 * Professional Film Management Panel with Tabs for CRUD Operations
 * Manages: Phim (Films), Đạo Diễn (Directors), Thể Loại (Genres)
 */
public class FrmQuanLyPhimPanel extends JPanel {
    private TaiKhoanNhanVien adminHienTai;
    private PhimService phimService;
    private DaoDienDAO daoDienDAO;
    private TheLoaiDAO theLoaiDAO;
    private PhimDAO phimDAO;
    
    private JTabbedPane tabbedPane;
    
    // Phim Tab Components
    private JTable tblPhim;
    private DefaultTableModel modelPhim;
    private JTextField txtTimKiemPhim;
    
    // Đạo Diễn Tab Components
    private JTable tblDaoDien;
    private DefaultTableModel modelDaoDien;
    private JTextField txtTimKiemDaoDien;
    
    // Thể Loại Tab Components
    private JTable tblTheLoai;
    private DefaultTableModel modelTheLoai;
    private JTextField txtTimKiemTheLoai;

    public FrmQuanLyPhimPanel(TaiKhoanNhanVien admin) throws SQLException {
        this.adminHienTai = admin;
        this.phimService = new PhimService();
        this.daoDienDAO = new DaoDienDAO();
        this.theLoaiDAO = new TheLoaiDAO();
        this.phimDAO = new PhimDAO();
        setupUI();
        loadAllData();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(ColorPalette.BACKGROUND_MAIN);

        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        tabbedPane.setBackground(ColorPalette.BACKGROUND_MAIN);
        tabbedPane.setForeground(ColorPalette.TEXT_LABEL);
        
        tabbedPane.addTab("Quản Lý Phim", createPhimTab());
        tabbedPane.addTab("Quản Lý Đạo Diễn", createDaoDienTab());
        tabbedPane.addTab("Quản Lý Thể Loại", createTheLoaiTab());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // ==================== PHIM TAB ====================
    
    private JPanel createPhimTab() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel pnlSearchBar = createPhimSearchBar();
        pnl.add(pnlSearchBar, BorderLayout.NORTH);

        JPanel pnlTable = createPhimTablePanel();
        pnl.add(pnlTable, BorderLayout.CENTER);

        JPanel pnlButtons = createPhimButtonPanel();
        pnl.add(pnlButtons, BorderLayout.SOUTH);

        return pnl;
    }

    private JPanel createPhimSearchBar() {
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
        pnlTitle.setPreferredSize(new Dimension(300, 45));

        JLabel lblTitle = new JLabel("Quản Lý Phim");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE - 4, Font.BOLD));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(8, 15, 8, 15));
        pnlTitle.add(lblTitle);

        JPanel pnlSearchInput = new JPanel();
        pnlSearchInput.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pnlSearchInput.setBackground(ColorPalette.BACKGROUND_CONTENT);

        txtTimKiemPhim = new JTextField();
        txtTimKiemPhim.setPreferredSize(new Dimension(300, 38));
        txtTimKiemPhim.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtTimKiemPhim.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        txtTimKiemPhim.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { handleSearchPhim(); }
            @Override
            public void removeUpdate(DocumentEvent e) { handleSearchPhim(); }
            @Override
            public void changedUpdate(DocumentEvent e) { handleSearchPhim(); }
        });

        pnlSearchInput.add(txtTimKiemPhim);

        pnl.add(pnlTitle, BorderLayout.WEST);
        pnl.add(pnlSearchInput, BorderLayout.EAST);

        return pnl;
    }

    private JPanel createPhimTablePanel() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

        modelPhim = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelPhim.addColumn("Mã Phim");
        modelPhim.addColumn("Tên Phim");
        modelPhim.addColumn("Thể Loại");
        modelPhim.addColumn("Thời Lượng");
        modelPhim.addColumn("Đạo Diễn");

        tblPhim = new JTable(modelPhim);
        tblPhim.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        tblPhim.setRowHeight(35);
        tblPhim.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPhim.setGridColor(new Color(220, 225, 235));

        // Style header
        JTableHeader header = tblPhim.getTableHeader();
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

        tblPhim.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblPhim.getColumnModel().getColumn(1).setPreferredWidth(250);
        tblPhim.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblPhim.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblPhim.getColumnModel().getColumn(4).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(tblPhim);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        pnl.add(scrollPane, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel createPhimButtonPanel() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

        JButton btnThemPhim = new JButton("Thêm Phim");
        btnThemPhim.setPreferredSize(new Dimension(120, 40));
        btnThemPhim.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnThemPhim.setBackground(new Color(52, 211, 153));
        btnThemPhim.setForeground(Color.WHITE);
        btnThemPhim.setFocusPainted(false);
        btnThemPhim.setBorderPainted(false);
        btnThemPhim.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnThemPhim.addActionListener(e -> handleThemPhim());

        JButton btnSuaPhim = new JButton("Sửa");
        btnSuaPhim.setPreferredSize(new Dimension(120, 40));
        btnSuaPhim.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnSuaPhim.setBackground(ColorPalette.PRIMARY);
        btnSuaPhim.setForeground(Color.WHITE);
        btnSuaPhim.setFocusPainted(false);
        btnSuaPhim.setBorderPainted(false);
        btnSuaPhim.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSuaPhim.addActionListener(e -> handleSuaPhim());

        JButton btnXoaPhim = new JButton("Xóa");
        btnXoaPhim.setPreferredSize(new Dimension(120, 40));
        btnXoaPhim.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnXoaPhim.setBackground(new Color(239, 68, 68));
        btnXoaPhim.setForeground(Color.WHITE);
        btnXoaPhim.setFocusPainted(false);
        btnXoaPhim.setBorderPainted(false);
        btnXoaPhim.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXoaPhim.addActionListener(e -> handleXoaPhim());

        pnl.add(btnThemPhim);
        pnl.add(btnSuaPhim);
        pnl.add(btnXoaPhim);

        return pnl;
    }

    // ==================== ĐẠO DIỄN TAB ====================
    
    private JPanel createDaoDienTab() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel pnlSearchBar = createDaoDienSearchBar();
        pnl.add(pnlSearchBar, BorderLayout.NORTH);

        JPanel pnlTable = createDaoDienTablePanel();
        pnl.add(pnlTable, BorderLayout.CENTER);

        JPanel pnlButtons = createDaoDienButtonPanel();
        pnl.add(pnlButtons, BorderLayout.SOUTH);

        return pnl;
    }

    private JPanel createDaoDienSearchBar() {
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
        pnlTitle.setPreferredSize(new Dimension(300, 45));

        JLabel lblTitle = new JLabel("Quản Lý Đạo Diễn");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE - 4, Font.BOLD));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(8, 15, 8, 15));
        pnlTitle.add(lblTitle);

        JPanel pnlSearchInput = new JPanel();
        pnlSearchInput.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pnlSearchInput.setBackground(ColorPalette.BACKGROUND_CONTENT);

        txtTimKiemDaoDien = new JTextField();
        txtTimKiemDaoDien.setPreferredSize(new Dimension(300, 38));
        txtTimKiemDaoDien.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtTimKiemDaoDien.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        txtTimKiemDaoDien.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { handleSearchDaoDien(); }
            @Override
            public void removeUpdate(DocumentEvent e) { handleSearchDaoDien(); }
            @Override
            public void changedUpdate(DocumentEvent e) { handleSearchDaoDien(); }
        });

        pnlSearchInput.add(txtTimKiemDaoDien);

        pnl.add(pnlTitle, BorderLayout.WEST);
        pnl.add(pnlSearchInput, BorderLayout.EAST);

        return pnl;
    }

    private JPanel createDaoDienTablePanel() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

        modelDaoDien = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelDaoDien.addColumn("Mã Đạo Diễn");
        modelDaoDien.addColumn("Tên Đạo Diễn");
        modelDaoDien.addColumn("Quốc Tích");

        tblDaoDien = new JTable(modelDaoDien);
        tblDaoDien.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        tblDaoDien.setRowHeight(35);
        tblDaoDien.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblDaoDien.setGridColor(new Color(220, 225, 235));

        JTableHeader header = tblDaoDien.getTableHeader();
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

        tblDaoDien.getColumnModel().getColumn(0).setPreferredWidth(120);
        tblDaoDien.getColumnModel().getColumn(1).setPreferredWidth(300);
        tblDaoDien.getColumnModel().getColumn(2).setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(tblDaoDien);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        pnl.add(scrollPane, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel createDaoDienButtonPanel() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

        JButton btnThemDD = new JButton("Thêm Đạo Diễn");
        btnThemDD.setPreferredSize(new Dimension(140, 40));
        btnThemDD.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnThemDD.setBackground(new Color(52, 211, 153));
        btnThemDD.setForeground(Color.WHITE);
        btnThemDD.setFocusPainted(false);
        btnThemDD.setBorderPainted(false);
        btnThemDD.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnThemDD.addActionListener(e -> handleThemDaoDien());

        JButton btnSuaDD = new JButton("Sửa");
        btnSuaDD.setPreferredSize(new Dimension(120, 40));
        btnSuaDD.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnSuaDD.setBackground(ColorPalette.PRIMARY);
        btnSuaDD.setForeground(Color.WHITE);
        btnSuaDD.setFocusPainted(false);
        btnSuaDD.setBorderPainted(false);
        btnSuaDD.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSuaDD.addActionListener(e -> handleSuaDaoDien());

        JButton btnXoaDD = new JButton("Xóa");
        btnXoaDD.setPreferredSize(new Dimension(120, 40));
        btnXoaDD.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnXoaDD.setBackground(new Color(239, 68, 68));
        btnXoaDD.setForeground(Color.WHITE);
        btnXoaDD.setFocusPainted(false);
        btnXoaDD.setBorderPainted(false);
        btnXoaDD.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXoaDD.addActionListener(e -> handleXoaDaoDien());

        pnl.add(btnThemDD);
        pnl.add(btnSuaDD);
        pnl.add(btnXoaDD);

        return pnl;
    }

    // ==================== THỂ LOẠI TAB ====================
    
    private JPanel createTheLoaiTab() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel pnlSearchBar = createTheLoaiSearchBar();
        pnl.add(pnlSearchBar, BorderLayout.NORTH);

        JPanel pnlTable = createTheLoaiTablePanel();
        pnl.add(pnlTable, BorderLayout.CENTER);

        JPanel pnlButtons = createTheLoaiButtonPanel();
        pnl.add(pnlButtons, BorderLayout.SOUTH);

        return pnl;
    }

    private JPanel createTheLoaiSearchBar() {
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
        pnlTitle.setPreferredSize(new Dimension(300, 45));

        JLabel lblTitle = new JLabel("Quản Lý Thể Loại");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE - 4, Font.BOLD));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(8, 15, 8, 15));
        pnlTitle.add(lblTitle);

        JPanel pnlSearchInput = new JPanel();
        pnlSearchInput.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pnlSearchInput.setBackground(ColorPalette.BACKGROUND_CONTENT);

        txtTimKiemTheLoai = new JTextField();
        txtTimKiemTheLoai.setPreferredSize(new Dimension(300, 38));
        txtTimKiemTheLoai.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtTimKiemTheLoai.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        txtTimKiemTheLoai.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { handleSearchTheLoai(); }
            @Override
            public void removeUpdate(DocumentEvent e) { handleSearchTheLoai(); }
            @Override
            public void changedUpdate(DocumentEvent e) { handleSearchTheLoai(); }
        });

        pnlSearchInput.add(txtTimKiemTheLoai);

        pnl.add(pnlTitle, BorderLayout.WEST);
        pnl.add(pnlSearchInput, BorderLayout.EAST);

        return pnl;
    }

    private JPanel createTheLoaiTablePanel() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

        modelTheLoai = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelTheLoai.addColumn("Mã Thể Loại");
        modelTheLoai.addColumn("Tên Thể Loại");

        tblTheLoai = new JTable(modelTheLoai);
        tblTheLoai.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        tblTheLoai.setRowHeight(35);
        tblTheLoai.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblTheLoai.setGridColor(new Color(220, 225, 235));

        JTableHeader header = tblTheLoai.getTableHeader();
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

        tblTheLoai.getColumnModel().getColumn(0).setPreferredWidth(150);
        tblTheLoai.getColumnModel().getColumn(1).setPreferredWidth(400);

        JScrollPane scrollPane = new JScrollPane(tblTheLoai);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        pnl.add(scrollPane, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel createTheLoaiButtonPanel() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

        JButton btnThemTL = new JButton("Thêm Thể Loại");
        btnThemTL.setPreferredSize(new Dimension(140, 40));
        btnThemTL.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnThemTL.setBackground(new Color(52, 211, 153));
        btnThemTL.setForeground(Color.WHITE);
        btnThemTL.setFocusPainted(false);
        btnThemTL.setBorderPainted(false);
        btnThemTL.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnThemTL.addActionListener(e -> handleThemTheLoai());

        JButton btnSuaTL = new JButton("Sửa");
        btnSuaTL.setPreferredSize(new Dimension(120, 40));
        btnSuaTL.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnSuaTL.setBackground(ColorPalette.PRIMARY);
        btnSuaTL.setForeground(Color.WHITE);
        btnSuaTL.setFocusPainted(false);
        btnSuaTL.setBorderPainted(false);
        btnSuaTL.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSuaTL.addActionListener(e -> handleSuaTheLoai());

        JButton btnXoaTL = new JButton("Xóa");
        btnXoaTL.setPreferredSize(new Dimension(120, 40));
        btnXoaTL.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
        btnXoaTL.setBackground(new Color(239, 68, 68));
        btnXoaTL.setForeground(Color.WHITE);
        btnXoaTL.setFocusPainted(false);
        btnXoaTL.setBorderPainted(false);
        btnXoaTL.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXoaTL.addActionListener(e -> handleXoaTheLoai());

        pnl.add(btnThemTL);
        pnl.add(btnSuaTL);
        pnl.add(btnXoaTL);

        return pnl;
    }

    // ==================== LOAD DATA METHODS ====================
    
    private void loadAllData() {
        loadPhimData();
        loadDaoDienData();
        loadTheLoaiData();
    }

    private void loadPhimData() {
        modelPhim.setRowCount(0);
        try {
            List<Phim> phimList = phimService.getAllPhim();
            for (Phim phim : phimList) {
                TheLoai tl = theLoaiDAO.getTheLoaiByMa(phim.getMaTheLoai());
                DaoDien dd = daoDienDAO.getDaoDienByMa(phim.getMaDD());
                
                modelPhim.addRow(new Object[]{
                    phim.getMaPhim(),
                    phim.getTenPhim(),
                    tl != null ? tl.getTenTheLoai() : "N/A",
                    phim.getThoiLuong() + " phút",
                    dd != null ? dd.getTenDD() : "N/A"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu phim: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDaoDienData() {
        modelDaoDien.setRowCount(0);
        try {
            List<DaoDien> daoDienList = daoDienDAO.getAllDaoDien();
            for (DaoDien dd : daoDienList) {
                modelDaoDien.addRow(new Object[]{
                    dd.getMaDD(),
                    dd.getTenDD(),
                    dd.getQuocTich() != null ? dd.getQuocTich() : "N/A"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu đạo diễn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTheLoaiData() {
        modelTheLoai.setRowCount(0);
        try {
            List<TheLoai> theLoaiList = theLoaiDAO.getAllTheLoai();
            for (TheLoai tl : theLoaiList) {
                modelTheLoai.addRow(new Object[]{
                    tl.getMaTheLoai(),
                    tl.getTenTheLoai()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu thể loại: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==================== SEARCH HANDLERS ====================
    
    private void handleSearchPhim() {
        String keyword = txtTimKiemPhim.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadPhimData();
            return;
        }

        modelPhim.setRowCount(0);
        try {
            List<Phim> phimList = phimService.getAllPhim();
            for (Phim phim : phimList) {
                if (phim.getTenPhim().toLowerCase().contains(keyword) || 
                    phim.getMaPhim().toLowerCase().contains(keyword)) {
                    TheLoai tl = theLoaiDAO.getTheLoaiByMa(phim.getMaTheLoai());
                    DaoDien dd = daoDienDAO.getDaoDienByMa(phim.getMaDD());
                    
                    modelPhim.addRow(new Object[]{
                        phim.getMaPhim(),
                        phim.getTenPhim(),
                        tl != null ? tl.getTenTheLoai() : "N/A",
                        phim.getThoiLuong() + " phút",
                        dd != null ? dd.getTenDD() : "N/A"
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSearchDaoDien() {
        String keyword = txtTimKiemDaoDien.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadDaoDienData();
            return;
        }

        modelDaoDien.setRowCount(0);
        try {
            List<DaoDien> daoDienList = daoDienDAO.getAllDaoDien();
            for (DaoDien dd : daoDienList) {
                if (dd.getTenDD().toLowerCase().contains(keyword) || 
                    dd.getMaDD().toLowerCase().contains(keyword)) {
                    modelDaoDien.addRow(new Object[]{
                        dd.getMaDD(),
                        dd.getTenDD(),
                        dd.getQuocTich() != null ? dd.getQuocTich() : "N/A"
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSearchTheLoai() {
        String keyword = txtTimKiemTheLoai.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadTheLoaiData();
            return;
        }

        modelTheLoai.setRowCount(0);
        try {
            List<TheLoai> theLoaiList = theLoaiDAO.getAllTheLoai();
            for (TheLoai tl : theLoaiList) {
                if (tl.getTenTheLoai().toLowerCase().contains(keyword)) {
                    modelTheLoai.addRow(new Object[]{
                        tl.getMaTheLoai(),
                        tl.getTenTheLoai()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==================== PHIM HANDLERS ====================
    
    private void handleThemPhim() {
        try {
            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Thêm Phim Mới");
            dialog.setSize(600, 500);
            dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            
            JPanel pnl = createPhimForm(null, dialog, () -> loadPhimData());
            dialog.add(pnl);
            dialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSuaPhim() {
        int selectedRow = tblPhim.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phim để sửa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            String maPhim = (String) modelPhim.getValueAt(selectedRow, 0);
            Phim phim = phimService.getPhimByMa(maPhim);
            
            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Sửa Phim");
            dialog.setSize(600, 500);
            dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            
            JPanel pnl = createPhimForm(phim, dialog, () -> loadPhimData());
            dialog.add(pnl);
            dialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleXoaPhim() {
        int selectedRow = tblPhim.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phim để xóa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String maPhim = (String) modelPhim.getValueAt(selectedRow, 0);
        int result = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa phim này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            String deleteResult = phimService.deletePhim(maPhim, adminHienTai);
            if ("SUCCESS".equals(deleteResult)) {
                JOptionPane.showMessageDialog(this, "Xóa phim thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadPhimData();
            } else {
                JOptionPane.showMessageDialog(this, deleteResult, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ==================== ĐẠO DIỄN HANDLERS ====================
    
    private void handleThemDaoDien() {
        try {
            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Thêm Đạo Diễn Mới");
            dialog.setSize(500, 350);
            dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            
            JPanel pnl = createDaoDienForm(null, dialog, () -> loadDaoDienData());
            dialog.add(pnl);
            dialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSuaDaoDien() {
        int selectedRow = tblDaoDien.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đạo diễn để sửa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            String maDD = (String) modelDaoDien.getValueAt(selectedRow, 0);
            DaoDien dd = daoDienDAO.getDaoDienByMa(maDD);
            
            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Sửa Đạo Diễn");
            dialog.setSize(500, 350);
            dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            
            JPanel pnl = createDaoDienForm(dd, dialog, () -> loadDaoDienData());
            dialog.add(pnl);
            dialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleXoaDaoDien() {
        int selectedRow = tblDaoDien.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đạo diễn để xóa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String maDD = (String) modelDaoDien.getValueAt(selectedRow, 0);
        int result = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa đạo diễn này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            if (daoDienDAO.deleteDaoDien(maDD)) {
                JOptionPane.showMessageDialog(this, "Xóa đạo diễn thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadDaoDienData();
                loadPhimData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa đạo diễn thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ==================== THỂ LOẠI HANDLERS ====================
    
    private void handleThemTheLoai() {
        try {
            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Thêm Thể Loại Mới");
            dialog.setSize(450, 250);
            dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            
            JPanel pnl = createTheLoaiForm(null, dialog, () -> loadTheLoaiData());
            dialog.add(pnl);
            dialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSuaTheLoai() {
        int selectedRow = tblTheLoai.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thể loại để sửa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            int maTL = (int) modelTheLoai.getValueAt(selectedRow, 0);
            TheLoai tl = theLoaiDAO.getTheLoaiByMa(maTL);
            
            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Sửa Thể Loại");
            dialog.setSize(450, 250);
            dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            
            JPanel pnl = createTheLoaiForm(tl, dialog, () -> loadTheLoaiData());
            dialog.add(pnl);
            dialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleXoaTheLoai() {
        int selectedRow = tblTheLoai.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thể loại để xóa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int maTL = (int) modelTheLoai.getValueAt(selectedRow, 0);
        int result = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa thể loại này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            if (theLoaiDAO.deleteTheLoai(maTL)) {
                JOptionPane.showMessageDialog(this, "Xóa thể loại thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadTheLoaiData();
                loadPhimData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thể loại thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ==================== FORM METHODS ====================
    
    private JPanel createPhimForm(Phim phim, JDialog dialog, Runnable onSuccess) {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(25, 25, 25, 25));
        pnl.setBackground(Color.WHITE);

        JLabel lblMaPhim = new JLabel("Mã Phim:");
        lblMaPhim.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblMaPhim.setForeground(ColorPalette.TEXT_LABEL);
        JTextField txtMaPhim = new JTextField();
        txtMaPhim.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtMaPhim.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT + 1, Font.PLAIN));
        txtMaPhim.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtMaPhim.setEditable(phim == null);

        JLabel lblTenPhim = new JLabel("Tên Phim:");
        lblTenPhim.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblTenPhim.setForeground(ColorPalette.TEXT_LABEL);
        JTextField txtTenPhim = new JTextField();
        txtTenPhim.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtTenPhim.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT + 1, Font.PLAIN));
        txtTenPhim.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        JLabel lblTheLoai = new JLabel("Thể Loại:");
        lblTheLoai.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblTheLoai.setForeground(ColorPalette.TEXT_LABEL);
        JComboBox<String> cboTheLoai = new JComboBox<>();
        cboTheLoai.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cboTheLoai.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT + 1, Font.PLAIN));

        JLabel lblThoiLuong = new JLabel("Thời Lượng (phút):");
        lblThoiLuong.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblThoiLuong.setForeground(ColorPalette.TEXT_LABEL);
        JSpinner spinThoiLuong = new JSpinner(new SpinnerNumberModel(90, 1, 300, 1));
        spinThoiLuong.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        spinThoiLuong.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT + 1, Font.PLAIN));
        JSpinner.NumberEditor editorThoiLuong = new JSpinner.NumberEditor(spinThoiLuong, "###");
        spinThoiLuong.setEditor(editorThoiLuong);

        JLabel lblDaoDien = new JLabel("Đạo Diễn:");
        lblDaoDien.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblDaoDien.setForeground(ColorPalette.TEXT_LABEL);
        JComboBox<String> cboDaoDien = new JComboBox<>();
        cboDaoDien.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cboDaoDien.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT + 1, Font.PLAIN));

        try {
            List<TheLoai> theLoaiList = theLoaiDAO.getAllTheLoai();
            for (TheLoai tl : theLoaiList) {
                cboTheLoai.addItem(tl.getMaTheLoai() + " - " + tl.getTenTheLoai());
            }

            List<DaoDien> daoDienList = daoDienDAO.getAllDaoDien();
            for (DaoDien dd : daoDienList) {
                cboDaoDien.addItem(dd.getMaDD() + " - " + dd.getTenDD());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog, "Lỗi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        if (phim != null) {
            txtMaPhim.setText(phim.getMaPhim());
            txtTenPhim.setText(phim.getTenPhim());
            spinThoiLuong.setValue(phim.getThoiLuong());
            try {
                TheLoai tl = theLoaiDAO.getTheLoaiByMa(phim.getMaTheLoai());
                DaoDien dd = daoDienDAO.getDaoDienByMa(phim.getMaDD());
                cboTheLoai.setSelectedItem(phim.getMaTheLoai() + " - " + (tl != null ? tl.getTenTheLoai() : "N/A"));
                cboDaoDien.setSelectedItem(phim.getMaDD() + " - " + (dd != null ? dd.getTenDD() : "N/A"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        pnl.add(lblMaPhim);
        pnl.add(txtMaPhim);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblTenPhim);
        pnl.add(txtTenPhim);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblTheLoai);
        pnl.add(cboTheLoai);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblThoiLuong);
        pnl.add(spinThoiLuong);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblDaoDien);
        pnl.add(cboDaoDien);
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
                String maPhim = txtMaPhim.getText().trim();
                String tenPhim = txtTenPhim.getText().trim();
                String theLoaiStr = (String) cboTheLoai.getSelectedItem();
                int maTheLoai = Integer.parseInt(theLoaiStr.split(" - ")[0]);
                int thoiLuong = (Integer) spinThoiLuong.getValue();
                String daoDienStr = (String) cboDaoDien.getSelectedItem();
                String maDD = daoDienStr.split(" - ")[0];

                Phim newPhim = new Phim(maPhim, tenPhim, maTheLoai, thoiLuong, maDD);
                
                String result;
                if (phim == null) {
                    result = phimService.addPhim(newPhim, adminHienTai);
                } else {
                    result = phimService.updatePhim(newPhim, adminHienTai);
                }
                
                if ("SUCCESS".equals(result)) {
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this, "Lưu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    onSuccess.run();
                } else {
                    JOptionPane.showMessageDialog(this, result, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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

    private JPanel createDaoDienForm(DaoDien dd, JDialog dialog, Runnable onSuccess) {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(25, 25, 25, 25));
        pnl.setBackground(Color.WHITE);

        JLabel lblMaDD = new JLabel("Mã Đạo Diễn:");
        lblMaDD.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblMaDD.setForeground(ColorPalette.TEXT_LABEL);
        JTextField txtMaDD = new JTextField();
        txtMaDD.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtMaDD.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT + 1, Font.PLAIN));
        txtMaDD.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtMaDD.setEditable(dd == null);

        JLabel lblTenDD = new JLabel("Tên Đạo Diễn:");
        lblTenDD.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblTenDD.setForeground(ColorPalette.TEXT_LABEL);
        JTextField txtTenDD = new JTextField();
        txtTenDD.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtTenDD.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT + 1, Font.PLAIN));
        txtTenDD.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        JLabel lblQuocTich = new JLabel("Quốc Tịch:");
        lblQuocTich.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblQuocTich.setForeground(ColorPalette.TEXT_LABEL);
        JTextField txtQuocTich = new JTextField();
        txtQuocTich.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtQuocTich.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT + 1, Font.PLAIN));
        txtQuocTich.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        if (dd != null) {
            txtMaDD.setText(dd.getMaDD());
            txtTenDD.setText(dd.getTenDD());
            txtQuocTich.setText(dd.getQuocTich() != null ? dd.getQuocTich() : "");
        }

        pnl.add(lblMaDD);
        pnl.add(txtMaDD);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblTenDD);
        pnl.add(txtTenDD);
        pnl.add(Box.createVerticalStrut(12));
        pnl.add(lblQuocTich);
        pnl.add(txtQuocTich);
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
                String maDD = txtMaDD.getText().trim();
                String tenDD = txtTenDD.getText().trim();
                String quocTich = txtQuocTich.getText().trim();

                if (maDD.isEmpty() || tenDD.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Mã đạo diễn và tên đạo diễn không được để trống!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (dd == null && daoDienDAO.isDaoDienExists(maDD)) {
                    JOptionPane.showMessageDialog(dialog, "Mã đạo diễn '" + maDD + "' đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                DaoDien newDD = new DaoDien(maDD, tenDD, quocTich);
                
                if (dd == null) {
                    daoDienDAO.createDaoDien(newDD);
                } else {
                    daoDienDAO.updateDaoDien(newDD);
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

    private JPanel createTheLoaiForm(TheLoai tl, JDialog dialog, Runnable onSuccess) {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(25, 25, 25, 25));
        pnl.setBackground(Color.WHITE);

        JLabel lblTenTheLoai = new JLabel("Tên Thể Loại:");
        lblTenTheLoai.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
        lblTenTheLoai.setForeground(ColorPalette.TEXT_LABEL);
        JTextField txtTenTheLoai = new JTextField();
        txtTenTheLoai.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtTenTheLoai.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT + 1, Font.PLAIN));
        txtTenTheLoai.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        if (tl != null) {
            txtTenTheLoai.setText(tl.getTenTheLoai());
        }

        pnl.add(lblTenTheLoai);
        pnl.add(txtTenTheLoai);
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
                String tenTheLoai = txtTenTheLoai.getText().trim();
                
                if (tenTheLoai.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Tên thể loại không được để trống!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (tl == null) {
                    TheLoai newTL = new TheLoai();
                    newTL.setTenTheLoai(tenTheLoai);
                    theLoaiDAO.createTheLoai(newTL);
                } else {
                    TheLoai updateTL = new TheLoai();
                    updateTL.setMaTheLoai(tl.getMaTheLoai());
                    updateTL.setTenTheLoai(tenTheLoai);
                    theLoaiDAO.updateTheLoai(updateTL);
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
}
