package iuh.fit.se.nhom10.view.admin;

import iuh.fit.se.nhom10.dao.GheNgoiDAO;
import iuh.fit.se.nhom10.dao.LoaiPhongDAO;
import iuh.fit.se.nhom10.dao.PhongChieuDAO;
import iuh.fit.se.nhom10.model.GheNgoi;
import iuh.fit.se.nhom10.model.LoaiPhong;
import iuh.fit.se.nhom10.model.PhongChieu;
import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.util.ColorPalette;
import iuh.fit.se.nhom10.util.ButtonStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Professional Cinema Room and Seat Management Panel
 * Complete CRUD for room types, rooms, and seats with enhanced UI
 */
public class FrmQuanLyPhongChieuPanel extends JPanel {
    private TaiKhoanNhanVien adminHienTai;
    private JTabbedPane tabbedPane;
    
    // DAO
    private LoaiPhongDAO loaiPhongDAO;
    private PhongChieuDAO phongChieuDAO;
    private GheNgoiDAO gheNgoiDAO;
    
    // Tab 1: Loại phòng
    private JTable tblLoaiPhong;
    private DefaultTableModel modelLoaiPhong;
    private JTextField txtSearchLoaiPhong;
    private List<LoaiPhong> dsLoaiPhong;
    
    // Tab 2: Phòng Chiếu
    private JPanel pnlRoomCards;
    private JTextField txtSearchPhong;
    private List<PhongChieu> dsPhong;
    
    // Tab 3: Ghế Ngồi
    private JPanel pnlSeatMatrix;
    private String selectedRoom;
    private String selectedRoomName;
    private JLabel lblRoomInfo;
    private List<GheNgoi> dsGhe;

    public FrmQuanLyPhongChieuPanel(TaiKhoanNhanVien admin) {
        this.adminHienTai = admin;
        this.loaiPhongDAO = new LoaiPhongDAO();
        this.phongChieuDAO = new PhongChieuDAO();
        this.gheNgoiDAO = new GheNgoiDAO();
        
        this.dsLoaiPhong = new ArrayList<>();
        this.dsPhong = new ArrayList<>();
        this.dsGhe = new ArrayList<>();
        
        setupUI();
        loadData();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(ColorPalette.BACKGROUND_CONTENT);
        setBorder(new EmptyBorder(15, 15, 15, 15));

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
        pnlTitle.setPreferredSize(new Dimension(500, 50));

        JLabel lblTitle = new JLabel("Quản Lý Phòng Chiếu & Ghế Ngồi");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE - 2, Font.BOLD));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(10, 15, 10, 15));
        pnlTitle.add(lblTitle);

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        tabbedPane.setBackground(ColorPalette.BACKGROUND_CONTENT);
        tabbedPane.setForeground(ColorPalette.PRIMARY);
        
        tabbedPane.addTab("Quản Lý Loại Phòng", createLoaiPhongTab());
        tabbedPane.addTab("Quản Lý Phòng Chiếu", createPhongChieuTab());
        tabbedPane.addTab("Quản Lý Ghế Ngồi", createGheNgoiTab());

        add(pnlTitle, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    // ==================== TAB 1: LOẠI PHÒNG ====================
    private JPanel createLoaiPhongTab() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Search panel
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlSearch.setBackground(ColorPalette.BACKGROUND_CONTENT);
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        txtSearchLoaiPhong = new JTextField(20);
        txtSearchLoaiPhong.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtSearchLoaiPhong.setBorder(new LineBorder(Color.GRAY));
        txtSearchLoaiPhong.setPreferredSize(new Dimension(250, 35));
        
        txtSearchLoaiPhong.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterLoaiPhong(); }
            public void removeUpdate(DocumentEvent e) { filterLoaiPhong(); }
            public void changedUpdate(DocumentEvent e) { filterLoaiPhong(); }
        });
        
        pnlSearch.add(lblSearch);
        pnlSearch.add(txtSearchLoaiPhong);

        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        pnlButton.setBackground(ColorPalette.BACKGROUND_CONTENT);
        
        // Thêm Loại button (Green)
        JButton btnAdd = new JButton("Thêm Loại");
        btnAdd.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setBackground(new Color(76, 175, 80)); // Green color
        btnAdd.setOpaque(true);
        btnAdd.setBorderPainted(false);
        btnAdd.setFocusPainted(false);
        btnAdd.setPreferredSize(new Dimension(130, 40));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> addLoaiPhong());
        
        // Sửa button (Blue/Primary)
        JButton btnEdit = new JButton("Sửa");
        btnEdit.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setBackground(ColorPalette.PRIMARY); // Blue/Teal
        btnEdit.setOpaque(true);
        btnEdit.setBorderPainted(false);
        btnEdit.setFocusPainted(false);
        btnEdit.setPreferredSize(new Dimension(130, 40));
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEdit.addActionListener(e -> editLoaiPhong());
        
        // Xóa button (Red)
        JButton btnDelete = new JButton("Xóa");
        btnDelete.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setBackground(new Color(244, 67, 54)); // Red color
        btnDelete.setOpaque(true);
        btnDelete.setBorderPainted(false);
        btnDelete.setFocusPainted(false);
        btnDelete.setPreferredSize(new Dimension(130, 40));
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.addActionListener(e -> deleteLoaiPhong());
        
        pnlButton.add(btnAdd);
        pnlButton.add(btnEdit);
        pnlButton.add(btnDelete);

        JPanel pnlSearchButton = new JPanel(new BorderLayout());
        pnlSearchButton.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnlSearchButton.add(pnlSearch, BorderLayout.WEST);
        pnlSearchButton.add(pnlButton, BorderLayout.EAST);

        String[] columns = {"Mã Loại phòng", "Tên Loại phòng"};
        modelLoaiPhong = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblLoaiPhong = new JTable(modelLoaiPhong);
        tblLoaiPhong.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        tblLoaiPhong.setRowHeight(35);
        tblLoaiPhong.setSelectionBackground(new Color(200, 220, 240));
        tblLoaiPhong.setSelectionForeground(ColorPalette.PRIMARY);
        tblLoaiPhong.getColumnModel().getColumn(0).setPreferredWidth(120);
        tblLoaiPhong.getColumnModel().getColumn(1).setPreferredWidth(300);
        
        JTableHeader header = tblLoaiPhong.getTableHeader();
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

        JScrollPane scrollPane = new JScrollPane(tblLoaiPhong, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBackground(ColorPalette.BACKGROUND_CONTENT);
        scrollPane.getViewport().setBackground(ColorPalette.BACKGROUND_CONTENT);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));

        pnl.add(pnlSearchButton, BorderLayout.NORTH);
        pnl.add(scrollPane, BorderLayout.CENTER);

        return pnl;
    }

    // ==================== TAB 2: PHÒNG CHIẾU ====================
    private JPanel createPhongChieuTab() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Search panel
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlSearch.setBackground(ColorPalette.BACKGROUND_CONTENT);
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        txtSearchPhong = new JTextField(20);
        txtSearchPhong.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtSearchPhong.setBorder(new LineBorder(Color.GRAY));
        txtSearchPhong.setPreferredSize(new Dimension(250, 35));
        
        txtSearchPhong.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterPhong(); }
            public void removeUpdate(DocumentEvent e) { filterPhong(); }
            public void changedUpdate(DocumentEvent e) { filterPhong(); }
        });
        
        JButton btnAddPhong = ButtonStyle.createPrimaryButton("Thêm Phòng");
        btnAddPhong.setBackground(ColorPalette.BUTTON_SUCCESS_BG);
        btnAddPhong.addActionListener(e -> addPhong());
        
        pnlSearch.add(lblSearch);
        pnlSearch.add(txtSearchPhong);
        pnlSearch.add(Box.createHorizontalStrut(20));
        pnlSearch.add(btnAddPhong);

        // Room cards panel
        pnlRoomCards = new JPanel();
        pnlRoomCards.setLayout(new GridLayout(0, 4, 15, 15));
        pnlRoomCards.setBackground(ColorPalette.BACKGROUND_CONTENT);

        JScrollPane scrollPane = new JScrollPane(pnlRoomCards);
        scrollPane.setBackground(ColorPalette.BACKGROUND_CONTENT);

        pnl.add(pnlSearch, BorderLayout.NORTH);
        pnl.add(scrollPane, BorderLayout.CENTER);

        return pnl;
    }

    // ==================== TAB 3: GHẾ NGỒI ====================
    private JPanel createGheNgoiTab() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Info panel
        JPanel pnlInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlInfo.setBackground(ColorPalette.PRIMARY);
        lblRoomInfo = new JLabel("Chọn phòng chiếu để xem sơ đồ ghế");
        lblRoomInfo.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        lblRoomInfo.setForeground(Color.WHITE);
        pnlInfo.add(lblRoomInfo);

        // Seat matrix panel
        pnlSeatMatrix = new JPanel();
        pnlSeatMatrix.setLayout(new BoxLayout(pnlSeatMatrix, BoxLayout.Y_AXIS));
        pnlSeatMatrix.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnlSeatMatrix.setBorder(new EmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(pnlSeatMatrix);
        scrollPane.setBackground(ColorPalette.BACKGROUND_CONTENT);

        pnl.add(pnlInfo, BorderLayout.NORTH);
        pnl.add(scrollPane, BorderLayout.CENTER);

        return pnl;
    }

    // ==================== DATA LOADING ====================
    private void loadData() {
        loadLoaiPhong();
        loadPhong();
    }

    private void loadLoaiPhong() {
        dsLoaiPhong = loaiPhongDAO.getAllLoaiPhong();
        displayLoaiPhong(dsLoaiPhong);
    }

    private void displayLoaiPhong(List<LoaiPhong> list) {
        modelLoaiPhong.setRowCount(0);
        for (LoaiPhong lp : list) {
            modelLoaiPhong.addRow(new Object[]{lp.getMaLoaiPhong(), lp.getTenLoaiPhong()});
        }
    }

    private void filterLoaiPhong() {
        String keyword = txtSearchLoaiPhong.getText().toLowerCase();
        List<LoaiPhong> filtered = new ArrayList<>();
        for (LoaiPhong lp : dsLoaiPhong) {
            if (lp.getTenLoaiPhong().toLowerCase().contains(keyword)) {
                filtered.add(lp);
            }
        }
        displayLoaiPhong(filtered);
    }

    private void loadPhong() {
        dsPhong = phongChieuDAO.getAllPhongChieu();
        displayPhongCards(dsPhong);
    }

    private void displayPhongCards(List<PhongChieu> list) {
        pnlRoomCards.removeAll();
        for (PhongChieu phong : list) {
            JPanel cardPanel = createRoomCard(phong);
            pnlRoomCards.add(cardPanel);
        }
        pnlRoomCards.revalidate();
        pnlRoomCards.repaint();
    }

    private JPanel createRoomCard(PhongChieu phong) {
        JPanel card = new JPanel(new BorderLayout(0, 12)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.setColor(new Color(200, 200, 200));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(14, 14, 14, 14));
        card.setPreferredSize(new Dimension(200, 240));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Header with gradient background
        JPanel pnlHeader = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(ColorPalette.PRIMARY);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
            }
        };
        pnlHeader.setOpaque(false);
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
        pnlHeader.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnlHeader.setPreferredSize(new Dimension(200, 60));

        JLabel lblPhongName = new JLabel(phong.getTenPhong());
        lblPhongName.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 3, Font.BOLD));
        lblPhongName.setForeground(Color.WHITE);
        lblPhongName.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlHeader.add(lblPhongName);

        // Content panel with better spacing
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(6, 0, 6, 0));

        JLabel lblPhongCode = new JLabel("Mã: " + phong.getMaPhong().trim());
        lblPhongCode.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        lblPhongCode.setForeground(new Color(80, 80, 80));
        lblPhongCode.setAlignmentX(Component.LEFT_ALIGNMENT);

        LoaiPhong loaiPhong = loaiPhongDAO.getLoaiPhongByMa(phong.getMaLoaiPhong());
        String tenLoai = loaiPhong != null ? loaiPhong.getTenLoaiPhong() : "N/A";
        JLabel lblLoai = new JLabel("Loại: " + tenLoai);
        lblLoai.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        lblLoai.setForeground(new Color(80, 80, 80));
        lblLoai.setAlignmentX(Component.LEFT_ALIGNMENT);

        int soGhe = phongChieuDAO.getSoGheByPhong(phong.getMaPhong());
        JLabel lblSeats = new JLabel("Ghế: " + soGhe);
        lblSeats.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        lblSeats.setForeground(ColorPalette.PRIMARY);
        lblSeats.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(lblPhongCode);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(lblLoai);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(lblSeats);
        contentPanel.add(Box.createVerticalGlue());

        // Action buttons with better styling
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 8, 0));
        btnPanel.setOpaque(false);

        JButton btnEdit = new JButton("Sửa");
        btnEdit.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.BOLD));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setBackground(ColorPalette.PRIMARY);
        btnEdit.setOpaque(true);
        btnEdit.setBorderPainted(false);
        btnEdit.setFocusPainted(false);
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEdit.addActionListener(e -> editPhong(phong));

        JButton btnDelete = new JButton("Xóa");
        btnDelete.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.BOLD));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setBackground(new Color(220, 50, 50));
        btnDelete.setOpaque(true);
        btnDelete.setBorderPainted(false);
        btnDelete.setFocusPainted(false);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.addActionListener(e -> deletePhong(phong));

        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);

        card.add(pnlHeader, BorderLayout.NORTH);
        card.add(contentPanel, BorderLayout.CENTER);
        card.add(btnPanel, BorderLayout.SOUTH);

        // Click to view seats
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                selectedRoom = phong.getMaPhong();
                selectedRoomName = phong.getTenPhong();
                loadSeats(phong.getMaPhong(), phong.getTenPhong());
                tabbedPane.setSelectedIndex(2);
            }
        });

        return card;
    }

    private void filterPhong() {
        String keyword = txtSearchPhong.getText().toLowerCase();
        List<PhongChieu> filtered = new ArrayList<>();
        for (PhongChieu p : dsPhong) {
            if (p.getTenPhong().toLowerCase().contains(keyword) || 
                p.getMaPhong().toLowerCase().contains(keyword)) {
                filtered.add(p);
            }
        }
        displayPhongCards(filtered);
    }

    private void loadSeats(String maPhong, String tenPhong) {
        selectedRoom = maPhong;
        selectedRoomName = tenPhong;
        dsGhe = gheNgoiDAO.getGheByPhong(maPhong);
        lblRoomInfo.setText("Sơ đồ ghế - " + tenPhong + " (Mã: " + maPhong + ")");
        displaySeatMatrix();
    }

    private void displaySeatMatrix() {
        pnlSeatMatrix.removeAll();

        if (dsGhe.isEmpty()) {
            JLabel lblEmpty = new JLabel("Chưa có ghế trong phòng này - Nhấn nút bên dưới để thêm");
            lblEmpty.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
            pnlSeatMatrix.add(Box.createVerticalStrut(30));
            pnlSeatMatrix.add(lblEmpty);
            pnlSeatMatrix.add(Box.createVerticalStrut(30));
            
            // Add button to generate seats
            JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            addPanel.setOpaque(false);
            JButton btnGenerateSeats = ButtonStyle.createPrimaryButton("+ Thêm Ghế");
            btnGenerateSeats.setBackground(ColorPalette.BUTTON_SUCCESS_BG);
            btnGenerateSeats.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
            btnGenerateSeats.setForeground(Color.WHITE);
            btnGenerateSeats.setPreferredSize(new Dimension(150, 45));
            btnGenerateSeats.addActionListener(e -> showBulkSeatGenerationDialog());
            addPanel.add(btnGenerateSeats);
            pnlSeatMatrix.add(addPanel);
        } else {
            List<String> bookedSeats = gheNgoiDAO.getBookedSeatsInRoom(selectedRoom);
            
            java.util.Map<String, List<GheNgoi>> seatsByRow = new java.util.TreeMap<>();
            for (GheNgoi ghe : dsGhe) {
                seatsByRow.computeIfAbsent(ghe.getHang(), k -> new ArrayList<>()).add(ghe);
            }

            // Display screen label
            JLabel lblScreen = new JLabel("MÀN HÌNH");
            lblScreen.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 2, Font.BOLD));
            lblScreen.setForeground(ColorPalette.PRIMARY);
            lblScreen.setAlignmentX(Component.CENTER_ALIGNMENT);
            pnlSeatMatrix.add(lblScreen);
            pnlSeatMatrix.add(Box.createVerticalStrut(15));

            for (String hang : seatsByRow.keySet()) {
                List<GheNgoi> rowSeats = seatsByRow.get(hang);
                rowSeats.sort((a, b) -> Integer.compare(a.getCot(), b.getCot()));

                // Row container with BoxLayout for better centering
                JPanel seatRowPanel = new JPanel();
                seatRowPanel.setLayout(new BoxLayout(seatRowPanel, BoxLayout.X_AXIS));
                seatRowPanel.setOpaque(false);
                seatRowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

                // Row label
                JLabel lblRow = new JLabel(hang);
                lblRow.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 1, Font.BOLD));
                lblRow.setPreferredSize(new Dimension(40, 42));
                lblRow.setMaximumSize(new Dimension(40, 42));
                lblRow.setHorizontalAlignment(SwingConstants.CENTER);
                lblRow.setForeground(ColorPalette.PRIMARY);
                seatRowPanel.add(lblRow);
                
                seatRowPanel.add(Box.createHorizontalStrut(10));
                
                // Container for seats (centered)
                JPanel seatsContainer = new JPanel();
                seatsContainer.setLayout(new BoxLayout(seatsContainer, BoxLayout.X_AXIS));
                seatsContainer.setOpaque(false);
                seatsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
                
                for (GheNgoi ghe : rowSeats) {
                    JButton btnSeat = new JButton(String.valueOf(ghe.getCot()));
                    btnSeat.setPreferredSize(new Dimension(42, 42));
                    btnSeat.setMaximumSize(new Dimension(42, 42));
                    btnSeat.setMinimumSize(new Dimension(42, 42));
                    btnSeat.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.BOLD));
                    
                    if (bookedSeats.contains(ghe.getMaGhe())) {
                        btnSeat.setBackground(new Color(200, 200, 200)); // Gray for booked seats
                    } else {
                        btnSeat.setBackground(ColorPalette.SEAT_EMPTY);  // Green for available seats
                    }
                    
                    btnSeat.setForeground(Color.BLACK);
                    btnSeat.setOpaque(true);
                    btnSeat.setBorderPainted(false);
                    btnSeat.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    btnSeat.setFocusPainted(false);
                    
                    btnSeat.addActionListener(e -> showSeatDialog(ghe));
                    seatsContainer.add(btnSeat);
                    seatsContainer.add(Box.createHorizontalStrut(6)); // spacing between seats
                }
                
                // Add stretch space after seats for centering effect
                seatsContainer.add(Box.createHorizontalGlue());
                
                seatRowPanel.add(seatsContainer);
                seatRowPanel.add(Box.createHorizontalGlue()); // stretch remaining space
                seatRowPanel.setPreferredSize(new Dimension(800, 64));
                seatRowPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 54));
                
                pnlSeatMatrix.add(seatRowPanel);
            }

            // Add buttons for manage seats
            pnlSeatMatrix.add(Box.createVerticalStrut(20));
            JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
            actionPanel.setOpaque(false);
            
            JButton btnAddSingleSeat = ButtonStyle.createPrimaryButton("+ Thêm Ghế");
            btnAddSingleSeat.setBackground(ColorPalette.BUTTON_SUCCESS_BG);
            btnAddSingleSeat.addActionListener(e -> addSeat());
            
            JButton btnGenerateNewSet = ButtonStyle.createPrimaryButton("Tạo Ma Trận");
            btnGenerateNewSet.setBackground(ColorPalette.PRIMARY);
            btnGenerateNewSet.addActionListener(e -> showBulkSeatGenerationDialog());
            
            JButton btnClearAll = ButtonStyle.createDangerButton("Xóa Tất Cả");
            btnClearAll.addActionListener(e -> clearAllSeats());
            
            actionPanel.add(btnAddSingleSeat);
            actionPanel.add(btnGenerateNewSet);
            actionPanel.add(btnClearAll);
            pnlSeatMatrix.add(actionPanel);
        }

        pnlSeatMatrix.add(Box.createVerticalGlue());
        pnlSeatMatrix.revalidate();
        pnlSeatMatrix.repaint();
    }

    private void showBulkSeatGenerationDialog() {
        if (selectedRoom == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng trước!");
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Tạo Ma Trận Ghế", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Title
        JLabel lblTitle = new JLabel("Tạo Ma Trận Ghế Tự Động");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL + 3, Font.BOLD));
        lblTitle.setForeground(ColorPalette.PRIMARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Description
        JLabel lblDescription = new JLabel("<html>Nhập số hàng và cột để tạo ma trận ghế.<br>Ví dụ: 5 hàng (A-E), 10 cột (1-10) = 50 ghế</html>");
        lblDescription.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.PLAIN));
        lblDescription.setForeground(new Color(100, 100, 100));
        lblDescription.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Number of rows
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        rowPanel.setOpaque(false);
        JLabel lblRows = new JLabel("Số hàng (A-Z):");
        lblRows.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        lblRows.setPreferredSize(new Dimension(120, 25));
        JSpinner spnRows = new JSpinner(new SpinnerNumberModel(5, 1, 26, 1));
        spnRows.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        spnRows.setPreferredSize(new Dimension(100, 40));
        rowPanel.add(lblRows);
        rowPanel.add(spnRows);
        rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Number of columns
        JPanel colPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        colPanel.setOpaque(false);
        JLabel lblCols = new JLabel("Số cột (1-100):");
        lblCols.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        lblCols.setPreferredSize(new Dimension(120, 25));
        JSpinner spnCols = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        spnCols.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        spnCols.setPreferredSize(new Dimension(100, 40));
        colPanel.add(lblCols);
        colPanel.add(spnCols);
        colPanel.setAlignmentX(Component.LEFT_ALIGNMENT);


        // Info label for seat count
        JLabel lblInfo = new JLabel("Sẽ tạo: 50 ghế");
        lblInfo.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SMALL, Font.ITALIC));
        lblInfo.setForeground(new Color(80, 150, 80));
        lblInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Update info when values change
        spnRows.addChangeListener(e -> {
            int rows = (Integer) spnRows.getValue();
            int cols = (Integer) spnCols.getValue();
            lblInfo.setText("Sẽ tạo: " + (rows * cols) + " ghế");
        });
        spnCols.addChangeListener(e -> {
            int rows = (Integer) spnRows.getValue();
            int cols = (Integer) spnCols.getValue();
            lblInfo.setText("Sẽ tạo: " + (rows * cols) + " ghế");
        });

        // Button panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnPanel.setOpaque(false);
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton btnGenerate = ButtonStyle.createPrimaryButton("Tạo Ma Trận");
        btnGenerate.setBackground(ColorPalette.BUTTON_SUCCESS_BG);
        btnGenerate.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        btnGenerate.setForeground(Color.WHITE);
        btnGenerate.setPreferredSize(new Dimension(140, 45));
        btnGenerate.addActionListener(e -> {
            int numRows = (Integer) spnRows.getValue();
            int numCols = (Integer) spnCols.getValue();
            
            if (generateSeatMatrix(numRows, numCols)) {
                JOptionPane.showMessageDialog(dialog, "Tạo ma trận ghế thành công!");
                loadSeats(selectedRoom, selectedRoomName);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Lỗi tạo ma trận ghế!");
            }
        });

        JButton btnCancel = ButtonStyle.createSecondaryButton("Hủy");
        btnCancel.setPreferredSize(new Dimension(140, 45));
        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnGenerate);
        btnPanel.add(btnCancel);

        // Add components to panel
        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblDescription);
        panel.add(Box.createVerticalStrut(15));
        panel.add(rowPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(colPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblInfo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnPanel);
        panel.add(Box.createVerticalGlue());

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private boolean generateSeatMatrix(int numRows, int numCols) {
        try {
            for (int i = 0; i < numRows; i++) {
                String hang = String.valueOf((char) ('A' + i)); // A, B, C, ...
                for (int j = 1; j <= numCols; j++) {
                    String maGhe = selectedRoom.trim() + "_" + hang + j;
                    GheNgoi ghe = new GheNgoi(maGhe, hang, j, selectedRoom);
                    if (!gheNgoiDAO.createGheNgoi(ghe)) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi tạo ma trận ghế: " + e.getMessage());
            return false;
        }
    }

    private void clearAllSeats() {
        if (selectedRoom == null) return;
        
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Bạn có chắc chắn muốn xóa tất cả ghế trong phòng này?\nHành động này không thể hoàn tác!",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Iterate through a copy of dsGhe to avoid ConcurrentModificationException
            List<GheNgoi> seatsToRemove = new ArrayList<>(dsGhe);
            for (GheNgoi ghe : seatsToRemove) {
                gheNgoiDAO.deleteGheNgoi(ghe.getMaGhe());
            }
            loadSeats(selectedRoom, selectedRoomName);
            JOptionPane.showMessageDialog(this, "Xóa tất cả ghế thành công!");
        }
    }

    // ==================== DIALOG METHODS ====================
    private void addLoaiPhong() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Loại Phòng Mới", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        JLabel lblName = new JLabel("Tên Loại Phòng:");
        lblName.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        JTextField txtName = new JTextField();
        txtName.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtName.setPreferredSize(new Dimension(300, 40));
        txtName.setBorder(new LineBorder(Color.GRAY));
        txtName.setMaximumSize(new Dimension(350, 40));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Color.WHITE);
        
        JButton btnSave = ButtonStyle.createPrimaryButton("Lưu");
        btnSave.setBackground(ColorPalette.BUTTON_SUCCESS_BG);
        btnSave.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> {
            if (!txtName.getText().trim().isEmpty()) {
                LoaiPhong lp = new LoaiPhong(txtName.getText().trim());
                if (loaiPhongDAO.createLoaiPhong(lp)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm loại phòng thành công!");
                    loadLoaiPhong();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi thêm loại phòng!");
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập tên loại phòng!");
            }
        });

        JButton btnCancel = ButtonStyle.createSecondaryButton("Hủy");
        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        panel.add(lblName);
        panel.add(Box.createVerticalStrut(10));
        panel.add(txtName);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnPanel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void editLoaiPhong() {
        int row = tblLoaiPhong.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng cần sửa!");
            return;
        }

        Object cellValue = modelLoaiPhong.getValueAt(row, 0);
        int maLoaiPhong;
        if (cellValue instanceof Integer) {
            maLoaiPhong = (Integer) cellValue;
        } else {
            maLoaiPhong = Integer.parseInt(cellValue.toString().trim());
        }
        
        LoaiPhong lp = loaiPhongDAO.getLoaiPhongByMa(maLoaiPhong);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa Loại Phòng", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        JLabel lblName = new JLabel("Tên Loại Phòng:");
        lblName.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        JTextField txtName = new JTextField(lp.getTenLoaiPhong());
        txtName.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtName.setPreferredSize(new Dimension(300, 40));
        txtName.setBorder(new LineBorder(Color.GRAY));
        txtName.setMaximumSize(new Dimension(350, 40));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Color.WHITE);
        
        JButton btnSave = ButtonStyle.createPrimaryButton("Lưu");
        btnSave.setBackground(ColorPalette.BUTTON_SUCCESS_BG);
        btnSave.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> {
            if (!txtName.getText().trim().isEmpty()) {
                lp.setTenLoaiPhong(txtName.getText().trim());
                if (loaiPhongDAO.updateLoaiPhong(lp)) {
                    JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
                    loadLoaiPhong();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi cập nhật!");
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập tên loại phòng!");
            }
        });

        JButton btnCancel = ButtonStyle.createSecondaryButton("Hủy");
        btnCancel.setBackground(ColorPalette.BUTTON_SECONDARY_BG);
        btnCancel.setForeground(ColorPalette.BUTTON_SECONDARY_TEXT);
        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        panel.add(lblName);
        panel.add(Box.createVerticalStrut(10));
        panel.add(txtName);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnPanel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void deleteLoaiPhong() {
        int row = tblLoaiPhong.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng cần xóa!");
            return;
        }

        Object cellValue = modelLoaiPhong.getValueAt(row, 0);
        int maLoaiPhong;
        if (cellValue instanceof Integer) {
            maLoaiPhong = (Integer) cellValue;
        } else {
            maLoaiPhong = Integer.parseInt(cellValue.toString().trim());
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (loaiPhongDAO.deleteLoaiPhong(maLoaiPhong)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadLoaiPhong();
                loadPhong(); // Reload rooms as well, as they might be linked to this type
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi xóa!");
            }
        }
    }

    private void addPhong() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Phòng Chiếu Mới", true);
        dialog.setSize(450, 300);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        JLabel lblMa = new JLabel("Mã phòng:");
        lblMa.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        JTextField txtMa = new JTextField();
        txtMa.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtMa.setPreferredSize(new Dimension(350, 40));
        txtMa.setBorder(new LineBorder(Color.GRAY));
        txtMa.setMaximumSize(new Dimension(350, 40));

        JLabel lblTen = new JLabel("Tên Phòng:");
        lblTen.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        JTextField txtTen = new JTextField();
        txtTen.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtTen.setPreferredSize(new Dimension(350, 40));
        txtTen.setBorder(new LineBorder(Color.GRAY));
        txtTen.setMaximumSize(new Dimension(350, 40));

        JLabel lblLoai = new JLabel("Loại Phòng:");
        lblLoai.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        JComboBox<LoaiPhong> cmbLoai = new JComboBox<>(dsLoaiPhong.toArray(new LoaiPhong[0]));
        cmbLoai.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        cmbLoai.setPreferredSize(new Dimension(350, 40));
        cmbLoai.setMaximumSize(new Dimension(350, 40));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Color.WHITE);
        
        JButton btnSave = ButtonStyle.createPrimaryButton("Lưu");
        btnSave.setBackground(ColorPalette.BUTTON_SUCCESS_BG);
        btnSave.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> {
            String maphong = txtMa.getText().trim();
            String tenphong = txtTen.getText().trim();
            
            if (maphong.isEmpty() || tenphong.isEmpty() || cmbLoai.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (phongChieuDAO.isPhongChieuExists(maphong)) {
                JOptionPane.showMessageDialog(dialog, "Mã phòng '" + maphong + "' đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PhongChieu phong = new PhongChieu();
            phong.setMaPhong(maphong);
            phong.setTenPhong(tenphong);
            phong.setMaLoaiPhong(((LoaiPhong) cmbLoai.getSelectedItem()).getMaLoaiPhong());
            
            if (phongChieuDAO.createPhongChieu(phong)) {
                JOptionPane.showMessageDialog(dialog, "Thêm phòng thành công!");
                loadPhong();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Lỗi thêm phòng!");
            }
        });

        JButton btnCancel = ButtonStyle.createSecondaryButton("Hủy");
        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        panel.add(lblMa);
        panel.add(Box.createVerticalStrut(5));
        panel.add(txtMa);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblTen);
        panel.add(Box.createVerticalStrut(5));
        panel.add(txtTen);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblLoai);
        panel.add(Box.createVerticalStrut(5));
        panel.add(cmbLoai);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnPanel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void editPhong(PhongChieu phong) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa Phòng Chiếu", true);
        dialog.setSize(450, 300);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        JLabel lblMa = new JLabel("Mã phòng:");
        lblMa.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        JTextField txtMa = new JTextField(phong.getMaPhong());
        txtMa.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtMa.setPreferredSize(new Dimension(350, 40));
        txtMa.setBorder(new LineBorder(Color.GRAY));
        txtMa.setMaximumSize(new Dimension(350, 40));
        txtMa.setEditable(false);

        JLabel lblTen = new JLabel("Tên Phòng:");
        lblTen.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        JTextField txtTen = new JTextField(phong.getTenPhong());
        txtTen.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtTen.setPreferredSize(new Dimension(350, 40));
        txtTen.setBorder(new LineBorder(Color.GRAY));
        txtTen.setMaximumSize(new Dimension(350, 40));

        JLabel lblLoai = new JLabel("Loại Phòng:");
        lblLoai.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        JComboBox<LoaiPhong> cmbLoai = new JComboBox<>(dsLoaiPhong.toArray(new LoaiPhong[0]));
        for (int i = 0; i < dsLoaiPhong.size(); i++) {
            if (dsLoaiPhong.get(i).getMaLoaiPhong() == phong.getMaLoaiPhong()) {
                cmbLoai.setSelectedIndex(i);
                break;
            }
        }
        cmbLoai.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        cmbLoai.setPreferredSize(new Dimension(350, 40));
        cmbLoai.setMaximumSize(new Dimension(350, 40));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Color.WHITE);
        
        JButton btnSave = ButtonStyle.createPrimaryButton("Lưu");
        btnSave.setBackground(ColorPalette.BUTTON_SUCCESS_BG);
        btnSave.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> {
            if (!txtTen.getText().trim().isEmpty() && cmbLoai.getSelectedItem() != null) {
                phong.setTenPhong(txtTen.getText().trim());
                phong.setMaLoaiPhong(((LoaiPhong) cmbLoai.getSelectedItem()).getMaLoaiPhong());
                
                if (phongChieuDAO.updatePhongChieu(phong)) {
                    JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
                    loadPhong();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi cập nhật!");
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!");
            }
        });

        JButton btnCancel = ButtonStyle.createSecondaryButton("Hủy");
        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        panel.add(lblMa);
        panel.add(Box.createVerticalStrut(5));
        panel.add(txtMa);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblTen);
        panel.add(Box.createVerticalStrut(5));
        panel.add(txtTen);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblLoai);
        panel.add(Box.createVerticalStrut(5));
        panel.add(cmbLoai);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnPanel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void deletePhong(PhongChieu phong) {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa phòng này?\nCác ghế liên quan sẽ bị xóa!", "Xác nhận", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (phongChieuDAO.deletePhongChieu(phong.getMaPhong())) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadPhong();
                // Clear the seat matrix if the deleted room was the one being viewed
                if (selectedRoom != null && selectedRoom.equals(phong.getMaPhong())) {
                    pnlSeatMatrix.removeAll();
                    lblRoomInfo.setText("Chọn phòng chiếu để xem sơ đồ ghế");
                    pnlSeatMatrix.revalidate();
                    pnlSeatMatrix.repaint();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi xóa!");
            }
        }
    }

    private void addSeat() {
        if (selectedRoom == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng trước!");
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Ghế Mới", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        JLabel lblHang = new JLabel("Hàng:");
        lblHang.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        JComboBox<String> cmbHang = new JComboBox<>();
        for (char c = 'A'; c <= 'Z'; c++) {
            cmbHang.addItem(String.valueOf(c));
        }
        cmbHang.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        cmbHang.setPreferredSize(new Dimension(300, 40));
        cmbHang.setMaximumSize(new Dimension(300, 40));

        JLabel lblCot = new JLabel("Cột:");
        lblCot.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        JSpinner spnCot = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        spnCot.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        spnCot.setPreferredSize(new Dimension(300, 40));
        spnCot.setMaximumSize(new Dimension(300, 40));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Color.WHITE);
        
        JButton btnSave = ButtonStyle.createPrimaryButton("Lưu");
        btnSave.setBackground(ColorPalette.BUTTON_SUCCESS_BG);
        btnSave.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> {
            String hang = (String) cmbHang.getSelectedItem();
            int cot = (Integer) spnCot.getValue();
            
            String maPhongTrimmed = selectedRoom.trim();
            String maGhe = maPhongTrimmed + "_" + hang + cot;
            
            System.out.println("[v0] addSeat() - Creating seat: maGhe=" + maGhe + ", hang=" + hang + ", cot=" + cot + ", maPhong=" + maPhongTrimmed);

            GheNgoi ghe = new GheNgoi(maGhe, hang, cot, maPhongTrimmed);
            
            System.out.println("[v0] addSeat() - GheNgoi object created: " + ghe.toString());
            System.out.println("[v0] addSeat() - Calling gheNgoiDAO.createGheNgoi()");
            
            if (gheNgoiDAO.createGheNgoi(ghe)) {
                System.out.println("[v0] addSeat() - SUCCESS: Seat created successfully");
                JOptionPane.showMessageDialog(dialog, "Thêm ghế thành công!");
                loadSeats(selectedRoom, selectedRoomName);
                dialog.dispose();
            } else {
                System.out.println("[v0] addSeat() - FAILED: createGheNgoi returned false");
                JOptionPane.showMessageDialog(dialog, "Lỗi thêm ghế! Vui lòng kiểm tra thông tin.");
            }
        });

        JButton btnCancel = ButtonStyle.createSecondaryButton("Hủy");
        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        panel.add(lblHang);
        panel.add(Box.createVerticalStrut(5));
        panel.add(cmbHang);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblCot);
        panel.add(Box.createVerticalStrut(5));
        panel.add(spnCot);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnPanel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showSeatDialog(GheNgoi ghe) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi Tiết Ghế - " + ghe.getMaGhe(), true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        JLabel lblInfo = new JLabel("Mã Ghế: " + ghe.getMaGhe());
        lblInfo.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));

        JLabel lblHang = new JLabel("Hàng: " + ghe.getHang());
        lblHang.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));

        JLabel lblCot = new JLabel("Cột: " + ghe.getCot());
        lblCot.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Color.WHITE);

        JButton btnDelete = ButtonStyle.createDangerButton("Xóa");
        btnDelete.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dialog, "Xóa ghế này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (gheNgoiDAO.deleteGheNgoi(ghe.getMaGhe())) {
                    JOptionPane.showMessageDialog(dialog, "Xóa thành công!");
                    loadSeats(selectedRoom, selectedRoomName);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi xóa!");
                }
            }
        });

        JButton btnClose = ButtonStyle.createSecondaryButton("Đóng");
        btnClose.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnDelete);
        btnPanel.add(btnClose);

        panel.add(lblInfo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblHang);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblCot);
        panel.add(Box.createVerticalGlue());
        panel.add(btnPanel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

}
