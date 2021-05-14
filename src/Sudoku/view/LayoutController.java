package Sudoku.view;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;



public class LayoutController {

    @FXML
    private GridPane sudokuTextContainer;

    private static TextField[][] sudokuCells = new TextField[9][9];

    @FXML
    private void initialize(){
        sudokuTextContainer.getStyleClass().add("cells-container");
        for (int rowIndex = 0; rowIndex < 9; rowIndex++) {
            for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
                sudokuCells[rowIndex][columnIndex] = new TextField();
                GridPane.setConstraints(sudokuCells[rowIndex][columnIndex],columnIndex,rowIndex);
                sudokuTextContainer.getChildren().add(sudokuCells[rowIndex][columnIndex]);

                sudokuCells[rowIndex][columnIndex].getStyleClass().add("cell");

                if(columnIndex==2||columnIndex==5){
                    sudokuCells[rowIndex][columnIndex].getStyleClass().add("border-right");
                }


            }


        }


    }
}
