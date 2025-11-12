package iuh.fit.se.nhom10.view;

import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.model.PhongChieu;
import iuh.fit.se.nhom10.model.LoaiPhong;
import iuh.fit.se.nhom10.model.GheNgoi;
import iuh.fit.se.nhom10.service.PhongChieuService;
import iuh.fit.se.nhom10.util.ColorPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Panel quản lý phòng chiếu
 */
public class FrmQuanLyPhongChieuPanel extends JPanel {
    private TaiKhoanNhanVien admin;
    private PhongChieuService service;
    private JTabbedPane tabbedPane;
    private JTable tblPhongChieu, tblLoaiPhong, tblGheNgoi;
    private DefaultTableModel modelPhong, modelLoaiPhong, modelGhe;

    public FrmQuanLyPhongChieuPanel(TaiKhoanNhanVien admin) {
        this.admin = admin;
        this.service = new PhongChieuService();
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(ColorPalette.BACKGROUND_CONTENT);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        
        tabbedPane.addTab("Loại Phòng", createLoaiPhongTab());
        tabbedPane.addTab("Phòng Chiếu", createPhongChieuTab());
        tabbedPane.addTab("Ghế Ngồi", createGheNgoiTab());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // ===== TAB LOẠI PHÒNG =====
    
    private JPanel createLoaiPhongTab() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel pnlControl = createControlPanel("Loại Phòng", "Thêm Loại", new AbstractAction("Thêm Loại") {
            @Override
            public void actionPerformed(ActionEvent e) {
                addLoaiPhong();
            }
        });
        pnl.add(pnlControl, BorderLayout.NORTH);

        String[] columnsLoaiPhong = {"Mã Loại Phòng", "Tên Loại Phòng"};
        modelLoaiPhong = new DefaultTableModel(columnsLoaiPhong, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblLoaiPhong = createTable(modelLoaiPhong);
        JScrollPane scrollPane = new JScrollPane(tblLoaiPhong);
        pnl.add(scrollPane, BorderLayout.CENTER);

        JPanel pnlButtons = createButtonPanel("Sửa Loại Phòng", "Xóa Loại Phòng",
            new AbstractAction("Sửa") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    editLoaiPhong();
                }
            },
            new AbstractAction("Xóa") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteLoaiPhong();
                }
            });
        pnl.add(pnlButtons, BorderLayout.SOUTH);

        loadLoaiPhongData();
        return pnl;
    }

    private void loadLoaiPhongData() {
        modelLoaiPhong.setRowCount(0);
        try {
            List<LoaiPhong> list = service.getAllLoaiPhong();
            for (LoaiPhong lp : list) {
                modelLoaiPhong.addRow(new Object[]{lp.getMaLoaiPhong(), lp.getTenLoaiPhong()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + ex.getMessage());
        }
    }

    private void addLoaiPhong() {
        String tenLoaiPhong = JOptionPane.showInputDialog(this, "Nhập tên loại phòng:");
        if (tenLoaiPhong != null && !tenLoaiPhong.trim().isEmpty()) {
            try {
                LoaiPhong lp = new LoaiPhong(tenLoaiPhong);
                service.createLoaiPhong(lp);
                loadLoaiPhongData();
                JOptionPane.showMessageDialog(this, "Thêm loại phòng thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }

    private void editLoaiPhong() {
        int row = tblLoaiPhong.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng để sửa!");
            return;
        }
        try {
            int maLoaiPhong = (int) modelLoaiPhong.getValueAt(row, 0);
            String tenHienTai = modelLoaiPhong.getValueAt(row, 1).toString();
            String tenMoi = JOptionPane.showInputDialog(this, "Nhập tên loại phòng mới:", tenHienTai);
            
            if (tenMoi != null && !tenMoi.trim().isEmpty()) {
                LoaiPhong lp = service.getLoaiPhongByMa(String.valueOf(maLoaiPhong));
                lp.setTenLoaiPhong(tenMoi);
                service.updateLoaiPhong(lp);
                loadLoaiPhongData();
                JOptionPane.showMessageDialog(this, "Cập nhật loại phòng thành công!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void deleteLoaiPhong() {
        int row = tblLoaiPhong.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng để xóa!");
            return;
        }
        try {
            int maLoaiPhong = (int) modelLoaiPhong.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                service.deleteLoaiPhong(String.valueOf(maLoaiPhong));
                loadLoaiPhongData();
                loadPhongChieuData();
                JOptionPane.showMessageDialog(this, "Xóa loại phòng thành công!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    // ===== TAB PHÒNG CHIẾU =====
    
    private JPanel createPhongChieuTab() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel pnlControl = createControlPanel("Phòng Chiếu", "Thêm Phòng", new AbstractAction("Thêm Phòng") {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPhongChieu();
            }
        });
        pnl.add(pnlControl, BorderLayout.NORTH);

        String[] columnsPhong = {"Mã Phòng", "Tên Phòng", "Số Ghế", "Loại Phòng"};
        modelPhong = new DefaultTableModel(columnsPhong, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPhongChieu = createTable(modelPhong);
        JScrollPane scrollPane = new JScrollPane(tblPhongChieu);
        pnl.add(scrollPane, BorderLayout.CENTER);

        JPanel pnlButtons = createButtonPanel("Sửa Phòng", "Xóa Phòng",
            new AbstractAction("Sửa") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    editPhongChieu();
                }
            },
            new AbstractAction("Xóa") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deletePhongChieu();
                }
            });
        pnl.add(pnlButtons, BorderLayout.SOUTH);

        loadPhongChieuData();
        return pnl;
    }

    private void loadPhongChieuData() {
        modelPhong.setRowCount(0);
        try {
            List<PhongChieu> list = service.getAllPhongChieu();
            for (PhongChieu p : list) {
                LoaiPhong lp = service.getLoaiPhongByMa(String.valueOf(p.getMaLoaiPhong()));
                String tenLoaiPhong = (lp != null) ? lp.getTenLoaiPhong() : "N/A";
                modelPhong.addRow(new Object[]{p.getMaPhong(), p.getTenPhong(), p.getSoGhe(), tenLoaiPhong});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + ex.getMessage());
        }
    }

    private void addPhongChieu() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField txtMaPhong = new JTextField();
        JTextField txtTenPhong = new JTextField();
        JTextField txtSoGhe = new JTextField();
        JComboBox<LoaiPhong> cmbLoaiPhong = new JComboBox<>();
        
        try {
            for (LoaiPhong lp : service.getAllLoaiPhong()) {
                cmbLoaiPhong.addItem(lp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        panel.add(new JLabel("Mã Phòng:"));
        panel.add(txtMaPhong);
        panel.add(new JLabel("Tên Phòng:"));
        panel.add(txtTenPhong);
        panel.add(new JLabel("Số Ghế:"));
        panel.add(txtSoGhe);
        panel.add(new JLabel("Loại Phòng:"));
        panel.add(cmbLoaiPhong);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Thêm Phòng Chiếu", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                PhongChieu p = new PhongChieu();
                p.setMaPhong(txtMaPhong.getText());
                p.setTenPhong(txtTenPhong.getText());
                p.setSoGhe(Integer.parseInt(txtSoGhe.getText()));
                LoaiPhong lp = (LoaiPhong) cmbLoaiPhong.getSelectedItem();
                if (lp != null) {
                    p.setMaLoaiPhong(lp.getMaLoaiPhong());
                }
                service.createPhongChieu(p);
                loadPhongChieuData();
                JOptionPane.showMessageDialog(this, "Thêm phòng chiếu thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }

    private void editPhongChieu() {
        int row = tblPhongChieu.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng để sửa!");
            return;
        }
        try {
            String maPhong = modelPhong.getValueAt(row, 0).toString();
            PhongChieu p = service.getPhongChieuByMa(maPhong);
            
            JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
            JTextField txtTenPhong = new JTextField(p.getTenPhong());
            JTextField txtSoGhe = new JTextField(String.valueOf(p.getSoGhe()));
            JComboBox<LoaiPhong> cmbLoaiPhong = new JComboBox<>();
            
            for (LoaiPhong lp : service.getAllLoaiPhong()) {
                cmbLoaiPhong.addItem(lp);
                if (lp.getMaLoaiPhong() == p.getMaLoaiPhong()) {
                    cmbLoaiPhong.setSelectedItem(lp);
                }
            }
            
            panel.add(new JLabel("Mã Phòng:"));
            panel.add(new JLabel(maPhong));
            panel.add(new JLabel("Tên Phòng:"));
            panel.add(txtTenPhong);
            panel.add(new JLabel("Số Ghế:"));
            panel.add(txtSoGhe);
            panel.add(new JLabel("Loại Phòng:"));
            panel.add(cmbLoaiPhong);
            
            int result = JOptionPane.showConfirmDialog(this, panel, "Sửa Phòng Chiếu", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                p.setTenPhong(txtTenPhong.getText());
                p.setSoGhe(Integer.parseInt(txtSoGhe.getText()));
                LoaiPhong lp = (LoaiPhong) cmbLoaiPhong.getSelectedItem();
                if (lp != null) {
                    p.setMaLoaiPhong(lp.getMaLoaiPhong());
                }
                service.updatePhongChieu(p);
                loadPhongChieuData();
                JOptionPane.showMessageDialog(this, "Cập nhật phòng chiếu thành công!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void deletePhongChieu() {
        int row = tblPhongChieu.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng để xóa!");
            return;
        }
        try {
            String maPhong = modelPhong.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                service.deletePhongChieu(maPhong);
                loadPhongChieuData();
                loadGheNgoiData();
                JOptionPane.showMessageDialog(this, "Xóa phòng chiếu thành công!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    // ===== TAB GHẾ NGỒI =====
    
    private JPanel createGheNgoiTab() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel pnlControl = createControlPanel("Ghế Ngồi", "Thêm Ghế", new AbstractAction("Thêm Ghế") {
            @Override
            public void actionPerformed(ActionEvent e) {
                addGheNgoi();
            }
        });
        pnl.add(pnlControl, BorderLayout.NORTH);

        String[] columnsGhe = {"Mã Ghế", "Hàng", "Cột", "Trạng Thái"};
        modelGhe = new DefaultTableModel(columnsGhe, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblGheNgoi = createTable(modelGhe);
        JScrollPane scrollPane = new JScrollPane(tblGheNgoi);
        pnl.add(scrollPane, BorderLayout.CENTER);

        JPanel pnlButtons = createButtonPanel("Sửa Ghế", "Xóa Ghế",
            new AbstractAction("Sửa") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    editGheNgoi();
                }
            },
            new AbstractAction("Xóa") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteGheNgoi();
                }
            });
        pnl.add(pnlButtons, BorderLayout.SOUTH);

        loadGheNgoiData();
        return pnl;
    }

    private void loadGheNgoiData() {
        modelGhe.setRowCount(0);
        try {
            List<GheNgoi> list = service.getAllGheNgoi();
            for (GheNgoi g : list) {
                modelGhe.addRow(new Object[]{g.getMaGhe(), g.getHang(), g.getCot(), g.getTrangThai()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + ex.getMessage());
        }
    }

    private void addGheNgoi() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField txtMaGhe = new JTextField();
        JTextField txtHang = new JTextField();
        JTextField txtCot = new JTextField();
        JComboBox<String> cmbTrangThai = new JComboBox<>(new String[]{"Trống", "Đã Đặt", "Bảo Trì"});
        
        panel.add(new JLabel("Mã Ghế:"));
        panel.add(txtMaGhe);
        panel.add(new JLabel("Hàng:"));
        panel.add(txtHang);
        panel.add(new JLabel("Cột:"));
        panel.add(txtCot);
        panel.add(new JLabel("Trạng Thái:"));
        panel.add(cmbTrangThai);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Thêm Ghế Ngồi", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                GheNgoi g = new GheNgoi();
                g.setMaGhe(txtMaGhe.getText());
                g.setHang(txtHang.getText());
                g.setCot(Integer.parseInt(txtCot.getText()));
                g.setTrangThai((String) cmbTrangThai.getSelectedItem());
                service.createGheNgoi(g);
                loadGheNgoiData();
                JOptionPane.showMessageDialog(this, "Thêm ghế ngồi thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }

    private void editGheNgoi() {
        int row = tblGheNgoi.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ghế để sửa!");
            return;
        }
        try {
            String maGhe = modelGhe.getValueAt(row, 0).toString();
            GheNgoi g = service.getGheNgoiByMa(maGhe);
            
            JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
            JTextField txtHang = new JTextField(g.getHang());
            JTextField txtCot = new JTextField(String.valueOf(g.getCot()));
            JComboBox<String> cmbTrangThai = new JComboBox<>(new String[]{"Trống", "Đã Đặt", "Bảo Trì"});
            cmbTrangThai.setSelectedItem(g.getTrangThai());
            
            panel.add(new JLabel("Hàng:"));
            panel.add(txtHang);
            panel.add(new JLabel("Cột:"));
            panel.add(txtCot);
            panel.add(new JLabel("Trạng Thái:"));
            panel.add(cmbTrangThai);
            
            int result = JOptionPane.showConfirmDialog(this, panel, "Sửa Ghế Ngồi", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                g.setHang(txtHang.getText());
                g.setCot(Integer.parseInt(txtCot.getText()));
                g.setTrangThai((String) cmbTrangThai.getSelectedItem());
                service.updateGheNgoi(g);
                loadGheNgoiData();
                JOptionPane.showMessageDialog(this, "Cập nhật ghế ngồi thành công!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void deleteGheNgoi() {
        int row = tblGheNgoi.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ghế để xóa!");
            return;
        }
        try {
            String maGhe = modelGhe.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                service.deleteGheNgoi(maGhe);
                loadGheNgoiData();
                JOptionPane.showMessageDialog(this, "Xóa ghế ngồi thành công!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    // ===== HELPER METHODS =====
    
    private JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        table.setRowHeight(25);
        table.getTableHeader().setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        table.getTableHeader().setBackground(ColorPalette.BUTTON_PRIMARY_BG);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(ColorPalette.BUTTON_SECONDARY_BG);
        table.setSelectionForeground(Color.WHITE);
        return table;
    }

    private JPanel createControlPanel(String title, String buttonText, AbstractAction action) {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BorderLayout());
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

        JLabel lbl = new JLabel(title);
        lbl.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_SUBTITLE, Font.BOLD));
        lbl.setForeground(ColorPalette.TEXT_LABEL);

        JButton btn = new JButton(action);
        btn.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        btn.setBackground(ColorPalette.BUTTON_PRIMARY_BG);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnl.add(lbl, BorderLayout.WEST);
        pnl.add(btn, BorderLayout.EAST);

        return pnl;
    }

    private JPanel createButtonPanel(String btn1Text, String btn2Text, AbstractAction action1, AbstractAction action2) {
        JPanel pnl = new JPanel();
        pnl.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

        JButton btn1 = new JButton(action1);
        btn1.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        btn1.setBackground(ColorPalette.BUTTON_SECONDARY_BG);
        btn1.setForeground(Color.WHITE);
        btn1.setFocusPainted(false);
        btn1.setOpaque(true);
        btn1.setContentAreaFilled(true);
        btn1.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btn2 = new JButton(action2);
        btn2.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        btn2.setBackground(ColorPalette.BUTTON_DANGER_BG);
        btn2.setForeground(Color.WHITE);
        btn2.setFocusPainted(false);
        btn2.setOpaque(true);
        btn2.setContentAreaFilled(true);
        btn2.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnl.add(btn1);
        pnl.add(btn2);

        return pnl;
    }
}
