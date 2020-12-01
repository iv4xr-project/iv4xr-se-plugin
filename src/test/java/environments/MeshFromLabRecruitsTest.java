package environments;

import static org.junit.jupiter.api.Assertions.* ;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import agents.TestSettings;
import eu.iv4xr.framework.extensions.pathfinding.SurfaceNavGraph;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
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
    	//TestSettings.USE_SERVER_FOR_TEST = false ;
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
    public void test_how_door_affects_visibility_on_vertices() throws InterruptedException {
    	assertTrue(labRecruitsTestServer != null) ;
    	var level = "longcorridorWithDoorBetween" ;
    	System.out.println(">>> level: " + level) ;
    	var config = new LabRecruitsConfig(level);
    	var env = new LabRecruitsEnvironment(config);
    	System.out.println(">>> Mesh information:") ;
    	var mesh = env.worldNavigableMesh ;
    	System.out.println(">>> #vertices: " + mesh.vertices.size()) ;
    	System.out.println(">>> #faces: " + mesh.faces.size()) ;
    	var bottomleft = getBottomLeftCorner(mesh) ;
    	var topright = getTopRightCorner(mesh); 
    	System.out.println(">>> bottom-left: " + bottomleft) ;
    	System.out.println(">>> top-right: " + topright) ;
    	
    	// Door-0 is initially closed. Let's check which nodes should be visible:
    	
    	var obs = env.observe("agent0") ;
    	var door0 = obs.getElement("door0") ;
    	System.out.println(">>> agent position:" + obs.position) ;
    	System.out.println(">>> door0 position:" + door0.position + ", distance: " + 
    	                        Vec3.dist(door0.position, obs.position)) ;
    	// button-1 should NOT be visible:
    	System.out.println(">>> can see button1: " + obs.getElement("button1")) ;
    	assertFalse(obs.getElement("button1") != null) ;

    	var visibleNodes = obs.visibleNavigationNodes ;
    	var agentPosition = obs.position ;
    	for (int v : visibleNodes) {
    		var v_ = mesh.vertices.get(v) ;
    		var dist = Vec3.dist(agentPosition,v_) ;
    		System.out.println("   ** Visible: vertex " + v 
    				+ " " + v_ 
    				+ ", distance = " + dist) ;
    		assertTrue(dist <= Vec3.dist(door0.position, obs.position) + 0.5) ;
    	}
    	for (var v : getUnvisibleVertices(mesh,visibleNodes)) {
    		var v_ = mesh.vertices.get(v) ;
    		var dist = Vec3.dist(agentPosition,v_) ;
    		System.out.println("   ** NOT visible: vertex " + v + " " + v_ + ", distance = " + dist) ;
    		boolean onTheSameFloor = Math.abs(v_.y - 0f) <= 0.1 ;
    		assertTrue(!onTheSameFloor || dist > Vec3.dist(door0.position, obs.position) + 0.5) ;
    	}
    	
    	// Let's not open door-1
    	env.interact("agent0", "button0", "") ;
    	// The door opens slowly :| So make sure to add enough delay. The door needs to be completely open
    	// for the agent to get full sight to what lies behind it:
    	Thread.sleep(1000);

    	obs = env.observe("agent0") ;
    	System.out.println(">>> agent position:" + obs.position) ;
    	System.out.println(">>> door0 position:" + door0.position + ", distance: " + 
    	                        Vec3.dist(door0.position, obs.position)) ;
    	// button1 should not be visible:
    	System.out.println(">>> can see button1: " + obs.getElement("button1")) ;
    	assertTrue(obs.getElement("button1") != null) ;

    	visibleNodes = obs.visibleNavigationNodes ;
    	agentPosition = obs.position ;
    	for (int v : visibleNodes) {
    		var v_ = mesh.vertices.get(v) ;
    		var dist = Vec3.dist(agentPosition,v_) ;
    		System.out.println("   ** Visible: vertex " + v 
    				+ " " + v_ 
    				+ ", distance = " + dist) ;
    		assertTrue(dist <= config.view_distance) ;
    	}
    	for (var v : getUnvisibleVertices(mesh,visibleNodes)) {
    		var v_ = mesh.vertices.get(v) ;
    		var dist = Vec3.dist(agentPosition,v_) ;
    		System.out.println("   ** NOT visible: vertex " + v + " " + v_ + ", distance = " + dist) ;
    		boolean onTheSameFloor = Math.abs(v_.y - 0f) <= 0.1 ;
    		assertTrue(!onTheSameFloor || dist > config.view_distance) ;
    	}
    }
    
    
    
    /**
     * Test how different layout of static obstacles influence the reachability of nodes in 
     * navigation mesh. Obviously, walls should block reachability. 
     * But also static furnitures like tables, chairs, book-cases. Color-screen
     * also blocks reachability.
     */ 
    @Test
    public void test_nodes_reachability_behind_static_obstacles() {
    	// basel line: no obstacle in between:
    	var path = test_nodes_reachability_behind_static_obstacles("squareWithOneButton") ;
    	assertTrue(path != null) ;
    	
    	// All theses cases are where we put walls or other thing to completely block the way
    	// to the button. So, they all should return null-path.
    	
    	// half-walls fully block the way
    	path = test_nodes_reachability_behind_static_obstacles("squareWithOneButtonAndWallsBetween") ;
    	assertTrue(path==null) ;
    	// a series of chairs fully block the way
    	path = test_nodes_reachability_behind_static_obstacles("squareWithOneButtonAndChairsBetween") ;
    	assertTrue(path==null) ;
    	// a series of color-screens fully block the way
    	path = test_nodes_reachability_behind_static_obstacles("squareWithOneButtonAndColorScreensBetween") ;
    	assertTrue(path==null) ;   	
    	
    	// these cases are where with put obstacles in the way, but with a "hole" so the button
    	// should still be reachable. So, a non-null path should be returned.
    	// Note that the worker function test_nodes_reachability_behind_static_obstacles already check
    	// the the path indeed starts and ends in the vicinity of the real start and end Vec3 locations.

    	// half-walls in between, but not completely block the way
    	path = test_nodes_reachability_behind_static_obstacles("squareWithOneButtonAndWallsBetween_andHole") ;
    	assertTrue(path != null) ;
    	// chairs in between, but not completely block the way
    	path = test_nodes_reachability_behind_static_obstacles("squareWithOneButtonAndChairsBetween_andHole") ;
    	assertTrue(path != null) ;
    }
    
    /**
     * For the given level, check if a button, assumed to be located at Vec3(5,0,5), is reachable
     * from the agent location. If it is a path is returned, else null.
     * If a path is returned, we check that its starting node and its end node are whithin 0.5
     * distance from the actual Vec3 start and end locations.
     * The "0.5" is somewhat arbitrary, as it depends on the mesh generated by LR, but it seems
     * to be a reasonable assumption for the tested levels.
     */
    List<Integer> test_nodes_reachability_behind_static_obstacles(String level) {
    	assertTrue(labRecruitsTestServer != null) ;
    	System.out.println("======= level: " + level) ;
    	var config = new LabRecruitsConfig(level);
    	var env = new LabRecruitsEnvironment(config);
    	var obs = env.observe("agent0") ;
    	System.out.println(">>> visibility range:" + config.view_distance) ;
    	System.out.println(">>> agent position:" + obs.position) ;
    	var mesh = env.worldNavigableMesh ;
    	System.out.println(">>> #vertices: " + mesh.vertices.size()) ;
    	System.out.println(">>> #faces: " + mesh.faces.size()) ;
    	var bottomleft = getBottomLeftCorner(mesh) ;
    	var topright = getTopRightCorner(mesh); 
    	var visibleNodes = obs.visibleNavigationNodes ;
    	System.out.println(">>> bottom-left: " + bottomleft) ;
    	System.out.println(">>> top-right: " + topright) ;
    	System.out.println(">>> #visible vertices: " + visibleNodes.length + ", these are:") ;
    	// and else they are unvisible:
    	for (int v : visibleNodes) {
    		var v_ = mesh.vertices.get(v) ;
    		System.out.println("     vertex " + v + ": " + mesh.vertices.get(v)) ;
    	}
    	// We will use SurfaceNavGraph to verify reachability
    	SurfaceNavGraph navgraph = new SurfaceNavGraph(mesh,0.5f) ;
    	
    	// let's check intrinsic reachability .. so assume perfect-memory:
    	navgraph.perfect_memory_pathfinding = true ;
    	
    	for(WorldEntity e :obs.elements.values()) {
    		if (e.type.equals(LabEntity.COLORSCREEN)) {
    			LabEntity e_ = (LabEntity) e ;
    			System.out.println(">>> " + e_.id + " " + e_.getFloorPosition() + ", extent: " + e_.extent) ;
    			navgraph.addObstacleInBlockingState(e_); 
    		}
    	}
    	
    	// navgraph.markAsSeen(visibleNodes);
    	var agent_loc = Vec3.add(obs.position, Vec3.zero()) ;
    	agent_loc.y = 0 ;
    	// button location... assumed to be at (5,0,5):
    	var button_loc = new Vec3(5,0,5) ;
    	
    	var path = navgraph.findPath(agent_loc, button_loc, 0.2f) ;
    	System.out.println(">>> SurfaceNavgraph info:") ;
    	System.out.println(">>>   #vertices: " + navgraph.vertices.size()) ;
    	System.out.println(">>>   #faces: " + navgraph.faces.size()) ;
    	System.out.println(">>> path from " 
    					   + agent_loc + " to "
    					   + button_loc + ": " + path) ;
    	if (path != null) {
    		for(int i=0; i<path.size();i++) {
        		System.out.println("   " + i + 
        				": " 
        				+ navgraph.vertices.get(path.get(i))) ;    			
    		}
    		var N = path.size() ;
    		var dist1 = Vec3.dist(agent_loc,  navgraph.vertices.get(path.get(0))) ;
    		var dist2 = Vec3.dist(button_loc, navgraph.vertices.get(path.get(N-1))) ;
    		System.out.println(">>> dist. start path to agent: " + dist1) ;
    		System.out.println(">>> dist. end path to target-button: " + dist1) ;
    		assertTrue(dist1 <= 0.5f) ;
    		assertTrue(dist2 <= 0.5f) ;	
    	}
    	return path ;
    }
    
    boolean checkPath(List<Integer> pathToCheck, int ... expectedPath) {
		System.out.println("** path to check: " + pathToCheck) ;
		if (pathToCheck.size() != expectedPath.length) return false ;
		for(int k=0; k<expectedPath.length; k++) {
			if(pathToCheck.get(k) != expectedPath[k]) return false ;
		}
	return true ;	
	}
    

    @Test
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
    		assertTrue(dist <= 4f) ;
    	}
    	for (var v : getUnvisibleVertices(mesh,visibleNodes)) {
    		var v_ = mesh.vertices.get(v) ;
    		var dist = Vec3.dist(agentPosition,v_) ;
    		boolean onTheSameFloor = Math.abs(v_.y - 0f) <= 0.1 ;
    		System.out.println("   ** NOT visible: vertex " + v + " @" + v_ + ", distance = " + dist) ;
    		assertTrue(!onTheSameFloor || dist > 4f) ;
    	}
    }
    

}
