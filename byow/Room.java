package byow;
// import byow.Core.RandomUtils;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;


import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Room implements Serializable {
    private boolean visited;
    private int x1;
    private int y1;
    private int width;
    private int height;
    private int xs;
    private int ys;
    private ArrayList<Room> children = new ArrayList<Room>(4);
    private ArrayList<Point> doors = new ArrayList<Point>();



    public Room(int x1, int y1, int width, int height) {
        this.x1 = x1;
        this.y1 = y1;
        this.width = width;
        this.height = height;
        xs = 0;
        ys = 0;
        visited = false;
        for (int i = 0; i < 4; i++) {
            children.add(i, null);
        }
    }

    public void split(int x, int y) {
        if (xs >= width || ys >= height) {
            System.out.println("Split point is outside of room.");
            System.exit(0);
        }
        xs = x;
        ys = y;
        children.set(0, new Room(x1, y1, xs, ys));
        children.set(1, new Room(x1 + xs, y1, width - xs, ys));
        children.set(2, new Room(x1 + xs, y1 + ys, width - xs, height - ys));
        children.set(3, new Room(x1, y1 + ys, xs, height - ys));
    }


    public void randomSplit(World w) {

        int r = w.getRand(5);

        if (width <= 4 || height <= 4) {
            return;
        }
        switch (r) {
            case 0:
                xs = 2;
                ys = 2 + w.getRand(height - 4);
                break;
            case 1:
                xs = width - 2;
                ys = 2 + w.getRand(height - 4);
                break;
            case 2:
                ys = 2;
                xs = 2 + w.getRand(width - 4);
                break;
            case 3:
                ys = height - 2;
                xs = 2 + w.getRand(width - 4);
                break;
            case 4:
                if (height < 20 || width < 20) {
                    return;
                }
                break;
            default:
                System.out.println("oops");
        }
        split(xs, ys);

        for (Room room : children) {
            if (room != null) {
                room.randomSplit(w);
            }
        }
    }


    public void shuffleDir(ArrayList<Integer> dir, World w) {
        for (int i = 0; i < 4; i++) {
            int r = w.getRand(4);
            Collections.swap(dir, i, r);
        }
    }


    public boolean checkBounds(int val, int start, int range) {
        return (val > start) && (val < start + range);
    }


    public void moveHor(int delta, Room root, World w) {
        int xn = x1 + delta;
        int yn;
        int xd;
        if (delta > 0) {
            xd = xn - 1;
        } else {
            xd = xn + 1;

        }
        for (yn = y1 + 1; yn < y1 + height; yn++) {
            checkMove(root, xn, yn, xd, yn, w);
        }
    }


    public void moveVer(int delta, Room root, World w) {
        int xn;
        int yn = y1 + delta;
        int yd;
        if (delta > 0) {
            yd = yn - 1;
        } else {
            yd = yn + 1;

        }
        for (xn = x1 + 1; xn < x1 + width; xn++) {
            checkMove(root, xn, yn, xn, yd, w);
        }
    }

    public Room checkMove(Room root, int xn, int yn, int xd, int yd, World w) {
        Room next = root.isIncluded(xn, yn);
        if (next == null) {
            return null;
        }
        TETile t = w.getTile(xn, yn);
        if (w.charEquals(t.character(), Tileset.NOTHING.character())) {
            w.setTile(xd, yd, Tileset.DOOR);
            doors.add(new Point(xd, yd));
            next.doors.add(new Point(xd, yd));
            next.visit(root, w);
        }
        return next;
    }


    public void drawWalls(World w) {
        w.horizontalWall(y1, x1, x1 + width, Tileset.WALL);
        w.verticalWall(x1 + width, y1, y1 + height, Tileset.WALL);
        w.horizontalWall(y1 + height, x1, x1 + width, Tileset.WALL);
        w.verticalWall(x1, y1, y1 + height, Tileset.WALL);

        if (xs != 0) {
            w.horizontalWall(ys + y1, x1, x1 + width, Tileset.WALL);
            w.verticalWall(xs + x1, y1, y1 + height, Tileset.WALL);
        }

        for (Room r : children) {
            if (r != null) {
                r.drawWalls(w);
            }
        }
    }


    public Room isIncluded(int x, int y) {
        if (!checkBounds(x, x1, width + 1) || !checkBounds(y, y1, height + 1)) {
            return null;
        }

        for (Room r : children) {
            if (r == null) {
                continue;
            }
            Room p = r.isIncluded(x, y);
            if (p != null) {
                return p;
            }
        }
        return this;
    }


    public boolean isChild(Room r) {
        for (Room c : children) {
            if (r.equals(c)) {
                return true;
            }
        }
        return false;
    }


    public void visit(Room root, World w) {
        ArrayList<Integer> dir = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        shuffleDir(dir, w);
        visited = true;
        fill(Tileset.FLOOR, w);

        for (int d : dir) {
            switch (d) {
                case 0:
                    moveHor((int) width + 1, root, w);
                    break;
                case 1:
                    moveHor(-1, root, w);
                    break;
                case 2:
                    moveVer((int) height + 1, root, w);
                    break;
                case 3:
                    moveVer(-1, root, w);
                    break;
                default:
                    System.out.println("Invalid direction");
                    System.exit(0);
            }
        }
    }

    public void fill(TETile tile, World w) {
        for (int y = y1 + 1; y < y1 + height; y++) {
            for (int x = x1 + 1; x < x1 + width; x++) {
                w.setTile(x, y, tile);
            }
        }
    }

    public Point xy1() {
        return new Point(x1, y1);
    }

    public Point xy2() {
        return new Point(x1 + width, y1 + height);
    }

    public ArrayList<Point> getDoors() {
        return doors;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }


}
