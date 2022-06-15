import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.io.File;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
// possible exceptions
import java.io.IOException;

public class Bullet {
    private double x;
    private double y;
    private double aimX, aimY;
    private double startX, startY;
    private double angle;
    private int r;
    private double speed;
    private int team;
    private int damage;
    private int bulletRange;
    private boolean isRemovedOnHit;
    private BufferedImage original;
    private BufferedImage picture;

    Bullet(double x, double y, double aimX, double aimY, int r, double speed, int team,
            int damage, int bulletRange, boolean isRemovedOnHit, String picName) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.aimX = aimX - x;
        this.aimY = aimY - y;
        this.angle = getAngle();
        this.r = r;
        this.speed = speed;
        this.team = team;
        this.damage = damage;
        this.bulletRange = bulletRange;
        this.isRemovedOnHit = isRemovedOnHit;

        try {
            this.original = ImageIO.read(new File("images/" + picName + ".png"));
        } catch (IOException ex) {
            System.out.println("Image not found");
        }
        this.picture = rotateImage(this.original, this.angle);
    }

    public void update(ArrayList<Entity> entities, ArrayList<Bullet> bullets, SlowmoTracker slowmoTracker) {
        this.x = this.x + this.speed * slowmoTracker.getActiveSlowAmount() * aimX
                / Math.sqrt(Math.pow(this.aimX, 2) + Math.pow(this.aimY, 2));
        this.y = this.y + this.speed * slowmoTracker.getActiveSlowAmount() * this.aimY
                / Math.sqrt(Math.pow(this.aimX, 2) + Math.pow(this.aimY, 2));

        for (int i = 0; i < entities.size() && this != null; i++) {
            if (circRectDetect(this, entities.get(i)) && this.team != entities.get(i).getTeam()) {
                entities.get(i).takeDamage(this.damage);
                if (this.isRemovedOnHit) {
                    bullets.remove(this);
                }
            }
        }

        if (this != null) {
            if (distance(this.x, this.y, this.startX, this.startY) > this.bulletRange) {
                bullets.remove(this);
            }
        }

    }

    public double getAngle() {
        // v1 moving object
        double boxX = this.x + this.r / 2;
        double boxY = this.y + this.r / 2;

        // v2 user touch
        double touchX = this.aimX + x;
        double touchY = this.aimY + y;

        double theta = 180.0 / Math.PI * Math.atan2(boxX - touchX, boxY - touchY);

        return theta;
    }

    public void draw(Graphics g, int xRange, int yRange) {
        g.drawImage(this.picture, (int) this.x - xRange - this.r / 2, (int) this.y - yRange - this.r / 2, null);
        // g.setColor(Color.black);
        // g.fillOval((int) this.x - xRange - this.r / 2, (int) this.y - yRange - this.r
        // / 2, this.r, this.r);
    }

    public boolean circRectDetect(Bullet circle, Entity rect) {
        double leftSide = rect.getX();
        double rightSide = rect.getX() + rect.getLength();
        double topSide = rect.getY();
        double botSide = rect.getY() + rect.getWidth();
        if (circle.x + circle.r / 2 > leftSide && circle.x - circle.r / 2 < rightSide
                && circle.y + circle.r / 2 > topSide
                && circle.y - circle.r / 2 < botSide) {
            return true;
        }
        return false;
    }

    public boolean circCircDetect(Bullet circle, Bullet circle2) {
        if (distance(circle.x, circle.y, circle2.x, circle2.y) < circle.r / 2 + circle2.r / 2) {
            return true;
        }
        return false;
    }

    public double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }

    private BufferedImage copyImage(BufferedImage image) {
        BufferedImage copiedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g2d = copiedImage.createGraphics();
        g2d.drawImage(image, null, 0, 0);
        g2d.dispose();
        return copiedImage;
    }

    private BufferedImage rotateImage(BufferedImage image, double angle) {
        BufferedImage rotatedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g2d = rotatedImage.createGraphics();
        g2d.rotate(Math.toRadians(-this.angle), image.getWidth() / 2, image.getHeight() / 2);
        g2d.drawImage(image, null, 0, 0);
        g2d.dispose();
        return rotatedImage;
    }

    // setters
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public void setAim(double x, double y) {
        this.aimX = x;
        this.aimY = y;
        this.angle = getAngle();
        this.picture = rotateImage(this.original, this.angle);
    }

    // getters
    public double getSpeed() {
        return this.speed;
    }

    public int getTeam() {
        return this.team;
    }

    public double getAimX() {
        return aimX;
    }

    public double getAimY() {
        return aimY;
    }
}
