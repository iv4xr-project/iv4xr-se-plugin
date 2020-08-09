package environments;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eu.iv4xr.framework.spatial.meshes.Face;
import game.LabRecruitsTestServer;
import game.Platform;
import world.LabEntity;
import world.LabWorldModel;

/**
 * For checking basic properties of the navigation mesh sent by Lab Recruites.
 */
public class MeshFromLabRecruitsTest {
	
	private LabRecruitsTestServer labRecruitsTestServer ;

    @BeforeEach
    void start() {
    	// set this to true to make the game's graphic visible:
    	var useGraphics = false ;
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
    public void test_mesh() {
    	assertTrue(labRecruitsTestServer != null) ;
    	var env = new LabRecruitsEnvironment(new LabRecruitsConfig("minimal"));
    	System.out.println(env.worldNavigableMesh) ;
    	var connectedComponents = env.worldNavigableMesh.getConnectedComponets() ;
    	System.out.println("#Connected components = " + connectedComponents.size()) ;
    	int k = 0 ;
    	for (var group : connectedComponents) {
    		System.out.println("** Component " + k) ;
    		for (Face f : group) {
    			System.out.println("   Face " + f.toString(env.worldNavigableMesh.vertices)) ;
    		}
    		k++ ;
    	}
    }

}
