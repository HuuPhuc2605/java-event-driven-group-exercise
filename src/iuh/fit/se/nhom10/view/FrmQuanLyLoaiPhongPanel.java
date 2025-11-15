package iuh.fit.se.nhom10.view;

import iuh.fit.se.nhom10.dao.LoaiPhongDAO;
import iuh.fit.se.nhom10.model.LoaiPhong;
import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.util.ButtonStyle;
import iuh.fit.se.nhom10.util.ColorPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Quản lý loại phòng chiếu - Thêm, Sửa, Xóa loại phòng
 */
public class FrmQuanLyLoaiPhongPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable tblLoaiPhong;
    private JTextField txtSearch;
    private JButton btnThem, btnSua, btnXoa;
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
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel pnlHeader = createHeaderPanel();
        add(pnlHeader, BorderLayout.NORTH);
        
        // Panel bảng
        JPanel pnlTable = createTablePanel();
        add(pnlTable, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel pnl = new JPanel(new BorderLayout(10, 0));
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setPreferredSize(new Dimension(0, 60));

        JPanel pnlTitle = new JPanel();
        pnlTitle.setPreferredSize(new Dimension(300, 50));
        pnlTitle.setBackground(ColorPalette.PRIMARY);
        pnlTitle.setLayout(new BoxLayout(pnlTitle, BoxLayout.X_AXIS));
        pnlTitle.setBorder(new EmptyBorder(0, 15, 0, 15));

        JLabel lblTitle = new JLabel("Quản Lý Loại Phòng");
        lblTitle.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_TITLE - 4, Font.BOLD));
        lblTitle.setForeground(Color.WHITE);
        pnlTitle.add(lblTitle);

        // Search and button control panel
        JPanel pnlSearchControl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 7));
        pnlSearchControl.setBackground(ColorPalette.BACKGROUND_CONTENT);

        // Search input
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(250, 38));
        txtSearch.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_INPUT, Font.PLAIN));
        txtSearch.setToolTipText("Tìm kiếm theo tên loại phòng");
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorPalette.BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterTable(); }
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            public void changedUpdate(DocumentEvent e) { filterTable(); }
        });

        btnThem = ButtonStyle.createPrimaryButton("Thêm Loại");
        btnThem.setBackground(ColorPalette.BUTTON_SUCCESS_BG);
        btnThem.addActionListener(e -> showDialogThem());
        
        btnSua = ButtonStyle.createPrimaryButton("Sửa");
        btnSua.addActionListener(e -> showDialogSua());
        
        btnXoa = ButtonStyle.createDangerButton("Xóa");
        btnXoa.addActionListener(e -> showDialogXoa());

        pnlSearchControl.add(txtSearch);
        pnlSearchControl.add(btnThem);
        pnlSearchControl.add(btnSua);
        pnlSearchControl.add(btnXoa);

        pnl.add(pnlTitle, BorderLayout.WEST);
        pnl.add(pnlSearchControl, BorderLayout.EAST);

        return pnl;
    }

    private JPanel createTablePanel() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(ColorPalette.BACKGROUND_CONTENT);
        pnl.setBorder(new EmptyBorder(0, 0, 0, 0));

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableModel.addColumn("Mã Loại Phòng");
        tableModel.addColumn("Tên Loại Phòng");

        tblLoaiPhong = new JTable(tableModel);
        tblLoaiPhong.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.PLAIN));
        tblLoaiPhong.setRowHeight(35);
        tblLoaiPhong.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblLoaiPhong.setGridColor(new Color(220, 225, 235));

        JTableHeader header =  tblLoaiPhong.getTableHeader();
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
                setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));
                return this;
            }
        });

        tblLoaiPhong.getColumnModel().getColumn(0).setPreferredWidth(150);
        tblLoaiPhong.getColumnModel().getColumn(1).setPreferredWidth(350);

        JScrollPane scrollPane = new JScrollPane(tblLoaiPhong);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER_LIGHT, 1));
        pnl.add(scrollPane, BorderLayout.CENTER);

        return pnl;
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
        for (LoaiPhong loaiPhong : list) {
            tableModel.addRow(new Object[]{
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
        
        JButton btnOk = ButtonStyle.createPrimaryButton("Lưu");
        btnOk.setBackground(ColorPalette.BUTTON_SUCCESS_BG);
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
        
        JButton btnCancel = ButtonStyle.createSecondaryButton("Hủy");
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

        int maLoaiPhong = (int) tableModel.getValueAt(selectedRow, 0);
        String tenLoaiPhong = (String) tableModel.getValueAt(selectedRow, 1);

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
        
        JButton btnOk = ButtonStyle.createPrimaryButton("Lưu");
        btnOk.setBackground(ColorPalette.BUTTON_SUCCESS_BG);
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
        
        JButton btnCancel = ButtonStyle.createSecondaryButton("Hủy");
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

        int maLoaiPhong = (int) tableModel.getValueAt(selectedRow, 0);
        String tenLoaiPhong = (String) tableModel.getValueAt(selectedRow, 1);

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
