/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package logger;

public class PrintColor {

    private static final String RESET = "\u001B[0m";

    public static String RED(String text){
        return "\u001B[31m" + text + RESET;
    }

    public static String GREEN(String text){
        return "\u001B[32m" + text + RESET;
    }

    public static String YELLOW(String text){
        return "\u001B[33m" + text + RESET;
    }

    public static String BLUE(String text){
        return "\u001B[34m" + text + RESET;
    }

    public static String PURPLE(String text){
        return "\u001B[35m" + text + RESET;
    }

    public static String CYAN(String text){
        return "\u001B[36m" + text + RESET;
    }

    public static String UNITY(){
        return BLUE("Unity");
    }

    public static String APLIB(){
        return BLUE("APlib");
    }

    public static String SUCCESS(){
        return GREEN("SUCCESS");
    }

    public static String FAILURE(){
        return RED("FAILURE");
    }
}
