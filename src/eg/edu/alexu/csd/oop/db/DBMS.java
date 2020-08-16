/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eg.edu.alexu.csd.oop.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author crap
 */
public class DBMS implements Database{
    private static final String CREATETABLE = "((CREATE TABLE )([a-zA-Z0-9]+ )[(](([a-zA-Z]+ )(int|varchar)[,] )*([a-zA-Z])+ (int|varchar)[)][;])";
    private static final String DROPTABLE = "(DROP TABLE )([a-zA-Z0-9]+)[;]";
    private static final String INSERTINTOTABLE = "((INSERT INTO )([a-zA-Z0-9]+ )[(]((([a-zA-Z]+ )(int|varchar)[,] )*([a-zA-Z])+ (int|varchar))[)]( VALUES )[(]([a-zA-Z0-9]+, )*([a-zA-Z0-9]+)[)][;])";
    private static final String DELETEFROMTABLE = "(DELETE FROM )([a-zA-Z0-9]+ )(WHERE )([a-zA-Z0-9]+)( )*[<>=]( )*([a-zA-Z0-9]+)[;]";
    private static final String SELECTFROMTABLE = "((SELECT)|(select)) ([a-zA-Z]+, )*[a-zA-Z]+( FROM )([a-zA-Z0-9]+ )(WHERE )[a-zA-Z0-9]+( )*[<>=]( )*([a-zA-Z0-9]+)[;]";
    @Override
    public boolean executeStructureQuery(String query) throws SQLException {
       
        if(Pattern.matches(DROPTABLE, query)){
            if(new File(SQLInterpreter.dropTableRealization(query)+".xml").exists()){
                try {
                    Parser.dropTable(SQLInterpreter.dropTableRealization(query));
                } catch (IOException | XMLStreamException ex) {
                    Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
                }
                return true;
            }else{
                System.out.println("File doesn't exist.");
                JOptionPane.showMessageDialog(null, "File doesn't exist.", "Error!", JOptionPane.INFORMATION_MESSAGE);
            }
            
        }else if(Pattern.matches(CREATETABLE, query)){
            if(!new File(SQLInterpreter.createTableRealization(query)[0]+".xml").exists()){
                try {
                Parser.createTable(SQLInterpreter.createTableRealization(query));
                Parser.createXSD(SQLInterpreter.createTableRealization(query));
                if(Validation.checkValidation(SQLInterpreter.createTableRealization(query)[0])){
                System.out.println("Creation Successful");
                JOptionPane.showMessageDialog(null, "Creation Successful", "Tuple created!", JOptionPane.INFORMATION_MESSAGE);

            }else{
                System.out.println("Invalid file");
                JOptionPane.showMessageDialog(null, "Invalid file", "Error!", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
                return true;
            } catch (Exception ex) {
                Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
                  
                return false;
            }
            }else{
                System.out.println("File already exists.");
                JOptionPane.showMessageDialog(null, "File already exists.", "Error!", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
                
        }else{
            System.out.println("[SQL syntax error] Please check input.");
            JOptionPane.showMessageDialog(null, "[SQL syntax error] Please check input.", "Error!", JOptionPane.INFORMATION_MESSAGE);
        }
        return false;
        
    }

    @Override
    public Object[][] executeRetrievalQuery(String query) throws SQLException {
        if(Pattern.matches(SELECTFROMTABLE, query)){
            try {
                if(new File(SQLInterpreter.selectFromTable(query).get(0)+".xml").exists()){
                    try {
                        
                        
                        Object[][] list = Parser.selectFromTable(query);
                        
                        Object[][]organizedList = new String[list[0].length][list.length];
                        for(int i=0;i<list[0].length;i++){
                            for(int j=0;j<list.length;j++){
                           
                                organizedList[i][j] = list[j][i];
                            }
                         
                        }
                        System.out.println("------------------------------------------------------------------------");
                        for(int i =0;i<list[0].length;i++){
                            for(int j=0;j<list.length;j++){
                                System.out.print(organizedList[i][j] +"        ");
                            }
                            System.out.println();
                            System.out.println("------------------------------------------------------------------------");
                        }
                        System.out.println(Arrays.deepToString(organizedList));
                        JOptionPane.showMessageDialog(null, "Selection Successful\n" + Arrays.deepToString(organizedList) , "Successful!", JOptionPane.INFORMATION_MESSAGE);
                        return organizedList;
                    } catch (IOException ex) {
                        Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    
                }else{
                    System.out.println("File doesn't exist.");
                    JOptionPane.showMessageDialog(null, "File doesn't exist.", "Error!", JOptionPane.INFORMATION_MESSAGE);
                    String[][] list = new String[0][0];
                    return list;
                }
            } catch (IOException ex) {
                Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            System.out.println("[SQL syntax error] Please check input.");
            JOptionPane.showMessageDialog(null, "[SQL syntax error] Please check input.", "Error!", JOptionPane.INFORMATION_MESSAGE);
            String[][] list = new String[0][0];
                return list;
            
        }
        String[][] list = new String[0][0];
                return list;
    }
    @Override
    
    public int executeUpdateQuery(String query) throws SQLException {
        if(Pattern.matches(INSERTINTOTABLE, query)){
            if(new File(SQLInterpreter.getTableName(query)+".xml").exists()){
                try {
                   int changes = Parser.insertIntoTable(query);
                    
                   return changes;
                } catch (XMLStreamException ex) {
                    Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }else{
                System.out.println("File doesn't exist.");
               JOptionPane.showMessageDialog(null, "File doesn't exist.", "Error!", JOptionPane.INFORMATION_MESSAGE);
            }
    }else if(Pattern.matches(DELETEFROMTABLE, query)){
        if(new File(SQLInterpreter.getTableName(query)+".xml").exists()){
            try {
                    int changes = Parser.deleteFromTable(query);
                    
                    return changes;
                } catch (Exception ex) {
                    Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
                }
        }else{
            System.out.println("File doesn't exist.");
            JOptionPane.showMessageDialog(null, "File doesn't exist.", "Error!", JOptionPane.INFORMATION_MESSAGE);
            
        }
    }else{
            System.out.println("[SQL syntax error] Please check input.");
            JOptionPane.showMessageDialog(null, "[SQL syntax error] Please check input.", "Error!", JOptionPane.INFORMATION_MESSAGE);
    }
        return 0;
    }
    
}
