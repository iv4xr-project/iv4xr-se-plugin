package environments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.net.SocketException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import logger.PrintColor;

/**
 * Provide a reader/writer to a socket to communicate with the system under test.
 * This class only provides raw communication over socket, where we can send
 * and receive an object. When an object is sent, it will be serialized to 
 * a Json string. Likewise, the system under test is assumed to send each response
 * object as a Json string.
 */
public class SocketReaderWriter {
	
	public static boolean debug = false ;
	
	
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    
    // Configuring the json serializer/deserializer. Register custom serializers
    // here.
    // Transient modifiers should be excluded, otherwise they will be send with json
    private static Gson gson = new GsonBuilder()
            .serializeNulls()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .create();
    
    
    /**
     * Constructor. Will setup the needed socket to communicate with the given host
     * at the given port.
     */
    public SocketReaderWriter(String host, int port) {
        int maxWaitTime = 20000;
        System.out.println(String.format("Trying to connect with %s on %s:%s (will time-out after %s seconds)", PrintColor.BLUE("Unity"), host, port, maxWaitTime/1000));

        long startTime = System.nanoTime();

        while (!socketReady() && millisElapsed(startTime) < maxWaitTime){
            try {
                socket = new Socket(host, port);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException ignored) { }
        }
        /* WP: disabled. This is now fixed.
        try {
        	// set the socket to timeout in 5 secs
        	// Currently LR just hang when we try to send an illegal command. This should be fixed,
        	// for now we place this time-out.
        	socket.setSoTimeout(5000) ;
        }
        catch(SocketException e) {
        	System.out.println("Failing to set timeout on the socketL: " + e.getMessage()) ;
        }
        */

        if(socketReady()){
            System.out.println(String.format("%s: Connected with %s on %s:%s", PrintColor.SUCCESS(), PrintColor.UNITY(), host, port));
        }
        else{
            System.out.println(String.format("%s: Could not establish a connection with %s, please start %s before creating a GymEnvironment.", PrintColor.FAILURE(), PrintColor.UNITY(), PrintColor.UNITY()));
        }
    }
    
    /**
     * @return true if the socket and readers are not null
     */
    private boolean socketReady(){
        return socket != null && reader != null && writer != null;
    }

    /**
     * @param startTimeNano the start time in long
     * @return the elapsed time from the start time converted to milliseconds
     */
    private float millisElapsed(long startTimeNano){
        return (System.nanoTime() - startTimeNano) / 1000000f;
    }
    
    /**
     * Send an object to the system under test. The object will first be serialized
     * to a json string, so it is assumed that the json serializer knows how to
     * handle the object.
     */
    public void write(Object packageToSend) {
    	String json = gson.toJson(packageToSend) ;
    	if (debug) {
        	System.out.println("** SENDING: " + json);
        }
    	writer.println(json);
    }
    
    /**
     * Read an object that was sent by the system under test. The object will be
     * received as a json string, which is then converted into an instance of the
     * given class. It is assumed that the json deserializer knows how to do this.
     * The resulting object is then returned.
     */
    public <T> T read(Class<T> expectedClassOfResultObj) throws IOException {
    	
    	String response = reader.readLine() ; 
        // we do not have to cast to T, since req.responseType is of type Class<T>
        if (debug) {
        	System.out.println("** RECEIVING: " + response);
        }
        //return null ;
    	return gson.fromJson(response,expectedClassOfResultObj);
    }

    /**
     * Close the socket/reader/writer
     * @throws IOException 
     */
    public void close() throws IOException {
         if (reader != null) reader.close();
         if (writer != null) writer.close();
         if (socket != null) socket.close();
         System.out.println(String.format("%s: Disconnected from the host", PrintColor.SUCCESS()));
    }
}
