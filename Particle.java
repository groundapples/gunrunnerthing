import java.util.*;

public class Particle extends Entity {
    private long creationTime;
    private double duration;

    Particle(int x, int y, int length, int width, String picName) {
        super(x, y, length, width, picName);
        super.setType("Particle");
        super.setTeam(-1);
        this.creationTime = System.currentTimeMillis();
        this.duration = 0;
    }

    @Override
    public void update(ArrayList<Entity> entities, ArrayList<Bullet> bullets, SlowmoTracker slowmoTracker) {
        this.setX(this.getX() + this.getXSpeed() * slowmoTracker.getActiveSlowAmount());
        this.setY(this.getY() + this.getYSpeed() * slowmoTracker.getActiveSlowAmount());
        super.update(entities, bullets, slowmoTracker);
        if ((System.currentTimeMillis() - creationTime) * slowmoTracker.getActiveSlowAmount() > duration) {
            entities.remove(this);
        }
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
