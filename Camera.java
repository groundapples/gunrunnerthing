import java.util.*;

public class Camera {
    private int xRange;
    private int yRange;
    private int focusIndex;

    Camera(int focusIndex) {
        xRange = 0;
        yRange = 0;
        this.focusIndex = focusIndex;
    }

    public void update(ArrayList<Entity> entities) {
        if (entities.get(focusIndex).getType().equals("Player")) {
            xRange = (int) (entities.get(focusIndex).getX() + entities.get(focusIndex).getLength() / 2
                    - Const.WIDTH / 2);
            yRange = (int) (entities.get(focusIndex).getY() + entities.get(focusIndex).getWidth() / 2
                    - Const.HEIGHT / 2);

        }
    }

    public int getXRange() {
        return xRange;
    }

    public int getYRange() {
        return yRange;
    }
}
