import java.util.*;
import java.awt.Color;

public class Blood extends Particle {
    Blood(int x, int y, double xSpeed, double ySpeed, Color color) {
        super(x, y, 10, 10, "");
        super.setType("Blood");
        super.setXSpeed(xSpeed);
        super.setYSpeed(ySpeed);
        super.setTeam(-1);
        super.setDuration(1000);
        super.setColor(color);
        super.setGravity(0.2);
        super.setTouchable(false);
    }

    @Override
    public void update(ArrayList<Entity> entities, ArrayList<Bullet> bullets, SlowmoTracker slowmoTracker) {

        super.update(entities, bullets, slowmoTracker);

        super.setX((super.getX() + super.getXSpeed() * slowmoTracker.getActiveSlowAmount()));
        for (int i = 0; i < entities.size(); i++) {
            if (!this.equals(entities.get(i)) && super.rectRectDetect(this, entities.get(i))
                    && entities.get(i).getTouchable()) {
                // entities.remove(this);
                super.setX((super.getX() - super.getXSpeed() *
                        slowmoTracker.getActiveSlowAmount()));
                super.setXSpeed(0);
            }
        }

        if (this != null) {
            super.setY((super.getY() + super.getYSpeed() * slowmoTracker.getActiveSlowAmount()));
            for (int i = 0; i < entities.size(); i++) {
                if (!this.equals(entities.get(i)) && super.rectRectDetect(this, entities.get(i))
                        && entities.get(i).getTouchable()) {
                    // entities.remove(this);
                    super.setY((super.getY() - super.getYSpeed() *
                            slowmoTracker.getActiveSlowAmount()));
                    super.setYSpeed(0);
                }
            }
        }
    }
}
