import javax.swing.border.Border;
import java.awt.*;

class RoundedBorder implements Border {
    private int radius;     // Radius of the rounded corners
    private int thickness;  // Thickness of the border
    private Color color;    // Color of the border

    RoundedBorder(int radius, int thickness, Color color) {
        this.radius = radius;
        this.thickness = thickness;
        this.color = color;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        // Return insets that match the border thickness
        return new Insets(thickness, thickness, thickness, thickness);
    }

    @Override
    public boolean isBorderOpaque() {
        // Indicate that the border is opaque
        return true;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create(); // Create a Graphics2D object

        // Set rendering hints for smooth rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color); // Set the border color
        g2d.setStroke(new BasicStroke(thickness)); // Set the border thickness

        // Calculate the diameter of the circle to be drawn
        int diameter = radius * 2;

        // Draw a rounded rectangle with the specified dimensions
        g2d.drawRoundRect(x, y, width - 1, height - 1, diameter, diameter);

        g2d.dispose(); // Dispose of the Graphics2D object
    }
}
