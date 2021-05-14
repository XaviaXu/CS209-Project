package Sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SudokuApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Sudoku");

        initRootLayout();
        showSudoku();

    }

    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SudokuApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
//            scene.getStylesheets().add("/src/Sudoku/gameSceneStyle.css");
            primaryStage.setScene(scene);
//            RootLayoutController controller = loader.getController();
//            ctrl = controller;
//            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showSudoku(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SudokuApp.class.getResource("view/SudokuLayout.fxml"));
            AnchorPane sudoku = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(sudoku);
            
            
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
