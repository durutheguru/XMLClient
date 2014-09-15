/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlclient.xml;

/**
 * @author Duru Dumebi Julian
 */

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.DOMException;
import xmlclient.ui.ClientFrame;


public class XMLProcessor {
    
    public static Document convertToRequest(String query) throws DOMException, ParserConfigurationException{
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        
        Element root = document.createElement("request");
        root.setAttribute("timestamp", System.currentTimeMillis() + "");
        root.setAttribute("database", ClientFrame.getInstance().getIFrame().getSelectedDatabase());
        
        Element sql = document.createElement("sql");
        Text q = document.createTextNode(query);
        
        sql.appendChild(q);        
        root.appendChild(sql);
        document.appendChild(root);
        
        return document;
    }
    
    public static Document createDatabasesRequest() throws DOMException, ParserConfigurationException {        
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        
        Element root = document.createElement("_request");
        root.setAttribute("timestamp", System.currentTimeMillis() + "");
        root.setAttribute("type", "viewdb");
        root.setAttribute("database", ClientFrame.getInstance().getIFrame().getSelectedDatabase());
        
        Element sql = document.createElement("sql");
        Text q = document.createTextNode("show databases");
        
        sql.appendChild(q);        
        root.appendChild(sql);
        document.appendChild(root);
        
        return document;
    }
    
    public static String getMessage(Document document) {
        XMLDocumentConstant type = getDocumentType(document);
        if (type == XMLDocumentConstant.ERROR_MSG || type == XMLDocumentConstant.SUCCESS_MSG){
            Element root = document.getDocumentElement();
            Element child = (Element)root.getFirstChild();
            
            return child.getFirstChild().getNodeValue();
        }
        else
            throw new IllegalArgumentException("Document does not contain a message.");
    }
    
    public static XMLDocumentConstant getDocumentType(Document document){
        Element root = document.getDocumentElement();
        
        if (root.getTagName().startsWith("_"))
            return XMLDocumentConstant.SPECIAL_RESPONSE;
        
        Element firstChild = (Element)root.getFirstChild();
        XMLDocumentConstant type;
        switch(firstChild.getTagName()){
            case "column-names":
                type = XMLDocumentConstant.RESULT_SET;
                break;
            case "success":
                type = XMLDocumentConstant.SUCCESS_MSG;
                break;
            case "error":
                type = XMLDocumentConstant.ERROR_MSG;
                break;
            default:
                type = XMLDocumentConstant.OTHER;                        
        }
        
        return type;
    }
    
    public static String[] extractDBsFromResponse(Document result){
        Element root = result.getDocumentElement();
        Element data = (Element)root.getElementsByTagName("resultset-data").item(0);
        NodeList rows = data.getElementsByTagName("row");
        int length = rows.getLength();
        
        String[] dbs = new String[length];
        for (int i = 0; i < dbs.length; i++) {
            Node dataItem = ((Element)rows.item(i)).getElementsByTagName("data").item(0);
            dbs[i] = dataItem.getFirstChild().getNodeValue();
        }
        
        return dbs;
    }
    
    
}
