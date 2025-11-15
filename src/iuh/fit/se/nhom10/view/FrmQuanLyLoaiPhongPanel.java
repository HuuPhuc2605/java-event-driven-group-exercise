package iuh.fit.se.nhom10.view;

import iuh.fit.se.nhom10.dao.LoaiPhongDAO;
import iuh.fit.se.nhom10.model.LoaiPhong;
import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.util.ColorPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Quản lý loại phòng chiếu - Thêm, Sửa, Xóa loại phòng
 */
public class FrmQuanLyLoaiPhongPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable tblLoaiPhong;
    private JTextField txtSearch;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;
    private LoaiPhongDAO loaiPhongDAO;
    private List<LoaiPhong> currentList;
    private TaiKhoanNhanVien admin;

    public FrmQuanLyLoaiPhongPanel(TaiKhoanNhanVien admin) {
        this.admin = admin;
        this.loaiPhongDAO = new LoaiPhongDAO();
        this.currentList = new ArrayList<>();
        
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(ColorPalette.BACKGROUND_CONTENT);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel tìm kiếm
        JPanel pnlSearch = createSearchPanel();
        
        // Panel bảng
        JPanel pnlTable = createTablePanel();
        
        // Panel nút điều khiển
        JPanel pnlButton = createButtonPanel();

        add(pnlSearch, BorderLayout.NORTH);
        add(pnlTable, BorderLayout.CENTER);
        add(pnlButton, BorderLayout.SOUTH);
    }

    private JPanel createSearchPanel() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        
        txtSearch = new JTextField(20);
        txtSearch.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtSearch.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT));
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filterTable(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filterTable(); }
        });

        pnl.add(lblSearch);
        pnl.add(txtSearch);
        return pnl;
    }

    private JPanel createTablePanel() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

        // Tạo table model
        tableModel = new DefaultTableModel(
            new String[]{"STT", "Mã Loại", "Tên Loại Phòng"},
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblLoaiPhong = new JTable(tableModel);
        tblLoaiPhong.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        tblLoaiPhong.setRowHeight(25);
        tblLoaiPhong.setBackground(Color.WHITE);
        tblLoaiPhong.setGridColor(ColorPalette.BORDER_LIGHT);
        tblLoaiPhong.getTableHeader().setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        tblLoaiPhong.getTableHeader().setBackground(ColorPalette.PRIMARY);
        tblLoaiPhong.getTableHeader().setForeground(Color.WHITE);
        tblLoaiPhong.getSelectionModel().addListSelectionListener(e -> {
            if (tblLoaiPhong.getSelectedRow() != -1) {
                btnSua.setEnabled(true);
                btnXoa.setEnabled(true);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblLoaiPhong);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT));
        pnl.add(scrollPane, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel createButtonPanel() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

        btnThem = createButton("Thêm", ColorPalette.BUTTON_PRIMARY_BG);
        btnThem.addActionListener(e -> showDialogThem());

        btnSua = createButton("Sửa", ColorPalette.BUTTON_PRIMARY_BG);
        btnSua.addActionListener(e -> showDialogSua());
        btnSua.setEnabled(false);

        btnXoa = createButton("Xóa", ColorPalette.BUTTON_DANGER_BG);
        btnXoa.addActionListener(e -> showDialogXoa());
        btnXoa.setEnabled(false);

        btnLamMoi = createButton("Làm Mới", ColorPalette.BUTTON_SECONDARY_BG);
        btnLamMoi.addActionListener(e -> {
            loadData();
            txtSearch.setText("");
        });

        pnl.add(btnThem);
        pnl.add(btnSua);
        pnl.add(btnXoa);
        pnl.add(btnLamMoi);

        return pnl;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_BUTTON, Font.BOLD));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 35));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(ColorPalette.lighten(bgColor, 0.1f));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });
        return btn;
    }

    private void loadData() {
        try {
            currentList = loaiPhongDAO.getAllLoaiPhong();
            updateTable(currentList);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable(List<LoaiPhong> list) {
        tableModel.setRowCount(0);
        int stt = 1;
        for (LoaiPhong loaiPhong : list) {
            tableModel.addRow(new Object[]{
                stt++,
                loaiPhong.getMaLoaiPhong(),
                loaiPhong.getTenLoaiPhong()
            });
        }
    }

    private void filterTable() {
        String keyword = txtSearch.getText().toLowerCase().trim();
        List<LoaiPhong> filtered = new ArrayList<>();
        for (LoaiPhong loaiPhong : currentList) {
            if (loaiPhong.getTenLoaiPhong().toLowerCase().contains(keyword)) {
                filtered.add(loaiPhong);
            }
        }
        updateTable(filtered);
    }

    private void showDialogThem() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Loại Phòng", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

        JLabel lbl = new JLabel("Tên Loại Phòng:");
        lbl.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        JTextField txt = new JTextField();
        txt.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        pnl.add(lbl);
        pnl.add(Box.createVerticalStrut(5));
        pnl.add(txt);
        pnl.add(Box.createVerticalGlue());

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setBackground(ColorPalette.BACKGROUND_CONTENT);
        JButton btnOk = createButton("Lưu", ColorPalette.BUTTON_PRIMARY_BG);
        btnOk.addActionListener(e -> {
            if (txt.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập tên loại phòng");
                return;
            }
            LoaiPhong loaiPhong = new LoaiPhong(txt.getText().trim());
            if (loaiPhongDAO.createLoaiPhong(loaiPhong)) {
                JOptionPane.showMessageDialog(dialog, "Thêm loại phòng thành công!");
                loadData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Lỗi thêm loại phòng", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton btnCancel = createButton("Hủy", ColorPalette.BUTTON_SECONDARY_BG);
        btnCancel.addActionListener(e -> dialog.dispose());
        pnlButtons.add(btnOk);
        pnlButtons.add(btnCancel);
        pnl.add(pnlButtons);

        dialog.add(pnl);
        dialog.setVisible(true);
    }

    private void showDialogSua() {
        int selectedRow = tblLoaiPhong.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng cần sửa");
            return;
        }

        int maLoaiPhong = (int) tableModel.getValueAt(selectedRow, 1);
        String tenLoaiPhong = (String) tableModel.getValueAt(selectedRow, 2);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa Loại Phòng", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);

        JLabel lbl = new JLabel("Tên Loại Phòng:");
        lbl.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        JTextField txt = new JTextField(tenLoaiPhong);
        txt.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        pnl.add(lbl);
        pnl.add(Box.createVerticalStrut(5));
        pnl.add(txt);
        pnl.add(Box.createVerticalGlue());

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setBackground(ColorPalette.BACKGROUND_CONTENT);
        JButton btnOk = createButton("Lưu", ColorPalette.BUTTON_PRIMARY_BG);
        btnOk.addActionListener(e -> {
            if (txt.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập tên loại phòng");
                return;
            }
            LoaiPhong loaiPhong = new LoaiPhong(maLoaiPhong, txt.getText().trim());
            if (loaiPhongDAO.updateLoaiPhong(loaiPhong)) {
                JOptionPane.showMessageDialog(dialog, "Sửa loại phòng thành công!");
                loadData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Lỗi sửa loại phòng", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton btnCancel = createButton("Hủy", ColorPalette.BUTTON_SECONDARY_BG);
        btnCancel.addActionListener(e -> dialog.dispose());
        pnlButtons.add(btnOk);
        pnlButtons.add(btnCancel);
        pnl.add(pnlButtons);

        dialog.add(pnl);
        dialog.setVisible(true);
    }

    private void showDialogXoa() {
        int selectedRow = tblLoaiPhong.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng cần xóa");
            return;
        }

        int maLoaiPhong = (int) tableModel.getValueAt(selectedRow, 1);
        String tenLoaiPhong = (String) tableModel.getValueAt(selectedRow, 2);

        int result = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn xóa loại phòng: " + tenLoaiPhong + "?\n(Tất cả phòng thuộc loại này sẽ bị xóa)",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            if (loaiPhongDAO.deleteLoaiPhong(maLoaiPhong)) {
                JOptionPane.showMessageDialog(this, "Xóa loại phòng thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi xóa loại phòng", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
