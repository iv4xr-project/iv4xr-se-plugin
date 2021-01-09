/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package environments;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logger.PrintColor;
import nl.uu.cs.aplib.mainConcepts.Environment;

import java.io.*;
import java.lang.reflect.Modifier;
import java.net.Socket;

public class SocketEnvironment extends Environment {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    // transient modifiers should be excluded, otherwise they will be send with json
    private static Gson gson = new GsonBuilder()
            .serializeNulls()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .create();

    SocketEnvironment(String host, int port) {

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
     * Close the socket/reader/writer
     */
    public boolean close() {

        // try to disconnect
        boolean success = getResponse(Request.disconnect());

        if(success){
            try {
                if (reader != null)
                    reader.close();
                if (writer != null)
                    writer.close();
                if (socket != null)
                    socket.close();

                System.out.println(String.format("%s: Disconnected from the host", PrintColor.SUCCESS()));

            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println(String.format("%s: Could not disconnect from the host by closing the socket.", PrintColor.FAILURE()));
                return false;
            }
        }
        else {
            System.out.println(String.format("%s: Unity does not respond to a disconnection request.", PrintColor.FAILURE()));
        }

        return success;
    }

    /**
     * @param cmd representing the command to send to the real environment.
     * @return an object that the real environment sends back as the result of the
     * command, if any.
     */
    @Override
    protected Object sendCommand_(EnvOperation cmd) {
        // The Environment super class uses sendCommand_ to send the json object
        String json = (String) cmd.arg;
        switch (cmd.command) {
            case "debug":
                return debug(json);
            case "request":
                try {
                    // write to the socket
                    writer.println(json);
                    // read from the socket
                    return reader.readLine();
                } catch (IOException ex) {
                    System.out.println("I/O error: " + ex.getMessage());
                    return null;
                }
        }
        throw new IllegalArgumentException();
    }

    /**
     * This method provides a higher level wrapper over Environment.sendCommand. It
     * calls Environment.sendCommand which in turn will call SocketEnvironment.sendCommand_
     * It will also cast the json back to type T.
     * @param req
     * @param <T> any response type, make sure Unity actually sends this object back
     * @return response
     */
    public <T> T getResponse(Request<T> req) {
    	// WP note:
    	// the actual id of the agent and the id of its target (if it interacts with 
    	// something) are put inside the req object ... :|
        String json = (String) sendCommand("APlib", "Unity", "request", gson.toJson(req));
        // we do not have to cast to T, since req.responseType is of type Class<T>
        //System.out.println(json);
        return gson.fromJson(json, req.responseType);
    }

    private String debug(String json){
        System.out.println("SENDING:" + json);
        return null;
    }
}
