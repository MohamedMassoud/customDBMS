/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eg.edu.alexu.csd.oop.db;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/**
 *
 * @author crap
 */
public class Test {
    public static void test() throws XMLStreamException, FileNotFoundException, SQLException, SAXException, IOException, Exception{
     
    DBMS x = new DBMS();
      x.executeRetrievalQuery("SELECT firstName, age FROM test WHERE age < 25;");
    }
}
