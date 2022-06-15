import java.util.*;
import java.awt.Color;

public class Box extends Wall {
    private int hp;
    private int maxHp;

    Box(int x, int y, int length, int width, String picName, int hp) {
        super(x, y, length, width, picName);
        super.setGravity(0.2);
        this.hp = hp;
        this.maxHp = hp;
    }

    @Override
    public void takeDamage(int damage) {
        this.hp -= damage;
    }

    @Override
    public void update(ArrayList<Entity> entities, ArrayList<Bullet> bullets, SlowmoTracker slowmoTracker) {
        super.update(entities, bullets, slowmoTracker);
        super.setY((super.getY() + super.getYSpeed() * slowmoTracker.getActiveSlowAmount()));
        for (int i = 0; i < entities.size(); i++) {
            if (!this.equals(entities.get(i)) && super.rectRectDetect(this, entities.get(i))
                    && entities.get(i).getTouchable()) {
                super.setY((super.getY() - super.getYSpeed() * slowmoTracker.getActiveSlowAmount()));
                super.setYSpeed(0);
            }
        }

        if (this.hp <= 0) {
            for (int i = 0; i < 10; i++) {
                entities.add(new Blood((int) this.getX() + this.getLength() / 2,
                        (int) this.getY() + this.getWidth() / 2,
                        randint(-20, 20), randint(-30, 0), new Color(150, 75, 0)));
            }
            entities.remove(this);
        }
    }
}
