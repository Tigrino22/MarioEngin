package utils;

public class Time {

    private static float timeStarted = System.nanoTime();

    public static float getTime(){
        return (float)((System.nanoTime() - timeStarted) * 1.0E-9);
    }
}
