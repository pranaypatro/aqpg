package sample;


import com.sun.javafx.scene.control.skin.VirtualFlow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvHandler {

    public static List<CsvRecords> readCsv(String path) {
        List<String> fileLines = new ArrayList<>();
        List<CsvRecords> csvRecorde = new ArrayList<>();
        try {

            fileLines = Files.readAllLines(Paths.get(path));
            for (String s : fileLines) {
                String[] commaSplitedValues = s.split(",");
                csvRecorde.add(new CsvRecords(commaSplitedValues[0], commaSplitedValues[1], commaSplitedValues[2] ));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvRecorde;
    }

    public static boolean insertCsvIntoTable(List<CsvRecords> csvRecorde ) {
        boolean status = false;
        for (CsvRecords csv : csvRecorde ) {
            DatabaseHandler databaseHandler = new DatabaseHandler();
            String question = csv.getQuestion();
            String marks = csv.getMarks();
            String chapterNo = csv.getChapterNo();
            status = databaseHandler.insertDataIntoTable( question, marks, chapterNo );
        }
        return status;
    }

}
