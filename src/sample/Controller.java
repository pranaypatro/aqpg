package sample;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javax.swing.*;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import javafx.stage.FileChooser;

public class Controller {

    public JFXComboBox jfxcbChapter;
    public Button btnSubmit;
    public TextField txtQuestion;
    public TextField txtMarks;
    public TextField txtChapterNumber;
    public JFXComboBox jfxcbSubject;
    public JFXButton btnRed;
    public JFXTextField jfxChapterWeight;
    public JFXTreeTableView jfxTable;
    public AnchorPane mainScreen;
    public AnchorPane pane2;
    public AnchorPane insertQuestionForm;
    public JFXButton btnQuestionInsertForm;
    public JFXButton btnGeneratorForm;
    public StackPane stackPane;
    public FileChooser fileChooser;
    public JFXCheckBox chkBoxAgree;

    //    public JFXColorPicker colorPicker;
    Main main;
    DatabaseHandler databaseHandler;
    CsvHandler csvHandler;
    HashMap<Integer, Integer> chapterMarks = new HashMap<>();
//    IconLabel iconLabel;
ObservableList<String> subjects = FXCollections.observableArrayList("java", "os", "mup", "amup", "python");
    ObservableList<String> chapters = FXCollections.observableArrayList();
    @FXML
    public void initialize(){
        main = new Main();
        databaseHandler = new DatabaseHandler();
        csvHandler = new CsvHandler();
        try {
            chapters = databaseHandler.getChapters();
            subjects = databaseHandler.getSubjects();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        jfxcbChapter.setItems(chapters);
        jfxcbSubject.setItems(subjects);
        mainScreen.setVisible(false);
        insertQuestionForm.setVisible(false);

//        btnSubmit.setOnAction(this::pressButton);

    }

    public String getQuestion() {
        return this.txtQuestion.getText();
    }

    public String getMarks() {
        return this.txtMarks.getText();
    }

    public String getChapterNo() {
        return this.txtChapterNumber.getText();
    }

    public void showQuestionInsertionForm(javafx.event.ActionEvent actionEvent) {
        mainScreen.setVisible(false);
        insertQuestionForm.setVisible(true);
    }

    public void insertQuestionToDB(javafx.event.ActionEvent actionEvent) {

        String question = getQuestion();
        String marks = getMarks();
        String chapterNo = getChapterNo();
        if (!databaseHandler.insertDataIntoTable(question, marks, chapterNo)) {
            showDialogBox("alert", "The Given Information has not been inserted Successfully (Operation Failed)" );
        } else {
            showDialogBox("alert", "The Given Information has been inserted Successfully" );
        }
    }


    public void showGeneratorForm(javafx.event.ActionEvent actionEvent) {
        insertQuestionForm.setVisible(false);
        mainScreen.setVisible(true);
    }

    public void csvPressed(javafx.event.ActionEvent actionEvent) {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Select CSV file");
        File file = fileChooser.showOpenDialog(main.getPrimarStage());
        String path = file.getAbsolutePath();
        List<CsvRecords> csvRecords = csvHandler.readCsv(path);

        if( CsvHandler.insertCsvIntoTable(csvRecords) ) {
            showDialogBox("Alert", "CSV Imported Successfully");
        } else {
            showDialogBox("Alert", "There was some Issue while importing");
        }
    }

    public void addChapterAndMarks(javafx.event.ActionEvent actionEvent) {
        int currentChapterWeight = Integer.parseInt(jfxChapterWeight.getText());
        int currentChapterNumber = Integer.parseInt(jfxcbChapter.getValue().toString());
        chapterMarks.put(currentChapterNumber , currentChapterWeight);
    }

    boolean agree = false;
    public void agreeChecker(javafx.event.ActionEvent actionEvent) {
        agree = !agree;
    }

    public void generatePaper(javafx.event.ActionEvent actionEvent) {
//        int paperWeight = 0;
//        while(paperWeight !=  100){

            if(agree) {
                OptimizingCode.start(chapterMarks);
                showDialogBox("Generator", "Paper Generation Successfull.");
            } else {
                showDialogBox("Generator", "Please Agree the Above Checkbox.");
            }




//        }
    }


    private void showDialogBox(String title, String body) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(title));
        content.setBody( new Text(body) );
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton btnOk = new JFXButton("OK");
        btnOk.setOnAction(event -> dialog.close());
        content.setActions(btnOk);
        dialog.show();
    }
}