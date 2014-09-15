/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlclient.ui;

/**
 *
 * @author Duru Dumebi Julian
 */

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import javax.swing.JLabel;
import javax.swing.AbstractAction;

public class ActionLabel extends JLabel{
    
    private AbstractAction action;
    
    public ActionLabel(){
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    public ActionLabel(AbstractAction action){
        this();
        setClickAction(action);
    }
    
    public ActionLabel(String text){
        super("<html><a href=\"\">" + text + "</a></html>");
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    public ActionLabel(String text, AbstractAction action){
        this(text);
        setClickAction(action);
    }
    
    @Override
    public void setText(String text){
        super.setText("<html><a href=\"\">" + text + "</a></html>");
    }
    
    protected final void setClickAction(final AbstractAction action){
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if (ActionLabel.this.isEnabled())
                    action.actionPerformed(new ActionEvent(ActionLabel.this, e.getID(), "Clicked"));
            }
        });
        this.action = action;
    }
    
    public AbstractAction getAction(){
        return action;
    }
    
}
