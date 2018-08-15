package sample;

import com.jfoenix.controls.*;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellEditEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class TableHandler extends Application {
    private static final String COMPUTER_DEPARTMENT = "Computer Department";
    private static final String SALES_DEPARTMENT = "Sales Department";
    private static final String IT_DEPARTMENT = "IT Department";
    private static final String HR_DEPARTMENT = "HR Department";

    

    @Override
    public void start(Stage primaryStage) throws Exception {

        JFXTreeTableColumn<Weightage, String> chapterColumn = new JFXTreeTableColumn<>("Chapter No");
        chapterColumn.setPrefWidth(150);
        chapterColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Weightage, String> param) -> {
            if (chapterColumn.validateValue(param)) {
                return param.getValue().getValue().chapterNo;
            } else {
                return chapterColumn.getComputedValue(param);
            }
        });

        JFXTreeTableColumn<Weightage, String> marksColumn = new JFXTreeTableColumn<>("Marks");
        marksColumn.setPrefWidth(150);
        marksColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Weightage, String> param) -> {
            if (marksColumn.validateValue(param)) {
                return param.getValue().getValue().marks;
            } else {
                return marksColumn.getComputedValue(param);
            }
        });

        

        // yaha pe cell pe double click wala edit krne pe kaam krne wala code hai
        marksColumn.setCellFactory((TreeTableColumn<Weightage, String> param) -> new GenericEditableTreeTableCell<>(
            new TextFieldEditorBuilder()));
        marksColumn.setOnEditCommit((CellEditEvent<Weightage, String> t) -> t.getTreeTableView()
                                                                      .getTreeItem(t.getTreeTablePosition()
                                                                                    .getRow())
                                                                      .getValue().marks.set(t.getNewValue()));

        chapterColumn.setCellFactory((TreeTableColumn<Weightage, String> param) -> new GenericEditableTreeTableCell<>(
            new TextFieldEditorBuilder()));
        chapterColumn.setOnEditCommit((CellEditEvent<Weightage, String> t) -> t.getTreeTableView()
                                                                       .getTreeItem(t.getTreeTablePosition()
                                                                                     .getRow())
                                                                       .getValue().chapterNo.set(t.getNewValue()));


        // data
        ObservableList<Weightage> weightage = FXCollections.observableArrayList();
        weightage.add(new Weightage(COMPUTER_DEPARTMENT, "CD 1"));
        weightage.add(new Weightage(SALES_DEPARTMENT, "Employee 1"));
        weightage.add(new Weightage(SALES_DEPARTMENT, "Employee 2"));
        weightage.add(new Weightage(SALES_DEPARTMENT, "Employee 4"));
        weightage.add(new Weightage(SALES_DEPARTMENT, "Employee 5"));
        weightage.add(new Weightage(IT_DEPARTMENT, "ID 2"));
        weightage.add(new Weightage(HR_DEPARTMENT, "HR 1"));
        weightage.add(new Weightage(HR_DEPARTMENT, "HR 2"));

        final TreeItem<Weightage> root = new RecursiveTreeItem<>(weightage, RecursiveTreeObject::getChildren);

        JFXTreeTableView<Weightage> treeView = new JFXTreeTableView<>(root);
        treeView.setShowRoot(false);
        treeView.setEditable(true);
        treeView.getColumns().setAll(chapterColumn, marksColumn);

        FlowPane main = new FlowPane();
        main.setPadding(new Insets(10));
        main.getChildren().add(treeView);


        JFXButton groupButton = new JFXButton("Group");
        groupButton.setOnAction((action) -> new Thread(() -> treeView.group(marksColumn)).start());
        main.getChildren().add(groupButton);

        JFXButton unGroupButton = new JFXButton("unGroup");
        unGroupButton.setOnAction((action) -> treeView.unGroup(marksColumn));
        main.getChildren().add(unGroupButton);

        JFXTextField filterField = new JFXTextField();
        main.getChildren().add(filterField);

        Label size = new Label();

        filterField.textProperty().addListener((o, oldVal, newVal) -> {
            treeView.setPredicate(userProp -> {
                final Weightage user = userProp.getValue();
                return user.chapterNo.get().contains(newVal)
                    || user.marks.get().contains(newVal);
            });
        });

        size.textProperty()
            .bind(Bindings.createStringBinding(() -> String.valueOf(treeView.getCurrentItemsCount()),
                                               treeView.currentItemsCountProperty()));
        main.getChildren().add(size);

        Scene scene = new Scene(main, 475, 500);
        scene.getStylesheets().add(TableHandler.class.getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static final class Weightage extends RecursiveTreeObject<Weightage> {
        final StringProperty marks;
        final StringProperty chapterNo;

        Weightage(String chapterNo, String marks) {
            this.chapterNo = new SimpleStringProperty(chapterNo);
            this.marks = new SimpleStringProperty(marks);
        }
    }
}
