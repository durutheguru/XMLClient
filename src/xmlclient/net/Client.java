/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlclient.net;

/**
 *
 * @author Duru Dumebi Julian
 */

import xmlclient.ui.dialog.Message;
import xmlclient.ui.ClientFrame;
import xmlclient.ui.dialog.ViewDBDialog;
import xmlclient.xml.XMLDocumentConstant;
import xmlclient.xml.XMLTableModel;
import xmlclient.xml.XMLProcessor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.net.UnknownHostException;

import java.util.Observable;

import org.w3c.dom.Document;

import code.concurrent.ThreadExecutor;
import javax.xml.parsers.ParserConfigurationException;

public class Client extends Observable implements Runnable{
    
    private static Client instance;
    
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
    private Client(String ip, int port) throws UnknownHostException, IOException{
        socket = new Socket(ip, port);
        initializeStreams();
    }
    
    private void initializeStreams() throws IOException {
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
    }
    
    @Override
    public void run(){
        logMessage("LOG: Client is currently running.");
        while (!socket.isClosed()){
            try{
                Document response = (Document) in.readObject();
                logMessage("LOG: Response message received from server");
                XMLDocumentConstant docType = XMLProcessor.getDocumentType(response);
                
                if (docType ==XMLDocumentConstant.SPECIAL_RESPONSE){
                    String type = response.getDocumentElement().getAttribute("type");
                    
                    if (type.equals("viewdb"))
                        selectDatabase(response);                    
                }
                else if (docType == XMLDocumentConstant.RESULT_SET) 
                    XMLTableModel.getInstance().setDataSource(response, response.getDocumentElement().getAttribute("sql"));
                else {
                    XMLTableModel.getInstance().clearData();
                    if (docType == XMLDocumentConstant.SUCCESS_MSG)
                        Message.showMessageDialog(XMLProcessor.getMessage(response), "Success", ClientFrame.getInstance().getMessageParent());
                    else if (docType == XMLDocumentConstant.ERROR_MSG)
                        Message.showErrorDialog(XMLProcessor.getMessage(response), ClientFrame.getInstance().getMessageParent());
                    else if (docType == XMLDocumentConstant.OTHER)
                        Message.showErrorDialog("Unrecongnized response from server", ClientFrame.getInstance().getMessageParent());
                }
            }
            catch(ClassNotFoundException | ParserConfigurationException ex){
                logMessage("ERROR: " + ex.getMessage());
                ex.printStackTrace();
            }
            catch(IOException ex){
                logMessage("ERROR: A network error has occured. " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
        
        try{
            stopClient();
        }
        catch(IOException io){}
    }
    
    private void selectDatabase(Document response) throws ParserConfigurationException, IOException{
        ViewDBDialog.setDBData(XMLProcessor.extractDBsFromResponse(response));
        String db = ViewDBDialog.showNew();
        if (db != null){
            ClientFrame.getInstance().getIFrame().setDB(db);
            sendRequest(XMLProcessor.convertToRequest("show tables"));
        }
    }
    
    public void sendRequest(Document request) throws IOException {
        out.writeObject(request);
        out.flush();
    }
    
    private void logMessage(String msg) {
        setChanged();
        notifyObservers(msg);
    }
    
    public static synchronized Client getInstance(){
        if (instance == null)
            throw new NullPointerException("Client has not been instantiated");
        
        return instance;
    }
    
    public static synchronized void runClient(String ip, int port) throws UnknownHostException, IOException{
        if (instance != null)
            throw new IllegalStateException("The Client is already live");
        
        instance = new Client(ip, port);
        ThreadExecutor.run(instance);
    }
    
    public static synchronized void stopClient() throws IOException {
        if (instance != null){
            instance.logMessage("CLIENT_DISCONNECTED");
            instance.logMessage("LOG: Currently stopping client.");
            instance.out.close();
            instance.in.close();
            instance.socket.close();
            instance = null;      
        }
    }
    
}
