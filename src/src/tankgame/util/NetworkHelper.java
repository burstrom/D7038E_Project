/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame.util;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;

/**
 * Example of networking with SpiderMonkey and threads in 
 * jMonkeyEngeine (Java).  
 * 
 * @author hj
 */
public class NetworkHelper {

    /*
     * The constants below work for me at home but must be changed in order
     * to work elsewhere.
     */
    public static final int PORT_NUMBER = 6003;
    public static final String HOST_2 = "localhost";
    public static final String HOST_1 = "localhost"; 
    public static final String HOSTNAME = HOST_2;
    
    /*
     * Constans below is added for the specific Operationtypes
     * 200+ is administrative operations. 100-199 means game mechanic operations
     * Network OPeration
     */
    public static final int NOPUndefined = 0;
    public static final int NOPMove = 101;
    public static final int NOPCollision = 102;
    public static final int NOPChat = 200;
    public static final int NOPMessage = 201;
    public static final int NOPBroadcast = 300;
    
    

    /*
     * This method must be called from all classes that send and/or receives 
     * messages. Both the server and the client classes do this. 
     */
    public static void initialiseSerializables() {
        /*
         * Supply all (yes all) message classes as argument in the method 
         * call below. For every message class M, you should add M.class. 
         * (Put a comma between the arguments.)
         */
        Serializer.registerClass(NetworkMessage.class);
    }

    /*
     * In this example, there is just one message type. 
     * 
     * The annotation "@Serializable" is crucial. Also, the message 
     * class must be a subclass of AbstractMessage (however, it need not be
     * an immediate subclass.)
     */
    @Serializable
    public static class NetworkMessage extends AbstractMessage {

        /*
         * A NetworkMessage contains just a string. 
         */
        private String message = "";
        private int operation = NOPUndefined; // Which type of command was issued, key input, move, fire.
        
        /*
         * Every message class must have a parameterless constructor.
         */
        public NetworkMessage() {
        }

        /*
         * In addition, the class can have any number of other constructors.
         */
        public NetworkMessage(int opCode, String message) {
            this.message = message;
            this.operation = opCode;
        }

        /*
         * For each piece of data stored in the message, we add a method 
         * that returns the data.
         */
        public String getMessage() {
            return message;
        }
        
        public int getOperation(){
            return operation;
        }
    }
}
