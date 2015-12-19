package tankgame;

import tankgame.util.Util;
import com.jme3.app.SimpleApplication;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import tankgame.util.Util.NetworkMessage;

/**
 * Example of networking with SpiderMonkey and threads in jMonkeyEngeine (Java).
 *
 * A server sends messages to a client triggered in two ways, either by
 * keystrokes or automatically every now and then. The client receives each
 * message and sends back an acknowledgement. The functionality can be followed
 * by printouts in the console made by both the server and the client.
 *
 * @author hj
 */
public class GameClient extends GameCommon {

    /*
     * Our server object that handles communication behind the scenes. 
     */
    private Client client;
    /*
     * The queue below is used to transfer incoming messages from the thread
     * that receives them (ClientNetworkMessageListener) to the jME thread.
     * This is not needed in this example - ClientNetworkMessageListener could
     * do the printout itself - but if something needs to be done with the s
     * cene graph, using enqueue and creating a Callable is vital. 
     */
    private ConcurrentLinkedQueue<String> messageQueue;

    public static void main(String[] args) {
        /*
         * Make sure all messages are serialized so we can use them.
         */
        Util.initialiseSerializables();

        GameClient app = new GameClient();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        try {
            /*
             * Open up a connection to the server. Note how the IP address 
             * and port number is taken from the class Util. 
             */
            client = Network.connectToServer(Util.hostName, Util.portNumber);
            client.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        /* 
         * Build a (very) small scene graph with just a white box.
         */
        Geometry geom = new CreateGeoms(this).createBox();
        rootNode.attachChild(geom);

        /*
         * Remember to create all objects (just declaring a reference 
         * variable does not create an object to which is the refers. No, 
         * you have to create the object explicitely.)
         */
        messageQueue = new ConcurrentLinkedQueue<String>();
        /*
         * Add a listener that will handle incoming messages (network packets).
         */
        client.addMessageListener(new ClientNetworkMessageListener());
    }

    @Override
    public void simpleUpdate(float tpf) {
        /*
         * Process everything in the queue, that is all messages that have 
         * arrived during the last tpf seconds. The queue is thread safe, so 
         * no messages will be lost. (Make sure all data structures you use 
         * are thread safe or strange and hard-to-find bugs will result.)
         */
        String message = messageQueue.poll();
        while (message != null) {
            System.out.println(message);
            message = messageQueue.poll();
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }

    /*
     * The class takes care of all incoming messages. 
     */
    private class ClientNetworkMessageListener implements MessageListener<Client> {

        /*
         * Whenever a message arrives, this method is called. 
         */
        public void messageReceived(Client source, Message m) {
            if (m instanceof NetworkMessage) {
                NetworkMessage message = (NetworkMessage) m;
                /*
                 * Add the message to the queue so the jME thread can find it
                 * when the next call to simpleUpdate is done. 
                 */
                messageQueue.add(message.getMessage());
                /*
                 * Respond (acknowledge) back to the server. 
                 */
                client.send(new NetworkMessage(Util.NOPMessage,message.getMessage() + " ID "+client.getId() +" ACK"));
            }
        }
    }

    /* 
     * This method will terminate threads gracefully. 
     */
    @Override
    public void destroy() {
        client.close();
        super.destroy();
    }
}
