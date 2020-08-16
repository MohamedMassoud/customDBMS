/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eg.edu.alexu.csd.oop.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.SAXException;

/**
 *
 * @author crap
 */

public class Parser {
private static HashMap<String, HashMap> alldata = new HashMap();

    /**
     * @param args the command line arguments
     * @throws javax.xml.stream.XMLStreamException
     * @throws java.io.FileNotFoundException
     */

    


    public static void createTable(String []array) throws Exception{  
            //createTable
            
            XMLOutputFactory factory = XMLOutputFactory.newFactory();
            FileOutputStream stream = new FileOutputStream(new File (array[0] + ".xml"));
            XMLStreamWriter writer = factory.createXMLStreamWriter(stream);
            //rintWriter dtd = new PrintWriter(array[0]+".dtd");
            
            
            writer.writeStartDocument("UTF-8", "1.0");
            
            writer.writeStartElement(array[0].replace(" varchar", "").replace(" int", ""));
            writer.writeStartElement("name");
            writer.writeAttribute("RowCount", "0");
            for(int l=1;l<array.length;l++){
                if(array[l].contains("varchar")){
                    writer.writeAttribute(array[l].replace(" varchar", ""),"");
               
                }else if(array[l].contains("int")){
                    
                    
                    writer.writeAttribute(array[l].replace(" int", ""), "0");
                }
                
            }
       
           writer.writeEndElement();
           writer.writeEndElement();
           writer.writeEndDocument();
           writer.flush();
           writer.close();
           stream.close();
            
            
    }

    public static void dropTable(String tableName) throws FileNotFoundException, IOException, XMLStreamException{
        /*XMLOutputFactory factory = XMLOutputFactory.newFactory();
        FileOutputStream stream = new FileOutputStream(new File (tableName+ ".xml"));
        stream.flush();
        stream.close();*/
        System.gc();
        File removedXML = new File(tableName+".xml");
        File removedXSD = new File(tableName+".xsd");
        if(removedXML.delete()==true){
            System.out.println("XML data file deleted.");
            JOptionPane.showMessageDialog(null, "Tuple data file deleted successfully.", "Successul!", JOptionPane.INFORMATION_MESSAGE);
        }else{
            System.out.println("can't delete file");
            JOptionPane.showMessageDialog(null, "Can't delete file.", "Error!", JOptionPane.INFORMATION_MESSAGE);
        }
        
        if(removedXSD.delete()==true){
            System.out.println("XSD validation file deleted");
            JOptionPane.showMessageDialog(null, "Tuple verification XSD data file deleted successfully.", "Successul!", JOptionPane.INFORMATION_MESSAGE);
        }else{
            System.out.println("can't delete file");
            JOptionPane.showMessageDialog(null, "Can't delete file.", "Error!", JOptionPane.INFORMATION_MESSAGE);
        }
        
       
    }
    public static void createXSD(String[] data) throws FileNotFoundException, XMLStreamException, SAXException, IOException{
        PrintWriter writer = new PrintWriter(data[0].replace(" varchar", "").replace(" int", "")+".xsd");
        writer.println("<?xml version = \"1.0\"?>\n" +
"\n" +
"<xs:schema xmlns:xs = \"http://www.w3.org/2001/XMLSchema\">\n" +
"   <xs:element name = '"+data[0].replace(" varchar", "").replace(" int", "")+"'>\n" +
"      <xs:complexType>\n" +
"         <xs:sequence>\n" +
"             <xs:element name = 'name' type = 'NameType' minOccurs = '0' \n" +
"                maxOccurs = 'unbounded' />\n" +
"         </xs:sequence>\n" +
"      </xs:complexType>\n" +
"   </xs:element>");
        writer.println();
        writer.println("   <xs:complexType name = \"NameType\">\n");
//"      <xs:sequence>");
writer.println("         <xs:attribute name = \""+"RowCount"+"\" type = \"xs:integer\" use = \"optional\"/>");
        for(int i=1; i<data.length;i++){
            if(data[i].contains("varchar")){
                writer.println("         <xs:attribute name = \""+data[i].replace(" varchar", "")+"\" type = \"xs:string\" use = \"optional\"/>");
            }else{
                if(data[i].contains("int")){
                    writer.println("         <xs:attribute name = \""+data[i].replace(" int", "")+"\" type = \"xs:integer\" use = \"optional\"/>");
                }
            }
            
        }
       // writer.println("      </xs:sequence>\n" +
writer.println("   </xs:complexType>			 \n" +
"</xs:schema>");
        

        writer.close();
        
        if(!Validation.checkValidation(data[0])){
          new File(data[0]+".xml").delete();
          new File(data[0]+".xsd").delete();
            System.out.println("Input Invalid");
            JOptionPane.showMessageDialog(null, "Input Invalid", "Error!", JOptionPane.INFORMATION_MESSAGE);
        }
       

    }
    public static int insertIntoTable(String query) throws FileNotFoundException, IOException, XMLStreamException, Exception{
if(SQLInterpreter.insertIntoTableRealization(query)==null){
            return 0;
        }
if(!Validation.checkValidation(SQLInterpreter.getTableName(query))){
    System.out.println("Invalid File");
    JOptionPane.showMessageDialog(null, "Invalid File", "Error!", JOptionPane.INFORMATION_MESSAGE);
    return 0;
}
    
   String id ="0";
        

            //delete selected node
           
            HashMap<String, String> design  = new HashMap <String, String>();
            XMLInputFactory ifactory=XMLInputFactory.newFactory();
  XMLOutputFactory ofactory=XMLOutputFactory.newFactory();
  StreamSource source =new StreamSource(SQLInterpreter.getTableName(query)+".xml");
  StreamResult result=new StreamResult(SQLInterpreter.getTableName(query)+".xml");
  try {
   XMLEventReader in=ifactory.createXMLEventReader(source);
   XMLEventWriter out=ofactory.createXMLEventWriter(result);
   
 DefaultValues.setDefaultValues(SQLInterpreter.getTableName(query));
        alldata = DefaultValues.getDefaultValues();

        design.putAll(alldata.get(SQLInterpreter.getTableName(query)));
        design.putAll(SQLInterpreter.insertIntoTableRealization(query));
        

 
   while(in.hasNext()){
 
    XMLEvent e=in.nextEvent();
    if(e.isStartElement() && ((StartElement)e).getName().getLocalPart().equalsIgnoreCase("name")){
     Attribute a=((StartElement)e).getAttributeByName(new QName("RowCount"));
     
     
     
     /*Pattern patt = Pattern.compile(" [a-zA-Z]+[=]");
        Matcher match = patt.matcher(e.toString());
        String v;
        while(match.find() && !stock.containsKey((v=match.group()))){
            System.out.println(match.group().replace(" ", "").replace("=", ""));
            stock.put(v.replace(" ", "").replace("=", ""), "");
            
        }
     
        System.out.println(stock +   "  REALESTOCK");
        design.putAll(stock);*/
        
        
        Iterator I2 = ((StartElement)e).getAttributes();
        
       
            while(I2.hasNext()){
            String n = ((Attribute)I2.next()).getName().toString();
            
            if("RowCount".equals(n)){
                String temp = a.getValue();
                
                if(Integer.parseInt(temp)>Integer.parseInt(id)){
                    id = temp;   
                   
                }
                
            }
            
        }
            
            
           
     if(a.getValue().equalsIgnoreCase("0")){
       
      in.next();
      continue;
     }
    }
    out.add(e);
   }
   in.close();
   out.close();
  
                id=String.valueOf(Integer.parseInt(id)+1) ;
                design.put("RowCount", id);
  } catch (XMLStreamException e) {
   e.printStackTrace();
  }
  
           
  
              //remove closing node
  
  RandomAccessFile f = new RandomAccessFile(SQLInterpreter.getTableName(query)+".xml", "rw");
        long length = f.length() -1;
        long temp = f.length()-1;
        String rootName = "</"+SQLInterpreter.getTableName(query)+">";
        long rootLength = rootName.length()-1;

        byte b;
        do {                     
            length -= 1;
            f.seek(length);
             b = f.readByte();
        } while(b != 10 && length > rootLength);
            if (length == rootLength) { 
                f.setLength(temp-rootLength);
                
            } else {
                f.setLength(temp-rootLength);
            
            }
            f.seek(temp-rootLength);
            long check = rootLength+1;
            for(long i=0;i<check;i++){
                f.writeBytes("");
            }
  
            
    
    XMLOutputFactory factory = XMLOutputFactory.newFactory();
            FileOutputStream stream = new FileOutputStream (new File(SQLInterpreter.getTableName(query)+".xml"),true);
            XMLStreamWriter writer = factory.createXMLStreamWriter(stream);
            //input filled row
            
           
            writer.writeStartElement("name");
            for(String n : design.keySet()){  
                    writer.writeAttribute(n, (String) design.get(n));
                    
                
            }
            writer.writeEndElement();
            
            
            writer.writeDTD("</"+SQLInterpreter.getTableName(query)+">");
            writer.writeEndDocument();
            //writer.flush();
            //writer.close();
            if(!Validation.checkValidation(SQLInterpreter.getTableName(query))){
                System.out.println("Parameters Invalid");
                JOptionPane.showMessageDialog(null, "Parameters Invalid", "Error!", JOptionPane.INFORMATION_MESSAGE);
                
                
              XMLInputFactory ifactory2=XMLInputFactory.newFactory();
  XMLOutputFactory ofactory2=XMLOutputFactory.newFactory();
  StreamSource source2 =new StreamSource(SQLInterpreter.getTableName(query)+".xml");
  StreamResult result2=new StreamResult(SQLInterpreter.getTableName(query)+".xml");
  try {
   XMLEventReader in=ifactory2.createXMLEventReader(source2);
   XMLEventWriter out=ofactory2.createXMLEventWriter(result2);
   while(in.hasNext()){
    XMLEvent e=in.nextEvent();
    if(e.isStartElement() && ((StartElement)e).getName().getLocalPart().equalsIgnoreCase("name")){
     Attribute a=((StartElement)e).getAttributeByName(new QName("RowCount"));
     if(a.getValue().equalsIgnoreCase(id)){
      in.next();
      continue;
     }
    }
    out.add(e);
   }
   in.close();
   out.close();
  } catch (XMLStreamException e) {
   e.printStackTrace();
  }
   
            }
            
            
            
            
            
            RandomAccessFile f2 = new RandomAccessFile(SQLInterpreter.getTableName(query)+".xml", "rw");
        long length2 = f.length() -1;
        long temp2 = f.length()-1;
        String rootName2 = "</"+SQLInterpreter.getTableName(query)+">";
        long rootLength2 = rootName2.length()-1;

        byte b2;
        do {                     
            length2 -= 1;
            f.seek(length2);
             b2 = f2.readByte();
        } while(b2 != 10 && length2 > rootLength2);
            if (length2 == rootLength2) { 
                f2.setLength(temp2-rootLength2);
                
            } else {
                f2.setLength(temp2-rootLength2);
            
            }
            f2.seek(temp2-rootLength2);
            long check2 = rootLength2+1;
            for(long i=0;i<check2;i++){
                f2.writeBytes("");
            }
            
            
            
            
            
            
            
            
            //insert new empty row
            
            //rintWriter dtd = new PrintWriter(array[0]+".dtd");
            
            //writer.writeStartElement(array[0].replace(" varchar", "").replace(" int", ""));
           /* System.out.println(stock + "STOCK");
            writer.writeStartElement("name");
            writer.writeAttribute("RowCount", "0");
            for(String n : stock.keySet()){
                System.out.println(n);
                if(SQLInterpreter.insertIntoTableRealization(query).get(n).chars().allMatch( Character::isDigit )){
                    writer.writeAttribute(n, "0");
      
                }else{
                    writer.writeAttribute(n, "");
                }
            }
            writer.writeEndElement();
            //writer.writeEndElement();*/
            writer.writeDTD("</"+SQLInterpreter.getTableName(query)+">");
            writer.writeEndDocument();
            stream.flush();
            stream.close();
            writer.flush();
            writer.close();
            f.close();
            f2.close();
            System.gc();
            
            System.out.println("Insertion Successful");
            JOptionPane.showMessageDialog(null, "Insertion Successful", "Successful!", JOptionPane.INFORMATION_MESSAGE);
            return Integer.parseInt(id);
            
            
    }
    public static int deleteFromTable(String query) throws IOException{
        int changes=0;
        if(!Validation.checkValidation(SQLInterpreter.getTableName(query))){
            System.out.println("File Invalid");
            JOptionPane.showMessageDialog(null, "File Invalid", "Error!", JOptionPane.INFORMATION_MESSAGE);
            return 0;
        }
        DefaultValues.setDefaultValues(SQLInterpreter.getTableName(query));
        HashMap allData = DefaultValues.getDefaultValues();
        HashMap dataMap = (HashMap) allData.get(SQLInterpreter.getTableName(query));
        String[] data = SQLInterpreter.deleteFromTableRealization(query);
        String first = data[0];
        String second = data[1];
        String operation = data[2];

        
        String name = SQLInterpreter.getTableName(query);
         //  (!temp.chars().allMatch( Character::isDigit ) && !second.chars().allMatch( Character::isDigit ))
         if(dataMap.containsKey(first)){
             String temp = (String) dataMap.get(first);
             if(((temp.chars().allMatch( Character::isDigit ) && second.chars().allMatch( Character::isDigit )) && !temp.equals("") )){
            
            XMLInputFactory ifactory=XMLInputFactory.newFactory();
  XMLOutputFactory ofactory=XMLOutputFactory.newFactory();
  StreamSource source =new StreamSource(name+".xml");
  StreamResult result=new StreamResult(name+".xml");
            switch(operation){
                case ">" :
                    
  try {
   XMLEventReader in=ifactory.createXMLEventReader(source);
   XMLEventWriter out=ofactory.createXMLEventWriter(result);
   while(in.hasNext()){
    XMLEvent e=in.nextEvent();
    if(e.isStartElement() && ((StartElement)e).getName().getLocalPart().equalsIgnoreCase("name")){
     Attribute a=((StartElement)e).getAttributeByName(new QName(first));
     if(Integer.parseInt(a.getValue())>Integer.parseInt(second)){
         changes++;
      in.next();
      continue;
     }
    }
    out.add(e);
   }
   in.close();
   out.close();
  } catch (XMLStreamException e) {
   e.printStackTrace();
  }
                    break;
                    
                case "<" :
                    try {
   XMLEventReader in=ifactory.createXMLEventReader(source);
   XMLEventWriter out=ofactory.createXMLEventWriter(result);
   while(in.hasNext()){
    XMLEvent e=in.nextEvent();
    if(e.isStartElement() && ((StartElement)e).getName().getLocalPart().equalsIgnoreCase("name")){
     Attribute a=((StartElement)e).getAttributeByName(new QName(first));
     if(Integer.parseInt(a.getValue())<Integer.parseInt(second)){
         changes++;
      in.next();
      continue;
     }
    }
    out.add(e);
   }
   in.close();
   out.close();
  } catch (XMLStreamException e) {
   e.printStackTrace();
  }
                    break;
               
                case "=" :
                    try {
   XMLEventReader in=ifactory.createXMLEventReader(source);
   XMLEventWriter out=ofactory.createXMLEventWriter(result);
   while(in.hasNext()){
    XMLEvent e=in.nextEvent();
    if(e.isStartElement() && ((StartElement)e).getName().getLocalPart().equalsIgnoreCase("name")){
     Attribute a=((StartElement)e).getAttributeByName(new QName(first));
     if(Integer.parseInt(a.getValue())==Integer.parseInt(second)){
         changes++;
      in.next();
      continue;
     }
    }
    out.add(e);
   }
   in.close();
   out.close();
  } catch (XMLStreamException e) {
   e.printStackTrace();
  }
                    break;
                    
                default:
                    System.out.println("Invalid expression");
                    JOptionPane.showMessageDialog(null, "Invalid expression", "Error!", JOptionPane.INFORMATION_MESSAGE);
                    break;
            }
        }
        
        
        else if(((!temp.chars().allMatch( Character::isDigit ) || temp.equals("")) && !second.chars().allMatch( Character::isDigit ))){
            
            XMLInputFactory ifactory=XMLInputFactory.newFactory();
  XMLOutputFactory ofactory=XMLOutputFactory.newFactory();
  StreamSource source =new StreamSource(name+".xml");
  StreamResult result=new StreamResult(name+".xml");
  
  switch(operation){
      case "=":
 try {
   XMLEventReader in=ifactory.createXMLEventReader(source);
   XMLEventWriter out=ofactory.createXMLEventWriter(result);
   while(in.hasNext()){
    XMLEvent e=in.nextEvent();
    if(e.isStartElement() && ((StartElement)e).getName().getLocalPart().equalsIgnoreCase("name")){
       
     Attribute a=((StartElement)e).getAttributeByName(new QName(first));
     
     if(a.getValue().equals(second)){
         changes++;
      in.next();
      continue;
     }
    }
    out.add(e);
   }
   
   in.close();
   out.close();
  } catch (XMLStreamException e) {
   e.printStackTrace();
  }
 
break;
                
                    
                default:
                    System.out.println("Invalid expression");
                    JOptionPane.showMessageDialog(null, "Invalid expression", "Error!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
     }else if(dataMap.containsKey(second)){
         String temp = (String) dataMap.get(second);
          
         if(((temp.chars().allMatch( Character::isDigit ) && first.chars().allMatch( Character::isDigit )) && !temp.equals("") )){
            
            XMLInputFactory ifactory=XMLInputFactory.newFactory();
  XMLOutputFactory ofactory=XMLOutputFactory.newFactory();
  StreamSource source =new StreamSource(name+".xml");
  StreamResult result=new StreamResult(name+".xml");
            switch(operation){
                case "<" :
                    
  try {
   XMLEventReader in=ifactory.createXMLEventReader(source);
   XMLEventWriter out=ofactory.createXMLEventWriter(result);
   while(in.hasNext()){
    XMLEvent e=in.nextEvent();
    if(e.isStartElement() && ((StartElement)e).getName().getLocalPart().equalsIgnoreCase("name")){
     Attribute a=((StartElement)e).getAttributeByName(new QName(second));
     if(Integer.parseInt(a.getValue())>Integer.parseInt(first)){
         changes++;
      in.next();
      continue;
     }
    }
    out.add(e);
   }
   in.close();
   out.close();
  } catch (XMLStreamException e) {
   e.printStackTrace();
  }
                    break;
                    
                case ">" :
                    try {
   XMLEventReader in=ifactory.createXMLEventReader(source);
   XMLEventWriter out=ofactory.createXMLEventWriter(result);
   while(in.hasNext()){
    XMLEvent e=in.nextEvent();
    if(e.isStartElement() && ((StartElement)e).getName().getLocalPart().equalsIgnoreCase("name")){
     Attribute a=((StartElement)e).getAttributeByName(new QName(second));
     if(Integer.parseInt(a.getValue())<Integer.parseInt(first)){
         changes++;
      in.next();
      continue;
     }
    }
    out.add(e);
   }
   in.close();
   out.close();
  } catch (XMLStreamException e) {
   e.printStackTrace();
  }
                    break;
               
                case "=" :
                    try {
   XMLEventReader in=ifactory.createXMLEventReader(source);
   XMLEventWriter out=ofactory.createXMLEventWriter(result);
   while(in.hasNext()){
    XMLEvent e=in.nextEvent();
    if(e.isStartElement() && ((StartElement)e).getName().getLocalPart().equalsIgnoreCase("name")){
     Attribute a=((StartElement)e).getAttributeByName(new QName(second));
     if(Integer.parseInt(a.getValue())==Integer.parseInt(first)){
         changes++;
      in.next();
      continue;
     }
    }
    out.add(e);
   }
   in.close();
   out.close();
  } catch (XMLStreamException e) {
   e.printStackTrace();
  }
                    break;
                    
                default:
                    System.out.println("Invalid expression");
                    JOptionPane.showMessageDialog(null, "Invalid expression", "Error!", JOptionPane.INFORMATION_MESSAGE);
                    break;
            }
        }
        
        
        else if(((!temp.chars().allMatch( Character::isDigit ) || temp.equals("")) && !first.chars().allMatch( Character::isDigit ))){
           
            XMLInputFactory ifactory=XMLInputFactory.newFactory();
  XMLOutputFactory ofactory=XMLOutputFactory.newFactory();
  StreamSource source =new StreamSource(name+".xml");
  StreamResult result=new StreamResult(name+".xml");
  
  switch(operation){
      case "=":
 try {
   XMLEventReader in=ifactory.createXMLEventReader(source);
   XMLEventWriter out=ofactory.createXMLEventWriter(result);
   while(in.hasNext()){
    XMLEvent e=in.nextEvent();
    if(e.isStartElement() && ((StartElement)e).getName().getLocalPart().equalsIgnoreCase("name")){
     Attribute a=((StartElement)e).getAttributeByName(new QName(second));

     if(a.getValue().equals(first)){
         changes++;
      in.next();
      continue;
     }
    }
    out.add(e);
   }
   in.close();
   out.close();
  } catch (XMLStreamException e) {
   e.printStackTrace();
  }
break;
                
                    
                default:
                    System.out.println("Invalid expression");
                    JOptionPane.showMessageDialog(null, "Invalid expression", "Error!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
     }
         
         if(Validation.checkValidation(SQLInterpreter.getTableName(query))){
             System.out.println("Deletion Successful");
             JOptionPane.showMessageDialog(null, "Deletion Successful", "Successful!", JOptionPane.INFORMATION_MESSAGE);
             System.gc();
             return changes;
         }
        return 0;
    }
    
    public static Object[][] selectFromTable(String query) throws IOException{
        
        
        ArrayList<String> inData = SQLInterpreter.selectFromTable(query);
        String name = inData.get(0);
        
        if(!Validation.checkValidation(name)){
            System.out.println("Invalid file");
            JOptionPane.showMessageDialog(null, "Invalid file", "Error!", JOptionPane.INFORMATION_MESSAGE);
            Object[][] obj = new Object[0][0];
            return obj;
        }
        String first = inData.get(1);
        String second = inData.get(2);
        String operation= inData.get(3);
        
        DefaultValues.setDefaultValues(SQLInterpreter.selectFromTable(query).get(0));
        HashMap allData = DefaultValues.getDefaultValues();
        HashMap dataMap = (HashMap) allData.get(SQLInterpreter.selectFromTable(query).get(0));
        ArrayList<String> keys = new ArrayList();
        ArrayList<String> values = new ArrayList();
        HashMap <String, ArrayList<String>> multiMap= new HashMap();
     
        
        if(dataMap.containsKey(first)){
            String temp = (String) dataMap.get(first);
            if(((temp.chars().allMatch( Character::isDigit ) && second.chars().allMatch( Character::isDigit )) && !temp.equals("") )){
            
            XMLInputFactory ifactory=XMLInputFactory.newFactory();
  XMLOutputFactory ofactory=XMLOutputFactory.newFactory();
  StreamSource source =new StreamSource(name+".xml");
  StreamResult result=new StreamResult(name+".xml");
            switch(operation){
                case ">" :
                    
  try {
   XMLEventReader in=ifactory.createXMLEventReader(source);
   XMLEventWriter out=ofactory.createXMLEventWriter(result);
   while(in.hasNext()){
    XMLEvent e=in.nextEvent();
    if(e.isStartElement() && ((StartElement)e).getName().getLocalPart().equalsIgnoreCase("name")){
         Attribute b=((StartElement)e).getAttributeByName(new QName(first));
      Attribute [] a = new Attribute[inData.size()-4];
      for(int i=0;i<a.length;i++){
          a[i]=((StartElement)e).getAttributeByName(new QName(inData.get(i+4)));
         
          if(Integer.parseInt(b.getValue())>Integer.parseInt(second)){
              if(multiMap.containsKey(inData.get(i+4))){
                  ArrayList<String> tempo = multiMap.get(inData.get(i+4));
                  tempo.add(a[i].getValue());
                  multiMap.put(inData.get(i+4), tempo);
              }else{
                  ArrayList<String> tempo = new ArrayList();
                  tempo.add(a[i].getValue());
                  multiMap.put(inData.get(i+4), tempo);
              }
          }
      }
     
     
    }
    out.add(e);
   }
   in.close();
   out.close();
     
  } catch (XMLStreamException e) {
   e.printStackTrace();
  }
                    break;
                    
                case "<" :
                    try {
   XMLEventReader in=ifactory.createXMLEventReader(source);
   XMLEventWriter out=ofactory.createXMLEventWriter(result);
   while(in.hasNext()){
    XMLEvent e=in.nextEvent();
    if(e.isStartElement() && ((StartElement)e).getName().getLocalPart().equalsIgnoreCase("name")){
        Attribute b=((StartElement)e).getAttributeByName(new QName(first));
     Attribute [] a = new Attribute[inData.size()-4];
      for(int i=0;i<a.length;i++){
          a[i]=((StartElement)e).getAttributeByName(new QName(inData.get(i+4)));
          
          if(Integer.parseInt(b.getValue())<Integer.parseInt(second)){
              if(multiMap.containsKey(inData.get(i+4))){
                  ArrayList<String> tempo = multiMap.get(inData.get(i+4));
                  tempo.add(a[i].getValue());
                  multiMap.put(inData.get(i+4), tempo);
              }else{
                  ArrayList<String> tempo = new ArrayList();
                  tempo.add(a[i].getValue());
                  multiMap.put(inData.get(i+4), tempo);
              }
          }
      }
    }
    out.add(e);
   }
   in.close();
   out.close();
  } catch (XMLStreamException e) {
   e.printStackTrace();
  }
                  
                    break;
               
                case "=" :
                    try {
   XMLEventReader in=ifactory.createXMLEventReader(source);
   XMLEventWriter out=ofactory.createXMLEventWriter(result);
   while(in.hasNext()){
    XMLEvent e=in.nextEvent();
    if(e.isStartElement() && ((StartElement)e).getName().getLocalPart().equalsIgnoreCase("name")){
     Attribute b=((StartElement)e).getAttributeByName(new QName(first));
     Attribute [] a = new Attribute[inData.size()-4];
      for(int i=0;i<a.length;i++){
          a[i]=((StartElement)e).getAttributeByName(new QName(inData.get(i+4)));
          
          if(Integer.parseInt(b.getValue())==Integer.parseInt(second)){
              if(multiMap.containsKey(inData.get(i+4))){
                  ArrayList<String> tempo = multiMap.get(inData.get(i+4));
                  tempo.add(a[i].getValue());
                  multiMap.put(inData.get(i+4), tempo);
              }else{
                  ArrayList<String> tempo = new ArrayList();
                  tempo.add(a[i].getValue());
                  multiMap.put(inData.get(i+4), tempo);
              }
          }
      }
    }
    out.add(e);
   }
   in.close();
   out.close();
  } catch (XMLStreamException e) {
   e.printStackTrace();
  }
                    break;
                    
                default:
                    System.out.println("Invalid expression");
                    JOptionPane.showMessageDialog(null, "Invalid expression", "Error!", JOptionPane.INFORMATION_MESSAGE);
                    break;
            }
        }
        
        
        else if(dataMap.containsKey(first) && ((!temp.chars().allMatch( Character::isDigit ) || temp.equals("")) && !second.chars().allMatch( Character::isDigit ))){
        
            XMLInputFactory ifactory=XMLInputFactory.newFactory();
  XMLOutputFactory ofactory=XMLOutputFactory.newFactory();
  StreamSource source =new StreamSource(name+".xml");
  StreamResult result=new StreamResult(name+".xml");
  
  switch(operation){
      case "=":
 try {
   XMLEventReader in=ifactory.createXMLEventReader(source);
   XMLEventWriter out=ofactory.createXMLEventWriter(result);
   while(in.hasNext()){
    XMLEvent e=in.nextEvent();
    if(e.isStartElement() && ((StartElement)e).getName().getLocalPart().equalsIgnoreCase("name")){
       Attribute b=((StartElement)e).getAttributeByName(new QName(first));
     Attribute [] a = new Attribute[inData.size()-4];
      for(int i=0;i<a.length;i++){
          a[i]=((StartElement)e).getAttributeByName(new QName(inData.get(i+4)));
          
          if((b.getValue()).equals(second)){
              if(multiMap.containsKey(inData.get(i+4))){
                  ArrayList<String> tempo = multiMap.get(inData.get(i+4));
                  tempo.add(a[i].getValue());
                  multiMap.put(inData.get(i+4), tempo);
              }else{
                  ArrayList<String> tempo = new ArrayList();
                  tempo.add(a[i].getValue());
                  multiMap.put(inData.get(i+4), tempo);
              }
          }
      }
    }
    out.add(e);
   }
   
   in.close();
   out.close();
  } catch (XMLStreamException e) {
   e.printStackTrace();
  }
 
break;
                
                    
                default:
                    System.out.println("Invalid expression");
                    JOptionPane.showMessageDialog(null, "Invalid expression", "Error!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
     }else if(dataMap.containsKey(second)){
         String temp = (String) dataMap.get(second);
      
         if(((temp.chars().allMatch( Character::isDigit ) && first.chars().allMatch( Character::isDigit )) && !temp.equals("") )){
            
            XMLInputFactory ifactory=XMLInputFactory.newFactory();
  XMLOutputFactory ofactory=XMLOutputFactory.newFactory();
  StreamSource source =new StreamSource(name+".xml");
  StreamResult result=new StreamResult(name+".xml");
            switch(operation){
                case "<" :
                    
  try {
   XMLEventReader in=ifactory.createXMLEventReader(source);
   XMLEventWriter out=ofactory.createXMLEventWriter(result);
   while(in.hasNext()){
    XMLEvent e=in.nextEvent();
    if(e.isStartElement() && ((StartElement)e).getName().getLocalPart().equalsIgnoreCase("name")){
     Attribute b=((StartElement)e).getAttributeByName(new QName(second));
     Attribute [] a = new Attribute[inData.size()-4];
      for(int i=0;i<a.length;i++){
          a[i]=((StartElement)e).getAttributeByName(new QName(inData.get(i+4)));
          
          if(Integer.parseInt(b.getValue())>Integer.parseInt(first)){
             if(multiMap.containsKey(inData.get(i+4))){
                  ArrayList<String> tempo = multiMap.get(inData.get(i+4));
                  tempo.add(a[i].getValue());
                  multiMap.put(inData.get(i+4), tempo);
              }else{
                  ArrayList<String> tempo = new ArrayList();
                  tempo.add(a[i].getValue());
                  multiMap.put(inData.get(i+4), tempo);
              }
          }
      }
    }
    out.add(e);
   }
   in.close();
   out.close();
  } catch (XMLStreamException e) {
   e.printStackTrace();
  }
                    break;
                    
                case ">" :
                    try {
   XMLEventReader in=ifactory.createXMLEventReader(source);
   XMLEventWriter out=ofactory.createXMLEventWriter(result);
   while(in.hasNext()){
    XMLEvent e=in.nextEvent();
    if(e.isStartElement() && ((StartElement)e).getName().getLocalPart().equalsIgnoreCase("name")){
     Attribute b=((StartElement)e).getAttributeByName(new QName(second));
     Attribute [] a = new Attribute[inData.size()-4];
      for(int i=0;i<a.length;i++){
          a[i]=((StartElement)e).getAttributeByName(new QName(inData.get(i+4)));
          
          if(Integer.parseInt(b.getValue())<Integer.parseInt(first)){
             if(multiMap.containsKey(inData.get(i+4))){
                  ArrayList<String> tempo = multiMap.get(inData.get(i+4));
                  tempo.add(a[i].getValue());
                  multiMap.put(inData.get(i+4), tempo);
              }else{
                  ArrayList<String> tempo = new ArrayList();
                  tempo.add(a[i].getValue());
                  multiMap.put(inData.get(i+4), tempo);
              }
          }
      }
    }
    out.add(e);
   }
   in.close();
   out.close();
  } catch (XMLStreamException e) {
   e.printStackTrace();
  }
                    break;
               
                case "=" :
                    try {
   XMLEventReader in=ifactory.createXMLEventReader(source);
   XMLEventWriter out=ofactory.createXMLEventWriter(result);
   while(in.hasNext()){
    XMLEvent e=in.nextEvent();
    if(e.isStartElement() && ((StartElement)e).getName().getLocalPart().equalsIgnoreCase("name")){
     Attribute b=((StartElement)e).getAttributeByName(new QName(second));
     Attribute [] a = new Attribute[inData.size()-4];
      for(int i=0;i<a.length;i++){
          a[i]=((StartElement)e).getAttributeByName(new QName(inData.get(i+4)));
          
          if(Integer.parseInt(b.getValue())==Integer.parseInt(first)){
              if(multiMap.containsKey(inData.get(i+4))){
                  ArrayList<String> tempo = multiMap.get(inData.get(i+4));
                  tempo.add(a[i].getValue());
                  multiMap.put(inData.get(i+4), tempo);
              }else{
                  ArrayList<String> tempo = new ArrayList();
                  tempo.add(a[i].getValue());
                  multiMap.put(inData.get(i+4), tempo);
              }
          }
      }
    }
    out.add(e);
   }
   in.close();
   out.close();
  } catch (XMLStreamException e) {
   e.printStackTrace();
  }
                    break;
                    
                default:
                    System.out.println("Invalid expression");
                    JOptionPane.showMessageDialog(null, "Invalid expression", "Error!", JOptionPane.INFORMATION_MESSAGE);
                    break;
            }
        }
        
        
        else if(dataMap.containsKey(second) && ((!temp.chars().allMatch( Character::isDigit ) || temp.equals("")) && !first.chars().allMatch( Character::isDigit ))){
           
            XMLInputFactory ifactory=XMLInputFactory.newFactory();
  XMLOutputFactory ofactory=XMLOutputFactory.newFactory();
  StreamSource source =new StreamSource(name+".xml");
  StreamResult result=new StreamResult(name+".xml");
  
  switch(operation){
      case "=":
 try {
   XMLEventReader in=ifactory.createXMLEventReader(source);
   XMLEventWriter out=ofactory.createXMLEventWriter(result);
   while(in.hasNext()){
    XMLEvent e=in.nextEvent();
    if(e.isStartElement() && ((StartElement)e).getName().getLocalPart().equalsIgnoreCase("name")){
     Attribute b=((StartElement)e).getAttributeByName(new QName(second));
     Attribute [] a = new Attribute[inData.size()-4];
      for(int i=0;i<a.length;i++){
          a[i]=((StartElement)e).getAttributeByName(new QName(inData.get(i+4)));
          
          if((b.getValue()).equals(first)){
              if(multiMap.containsKey(inData.get(i+4))){
                  ArrayList<String> tempo = multiMap.get(inData.get(i+4));
                  tempo.add(a[i].getValue());
                  multiMap.put(inData.get(i+4), tempo);
              }else{
                  ArrayList<String> tempo = new ArrayList();
                  tempo.add(a[i].getValue());
                  multiMap.put(inData.get(i+4), tempo);
              }
          }
      }
    }
    out.add(e);
   }
   in.close();
   out.close();
  } catch (XMLStreamException e) {
   e.printStackTrace();
  }
break;
                
                    
                default:
                    System.out.println("Invalid expression");
                    JOptionPane.showMessageDialog(null, "Invalid expression", "Error!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
     
        }
        
        if(multiMap.isEmpty()){
            System.out.println("No matches found");
            
            Object[][] obj = new Object[0][0];
            return obj;
        }
        String [][]list = new String[multiMap.keySet().size()][multiMap.get(inData.get(4)).size()];
        int i=0;
        int j=0;
        
        for(String x : multiMap.keySet()){
            for(String z : multiMap.get(x)){
             
                if(((HashMap)DefaultValues.getDefaultValues().get(name)).get(x).equals("0")){
                    
                    list[i][j]= x+" = "+Integer.parseInt(z);
                    
                     j++;
                }else{
                    list[i][j]= x+" = "+z;
                   
                     j++;
                }
                     
            }
            j=0;
            i++;
        }
        
        if(Validation.checkValidation(inData.get(0))){
             System.out.println("Selection Successful");
             
         }
        System.gc();
        return list;
    }
    
    

}
