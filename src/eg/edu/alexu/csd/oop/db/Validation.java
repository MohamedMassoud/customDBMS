/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eg.edu.alexu.csd.oop.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;



/**
 *
 * @author crap
 */
public class Validation {
    public static boolean checkValidation(String fileName) {
        try {
            FileInputStream op = new FileInputStream(fileName+".xml");
            XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(op);
            
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(fileName +".xsd"));
            
            Validator validator = schema.newValidator();
            validator.validate(new StAXSource(reader));
            op.close();
            reader.close();
            
            //no exception thrown, so valid
            System.out.println("Document is valid");
        } catch (XMLStreamException ex) {
            Logger.getLogger(Validation.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Validation.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (SAXException ex) {
            Logger.getLogger(Validation.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(Validation.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
}

