package agents;

import static agents.TestSettings.USE_GRAPHICS;
import static agents.TestSettings.USE_SERVER_FOR_TEST;

import game.LabRecruitsTestServer;
import game.Platform;

public class TestSettings {

    public static boolean USE_SERVER_FOR_TEST = true;
    public static boolean USE_GRAPHICS = false;
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

}
