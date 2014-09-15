/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlclient;

/**
 *
 * @author Duru Dumebi Julian
 */

import xmlclient.ui.ClientFrame;
import code.ui.Utils;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        History.readHistory();
        Utils.setNimbusLAF();
        ClientFrame frame = ClientFrame.getInstance();
        frame.setVisible(true);
        
        Runtime.getRuntime().addShutdownHook(new Thread(){
            
            @Override
            public void run(){
                History.saveHistory();
            }
            
        });
    }
}
