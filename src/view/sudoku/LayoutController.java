package view.sudoku;

import javafx.animation.Animation;
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
import module.ms.Algo1;
import module.sk.SK;
import module.sk.SKWrapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

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

    private int[][] cnst;//getcost:冲突值
    private SK sk = new SK(3);

    public static double eps = 1e-18;
    public static double delt = 0.995;
    public static int T = 500000;

    private int cost;
    private int min;
    private double t = T;

//    private boolean rst = false;
    private int[][] localMin;


    @FXML
    private void initialize(){
        sudokuTextContainer.getStyleClass().add("cells-container");
//        sudokuBoard = sk.square;
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
                        sk.square[finalRowIndex][finalColumnIndex] = 0;
                    }
                    else if(!isInputValid(current.getText())){
                        sk.square[finalRowIndex][finalColumnIndex] = 0;
                        current.setText("");
                    }
                    else{
                        sk.square[finalRowIndex][finalColumnIndex] = Integer.parseInt(current.getText());
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
                        current.getStyleClass().add("cell");
//                        current.getStyleClass().remove("cell:readonly");
                    }

                    error.setText(String.valueOf(sk.getCost()));
                });

                current.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(event.getClickCount()==2&&cnst[finalRowIndex][finalColumnIndex]==0){
                            current.setText("");
                            sk.square[finalRowIndex][finalColumnIndex] = 0;
                        }
                    }
                });
            }
        }

        preset.getItems().addAll("PRESET1","PRESET2","PRESET3","PRESET4","CLEAR");
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
            fileName = "input/easy3.in";
        }else if(selectedIndex == 1){
            //normal
            fileName = "input/normal.in";
        }else if(selectedIndex==2){
            fileName = "input/easy.in";
        }else if(selectedIndex ==3){
            //hard
            fileName = "input/test.in";
        }else{
            return;
        }
        sk.readMs(fileName);
        setBoard();

    }
    private void setBoard(){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(sk.square[i][j]==0){
                    sudokuCells[i][j].setText(cnst[i][j]==0?"":String.valueOf(cnst[i][j]));
                }else{
                    sudokuCells[i][j].setText(String.valueOf(sk.square[i][j]));
                }
                if(sk.cnst[i][j]==0){
                    sudokuCells[i][j].setStyle("-fx-text-fill: rgba(169, 224, 253, 0.9)");
                }else{
                    sudokuCells[i][j].setStyle("-fx-text-fill: rgba(231, 253, 169, 0.9)");
                }
                if(checkConstrain(i,j)){
                    //正常
                    sudokuCells[i][j].setStyle("-fx-background-color: rgba(255, 255, 255, 0.3);");
                }else{
                    sudokuCells[i][j].setStyle("-fx-background-color: rgba(243,79,79,0.3);");
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
        sk.square = new int[9][9];
        playerCtrl = true;
        ctrlButton.setText(Condition.START.toString());
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j <9; j++) {
                sudokuCells[i][j].setText("");
                sudokuCells[i][j].setEditable(true);
            }
        }
        generation.setText("0");
        error.setText("0");
    }

    @FXML
    private void gameControl(){
        if(ctrlButton.getText().equals(Condition.START.toString())){
            ctrlButton.setText(Condition.PAUSED.toString());
            playerCtrl = false;
            sk.fill();

            min = sk.getCost();
            int[][]mb = sk.square.clone();
            final int[] cnt = {0};

            timeline = new Timeline(new KeyFrame(Duration.seconds(0.0007), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(!(t>=eps&&cost>0)){
                        if(sk.getCost()!=0){
                            sk.square = mb.clone();
                            cost = min;
                            t = T;
                        }
                    }
                    sk.swapCells(t,eps);
                    t *= delt;
                    generation.setText(String.valueOf(++cnt[0]));
                    error.setText(String.valueOf(sk.getCost()));

                    if(cnt[0]%50==0){
                        setBoard();
                    }
                    if(sk.getCost()==0){
                        setBoard();
                        ctrlButton.setText("REPLAY");
                        timeline.stop();
                    }
                }
            }));
            timeline.setCycleCount(10000000);
            timeline.play();
        }else if(ctrlButton.getText().equals(Condition.PAUSED.toString())){
            ctrlButton.setText(Condition.CONTINUE.toString());
            playerCtrl = true;
            if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) timeline.pause();
        }else if(ctrlButton.getText().equals(Condition.CONTINUE.toString())){
            ctrlButton.setText(Condition.PAUSED.toString());
            playerCtrl = false;
            sk.fill();
            if (timeline != null && timeline.getStatus() == Animation.Status.PAUSED) timeline.play();
        }else{
            //reset
            clearBoard();
            ctrlButton.setText(Condition.START.toString());
            generation.setText("0");
            error.setText("0");
            setBoard();
//            playerCtrl = true;
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
//            getFilePath(file);
            saveSudokuData(file);
        }



    }

    @FXML
    private void handleSave(){
        File skFile = this.getFilePath();
        if(skFile!=null){
            saveSudokuData(skFile);
        }else{
            handleSaveAs();
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

    private File getFilePath(){
        Preferences prefs = Preferences.userNodeForPackage(LayoutController.class);
        String filePath = prefs.get("filePath",null);
        if(filePath != null){
            return new File(filePath);
        }else{
            return null;
        }
    }

    private void setFilePath(File file){
        Preferences pref = Preferences.userNodeForPackage(LayoutController.class);
        if(file!=null){
            pref.put("filePath",file.getPath());
        }else{
            pref.remove("filePath");
        }
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
            setFilePath(file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    private void loadSudokuData(File file){
        try {
            JAXBContext context = JAXBContext.newInstance(SKWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            SKWrapper wrapper = (SKWrapper)um.unmarshal(file);
            clearBoard();
            sk.cnst = wrapper.getCnst();
            sk.square = wrapper.getSquare();
//            sudokuBoard = sk.square;
            cnst = sk.cnst;

            generation.setText(String.valueOf(wrapper.getGeneration()));
            error.setText(String.valueOf(wrapper.getError()));

            setBoard();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }


    public void set_stage(Stage stage) {
        this.stage = stage;
    }

    private boolean checkConstrain(int row, int column){
        if(sk.square[row][column]==0||sk.cnst[row][column]!=0){return true;}
        for (int i = 0; i < 9; i++) {
            if(sk.square[row][i]==sk.square[row][column]&&i!=column){
                return false;
            }
            if(sk.square[i][column]==sk.square[row][column]&&i!=row){
                return false;
            }
        }
        if(!playerCtrl){return true;}
        int rowDiff = row%3;
        int columnDiff = column%3;

        for (int i = 0; i <3 ; i++) {
            for (int j = 0; j < 3; j++) {
                int x = row-rowDiff+i;
                int y = column-columnDiff+j;
                if(sk.square[x][y]==sk.square[row][column]&&!(x==row&&y==column)){
                    return false;
                }
            }
        }
        
        return true;
    }


}
