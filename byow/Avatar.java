package byow;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.Point;
import java.awt.Color;
import java.io.Serializable;

public class Avatar  implements Serializable {
    private final TETile sprite = Tileset.AVATAR;
    private int x;
    private int y;
    TETile lastTile = Tileset.FLOOR;
    private boolean walkThroughWalls = false;
    private boolean hasKey = false;
    static final Point [] DIRECTIONS = {new Point(1, 0),
        new Point(-1, 0),
        new Point(0, 1),
        new Point(0, -1)
    };
    public Avatar(World w, Room r) {
        while (true) {
            this.x = w.getRand(r.getWidth());
            this.y = w.getRand(r.getHeight());
            TETile t = w.getTile(x, y);
            if (!t.getBlocks()) {
                break;
            }
        }
        w.setTile(this.x, this.y, sprite);
        sprite.changeTextColor(Color.white);
        sprite.setBrightness(1.0);
    }

    public int[] setStart(World w) {
        int x1 = w.getRand(w.getWidth());
        int y1 = w.getRand(w.getHeight());
        char visual = w.getWorld()[x][y].character();
        int result = Character.compare(visual, Tileset.FLOOR.character());

        if (result != 0) {
            int[] arr = setStart(w);
            return arr;
        }
        int[] arr = {x1, y1};
        return arr;
    }


    public boolean move(World w, int dx, int dy) {
        if (!w.boundsCheck(x + dx, y + dy)) {
            return false;
        }
        TETile t = w.getTile(x + dx, y + dy);
        if (!walkThroughWalls && t.getBlocks()) {
            return false;
        }
        w.setTile(x, y, Tileset.FLOOR);
        x += dx;
        y += dy;
        if (t.character() == Tileset.FLOWER.character()) {
            this.walkThroughWalls = true;
        } else if (t.character() == Tileset.KEY.character()) {
            Tileset.LOCKED_DOOR.setBlocks(false);
        }
        w.setTile(x, y, Tileset.AVATAR);
        return true;
    }

    public boolean move(World w, int i) {
        if (i >= 0 && i < 4) {
            return move(w, DIRECTIONS[i].x, DIRECTIONS[i].y);
        }
        return false;
    }
    public void set(World w, int x1, int y1) {
        if (!w.boundsCheck(x1, y1)) {
            return;
        }
        if (!w.getTile(x1, y1).getBlocks()) {
            w.setTile(x1, y1, Tileset.AVATAR);
            this.x = x1;
            this.y = y1;
        }
    }

    public void show(World w) {
        w.setTile(this.x, this.y, sprite);
    }

    /*public void play(World myWorld) {
        Tileset.FLOOR.blocks = false;
        Tileset.DOOR.blocks = false;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                if (StdDraw.nextKeyTyped() == 'l') {
                    myWorld.lightsOnOff();
                }
            } else if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT))
                this.move(myWorld, -1, 0);
            else if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT))
                this.move(myWorld, 1, 0);
            else if (StdDraw.isKeyPressed(KeyEvent.VK_UP))
                this.move(myWorld, 0, 1);
            else if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN))
                this.move(myWorld, 0, -1);
            StdDraw.pause(50);
        }
    }
*/
    public void draw() {
        //sprite.draw(x, y);
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
    /*
    public static void main(String[] args) {
        World myWorld = new World(1547);
        myWorld.initializeRooms();
        Avatar myAvatar = new Avatar(myWorld,null);
        myWorld.show();
        myAvatar.play(myWorld);
        }
    }

     */


