/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlclient.xml;

/**
 *
 * @author Duru Dumebi Julian
 */

import javax.swing.table.AbstractTableModel;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

public class XMLTableModel extends AbstractTableModel{
    
    private int rowCount = 0;
    private int colCount = 0;
    private String sql;
    private Document documentSource;
    private String[] columnHeaders;
    private String[][] tableData;
    
    private static XMLTableModel instance;
    
    private XMLTableModel(){}
    
    private void initializeTableData(){
        Element root = documentSource.getDocumentElement();
        Element columns = (Element)root.getElementsByTagName("column-names").item(0);
        NodeList columnElements = columns.getElementsByTagName("column");
        
        colCount = columnElements.getLength();
        columnHeaders = new String[colCount];
        
        for (int i = 0; i < columnHeaders.length; i++) {
            Element cNode = (Element)columnElements.item(i);
            columnHeaders[i] = cNode.getFirstChild().getNodeValue();
        }
        
        Element rSetData = (Element)root.getElementsByTagName("resultset-data").item(0);   
        NodeList rows = rSetData.getElementsByTagName("row");
        rowCount = rows.getLength();
        
        tableData = new String[rowCount][colCount];
        
        for (int i = 0; i < rowCount; i++){
            Element row = (Element)rows.item(i);
            NodeList data = row.getElementsByTagName("data");
            
            for (int j = 0; j < colCount; j++){
                try{
                    tableData[i][j] = ((Element)data.item(j)).getChildNodes().item(0).getNodeValue();            
                }
                catch(Exception e){
                    continue;
                }
            }
        }
    }
    
    public void setDataSource(Document doc, String query){
        documentSource = doc;
        sql = query;
        initializeTableData();
        fireTableStructureChanged();
    }
    
    public Document getDocumentSource(){
        return documentSource;
    }
    
    public String getSQLSource(){
        return sql;
    }
    
    @Override
    public String getColumnName(int column){
        return columnHeaders[column];
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return colCount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {        
        Object data;
        
        try{
            data = tableData[rowIndex][columnIndex];
        }
        catch(ArrayIndexOutOfBoundsException e){
            data = "";
        }
        
        return data;
    }
    
    public void clearData(){
        reinitialize();
    }
    
    private void reinitialize(){
        rowCount = 0;
        colCount = 0;
        sql = "";
        documentSource = null;
        columnHeaders = null;
        tableData = null;
        
        fireTableStructureChanged();
    }
    
    public synchronized static XMLTableModel getInstance(){
        if (instance == null)
            instance = new XMLTableModel();
        
        return instance;
    }
    
}
