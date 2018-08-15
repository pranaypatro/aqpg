package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class DatabaseHandler {

    /**
     * Constructor.
     */
    public DatabaseHandler() {
        conn = MySQLConnect.connectDB();
        try {
            statement = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method returns a List of all the Chapters, it works in the following ways :-
     * it takes the max number from the chapters column and the chapters List is populated with all the numbers starting
     * from 1 till the max number and it is returned to the calling method.
     * @return an ObservableList<String> the return type is this because it is required by the JFXCombobox in which the required type is this.
     * @throws SQLException
     */
    public ObservableList<String> getChapters() throws SQLException {
        ObservableList<String> chapters = FXCollections.observableArrayList();
        String sql = "SELECT MAX(chapter_no) FROM java";
        int max = 0;
        ResultSet rs = statement.executeQuery(sql);
        while(rs.next()) {
            String sub = rs.getString(1);
            max = Integer.parseInt(sub);
        }
        while(max != 0) {
            chapters.add(new String(String.valueOf(max)));
            max--;
        }
        return chapters;
    }

    /**
     *
     * @return an ObservableList<String> the return type is this because it is required by the JFXCombobox in which the required type is this.
     * @throws SQLException
     */
    public ObservableList<String> getSubjects() throws SQLException {
        ObservableList<String> subjects = FXCollections.observableArrayList();
        String sql = "SHOW TABLES";
        int max = 0;
        ResultSet rs = statement.executeQuery(sql);
        while(rs.next()) {
            String sub = rs.getString(1);
            subjects.add(sub);
        }

        return subjects;
    }


    /**
     *
     * @param question
     * @param marks
     * @param chapterNo
     * @return
     */
    public boolean insertDataIntoTable(String question, String marks, String chapterNo) {

        String sql = "INSERT INTO java (questions, priority, chapter_no, marks) values(?, 10, ?, ?)";
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, question);
            preparedStatement.setString(2, chapterNo);
            preparedStatement.setString(3, marks);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return false;
        }
        return true;
    }

//    variable declaration
    Connection conn;
    Statement statement;
    PreparedStatement preparedStatement;
}
