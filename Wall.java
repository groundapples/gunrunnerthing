import java.awt.Graphics;
import java.awt.Color;
import java.io.File;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
// possible exceptions
import java.io.IOException;

public class Wall extends Entity {
    private BufferedImage image;

    Wall(int x, int y, int length, int width, String picName) {
        super(x, y, length, width, picName);
        super.setType("Wall");
        super.setTeam(-1);
    }

    @Override
    public void draw(Graphics g, int xRange, int yRange) {
        if (super.checkInRange(xRange, yRange)) {
            if (super.getPicName().equals("")) {
                g.setColor(super.getColor());
                g.fillRect((int) super.getX() - xRange, (int) super.getY() - yRange, super.getLength(),
                        super.getWidth());
            } else {
                // walls are static, only have 1 image
                for (int i = 0; i < super.getLength(); i += 50) {
                    g.drawImage(image, (int) super.getX() - xRange + i, (int) super.getY() - yRange,
                            null);
                }
            }
        }

    }

    @Override
    public void loadImages() {
        if (!super.getPicName().equals("")) {
            try {
                image = ImageIO.read(new File("images/" + super.getPicName() + ".png"));
            } catch (IOException ex) {
                System.out.println(ex);
                System.out.println("failed to load wall");
            }
        }
    }

    public BufferedImage getImage() {
        return image;
    }
}
