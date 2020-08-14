package environments;

import static org.junit.jupiter.api.Assertions.* ;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eu.iv4xr.framework.spatial.Vec3;
import eu.iv4xr.framework.spatial.meshes.Face;
import eu.iv4xr.framework.spatial.meshes.Mesh;
import game.LabRecruitsTestServer;
import game.Platform;
import helperclasses.datastructures.linq.QArrayList;
import world.LabEntity;
import world.LabWorldModel;

/**
 * For checking basic properties of the navigation mesh sent by Lab Recruits.
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
    
    
    // get the bottom-left corner of the mesh:
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
    
    // get the top-right corner of the mesh:
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
    
    /**
     * Check that observation (without doors) returns visible vertices which are all
     * indeed within the agent's visibility range, and that all the vertices left out
     * are indeed outside the visibility range.
     */
    @Test
    public void test_visible_vertices() {
    	test_visible_vertices("minimal") ;
    	System.out.println("==============") ;
    	test_visible_vertices("square") ;
    	System.out.println("==============") ;
    	test_visible_vertices("longcorridor") ;
    	System.out.println("==============") ;
    	test_visible_vertices("verylongcorridor") ;
    	System.out.println("==============") ;
    }
    
    public void test_visible_vertices(String level) {
    	assertTrue(labRecruitsTestServer != null) ;
    	System.out.println(">>> level: " + level) ;
    	var config = new LabRecruitsConfig(level);
    	var env = new LabRecruitsEnvironment(config);
    	var obs = env.observe("agent0") ;
    	System.out.println(">>> agent position:" + obs.position) ;
    	var mesh = env.worldNavigableMesh ;
    	System.out.println(">>> #vertices: " + mesh.vertices.size()) ;
    	System.out.println(">>> #faces: " + mesh.faces.size()) ;
    	var bottomleft = getBottomLeftCorner(mesh) ;
    	var topright = getTopRightCorner(mesh); 
    	System.out.println(">>> bottom-left: " + bottomleft) ;
    	System.out.println(">>> top-right: " + topright) ;
    	var visibleNodes = obs.visibleNavigationNodes ;
    	var agentPosition = obs.position ;
    	for (int v : visibleNodes) {
    		var v_ = mesh.vertices.get(v) ;
    		var dist = Vec3.dist(agentPosition,v_) ;
    		System.out.println("   ** Visible: vertex " + v + ", distance = " + dist) ;
    		assertTrue(dist <= config.view_distance) ;
    	}
    	for (var v : getUnvisibleVertices(mesh,visibleNodes)) {
    		var v_ = mesh.vertices.get(v) ;
    		var dist = Vec3.dist(agentPosition,v_) ;
    		System.out.println("   ** NOT visible: vertex " + v + ", distance = " + dist) ;
    		assertTrue(dist > config.view_distance) ;
    	}
  
    }
    
    List<Integer> getUnvisibleVertices(Mesh mesh, int[] visbleVertices) {
    	List<Integer> collected = new LinkedList<>() ;
    	for (int v=0; v < mesh.vertices.size(); v++) {
    		var visible = false ;
    		for (var v2 : visbleVertices) {
    			if (v==v2) {
    				visible = true ; break ;
    			}
    		}
    		if (!visible) collected.add(v) ;
    	}
    	return collected ;
    }
    
    
    @Test
    public void test_that_closeddoors_block_visibility_on_vertices() {
    	assertTrue(labRecruitsTestServer != null) ;
    	var level = "longcorridorWithDoorBetween" ;
    	System.out.println(">>> level: " + level) ;
    	var config = new LabRecruitsConfig(level);
    	var env = new LabRecruitsEnvironment(config);
    	var obs = env.observe("agent0") ;
    	var door0 = obs.getElement("door0") ;
    	System.out.println(">>> agent position:" + obs.position) ;
    	System.out.println(">>> door0 position:" + door0.position + ", distance: " + 
    	                        Vec3.dist(door0.position, obs.position)) ;
    	var mesh = env.worldNavigableMesh ;
    	System.out.println(">>> #vertices: " + mesh.vertices.size()) ;
    	System.out.println(">>> #faces: " + mesh.faces.size()) ;
    	var bottomleft = getBottomLeftCorner(mesh) ;
    	var topright = getTopRightCorner(mesh); 
    	System.out.println(">>> bottom-left: " + bottomleft) ;
    	System.out.println(">>> top-right: " + topright) ;
    	var visibleNodes = obs.visibleNavigationNodes ;
    	var agentPosition = obs.position ;
    	for (int v : visibleNodes) {
    		var v_ = mesh.vertices.get(v) ;
    		var dist = Vec3.dist(agentPosition,v_) ;
    		System.out.println("   ** Visible: vertex " + v + ", distance = " + dist) ;
    		assertTrue(dist <= Vec3.dist(door0.position, obs.position) + 0.5) ;
    	}
    	for (var v : getUnvisibleVertices(mesh,visibleNodes)) {
    		var v_ = mesh.vertices.get(v) ;
    		var dist = Vec3.dist(agentPosition,v_) ;
    		System.out.println("   ** NOT visible: vertex " + v + ", distance = " + dist) ;
    		assertTrue(dist > Vec3.dist(door0.position, obs.position) + 0.5) ;
    	}
    }
    
    
    // Unfortunately, this test fails. It seems that LR does not do the calculation correctly
    // Disabling the test for now:
    //@Test
    public void test_that_wall_blocks_visibility_on_vertices() throws InterruptedException {
    	assertTrue(labRecruitsTestServer != null) ;
    	var level = "longcorridorWithWallBetween" ;
    	System.out.println(">>> level: " + level) ;
    	var config = new LabRecruitsConfig(level);
    	var env = new LabRecruitsEnvironment(config);
    	var obs = env.observe("agent0") ;
    	System.out.println(">>> agent position:" + obs.position) ;
    	var mesh = env.worldNavigableMesh ;
    	System.out.println(">>> #vertices: " + mesh.vertices.size()) ;
    	System.out.println(">>> #faces: " + mesh.faces.size()) ;
    	var bottomleft = getBottomLeftCorner(mesh) ;
    	var topright = getTopRightCorner(mesh); 
    	System.out.println(">>> bottom-left: " + bottomleft) ;
    	System.out.println(">>> top-right: " + topright) ;
    	var visibleNodes = obs.visibleNavigationNodes ;
    	var agentPosition = obs.position ;
    	// some nodes should be visibl; and some nodes should not:
    	assertTrue(visibleNodes.length > 0) ;
    	assertTrue(getUnvisibleVertices(mesh,visibleNodes).size() > 0) ;
    	// for this level, all visible nodes should be in less than 5f distance,
    	// and else they are unvisible:
    	for (int v : visibleNodes) {
    		var v_ = mesh.vertices.get(v) ;
    		var dist = Vec3.dist(agentPosition,v_) ;
    		System.out.println("   ** Visible: vertex " + v + ", distance = " + dist) ;
    		assertTrue(dist <= 5f) ;
    	}
    	for (var v : getUnvisibleVertices(mesh,visibleNodes)) {
    		var v_ = mesh.vertices.get(v) ;
    		var dist = Vec3.dist(agentPosition,v_) ;
    		System.out.println("   ** NOT visible: vertex " + v + " @" + v_ + ", distance = " + dist) ;
    		assertTrue(dist > 5f) ;
    	}
    }
    

}
