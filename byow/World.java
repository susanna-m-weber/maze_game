package byow;
import byow.Core.Engine;
import byow.Core.RandomUtils;
//import edu.princeton.cs.algs4.StdDraw;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;

public class World implements Serializable {

    //static final File AVATAR_FILE = Utils.join(SAVE_DIR, "avatar");
    //static final File MONSTER_FILE = Utils.join(SAVE_DIR, "monster");


    private static int WIDTH = 61;
    private static int HEIGHT = 25;
    private static int HUDHEIGHT = 5;
    private TETile[][] world;
    private long seed;
    private Random rand = new Random();
    private TERenderer ter = null;
    private String hudText = "CS 61B: The Game. Reach the exit from the maze. ";
    private String helpText = "WSAD - move Avatar. "
            +
            ":q Save and quit. "
            +
            "b - Toggle lights. "
            +
            "p - show monster path";
    Room wholeRoom;
    Point exitDoor;
    Point portal1;
    Point portal2;
    Avatar a;
    Monster m;
    boolean lightsOn = true;
    boolean showMonsterPath = true;

    boolean waitForQuit = false;


    static final String AVATAR_MOVES = "DAWS"; // Keys to move avatar RIGHT, LEFT, UP, DOWN

    public World(long seed, TERenderer ter) {
        this.seed = seed;
        this.rand.setSeed(seed);
        this.ter = ter;
        //if (ter.width() < WIDTH || ter.height() < HEIGHT + HUDHEIGHT) {
            //System.out.println("StdDraw window too small for world");
        //}
        world = new TETile[WIDTH][HEIGHT];
        initializeWorld();
    }


    private void initializeWorld() {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    public void initSprites() {
        a = new Avatar(this, wholeRoom);
        m = new Monster(this, wholeRoom);
        Point p1 = randomPoint();
        setTile(p1.x, p1.y, Tileset.FLOWER);
        Point p2 = new Point(p1);
        while (p2.equals(p1)) {
            p2 = randomPoint();
        }
        setTile(p2.x, p2.y, Tileset.KEY);
        portal1 = randomPoint();
        setTile(portal1.x, portal1.y, Tileset.TREE);
        portal2 = new Point(portal1);
        while (portal1.equals(portal2)) {
            portal2 = randomPoint();
        }
        setTile(portal2.x, portal2.y, Tileset.TREE);
    }


    public Point randomPoint() {
        while (true) {
            int x = getRand(WIDTH);
            int y = getRand(HEIGHT);
            TETile t = getTile(x, y);
            if (!t.getBlocks()) {
                return new Point(x, y);
            }
        }
    }

    public int getRand(int maxValue) {
        return RandomUtils.uniform(rand, maxValue);
    }


    public void verticalWall(int x, int y1, int y2, TETile type) {
        for (int y = y1; y <= y2; ++y) {
            world[x][y] = type;
        }
    }


    public void horizontalWall(int y, int x1, int x2, TETile type) {
        for (int x = x1; x <= x2; ++x) {
            world[x][y] = type;
        }
    }


    public TETile getTile(int x, int y) {
        if (x < 0 || x >= WIDTH) {
            return Tileset.INVALID;
        }
        if (y < 0 || y >= HEIGHT) {
            return Tileset.INVALID;
        }
        return world[x][y];
    }


    public long getSeed() {
        return seed;
    }


    public TETile[][] getWorld() {
        return world;
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }


    public boolean boundsCheck(int x, int y) {
        return !((x >= WIDTH || y >= HEIGHT) || (x < 0 || y < 0));
    }

    public Point makeLockedDoor() {
        int x = 0;
        int y = 0;
        int xd = 0;
        int yd = 0;
        exitDoor = new Point(-1, -1);
        int width = wholeRoom.getWidth();
        int height = wholeRoom.getHeight();
        Point xy1 = wholeRoom.xy1();
        int x1 = xy1.x;
        int y1 = xy1.y;
        while (true) {
            int pick = getRand(3);
            if (pick == 0) {
                x = 1 + getRand(width - 1); // south wall
                y = y1 + 1;
                xd = x;
                yd = y1;
            } else if (pick == 1) {
                x = 1 + getRand(width - 1); //north wall
                y = y1 + height - 1;
                xd = x;
                yd = y1 + height;
            } else if (pick == 2) { //west wall
                x = x1 + 1;
                y = 1 + y1 + getRand(height - 1);
                xd = x1;
                yd = y;
            } else if (pick == 3) { //east wall
                x = x1 + width - 1;
                y = 1 + getRand(height - 1);
                xd = x1 + width;
                yd = y - 1;
            }
            if (getTile(x, y).character() != Tileset.WALL.character()) {
                setTile(xd, yd, Tileset.LOCKED_DOOR);
                exitDoor.x = xd;
                exitDoor.y = yd;
                break;
            }
        }
        return exitDoor;
    }

    public void show(Avatar av, Monster mo, int finished) {
        ter.renderFrame(world);
        mo.draw();
        av.draw();
        int x = (int) (StdDraw.mouseX());
        int y = (int) (StdDraw.mouseY());
        TETile t = getTile(x, y);
        StdDraw.setPenColor(Color.LIGHT_GRAY);
        if (finished == 0) {
            StdDraw.textLeft(1, ter.height() - 1, hudText);
            StdDraw.textLeft(1, ter.height() - 2, helpText);
            if (t != Tileset.INVALID) {
                StdDraw.textLeft(1, ter.height() - 4, t.description());
            }
        } else if (finished == 1) {
            StdDraw.textLeft(5.0, ter.height() - 2, "Game over :(");
        } else if (finished == 2) {
            StdDraw.textLeft(5.0, ter.height() - 2, "Congratulations, you win! :-)");
        }
        StdDraw.show(); //UNCOMMENT TO DRAW
    }

    public Room initializeRooms() {
        wholeRoom = new Room(0, 0, WIDTH - 1, HEIGHT - 1);
        wholeRoom.randomSplit(this);
        wholeRoom.drawWalls(this);
        Room firstRoom = wholeRoom.isIncluded(1, 1);
        firstRoom.visit(wholeRoom, this);
        return wholeRoom;
    }

    public boolean charEquals(char c1, char c2) {
        return Character.compare(c1, c2) == 0;
    }


    public void setTile(int x, int y, TETile t) {
        if (boundsCheck(x, y)) {
            world[x][y] = t;
        }
    }


    public void setBrightness(double b) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = world[x][y].changeBrightness(b);
            }
        }
    }


    public void illuminateFrom(int lx, int ly) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                double dist = (lx - x) * (lx - x) + (ly - y) * (ly - y);
                if (dist > 20) {
                    continue;
                }
                if (boundsCheck(x, y)) {
                    if (dist < 1.0) {
                        dist = 1.0;
                    }
                    world[x][y] = world[x][y].changeBrightness(1 / dist * 2);
                }
            }
        }
    }


    boolean processCommands(char c, Monster m1, Avatar a1) {
        boolean processed = true;
        switch (c) {
            case 'B':
                lightsOn = !lightsOn;
                if (lightsOn) {
                    setBrightness(1.0);
                } else {
                    setBrightness(0.0);
                }
                break;
            case 'P':
                showMonsterPath = !showMonsterPath;
                m1.showPath(showMonsterPath);
                break;
            case ':':
                waitForQuit = true;
                return true;
            case 'Q':
                if (waitForQuit) {
                    System.out.println("Quit and saved");
                    Engine.writeGameToFile(this);
                    processed = false;
                    break;
                }
                break;
            default:
                processed = false;
                break;
        }
        waitForQuit = false;
        return processed;
    }

    char getCmd(LinkedList<Character> cmdQ) {
        char c = (char) 0;
        if (cmdQ.size() != 0) {
            c = cmdQ.removeFirst();
        }

        /* else if (falseStdDraw.hasNextKeyTyped()) {
            c = Character.toUpperCase(StdDraw.nextKeyTyped());
        }*/
        return c;
    }

    public int playGame(String cmds) {
        LinkedList<Character> cmdQ = new LinkedList<Character>();
        for (int i = 0; i < cmds.length(); ++i) {
            cmdQ.addLast(cmds.charAt(i));
        }
        Room r = wholeRoom;
        Point exitPoint = exitDoor;
        boolean avatarMoved = false;
        m.updateMonsterPath(a, this, r);
        m.showPath(showMonsterPath);

        int finished = 0;
        //show(a, m, finished); //UNCOMENT TO DRAW
        while (finished == 0) {
            if (avatarMoved) {
                m.addPoint(a.getX(), a.getY());
                avatarMoved = false;
            }
            m.move(this, showMonsterPath);
            // Monster reached avatar
            if ((m.getX() == a.getX() && m.getY() == a.getY())) {
                finished = 1;
                Tileset.AVATAR.changeTextColor(Color.RED);
            }
            char c = getCmd(cmdQ);
            if (c == (char) 0) { // For Autograder DELETE LATER
                return 0;
            }
            if (c != (char) 0) {
                boolean processed = processCommands(c, m, a);
                if (!processed) {
                    if (c == 'Q') { // to avoid System.exit in autograder.
                        return 0;
                    }
                    int ind = AVATAR_MOVES.indexOf(c);
                    if (ind >= 0) {
                        avatarMoved = a.move(this, ind);
                    }
                }
                // Avatar reached exit
                if (avatarMoved && a.getX() == exitPoint.x && a.getY() == exitPoint.y) {
                    Tileset.AVATAR.changeTextColor(Color.YELLOW);
                    finished = 2;
                }

                if (avatarMoved && a.getX() == portal1.x && a.getY() == portal1.y) {
                    setTile(portal1.x, portal1.y, Tileset.FLOOR);
                    a.set(this, portal2.x, portal2.y);
                    portal1.x = -1;
                    portal2.x = -1;
                    m.updateMonsterPath(a, this, r);
                }

                if (avatarMoved && a.getX() == portal2.x && a.getY() == portal2.y) {
                    setTile(portal2.x, portal2.y, Tileset.FLOOR);
                    a.set(this, portal1.x, portal1.y);
                    portal1.x = -1;
                    portal2.x = -1;
                    m.updateMonsterPath(a, this, r);
                }
            }
            if (!lightsOn) {
                //changeBrightness(0.0);
                illuminateFrom(a.getX(), a.getY());
                illuminateFrom(exitPoint.x, exitPoint.y);
            }
            show(a, m, finished); //UNCOMMENT TO DRAW
            StdDraw.pause(300); //UNCOMMENT TO DRAW
        }
        return finished;
    }
}







