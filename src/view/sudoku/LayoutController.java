package view.sudoku;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import module.Condition;
import module.sk.SK;
import module.sk.SKWrapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class LayoutController {

    @FXML
    private GridPane sudokuTextContainer;

    @FXML
    private ChoiceBox<String> preset;

    @FXML
    private MenuBar mainMenu;

    @FXML
    private Label generation;

    @FXML
    private Label error;

    @FXML
    private Button ctrlButton;



    private static TextField[][] sudokuCells = new TextField[9][9];
    private boolean playerCtrl = true;
    private Stage stage;
    private Timeline timeline;

//    private Sudoku sudoku;
    private int[][] sudokuBoard;
    private int[][] cnst;//getcost:冲突值
    private SK sk = new SK(3);

    @FXML
    private void initialize(){
        sudokuTextContainer.getStyleClass().add("cells-container");
        sudokuBoard = sk.square;
        cnst = sk.cnst;

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
                    if(current.getText().length()>1||current.getText().length()==0){
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
                        current.setStyle("-fx-background-color: rgba(243,79,79,0.3);");
                    }

                    if(cnst[finalRowIndex][finalColumnIndex]!=0){
                        current.setEditable(false);
                    }else{
                        current.setEditable(current.isEditable()&&playerCtrl);
                    }
                });

                current.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(event.getButton()== MouseButton.SECONDARY&&cnst[finalRowIndex][finalColumnIndex]==0){
                            current.setText("");
                            sudokuBoard[finalRowIndex][finalColumnIndex] = 0;
                        }
                    }
                });
            }
        }

        preset.getItems().addAll("EASY","NORMAL","HARD","CLEAR");
        preset.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {
                    selectPreset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mainMenu.setOnMouseEntered(e->mainMenu.setOpacity(1));
        mainMenu.setOnMouseExited(event -> mainMenu.setOpacity(0));

        generation.setText("0");
        error.setText("0");

    }

    @FXML
    private void selectPreset() throws IOException {
        int selectedIndex = preset.getSelectionModel().selectedIndexProperty().intValue();
        String fileName = null;
        clearBoard();
        if(selectedIndex==0){
            //easy
            fileName = "input/easy.in";
        }else if(selectedIndex == 1){
            //normal
            fileName = "input/normal.in";
        }else if(selectedIndex ==2){
            //hard
            fileName = "input/hard.in";
        }else{
            return;
        }
        sk.readMs(fileName);
        setBoard();

    }
    private void setBoard(){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(sudokuBoard[i][j]==0){
                    sudokuCells[i][j].setText(cnst[i][j]==0?"":String.valueOf(cnst[i][j]));
                }else{
                    sudokuCells[i][j].setText(String.valueOf(sudokuBoard[i][j]));
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

    private void clearBoard(){
        sudokuBoard = new int[9][9];
        playerCtrl = true;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j <9; j++) {
                sudokuCells[i][j].setText("");
            }
        }


    }

    @FXML
    private void gameControl(){
        if(ctrlButton.getText().equals(Condition.START.toString())){
            ctrlButton.setText(Condition.PAUSED.toString());
            playerCtrl = false;

        }else if(ctrlButton.getText().equals(Condition.PAUSED.toString())){
            ctrlButton.setText(Condition.CONTINUE.toString());
            playerCtrl = true;
        }else{
            ctrlButton.setText(Condition.PAUSED.toString());
            playerCtrl = false;
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
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
//            setStudentFilePath(selected.getText(),file);
//            saveStudentDataToFile(data, file);
            saveSudokuData(file);
        }



    }

    @FXML
    private void handleOpen(){
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
//            mainApp.loadPersonDataFromFile(file);
            loadSudokuData(file);
        }
    }

    private void setFilePath(File file){

    }

    private void saveSudokuData(File file){
        try{
            JAXBContext context = JAXBContext.newInstance(SKWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);

            SKWrapper wrapper = new SKWrapper();
            wrapper.setN(sk.n);
            wrapper.setSquare(sk.square);
            wrapper.setCnst(sk.cnst);
            wrapper.setError(0);
            wrapper.setGeneration(0);

            m.marshal(wrapper,file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    private void loadSudokuData(File file){
        try {
            JAXBContext context = JAXBContext.newInstance(SKWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            SKWrapper wrapper = (SKWrapper)um.unmarshal(file);
            sk.cnst = wrapper.getCnst();
            sk.square = wrapper.getSquare();
            sudokuBoard = sk.square;
            cnst = sk.cnst;
            setBoard();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }


    public void set_stage(Stage stage) {
        this.stage = stage;
    }

    private boolean checkConstrain(int row, int column){
        if(sudokuBoard[row][column]==0){return true;}
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
