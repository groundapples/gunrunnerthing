import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class Room {
    private int id;
    private int x, y, buildX, buildY;
    private int length, width;
    private ArrayList<Connector> connectors;

    Room(int id, int x, int y, int length, int width) {
        this.x = x;
        this.y = y;
        this.buildX = x;
        this.buildY = y;
        this.length = length;
        this.width = width;
        this.id = id;
    }

    Room() {
        this.x = 0;
        this.y = 0;
        this.buildX = 0;
        this.buildY = 0;
        this.length = 0;
        this.width = 0;
        this.id = 0;
    }

    public void draw(Graphics g, int xRange, int yRange) {
        g.setColor(Color.GRAY);
        g.fillRect((int) this.x - xRange, (int) this.y - yRange, this.length, this.width);
    }

    public boolean overlaps(ArrayList<Room> rooms) {
        for (int i = 0; i < rooms.size(); i++) {
            if (this != rooms.get(i) && rectRectDetect(rooms.get(i), this)) {
                return true;
            }
        }
        return false;
    }

    public boolean rectRectDetect(Room rect, Room rect2) {
        double leftSide = rect.x;
        double rightSide = rect.x + rect.length;
        double topSide = rect.y;
        double botSide = rect.y + rect.width;
        if (rect2.x + rect2.length > leftSide && rect2.x < rightSide && rect2.y + rect2.width > topSide
                && rect2.y < botSide) {
            return true;
        }
        return false;
    }

    public static int randint(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    // getters
    public int getLength() {
        return this.length;
    }

    public int getWidth() {
        return this.width;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    // setters
    public void setLength(int length) {
        this.length = length;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
