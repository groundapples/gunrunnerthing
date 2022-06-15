import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class Animation {
    JFrame gameWindow;
    GraphicsPanel canvas;
    ArrayList<Entity> entities = new ArrayList<Entity>();
    ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    Camera camera = new Camera(0);
    Map map;
    SlowmoTracker slowmoTracker;
    MovementKeyListener keyListener;
    BasicMouseListener mouseListener;

    Animation() {
        gameWindow = new JFrame("Game Window");
        gameWindow.setSize(Const.WIDTH, Const.HEIGHT);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas = new GraphicsPanel();
        gameWindow.add(canvas);

        keyListener = new MovementKeyListener();
        canvas.addKeyListener(keyListener);

        mouseListener = new BasicMouseListener();
        canvas.addMouseListener(mouseListener);

        entities.add(new Player(300, 160, 34, 44, "Player/"));
        // entities.add(new Guard(300, 100, 60, 60, "testAnimation/girl", 4, 8));

        entities.add(new Shop(500, 160, 100, 100, "shop", 3));

        map = new Map(0, 0, entities, 2);
        slowmoTracker = new SlowmoTracker(5000, 0.1);

        gameWindow.setVisible(true);
    }

    // ------------------------------------------------------------------------------
    public void runGameLoop() {
        while (true) {
            // 1. and 2. Clear the game window and draw everything
            gameWindow.repaint();
            // 3. Wait enough time, so human eye can perceive the drawing
            try {
                Thread.sleep(Const.FRAME_PERIOD);
            } catch (Exception e) {
            }

            // any animations after
            for (int i = 0; i < bullets.size(); i++) {
                bullets.get(i).update(entities, bullets, slowmoTracker);
            }

            for (int i = 0; i < entities.size(); i++) {
                if (entities.get(i).checkInRange(camera.getXRange(), camera.getYRange())) {
                    entities.get(i).update(entities, bullets, slowmoTracker);
                }
            }
            camera.update(entities);

        } // 5. Repeat
    }

    // ------------------------------------------------------------------------------
    public class GraphicsPanel extends JPanel {
        public GraphicsPanel() {
            setFocusable(true);
            requestFocusInWindow();
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < map.getRooms().size(); i++) {
                map.getRooms().get(i).draw(g, camera.getXRange(), camera.getYRange());
            }

            for (int i = entities.size() - 1; i >= 0; i--) {
                entities.get(i).draw(g, camera.getXRange(), camera.getYRange());
            }

            for (int i = bullets.size() - 1; i >= 0; i--) {
                bullets.get(i).draw(g, camera.getXRange(), camera.getYRange());
            }
        }
    }

    // ------------------------------------------------------------------------------
    public class BasicMouseListener implements MouseListener {
        public void mouseClicked(MouseEvent e) { // moves the box at the mouse location
        }

        public void mousePressed(MouseEvent e) { // MUST be implemented even if not used!
            // bullets.add(new Bullet(entities.get(0).getX() + entities.get(0).getLength() /
            // 2,
            // entities.get(0).getY() + entities.get(0).getWidth() / 2,
            // e.getX() + camera.getXRange(), e.getY() + camera.getYRange(), 10, 5,
            // entities.get(0).getTeam(),
            // 100, 2000, true, "bullet.png"));
            entities.get(0).attack(e.getX() + camera.getXRange(),
                    e.getY() + camera.getYRange(), entities, bullets);
        }

        public void mouseReleased(MouseEvent e) { // MUST be implemented even if not used!
        }

        public void mouseEntered(MouseEvent e) { // MUST be implemented even if not used!
        }

        public void mouseExited(MouseEvent e) { // MUST be implemented even if not used!
        }
    }

    // ---------------------------------------------------------------------------------
    public class MovementKeyListener implements KeyListener {
        public void keyPressed(KeyEvent e) {
            char keyChar = e.getKeyChar();
            if (keyChar == 'a') {
                entities.get(0).move("left");
            } else if (keyChar == 'd') {
                entities.get(0).move("right");
            } else if (keyChar == 'w') {
                entities.get(0).jump();
            } else if (keyChar == 'e') {
                entities.get(0).checkInteract(entities);
            }

            int key = e.getKeyCode();
            if (key == 16) {
                slowmoTracker.activateSlow();
            } else if (key == 32) {
                Point p = MouseInfo.getPointerInfo().getLocation();
                entities.get(0).dash(p.getX() + camera.getXRange(),
                        p.getY() + camera.getYRange(), entities, bullets);
            }
        }

        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_ESCAPE) {
                gameWindow.dispose();
            } else if (key == KeyEvent.VK_A && entities.get(0).getDirection().equals("left")) {
                entities.get(0).move("none");
            } else if (key == KeyEvent.VK_D && entities.get(0).getDirection().equals("right")) {
                entities.get(0).move("none");
            }
        }

        public void keyTyped(KeyEvent e) {
        }
    }

    // -----------------------------------------------
    public static void main(String[] args) {
        Animation game = new Animation();
        game.runGameLoop();
    }

    public static int randInt(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }
}
