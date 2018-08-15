
package sample;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;



public class MySQLConnect {

    public static Connection connectDB(){
        try {

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/questionpapergeneratordb" , "root", "");
            //JOptionPane.showMessageDialog(null, "Connection Successful", "Connection", JOptionPane.INFORMATION_MESSAGE);
            return conn;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Connection Unsuccessful "+ e.getMessage(), "Connection" , JOptionPane.INFORMATION_MESSAGE);
            return null;
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "unasjdbh" + e.getMessage(), "Connection", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
