package iuh.fit.se.nhom10.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Lớp để tạo nút bấm chuyên nghiệp với hiệu ứng hover và shadow
 */
public class ButtonStyle {
    
    /**
     * Tạo nút Primary (Đăng Nhập, OK, Lưu, v.v)
     */
    public static void stylePrimaryButton(JButton button) {
        button.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_BUTTON, Font.BOLD));
        button.setBackground(ColorPalette.BUTTON_PRIMARY_BG);
        button.setForeground(ColorPalette.BUTTON_PRIMARY_TEXT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(130, 40));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ColorPalette.BUTTON_PRIMARY_BG_HOVER);
                button.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ColorPalette.BUTTON_PRIMARY_BG);
                button.repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(ColorPalette.BUTTON_PRIMARY_BG_PRESS);
                button.repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(ColorPalette.BUTTON_PRIMARY_BG_HOVER);
                button.repaint();
            }
        });
    }
    
    /**
     * Tạo nút Secondary (Thoát, Hủy, v.v)
     */
    public static void styleSecondaryButton(JButton button) {
        button.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_BUTTON, Font.BOLD));
        button.setBackground(ColorPalette.BUTTON_SECONDARY_BG);
        button.setForeground(ColorPalette.BUTTON_SECONDARY_TEXT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(130, 40));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ColorPalette.BUTTON_SECONDARY_BG_HOVER);
                button.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ColorPalette.BUTTON_SECONDARY_BG);
                button.repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(ColorPalette.BUTTON_SECONDARY_BG_PRESS);
                button.repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(ColorPalette.BUTTON_SECONDARY_BG_HOVER);
                button.repaint();
            }
        });
    }
    
    /**
     * Tạo nút Danger (Xóa, v.v)
     */
    public static void styleDangerButton(JButton button) {
        button.setFont(ColorPalette.getFont(ColorPalette.FONT_SIZE_BUTTON, Font.BOLD));
        button.setBackground(ColorPalette.BUTTON_DANGER_BG);
        button.setForeground(ColorPalette.BUTTON_DANGER_TEXT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(130, 40));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ColorPalette.BUTTON_DANGER_BG_HOVER);
                button.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ColorPalette.BUTTON_DANGER_BG);
                button.repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(ColorPalette.BUTTON_DANGER_BG_PRESS);
                button.repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(ColorPalette.BUTTON_DANGER_BG_HOVER);
                button.repaint();
            }
        });
    }

    /**
     * Tạo nút Primary mới (Đăng Nhập, OK, Lưu, v.v)
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        stylePrimaryButton(button);
        return button;
    }

    /**
     * Tạo nút Secondary mới (Thoát, Hủy, v.v)
     */
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        styleSecondaryButton(button);
        return button;
    }

    /**
     * Tạo nút Danger mới (Xóa, v.v)
     */
    public static JButton createDangerButton(String text) {
        JButton button = new JButton(text);
        styleDangerButton(button);
        return button;
    }
}
