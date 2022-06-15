import java.util.*;

abstract class Enemy extends Creature {
    private double targetDestinationX = 0;
    private double targetDestinationY = 0;
    private int detectRange = 0;
    private int engageRange = 0;

    Enemy(int x, int y, int length, int width, String picName) {
        super(x, y, length, width, picName);
        super.setType("Enemy");
    }

    abstract void attack(ArrayList<Entity> entities, ArrayList<Bullet> bullets);

    // abstract void idleMovement();

    public void updateDestination(ArrayList<Entity> entities) {
        if (distance(super.getX(), super.getY(), entities.get(0).getX(),
                entities.get(0).getY()) < detectRange) {
            setDestinationX(entities.get(0).getX() + entities.get(0).getLength() / 2);
            setDestinationY(entities.get(0).getY() + entities.get(0).getWidth() / 2);
        }
    }

    public void search(ArrayList<Entity> entities, ArrayList<Bullet> bullets) {
        if (this.getDestinationX() != 0 && this.getDestinationY() != 0) {
            if (super.distance(super.getX() + super.getLength() / 2, super.getY() + super.getWidth() / 2,
                    this.getDestinationX(), this.getDestinationY()) < this.getEngageRange()) {
                // DO SOMETHING HERE! Attack?
                if (super.getCanAttack()) {
                    attack(entities, bullets);
                    super.setCanAttack(false);
                    super.setLastAttack(System.currentTimeMillis());
                }
                super.setXAccel(0);
                super.setYAccel(0);
            } else if (super.getXSpeed() == 0 && super.getCanJump()) {
                super.setYAccel(-super.getJumpSpeed());
                super.setCanJump(false);
            } else if (super.getX() + super.getLength() / 2 > this.getDestinationX()) {
                super.setXAccel(-super.getRunAccel());
                super.setYAccel(0);
                super.setDirection("left");
            } else if (super.getX() + super.getLength() / 2 < this.getDestinationX()) {
                super.setXAccel(super.getRunAccel());
                super.setYAccel(0);
                super.setDirection("right");
            }
        }
    }

    // Setters
    public void setDetectRange(int range) {
        this.detectRange = range;
    }

    public void setEngageRange(int range) {
        this.engageRange = range;
    }

    public void setDestinationX(double x) {
        this.targetDestinationX = x;
    }

    public void setDestinationY(double y) {
        this.targetDestinationY = y;
    }

    // Getters
    public double getDestinationX() {
        return this.targetDestinationX;
    }

    public double getDestinationY() {
        return this.targetDestinationY;
    }

    public int getDetectRange() {
        return this.detectRange;
    }

    public int getEngageRange() {
        return this.engageRange;
    }

}
