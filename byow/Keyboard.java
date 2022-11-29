package byow;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.event.KeyEvent;
import java.io.Serializable;

public class Keyboard implements Serializable {
    static class KeyMap {
        private int key;
        private char c;
        KeyMap(int kin, char cin) {
            key = kin;
            c = cin;
        }
    }

    static KeyMap[] keyCmds = { new KeyMap(KeyEvent.VK_A, 'a'), new KeyMap(KeyEvent.VK_W, 'w'),
                                new KeyMap(KeyEvent.VK_S, 's'), new KeyMap(KeyEvent.VK_D, 'd'),
                                new KeyMap(KeyEvent.VK_N, 'n'), new KeyMap(KeyEvent.VK_Q, 'q'),
                                new KeyMap(KeyEvent.VK_COLON, ':'), new KeyMap(KeyEvent.VK_P, 'p'),
                                new KeyMap(KeyEvent.VK_L,  'l') };
    static int lastKey;
    public  static char getCmd(boolean wait) {
        //System.out.println("getKey starts");
        if (wait && StdDraw.isKeyPressed(lastKey)) {
            //System.out.println("same key pressed");
            StdDraw.pause(150);
        }
        while (true) {
            for (KeyMap k: keyCmds) {
                if (StdDraw.isKeyPressed(k.key)) {
                    lastKey = k.key;
                    return k.c;
                }
            }
        }
    }
    public static char waitKey() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                return StdDraw.nextKeyTyped();
            } else {
                StdDraw.pause(150);
            }
        }
    }
}
