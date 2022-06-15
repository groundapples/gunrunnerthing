import java.util.*;

public class Guard extends Enemy {
    Guard(int x, int y, int length, int width, String picName) {
        super(x, y, length, width, picName);
        super.setGravity(0.2);
        super.setDetectRange(500);
        super.setEngageRange(300);
        super.setRunAccel(0.6);
        super.setJumpSpeed(5);
        super.setTeam(1);
    }

    public void attack(ArrayList<Entity> entities, ArrayList<Bullet> bullets) {
        bullets.add(new Bullet(super.getX() + super.getLength() / 2, super.getY() + super.getWidth() / 2,
                super.getDestinationX(), super.getDestinationY(), 10, 20, super.getTeam(), 100, 5000, true,
                "bullet"));
    }

    @Override
    public void update(ArrayList<Entity> entities, ArrayList<Bullet> bullets, SlowmoTracker slowmoTracker) {
        super.updateDestination(entities);
        super.update(entities, bullets, slowmoTracker);
        super.search(entities, bullets);
    }
}
