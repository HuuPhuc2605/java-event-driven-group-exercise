package iuh.fit.se.nhom10.util;

import java.awt.*;

/**
 * Lớp định nghĩa bảng màu tổng cho toàn bộ ứng dụng
 * Tất cả các lớp view khác chỉ cần import và sử dụng các hằng số màu từ lớp này
 * Nếu cần thay đổi màu, chỉ cần thay đổi ở một nơi duy nhất
 */
public class ColorPalette {
    
    // ====== PRIMARY COLORS (Gradient xanh lục chuyên nghiệp) ======
    public static final Color PRIMARY_DARK = new Color(13, 71, 95);      // Xanh đậm
    public static final Color PRIMARY = new Color(21, 107, 141);          // Xanh chính
    public static final Color PRIMARY_LIGHT = new Color(29, 145, 187);    // Xanh nhạt
    public static final Color ACCENT = new Color(0, 184, 148);            // Xanh lá cây (accent)
    
    // ====== SEAT COLORS ======
    public static final Color SEAT_EMPTY = new Color(52, 211, 153);       // Xanh lá nhạt - ghế trống
    public static final Color SEAT_SELECTED = new Color(34, 197, 94);     // Xanh lá đậm - ghế được chọn
    public static final Color SEAT_BOOKED = new Color(156, 163, 175);     // Xám - ghế đã đặt
    public static final Color SEAT_MAINTENANCE = new Color(239, 68, 68);  // Đỏ - ghế bảo trì
    
    // ====== ROOM STATUS COLORS ======
    public static final Color ROOM_AVAILABLE = new Color(52, 211, 153);   // Xanh - còn chỗ
    public static final Color ROOM_FULL = new Color(239, 68, 68);          // Đỏ - phòng full
    
    // ====== HEADER GRADIENT ======
    public static final Color HEADER_GRADIENT_TOP = PRIMARY;
    public static final Color HEADER_GRADIENT_BOTTOM = PRIMARY_DARK;
    
    // ====== BACKGROUNDS ======
    public static final Color BACKGROUND_MAIN = new Color(245, 247, 250);    // Xám rất nhạt
    public static final Color BACKGROUND_CONTENT = Color.WHITE;
    public static final Color BACKGROUND_INPUT = new Color(248, 250, 252);   // Xám siêu nhạt
    
    // ====== TEXT & LABELS ======
    public static final Color TEXT_HEADER_TITLE = Color.WHITE;
    public static final Color TEXT_HEADER_SUBTITLE = new Color(180, 220, 240);
    public static final Color TEXT_LABEL = new Color(45, 55, 70);
    public static final Color TEXT_PLACEHOLDER = new Color(130, 145, 160);
    public static final Color TEXT_BODY = new Color(55, 65, 85);
    
    // ====== BORDERS & SEPARATORS ======
    public static final Color BORDER_INPUT = new Color(190, 210, 230);
    public static final Color BORDER_LIGHT = new Color(225, 235, 245);
    public static final Color BORDER_FOCUS = ACCENT;
    
    // ====== BUTTONS ======
    // Primary Button (Đăng Nhập)
    public static final Color BUTTON_PRIMARY_BG = PRIMARY;
    public static final Color BUTTON_PRIMARY_BG_HOVER = PRIMARY_LIGHT;
    public static final Color BUTTON_PRIMARY_BG_PRESS = PRIMARY_DARK;
    public static final Color BUTTON_PRIMARY_TEXT = Color.WHITE;
    
    // Secondary Button (Thoát/Hủy)
    public static final Color BUTTON_SECONDARY_BG = new Color(180, 195, 215);
    public static final Color BUTTON_SECONDARY_BG_HOVER = new Color(155, 175, 205);
    public static final Color BUTTON_SECONDARY_BG_PRESS = new Color(140, 160, 190);
    public static final Color BUTTON_SECONDARY_TEXT = new Color(50, 50, 50);
    
    // Danger Button (Xóa)
    public static final Color BUTTON_DANGER_BG = new Color(220, 53, 69);
    public static final Color BUTTON_DANGER_BG_HOVER = new Color(200, 35, 51);
    public static final Color BUTTON_DANGER_BG_PRESS = new Color(180, 20, 36);
    public static final Color BUTTON_DANGER_TEXT = Color.WHITE;
    
    // Success Button (Thêm/Lưu)
    public static final Color BUTTON_SUCCESS_BG = new Color(40, 167, 69);
    public static final Color BUTTON_SUCCESS_BG_HOVER = new Color(33, 136, 56);
    public static final Color BUTTON_SUCCESS_BG_PRESS = new Color(26, 110, 44);
    public static final Color BUTTON_SUCCESS_TEXT = Color.WHITE;
    
    // Info Button (Sửa)
    public static final Color BUTTON_INFO_BG = PRIMARY;
    public static final Color BUTTON_INFO_BG_HOVER = PRIMARY_LIGHT;
    public static final Color BUTTON_INFO_BG_PRESS = PRIMARY_DARK;
    public static final Color BUTTON_INFO_TEXT = Color.WHITE;
    
    // Settings Button
    public static final Color BUTTON_SETTINGS_BG = new Color(150, 150, 150);
    
    // ====== STATUS MESSAGES ======
    public static final Color STATUS_SUCCESS = new Color(40, 167, 69);
    public static final Color STATUS_ERROR = new Color(220, 53, 69);
    public static final Color STATUS_WARNING = new Color(255, 193, 7);
    public static final Color STATUS_INFO = new Color(23, 162, 184);
    
    // ====== SHADOW & EFFECTS ======
    public static final Color SHADOW = new Color(0, 0, 0, 30);
    
    // ====== FONTS ======
    public static final String FONT_FAMILY = "Segoe UI";
    public static final String FONT_FAMILY_HEADING = "Segoe UI";
    public static final int FONT_SIZE_TITLE = 28;
    public static final int FONT_SIZE_SUBTITLE = 14;
    public static final int FONT_SIZE_LABEL = 12;
    public static final int FONT_SIZE_INPUT = 13;
    public static final int FONT_SIZE_SMALL = 11;
    public static final int FONT_SIZE_BUTTON = 13;
    
    // ====== HELPER METHODS ======
    
    /**
     * Tạo Font với kích thước và kiểu cụ thể
     */
    public static Font getFont(int size, int style) {
        return new Font(FONT_FAMILY, style, size);
    }
    
    /**
     * Tạo Font với kích thước, kiểu và font family cụ thể
     */
    public static Font getFont(String fontFamily, int size, int style) {
        return new Font(fontFamily, style, size);
    }
    
    /**
     * Làm nhạt màu để tạo hover effect
     */
    public static Color lighten(Color color, float amount) {
        int r = Math.min(255, (int) (color.getRed() + (255 - color.getRed()) * amount));
        int g = Math.min(255, (int) (color.getGreen() + (255 - color.getGreen()) * amount));
        int b = Math.min(255, (int) (color.getBlue() + (255 - color.getBlue()) * amount));
        return new Color(r, g, b, color.getAlpha());
    }
}
