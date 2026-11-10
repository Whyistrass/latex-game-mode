import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class GenTexture {
    public static void main(String[] args) throws Exception {
        int size = 16;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        Color woodDark = new Color(63, 42, 23);
        Color wood = new Color(107, 69, 38);
        Color woodLight = new Color(134, 90, 52);
        Color paper = new Color(242, 230, 201);
        Color paperLine = new Color(224, 210, 179);

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int edge = Math.min(Math.min(x, y), Math.min(size - 1 - x, size - 1 - y));
                Color color;
                if (edge == 0) {
                    color = woodDark;
                } else if (edge == 1) {
                    color = (x + y) % 3 == 0 ? woodLight : wood;
                } else if (edge == 2) {
                    color = paperLine;
                } else {
                    color = paper;
                }
                g.setColor(color);
                g.fillRect(x, y, 1, 1);
            }
        }
        g.dispose();

        File out = new File("src/main/resources/assets/latexboard/textures/block/latex_board.png");
        out.getParentFile().mkdirs();
        ImageIO.write(image, "png", out);
        System.out.println("wrote " + out.getPath());
    }
}
