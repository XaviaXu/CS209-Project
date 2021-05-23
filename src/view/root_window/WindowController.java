package view.root_window;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import view.magic_square.magicSquareController;
import view.sudoku.LayoutController;

import java.io.IOException;


public class WindowController {
    @FXML
    public void MSClick() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(WindowController.class.getResource("../magic_square/magicSquare.fxml"));
        AnchorPane rootLayout = (AnchorPane) loader.load();

        Scene scene = new Scene(rootLayout);
        Stage stage = new Stage();
        magicSquareController mqc = loader.getController();
        mqc.setMS_stage(stage);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void SClick() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(WindowController.class.getResource("../sudoku/SudokuLayout.fxml"));
        AnchorPane rootLayout = (AnchorPane) loader.load();

        Scene scene = new Scene(rootLayout);
        Stage stage = new Stage();
        LayoutController sc = loader.getController();
        sc.set_stage(stage);
        stage.setScene(scene);
        stage.show();
    }
}
