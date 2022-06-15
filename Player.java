import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;

public class Player extends Creature {

    Player(int x, int y, int length, int width, String picName) {
        super(x, y, length, width, picName);
        super.setType("Player");
        super.setGravity(0.3);
        super.setRunAccel(0.6);
        super.setJumpSpeed(10);
        super.setTeam(0);
    }

    @Override
    public void draw(Graphics g, int xRange, int yRange) {
        super.draw(g, xRange, yRange);
        if (super.getInteractingWith() != null) {
            drawShop(g, xRange, yRange);
        }
    }

    public void drawShop(Graphics g, int xRange, int yRange) {
        g.setColor(new Color(51, 62, 181));
        g.fillRect(190, 390, Const.WIDTH - 380, Const.HEIGHT - 430);
        g.setColor(new Color(78, 171, 237));
        g.fillRect(200, 400, Const.WIDTH - 400, Const.HEIGHT - 450);
        for (int i = 0; i < super.getInteractingWith().getItems().size(); i++) {
            super.getInteractingWith().getItems().get(i).draw(g);
        }
    }

    @Override
    public void attack(int aimX, int aimY, ArrayList<Entity> entities, ArrayList<Bullet> bullets) {
        if (super.getCanAttack()) {
            bullets.add(new Slash(this.getX() + this.getLength() / 2,
                    this.getY() + this.getWidth() / 2,
                    aimX, aimY, 100, 5,
                    this.getTeam(),
                    10, 100, false, "slash"));
            super.setCanAttack(false);
            super.setLastAttack(System.currentTimeMillis());
        }
    }

    @Override
    public void dash(double travelX, double travelY, ArrayList<Entity> entities, ArrayList<Bullet> bullets) {
        if (super.getCanAttack()) {
            double startX = this.getX();
            double startY = this.getY();
            double aimX = travelX - this.getX() - this.getLength() / 2;
            double aimY = travelY - this.getY() - this.getWidth() / 2;
            boolean stop = false;
            while (super.distance(startX, startY, this.getX() + this.getLength() / 2,
                    this.getY() + this.getWidth() / 2) < 1000
                    && !stop &&
                    super.distance(travelX, travelY, this.getX() + this.getLength() / 2,
                            this.getY() + this.getWidth() / 2) > 20) {
                this.setX(this.getX() + 10 * aimX
                        / Math.sqrt(Math.pow(aimX, 2) + Math.pow(aimY, 2)));
                this.setY(this.getY() + 10 * aimY
                        / Math.sqrt(Math.pow(aimX, 2) + Math.pow(aimY, 2)));

                bullets.add(new Bullet(this.getX() + this.getLength() / 2, this.getY() + this.getWidth() / 2, 10,
                        0, 0, 1, this.getTeam(), 100,
                        1, false, "dash"));

                for (int i = 0; i < entities.size(); i++) {
                    if (!this.equals(entities.get(i)) && super.rectRectDetect(this, entities.get(i))
                            && (entities.get(i).getType().equals("Wall") ||
                                    entities.get(i).getType().equals("Door"))) {
                        stop = true;
                        this.setX(this.getX() - 10 * aimX
                                / Math.sqrt(Math.pow(aimX, 2) + Math.pow(aimY, 2)));
                        this.setY(this.getY() - 10 * aimY
                                / Math.sqrt(Math.pow(aimX, 2) + Math.pow(aimY, 2)));
                    }
                }
            }

            super.setCanAttack(false);
            super.setLastAttack(System.currentTimeMillis());
        }
    }
}
