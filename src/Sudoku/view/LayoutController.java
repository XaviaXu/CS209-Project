package Sudoku.view;

import com.sun.javafx.collections.ObservableIntegerArrayImpl;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableIntegerArray;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;



public class LayoutController {

    @FXML
    private GridPane sudokuTextContainer;

    @FXML
    private ChoiceBox<String> preset;

    private static TextField[][] sudokuCells = new TextField[9][9];
    private boolean playerCtrl = false;


    private int[][] sudokuBoard;
    private boolean[][] blocked;

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
                if(rowIndex == 3 ||rowIndex==6){
                    sudokuCells[rowIndex][columnIndex].getStyleClass().add("border-top");
                    if(columnIndex==2||columnIndex==5){
                        sudokuCells[rowIndex][columnIndex].getStyleClass().add("border-top-right");
                    }
                }

                TextField current = sudokuCells[rowIndex][columnIndex];
                int finalRowIndex = rowIndex;
                int finalColumnIndex = columnIndex;
                current.textProperty().addListener((observable, oldVal, newVal) -> {
                    if(current.getText().length()>1){
                        current.setText("");
                    }
                    else if(!isInputValid(current.getText())){
                        current.setText("");
                    }
                });

            }
        }

        preset.getItems().addAll("EASY","NORMAL","HARD");


    }

    @FXML
    private void selectPreset(){
        int selectedIndex = preset.getSelectionModel().selectedIndexProperty().intValue();
        int[][] inSudoku = new int[9][9];
        if(selectedIndex==0){
            //easy

        }else if(selectedIndex == 1){
            //normal

        }else if(selectedIndex ==2){
            //hard
        }

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if(inSudoku[row][column]!=0){
                    sudokuCells[row][column].setText(String.valueOf(inSudoku[row][column]));
                    sudokuCells[row][column].setEditable(false);
                }
                else{
                    sudokuCells[row][column].setText("");
                    sudokuCells[row][column].setEditable(true);
                }

            }
        }

    }

    private boolean isInputValid(String input) {
        int checkInput;
        try {
            checkInput = Integer.parseInt(input);
            if (checkInput <= 0 || checkInput > 9) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void showBoard(){
//        int[][] newBoard =


    }

    private void setUserEdit(){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudokuCells[i][j].setEditable(playerCtrl);
            }
        }
    }


}
