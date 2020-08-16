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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author crap
 */

public class SQLInterpreter {
    private static final String CREATETABLE = "((CREATE TABLE )([a-zA-Z0-9]+ )[(](([a-zA-Z]+ )(int|varchar)[,] )*([a-zA-Z])+ (int|varchar)[)][;])"; //ex CREATE TABLE student (name varchar, age int);
    private static final String DROPTABLE = "(DROP TABLE )([a-zA-Z0-9]+)[;]";
    private static final String INSERTINTOTABLE = "((INSERT INTO )([a-zA-Z0-9]+ )[(]((([a-zA-Z]+ )(int|varchar)[,] )*([a-zA-Z])+ (int|varchar))[)]( VALUES )[(]([a-zA-Z0-9]+, )*([a-zA-Z0-9]+)[)][;])"; // ex INSERT INTO student (name varchar, age int) VALUES (Mohamed, 24);
    private static final String DELETEFROMTABLE = "(DELETE FROM )([a-zA-Z0-9]+ )(WHERE )([a-zA-Z0-9]+)( )*[<>=]( )*([a-zA-Z0-9]+)[;]";
    private static final String SELECTFROMTABLE = "((SELECT)|(select)) ([a-zA-Z]+, )*[a-zA-Z]+( FROM )([a-zA-Z0-9]+ )(WHERE )[a-zA-Z0-9]+( )*[<>=]( )*([a-zA-Z0-9]+)[;]";
    public static void readQuery(String query) throws XMLStreamException, FileNotFoundException, SQLException{
        
        
        if(Pattern.matches(CREATETABLE, query)==true){
                 
                 DBMS dbms = new DBMS();
                 dbms.executeStructureQuery(query);     
        }else if(Pattern.matches(DROPTABLE, query)==true){
                
                 DBMS dbms = new DBMS();
                 dbms.executeStructureQuery(query); 
        
        }else if(Pattern.matches(DROPTABLE, query)==true){
            
        }else if(Pattern.matches(INSERTINTOTABLE, query)){
                DBMS dbms = new DBMS();
                dbms.executeUpdateQuery(query);
        }else if(Pattern.matches(DELETEFROMTABLE, query)){
            DBMS dbms = new DBMS();
            dbms.executeUpdateQuery(query);
        }else if(Pattern.matches(SELECTFROMTABLE, query)){
        DBMS dbms = new DBMS();
            dbms.executeRetrievalQuery(query);
        }else{
            System.out.println("[SQL Syntax Error] Please check input");
            JOptionPane.showMessageDialog(null, "[SQL Syntax Error] Please check input", "Error!", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    
    
    public static String[] createTableRealization(String query){
        ArrayList<String> params = new ArrayList();
        Pattern pattern = Pattern.compile("([a-zA-z0-9]+[\\s][a-zA-z0-9]+)");
             Matcher matcher = pattern.matcher(query);
             Pattern namePattern = Pattern.compile("([a-zA-z0-9]+) [(]");
             Matcher nameMatcher = namePattern.matcher(query);
             while(nameMatcher.find()){
                 params.add(nameMatcher.group().replace(" (", ""));
             }
             while(matcher.find()){
                   if(matcher.group().contains("varchar")){
                params.add(matcher.group());
                    }else if(matcher.group().contains("int")){
                        params.add(matcher.group());
                    }
                }
    String[] paramsarray = new String[params.size()];
         paramsarray =  params.toArray(paramsarray);
        return paramsarray;
    }
    
    public static String dropTableRealization(String query){
        String tableName = query.replace("DROP TABLE ", "").replace(";","");
        return tableName;
    }
    
    public static HashMap<String,String> insertIntoTableRealization(String query){
        HashMap<String,String> data = new HashMap<String,String>();
        ArrayList<String>keys = new ArrayList<String>();
        ArrayList<String>values = new ArrayList<String>();
        Pattern pattern = Pattern.compile("([a-zA-Z0-9]+ varchar)|([a-zA-Z0-9]+ int)|(VALUES )[(]([a-zA-Z0-9]+, )*[a-zA-Z0-9]+[)]");
        Matcher matcher = pattern.matcher(query);
        
        
        while(matcher.find()){
          String x = matcher.group();
            String y;
           if(x.contains("varchar")){
                y = x.replace(" varchar", "");
               keys.add(y);
                
            }else if(x.contains("int")){
               y = x.replace(" int", "");
              keys.add(y);
            }else if(x.contains("VALUES")){
                for(String f : x.replace("VALUES (", "").replace(")","" ).split(", ")){
                    values.add(f);
                }
        }
    }

        if(values.size()==keys.size()){
            for(int i =0;i<keys.size();i++){
                data.put(keys.get(i),values.get(i));
            }
            
            return data;
    
        
        }else{
            System.out.println("Invalid Syntax");
            JOptionPane.showMessageDialog(null, "Invalid Syntax", "Error!", JOptionPane.INFORMATION_MESSAGE);
            return null;
            
        }
    }
    
    public static String getTableName(String query){
        if(Pattern.matches(INSERTINTOTABLE, query)){
            String[] name = query.split(" ");
            return name[2];
        }else if(Pattern.matches(CREATETABLE, query)){
            String[] name = query.split(" ");
            return name[2];
        }else if(Pattern.matches(DELETEFROMTABLE, query)){
            String[] name = query.split(" ");
            return name[2];

        }else{
            System.out.println("Invalid");
            JOptionPane.showMessageDialog(null, "Invalid Syntax", "Error!", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
    }
    
    public static String[] deleteFromTableRealization(String query){
        String[] data = new String[3];
        String firstReg = "[a-zA-Z0-9]+( )*[><=]";
        String secondReg = "[><=]( )*[a-zA-Z0-9]+";
        String oprReg = "[<>=]";
        Pattern patt1 = Pattern.compile(firstReg);
        Pattern patt2 = Pattern.compile(secondReg);
        Pattern patt3 = Pattern.compile(oprReg);
        Matcher mat1 = patt1.matcher(query);
        Matcher mat2 = patt2.matcher(query);
        Matcher mat3 = patt3.matcher(query);
        while(mat1.find()){                 //1st operand
            data[0] = mat1.group().replace(" ","").replace("<", "").replace(">", "").replace("=","");
        }
        while(mat2.find()){                 //2nd operand
            data[1] = mat2.group().replace(" ","").replace("<", "").replace(">", "").replace("=","");
        }
        while(mat3.find()){                 //operation
            data[2] = mat3.group();
        }
        
        return data;
    }
    
    public static ArrayList<String> selectFromTable(String query) throws IOException{
        ArrayList <String> allData = new ArrayList<String> ();
        String nameReg = "( FROM )([a-zA-Z0-9]+ )(WHERE )";
        Pattern patt1 = Pattern.compile(nameReg);
        Matcher mat1 = patt1.matcher(query);
        while(mat1.find()){
            allData.add(mat1.group().replace(" FROM ", "").replace("WHERE ", "").replace(" ", ""));
        }
        String firstReg = "[ ][a-zA-Z0-9]+( )*[<>=]";
        Pattern patt2 = Pattern.compile(firstReg);
        Matcher mat2 = patt2.matcher(query);
        while(mat2.find()){
            allData.add(mat2.group().replace(" ", "").replace("<", "").replace(">", "").replace("=", ""));
        }
        String secondReg = "( )*([a-zA-Z0-9]+)[;]";
        Pattern patt3 = Pattern.compile(secondReg);
        Matcher mat3 = patt3.matcher(query);
        while(mat3.find()){
            allData.add(mat3.group().replace(" ", "").replace(";", ""));
        }
        String oprReg = "[<>=]";
        Pattern patt4 = Pattern.compile(oprReg);
        Matcher mat4 = patt4.matcher(query);
        while(mat4.find()){
            allData.add(mat4.group());
        }
        
        String[]data2=null;
        String colsReg = "(SELECT) ([a-zA-Z]+, )*[a-zA-Z]+( FROM )";
        Pattern patt5 = Pattern.compile(colsReg);
        Matcher mat5 = patt5.matcher(query);
        while(mat5.find()){
            data2 = mat5.group().replace("SELECT", "").replace("FROM", "").replace(" ", "").split(",");
        }
        DefaultValues.setDefaultValues(allData.get(0));
        
        for(String i : data2){
            if(((HashMap)(DefaultValues.getDefaultValues().get(allData.get(0)))).containsKey(i)){
                allData.add(i);
            }else{
                System.out.println("Invalid Parameters");
                JOptionPane.showMessageDialog(null, "Invalid Parameters", "Error!", JOptionPane.INFORMATION_MESSAGE);
          
                
                ArrayList<String>empty = new ArrayList();
                return empty;
            }
            
        }
        return allData;
    }
}