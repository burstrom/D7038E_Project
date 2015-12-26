package tankgame;


import tankgame.util.NetworkHelper;
import com.jme3.app.SimpleApplication;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultCaret;

/**
 * Example of networking with SpiderMonkey and threads in 
 * jMonkeyEngeine (Java). 
 * 
 * A server sends messages to a client triggered in two ways, either by
 * keystrokes or automatically every now and then. The client receives each
 * message and sends back an acknowledgement. The functionality can be followed
 * by printouts in the console made by both the server and the client.
 * 
 * Server part. 
 * 
 * @author hj
 */
public class GameServer extends GameCommon {

    /*
     * This is our server object that handles all communication. 
     */
    private Server server;
    /*
     * This is an area that will show up in a JFrame and in which message 
     * tests will be written.
     */
    private JTextArea textArea;

    public static void main(String[] args) {
        /*
         * Make sure all messages are serialized so we can use them.
         */
        NetworkHelper.initialiseSerializables();

        GameServer app = new GameServer();
        /*
         * "JmeContext.Type.Headless" means there will be no displayed 
         * graphics. However, despite this the server must maintain 
         * the same scene graph as the clients. It does so by calling all 
         * the ordinary methods, that work just fine (although there is 
         * no visible result). 
         */
        app.start(JmeContext.Type.Headless);

        /*
         * Below is a small text-based user interface.
         */
        System.out.println("Server console - echos text until 'q' is entered\n");

        // read lines and print them until "q" is encountered

        Scanner s = new Scanner(System.in);
        s.useDelimiter("\\n");
        skip:
        while (s.hasNext()) {
            String input = s.next();
            if (input.equals("q")) {
                break skip; // jump out of the loop marked "skip"
            }
            System.out.println(input);
        }
        s.close();
        System.out.println("Bye\n");
        System.exit(0); // end all server threads by brute force
    }

    GameServer() {
    }

    @Override
    public void simpleInitApp() {
        try {
            /*
             * Create the actual server object.
             */
            server = Network.createServer(NetworkHelper.PORT_NUMBER);
            server.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        /*
         * Build a (very) small scene graph.
         */
        Geometry geom = new CreateGeoms(this).createBox();
        rootNode.attachChild(geom);
        /*
         * Start a separat thread that will send messages to all connected 
         * clients every now and then. This thread will then create a 
         * KeyListener that will send messages whenever a key is typed.
         */
        new Thread(new AutomaticServerNetWrite("ServerNetWrite")).start();
        /*
         * Here we add, to the server, a listener that automatically handles 
         * all incoming messages. 
         */
        server.addMessageListener(new ServerListener());
    }

    @Override
    public void simpleUpdate(float tpf) {
        /*
         * In this simple example we do not update the scene graph but in 
         * a game we would of course do quite a lot here. 
         * 
         * For our purposes it is enough that we get a white box that will 
         * be visible at the clients but not the server, but that all still 
         * can change. 
         */
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }

     /* 
     * This method will terminate threads gracefully. 
     */
    @Override
    public void destroy() {
        server.close();
        super.destroy();
    }

    /*
     * Prints msg in the TextArea in the window that is not 
     * the jME window. 
     * 
     * @param msg 
     */
    private void print(String msg) {
        textArea.append(msg + "\n");
    }

    /*
     * Receives messages from clients. 
     */
    private class ServerListener implements MessageListener<HostedConnection> {

        /*
         * This method is automatically called whenever a message arrives 
         * from a client.
         */
        public void messageReceived(HostedConnection source, Message m) {

            if (m instanceof NetworkHelper.NetworkMessage) {
                NetworkHelper.NetworkMessage message = (NetworkHelper.NetworkMessage) m;
                /* 
                 * The only thing we do is to print the message string 
                 * contained in the message object.
                 */ 
                print(message.getMessage());
            } else {
                /*
                 * This should never happen. 
                 */
                print("Unknown message received");
            }
        }
    }

    /*
     * Sends a message to all clients when a(ny) key is pressed. 
     */
    private class ManualServerNetWrite implements KeyListener {

        /*
         * A counter to number the messages sent. This number is included 
         * in the text message. 
         */
        private int manualCounter = 1;

        ManualServerNetWrite() {
        }

        public void keyPressed(KeyEvent arg0) {
            /* do nothing */
        }

        public void keyReleased(KeyEvent arg0) {
            /* do nothing */
        }

        /*
         * Sends a message to all clients whenever a key is typed on the keybord
         * by sending over an order to the jME thread (the SimpleApplication,
         * know as "this").
         */
        public void keyTyped(KeyEvent e) {
            /*
             * "Eat" the key pressed (otherwise it will end up in the printout).
             */
            e.consume();
            /*
             * NB! Must be "final"!
             */
            final String m = "Manual (sent from jME thread) "
                    + (manualCounter++);
            /*
             * Print the string m.
             */   
            print(m);
            /*
             * Here several things happen. We see a call to the method enqueue 
             * on the object ServerMain.this (that is, a SimpleApplication, 
             * our jME thread). The argument is a new object of type Callable. 
             * What you see is the creation of an anonymous class, or actually
             * instance, that just contains a method call() with two Java 
             * statements. It is this method that will be called by the 
             * jME thread, the receiving thread. What is in this thread will be 
             * carried out by the jME thread when the next call to simpleUpdate
             * is made and before the method simpleUpdate is executed. 
             */
            Future result = GameServer.this.enqueue(new Callable() {
                /*
                 * Define what should be done by the receiving thread.
                 */
                public Object call() throws Exception {
                    /*
                     * Send a message to all clients (do a "broadcast"). 
                     */
                    server.broadcast(new NetworkHelper.NetworkMessage(NetworkHelper.NOPMessage,m));
                    /*
                     * A Callable have to return something so I choose to 
                     * return a boolean, but anything works. Here the result 
                     * is not used. 
                     */
                    return true;
                }
            });
        }
    }

    /*
     * Sends to clients every now and then. 
     */
    private class AutomaticServerNetWrite implements Runnable {

        /*
         * Constants used to create a JFRame and determine how often to send. 
         */
        private int XPOS = 600, YPOS = 700, WINDOW_SIZE = 700,
                SLEEP_MIN = 1000, SLEEP_EXTRA = 2000,
                autoCounter = 1;
        private Random rnd = new Random();

        /*
         * Creates a window with a TextArea separate from the jME thread. 
         * 
         * Adds a KeyListerer to the window. 
         */
        AutomaticServerNetWrite(final String name) {
            /*
             * Create a window and put "name" in the top bar.
             */
            JFrame frame = new JFrame(name);
            /*
             * The thread with the JFrame should terminate if we click
             * to kill the window.
             */
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            /*
             * The area in which messages will be printed. 
             */
            textArea = new JTextArea("");
            /*
             * Add a listener teh reacts to keys pressed and sends a messages 
             * to all clients whenever a(ny) key is typed. 
             */
            textArea.addKeyListener(new ManualServerNetWrite());
            /*
             * Tedious GUI details (google for info...)
             */
            DefaultCaret caret = (DefaultCaret) textArea.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            JScrollPane scroll = new JScrollPane(textArea,
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            frame.setSize(new Dimension(WINDOW_SIZE, WINDOW_SIZE));
            frame.setLocation(XPOS, YPOS);
            frame.add(scroll);
            frame.setVisible(true);
        }

       /*
        * This method is called when start() is called on a thread object. 
        */
        public void run() {
            /*
             * Eternal loop...
             */
            while (true) {
                String m = "Automatic (sent from ServerNetWrite) "
                        + (autoCounter++);
                print(m);
                /*
                 * Send to all clients. 
                 */
                server.broadcast(new NetworkHelper.NetworkMessage(NetworkHelper.NOPBroadcast,m));
                try {
                    /*
                     * Take a nap between SLEEP_MIN and SLEEP_MIN+SLEEP_EXTRA
                     * seconds.
                     */
                    Thread.sleep(SLEEP_MIN + rnd.nextInt(SLEEP_EXTRA));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

