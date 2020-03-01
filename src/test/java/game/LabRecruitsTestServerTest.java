/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package game;

import org.junit.jupiter.api.Assertions ;
import org.junit.jupiter.api.Test;

public class LabRecruitsTestServerTest {

    // the game is present in the repository
    @Test
    public void binExistsTest() {

    	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
        LabRecruitsTestServer testServer = new LabRecruitsTestServer(
                false,
                Platform.PathToLabRecruitsExecutable(labRecruitesExeRootDir));

        // should be false because server is not started yet
        Assertions.assertTrue(testServer.isRunning());

        testServer.close();
    }

    // the game is not present in the root of the repository and should throw an exception
    //@Test(expected = IllegalArgumentException.class)
    @Test
    public void binNotExistsTest() {
    	Assertions.assertThrows(IllegalArgumentException.class, 
    		() -> { var testServer = new LabRecruitsTestServer(
    	                   false,
    	                   System.getProperty("user.dir") + "LabRecruits.exe") ;
    		         // should be false because server is not started yet
    	             Assertions.assertFalse(testServer.isRunning());
    	             testServer.close(); }
    	    ) ;  
    }
}
