/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;


public class GameMessage extends AbstractMessage {

    @Serializable
    public class HelloMessage  {
        private String hello;       // custom message data

        public HelloMessage() {
        }    // empty constructor

        public HelloMessage(String s) {
            hello = s;
        } // custom constructor
    }
    
}
