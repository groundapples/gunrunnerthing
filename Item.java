import java.awt.Graphics;
import java.awt.Color;
import java.io.File;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
// possible exceptions
import java.io.IOException;

public class Item {
    private String name;
    private BufferedImage image;
    private int x, y, length, width;

    Item(String name, int x, int y, int length, int width) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.length = length;
        this.width = width;
        loadImages();
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    public boolean contains(int x, int y) {
        if (x > this.x && x < this.x + this.length &&
                y > this.y && y < this.y + this.width) {
            return true;
        }
        return false;
    }

    public void loadImages() {
        if (!name.equals("")) {
            try {
                image = ImageIO.read(new File("images/items/" + name + ".png"));
            } catch (IOException ex) {
                System.out.println(ex);
                System.out.println("failed to load image");
            }
        }
    }
}
