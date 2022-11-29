package byow;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.Point;
import java.io.Serializable;


public class Monster  implements Serializable {
    private final TETile sprite = Tileset.MONSTER;
    private int x = 0;
    private int y = 0;
    Path p;
    boolean showPath = false;
    public Monster(World w, Room r) {
        while (true) {
            this.x = w.getRand(r.getWidth());
            this.y = w.getRand(r.getHeight());
            TETile t = w.getTile(x, y);
            if (!t.getBlocks()) {
                break;
            }
        }
        p = new Path();
    }

    public void draw() {
        if (showPath) {
            for (Point pt : p.result)  {
                Tileset.HIGHLIGHT.draw(pt.x, pt.y);
            }
        }
        //sprite.draw(x, y);
    }

    public void showPath(boolean onOff) {
        showPath = onOff;
    }

    public void move(World w, boolean showNewPath) {
        if (p.result.size() == 0) {
            return;
        }
        Point pt = p.result.removeLast();
        this.x = pt.x;
        this.y = pt.y;
    }


    public Path updateMonsterPath(Avatar a, World w, Room r) {

        p.result.clear();
        p.checkExits(new Point(x, y), new Point(a.getX(), a.getY()), new Point(-1, -1), w, r);
        return p;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    void addPoint(int x1, int y1) {
        p.result.addFirst(new Point(x1, y1));
    }

    public void set(World w, int x1, int y1) {
        if (!w.boundsCheck(x1, y1)) {
            return;
        }
        this.x = x1;
        this.y = y1;
    }
}
