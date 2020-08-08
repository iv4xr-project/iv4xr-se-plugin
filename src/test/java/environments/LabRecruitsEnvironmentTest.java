/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package environments;

import org.junit.jupiter.api.Test;

import agents.TestSettings;
import game.LabRecruitsTestServer;
import game.Platform;

import static org.junit.jupiter.api.Assertions.* ;

import java.util.Scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;


public class LabRecruitsEnvironmentTest {
	
	private LabRecruitsTestServer labRecruitsTestServer ;

    @BeforeEach
    void start() {
    	// Uncomment this to make the game's graphic visible:
    	var useGraphics = true ;
    	SocketReaderWriter.debug = true ;
    	String labRecruitesExeRootDir = System.getProperty("user.dir") ;
    	labRecruitsTestServer =new LabRecruitsTestServer(
    			useGraphics,
                Platform.PathToLabRecruitsExecutable(labRecruitesExeRootDir));
    	labRecruitsTestServer.waitForGameToLoad();
    }

    @AfterEach
    void close() { 
    	SocketReaderWriter.debug = false ;
    	if(labRecruitsTestServer!=null) labRecruitsTestServer.close(); 
    }

    @Test
    public void test1() {
    	assertTrue(labRecruitsTestServer != null) ;
    	
    	var environment = new LabRecruitsEnvironment(new LabRecruitsConfig("minimal"));
    	
    	assertTrue(environment != null) ;
    	
    	System.out.println("You can drag then game window elsewhere for beter viewing. Then hit RETURN to continue.") ;
		new Scanner(System.in) . nextLine() ;
		
    }
}