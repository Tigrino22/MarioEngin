package aldoria;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwGetKeyScancode;

public class KeyListener {

    private static KeyListener instance = null;
    private static boolean[] keyButtonPressed = new boolean[350];

    public KeyListener(){
        for(int i = 0; i < KeyListener.keyButtonPressed.length; i++){
             keyButtonPressed[i] = false;
        }
    }

    public static KeyListener get(){
        if (KeyListener.instance == null){
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods){
        if(key < get().keyButtonPressed.length){
            if(action == GLFW_PRESS){
                get().keyButtonPressed[key] = true;
            } else if(action == GLFW_RELEASE) {
                get().keyButtonPressed[key] = false;
            }
        }
    }

    public static boolean isKeyPressed(int keyCode){
        if(keyCode < get().keyButtonPressed.length){
            return get().keyButtonPressed[keyCode];
        } else {
            return false;
        }
    }
}
