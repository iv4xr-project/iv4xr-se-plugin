package game;

import helperclasses.Util;
//import org.junit.jupiter.api.Assertions ;
import org.junit.jupiter.api.Test;

public class PlatformTest {

    @Test
    public void ProjectBuildPathTest(){
    	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
        if(Util.fileExists(Platform.PathToLabRecruitsExecutable(labRecruitesExeRootDir)))
            System.out.println("AgentFacility for " + Platform.current.toString() 
                 + " was found in this repository at: " 
            	 + Platform.PathToLabRecruitsExecutable(labRecruitesExeRootDir));
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
