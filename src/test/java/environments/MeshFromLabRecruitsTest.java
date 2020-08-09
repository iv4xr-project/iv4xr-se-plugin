package environments;

import static org.junit.jupiter.api.Assertions.* ;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eu.iv4xr.framework.spatial.Vec3;
import eu.iv4xr.framework.spatial.meshes.Face;
import eu.iv4xr.framework.spatial.meshes.Mesh;
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
    
    /**
     * Some basic checks on the nav-mesh sent by Lab Recruits. 
     */
    @Test
    public void test_mesh() {
    	assertTrue(labRecruitsTestServer != null) ;
        // test using the "minimal" level:
    	var level = "minimal" ;
    	System.out.println(">>> level: " + level) ;
    	var env = new LabRecruitsEnvironment(new LabRecruitsConfig(level));
    	var obs = env.observe("agent0") ;
    	System.out.println(">>> agent position:" + obs.position) ;
    	var mesh = env.worldNavigableMesh ;
    	System.out.println(">>> #vertices: " + mesh.vertices.size()) ;
    	System.out.println(">>> #faces: " + mesh.faces.size()) ;
    	var bottomleft = getBottomLeftCorner(mesh) ;
    	var topright = getTopRightCorner(mesh); 
    	System.out.println(">>> bottom-left: " + bottomleft) ;
    	System.out.println(">>> top-right: " + topright) ;
    	assertTrue(Vec3.dist(bottomleft, new Vec3(-0.2f,1f,-0.2f)) <= 0.1) ;
    	assertTrue(Vec3.dist(topright, new Vec3(2.2f,1f,3.2f)) <= 0.1) ;
    	
    	// using a horizontal corridor-shaped level:
    	level = "longcorridor" ;
    	System.out.println(">>> level: " + level) ;
    	env = new LabRecruitsEnvironment(new LabRecruitsConfig(level));
    	obs = env.observe("agent0") ;
    	System.out.println(">>> agent position:" + obs.position) ;
    	var meshHorzCorridor = env.worldNavigableMesh ;
    	mesh = meshHorzCorridor ;
    	System.out.println(">>> #vertices: " + mesh.vertices.size()) ;
    	System.out.println(">>> #faces: " + mesh.faces.size()) ;
    	bottomleft = getBottomLeftCorner(mesh) ;
    	topright = getTopRightCorner(mesh); 
    	System.out.println(">>> bottom-left: " + bottomleft) ;
    	System.out.println(">>> top-right: " + topright) ;
    	assertTrue(Vec3.dist(bottomleft, new Vec3(-0.2f,1f,-0.2f)) <= 0.1) ;
    	assertTrue(Vec3.dist(topright, new Vec3(10.2f,1f,2.2f)) <= 0.1) ;
    	
    	// using a vertical corridor-shaped level:
    	level = "longcorridor2" ;
    	System.out.println(">>> level: " + level) ;
    	env = new LabRecruitsEnvironment(new LabRecruitsConfig(level));
    	obs = env.observe("agent0") ;
    	System.out.println(">>> agent position:" + obs.position) ;
    	mesh = env.worldNavigableMesh ;
    	System.out.println(">>> #vertices: " + mesh.vertices.size()) ;
    	System.out.println(">>> #faces: " + mesh.faces.size()) ;
    	bottomleft = getBottomLeftCorner(mesh) ;
    	topright = getTopRightCorner(mesh); 
    	System.out.println(">>> bottom-left: " + bottomleft) ;
    	System.out.println(">>> top-right: " + topright) ;
    	assertTrue(Vec3.dist(bottomleft, new Vec3(-0.2f,1f,-0.2f)) <= 0.1) ;
    	assertTrue(Vec3.dist(topright, new Vec3(2.2f,1f,10.2f)) <= 0.1) ;
    	
    	// adding a door does not change the numer of vertices nor faces. Compare this
    	// with the long-corridor level:
    	level = "longcorridorWithDoorBetween" ;
    	System.out.println(">>> level: " + level) ;
    	env = new LabRecruitsEnvironment(new LabRecruitsConfig(level));
    	mesh = env.worldNavigableMesh ;
    	assertEquals(mesh.vertices.size(), meshHorzCorridor.vertices.size()) ;
    	assertEquals(mesh.faces.size(), meshHorzCorridor.faces.size()) ;
    	
    }
    
    Vec3 getBottomLeftCorner(Mesh mesh) {
    	float minX = Float.MAX_VALUE ;
    	float minZ = Float.MAX_VALUE ;
    	for (Vec3 p : mesh.vertices) {
    		minX = Math.min(minX, p.x) ;
    		minZ = Math.min(minZ, p.z) ;
    	}
    	for (Vec3 p : mesh.vertices) {
    		if (p.x == minX && p.z == minZ) {
    			return p ;
    		}
    	}
    	return null ;
    }
    
    Vec3 getTopRightCorner(Mesh mesh) {
    	float maxX = Float.MIN_VALUE ;
    	float maxZ = Float.MIN_VALUE ;
    	for (Vec3 p : mesh.vertices) {
    		maxX = Math.max(maxX, p.x) ;
    		maxZ = Math.max(maxZ, p.z) ;
    	}
    	for (Vec3 p : mesh.vertices) {
    		if (p.x == maxX && p.z == maxZ) {
    			return p ;
    		}
    	}
    	return null ;
    }
    
    

}
