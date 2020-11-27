package agents;

import static agents.TestSettings.USE_GRAPHICS;
import static agents.TestSettings.USE_SERVER_FOR_TEST;

import java.util.Scanner;

import game.LabRecruitsTestServer;
import game.Platform;

public class TestSettings {

	/**
	 * When set to true, tests will auto-launch Lab Recruits. Else they assume there is
	 * already an instance of Lab Recruits active. Using auto-launch is convenient for
	 * the tests, but if high turn around is desired we might not want to do that.
	 * 
	 * The default is true.
	 */
    public static boolean USE_SERVER_FOR_TEST = true ;
    
    /**
     * If set to true Lab Recruits, and the tests are set to auto-launch Lab Recruits,
     * then Lab Recruits will be launched with the graphics showns. Else, it will be
     * launched without graphics (if faster tests are desired, for example).
     * 
     * The default is false.
     */
    public static boolean USE_GRAPHICS = false ;
    
    public static boolean USE_INSTRUMENT = false;
    
    public static LabRecruitsTestServer start_LabRecruitsTestServer(String labRecruitesExeRootDir) {
    	LabRecruitsTestServer labRecruitsTestServer = null ;
    	if(USE_SERVER_FOR_TEST){
            labRecruitsTestServer =new LabRecruitsTestServer(
                    USE_GRAPHICS,
                    Platform.PathToLabRecruitsExecutable(labRecruitesExeRootDir));
            labRecruitsTestServer.waitForGameToLoad();
        }
    	return labRecruitsTestServer ;
    }
    
    public static void youCanRepositionWindow() {
    	if(USE_GRAPHICS) {
    		System.out.println("You can drag then game window elsewhere for beter viewing. Then hit RETURN to continue.") ;
    		new Scanner(System.in) . nextLine() ;
    	}
    }

}
