import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;

public class Shop extends Wall {
    private ArrayList<Item> items;

    Shop(int x, int y, int length, int width, String picName, int numberOfItemsOnSale) {
        super(x, y, length, width, picName);
        super.setTouchable(false);
        items = new ArrayList<Item>();
        loadShopItems(numberOfItemsOnSale);
    }

    public void loadShopItems(int numberOfItemsOnSale) {
        File shopFile = new File("");
        Scanner shopScanner = new Scanner("");
        try {
            shopFile = new File("items.txt");
            shopScanner = new Scanner(shopFile);
        } catch (Exception e) {
            System.out.println("File could not be found");
        }

        int numberOfCustomItems = 0;
        shopScanner = loadItem("items.txt", 0);
        while (shopScanner.hasNext()) {
            if (shopScanner.nextLine().equals("---")) {
                numberOfCustomItems++;
            }
        }

        for (int i = 0; i < numberOfItemsOnSale; i++) {
            int randomItem = randint(0, numberOfCustomItems - 1);
            shopScanner = loadItem("items.txt", randomItem);
            String itemName = shopScanner.next();
            if (itemName.equals("Boots")) {
                items.add(new Boots(300 + i * 280, 450, 50, 50));
            }
        }
    }

    @Override
    public void draw(Graphics g, int xRange, int yRange) {
        if (super.checkInRange(xRange, yRange)) {
            // walls are static, only have 1 image
            g.drawImage(getImage(), (int) super.getX() - xRange, (int) super.getY() - yRange,
                    null);
        }

    }

    @Override
    public void interact(Entity interactor) {
        if (interactor.getInteractingWith() == null) {
            interactor.setInteractingWith(this);
        }
    }

    @Override
    public void update(ArrayList<Entity> entities, ArrayList<Bullet> bullets, SlowmoTracker slowmoTracker) {
        if (distance(getX() + getLength() / 2, getY() + getWidth() / 2,
                entities.get(0).getX() + entities.get(0).getLength() / 2,
                entities.get(0).getY() + entities.get(0).getWidth() / 2) > 200) {
            entities.get(0).setInteractingWith(null);
        }
        super.update(entities, bullets, slowmoTracker);
    }

    public Scanner loadItem(String fileName, int itemNumber) {
        File shopFile = new File("");
        Scanner shopScanner = new Scanner("");
        try {
            shopFile = new File(fileName);
            shopScanner = new Scanner(shopFile);
        } catch (Exception e) {
            System.out.println("File could not be found");
        }
        int itemsPassed = 0;
        while (itemsPassed < itemNumber) {
            String text = shopScanner.nextLine();
            if (text.equals("---")) {
                itemsPassed++;
            }
        }
        return shopScanner;
    }

    @Override
    public ArrayList<Item> getItems() {
        return this.items;
    }
}
