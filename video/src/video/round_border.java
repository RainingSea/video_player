package video;

import java.awt.BasicStroke;

import java.awt.Color;

import java.awt.Component;

import java.awt.Graphics;

import java.awt.Graphics2D;

import java.awt.Insets;

import java.awt.Rectangle;

import java.awt.RenderingHints;

import java.awt.geom.Area;

import java.awt.geom.RoundRectangle2D;

import javax.swing.JLabel;

import javax.swing.JOptionPane;

import javax.swing.border.AbstractBorder;

public class round_border {
    public static void main(String[] args) {
        JLabel l = new JLabel("Text");
        l.setBorder(new ThreeDimensionalBorder(Color.BLACK, 400, 5));
        JOptionPane.showMessageDialog(null, l);
    }
}

class ThreeDimensionalBorder extends AbstractBorder {
    Color color;
    int thickness = 3;
    int radius = 8;
    Insets insets = null;
    BasicStroke stroke = null;
    int strokePad;
    RenderingHints hints;
    int shadowPad ;

    ThreeDimensionalBorder(Color color, int transparency, int shadowWidth) {
        this.color = color;
//        shadowPad = shadowWidth;
        shadowPad=0;
        stroke = new BasicStroke(thickness);
        strokePad = thickness / 2;
        hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int pad = radius + strokePad;
        int bottomPad = pad + strokePad + shadowPad;
        int rightPad = pad + strokePad + shadowPad;
        insets = new Insets(pad, pad, bottomPad + shadowPad, rightPad);
    }
    @Override

    public Insets getBorderInsets(Component c) {
        return insets;
    }

    @Override

    public Insets getBorderInsets(Component c, Insets insets) {
        return getBorderInsets(c);

    }

    @Override

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        int bottomLineY = height - thickness - shadowPad;
        RoundRectangle2D.Double bubble = new RoundRectangle2D.Double(0 + strokePad,

                0+ strokePad, width - thickness - shadowPad, bottomLineY, radius, radius);

        Area area = new Area(bubble);
        g2.setRenderingHints(hints);
        g2.setColor(color);
        g2.setStroke(stroke);
        g2.draw(area);
        g2.translate(shadowPad, shadowPad);
        g2.draw(area);
    }

}

