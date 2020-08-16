/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eg.edu.alexu.csd.oop.db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 *
 * @author crap
 */


public class DefaultValues {
  private static HashMap<String,HashMap> allData = new HashMap();
  private  HashMap <String , String> data = new HashMap();
    
    public static void setDefaultValues(String fileName) throws FileNotFoundException, IOException { 
        try{
        FileReader op = new FileReader(fileName+".xsd");
        BufferedReader br = new BufferedReader(op);       
        DefaultValues dv = new DefaultValues();
        
try {    
    int i=0;
    
    String line = br.readLine();
    while (line != null) {
        if(line.contains("attribute")){
            String extracted = (line).replace("         <xs:attribute name = ", "").replace("\"", "").replace("type = ", "").replace("xs:", "").replace(" use = optional/>", "");
            String[] ex = extracted.split(" ");
            if(ex[1].equals("integer")){
                
                dv.data.put(ex[0], "0");
                i++;
            }else if(ex[1].equals("string")){
                
                dv.data.put(ex[0], "");
                i++;
            }else{
                System.out.println("Invalid File");
                JOptionPane.showMessageDialog(null, "Invalid File", "Error!", JOptionPane.INFORMATION_MESSAGE);
                System.exit(1);
            }
        line = br.readLine();
        }else{
            line = br.readLine();
        }
        
    }
    if(i==0){
        System.out.println("File has no attributes or is invalid.");
        JOptionPane.showMessageDialog(null, "File has no attributes or is invalid.", "Error!", JOptionPane.INFORMATION_MESSAGE);
        return;
    }
    
    
} finally {
    br.close();
    op.close();
}
    allData.put(fileName, dv.data);
    br.close();
    op.close();
    }catch(Exception e){
            System.out.println("File invalid or doesn't Exist");
            JOptionPane.showMessageDialog(null, "File invalid or doesn't Exist", "Error!", JOptionPane.INFORMATION_MESSAGE);
    }
}
    public static HashMap getDefaultValues(){
        return allData;
    }
    
}
