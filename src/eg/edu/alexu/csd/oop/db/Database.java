/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eg.edu.alexu.csd.oop.db;

/**
 *
 * @author crap
 */
public interface Database {
    public boolean executeStructureQuery(String query) throws java.sql.SQLException; 
    public Object[][] executeRetrievalQuery(String query) throws java.sql.SQLException;
    public int executeUpdateQuery(String query) throws java.sql.SQLException; 
}
