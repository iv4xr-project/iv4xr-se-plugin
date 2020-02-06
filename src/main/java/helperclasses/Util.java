package helperclasses;

import java.awt.geom.IllegalPathStateException;
import java.io.File;

public class Util {

    public static boolean fileExists(String path){
        return new File(path).exists();
    }

    public static void verifyPath(String path){
        if(!fileExists(path))
            throw new IllegalArgumentException("Could not find: " + path + "\nPlease provide a valid path!");
    }
}
