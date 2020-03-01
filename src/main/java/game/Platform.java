/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package game;

import helperclasses.Util;
import logger.PrintColor;

import java.nio.file.Paths;

public abstract class Platform {

    private static String NOT_INSTALLED_MESSAGE = PrintColor.FAILURE() + ": It seems that you do not have the game installed on your pc, please visit our website/git on how to install the game!\n" +
            "If you chose to install the game in a different folder, use [new UnityTestServer(useGraphics, \"your installation path\")] to redirect it!";
    private static String UNSUPPORTED_PLATFORM_MESSAGE = "This platform is not supported, please contact the team!";

    public enum SupportedPlatforms { Windows, Mac, Linux }

    public static SupportedPlatforms current = currentPlatform();

    private static SupportedPlatforms currentPlatform(){
        String platform = System.getProperty("os.name").toLowerCase();
        if(platform.contains("win"))
            return SupportedPlatforms.Windows;
        else if (platform.contains("mac"))
            return SupportedPlatforms.Mac;
        else if(platform.contains("nix") || platform.contains("nux") || platform.contains("aix"))
            return SupportedPlatforms.Linux;
        else
            throw new IllegalCallerException("AgentFacility does not support this platform!");
    }

    public static String PathToLabRecruitsExecutable(String LabRecruitsRootDir) {
        //String path = Paths.get(System.getProperty("user.dir"), "gym", current.toString(), "bin").toAbsolutePath().toString();
        String path = Paths.get(LabRecruitsRootDir, "gym", current.toString(), "bin").toAbsolutePath().toString();
        switch (current){
            case Windows:
                return Paths.get(path, "LabRecruits.exe").toAbsolutePath().toString();
            case Linux:
                return Paths.get(path, "LabRecruits").toAbsolutePath().toString();
            case Mac:
                return Paths.get(path, "LabRecruits.app", "Contents", "MacOS", "LabRecruits").toAbsolutePath().toString();
            default:
                return UNSUPPORTED_PLATFORM_MESSAGE;
        }
    }

    public static String INSTALL_PATH = getInstallationPath();

    private static String getInstallationPath(){
        String path;
        switch (current){
            case Windows:
                // try the Program Files installation folder
                path = path(System.getenv("ProgramFiles"), "MuscleAI","LabRecruits","LabRecruits.exe");
                if(Util.fileExists(path))
                    return path;
                // try the Program Files (x86) installation folder
                path = path(System.getenv("ProgramFiles") + " (x86)","MuscleAI","LabRecruits","LabRecruits.exe");
                if(Util.fileExists(path))
                    return path;
                return NOT_INSTALLED_MESSAGE;
            case Mac:
                path = path("/Applications", "LabRecruits.app", "Contents","MacOS", "LabRecruits");
                if(Util.fileExists(path))
                    return path;
                return NOT_INSTALLED_MESSAGE;
            default:
                return UNSUPPORTED_PLATFORM_MESSAGE;
        }
    }

    public static String LEVEL_PATH = path(System.getProperty("user.dir"), "src", "test", "resources", "levels");

    private static String path(String first, String ... more){
        return Paths.get(first, more).toAbsolutePath().toString();
    }

    public static boolean isLinux() {
        return current == SupportedPlatforms.Linux;
    }
}
