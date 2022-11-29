package byow;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeMap;

public class Path implements Serializable {

    LinkedList<Point> result = new LinkedList<>();
    HashSet<Point> visited = new HashSet<>();


    public int dist(int x1, int y1, int x2, int y2) {
        return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
    }

    public static int dist(Point c1, Point c2) {
        return (c2.x - c1.x) * (c2.x - c1.x) + (c2.y - c1.y) * (c2.y - c1.y);
    }

    public static boolean validPos(Point c, World w) {
        TETile t = w.getTile(c.x, c.y);
        return !t.getBlocks();
    }

    public Point pointAdd(Point p1, Point p2) {
        return new Point(p1.x + p2.x, p1.y + p2.y);
    }

    public boolean drawPath(Point start, Point end, Point last,  World w, Room r) {
        Point[] dir = { new Point(1, 0), new Point(-1, 0), new Point(0, 1), new Point(0, -1)};
        if (start.x == end.x && start.y == end.y) {
            //System.out.printf("Found path to %d %d\n",end.x,end.y);
            return true;
        } else {
            TreeMap<Integer, Point> di = new TreeMap<>();
            for (int i = 0; i < 4; ++i) {
                Point c = pointAdd(start, dir[i]);
                if (!c.equals(last) && validPos(c, w) && (!visited.contains(c))) {
                    int  d = dist(c, end);
                    di.put(d, c);
                }
            }

            for (Point pt : di.values()) {
                result.addLast(pt);
                boolean foundIt = drawPath(pt, end, start, w, r);
                if (foundIt) {
                    return true;
                }
                result.removeLast();
                w.setTile(pt.x, pt.y, Tileset.FLOOR);
                //w.show();
                visited.add(pt);
            }
            return false;

        }

    }

    public Room checkExits(Point start, Point end, Point last, World w, Room r) {
        Room cr = r.isIncluded(start.x, start.y);

        Room er = r.isIncluded(end.x, end.y);
        if (er.equals(cr)) {
            result.addLast(end);
            drawPath(end, start, new Point(-1, -1), w, r);
            return cr;
        }
        ArrayList<Point> dl = cr.getDoors();
        Point xy1 = cr.xy1();
        Point xy2 = cr.xy2();

        TreeMap<Integer, Point> doorCoords = new TreeMap<Integer, Point>();
        for (Point pt:dl) {
            int distance = dist(pt, end);
            doorCoords.put(distance, pt);
        }

        for (Point pt: doorCoords.values()) {
            Room nr = null;
            if (pt.equals(last)) {
                continue;
            }
            if (pt.x == xy1.x) {
                nr = checkExits(new Point(pt.x - 1, pt.y), end, pt, w, r);
            } else if (pt.x == xy2.x) {
                nr = checkExits(new Point(pt.x + 1, pt.y), end, pt, w, r);
            } else if (pt.y == xy2.y) {
                nr = checkExits(new Point(pt.x, pt.y + 1), end, pt, w, r);
            } else if (pt.y == xy1.y) {
                nr = checkExits(new Point(pt.x, pt.y - 1), end, pt, w, r);
            }
            if (nr != null) {
                drawPath(pt, start, new Point(-1, -1), w, r);
                return nr;
            } else {
                if (result.size() > 0) {
                    result.removeLast();
                }
            }
        }
        return null;
    }

    public void showPath(World w) {
        for (Point p : result) {
            w.setTile(p.x, p.y, Tileset.HIGHLIGHT);
        }
    }

    public void turnOffPath(World w) {
        for (Point p : result) {
            w.setTile(p.x, p.y, Tileset.FLOOR);
        }
    }


}
