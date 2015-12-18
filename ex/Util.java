package comtest;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;

/**
 * Example of networking with SpiderMonkey and threads in 
 * jMonkeyEngeine (Java).  
 * 
 * @author hj
 */
public class Util {

    /*
     * The constants below work for me at home but must be changed in order
     * to work elsewhere.
     */
    public static final int portNumber = 6003;
    public static final String host2 = "10.0.1.11";
    public static final String host1 = "10.0.1.3"; 
    public static final String hostName = host2;

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

        /*
         * Every message class must have a parameterless constructor.
         */
        public NetworkMessage() {
        }

        /*
         * In addition, the class can have any number of other constructors.
         */
        public NetworkMessage(String message) {
            this.message = message;
        }

        /*
         * For each piece of data stored in the message, we add a method 
         * that returns the data.
         */
        public String getMessage() {
            return message;
        }
    }
}
