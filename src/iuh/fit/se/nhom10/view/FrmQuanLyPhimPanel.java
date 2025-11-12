package iuh.fit.se.nhom10.view;

import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.model.Phim;
import iuh.fit.se.nhom10.model.DaoDien;
import iuh.fit.se.nhom10.model.TheLoai;
import iuh.fit.se.nhom10.service.PhimService;
import iuh.fit.se.nhom10.dao.DaoDienDAO;
import iuh.fit.se.nhom10.dao.TheLoaiDAO;
import iuh.fit.se.nhom10.util.ColorPalette;
import iuh.fit.se.nhom10.util.ButtonStyle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Convert FrmQuanLyPhim from JFrame to JPanel for embedding in FrmAdminMenu
 */
public class FrmQuanLyPhimPanel extends JPanel {
    private TaiKhoanNhanVien adminHienTai;
    private PhimService phimService;
    private DaoDienDAO daoDienDAO;
    private TheLoaiDAO theLoaiDAO;
    
    private JTable tblPhim, tblDaoDien, tblTheLoai;
    private DefaultTableModel modelPhim, modelDaoDien, modelTheLoai;

    public FrmQuanLyPhimPanel(TaiKhoanNhanVien admin) throws SQLException {
        this.adminHienTai = admin;
        this.phimService = new PhimService();
        this.daoDienDAO = new DaoDienDAO();
        this.theLoaiDAO = new TheLoaiDAO();
        setupUI();
        loadAllData();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(ColorPalette.BACKGROUND_MAIN);

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(ColorPalette.BACKGROUND_MAIN);
        
        JPanel tabPhim = createPhimTab();
        JPanel tabDaoDien = createDaoDienTab();
        JPanel tabTheLoai = createTheLoaiTab();
        
        tabbedPane.addTab("Quản Lý Phim", tabPhim);
        tabbedPane.addTab("Quản Lý Đạo Diễn", tabDaoDien);
        tabbedPane.addTab("Quản Lý Thể Loại", tabTheLoai);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createPhimTab() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_MAIN);
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Table
        modelPhim = new DefaultTableModel();
        modelPhim.addColumn("Mã Phim");
        modelPhim.addColumn("Tên Phim");
        modelPhim.addColumn("Thể Loại");
        modelPhim.addColumn("Thời Lượng (phút)");
        modelPhim.addColumn("Đạo Diễn");

        tblPhim = createStyledTable(modelPhim);
        JScrollPane scrollPhim = new JScrollPane(tblPhim);

        // Buttons
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlButtons.setBackground(ColorPalette.BACKGROUND_MAIN);

        JButton btnThemPhim = ButtonStyle.createPrimaryButton("Thêm Phim Mới");
        btnThemPhim.addActionListener(e -> handleThemPhim());

        JButton btnSuaPhim = ButtonStyle.createPrimaryButton("Sửa Phim");
        btnSuaPhim.addActionListener(e -> handleSuaPhim());

        JButton btnXoaPhim = ButtonStyle.createDangerButton("Xóa Phim");
        btnXoaPhim.addActionListener(e -> handleXoaPhim());

        JButton btnLamMoi = ButtonStyle.createSecondaryButton("Làm Mới");
        btnLamMoi.addActionListener(e -> loadPhimData());

        pnlButtons.add(btnThemPhim);
        pnlButtons.add(btnSuaPhim);
        pnlButtons.add(btnXoaPhim);
        pnlButtons.add(btnLamMoi);

        pnl.add(scrollPhim, BorderLayout.CENTER);
        pnl.add(pnlButtons, BorderLayout.SOUTH);

        return pnl;
    }


    private JPanel createDaoDienTab() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_MAIN);
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Table
        modelDaoDien = new DefaultTableModel();
        modelDaoDien.addColumn("Mã Đạo Diễn");
        modelDaoDien.addColumn("Tên Đạo Diễn");
        modelDaoDien.addColumn("Quốc Tích");

        tblDaoDien = createStyledTable(modelDaoDien);
        JScrollPane scrollDaoDien = new JScrollPane(tblDaoDien);

        // Buttons
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlButtons.setBackground(ColorPalette.BACKGROUND_MAIN);

        JButton btnThemDD = ButtonStyle.createPrimaryButton("Thêm Đạo Diễn");
        btnThemDD.addActionListener(e -> handleThemDaoDien());

        JButton btnSuaDD = ButtonStyle.createPrimaryButton("Sửa Đạo Diễn");
        btnSuaDD.addActionListener(e -> handleSuaDaoDien());

        JButton btnXoaDD = ButtonStyle.createDangerButton("Xóa Đạo Diễn");
        btnXoaDD.addActionListener(e -> handleXoaDaoDien());

        JButton btnLamMoi = ButtonStyle.createSecondaryButton("Làm Mới");
        btnLamMoi.addActionListener(e -> loadDaoDienData());

        pnlButtons.add(btnThemDD);
        pnlButtons.add(btnSuaDD);
        pnlButtons.add(btnXoaDD);
        pnlButtons.add(btnLamMoi);

        pnl.add(scrollDaoDien, BorderLayout.CENTER);
        pnl.add(pnlButtons, BorderLayout.SOUTH);

        return pnl;
    }

    private JPanel createTheLoaiTab() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_MAIN);
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Table
        modelTheLoai = new DefaultTableModel();
        modelTheLoai.addColumn("Mã Thể Loại");
        modelTheLoai.addColumn("Tên Thể Loại");

        tblTheLoai = createStyledTable(modelTheLoai);
        JScrollPane scrollTheLoai = new JScrollPane(tblTheLoai);

        // Buttons
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlButtons.setBackground(ColorPalette.BACKGROUND_MAIN);

        JButton btnThemTL = ButtonStyle.createPrimaryButton("Thêm Thể Loại");
        btnThemTL.addActionListener(e -> handleThemTheLoai());

        JButton btnSuaTL = ButtonStyle.createPrimaryButton("Sửa Thể Loại");
        btnSuaTL.addActionListener(e -> handleSuaTheLoai());

        JButton btnXoaTL = ButtonStyle.createDangerButton("Xóa Thể Loại");
        btnXoaTL.addActionListener(e -> handleXoaTheLoai());

        JButton btnLamMoi = ButtonStyle.createSecondaryButton("Làm Mới");
        btnLamMoi.addActionListener(e -> loadTheLoaiData());

        pnlButtons.add(btnThemTL);
        pnlButtons.add(btnSuaTL);
        pnlButtons.add(btnXoaTL);
        pnlButtons.add(btnLamMoi);

        pnl.add(scrollTheLoai, BorderLayout.CENTER);
        pnl.add(pnlButtons, BorderLayout.SOUTH);

        return pnl;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(ColorPalette.PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));
        table.getTableHeader().setOpaque(true);
        table.setRowHeight(28);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(200, 200, 200));
        table.setSelectionBackground(ColorPalette.PRIMARY_LIGHT);
        table.setSelectionForeground(Color.WHITE);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        return table;
    }

    private void loadAllData() {
        loadPhimData();
        loadDaoDienData();
        loadTheLoaiData();
    }

    private void loadPhimData() {
        modelPhim.setRowCount(0);
        List<Phim> phimList = phimService.getAllPhim();
        for (Phim phim : phimList) {
            TheLoai tl = theLoaiDAO.getTheLoaiByMa(phim.getMaTheLoai());
            DaoDien dd = daoDienDAO.getDaoDienByMa(phim.getMaDD());
            
            modelPhim.addRow(new Object[]{
                phim.getMaPhim(),
                phim.getTenPhim(),
                tl != null ? tl.getTenTheLoai() : "N/A",
                phim.getThoiLuong(),
                dd != null ? dd.getTenDD() : "N/A"
            });
        }
    }

    private void loadDaoDienData() {
        modelDaoDien.setRowCount(0);
        List<DaoDien> list = daoDienDAO.getAllDaoDien();
        for (DaoDien dd : list) {
            modelDaoDien.addRow(new Object[]{
                dd.getMaDD(),
                dd.getTenDD(),
                dd.getQuocTich()
            });
        }
    }

    private void loadTheLoaiData() {
        modelTheLoai.setRowCount(0);
        List<TheLoai> list = theLoaiDAO.getAllTheLoai();
        for (TheLoai tl : list) {
            modelTheLoai.addRow(new Object[]{
                tl.getMaTheLoai(),
                tl.getTenTheLoai()
            });
        }
    }

    private void handleThemPhim() {
        try {
            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Thêm Phim Mới", true);
            dialog.setSize(600, 500);
            dialog.setLocationRelativeTo(this);
            
            JPanel pnl = createPhimForm(null, dialog);
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
            
            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Sửa Phim", true);
            dialog.setSize(600, 500);
            dialog.setLocationRelativeTo(this);
            
            JPanel pnl = createPhimForm(phim, dialog);
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
            if (phimService.deletePhim(maPhim, adminHienTai)) {
                JOptionPane.showMessageDialog(this, "Xóa phim thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadPhimData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa phim thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void handleThemDaoDien() {
        try {
            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Thêm Đạo Diễn", true);
            dialog.setSize(500, 350);
            dialog.setLocationRelativeTo(this);
            
            JPanel pnl = createDaoDienForm(null, dialog);
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
            DaoDien daoDien = daoDienDAO.getDaoDienByMa(maDD);
            
            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Sửa Đạo Diễn", true);
            dialog.setSize(500, 350);
            dialog.setLocationRelativeTo(this);
            
            JPanel pnl = createDaoDienForm(daoDien, dialog);
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

    private void handleThemTheLoai() {
        try {
            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Thêm Thể Loại", true);
            dialog.setSize(450, 250);
            dialog.setLocationRelativeTo(this);
            
            JPanel pnl = createTheLoaiForm(null, dialog);
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
            TheLoai theLoai = theLoaiDAO.getTheLoaiByMa(maTL);
            
            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Sửa Thể Loại", true);
            dialog.setSize(450, 250);
            dialog.setLocationRelativeTo(this);
            
            JPanel pnl = createTheLoaiForm(theLoai, dialog);
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

    private JPanel createPhimForm(Phim phim, JDialog dialog) {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnl.setBackground(Color.WHITE);

        JLabel lblMaPhim = new JLabel("Mã Phim:");
        JTextField txtMaPhim = new JTextField();
        txtMaPhim.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtMaPhim.setEditable(phim == null);

        JLabel lblTenPhim = new JLabel("Tên Phim:");
        JTextField txtTenPhim = new JTextField();
        txtTenPhim.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel lblTheLoai = new JLabel("Thể Loại:");
        JComboBox<String> cboTheLoai = new JComboBox<>();
        cboTheLoai.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel lblThoiLuong = new JLabel("Thời Lượng (phút):");
        JTextField txtThoiLuong = new JTextField();
        txtThoiLuong.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel lblDaoDien = new JLabel("Đạo Diễn:");
        JComboBox<String> cboDaoDien = new JComboBox<>();
        cboDaoDien.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        // Load combo data
        List<TheLoai> theLoaiList = theLoaiDAO.getAllTheLoai();
        for (TheLoai tl : theLoaiList) {
            cboTheLoai.addItem(tl.getMaTheLoai() + " - " + tl.getTenTheLoai());
        }

        List<DaoDien> daoDienList = daoDienDAO.getAllDaoDien();
        for (DaoDien dd : daoDienList) {
            cboDaoDien.addItem(dd.getMaDD() + " - " + dd.getTenDD());
        }

        if (phim != null) {
            txtMaPhim.setText(phim.getMaPhim());
            txtTenPhim.setText(phim.getTenPhim());
            txtThoiLuong.setText(String.valueOf(phim.getThoiLuong()));
            cboTheLoai.setSelectedItem(phim.getMaTheLoai() + " - " + theLoaiDAO.getTheLoaiByMa(phim.getMaTheLoai()).getTenTheLoai());
            cboDaoDien.setSelectedItem(phim.getMaDD() + " - " + daoDienDAO.getDaoDienByMa(phim.getMaDD()).getTenDD());
        }

        pnl.add(lblMaPhim);
        pnl.add(txtMaPhim);
        pnl.add(Box.createVerticalStrut(10));
        pnl.add(lblTenPhim);
        pnl.add(txtTenPhim);
        pnl.add(Box.createVerticalStrut(10));
        pnl.add(lblTheLoai);
        pnl.add(cboTheLoai);
        pnl.add(Box.createVerticalStrut(10));
        pnl.add(lblThoiLuong);
        pnl.add(txtThoiLuong);
        pnl.add(Box.createVerticalStrut(10));
        pnl.add(lblDaoDien);
        pnl.add(cboDaoDien);
        pnl.add(Box.createVerticalStrut(20));

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pnlButtons.setBackground(Color.WHITE);
        
        JButton btnLuu = ButtonStyle.createPrimaryButton("Lưu");
        btnLuu.addActionListener(e -> {
            try {
                String maPhim = txtMaPhim.getText().trim();
                String tenPhim = txtTenPhim.getText().trim();
                String theLoaiStr = (String) cboTheLoai.getSelectedItem();
                int maTheLoai = Integer.parseInt(theLoaiStr.split(" - ")[0]);
                int thoiLuong = Integer.parseInt(txtThoiLuong.getText().trim());
                String daoDienStr = (String) cboDaoDien.getSelectedItem();
                String maDD = daoDienStr.split(" - ")[0];

                Phim newPhim = new Phim(maPhim, tenPhim, maTheLoai, thoiLuong, maDD);
                
                if (phim == null) {
                    phimService.addPhim(newPhim, adminHienTai);
                } else {
                    phimService.updatePhim(newPhim, adminHienTai);
                }

                loadPhimData();
                dialog.dispose();
                JOptionPane.showMessageDialog(FrmQuanLyPhimPanel.this, "Lưu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(FrmQuanLyPhimPanel.this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnHuy = ButtonStyle.createSecondaryButton("Hủy");
        btnHuy.addActionListener(e -> dialog.dispose());

        pnlButtons.add(btnLuu);
        pnlButtons.add(btnHuy);
        pnl.add(pnlButtons);

        return pnl;
    }

    private JPanel createDaoDienForm(DaoDien daoDien, JDialog dialog) {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnl.setBackground(Color.WHITE);

        JLabel lblMaDD = new JLabel("Mã Đạo Diễn:");
        JTextField txtMaDD = new JTextField();
        txtMaDD.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtMaDD.setEditable(daoDien == null);

        JLabel lblTenDD = new JLabel("Tên Đạo Diễn:");
        JTextField txtTenDD = new JTextField();
        txtTenDD.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel lblQuocTich = new JLabel("Quốc Tích:");
        JTextField txtQuocTich = new JTextField();
        txtQuocTich.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        if (daoDien != null) {
            txtMaDD.setText(daoDien.getMaDD());
            txtTenDD.setText(daoDien.getTenDD());
            txtQuocTich.setText(daoDien.getQuocTich());
        }

        pnl.add(lblMaDD);
        pnl.add(txtMaDD);
        pnl.add(Box.createVerticalStrut(10));
        pnl.add(lblTenDD);
        pnl.add(txtTenDD);
        pnl.add(Box.createVerticalStrut(10));
        pnl.add(lblQuocTich);
        pnl.add(txtQuocTich);
        pnl.add(Box.createVerticalStrut(20));

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pnlButtons.setBackground(Color.WHITE);
        
        JButton btnLuu = ButtonStyle.createPrimaryButton("Lưu");
        btnLuu.addActionListener(e -> {
            try {
                String maDD = txtMaDD.getText().trim();
                String tenDD = txtTenDD.getText().trim();
                String quocTich = txtQuocTich.getText().trim();

                DaoDien newDD = new DaoDien(maDD, tenDD, quocTich);
                
                if (daoDien == null) {
                    daoDienDAO.createDaoDien(newDD);
                } else {
                    daoDienDAO.updateDaoDien(newDD);
                }

                loadDaoDienData();
                loadPhimData();
                dialog.dispose();
                JOptionPane.showMessageDialog(FrmQuanLyPhimPanel.this, "Lưu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(FrmQuanLyPhimPanel.this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnHuy = ButtonStyle.createSecondaryButton("Hủy");
        btnHuy.addActionListener(e -> dialog.dispose());

        pnlButtons.add(btnLuu);
        pnlButtons.add(btnHuy);
        pnl.add(pnlButtons);

        return pnl;
    }

    private JPanel createTheLoaiForm(TheLoai theLoai, JDialog dialog) {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnl.setBackground(Color.WHITE);

        JLabel lblTenTheLoai = new JLabel("Tên Thể Loại:");
        JTextField txtTenTheLoai = new JTextField();
        txtTenTheLoai.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        if (theLoai != null) {
            txtTenTheLoai.setText(theLoai.getTenTheLoai());
        }

        pnl.add(lblTenTheLoai);
        pnl.add(txtTenTheLoai);
        pnl.add(Box.createVerticalStrut(20));

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pnlButtons.setBackground(Color.WHITE);
        
        JButton btnLuu = ButtonStyle.createPrimaryButton("Lưu");
        btnLuu.addActionListener(e -> {
            try {
                String tenTheLoai = txtTenTheLoai.getText().trim();
                TheLoai newTL = new TheLoai();
                newTL.setTenTheLoai(tenTheLoai);
                
                if (theLoai == null) {
                    theLoaiDAO.createTheLoai(newTL);
                } else {
                    newTL.setMaTheLoai(theLoai.getMaTheLoai());
                    theLoaiDAO.updateTheLoai(newTL);
                }

                loadTheLoaiData();
                loadPhimData();
                dialog.dispose();
                JOptionPane.showMessageDialog(FrmQuanLyPhimPanel.this, "Lưu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(FrmQuanLyPhimPanel.this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnHuy = ButtonStyle.createSecondaryButton("Hủy");
        btnHuy.addActionListener(e -> dialog.dispose());

        pnlButtons.add(btnLuu);
        pnlButtons.add(btnHuy);
        pnl.add(pnlButtons);

        return pnl;
    }
}
