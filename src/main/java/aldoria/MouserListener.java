package aldoria;


import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouserListener {

    private static MouserListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;

    private final boolean[] mousePressedButton = new boolean[3];
    private boolean isDragging = false;


    public MouserListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;

    }

    public static MouserListener get() {
        if(MouserListener.instance == null) {
            MouserListener.instance = new MouserListener();
        }
        return MouserListener.instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos) {
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xpos;
        get().yPos = ypos;
        get().isDragging = get().mousePressedButton[0] || get().mousePressedButton[1] || get().mousePressedButton[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if(action == GLFW_PRESS) {
            if(button < get().mousePressedButton.length) {
                get().mousePressedButton[button] = true;
            }
        } else if(action == GLFW_RELEASE) {
            get().mousePressedButton[button] = false;
            get().isDragging = false;
        }
    }

    public static void mouseScrollCallback(long window, double xoffset, double yoffset) {
        get().scrollX = xoffset;
        get().scrollY = yoffset;
    }

    public static void endFrame() {
        get().scrollX = 0.0;
        get().scrollY = 0.0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX() {
        return (float) get().xPos;
    }

    public static float getY() {
        return (float) get().yPos;
    }

    public static float getDx() {
        return (float) (get().xPos - get().lastX);
    }

    public static float getDy() {
        return (float) (get().yPos - get().lastY);
    }

    public static float getScrollX() {
        return (float) get().scrollX;
    }

    public static float getScrollY() {
        return (float) get().scrollY;
    }

    public static boolean getDragging() {
        return get().isDragging;
    }

    public static boolean getMouseButtonPressed(int button){
        if(button < get().mousePressedButton.length){
            return get().mousePressedButton[button];
        } else {
            return false;
        }
    }

}
