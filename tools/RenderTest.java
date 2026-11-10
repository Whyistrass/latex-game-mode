import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class RenderTest {
    public static void main(String[] args) throws Exception {
        String latex = args.length > 0 ? args[0] : "\\frac{dx}{dt} = Ax + Bu";
        TeXFormula formula = new TeXFormula(latex);
        TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 48.0F);
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setComposite(AlphaComposite.Clear);
        graphics.fillRect(0, 0, width, height);
        graphics.setComposite(AlphaComposite.SrcOver);
        icon.setForeground(Color.BLACK);
        icon.paintIcon(null, graphics, 0, 0);
        graphics.dispose();

        BufferedImage preview = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D previewGraphics = preview.createGraphics();
        previewGraphics.setColor(new Color(242, 230, 201));
        previewGraphics.fillRect(0, 0, width, height);
        previewGraphics.drawImage(image, 0, 0, null);
        previewGraphics.dispose();

        ImageIO.write(preview, "png", new File(".tools/test_formula.png"));
        System.out.println("size=" + width + "x" + height);
    }
}
