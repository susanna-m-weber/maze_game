package byow.TileEngine;

import byow.Avatar;

import java.awt.Color;
import java.io.Serial;
import java.io.Serializable;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset implements Serializable {
    public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you");
    public static final TETile WALL = new TETile('#', new Color(54*4, 32*4, 32*4), Color.darkGray,
            "wall", true);
    public static final TETile FLOOR = new TETile('·', new Color(32*4, 48*4, 32*4), Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "Magic flower. Allows you to walk through walls");
    public static final TETile KEY = new TETile('\u26BF', Color.yellow, Color.black, "Key. Needed to exit");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door", true);
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "Jump Portal (can only be used once)");
    public static final TETile DOOR = new TETile('·', new Color(32*4, 48*4, 32*4), Color.black,
            "floor");
    public static final TETile LIGHT = new TETile('⎔', Color.white, Color.blue, "light source");
    public static final TETile MONSTER = new TETile('▢', Color.red, Color.black,
            "the monster!");
    public static final TETile HIGHLIGHT = new TETile('·', Color.red, Color.black,
            "the monster's path");
    public static final TETile LIGHT_WALL = new TETile('#', new Color(216, 128, 128), Color.lightGray," illuminated wall");
    public static final TETile LIGHT_FLOOR = new TETile('·', new Color(128, 192, 128), Color.darkGray, " illuminated floor");
    public static final TETile INVALID = new TETile('X', new Color(128, 192, 128), Color.darkGray, "invalid coords", true);
}


