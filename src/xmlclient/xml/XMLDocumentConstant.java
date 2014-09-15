/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlclient.xml;

/**
 *
 * @author Duru Dumebi Julian
 */

public enum XMLDocumentConstant {
    RESULT_SET(0),
    SUCCESS_MSG(1),
    ERROR_MSG(2),
    SPECIAL_RESPONSE(3),
    OTHER(4);

    int value;

    private XMLDocumentConstant(int val){
        this.value = val;
    }

    public int getValue(){
        return value;
    }
}
