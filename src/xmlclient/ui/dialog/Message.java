/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlclient.ui.dialog;

/**
 *
 * @author Duru Dumebi Julian
 */

import java.awt.Component;
import javax.swing.JOptionPane;

public class Message {
    
    public static void showErrorDialog(String msg, Component parent){
        JOptionPane.showMessageDialog(parent, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showMessageDialog(String msg, String title, Component parent){
        JOptionPane.showMessageDialog(parent, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void showMessageDialog(String msg, String title, Component parent, int type){
        JOptionPane.showMessageDialog(parent, msg, title, type);
    }
    
}
