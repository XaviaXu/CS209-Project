package view.sudoku;

import com.sun.javafx.collections.ObservableIntegerArrayImpl;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableIntegerArray;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

public class LayoutController {

    @FXML
    private GridPane sudokuTextContainer;

    @FXML
    private ChoiceBox<String> preset;

//    @FXML
//    private MenuBar mainMenu;

    @FXML
    private Label generation;

    @FXML
    private Label error;
//
//    @FXML
//    private Button ctrl;



    private static TextField[][] sudokuCells = new TextField[9][9];
    private boolean playerCtrl = false;
    private Stage stage;


    private int[][] sudokuBoard = new int[9][9];
    private boolean[][] status;

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
                        sudokuBoard[finalRowIndex][finalColumnIndex] = 0;
                    }
                    else if(!isInputValid(current.getText())){
                        sudokuBoard[finalRowIndex][finalColumnIndex] = 0;
                        current.setText("");
                    }
                    else{
                        sudokuBoard[finalRowIndex][finalColumnIndex] = Integer.parseInt(current.getText());
                    }

                    if(checkConstrain(finalRowIndex,finalColumnIndex)){
                        //正常
                        current.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3);");
                    }else{

                        current.setStyle("-fx-background-color: rgba(255,0,0,0.3);");
                    }


                });
                current.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(event.getButton()== MouseButton.SECONDARY){
                            current.setText("");
                            sudokuBoard[finalRowIndex][finalColumnIndex] = 0;
                        }
                    }
                });
            }
        }

        preset.getItems().addAll("EASY","NORMAL","HARD");
//        mainMenu.setOnMouseEntered(e->mainMenu.setOpacity(1));
//        mainMenu.setOnMouseExited(event -> mainMenu.setOpacity(0));

        generation.setText("0");
        error.setText("0");

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

    @FXML
    private void handleSaveAs(){

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(this.stage);

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".csv")) {
                file = new File(file.getPath() + ".csv");
            }
//            setStudentFilePath(selected.getText(),file);
//            saveStudentDataToFile(data, file);
        }



    }

    private void setFilePath(File file){

    }


    public void set_stage(Stage stage) {
        this.stage = stage;
    }

    private boolean checkConstrain(int row, int column){
        for (int i = 0; i < 9; i++) {
            if(sudokuBoard[row][i]==sudokuBoard[row][column]&&i!=column){
                return false;
            }
            if(sudokuBoard[i][column]==sudokuBoard[row][column]&&i!=row){
                return false;
            }
        }

        int rowDiff = row%3;
        int columnDiff = column%3;

        for (int i = 0; i <3 ; i++) {
            for (int j = 0; j < 3; j++) {
                int x = row-rowDiff+i;
                int y = column-columnDiff+j;
                if(sudokuBoard[x][y]==sudokuBoard[row][column]&&!(x==row&&y==column)){
                    return false;
                }
            }
        }
        
        return true;
    }



}
