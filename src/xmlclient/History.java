/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlclient;

/**
 *
 * @author Duru Dumebi Julian
 */

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;

public class History {
    
    private static int counter = 0;
    private static ArrayList<String> queries = new ArrayList<>();
    
    private final static String FILE_URL = System.getProperty("user.home") + "\\xmlclient.ser";
    
    public static void addQuery(String q){
        queries.add(q);
        counter = queries.size() - 1;
    }
    
    public static String back(){
        if (canGoBack()){
            --counter;
            return queries.get(counter);
        }
        else
            return "";
    }
    
    public static String forward(){
        if (canGoForward()) {
            ++counter;
            return queries.get(counter);
        }
        else
            return "";
    }
    
    public static boolean canGoBack(){
        return counter > 0;
    }
    
    public static boolean canGoForward(){
        return counter < queries.size() - 1;
    }
    
    public static void clearHistory(){
        queries = new ArrayList<>();
        counter = 0;
    }
    
    public static ArrayList<String> getHistory(){
        return queries;
    }
    
    protected static void saveHistory(){
        try{
            File f =  new File(FILE_URL);
            if (!f.exists()){
                if (!f.createNewFile())
                    throw new IOException("Unable to create file");
            }
            
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f))) {
                out.writeObject(queries);
                out.flush();
            }
        }
        catch(IOException io){
            System.err.println("Unable to save history. Error: " + io.getMessage());
        }
    }
    
    protected static void readHistory(){
        try{
            File f = new File(FILE_URL);
            if (f.exists()){
                try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))){
                    queries = (ArrayList<String>)in.readObject();                    
                }
            }
        }
        catch(IOException | ClassNotFoundException ex){
            System.err.println("Unable to read history. Error: " + ex.getMessage());
        }
    }
    
}
