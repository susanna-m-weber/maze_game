package byow.Core;


import byow.World;
import byow.Utils;
import byow.Keyboard;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
//import edu.princeton.cs.algs4.StdDraw;

//import java.awt.Color;
//import java.awt.Font;
import java.io.File;
import java.io.IOException;
//import byow.World;

//import java.util.Locale;

public class Engine {
    static final File CWD = new File(".");
    //static final File SAVE_DIR = Utils.join(CWD, ".game");
    static final File GAME_FILE = Utils.join(CWD, "CS61BSusannaGame");
    public static final int WIDTH = 62;
    public static final int HEIGHT = 30;

    String validCmd = "NSAWDLQ:"; //valid commands

    TERenderer ter;
    World myWorld;

    public Engine() {
        //ter = new TERenderer();
        //ter.initialize(WIDTH, HEIGHT); //UNCOMMENT TO DRAW
        //StdDraw.enableDoubleBuffering();
        //StdDraw.disableDoubleBuffering();
    }
    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void showMenu() {
        //StdDraw.clear(Color.DARK_GRAY);
        //StdDraw.setPenColor(Color.WHITE);
        //Font stdFont = StdDraw.getFont();
        //Font fontBig = new Font("Monaco", Font.BOLD, 20);
        //StdDraw.setFont(fontBig);
        //StdDraw.text(this.WIDTH / 2, this.HEIGHT - 1, "CS 61B: THE GAME");
        //StdDraw.text(this.WIDTH / 2, this.HEIGHT - 3, "New World (N)");
        //StdDraw.text(this.WIDTH / 2, this.HEIGHT - 5, "Load (L)");
        //StdDraw.text(this.WIDTH / 2, this.HEIGHT - 7, "Quit (Q)");
        //StdDraw.show(); //UNCOMMENT TO DRAW
        //StdDraw.setFont(stdFont);
    }

    String getCmd(char endChar) {
        StringBuilder result = new StringBuilder();
        result.append("N");
        int xpos = this.WIDTH / 2;
        int ypos = this.HEIGHT - 10;
        char c = ' ';
        while (c != endChar) {
            //showMenu(); //UNCOMMENT THIS BLOCK TO DRAW
            //StdDraw.text(xpos, ypos, "Enter seed number followed by S <enter>");
            //StdDraw.setPenColor(Color.GREEN);
            //StdDraw.text(xpos, ypos - 2, "> " + result.toString());
            //StdDraw.show();
            //System.out.printf("In get seed input string is %s\n",result.toString());
            c = Character.toUpperCase(Keyboard.waitKey());

            int ind = validCmd.indexOf(c);
            //System.out.printf("In get c is %c ind = %d\n",c,ind);
            if (Character.isDigit(c) || (ind >= 1)) {
                result.append(c);
            } else if (c == '\b') {
                if (result.length() > 1) {
                    result.setLength(result.length() - 1);
                }
            }

        }
        return result.toString();
    }

    public static void writeGameToFile(World w) {
        //Utils.restrictedDelete(GAME_FILE);
        //Utils.restrictedDelete(MONSTER_FILE);
        //Utils.restrictedDelete(AVATAR_FILE);

        //byte[] serializedAvatar = Utils.serialize(a);
        //byte[] serializedMonster = Utils.serialize(m);
        byte[] serializedWorld = Utils.serialize(w);

        /**
        if (!(SAVE_DIR.exists())) {
            SAVE_DIR.mkdir();
        }
        **/

        Utils.writeContents(GAME_FILE, serializedWorld);

    }

    public void interactWithKeyboard() {
        while (true) {
            //showMenu(); //UNCOMMENT TO DRAW
            char c = Character.toLowerCase(Keyboard.getCmd(true));
            switch (c) {
                case 'n':
                    interactWithInputString(getCmd('S'));
                    Keyboard.waitKey();
                    break;
                case 'l':
                    interactWithInputString("L");
                    Keyboard.waitKey();
                    break;
                case 'q':
                    //System.exit(0);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */


    public TETile[][] interactWithInputString(String input) {
        input = input.toUpperCase();
        char first = input.charAt(0);

        String seed = ""; //input.substring(1, input.length() - 1);
        String commands = input;
        Long seedNum = Long.valueOf(0);

        if (first == 'N') {
            boolean error = true;
            for (int i = 1; i < input.length(); i++) {
                char c = input.charAt(i);
                if (!Character.isDigit(c)) {
                    if (c == 'S' && i >= 2) {
                        seed = input.substring(1, i);
                        seedNum = Long.valueOf(seed);
                        if (i == input.length() - 1) {
                            commands = "";
                        } else {
                            commands = input.substring(i + 1, input.length());
                        }
                        error = false;
                    }
                    break;
                }
            }
            if (error) {
                System.out.println("Invalid input. Please enter in the format N#######S");

            }
            myWorld = new World(seedNum, ter);
            myWorld.initializeRooms();
            myWorld.makeLockedDoor();
            myWorld.initSprites();
        } else if (first == 'L') {
            myWorld = Utils.readObject(GAME_FILE, World.class);
            commands = commands.substring(1);
        }

        for (int i = 0; i < commands.length(); i++) {
            char c = commands.charAt(i);
            if (validCmd.indexOf(c) < 1) {
                System.out.printf("String contains invalid character '%c'. "
                        +
                        "Please use WASD to move and :Q to save.\n", c);
            }
        }
        myWorld.playGame(commands);
        return myWorld.getWorld();

        //  Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
    }

    //Helpers and misc

}
