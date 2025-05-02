import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;


// 自定义TabbedPaneUI以区分边框
public class CustomTabbedPaneUI extends BasicTabbedPaneUI {
    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color selectedColorBrighter = new Color(204, 204, 255); // 更亮的颜色
        Color selectedColorDarker = new Color(153, 153, 255);   // 更暗的颜色
        Color unselectedColorLight = Color.WHITE;              // 未选中时的浅色
        Color unselectedColorDark = Color.LIGHT_GRAY;          // 未选中时的深色

        GradientPaint gradient;
        if (isSelected) {
            gradient = new GradientPaint(x, y, selectedColorBrighter, x, y + h, selectedColorDarker);
        } else {
            gradient = new GradientPaint(x, y, unselectedColorLight, x, y + h, unselectedColorDark);
        }

        g2d.setPaint(gradient);
        g2d.fillRect(x, y, w, h);

        g2d.dispose();
    }

    @Override
    protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
        // 不绘制焦点指示器
    }

    @Override
    protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
        // 不绘制内容边框
    }

    @Override
    protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
        // 不绘制标签页区域的边框
    }

    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        // 不绘制单个标签页的边框
    }
}

