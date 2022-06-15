import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;

public class Map {
    private ArrayList<Room> rooms;
    private int mapSize;
    private int numberOfCustomRooms;

    Map(int x, int y, ArrayList<Entity> entities, int mapSize) {
        rooms = new ArrayList<Room>();

        loadMapFile(entities, x, y, mapSize);
    }

    public void loadMapFile(ArrayList<Entity> entities, int x, int y, int mapSize) {
        File mapFile = new File("");
        Scanner mapScanner = new Scanner("");
        try {
            mapFile = new File("rooms.txt");
            mapScanner = new Scanner(mapFile);
        } catch (Exception e) {
            System.out.println("File could not be found");
        }

        mapScanner = loadRoom("rooms.txt", 0);
        while (mapScanner.hasNext()) {
            if (mapScanner.nextLine().equals("---")) {
                numberOfCustomRooms++;
            }
        }

        int xPos = x;
        int yPos = y;
        int lineNum = 0;
        int startX = -1;
        int startY = -1;
        ArrayList<Integer> exitX = new ArrayList<Integer>();
        ArrayList<Integer> exitY = new ArrayList<Integer>();
        // int exitX = -1;
        // int exitY = -1;
        int roomLength = 0;
        int roomWidth = 0;
        int roomsSpawned = 0;
        String[] wallTypes = { "tiledWall", "woodenWall" };

        while (roomsSpawned < mapSize) {
            roomsSpawned++;

            int roomToSpawn = randint(0, numberOfCustomRooms - 1);
            String wallTypeToSpawn = wallTypes[randint(0, wallTypes.length - 1)];
            // Set the map scanner to the position where the room is in the file
            boolean roomFits = false;
            boolean loadRoom = true;
            int timesTriedToLoad = 0;
            // Create a default room
            rooms.add(new Room());
            do {
                mapScanner = loadRoom("rooms.txt", roomToSpawn);
                loadRoom = true;
                while (loadRoom) {
                    String text = mapScanner.nextLine();
                    if (text.equals("---")) {
                        loadRoom = false;
                    } else {
                        roomLength = text.length() * 50;
                        for (int i = 0; i < text.length(); i++) {
                            String subsection = text.substring(i, i + 1);
                            if (subsection.equals("S")) {
                                startX = xPos + i * 50;
                                startY = lineNum * 50;
                            }
                        }
                        yPos += 50;
                        lineNum++;
                    }
                    roomWidth = lineNum * 50;
                }

                mapScanner = loadRoom("rooms.txt", roomToSpawn);
                loadRoom = true;
                lineNum = 0;
                if (!exitX.isEmpty() && !exitY.isEmpty()) {
                    yPos = exitY.get(0) - startY;
                    xPos = exitX.get(0) + 50;
                } else {
                    xPos = x;
                    yPos = y;
                }
                // Adjust last room
                rooms.get(rooms.size() - 1).setX(xPos);
                rooms.get(rooms.size() - 1).setY(yPos);
                rooms.get(rooms.size() - 1).setLength(roomLength);
                rooms.get(rooms.size() - 1).setWidth(roomWidth);

                if (rooms.get(rooms.size() - 1).overlaps(rooms)) {
                    roomFits = false;
                    // select a different room if it doesnt fit
                    roomToSpawn = randint(0, numberOfCustomRooms - 1);
                    timesTriedToLoad++;
                } else {
                    roomFits = true;
                    // remove the exit locations when the room is successfully built
                    if (!exitX.isEmpty()) {
                        exitX.remove(0);
                        exitY.remove(0);
                    }
                }
                // check if the new room clips any other room
            } while (!roomFits && timesTriedToLoad < 100);

            if (timesTriedToLoad == 100) {
                entities.add(new Wall(exitX.get(0), exitY.get(0), 50, 50, wallTypeToSpawn));
                entities.add(new Wall(exitX.get(0), exitY.get(0) + 50, 50, 50, wallTypeToSpawn));
                removeEntityAt(exitX.get(0) + 10, exitY.get(0), entities);
                removeEntityAt(exitX.get(0) + 10, exitY.get(0) + 50, entities);
                exitX.remove(0);
                exitY.remove(0);
                rooms.remove(rooms.size() - 1);
                loadRoom = false;
                System.out.println("Triggered wall");
            }

            while (loadRoom) {
                String text = mapScanner.nextLine();
                if (text.equals("---")) {
                    loadRoom = false;
                } else {
                    for (int i = 0; i < text.length(); i++) {
                        String subsection = text.substring(i, i + 1);
                        if (subsection.equals("#")) {
                            // this part extends a block instead of adding another
                            if (entities.get(entities.size() - 1).getY() == yPos &&
                                    (xPos + i * 50) - (entities.get(entities.size() - 1).getX()
                                            + entities.get(entities.size() - 1).getLength()) == 0) {
                                entities.get(entities.size() - 1)
                                        .setLength(entities.get(entities.size() - 1).getLength() + 50);
                            } else {
                                entities.add(new Wall(xPos + i * 50, yPos, 50, 50, wallTypeToSpawn));
                            }
                        } else if (subsection.equals("|")) {
                            entities.add(new Door(xPos + i * 50 + 10, yPos, 30, 50, ""));
                        } else if (subsection.equals("-")) {
                            entities.add(new Platform(xPos + i * 50 + 10, yPos + 20, 30, 10, ""));
                        } else if (subsection.equals("B")) {
                            entities.add(new Box(xPos + i * 50, yPos, 50, 50, "box", 10));
                        } else if (subsection.equals("S")) {
                            entities.add(new Door(xPos + i * 50 + 10, yPos, 30, 50, ""));
                        } else if (subsection.equals("E")) {
                            entities.add(new Door(xPos + i * 50 + 10, yPos, 30, 50, ""));
                            exitX.add(xPos + i * 50);
                            exitY.add(yPos);
                        }
                    }
                    yPos += 50;
                }
            }

            if (roomsSpawned != 1) {
                addEnemy(rooms.get(rooms.size() - 1), entities);
                addEnemy(rooms.get(rooms.size() - 1), entities);
            }
        }

        mapScanner.close();
    }

    private boolean removeEntityAt(int x, int y, ArrayList<Entity> entities) {
        for (int i = entities.size() - 1; i >= 0; i--) {
            if (entities.get(i).getX() == x && entities.get(i).getY() == y) {
                entities.remove(i);
                return true;
            }
        }
        return false;
    }

    public void addEnemy(Room room, ArrayList<Entity> entities) {
        int spawnX = randint(room.getX(), room.getX() + room.getLength() - 10);
        int spawnY = randint(room.getY(), room.getY() + room.getWidth() - 10);
        entities.add(new Guard(spawnX, spawnY, 34, 44, "Guard/"));

        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < entities.size(); i++) {
                if (rectRectDetect(entities.get(entities.size() - 1),
                        entities.get(i)) && entities.size() - 1 != i) {
                    entities.get(entities.size() - 1).setX(randint(room.getX(), room.getX() + room.getLength() - 10));
                    entities.get(entities.size() - 1).setY(randint(room.getY(), room.getY() + room.getWidth() - 10));
                    changed = true;
                }
            }
        }
    }

    public Scanner loadRoom(String fileName, int roomNumber) {
        Scanner mapScanner = new Scanner("");
        File mapFile;
        try {
            mapFile = new File(fileName);
            mapScanner = new Scanner(mapFile);
        } catch (Exception e) {
            System.out.println("File could not be found");
        }

        int roomsPassed = 0;
        while (roomsPassed < roomNumber) {
            String text = mapScanner.nextLine();
            if (text.equals("---")) {
                roomsPassed++;
            }
        }
        return mapScanner;
    }

    public boolean rectRectDetect(Entity rect, Entity rect2) {
        double leftSide = rect.getX();
        double rightSide = rect.getX() + rect.getLength();
        double topSide = rect.getY();
        double botSide = rect.getY() + rect.getWidth();
        if (rect2.getX() + rect2.getLength() > leftSide && rect2.getX() < rightSide &&
                rect2.getY() + rect2.getWidth() > topSide
                && rect2.getY() < botSide) {
            return true;
        }
        return false;
    }

    public int randint(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    public ArrayList<Room> getRooms() {
        return this.rooms;
    }
}
