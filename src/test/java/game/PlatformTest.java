package game;

import helperclasses.Util;
import org.junit.Test;

public class PlatformTest {

    @Test
    public void ProjectBuildPathTest(){
        if(Util.fileExists(Platform.PROJECT_BUILD_PATH))
            System.out.println("AgentFacility for " + Platform.current.toString() + " was found in this repository at: " + Platform.PROJECT_BUILD_PATH);
        else
            System.out.println("AgentFacility for " + Platform.current.toString() + " is not present in this repository");
    }

    @Test
    public void InstallPathTest(){
        if(Util.fileExists(Platform.INSTALL_PATH))
            System.out.println("A build was found at: " + Platform.INSTALL_PATH);
    }

    @Test
    public void LevelPathTest(){
        if(Util.fileExists(Platform.INSTALL_PATH))
            System.out.println("A level folder was found at: " + Platform.LEVEL_PATH);
    }
}
