package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
//    static Parent root;
    Stage primarStage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        setPrimarStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("QuestionPaperGeneratorUI.fxml"));
//        Parent second = FXMLLoader.load(getClass().getResource("second.fxml"));
        primaryStage.setTitle("Question Paper Generator");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        primaryStage.show();

    }

    public void setPrimarStage(Stage primarStage) {
        this.primarStage = primarStage;
    }

    public Stage getPrimarStage() {
        return primarStage;
    }
    //    public static void setCustomColor(Color color) {
//;

//    }

    public static void main(String[] args) {
        launch(args);
    }
}
