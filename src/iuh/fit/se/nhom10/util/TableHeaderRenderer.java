package iuh.fit.se.nhom10.util;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Custom renderer cho table header với styling chuyên nghiệp
 */
public class TableHeaderRenderer extends JLabel implements TableCellRenderer {
    
    public TableHeaderRenderer() {
        setOpaque(true);
        setBackground(ColorPalette.PRIMARY);
        setForeground(Color.WHITE);
        setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_LABEL, Font.BOLD));
        setHorizontalAlignment(JLabel.CENTER);
        setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText(value != null ? value.toString() : "");
        return this;
    }
}
