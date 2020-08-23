/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package environments;

import org.junit.jupiter.api.Test;

import agents.TestSettings;
import eu.iv4xr.framework.spatial.Vec3;
import game.LabRecruitsTestServer;
import game.Platform;
import world.LabEntity;
import world.LabWorldModel;

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

    void hit_RETURN() {
		System.out.println("You can drag then game window elsewhere for beter viewing. Then hit RETURN to continue.") ;
    	new Scanner(System.in) . nextLine() ;
	}
    
    /**
     * Test if an instance of LR-env can be created, connected to the LR game, and
     * if level nav-mesh is received.
     */
    @Test
    public void test_env_initialization() {
    	assertTrue(labRecruitsTestServer != null) ;
    	
    	var environment = new LabRecruitsEnvironment(new LabRecruitsConfig("minimal"));
    	
    	assertTrue(environment != null) ;
    	assertTrue(environment.worldNavigableMesh != null) ;
    	assertTrue(environment.worldNavigableMesh.faces.size() > 0 ) ;
    	assertTrue(environment.worldNavigableMesh.vertices.size() > 0 ) ;
    	
    	System.out.println(environment.worldNavigableMesh) ;
    	
    	//System.out.println("You can drag then game window elsewhere for beter viewing. Then hit RETURN to continue.") ;
		//new Scanner(System.in) . nextLine() ;
    }
    
    @Test
    public void test_env_simple_observation() {
    	assertTrue(labRecruitsTestServer != null) ;
    	var env = new LabRecruitsEnvironment(new LabRecruitsConfig("minimal"));
    	LabWorldModel obs = env.observe("agent0") ;
    	LabEntity b0 = obs.getElement("button0") ;
    	assertTrue(b0 != null) ;
    	assertTrue(b0.dynamic) ;
    	assertTrue(b0.id.equals("button0")) ;
    	assertTrue(b0.type.equals(LabEntity.SWITCH)) ;
    	assertFalse(b0.getBooleanProperty("isOn")) ;
    }
    
    @Test
    public void test_env_simple_interaction() {
    	assertTrue(labRecruitsTestServer != null) ;
    	var env = new LabRecruitsEnvironment(new LabRecruitsConfig("minimal"));
    	LabWorldModel obs = env.interact("agent0", "button0", "") ;
    	LabEntity b0 = obs.getElement("button0") ;
    	assertTrue(b0 != null) ;
    	assertTrue(b0.getBooleanProperty("isOn")) ;
    
    }
    
    @Test
    public void test_env_observation() {
    	assertTrue(labRecruitsTestServer != null) ;
    	var env = new LabRecruitsEnvironment(new LabRecruitsConfig("square2"));
    	LabWorldModel obs = env.observe("agent0") ;
    	// check if all buttons are observed:
    	var b0 = obs.getElement("button0") ;
    	assertFalse(b0.getBooleanProperty("isOn")) ;
    	var b1 = obs.getElement("button1") ;
    	assertFalse(b1.getBooleanProperty("isOn")) ;
    	var b2 = obs.getElement("button2") ;
    	assertFalse(b2.getBooleanProperty("isOn")) ;
    	// check if the door is observed:
    	var door0 = obs.getElement("door0") ;
    	assertFalse(door0.getBooleanProperty("isOpen")) ;
    	// check if the color screen is observed:
    	var cs0 = obs.getElement("escreen0") ;
    	System.out.println(">>> " + cs0.getStringProperty("color")) ;
    	assertTrue(cs0.getStringProperty("color").equals("CS 0 0 0")) ;
    }
    
    @Test
    public void test_env_observation_with_short_view_distance() {
    	assertTrue(labRecruitsTestServer != null) ;
    	var config = new LabRecruitsConfig("square2") ;
    	config.view_distance = 5 ; // set view-distance to 5, rather than the standard 10
    	var env = new LabRecruitsEnvironment(config);
    	LabWorldModel obs = env.observe("agent0") ;
    	// check that button 0 and 2 are visible:
    	assertTrue(obs.getElement("button0") != null) ;
    	assertTrue(obs.getElement("button2") != null) ;
    	// check that that other interactables are not visible:   	
    	assertTrue(obs.getElement("button1") == null) ;
    	assertTrue(obs.getElement("door0") == null) ;
    	assertTrue(obs.getElement("screen0") == null) ;

    }
    
    @Test
    public void test_env_sending_illegal_commands() throws InterruptedException {
    	assertTrue(labRecruitsTestServer != null) ;
    	
    	float InteractionRange = 0.35f ;
    	
    	var config = new LabRecruitsConfig("square1") ;
    	var env = new LabRecruitsEnvironment(config);
    	// agent-1 does not exist, so the following command is illegal. LR will not send back any
    	// response, but our LR-env will eventually time-out
    	
    	// hit_RETURN() ;
    	
    	LabWorldModel obs0 = env.observe("agent1") ;
    	assertTrue(obs0 == null) ;

    	// sending a legal command:
    	obs0 = env.observe("agent0") ;
    	assertTrue(obs0 != null && obs0.agentId.equals("agent0")) ;
    	
    	// try to interact with button0. This should not work because it is too far, but currently
    	// LR will just happily execute this command. FIX THIS (at the LR side).
    	obs0 = env.interact("agent0","button0","") ;
    	assertTrue(obs0 != null) ;

    	//System.out.println("" + obs0) ;
    	// This one is illegal because button1 does not exist. LR will not send back any
    	// response, but our LR-env will eventually time-out
    	obs0 = env.interact("agent0","button1","") ;
    	assertTrue(obs0 == null) ;
    	
    	//hit_RETURN() ;
    }
    
    @Test
    public void test_env_interaction() throws InterruptedException {
    	assertTrue(labRecruitsTestServer != null) ;
    	
    	float InteractionRange = 0.35f ;
    	
    	var config = new LabRecruitsConfig("square2") ;
    	var env = new LabRecruitsEnvironment(config);
    	LabWorldModel obs0 = env.observe("agent0") ;
        var button0Position = obs0.getElement("button0").position ;
        System.out.println(">>> agent position: " + obs0.position) ;
        System.out.println(">>> button0 position: " + button0Position) ;
        var distance = Float.MAX_VALUE ;
        for (int k=0; k<20; k++) {
        	env.moveToward("agent0",obs0.position, button0Position) ;
            Thread.sleep(30);
        	obs0 = env.observe("agent0") ;
        	var p0 = obs0.position.copy() ;
        	var p1 = button0Position.copy() ;
        	p0.y = 0 ;
        	p1.y = 0 ;
        	distance = Vec3.dist(p0,p1) ;
            System.out.println(">>> agent position: " + obs0.position + ", distance="+ distance) ;
            if (distance <= InteractionRange) {
            	System.out.println(">>>  close enough to the button0, now toggling it") ;
            	var obs1 = env.interact("agent0", "button0", "") ;
            	var button0State = obs1.getElement("button0").getBooleanProperty("isOn") ;
            	var door0State = obs1.getElement("door0").getBooleanProperty("isOpen") ;
            	System.out.println(">>> button0 isOn: " + button0State) ;
            	System.out.println(">>> door0 isOpen: " + door0State) ;
            	assertTrue(button0State) ;
            	assertTrue(door0State) ;
            	break ;
            }
        }
        
        if (distance > InteractionRange) 
        	fail("Fail to get close enough to button-0.") ;
        
        //System.out.println("Hit RETURN to continue.")  ;
        //new Scanner(System.in) . nextLine() ;
        
    }
    
}